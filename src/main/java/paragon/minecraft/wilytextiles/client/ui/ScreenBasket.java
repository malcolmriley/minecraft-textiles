package paragon.minecraft.wilytextiles.client.ui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import paragon.minecraft.library.client.ui.SimpleContainerScreen;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.tileentities.TEBasket;

@OnlyIn(Dist.CLIENT)
public class ScreenBasket extends SimpleContainerScreen<TEBasket.ContainerImpl> {

	/* Constants */
	private static final ResourceLocation UI_TEXTURE = Textiles.createResource(SimpleContainerScreen.backgroundTexturePath("basket"));

	public ScreenBasket(TEBasket.ContainerImpl container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title);
		this.xSize = 176;
		this.ySize = 186;
		this.playerInventoryTitleY = this.ySize - 94;
	}

	/* Supertype Override Methods */

	@Override
	protected ResourceLocation getBackgroundTexture() {
		return UI_TEXTURE;
	}

}
