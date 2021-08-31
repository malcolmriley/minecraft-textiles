package paragon.minecraft.wilytextiles.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class CushionBlock extends BlockPadding {

	/* BlockState Properties */
	public static final EnumProperty<SlabType> TYPE = BlockStateProperties.SLAB_TYPE;

	/* Internal Fields */
	protected static final VoxelShape HALF_TOP = Block.makeCuboidShape(0, 8, 0, 16, 16, 16);
	protected static final VoxelShape HALF_BOTTOM = Block.makeCuboidShape(0, 0, 0, 16, 8, 16);
	protected static final VoxelShape HALF_NORTH = Block.makeCuboidShape(0, 0, 0, 16, 16, 8);
	protected static final VoxelShape HALF_SOUTH = Block.makeCuboidShape(0, 0, 8, 16, 16, 16);
	protected static final VoxelShape HALF_EAST = Block.makeCuboidShape(8, 0, 0, 16, 16, 16);
	protected static final VoxelShape HALF_WEST = Block.makeCuboidShape(0, 0, 0, 8, 16, 16);
	
	protected static final VoxelShape PARTIAL_HALF_TOP = Block.makeCuboidShape(0, 8, 0, 16, 16 - SHAPE_OFFSET, 16);
	protected static final VoxelShape PARTIAL_HALF_BOTTOM = Block.makeCuboidShape(0, 0, 0, 16, 8 - SHAPE_OFFSET, 16);
	protected static final VoxelShape PARTIAL_HALF_NORTH = Block.makeCuboidShape(0, 0, 0, 16, 16 - SHAPE_OFFSET, 8);
	protected static final VoxelShape PARTIAL_HALF_SOUTH = Block.makeCuboidShape(0, 0, 8, 16, 16 - SHAPE_OFFSET, 16);
	protected static final VoxelShape PARTIAL_HALF_EAST = Block.makeCuboidShape(8, 0, 0, 16, 16 - SHAPE_OFFSET, 16);
	protected static final VoxelShape PARTIAL_HALF_WEST = Block.makeCuboidShape(0, 0, 0, 8, 16 - SHAPE_OFFSET, 16);

	public CushionBlock(MaterialColor color) {
		this(BlockPadding.createPropertiesFrom(color));
	}

	public CushionBlock(Properties properties) {
		super(properties);
	}

	/* Supertype Override Methods */

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos position, ISelectionContext context) {
		return CushionBlock.getPartialShapeFrom(state);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos position) {
		return CushionBlock.getFullShapeFrom(state);
	}

	@Override
	public VoxelShape getRayTraceShape(BlockState state, IBlockReader reader, BlockPos position, ISelectionContext context) {
		return CushionBlock.getFullShapeFrom(state);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return CushionBlock.getFullShapeFrom(state);
	}

	@Override
	public boolean isTransparent(BlockState state) {
		return !SlabType.DOUBLE.equals(CushionBlock.getTypeFrom(state));
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState state = context.getWorld().getBlockState(context.getPos());
		Direction facing = context.getFace();
		if (!context.getPlayer().isCrouching()) {
			// Swap facing if player isn't crouching, to allow placing cushions on near side of target block space
			facing = facing.getOpposite();
		}
		return state.isIn(this) ? state.with(CushionBlock.TYPE, SlabType.DOUBLE) : CushionBlock.applyDirectionTo(this.getDefaultState(), facing);
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
	
	@Override
	protected float getFallReduction(World world, BlockPos position, Entity fallen, float distance) {
		
		// Get values from original fallen-upon cushion block
		final BlockState originalState = world.getBlockState(position);
		final SlabType originalSlabType = CushionBlock.getTypeFrom(originalState);
		final Axis originalAxis = CushionBlock.getAxisFrom(originalState);
		
		// Get the initial count of cushion slabs that make up the original block
		int count = CushionBlock.countSlabsIn(originalSlabType);
		
		// If there are no gaps below this, count blocks below (If the original cushion slab is a double slab, or if it is a bottom slab aligned to the Y axis)
		if (SlabType.DOUBLE.equals(originalSlabType) || (SlabType.BOTTOM.equals(originalSlabType) && Axis.Y.equals(originalAxis))) {
			count += countCushions(world, position.down());
		}
		
		return CushionBlock.calculateFallReduction(count);
	}

	/* Internal Methods */
	
	protected int countCushions(World world, BlockPos originalPosition) {
		
		final int maxSeekDepth = 4;
		int count = 0;
		BlockPos.Mutable examinedPosition = new BlockPos.Mutable(originalPosition.getX(), originalPosition.getY(), originalPosition.getZ());
		
		examine: for (int depth = 1; depth < maxSeekDepth; depth += 1) {
			BlockState examinedState = world.getBlockState(examinedPosition);
			// If the discovered Block isn't a cushion, stop counting.
			if (!examinedState.isIn(this)) {
				break;
			}
			// If the cushion block is Y, evaluate whether there is a half-block gap
			if (Axis.Y.equals(CushionBlock.getAxisFrom(examinedState))) {
				switch (CushionBlock.getTypeFrom(examinedState)) {
					case BOTTOM:
						// If there is a bottom-only cushion slab, there is a gap above it. Don't count the slab and stop counting.
						break examine;
					case TOP:
						// If there is a top-only cushion slab, there is a gap below it. Count the slab, but stop counting.
						count += 1;
						break examine;
					default:
						// The slab type is double. Count both and continue counting.
						count += 2;
				}
			}
			// If the cushion block is not Y-aligned, there may be a horizontal gap.
			else {
				// Only continue if the slab type is double (there is no horizontal gap)
				if (!SlabType.DOUBLE.equals(CushionBlock.getTypeFrom(examinedState))) {
					count += 1;
					break;
				}
				count += 2;
			}
			
			examinedPosition.move(Direction.DOWN);
		}
		return count;
	}
	
	protected static int countSlabsIn(SlabType type) {
		return SlabType.DOUBLE.equals(type) ? 2 : 1;
	}
	
	protected static float calculateFallReduction(int cushionCount) {
		final double REDUCTION_PER_CUSHION = 0.2F;
		return (float)Math.pow(1.0 - REDUCTION_PER_CUSHION, cushionCount);
	}
	
	protected static VoxelShape getFullShapeFrom(BlockState state) {
		return CushionBlock.selectShapeFrom(state, VoxelShapes.fullCube(), HALF_EAST, HALF_WEST, HALF_SOUTH, HALF_NORTH, HALF_TOP, HALF_BOTTOM);
	}
	
	protected static VoxelShape getPartialShapeFrom(BlockState state) {
		return CushionBlock.selectShapeFrom(state, PARTIAL_FULL, PARTIAL_HALF_EAST, PARTIAL_HALF_WEST, PARTIAL_HALF_SOUTH, PARTIAL_HALF_NORTH, PARTIAL_HALF_TOP, PARTIAL_HALF_BOTTOM);
	}
	
	protected static VoxelShape selectShapeFrom(BlockState state, VoxelShape full, VoxelShape east, VoxelShape west, VoxelShape south, VoxelShape north, VoxelShape top, VoxelShape bottom) {
		SlabType type = CushionBlock.getTypeFrom(state);
		if (SlabType.DOUBLE.equals(type)) {
			return full;
		}
		switch(CushionBlock.getAxisFrom(state)) {
			case X: return CushionBlock.shapeFrom(type, east, west);
			case Z: return CushionBlock.shapeFrom(type, south, north);
			default: return CushionBlock.shapeFrom(type, top, bottom);	
		}
	}
	
	protected static BlockState applyDirectionTo(BlockState original, Direction facing) {
		return original.with(CushionBlock.AXIS, facing.getAxis()).with(CushionBlock.TYPE, CushionBlock.typeFromFacing(facing));
	}
	
	protected static SlabType typeFromFacing(Direction facing) {
		return Direction.AxisDirection.POSITIVE.equals(facing.getAxisDirection()) ? SlabType.TOP : SlabType.BOTTOM;
	}
	
	protected static VoxelShape shapeFrom(SlabType type, VoxelShape positive, VoxelShape negative) {
		return SlabType.BOTTOM.equals(type) ? negative : positive;
	}

	protected static SlabType getTypeFrom(BlockState state) {
		return state.get(CushionBlock.TYPE);
	}

}
