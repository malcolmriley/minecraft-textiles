package paragon.minecraft.wilytextiles.blocks;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import paragon.minecraft.wilytextiles.Textiles;

public class CushionBlock extends PaddedBlock {

	/* BlockState Properties */
	public static final EnumProperty<SlabType> TYPE = BlockStateProperties.SLAB_TYPE;

	/* Internal Fields */
	protected static final VoxelShape HALF_TOP = Block.box(0, 8, 0, 16, 16, 16);
	protected static final VoxelShape HALF_BOTTOM = Block.box(0, 0, 0, 16, 8, 16);
	protected static final VoxelShape HALF_NORTH = Block.box(0, 0, 0, 16, 16, 8);
	protected static final VoxelShape HALF_SOUTH = Block.box(0, 0, 8, 16, 16, 16);
	protected static final VoxelShape HALF_EAST = Block.box(8, 0, 0, 16, 16, 16);
	protected static final VoxelShape HALF_WEST = Block.box(0, 0, 0, 8, 16, 16);
	
	protected static final VoxelShape PARTIAL_HALF_TOP = Block.box(0, 8, 0, 16, 16 - SHAPE_OFFSET, 16);
	protected static final VoxelShape PARTIAL_HALF_BOTTOM = Block.box(0, 0, 0, 16, 8 - SHAPE_OFFSET, 16);
	protected static final VoxelShape PARTIAL_HALF_NORTH = Block.box(0, 0, 0, 16, 16 - SHAPE_OFFSET, 8);
	protected static final VoxelShape PARTIAL_HALF_SOUTH = Block.box(0, 0, 8, 16, 16 - SHAPE_OFFSET, 16);
	protected static final VoxelShape PARTIAL_HALF_EAST = Block.box(8, 0, 0, 16, 16 - SHAPE_OFFSET, 16);
	protected static final VoxelShape PARTIAL_HALF_WEST = Block.box(0, 0, 0, 8, 16 - SHAPE_OFFSET, 16);

	public CushionBlock(MaterialColor color) {
		this(PaddedBlock.createPropertiesFrom(color));
	}

	public CushionBlock(Properties properties) {
		super(properties);
	}

	/* Supertype Override Methods */

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos position, CollisionContext context) {
		return CushionBlock.getPartialShapeFrom(state);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos position, CollisionContext context) {
		return CushionBlock.getFullShapeFrom(state);
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos position, CollisionContext context) {
		return CushionBlock.getFullShapeFrom(state);
	}

	/*
	 * TODO: Find alternative
	@Override
	public boolean isTransparent(BlockState state) {
		return !SlabType.DOUBLE.equals(CushionBlock.getTypeFrom(state));
	}
	*/

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = context.getLevel().getBlockState(context.getClickedPos());
		Direction facing = context.getClickedFace();
		if (!context.getPlayer().isCrouching()) {
			// Swap facing if player isn't crouching, to allow placing cushions on near side of target block space
			facing = facing.getOpposite();
		}
		return state.is(this) ? state.setValue(CushionBlock.TYPE, SlabType.DOUBLE) : CushionBlock.applyDirectionTo(this.defaultBlockState(), facing);
	}

	@Override
	@SuppressWarnings("deprecation") // Return super.isReplaceable if not this, or if this and is SlabType.DOUBLE
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		return context.getItemInHand().getItem().equals(this.asItem()) && !SlabType.DOUBLE.equals(CushionBlock.getTypeFrom(state)) ? true : super.canBeReplaced(state, context);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(CushionBlock.TYPE);
	}

	@Override
	protected boolean shouldReduceFall() {
		return Textiles.CONFIG.cushionsReduceFall();
	}
	
	@Override
	protected float getFallDistanceModifier(Level world, BlockPos position, Entity fallen, float distance) {
		
		// Get values from original fallen-upon cushion block
		final BlockState originalState = world.getBlockState(position);
		final SlabType originalSlabType = CushionBlock.getTypeFrom(originalState);
		final Axis originalAxis = CushionBlock.getAxisFrom(originalState);
		
		// Get the initial count of cushion slabs that make up the original block
		int count = SlabType.DOUBLE.equals(originalSlabType) ? 2 : 1;
		
		// If there are no gaps below this, count blocks below (If the original cushion slab is a double slab, or if it is a bottom slab aligned to the Y axis)
		if (SlabType.DOUBLE.equals(originalSlabType) || (SlabType.BOTTOM.equals(originalSlabType) && Axis.Y.equals(originalAxis))) {
			count += countCushions(world, position.below());
		}
		
		return CushionBlock.calculateFallReduction(count);
	}

	/* Internal Methods */
	
	protected int countCushions(Level world, BlockPos originalPosition) {
		
		final int maxSeekDepth = Textiles.CONFIG.getCushionFallMaxSeek();
		int count = 0;
		if (maxSeekDepth > 0) {
			// Create mutable block Pos
			BlockPos.MutableBlockPos examinedPosition = new BlockPos.MutableBlockPos(originalPosition.getX(), originalPosition.getY(), originalPosition.getZ());
			
			// Yes it's a label. Feast your eyes on my legitimate use case and despair
			examine: for (int depth = 0; depth < maxSeekDepth; depth += 1) {
				BlockState examinedState = world.getBlockState(examinedPosition);
				// If the discovered Block isn't a cushion, stop counting.
				if (!examinedState.is(this)) {
					break;
				}
				// If the cushion block is Y, evaluate whether there is a half-block gap
				if (Axis.Y.equals(CushionBlock.getAxisFrom(examinedState))) {
					switch (CushionBlock.getTypeFrom(examinedState)) {
						// If there is a bottom-only cushion slab, there is a gap above it. Don't count the slab and stop counting.
						case BOTTOM:
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
				
				// Move the examined position down before continuing
				examinedPosition.move(Direction.DOWN);
			}
		}
		return count;
	}
	
	protected static float calculateFallReduction(int cushionCount) {
		return (float)Math.pow(1.0 - Textiles.CONFIG.getCushionFallReduction(), cushionCount);
	}
	
	protected static VoxelShape getFullShapeFrom(BlockState state) {
		return CushionBlock.selectShapeFrom(state, Shapes.block(), HALF_EAST, HALF_WEST, HALF_SOUTH, HALF_NORTH, HALF_TOP, HALF_BOTTOM);
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
		return original
			.setValue(CushionBlock.AXIS, facing.getAxis())
			.setValue(CushionBlock.TYPE, CushionBlock.typeFromFacing(facing));
	}
	
	protected static SlabType typeFromFacing(Direction facing) {
		return Direction.AxisDirection.POSITIVE.equals(facing.getAxisDirection()) ? SlabType.TOP : SlabType.BOTTOM;
	}
	
	protected static VoxelShape shapeFrom(SlabType type, VoxelShape positive, VoxelShape negative) {
		return SlabType.BOTTOM.equals(type) ? negative : positive;
	}

	protected static SlabType getTypeFrom(BlockState state) {
		return state.getValue(CushionBlock.TYPE);
	}

}
