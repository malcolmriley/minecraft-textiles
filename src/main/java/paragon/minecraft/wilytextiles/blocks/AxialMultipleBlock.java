package paragon.minecraft.wilytextiles.blocks;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/**
 * An axis-aligned block that appears to permit multiple objects within the same block space.
 * 
 * @author Malcolm Riley
 */
public class AxialMultipleBlock extends Block {
	
	/* Blockstate Fields */
	public static final int MIN_COUNT = 1;
	public static final int MAX_COUNT = 9;
	public static final IntegerProperty COUNT = IntegerProperty.create("count", MIN_COUNT, MAX_COUNT);
	public static final EnumProperty<Direction.Axis> FACING = EnumProperty.create("facing", Direction.Axis.class, Direction.Axis.X, Direction.Axis.Z);

	public AxialMultipleBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.createDefaultState());
	}
	
	/* Supertype Override Methods */
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = context.getLevel().getBlockState(context.getClickedPos());
		Axis axis = context.getHorizontalDirection().getAxis();
		return state.is(this) ? AxialMultipleBlock.incrementCount(state) : AxialMultipleBlock.withAxis(this.defaultBlockState(), axis);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos position) {
		final BlockPos belowPosition = position.below();
		final BlockState belowState = world.getBlockState(belowPosition);
		return this.isBlockBelowValid(world, position, state, belowPosition, belowState);
	}

	@Override
	@SuppressWarnings("deprecation") // Return super.isReplaceable as default if not this
	public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
		return useContext.getItemInHand().getItem().equals(this.asItem()) && (state.getValue(AxialMultipleBlock.COUNT).intValue() < AxialMultipleBlock.MAX_COUNT) ? true : super.canBeReplaced(state, useContext);
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(AxialMultipleBlock.FACING, AxialMultipleBlock.COUNT);
	}
	
	/* Internal Methods */
	
	/**
	 * Returns whether the {@link BlockState} beneath this one is suitable for keeping this {@link AxialMultipleBlock}.
	 * <p>
	 * This method is used by {@link #isValidPosition(BlockState, IWorldReader, BlockPos)} to determine whether this {@link AxialMultipleBlock} can be placed
	 * and whether it can stay after a neighbor has changed.
	 * 
	 * @param world - An {@link LevelReader} to use for state examination
	 * @param position - The {@link BlockPos} of this {@link AxialMultipleBlock}
	 * @param state - The {@link BlockState} of this {@link AxialMultipleBlock}
	 * @param belowPosition - The {@link BlockPos} beneath this {@link AxialMultipleBlock}
	 * @param belowState - The {@link BlockState} beneath this {@link AxialMultipleBlock}
	 * @return Whether the {@link BlockState} beneath this one can support this {@link AxialMultipleBlock}.
	 */
	protected boolean isBlockBelowValid(LevelReader world, BlockPos position, BlockState state, BlockPos belowPosition, BlockState belowState) {
		return Block.canSupportCenter(world, belowPosition, Direction.UP);
	}
	
	/**
	 * Creates the default {@link BlockState} for this {@link Block}.
	 * 
	 * @return The default {@link BlockState} for this {@link Block}.
	 */
	protected BlockState createDefaultState() {
		return this.stateDefinition.any()
			.setValue(AxialMultipleBlock.FACING, Direction.Axis.X)
			.setValue(AxialMultipleBlock.COUNT, AxialMultipleBlock.MIN_COUNT);
	}
	
	/**
	 * Applies the {@link #FACING} property specified by the provided {@link Axis} parameter to the provided {@link BlockState}.
	 * 
	 * @param original - The original {@link BlockState}
	 * @param facing - The {@link Axis} facing to apply
	 * @return The {@link BlockState}, with the applied axial facing.
	 */
	protected static BlockState withAxis(BlockState original, Axis facing) {
		return facing.isHorizontal() ? original.setValue(AxialMultipleBlock.FACING, facing) : original;
	}
	
	/**
	 * Safely increments the {@link #COUNT} property of the provided {@link BlockState} by one.
	 * 
	 * @param original - The original {@link BlockState}
	 * @return The {@link BlockState} after incrementing the {@link #COUNT} property.
	 */
	protected static BlockState incrementCount(BlockState original) {
		return original.setValue(AxialMultipleBlock.COUNT, Math.min(AxialMultipleBlock.MAX_COUNT, AxialMultipleBlock.getCountFrom(original) + 1));
	}
	
	/**
	 * Extracts the value of the {@link #COUNT} property from the provided {@link BlockState}.
	 * 
	 * @param state - The {@link BlockState} to extract from
	 * @return The value of the {@link #COUNT} property.
	 */
	protected static int getCountFrom(BlockState state) {
		return state.getValue(AxialMultipleBlock.COUNT).intValue();
	}
	
	/**
	 * Extracts the value of the {@link #FACING} property from the provided {@link BlockState}.
	 * 
	 * @param state - The {@link BlockState} to extract from
	 * @return The value of the {@link #FACING} property.
	 */
	protected static Axis getAxisFrom(BlockState state) {
		return state.getValue(AxialMultipleBlock.FACING);
	}

}
