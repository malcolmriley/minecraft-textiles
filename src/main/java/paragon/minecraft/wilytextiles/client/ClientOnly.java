package paragon.minecraft.wilytextiles.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import paragon.minecraft.library.client.ClientUtilities;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.client.ui.ScreenBasket;

/**
 * Event handler for client-only events.
 * 
 * @author Malcolm Riley
 */
@EventBusSubscriber(bus = Bus.MOD)
public final class ClientOnly {
	
	private ClientOnly() {}
	
	/* Event Handler Methods */

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(ClientOnly::registerScreenManagers);
		event.enqueueWork(ClientOnly::registerRenderTypeLookups);
	}
	
	/* Internal Methods */
	
	/**
	 * Method for performing registration of UI factories.
	 */
	private static void registerScreenManagers() {
		ClientUtilities.UI.bindGuiFactory(Textiles.CONTAINERS.BASKET, ScreenBasket::new);
	}
	
	/**
	 * Method for performing registration of {@link RenderType} for each block.
	 */
	private static void registerRenderTypeLookups() {
		ClientOnly.setCutoutRender(Textiles.BLOCKS.FLAX_CROP);
		Textiles.BLOCKS.streamFabricBlocks().forEach(block -> RenderTypeLookup.setRenderLayer(block, RenderType.getCutout()));
	}
	
	/**
	 * Sets the {@link RenderType} of the {@link Block} contained in the provided {@link RegistryObject} to {@link RenderType#getCutout()}.
	 * 
	 * @param registryObject - A {@link RegistryObject} containing a {@link Block} to set to the cutout render type.
	 */
	private static void setCutoutRender(RegistryObject<Block> registryObject) {
		ClientOnly.setRenderType(registryObject, RenderType.getCutout());
	}
	
	/**
	 * Sets the {@link RenderType} of the {@link Block} contained in the provided {@link RegistryObject}, if it is present.
	 * 
	 * @param registryObject - A {@link RegistryObject}, potentially holding a {@link Block}
	 * @param type - The {@link RenderType} to set to the {@link Block} held by the provided {@link RegistryObject}.
	 */
	private static void setRenderType(RegistryObject<Block> registryObject, RenderType type) {
		if (registryObject.isPresent()) {
			RenderTypeLookup.setRenderLayer(registryObject.get(), type);
		}
	}

}
