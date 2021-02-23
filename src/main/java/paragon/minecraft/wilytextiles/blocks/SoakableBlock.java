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
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

/**
 * Represents a block that "ages" while waterlogged.
 *
 * @author Malcolm Riley
 */
public class SoakableBlock extends Block implements IWaterLoggable {

	public SoakableBlock(Properties properties) {
		super(properties.tickRandomly());
		this.setDefaultState(this.stateContainer.getBaseState().with(BlockStateProperties.WATERLOGGED, false).with(BlockStateProperties.AGE_0_2, 0));
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos position) {
		BlockPos below = position.down();
		BlockState stateBelow = world.getBlockState(below);
		return stateBelow.isSolidSide(world, below, Direction.UP);
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
			int age = state.get(BlockStateProperties.AGE_0_2).intValue();
			if (age < 2) {
				age += 1;
				world.setBlockState(position, state.with(BlockStateProperties.AGE_0_2, age));
			}
		}
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Fluid fluid = context.getWorld().getFluidState(context.getPos()).getFluid();
		return super.getStateForPlacement(context).with(BlockStateProperties.WATERLOGGED, Boolean.valueOf((fluid == Fluids.WATER) || (fluid == Fluids.FLOWING_WATER)));
	}

	@Override
	public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(BlockStateProperties.WATERLOGGED, BlockStateProperties.AGE_0_2);
	}

	@Override
	@SuppressWarnings("deprecation") // Returning super.getFluidState() as default if not waterlogged.
	public FluidState getFluidState(BlockState state) {
		return state.get(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

}
