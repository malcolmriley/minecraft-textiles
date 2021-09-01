package paragon.minecraft.wilytextiles.blocks;

import java.util.List;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import paragon.minecraft.library.Utilities;
import paragon.minecraft.library.capabilities.InventoryHandler;
import paragon.minecraft.wilytextiles.tileentities.TEBasket;

/**
 * Implementation of the Basket {@link Block} class.
 * <p>
 * Baskets are 4x4 inventories that capture {@link EntityItem} within about a half-block's distance from their open face.
 * They can be oriented every direction except downwards.
 * 
 * @author Malcolm Riley
 */
public abstract class BlockBasket extends ContainerBlock implements IWaterLoggable {

	/* BlockProperty Fields */
	public static final DirectionProperty FACING = DirectionProperty.create("facing", (direction) -> direction != Direction.DOWN); // Cannot face down
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	/* Internal Fields */
	private static final float DEFAULT_HARDNESS = 0.8F;

	private static final double OFFSET = 1;
	public static final VoxelShape SHAPE_UPRIGHT = Block.makeCuboidShape(OFFSET, 0, OFFSET, 16 - OFFSET, 16, 16 - OFFSET);
	public static final VoxelShape SHAPE_NORTH_SOUTH = Block.makeCuboidShape(OFFSET, 0, 0, 16 - OFFSET, 16 - (OFFSET * 2), 16);
	public static final VoxelShape SHAPE_EAST_WEST = Block.makeCuboidShape(0, 0, OFFSET, 16, 16 - (OFFSET * 2), 16 - OFFSET);
	
	public static final VoxelShape CAPTURE_UP = Block.makeCuboidShape(0, 0, 0, 16, 24, 16);
	public static final VoxelShape CAPTURE_NORTH = Block.makeCuboidShape(0, 0, -8, 0, 16, 16);
	public static final VoxelShape CAPTURE_SOUTH = Block.makeCuboidShape(0, 0, 16, 16, 16, 24);
	public static final VoxelShape CAPTURE_EAST = Block.makeCuboidShape(16, 0, 0, 24, 16, 16);
	public static final VoxelShape CAPTURE_WEST = Block.makeCuboidShape(-8, 0, 0, 0, 16, 16);

	public BlockBasket(Properties builder) {
		super(builder);
		this.setDefaultState(this.createDefaultState());
	}
	
	/**
	 * Returns the suggested default properties for a {@link BlockBasket}.
	 * 
	 * @return Some suggested default properties for a {@link BlockBasket}.
	 */
	public static AbstractBlock.Properties createDefaultProperties() {
		return AbstractBlock.Properties.create(Material.LEAVES).sound(SoundType.BAMBOO);
	}
	
	/**
	 * Returns the item capture area {@link VoxelShape} for the {@link BlockBasket} based on the provided {@link BlockState}
	 * 
	 * @param state - The {@link BlockState} to examine
	 * @return The {@link VoxelShape} item capture area
	 */
	public static VoxelShape getCaptureShapeFrom(BlockState state) {
		Direction facing = BlockBasket.getFacingFrom(state);
		switch(facing) {
			case NORTH: return CAPTURE_NORTH;
			case SOUTH: return CAPTURE_SOUTH;
			case EAST: return CAPTURE_EAST;
			case WEST: return CAPTURE_WEST;
			default: return CAPTURE_UP;
		}
	}

	/* Supertype Override Methods */
	
	@Override
	@SuppressWarnings("deprecation") // Call super.onReplaced at end of method
	public void onReplaced(BlockState state, World world, BlockPos position, BlockState newState, boolean isMoving) {
		if (world.getTileEntity(position) instanceof IInventory) {
	        world.updateComparatorOutputLevel(position, this);
		}
        super.onReplaced(state, world, position, newState, isMoving);
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState blockState, World world, BlockPos position) {
		return Container.calcRedstone(world.getTileEntity(position));
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos position, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(world, position, state, placer, stack);
		Utilities.Game.tryRenameFrom(world.getTileEntity(position), stack);
	}

