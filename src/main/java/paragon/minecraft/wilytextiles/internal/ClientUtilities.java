package paragon.minecraft.wilytextiles.internal;

import com.mojang.blaze3d.platform.ScreenManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.MenuScreens.ScreenConstructor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.RegistryObject;

/**
 * Container class for various static utility functions that should be restricted to client-sided use only.
 *
 * @author Malcolm Riley
 */
public class ClientUtilities {
	
	/**
	 * Utility class for various client-sided UI methods.
	 */
	public static class UI {

		private UI() { }
		
		/**
		 * Convenience method for binding a {@link MenuType} {@link RegistryObject} to a {@link ScreenManager.IScreenFactory}.
		 * <p>
		 * Callers should ensure that the type parameter of the {@link MenuType} matches the expected type of the factory.
		 * 
		 * @param <C> The type of the {@link Container}
		 * @param <S> The type of the {@link Screen}
		 * @param source - The {@link RegistryObject} containing the {@link MenuType} to bind
		 * @param factory - The {@link ScreenManager.IScreenFactory} to bind to that type
		 */
		public static <C extends AbstractContainerMenu, S extends Screen & MenuAccess<C>> void bindGuiFactory(RegistryObject<MenuType<?>> source, ScreenConstructor<C, S> factory) {
			MenuScreens.register(UI.castFromRegistryObject(source), factory);
		}
		
		/* Internal Methods */
		
		@SuppressWarnings("unchecked") // Unchecked cast of erased parameterized type from RegistryObject for use in ScreenManager.registerFactory, which expects explicitly-matching type parameters
		protected static <T extends AbstractContainerMenu> MenuType<T> castFromRegistryObject(RegistryObject<MenuType<?>> source) {
			return (MenuType<T>)source.get();
		}
		
	}
	
	/**
	 * Utility class for various render-related methods.
	 */
	public static class Render {
		
		private Render() { }
		
		/**
		 * Method for drawing a simple centered background layer texture, suitable for use as a {@link ContainerScreen}-derived backdrop.
		 * <p>
		 * Resets the render color, binds the provided texture, and then blits the texture using the provided {@link PoseStack}.
		 * <p>
		 * The provided {@link ResourceLocation} should be a fully-qualified path with the appropriate suffix.
		 * 
		 * @param <S> The {@link Screen} type
		 * @param screenReference - A reference to the calling {@link Screen} derivative (callers may simply supply {@code this})
		 * @param currentStack - A reference to the current {@link PoseStack}
		 * @param texture - A {@link ResourceLocation} corresponding to the location of the texture to draw
		 * @param textureWidth - The width of the texture, in pixels
		 * @param textureHeight - The height of the texture, in pixels
		 */
		public static <S extends Screen> void drawCenteredBackgroundLayer(S screenReference, PoseStack currentStack, ResourceLocation texture, int textureWidth, int textureHeight) {
			Render.resetRenderColor();
			screenReference.getMinecraft().getTextureManager().bindForSetup(texture);
			int xPos = (screenReference.width - textureWidth) / 2;
			int yPos = (screenReference.height - textureHeight) / 2;
			screenReference.blit(currentStack, xPos, yPos, 0, 0, textureWidth, textureHeight);
		}

		/* Internal Methods */
		
		protected static void resetRenderColor() {
			RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
		}
		
	}

}
