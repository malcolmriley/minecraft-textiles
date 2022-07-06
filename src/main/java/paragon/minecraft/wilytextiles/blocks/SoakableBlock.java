package paragon.minecraft.wilytextiles.blocks;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Represents a block that "ages" while waterlogged.
 * <p>
 * Up to six of the same "block item" can occupy the same space.
 *
 * @author Malcolm Riley
 */
public class SoakableBlock extends Block implements SimpleWaterloggedBlock {

	/* Blockstate Fields */
	public static final int MAX_COUNT = 6;
	public static final IntegerProperty COUNT = IntegerProperty.create("count", 1, MAX_COUNT);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final VoxelShape SHAPE_1 = Block.box(0.0, 0.0, 0.0, 1.0, 1.0 / 3.0, 1.0);
	public static final VoxelShape SHAPE_2 = Block.box(0.0, 0.0, 0.0, 1.0, 2.0 / 3.0, 1.0);
	public static final VoxelShape SHAPE_3 = Shapes.block();

	public SoakableBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.createDefaultState());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos position, CollisionContext context) {
		int count = state.getValue(SoakableBlock.COUNT);
		switch (count) {
			case 6:
			case 5:
				return SHAPE_3;
			case 4:
			case 3:
				return SHAPE_2;
		}
		return SHAPE_1;
	}

	@Override
	@SuppressWarnings("deprecation") // Return super.isReplaceable as default if not this
	public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
		return useContext.getItemInHand().getItem().equals(this.asItem()) && (state.getValue(SoakableBlock.COUNT).intValue() < SoakableBlock.MAX_COUNT) ? true : super.canBeReplaced(state, useContext);
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = context.getLevel().getBlockState(context.getClickedPos());
		// If the target block is this, add one to the "count" state parameter
		if (state.is(this)) {
			int currentCount = state.getValue(SoakableBlock.COUNT);
			return state.setValue(SoakableBlock.COUNT, Integer.valueOf(Math.min(SoakableBlock.MAX_COUNT, currentCount + 1)));
		}
		// Otherwise, update fluid state
		return super.getStateForPlacement(context);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos position) {
		BlockPos below = position.below();
		BlockState stateBelow = world.getBlockState(below);
		return stateBelow.isFaceSturdy(world, below, Direction.UP) || stateBelow.is(this);
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.WATERLOGGED, SoakableBlock.COUNT);
	}

	@Override
	@SuppressWarnings("deprecation") // Returning super.getFluidState() as default if not waterlogged.
	public FluidState getFluidState(BlockState state) {
		return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}
	
	/* Internal Methods */

	/**
	 * Creates the default {@link BlockState} for this {@link Block}.
	 * 
	 * @return The default {@link BlockState} for this {@link Block}.
	 */
	protected BlockState createDefaultState() {
		return this.stateDefinition.any()
			.setValue(SoakableBlock.WATERLOGGED, false)
			.setValue(SoakableBlock.COUNT, Integer.valueOf(1));
	}
	
	protected static boolean getWaterlogStateFrom(BlockState state) {
		return state.getValue(SoakableBlock.WATERLOGGED).booleanValue();
	}
	
	protected static int getCountFrom(BlockState state) {
		return state.getValue(SoakableBlock.COUNT).intValue();
	}

}
