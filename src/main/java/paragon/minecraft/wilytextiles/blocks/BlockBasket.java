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
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import paragon.minecraft.library.Utilities;
import paragon.minecraft.wilytextiles.tileentities.TEBasket;

public class BlockBasket extends ContainerBlock {

	/* BlockProperty Fields */
	public static final DirectionProperty FACING = DirectionProperty.create("facing", (direction) -> direction != Direction.DOWN); // Cannot face down
	private static final double OFFSET = 1.0 / 16.0;
	public static final VoxelShape SHAPE_UPRIGHT = VoxelShapes.create(OFFSET, 0.0, OFFSET, 1.0 - OFFSET, 1.0, 1.0 - OFFSET);
	public static final VoxelShape SHAPE_EAST_WEST = VoxelShapes.create(0.0, OFFSET, OFFSET, 1.0, 1.0 - OFFSET, 1.0 - OFFSET);
	public static final VoxelShape SHAPE_NORTH_SOUTH = VoxelShapes.create(OFFSET, OFFSET, 0.0, 1.0 - OFFSET, 1.0 - OFFSET, 1.0);

	public BlockBasket(Properties builder) {
		super(builder);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP));
	}

	/* Supertype Override Methods */

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos position, ISelectionContext context) {
		switch(this.getFacingFrom(state)) {
			case EAST:
			case WEST:
				return BlockBasket.SHAPE_EAST_WEST;
			case NORTH:
			case SOUTH:
				return BlockBasket.SHAPE_NORTH_SOUTH;
			default: 
				return BlockBasket.SHAPE_UPRIGHT;
		}
	}

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
		Direction facing = context.getFace() == Direction.DOWN ? Direction.UP : context.getFace();
		return this.getDefaultState().with(FACING, facing);
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
	
	/* Internal Methods */
	
	protected Direction getFacingFrom(BlockState state) {
		return state.get(BlockBasket.FACING);
	}

}
