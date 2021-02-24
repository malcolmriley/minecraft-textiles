package paragon.minecraft.wilytextiles.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

/**
 * Represents a block that "ages" while waterlogged.
 * <p>
 * Up to six of the same "block item" can occupy the same space.
 *
 * @author Malcolm Riley
 */
public class SoakableBlock extends Block implements IWaterLoggable {

	/* Blockstate Fields */
	public static final int MAX_COUNT = 6;
	public static final int MAX_AGE = 2;
	public static final IntegerProperty COUNT = IntegerProperty.create("count", 1, MAX_COUNT);
	public static final IntegerProperty AGE = BlockStateProperties.AGE_0_2;
	public static final VoxelShape SHAPE_1 = VoxelShapes.create(0.0, 0.0, 0.0, 1.0, 1.0 / 3.0, 1.0);
	public static final VoxelShape SHAPE_2 = VoxelShapes.create(0.0, 0.0, 0.0, 1.0, 2.0 / 3.0, 1.0);
	public static final VoxelShape SHAPE_3 = VoxelShapes.fullCube();

	public SoakableBlock(Properties properties) {
		super(properties.tickRandomly());
		this.setDefaultState(this.stateContainer.getBaseState().with(BlockStateProperties.WATERLOGGED, false).with(SoakableBlock.AGE, Integer.valueOf(0)).with(SoakableBlock.COUNT, Integer.valueOf(1)));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos position, ISelectionContext context) {
		int count = state.get(SoakableBlock.COUNT);
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
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		return state.getFluidState().isEmpty();
	}

	@Override
	@SuppressWarnings("deprecation") // Return super.isReplaceable as default if not this
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
		return useContext.getItem().getItem().equals(this.asItem()) && (state.get(SoakableBlock.COUNT) < SoakableBlock.MAX_COUNT) ? true : super.isReplaceable(state, useContext);
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState state = context.getWorld().getBlockState(context.getPos());
		// If the target block is this, add one to the "count" state parameter
		if (state.isIn(this)) {
			int currentCount = state.get(SoakableBlock.COUNT);
			return state.with(SoakableBlock.COUNT, Integer.valueOf(Math.min(SoakableBlock.MAX_COUNT, currentCount + 1)));
		}
		// Otherwise, update fluid state
		// TODO: Other forge fluids? How does that work?
		Fluid fluid = context.getWorld().getFluidState(context.getPos()).getFluid();
		return super.getStateForPlacement(context).with(BlockStateProperties.WATERLOGGED, Boolean.valueOf((fluid == Fluids.WATER) || (fluid == Fluids.FLOWING_WATER)));
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos position) {
		BlockPos below = position.down();
		BlockState stateBelow = world.getBlockState(below);
		return stateBelow.isSolidSide(world, below, Direction.UP) || stateBelow.isIn(this);
	}

	@Override
	@SuppressWarnings("deprecation") // Returning super.updatePostPlacement() as default if not waterlogged
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos position, BlockPos facingPosition) {
		if (state.get(BlockStateProperties.WATERLOGGED).booleanValue()) {
			world.getPendingFluidTicks().scheduleTick(position, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return super.updatePostPlacement(state, facing, facingState, world, position, facingPosition);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos position, Random RNG) {
		if (state.get(BlockStateProperties.WATERLOGGED).booleanValue()) {
			int age = state.get(SoakableBlock.AGE).intValue();
			if (age < 2) {
				age += 1;
				world.setBlockState(position, state.with(SoakableBlock.AGE, age));
			}
		}
	}

	@Override
	public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(BlockStateProperties.WATERLOGGED, SoakableBlock.AGE, SoakableBlock.COUNT);
	}

	@Override
	@SuppressWarnings("deprecation") // Returning super.getFluidState() as default if not waterlogged.
	public FluidState getFluidState(BlockState state) {
		return state.get(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

}
