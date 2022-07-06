package paragon.minecraft.wilytextiles.tileentities;

import java.util.List;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import paragon.minecraft.library.capabilities.FilteredInventoryHandler;
import paragon.minecraft.library.capabilities.InventoryHandler;
import paragon.minecraft.library.client.ui.FilteredSlot;
import paragon.minecraft.library.client.ui.SimpleMenu;
import paragon.minecraft.library.client.ui.SlotGroup;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.blocks.BasketBlock;
import paragon.minecraft.wilytextiles.init.ModBlocks;
import paragon.minecraft.wilytextiles.internal.Utilities;

/**
 * Tile Entity implementation for the Basket block.
 * 
 * @author Malcolm Riley
 */
public class TEBasket extends RandomizableContainerBlockEntity {

	/* Shared Fields */
	private static final int INVENTORY_WIDTH = 4;
	private static final int INVENTORY_HEIGHT = 4;
	private static final int INVENTORY_SIZE = TEBasket.INVENTORY_WIDTH * TEBasket.INVENTORY_HEIGHT;

	/* Internal Fields */
	protected static final Component DEFAULT_NAME = new TranslatableComponent("container." + ModBlocks.Names.BASKET);
	protected final InventoryHandler ITEMS = new FilteredInventoryHandler(TEBasket.INVENTORY_SIZE, this, (index, stack) -> this.canHold(stack));
	protected final int RANDOMIZER = ThreadLocalRandom.current().nextInt(20);

	public TEBasket(BlockPos position, BlockState state) {
		super(Textiles.TILE_ENTITIES.BASKET.get(), position, state);
	}
	
	/* BlockEntityTicker Implementation */
	
	public static void tick(Level level, BlockPos position, BlockState state, TEBasket instance) {
		if (instance.shouldCaptureItems()) {
			instance.captureItems();
		}
	}
	
	/* Public Methods */

	/**
	 * Provides the {@link InventoryHandler} instance of this {@link TEBasket}.
	 * <p>
	 * Needed because {@link BasketBlock.KeepInventory#getDrops(BlockState, net.minecraft.loot.LootContext.Builder)} uses it to add dynamic drops.
	 * 
	 * @return The {@link InventoryHandler} for this {@link TEBasket}.
	 */
	public InventoryHandler getInventory() {
		return this.ITEMS;
	}
	
	/**
	 * Returns whether or not the basket can hold the provided {@link ItemStack}.
	 * 
	 * @param instance - The {@link ItemStack} to check
	 * @return Whether the provided {@link ItemStack} can be inserted into this {@link TEBasket}.
	 */
	public boolean canHold(ItemStack instance) {
		// TODO: Separate into another TE type?
		if (this.getBlockState().is(Textiles.BLOCKS.BASKET_STURDY.get())) {
			return !Utilities.Game.itemHasInventory(instance);
		}
		return true;
	}

	/* Supertype Override Methods */
	
	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		return this.canHold(stack);
	}

	@Override
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		if (!this.trySaveLootTable(compound)) {
			this.ITEMS.writeTo(compound);
		}
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		if (!this.tryLoadLootTable(compound)) {
			this.ITEMS.readFrom(compound);
		}
	}

	@Override
	public int getContainerSize() {
		return INVENTORY_SIZE;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.ITEMS.getItems();
	}

	@Override
	protected void setItems(NonNullList<ItemStack> items) {
		for (int index = 0; index < TEBasket.INVENTORY_SIZE; index += 1) {
			this.ITEMS.setStackInSlot(index, items.get(index));
		}
	}

	@Override
	protected Component getDefaultName() {
		return TEBasket.DEFAULT_NAME;
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
		return BasketMenu.create(id, inventory, this);
	}
	
	/* Internal Methods */
	
	protected void captureItems() {
		this.getCapturableItems().forEach(this::tryCaptureItem);
	}
	
	protected boolean shouldCaptureItems() {
		return this.hasLevel() && !this.level.isClientSide() && (this.level.getGameTime() + this.RANDOMIZER) % 5 == 0;
	}
	
	protected List<ItemEntity> getCapturableItems() {
		return this.getLevel().getEntitiesOfClass(ItemEntity.class, this.getCaptureArea());
	}
	
	protected AABB getCaptureArea() {
		return BasketBlock.getCaptureShapeFrom(this.getBlockState()).bounds().move(this.getBlockPos());
	}
	
	protected void tryCaptureItem(ItemEntity entity) {
		if (this.canHold(entity.getItem())) {
			HopperBlockEntity.addItem(this, entity); // Why re-invent the wheel?
		}
	}

	/* Container Implementation */

	public static class BasketMenu extends SimpleMenu {

		/* Internal Fields */
		protected final TEBasket basketInstance;
		protected final SlotGroup playerInventory;
		protected final SlotGroup playerHotbar;
		protected final SlotGroup basketGrid;

		private BasketMenu(int id, Inventory inventory, TEBasket instance) {
			super(Textiles.CONTAINERS.BASKET.get(), id);
			this.basketInstance = instance;
			this.playerInventory = this.addPlayerInventory(inventory, 8, 110);
			this.playerHotbar = this.addPlayerHotbar(inventory, 8, 168);
			this.basketGrid = this.addSlotGrid(this.basketInstance, 53, 18, TEBasket.INVENTORY_WIDTH, TEBasket.INVENTORY_HEIGHT, BasketMenu.createSlotFactory(instance));
		}

		/* Public Methods */

		public static BasketMenu create(int id, Inventory inventory, TEBasket instance) {
			return new BasketMenu(id, inventory, instance);
		}

		public static BasketMenu create(int id, Inventory inventory, FriendlyByteBuf data) {
			final BlockPos position = data.readBlockPos();
			final TEBasket basket = (TEBasket) inventory.player.getLevel().getBlockEntity(position);
			return new BasketMenu(id, inventory, basket);
		}

		/* Supertype Override Methods */

		@Override
		public ItemStack onStackTransfer(Player player, int sourceSlotIndex, ItemStack sourceStack) {
			if (this.basketGrid.holdsSlot(sourceSlotIndex)) {
				return this.tryApplyTransfer(sourceStack, false, this.playerHotbar, this.playerInventory);
			}
			return this.tryApplyTransfer(sourceStack, false, this.basketGrid);
		}
		
		/* IForgeMenuType Compliance Methods */

		@Override
		public boolean stillValid(Player player) {
			return this.basketInstance.canOpen(player);
		}
		
		/* Internal Methods */
		
		protected static ISlotFactory<Slot> createSlotFactory(TEBasket instance) {
			return (inventory, index, xPos, yPos) -> new FilteredSlot(inventory, index, xPos, yPos, instance::canHold);
		}

	}

}
