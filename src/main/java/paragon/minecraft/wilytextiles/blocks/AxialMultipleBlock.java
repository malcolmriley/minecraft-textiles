package paragon.minecraft.wilytextiles.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

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
		this.setDefaultState(this.createDefaultState());
	}
	
	/* Supertype Override Methods */
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState state = context.getWorld().getBlockState(context.getPos());
		Axis axis = context.getPlacementHorizontalFacing().getAxis();
		return state.isIn(this) ? AxialMultipleBlock.incrementCount(state) : AxialMultipleBlock.withAxis(this.getDefaultState(), axis);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos position) {
		final BlockPos below = position.down();
		final BlockState belowState = world.getBlockState(below);
		return belowState.isIn(this) || belowState.isSolidSide(world, below, Direction.UP);
	}


	@Override
	@SuppressWarnings("deprecation") // Return super.isReplaceable as default if not this
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
		return useContext.getItem().getItem().equals(this.asItem()) && (state.get(AxialMultipleBlock.COUNT).intValue() < AxialMultipleBlock.MAX_COUNT) ? true : super.isReplaceable(state, useContext);
	}

	@Override
	public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(AxialMultipleBlock.FACING, AxialMultipleBlock.COUNT);
	}
	
	/* Internal Methods */
	
	/**
	 * Creates the default {@link BlockState} for this {@link Block}.
	 * 
	 * @return The default {@link BlockState} for this {@link Block}.
	 */
	protected BlockState createDefaultState() {
		return this.stateContainer.getBaseState()
			.with(AxialMultipleBlock.FACING, Direction.Axis.X)
			.with(AxialMultipleBlock.COUNT, AxialMultipleBlock.MIN_COUNT);
	}
	
	/**
	 * Applies the {@link #FACING} property specified by the provided {@link Axis} parameter to the provided {@link BlockState}.
	 * 
	 * @param original - The original {@link BlockState}
	 * @param facing - The {@link Axis} facing to apply
	 * @return The {@link BlockState}, with the applied axial facing.
	 */
	protected static BlockState withAxis(BlockState original, Axis facing) {
		return facing.isHorizontal() ? original.with(AxialMultipleBlock.FACING, facing) : original;
	}
	
	/**
	 * Safely increments the {@link #COUNT} property of the provided {@link BlockState} by one.
	 * 
	 * @param original - The original {@link BlockState}
	 * @return The {@link BlockState} after incrementing the {@link #COUNT} property.
	 */
	protected static BlockState incrementCount(BlockState original) {
		return original.with(AxialMultipleBlock.COUNT, Math.min(AxialMultipleBlock.MAX_COUNT, AxialMultipleBlock.getCountFrom(original) + 1));
	}
	
	/**
	 * Extracts the value of the {@link #COUNT} property from the provided {@link BlockState}.
	 * 
	 * @param state - The {@link BlockState} to extract from
	 * @return The value of the {@link #COUNT} property.
	 */
	protected static int getCountFrom(BlockState state) {
		return state.get(AxialMultipleBlock.COUNT).intValue();
	}
	
	/**
	 * Extracts the value of the {@link #FACING} property from the provided {@link BlockState}.
	 * 
	 * @param state - The {@link BlockState} to extract from
	 * @return The value of the {@link #FACING} property.
	 */
	protected static Axis getAxisFrom(BlockState state) {
		return state.get(AxialMultipleBlock.FACING);
	}

}
