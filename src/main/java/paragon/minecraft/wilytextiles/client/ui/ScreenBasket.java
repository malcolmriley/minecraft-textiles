package paragon.minecraft.wilytextiles.client.ui;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.tileentities.TEBasket;

/**
 * Implementation of the GUI for the Basket.
 * 
 * @author Malcolm Riley
 */
@OnlyIn(Dist.CLIENT)
public class ScreenBasket extends SimpleContainerScreen<TEBasket.BasketMenu> {

	/* Constants */
	private static final ResourceLocation UI_TEXTURE = Textiles.createResource(SimpleContainerScreen.backgroundTexturePath("basket"));

	public ScreenBasket(TEBasket.BasketMenu container, Inventory inventory, Component title) {
		super(container, inventory, title);
		this.imageWidth = 176;
		this.imageHeight = 192;
		this.inventoryLabelY = this.height - 94;
	}

	/* Supertype Override Methods */

	@Override
	protected ResourceLocation getBackgroundTexture() {
		return UI_TEXTURE;
	}

}
