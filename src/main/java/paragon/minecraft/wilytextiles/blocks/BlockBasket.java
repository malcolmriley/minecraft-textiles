package paragon.minecraft.wilytextiles.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import paragon.minecraft.library.Utilities;
import paragon.minecraft.wilytextiles.tileentities.TEBasket;

public class BlockBasket extends ContainerBlock {

	/* BlockProperty Fields */
	public static final DirectionProperty FACING = DirectionProperty.create("facing", (direction) -> direction != Direction.DOWN); // Cannot face down

	public BlockBasket(Properties builder) {
		super(builder);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP));
	}

	/* Supertype Override Methods */

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos position, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (!world.isRemote) {
			return Utilities.UI.openUIFor(player, world, position) ? ActionResultType.CONSUME : ActionResultType.SUCCESS;
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new TEBasket();
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getFace().getOpposite());
	}

	@Override
	@SuppressWarnings("deprecation") // Call super.mirror if facing is not UP.
	public BlockState mirror(BlockState state, Mirror mirror) {
		final Direction facing = state.get(BlockBasket.FACING);
		return facing != Direction.UP ? super.mirror(state, mirror) : state;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING);
	}

}
