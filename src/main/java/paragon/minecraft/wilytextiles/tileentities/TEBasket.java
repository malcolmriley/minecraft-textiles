package paragon.minecraft.wilytextiles.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import paragon.minecraft.library.capabilities.InventoryHandler;
import paragon.minecraft.library.client.ui.AbstractContainer;
import paragon.minecraft.library.client.ui.SlotGroup;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.init.ModBlocks;

public class TEBasket extends LockableLootTileEntity {

	/* Shared Fields */
	private static final int INVENTORY_WIDTH = 4;
	private static final int INVENTORY_HEIGHT = 4;
	private static final int INVENTORY_SIZE = TEBasket.INVENTORY_WIDTH * TEBasket.INVENTORY_HEIGHT;

	/* Internal Fields */
	protected static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container." + ModBlocks.Names.BASKET);
	protected final InventoryHandler ITEMS = new InventoryHandler(TEBasket.INVENTORY_SIZE, this);

	public TEBasket() {
		super(Textiles.TILE_ENTITIES.BASKET.get());
	}

	/* Supertype Override Methods */

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		if (!this.checkLootAndWrite(compound)) {
			this.ITEMS.writeTo(compound);
		}
		return compound;
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		if (!this.checkLootAndRead(compound)) {
			this.ITEMS.readFrom(compound);
		}
	}

	@Override
	public int getSizeInventory() {
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
	protected ITextComponent getDefaultName() {
		return TEBasket.DEFAULT_NAME;
	}

	@Override
	protected Container createMenu(int id, PlayerInventory inventory) {
		return ContainerImpl.create(id, inventory, this);
	}

	/* Container Implementation */

	public static class ContainerImpl extends AbstractContainer {

		/* Internal Fields */
		protected final IInventory basketInstance;
		protected final SlotGroup playerInventory;
		protected final SlotGroup playerHotbar;
		protected final SlotGroup basketGrid;

		private ContainerImpl(int id, PlayerInventory inventory, TEBasket instance) {
			super(Textiles.CONTAINERS.BASKET.get(), id);
			this.basketInstance = instance;
			this.playerInventory = this.addPlayerInventory(inventory, 8, 104);
			this.playerHotbar = this.addPlayerHotbar(inventory, 8, 162);
			this.basketGrid = this.addSlotGrid(this.basketInstance, 53, 18, TEBasket.INVENTORY_WIDTH, TEBasket.INVENTORY_HEIGHT);
		}

		/* Public Methods */

		public static ContainerImpl create(int id, PlayerInventory inventory, TEBasket instance) {
			return new ContainerImpl(id, inventory, instance);
		}

		public static ContainerImpl createClientContainer(int id, PlayerInventory inventory, PacketBuffer data) {
			final BlockPos position = data.readBlockPos();
			final TEBasket basket = (TEBasket) inventory.player.world.getTileEntity(position);
			return new ContainerImpl(id, inventory, basket);
		}

		/* Supertype Override Methods */

		@Override
		public ItemStack onStackTransfer(PlayerEntity player, int sourceSlotIndex, ItemStack sourceStack) {
			if (this.basketGrid.holdsSlot(sourceSlotIndex)) {
				return this.tryApplyTransfer(sourceStack, false, this.playerHotbar, this.playerInventory);
			}
			return this.tryApplyTransfer(sourceStack, false, this.basketGrid);
		}

		@Override
		public boolean canInteractWith(PlayerEntity player) {
			return this.basketInstance.isUsableByPlayer(player);
		}

	}

}
