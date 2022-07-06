package paragon.minecraft.wilytextiles.client.ui;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import paragon.minecraft.wilytextiles.internal.ClientUtilities;
import paragon.minecraft.wilytextiles.internal.Utilities;

/**
 * Simple {@link ContainerScreen} implementation suitable for screens that merely draw a background texture and appropriate titles.
 * <p>
 * Implementors should ensure that they correctly set values for {@link #getXSize()} and {@link #getYSize()}, as well as that of the player inventory title position.
 * Setting these values in the constructor will be sufficient for most use cases.
 * 
 * @author Malcolm Riley
 *
 * @param <C> The {@link Container} type
 */
@OnlyIn(Dist.CLIENT)
public abstract class SimpleContainerScreen<C extends AbstractContainerMenu> extends AbstractContainerScreen<C> {
	
	public SimpleContainerScreen(C screenContainer, Inventory inventory, Component title) {
		super(screenContainer, inventory, title);
	}

	/* Supertype Override Methods */

	@Override
	public void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		this.renderBackground(matrixStack);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	public void renderBackground(PoseStack matrixStack) {
		ClientUtilities.Render.drawCenteredBackgroundLayer(this, matrixStack, this.getBackgroundTexture(), this.getXSize(), this.getYSize());
	}
	
	/* Abstract Methods */
	
	/**
	 * This method should return the {@link ResourceLocation} of the background texture to draw.
	 * <p>
	 * The path should be fully qualified within the resource domain, including the file suffix.
	 * @return A {@link ResourceLocation} path to the texture to use for the background.
	 */
	protected abstract ResourceLocation getBackgroundTexture();

	/* Internal Methods */
	
	/**
	 * Convenience method for returning a path to a texture, potentially useful with creating a {@link ResourceLocation} for use in {@link #getBackgroundTexture()}.
	 * <p>
	 * The returned path will be the concatenation of {@code textures/gui/container}, followed by the passed filename, followed by the {@code .png} suffix.
	 * <p>
	 * This resulting path will mirror the expected base-game location for similar textures.
	 * 
	 * @param filename - The endpoint filename of the texture
	 * @return A suitable texture path.
	 */
	protected static String backgroundTexturePath(String filename) {
		return Utilities.Strings.texturePath("textures", "gui", "container", filename);
	}

}
