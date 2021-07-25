package paragon.minecraft.wilytextiles.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import paragon.minecraft.wilytextiles.Textiles;

public class TallCrop extends BushBlock implements IGrowable {

	/* Blockstate Properties */
	public static final int MIN_AGE = 0;
	public static final int MAX_AGE = 5;
	public static final IntegerProperty AGE = BlockStateProperties.AGE_0_5;
	public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;

	private static final double HORIZONTAL_MIN = 0.25D;
	private static final double HORIZONTAL_MAX = 1.0 - HORIZONTAL_MIN;
	public static final VoxelShape SHAPE_HALF = VoxelShapes.create(HORIZONTAL_MIN, 0.0, HORIZONTAL_MIN, HORIZONTAL_MAX, 0.5, HORIZONTAL_MAX);
	public static final VoxelShape SHAPE_WHOLE = VoxelShapes.create(HORIZONTAL_MIN, 0.0, HORIZONTAL_MIN, HORIZONTAL_MAX, 1.0D, HORIZONTAL_MAX);

	public TallCrop(Properties builder) {
		super(builder.tickRandomly().doesNotBlockMovement());
		this.setDefaultState(this.stateContainer.getBaseState().with(TallCrop.AGE, Integer.valueOf(0)).with(TallCrop.BOTTOM, Boolean.TRUE));
	}

	/* Supertype Override Methods */

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos position, ISelectionContext context) {
		int age = state.get(AGE).intValue();
		return age > 2 ? SHAPE_WHOLE : SHAPE_HALF;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos position, Random RNG) {
		if (this.isBottomBlock(state)) {
			final int age = this.getAgeFrom(state);
			if (age >= (TallCrop.MAX_AGE - 1)) {
				BlockPos abovePosition = position.up();
				BlockState aboveState = world.getBlockState(abovePosition);
				if (aboveState.isIn(this)) {
					if (this.getAgeFrom(aboveState) < age) {
						return;
					}
				}
				else {
					this.tryGrowInto(world, state, position, abovePosition, aboveState, RNG);
					return;
				}
			}
		}
		this.tryApplyGrowth(world, state, position, RNG);
	}

	@Override
	public boolean ticksRandomly(BlockState state) {
		return this.belowMaxAge(state);
	}

	@Override
	public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos position, Direction direction, IPlantable plantable) {
		return (direction.equals(Direction.UP) && plantable.getPlant(world, position).isIn(this)) || super.canSustainPlant(state, world, position, direction, plantable);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(TallCrop.AGE, TallCrop.BOTTOM);
	}

	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return state.isIn(this) || super.isValidGround(state, worldIn, pos);
	}

	/* IGrowable Compliance Methods */

	@Override
	public boolean canGrow(IBlockReader world, BlockPos position, BlockState state, boolean isClient) {
		final int age = this.getAgeFrom(state);
		if (!Textiles.CONFIG.isLightAdequateForFlax(world, position)) {
			return false;
		}
		if (this.isBottomBlock(state)) {
			if (age < (TallCrop.MAX_AGE - 1)) {
				return true;
			}
			BlockPos abovePosition = position.up();
			BlockState aboveState = world.getBlockState(abovePosition);
			if (aboveState.isIn(this)) {
				return this.belowMaxAge(aboveState) || this.belowMaxAge(state);
			}
			return this.canGrowInto(world, aboveState, abovePosition);
		}
		return this.belowMaxAge(age);
	}

	@Override
	public boolean canUseBonemeal(World world, Random RNG, BlockPos position, BlockState state) {
		return this.canGrow(world, position, state, world.isRemote());
	}

	@Override
	public void grow(ServerWorld world, Random RNG, BlockPos position, BlockState state) {
		BlockPos targetPosition = position;
		BlockState targetState = state;
		int max = TallCrop.MAX_AGE;
		if (this.isBottomBlock(state)) {
			final int age = this.getAgeFrom(state);
			max = TallCrop.MAX_AGE - 1;
			if (age >= (TallCrop.MAX_AGE - 1)) {
				BlockPos abovePosition = position.up();
				BlockState aboveState = world.getBlockState(abovePosition);
				if (this.canGrowInto(world, aboveState, abovePosition)) {
					this.growInto(world, state, position, abovePosition, this.getBonemealGrowth(RNG));
					return;
				}
				if (aboveState.isIn(this)) {
					if (this.belowMaxAge(aboveState)) {
						targetPosition = abovePosition;
						targetState = aboveState;
					}
					max = TallCrop.MAX_AGE;
				}
			}
		}
		this.applyGrowth(world, targetState, targetPosition, this.getBonemealGrowth(RNG), max);
	}

	/* Internal Methods */

	protected void tryApplyGrowth(ServerWorld world, BlockState state, BlockPos position, Random RNG) {
		if (this.belowMaxAge(state) && this.checkHooks(world, state, position, RNG)) {
			this.applyGrowth(world, state, position, 1);
			ForgeHooks.onCropsGrowPost(world, position, state);
		}
	}

	protected void applyGrowth(ServerWorld world, BlockState state, BlockPos position, int quantity) {
		this.applyGrowth(world, state, position, quantity, TallCrop.MAX_AGE);
	}

	protected void applyGrowth(ServerWorld world, BlockState state, BlockPos position, int quantity, int max) {
		int newAge = MathHelper.clamp(this.getAgeFrom(state) + quantity, TallCrop.MIN_AGE, max);
		world.setBlockState(position, state.with(TallCrop.AGE, newAge));
	}

	@SuppressWarnings("deprecation") // Forge has marked isAir(IBlockReader, BlockPos) deprecated, but this method is also the way they recommend one uses their API. For now. See https://github.com/MinecraftForge/MinecraftForge/pull/7657.
	protected boolean canGrowInto(IBlockReader world, BlockState state, BlockPos position) {
		return state.isAir(world, position);
	}

	protected boolean tryGrowInto(ServerWorld world, BlockState state, BlockPos position, BlockPos abovePosition, BlockState aboveState, Random rng) {
		if (this.canGrowInto(world, aboveState, abovePosition) && this.checkHooks(world, aboveState, abovePosition, rng)) {
			this.growInto(world, state, position, abovePosition, TallCrop.MIN_AGE);
			ForgeHooks.onCropsGrowPost(world, position, state);
			return true;
		}
		return false;
	}

	protected boolean checkHooks(ServerWorld world, BlockState state, BlockPos position, Random RNG) {
		return ForgeHooks.onCropsGrowPre(world, position, state, Textiles.CONFIG.shouldFlaxGrow(state, world, position, RNG));
	}

	protected void growInto(ServerWorld world, BlockState state, BlockPos position, BlockPos abovePosition, int age) {
		world.setBlockState(abovePosition, this.getDefaultState().with(TallCrop.AGE, Integer.valueOf(age)).with(TallCrop.BOTTOM, Boolean.FALSE));
		world.setBlockState(position, state.with(TallCrop.BOTTOM, Boolean.TRUE));
	}

	protected int getBonemealGrowth(Random RNG) {
		return MathHelper.nextInt(RNG, 1, 3);
	}

	protected int getAgeFrom(BlockState state) {
		return state.get(TallCrop.AGE).intValue();
	}

	protected boolean belowMaxAge(int age) {
		return age < TallCrop.MAX_AGE;
	}

	protected boolean isBottomBlock(BlockState state) {
		return state.get(TallCrop.BOTTOM).booleanValue();
	}

	protected boolean belowMaxAge(BlockState state) {
		return this.belowMaxAge(this.getAgeFrom(state));
	}

}
