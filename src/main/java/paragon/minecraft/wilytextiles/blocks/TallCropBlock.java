package paragon.minecraft.wilytextiles.blocks;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

/**
 * Implementation of a two-block tall crop {@link Block}.
 * <p>
 * This {@link Block} is essentially a tall {@link BushBlock} that can be fertilized.
 * Once the bottom block is fully grown, the top block will grow.
 * 
 * @author Malcolm Riley
 */
public abstract class TallCropBlock extends BushBlock implements IPlantable, BonemealableBlock {

	/* Blockstate Properties */
	public static final int MIN_AGE = 0;
	public static final int MAX_AGE = 5;
	public static final IntegerProperty AGE = BlockStateProperties.AGE_5;
	public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;

	private static final double HORIZONTAL_MIN = 0.25D;
	private static final double HORIZONTAL_MAX = 1.0 - HORIZONTAL_MIN;
	public static final VoxelShape SHAPE_HALF = Shapes.box(HORIZONTAL_MIN, 0.0, HORIZONTAL_MIN, HORIZONTAL_MAX, 0.5, HORIZONTAL_MAX);
	public static final VoxelShape SHAPE_WHOLE = Shapes.box(HORIZONTAL_MIN, 0.0, HORIZONTAL_MIN, HORIZONTAL_MAX, 1.0D, HORIZONTAL_MAX);

	public TallCropBlock(Properties builder) {
		super(builder.randomTicks().noCollission());
		this.registerDefaultState(this.createDefaultState());
	}