	@Override
	@SuppressWarnings("deprecation") // Return super.updatePostPlacement after queueing water tick
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos position, BlockPos facingPosition) {
		Utilities.States.applyWaterlogPostPlacement(state, world, position);
		return super.updatePostPlacement(state, facing, facingState, world, position, facingPosition);
	}

	@Override
	@SuppressWarnings("deprecation") // Returning super.getFluidState as default if not waterlogged
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos position, ISelectionContext context) {
		switch (BlockBasket.getFacingFrom(state)) {
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
		return Utilities.States.applyWaterlogPlacementState(context, super.getStateForPlacement(context).with(BlockBasket.FACING, facing));
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		Direction facing = BlockBasket.getFacingFrom(state);
		return facing.equals(Direction.UP) ? state : state.with(BlockBasket.FACING, rotation.rotate(facing));
	}

	@Override
	@SuppressWarnings("deprecation") // Call super.mirror if facing is not UP.
	public BlockState mirror(BlockState state, Mirror mirror) {
		final Direction facing = state.get(BlockBasket.FACING);
		return facing != Direction.UP ? super.mirror(state, mirror) : state.with(BlockBasket.FACING, facing.getOpposite());
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(BlockBasket.FACING, BlockBasket.WATERLOGGED);
	}

	/* Internal Methods */

	/**
	 * Extracts the {@link #FACING} property from the provided {@link BlockState}.
	 * 
	 * @param state The {@link BlockState} to extract from
	 * @return The {@link #FACING} property of that {@link BlockState}.
	 */
	protected static Direction getFacingFrom(BlockState state) {
		return state.get(BlockBasket.FACING);
	}
	
	/**
	 * Creates the default {@link BlockState} for this {@link Block}.
	 * 
	 * @return The default {@link BlockState} for this {@link Block}.
	 */
	protected BlockState createDefaultState() {
		return this.stateContainer.getBaseState()
			.with(FACING, Direction.UP)
			.with(WATERLOGGED, Boolean.FALSE);
	}
	
	/* Specific Implementations */
	
	/**
	 * Normal {@link BlockBasket} implementation, functions the same as other inventory blocks.
	 * 
	 * @author Malcolm Riley
	 */
	public static class Normal extends BlockBasket {
		
		public Normal() {
			this(BlockBasket.createDefaultProperties().hardnessAndResistance(DEFAULT_HARDNESS));
		}

		public Normal(Properties builder) {
			super(builder);
		}
		
		/* Supertype Override Methods */

		@Override
		public void onReplaced(BlockState state, World world, BlockPos position, BlockState newState, boolean isMoving) {
			InventoryHandler.dropContents(state, world, position, newState);
			super.onReplaced(state, world, position, newState, isMoving);
		}
		
	}
	
	/**
	 * Specialized {@link BlockBasket} implementation, keeps inventory when block is dropped.
	 * 
	 * @author Malcolm Riley
	 */
	public static class KeepInventory extends BlockBasket {
		
		/* Internal Fields */
		protected static final ResourceLocation BLOCK_ENTITY_CONTENTS = ShulkerBoxBlock.CONTENTS;
		
		public KeepInventory() {
			this(BlockBasket.createDefaultProperties().hardnessAndResistance(DEFAULT_HARDNESS, 6.0F));
		}

		public KeepInventory(Properties builder) {
			super(builder);
		}
		
		@SuppressWarnings("deprecation") // Return super.getDrops()
		public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
			TileEntity discovered = builder.get(LootParameters.BLOCK_ENTITY);
			if (discovered instanceof TEBasket) {
				TEBasket basket = (TEBasket) discovered;
				if (!basket.isEmpty()) {
					builder.withDynamicDrop(BLOCK_ENTITY_CONTENTS, (context, consumer) -> {
						basket.getInventory().getItems().forEach(consumer);
					});
				}
			}

			return super.getDrops(state, builder);
		}

	}

}
