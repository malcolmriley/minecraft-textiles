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
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class AxialMultipleBlock extends Block {
	
	/* Blockstate Fields */
	public static final int MIN_COUNT = 1;
	public static final int MAX_COUNT = 9;
	public static final IntegerProperty COUNT = IntegerProperty.create("count", MIN_COUNT, MAX_COUNT);
	public static final EnumProperty<Direction.Axis> FACING = EnumProperty.create("facing", Direction.Axis.class, Direction.Axis.X, Direction.Axis.Z);

	private static final int SHAPE_INSET = 2;
	public static final VoxelShape SHAPE_X_1 = Block.makeCuboidShape(0 + SHAPE_INSET, 0, 0, 16 - SHAPE_INSET, 16.0 / 3.0, 16);
	public static final VoxelShape SHAPE_X_2 = Block.makeCuboidShape(0 + SHAPE_INSET, 0, 0, 16 - SHAPE_INSET, 32.0 / 3.0, 16);
	public static final VoxelShape SHAPE_X_3 = Block.makeCuboidShape(0 + SHAPE_INSET, 0, 0, 16 - SHAPE_INSET, 16, 16);
	
	public static final VoxelShape SHAPE_Z_1 = Block.makeCuboidShape(0, 0, 0 + SHAPE_INSET, 16, 16.0 / 3.0, 16 - SHAPE_INSET);
	public static final VoxelShape SHAPE_Z_2 = Block.makeCuboidShape(0, 0, 0 + SHAPE_INSET, 16, 32.0 / 3.0, 16 - SHAPE_INSET);
	public static final VoxelShape SHAPE_Z_3 = Block.makeCuboidShape(0, 0, 0 + SHAPE_INSET, 16, 16, 16 - SHAPE_INSET);

	public AxialMultipleBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(AxialMultipleBlock.FACING, Direction.Axis.X).with(AxialMultipleBlock.COUNT, AxialMultipleBlock.MIN_COUNT));
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
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos position, ISelectionContext context) {
		int count = AxialMultipleBlock.getCountFrom(state);
		Axis axis = AxialMultipleBlock.getAxisFrom(state);
		switch (count) {
			case 9:
			case 8:
			case 7:
				return axis.equals(Axis.X) ? SHAPE_X_3 : SHAPE_Z_3;
			case 6:
			case 5:
			case 4:
				return axis.equals(Axis.X) ? SHAPE_X_2 : SHAPE_Z_2;
		}
		return axis.equals(Axis.X) ? SHAPE_X_1 : SHAPE_Z_1;
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos position) {
		final BlockPos below = position.down();
		return world.getBlockState(below).isSolidSide(world, below, Direction.UP);
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
	
	protected static BlockState withAxis(BlockState original, Axis facing) {
		return facing.isHorizontal() ? original.with(AxialMultipleBlock.FACING, facing) : original;
	}
	
	protected static BlockState incrementCount(BlockState original) {
		return original.with(AxialMultipleBlock.COUNT, Math.min(AxialMultipleBlock.MAX_COUNT, AxialMultipleBlock.getCountFrom(original) + 1));
	}
	
	protected static int getCountFrom(BlockState state) {
		return state.get(AxialMultipleBlock.COUNT).intValue();
	}
	
	protected static Axis getAxisFrom(BlockState state) {
		return state.get(AxialMultipleBlock.FACING);
	}

}
