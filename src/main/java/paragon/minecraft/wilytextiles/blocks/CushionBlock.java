package paragon.minecraft.wilytextiles.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class CushionBlock extends BlockPadding {

	/* BlockState Properties */
	public static final EnumProperty<SlabType> TYPE = BlockStateProperties.SLAB_TYPE;

	/* Internal Fields */
	protected static final VoxelShape HALF_TOP = Block.makeCuboidShape(0, 8, 0, 16, 16, 16);
	protected static final VoxelShape HALF_BOTTOM = Block.makeCuboidShape(0, 0, 0, 16, 8, 16);
	protected static final VoxelShape HALF_NORTH = Block.makeCuboidShape(0, 0, 0, 8, 16, 16);
	protected static final VoxelShape HALF_SOUTH = Block.makeCuboidShape(8, 0, 0, 16, 16, 16);
	protected static final VoxelShape HALF_EAST = Block.makeCuboidShape(0, 0, 0, 16, 16, 8);
	protected static final VoxelShape HALF_WEST = Block.makeCuboidShape(0, 0, 8, 16, 16, 16);

	public CushionBlock(MaterialColor color) {
		this(BlockPadding.createPropertiesFrom(color));
	}

	public CushionBlock(Properties properties) {
		super(properties);
	}

	/* Supertype Override Methods */

	@Override
	@SuppressWarnings("deprecation") // Return super.isTransparent() if type is not double
	public boolean isTransparent(BlockState state) {
		return CushionBlock.getTypeFrom(state).equals(SlabType.DOUBLE) ? false : super.isTransparent(state);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		SlabType type = CushionBlock.getTypeFrom(state);
		if (SlabType.DOUBLE.equals(type)) {
			return VoxelShapes.fullCube();
		}
		switch(CushionBlock.getAxisFrom(state)) {
			case X: return CushionBlock.shapeFrom(type, HALF_NORTH, HALF_SOUTH);
			case Z: return CushionBlock.shapeFrom(type, HALF_EAST, HALF_WEST);
			default: return CushionBlock.shapeFrom(type, HALF_TOP, HALF_BOTTOM);	
		}
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState state = context.getWorld().getBlockState(context.getPos());
		Direction facing = context.getPlacementHorizontalFacing();
		return state.isIn(this) ? state.with(CushionBlock.TYPE, SlabType.DOUBLE) : CushionBlock.applyDirectionTo(state, facing);
	}

	@Override
	@SuppressWarnings("deprecation") // Return super.isReplaceable if not this, or if this and is SlabType.DOUBLE
	public boolean isReplaceable(BlockState state, BlockItemUseContext context) {
		return context.getItem().getItem().equals(this.asItem()) && !SlabType.DOUBLE.equals(CushionBlock.getTypeFrom(state)) ? true : super.isReplaceable(state, context);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(CushionBlock.TYPE);
	}

	/* Internal Methods */
	
	protected static BlockState applyDirectionTo(BlockState original, Direction facing) {
		return original.with(CushionBlock.AXIS, facing.getAxis()).with(CushionBlock.TYPE, CushionBlock.typeFromFacing(facing));
	}
	
	protected static SlabType typeFromFacing(Direction facing) {
		return Direction.AxisDirection.POSITIVE.equals(facing.getAxisDirection()) ? SlabType.TOP : SlabType.BOTTOM;
	}
	
	protected static VoxelShape shapeFrom(SlabType type, VoxelShape positive, VoxelShape negative) {
		return SlabType.TOP.equals(type) ? positive : negative;
	}

	protected static SlabType getTypeFrom(BlockState state) {
		return state.get(CushionBlock.TYPE);
	}

}
