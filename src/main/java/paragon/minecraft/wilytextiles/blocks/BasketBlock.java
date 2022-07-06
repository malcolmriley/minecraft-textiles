package paragon.minecraft.wilytextiles.blocks;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import paragon.minecraft.library.capabilities.InventoryHandler;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.internal.Utilities;
import paragon.minecraft.wilytextiles.tileentities.TEBasket;

/**
 * Implementation of the Basket {@link Block} class.
 * <p>
 * Baskets are 4x4 inventories that capture {@link EntityItem} within about a half-block's distance from their open face.
 * They can be oriented every direction except downwards.
 *
 * @author Malcolm Riley
 */
public abstract class BasketBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

	/* BlockProperty Fields */
	public static final DirectionProperty FACING = DirectionProperty.create("facing", (direction) -> direction != Direction.DOWN); // Cannot face down
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	/* Internal Fields */
	private static final float DEFAULT_HARDNESS = 0.8F;

	private static final double OFFSET = 1;
	public static final VoxelShape SHAPE_UPRIGHT = Block.box(OFFSET, 0, OFFSET, 16 - OFFSET, 16, 16 - OFFSET);
	public static final VoxelShape SHAPE_NORTH_SOUTH = Block.box(OFFSET, 0, 0, 16 - OFFSET, 16 - (OFFSET * 2), 16);
	public static final VoxelShape SHAPE_EAST_WEST = Block.box(0, 0, OFFSET, 16, 16 - (OFFSET * 2), 16 - OFFSET);

	public static final VoxelShape CAPTURE_UP = Block.box(0, 0, 0, 16, 24, 16);
	public static final VoxelShape CAPTURE_NORTH = Block.box(0, 0, -8, 0, 16, 16);
	public static final VoxelShape CAPTURE_SOUTH = Block.box(0, 0, 16, 16, 16, 24);
	public static final VoxelShape CAPTURE_EAST = Block.box(16, 0, 0, 24, 16, 16);
	public static final VoxelShape CAPTURE_WEST = Block.box(-8, 0, 0, 0, 16, 16);

	public BasketBlock(Properties builder) {
		super(builder);
		this.registerDefaultState(this.createDefaultState());
	}

	/**
	 * Returns the suggested default properties for a {@link BasketBlock}.
	 *
	 * @return Some suggested default properties for a {@link BasketBlock}.
	 */
	public static Properties createDefaultProperties() {
		return Properties.of(Material.LEAVES).sound(SoundType.BAMBOO);
	}

	/**
	 * Returns the item capture area {@link VoxelShape} for the {@link BasketBlock} based on the provided {@link BlockState}
	 *
	 * @param state - The {@link BlockState} to examine
	 * @return The {@link VoxelShape} item capture area
	 */
	public static VoxelShape getCaptureShapeFrom(BlockState state) {
		Direction facing = BasketBlock.getFacingFrom(state);
		switch (facing) {
			case NORTH: return CAPTURE_NORTH;
			case SOUTH: return CAPTURE_SOUTH;
			case EAST: return CAPTURE_EAST;
			case WEST: return CAPTURE_WEST;
			default: return CAPTURE_UP;
		}
	}

	/* Supertype Override Methods */

	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> entityType) {
		return Utilities.Game.getTicker(world, entityType, Textiles.TILE_ENTITIES.BASKET, TEBasket::tick);
	}

	@Override
	@SuppressWarnings("deprecation") // Call super.onReplaced at end of method
	public void onRemove(BlockState state, Level world, BlockPos position, BlockState newState, boolean isMoving) {
		if (world.getBlockEntity(position) instanceof Container) {
			world.updateNeighbourForOutputSignal(position, this);
		}
		super.onRemove(state, world, position, newState, isMoving);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos position) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(position));
	}

	@Override
	public void setPlacedBy(Level world, BlockPos position, BlockState state, LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(world, position, state, placer, stack);
		Utilities.Game.tryRenameFrom(world.getBlockEntity(position), stack);
	}

	@Override
	@SuppressWarnings("deprecation") // Returning super.getFluidState as default if not waterlogged
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos position, CollisionContext context) {
		switch (BasketBlock.getFacingFrom(state)) {
			case EAST:
			case WEST:
				return BasketBlock.SHAPE_EAST_WEST;
			case NORTH:
			case SOUTH:
				return BasketBlock.SHAPE_NORTH_SOUTH;
			default:
				return BasketBlock.SHAPE_UPRIGHT;
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos position, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!world.isClientSide()) {
			return Utilities.UI.openUIFor(player, world, position) ? InteractionResult.CONSUME : InteractionResult.SUCCESS;
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos position, BlockState state) {
		return new TEBasket(position, state);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction facing = context.getClickedFace() == Direction.DOWN ? Direction.UP : context.getClickedFace();
		return super.getStateForPlacement(context).setValue(BasketBlock.FACING, facing);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		Direction facing = BasketBlock.getFacingFrom(state);
		return facing.equals(Direction.UP) ? state : state.setValue(BasketBlock.FACING, rotation.rotate(facing));
	}

	@Override
	@SuppressWarnings("deprecation") // Call super.mirror if facing is not UP.
	public BlockState mirror(BlockState state, Mirror mirror) {
		final Direction facing = state.getValue(BasketBlock.FACING);
		return facing != Direction.UP ? super.mirror(state, mirror) : state.setValue(BasketBlock.FACING, facing.getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BasketBlock.FACING, BasketBlock.WATERLOGGED);
	}

	/* Internal Methods */

	/**
	 * Extracts the {@link #FACING} property from the provided {@link BlockState}.
	 *
	 * @param state The {@link BlockState} to extract from
	 * @return The {@link #FACING} property of that {@link BlockState}.
	 */
	protected static Direction getFacingFrom(BlockState state) {
		return state.getValue(BasketBlock.FACING);
	}

	/**
	 * Creates the default {@link BlockState} for this {@link Block}.
	 *
	 * @return The default {@link BlockState} for this {@link Block}.
	 */
	protected BlockState createDefaultState() {
		return this.stateDefinition.any()
			.setValue(FACING, Direction.UP)
			.setValue(WATERLOGGED, Boolean.FALSE);
	}

	/* Specific Implementations */

	/**
	 * Normal {@link BasketBlock} implementation, functions the same as other inventory blocks.
	 *
	 * @author Malcolm Riley
	 */
	public static class Normal extends BasketBlock {

		public Normal() {
			this(BasketBlock.createDefaultProperties().explosionResistance(DEFAULT_HARDNESS));
		}

		public Normal(Properties builder) {
			super(builder);
		}

		/* Supertype Override Methods */

		@Override
		public void onRemove(BlockState state, Level world, BlockPos position, BlockState newState, boolean isMoving) {
			InventoryHandler.dropContents(state, world, position, newState);
			super.onRemove(state, world, position, newState, isMoving);
		}

	}

	/**
	 * Specialized {@link BasketBlock} implementation, keeps inventory when block is dropped.
	 *
	 * @author Malcolm Riley
	 */
	public static class KeepInventory extends BasketBlock {

		/* Internal Fields */
		public static final ResourceLocation BLOCK_ENTITY_CONTENTS = ShulkerBoxBlock.CONTENTS;

		public KeepInventory() {
			this(BasketBlock.createDefaultProperties().explosionResistance(6.0F));
		}

		public KeepInventory(Properties builder) {
			super(builder);
		}

		@Override
		@SuppressWarnings("deprecation") // Return super.getDrops()
		public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
			if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof TEBasket basket) {
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