	/* Supertype Override Methods */

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos position, CollisionContext context) {
		int age = state.getValue(AGE).intValue();
		return age > 2 ? SHAPE_WHOLE : SHAPE_HALF;
	}

	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos position, Random RNG) {
		if (this.isBottomBlock(state)) {
			final int age = this.getAgeFrom(state);
			if (age >= (TallCropBlock.MAX_AGE - 1)) {
				BlockPos abovePosition = position.above();
				BlockState aboveState = world.getBlockState(abovePosition);
				if (aboveState.is(this)) {
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
	public boolean isRandomlyTicking(BlockState state) {
		return this.belowMaxAge(state);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos position) {
		return world.getBlockState(position.below()).getBlock().equals(this) || super.canSurvive(state, world, position);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(TallCropBlock.AGE, TallCropBlock.BOTTOM);
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return state.is(this) || super.mayPlaceOn(state, worldIn, pos);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos position, PathComputationType pathType) {
		return false;
	}

	@Override
	public BlockBehaviour.OffsetType getOffsetType() {
		return BlockBehaviour.OffsetType.XZ;
	}

	@Override
	@SuppressWarnings("deprecation") // Return super.getSeed if bottom block
	public long getSeed(BlockState state, BlockPos position) {
		return this.isBottomBlock(state) ? super.getSeed(state, position) : Mth.getSeed(position.getX(), position.below().getY(), position.getZ()) ;
	}

	/* IGrowable Compliance Methods */

	@Override
	public boolean isValidBonemealTarget(BlockGetter world, BlockPos position, BlockState state, boolean isClient) {
		final int age = this.getAgeFrom(state);
		if (this.isBottomBlock(state)) {
			if (age < (TallCropBlock.MAX_AGE - 1)) {
				return true;
			}
			BlockPos abovePosition = position.above();
			BlockState aboveState = world.getBlockState(abovePosition);
			if (aboveState.is(this)) {
				return this.belowMaxAge(aboveState) || this.belowMaxAge(state);
			}
			return aboveState.isAir();
		}
		return this.belowMaxAge(age);
	}

	@Override
	public void performBonemeal(ServerLevel world, Random RNG, BlockPos position, BlockState state) {
		BlockPos targetPosition = position;
		BlockState targetState = state;
		int max = TallCropBlock.MAX_AGE;
		if (this.isBottomBlock(state)) {
			final int age = this.getAgeFrom(state);
			max = TallCropBlock.MAX_AGE - 1;
			if (age >= (TallCropBlock.MAX_AGE - 1)) {
				BlockPos abovePosition = position.above();
				BlockState aboveState = world.getBlockState(abovePosition);
				if (aboveState.isAir()) {
					this.growInto(world, state, position, abovePosition, this.getBonemealGrowth(RNG));
					return;
				}
				if (aboveState.is(this)) {
					if (this.belowMaxAge(aboveState)) {
						targetPosition = abovePosition;
						targetState = aboveState;
					}
					max = TallCropBlock.MAX_AGE;
				}
			}
		}
		this.applyGrowth(world, targetState, targetPosition, this.getBonemealGrowth(RNG), max);
	}
	
	/* Abstract Methods */
	protected abstract boolean shouldGrow(ServerLevel world, BlockState state, BlockPos position, Random RNG);

	/* Internal Methods */
	
	/**
	 * Creates the default {@link BlockState} for this {@link Block}.
	 * 
	 * @return The default {@link BlockState} for this {@link Block}.
	 */
	protected BlockState createDefaultState() {
		return this.stateDefinition.any()
			.setValue(TallCropBlock.AGE, Integer.valueOf(0))
			.setValue(TallCropBlock.BOTTOM, Boolean.TRUE);
	}

	protected void tryApplyGrowth(ServerLevel world, BlockState state, BlockPos position, Random RNG) {
		if (this.belowMaxAge(state) && this.checkHooks(world, state, position, RNG)) {
			this.applyGrowth(world, state, position, 1);
			ForgeHooks.onCropsGrowPost(world, position, state);
		}
	}

	protected void applyGrowth(ServerLevel world, BlockState state, BlockPos position, int quantity) {
		this.applyGrowth(world, state, position, quantity, TallCropBlock.MAX_AGE);
	}

	protected void applyGrowth(ServerLevel world, BlockState state, BlockPos position, int quantity, int max) {
		int newAge = Mth.clamp(this.getAgeFrom(state) + quantity, TallCropBlock.MIN_AGE, max);
		world.setBlockAndUpdate(position, state.setValue(TallCropBlock.AGE, newAge));
	}

	protected boolean tryGrowInto(ServerLevel world, BlockState state, BlockPos position, BlockPos abovePosition, BlockState aboveState, Random rng) {
		if (aboveState.isAir() && this.checkHooks(world, aboveState, abovePosition, rng)) {
			this.growInto(world, state, position, abovePosition, TallCropBlock.MIN_AGE);
			ForgeHooks.onCropsGrowPost(world, position, state);
			return true;
		}
		return false;
	}

	/**
	 * Validates the pending growth tick with "external" validators - mod configuration and {@link ForgeHooks#onCropsGrowPre(World, BlockPos, BlockState, boolean)}.
	 * 
	 * @param world - The {@link ServerLevel} containing the crop
	 * @param state - The {@link BlockState} of the crop
	 * @param position The position of the crop
	 * @param RNG - A reference to a {@link Random} instance
	 * @return Whether all "external" validators permit the growth.
	 */
	protected final boolean checkHooks(ServerLevel world, BlockState state, BlockPos position, Random RNG) {
		return ForgeHooks.onCropsGrowPre(world, position, state, this.shouldGrow(world, state, position, RNG));
	}

	/**
	 * Performs the procedure of growing into a block, from the position specified to the position above. No checks are performed within this method.
	 * <p>
	 * Sets the {@link BlockState} at the provided "above" position to this, with the provided age, and sets the {@link #BOTTOM} property for both affected blocks.
	 * @param world - The {@link ServerLevel} containing the crop
	 * @param state - The {@link BlockState} of the crop
	 * @param position - The position of the crop
	 * @param abovePosition - The position of the block above the crop
	 * @param age - The age that the newly-added top {@link BlockState} should have. 
	 */
	protected void growInto(ServerLevel world, BlockState state, BlockPos position, BlockPos abovePosition, int age) {
		world.setBlockAndUpdate(abovePosition, this.defaultBlockState().setValue(TallCropBlock.AGE, Integer.valueOf(age)).setValue(TallCropBlock.BOTTOM, Boolean.FALSE));
		world.setBlockAndUpdate(position, state.setValue(TallCropBlock.BOTTOM, Boolean.TRUE));
	}

	/**
	 * Returns a random value between 1 and 3 inclusive, to mimick the bonemeal behavior of stadard crops.
	 * 
	 * @param RNG - A reference to a {@link Random} instance
	 * @return A value between 1 and 3, inclusive.
	 */
	protected int getBonemealGrowth(Random RNG) {
		return Mth.nextInt(RNG, 1, 3);
	}

	/**
	 * Simple check to determine whether the provided parameter is below the maximum permitted age value for this crop.
	 * 
	 * @param age - The value to check
	 * @return Whether the passed value is below {@value #MAX_AGE}.
	 */
	protected boolean belowMaxAge(int age) {
		return age < TallCropBlock.MAX_AGE;
	}
	
	/**
	 * Simple check to determine whether the value of the{@link #AGE} property on the provided {@link BlockState} is below the maximum permitted age value for this crop.
	 * 
	 * @param age - The {@link BlockState}
	 * @return Whether the value of the {@link #AGE} property on the provided {@link BlockState} is below {@value #MAX_AGE}.
	 */
	protected boolean belowMaxAge(BlockState state) {
		return this.belowMaxAge(this.getAgeFrom(state));
	}

	/**
	 * Extracts the value of the {@link #AGE} property from the provided {@link BlockState}.
	 * 
	 * @param state - The {@link BlockState} to extract from
	 * @return The value of the provided {@link BlockState}'s {@link #AGE} property.
	 */
	protected int getAgeFrom(BlockState state) {
		return state.getValue(TallCropBlock.AGE).intValue();
	}

	/**
	 * Returns whether the provided {@link BlockState} should be considered a "bottom block" of a crop based on the contained {@link #BOTTOM} property.
	 * 
	 * @param state - The {@link BlockState} to examine
	 * @return Whether the provided {@link BlockState} has a {@link #BOTTOM} value of {@code TRUE}.
	 */
	protected boolean isBottomBlock(BlockState state) {
		return state.getValue(TallCropBlock.BOTTOM).booleanValue();
	}

}
