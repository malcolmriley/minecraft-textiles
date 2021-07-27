package paragon.minecraft.wilytextiles.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class AxialMultipleBlock extends Block {
	
	/* Blockstate Fields */
	public static final int MIN_COUNT = 1;
	public static final int MAX_COUNT = 9;
	public static final IntegerProperty COUNT = IntegerProperty.create("count", MIN_COUNT, MAX_COUNT);
	public static final EnumProperty<Direction.Axis> FACING = EnumProperty.create("facing", Direction.Axis.class, Direction.Axis.X, Direction.Axis.Z);
	
	public static final VoxelShape SHAPE_1 = VoxelShapes.create(0.0, 0.0, 0.0, 1.0, 1.0 / 3.0, 1.0);
	public static final VoxelShape SHAPE_2 = VoxelShapes.create(0.0, 0.0, 0.0, 1.0, 2.0 / 3.0, 1.0);
	public static final VoxelShape SHAPE_3 = VoxelShapes.fullCube();

	public AxialMultipleBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(AxialMultipleBlock.FACING, Direction.Axis.X).with(AxialMultipleBlock.COUNT, AxialMultipleBlock.MIN_COUNT));
	}
	
	/* Supertype Override Methods */
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState state = context.getWorld().getBlockState(context.getPos());
		// If the target block is this, add one to the "count" state parameter, else return super
		return state.isIn(this) ? this.incrementCount(state) : super.getStateForPlacement(context);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos position, ISelectionContext context) {
		int count = state.get(AxialMultipleBlock.COUNT);
		switch (count) {
			case 9:
			case 8:
			case 7:
				return AxialMultipleBlock.SHAPE_3;
			case 6:
			case 5:
			case 4:
				return AxialMultipleBlock.SHAPE_2;
		}
		return AxialMultipleBlock.SHAPE_1;
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
	
	protected BlockState incrementCount(BlockState original) {
		return original.with(AxialMultipleBlock.COUNT, Math.min(AxialMultipleBlock.MAX_COUNT, this.getCountFrom(original) + 1));
	}
	
	protected int getCountFrom(BlockState state) {
		return state.get(AxialMultipleBlock.COUNT).intValue();
	}

}
