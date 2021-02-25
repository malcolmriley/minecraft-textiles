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

	public TallCrop(Properties builder) {
		super(builder.tickRandomly());
		this.setDefaultState(this.stateContainer.getBaseState().with(TallCrop.AGE, Integer.valueOf(0)).with(TallCrop.BOTTOM, Boolean.FALSE));
	}

	/* Supertype Override Methods */

	@Override
	protected boolean isValidGround(BlockState state, IBlockReader world, BlockPos position) {
		return super.isValidGround(state, world, position) || state.isIn(this);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos position, Random random) {
		if (world.isAreaLoaded(position, 1)) {
			int age = this.getAgeFrom(state);
			if (this.canGrow(age) && ForgeHooks.onCropsGrowPre(world, position, state, Textiles.CONFIG.shouldFlaxGrow(state, world, position, random))) {
				world.setBlockState(position, state.with(TallCrop.AGE, Integer.valueOf(age + 1)));
				ForgeHooks.onCropsGrowPost(world, position, state);
			}
			else if (!world.getBlockState(position.down()).isIn(this)) {
				BlockPos abovePosition = position.up();
				BlockState aboveState = world.getBlockState(abovePosition);
				if (this.canGrowInto(world, aboveState, abovePosition) && ForgeHooks.onCropsGrowPre(world, abovePosition, aboveState, Textiles.CONFIG.shouldFlaxGrow(state, world, position, random))) {
					this.growInto(world, position, state, abovePosition, TallCrop.MIN_AGE);
					ForgeHooks.onCropsGrowPost(world, abovePosition, aboveState);
				}
			}
		}
	}

	@Override
	public boolean ticksRandomly(BlockState state) {
		return this.canGrow(state);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(TallCrop.AGE, TallCrop.BOTTOM);
	}

	/* IGrowable Compliance Methods */

	@Override
	public boolean canGrow(IBlockReader world, BlockPos position, BlockState state, boolean isClient) {
		int age = this.getAgeFrom(state);
		return this.canGrow(age) || this.canGrow(world.getBlockState(position.up()));
	}

	@Override
	public boolean canUseBonemeal(World world, Random RNG, BlockPos position, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random RNG, BlockPos position, BlockState state) {
		int age = this.getAgeFrom(state);
		if (this.canGrow(age)) {
			this.applyBonemealGrowth(world, RNG, position, state);
		}
		else {
			BlockPos abovePosition = position.up();
			BlockState aboveState = world.getBlockState(abovePosition);
			if (aboveState.getBlock().equals(this)) {
				this.applyBonemealGrowth(world, RNG, abovePosition, aboveState);
			}
			else if (this.canGrowInto(world, aboveState, abovePosition)) {
				this.growInto(world, position, state, abovePosition, this.getBonemealGrowth(RNG));
			}
		}
	}

	@Override
	public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
		return facing.equals(Direction.UP) && plantable.getPlant(world, pos.offset(facing)).getBlock().equals(this);
	}

	/* Internal Methods */

	@SuppressWarnings("deprecation") // Forge has marked isAir(IBlockReader, BlockPos) deprecated, but this method is also the way they recommend one uses their API. For now. See https://github.com/MinecraftForge/MinecraftForge/pull/7657.
	protected boolean canGrowInto(ServerWorld world, BlockState state, BlockPos position) {
		return state.isAir(world, position);
	}

	protected void growInto(ServerWorld world, BlockPos position, BlockState state, BlockPos abovePosition, int age) {
		world.setBlockState(abovePosition, this.getDefaultState().with(TallCrop.AGE, Integer.valueOf(age)));
		world.setBlockState(position, state.with(TallCrop.BOTTOM, Boolean.TRUE));
	}

	protected int getBonemealGrowth(Random RNG) {
		return MathHelper.nextInt(RNG, 1, 3);
	}

	protected void applyBonemealGrowth(ServerWorld world, Random RNG, BlockPos position, BlockState state) {
		int age = this.getAgeFrom(state);
		world.setBlockState(position, state.with(TallCrop.AGE, MathHelper.clamp(age + this.getBonemealGrowth(RNG), TallCrop.MIN_AGE, TallCrop.MAX_AGE)));
	}

	protected int getAgeFrom(BlockState state) {
		return state.get(TallCrop.AGE).intValue();
	}

	protected boolean canGrow(int age) {
		return age < TallCrop.MAX_AGE;
	}

	protected boolean canGrow(BlockState state) {
		return this.canGrow(this.getAgeFrom(state));
	}

}
