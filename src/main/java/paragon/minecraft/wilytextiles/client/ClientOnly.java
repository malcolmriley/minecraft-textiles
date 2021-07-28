package paragon.minecraft.wilytextiles.client;

import java.util.stream.Stream;

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
	
	private static void registerScreenManagers() {
		ClientUtilities.UI.bindGuiFactory(Textiles.CONTAINERS.BASKET, ScreenBasket::new);
	}
	
	private static void registerRenderTypeLookups() {
		Stream<RegistryObject<Block>> cutout = Stream.of(
			Textiles.BLOCKS.FLAX_CROP, 
			Textiles.BLOCKS.FABRIC_PLAIN,
			Textiles.BLOCKS.FABRIC_RED,
			Textiles.BLOCKS.FABRIC_ORANGE,
			Textiles.BLOCKS.FABRIC_YELLOW,
			Textiles.BLOCKS.FABRIC_LIME,
			Textiles.BLOCKS.FABRIC_GREEN,
			Textiles.BLOCKS.FABRIC_CYAN,
			Textiles.BLOCKS.FABRIC_LIGHT_BLUE,
			Textiles.BLOCKS.FABRIC_BLUE,
			Textiles.BLOCKS.FABRIC_PURPLE,
			Textiles.BLOCKS.FABRIC_MAGENTA,
			Textiles.BLOCKS.FABRIC_PINK,
			Textiles.BLOCKS.FABRIC_WHITE,
			Textiles.BLOCKS.FABRIC_LIGHT_GRAY,
			Textiles.BLOCKS.FABRIC_GRAY,
			Textiles.BLOCKS.FABRIC_BLACK,
			Textiles.BLOCKS.FABRIC_BROWN
		);
		cutout.forEach(ClientOnly::setCutoutRender);
	}
	
	private static void setCutoutRender(RegistryObject<Block> registryObject) {
		ClientOnly.setRenderType(registryObject, RenderType.getCutout());
	}
	
	private static void setRenderType(RegistryObject<Block> registryObject, RenderType type) {
		if (registryObject.isPresent()) {
			RenderTypeLookup.setRenderLayer(registryObject.get(), type);
		}
	}

}
