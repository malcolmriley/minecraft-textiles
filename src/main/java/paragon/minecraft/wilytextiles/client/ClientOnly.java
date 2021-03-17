package paragon.minecraft.wilytextiles.client;

import net.minecraftforge.eventbus.api.SubscribeEvent;
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
	}
	
	/* Internal Methods */
	
	private static void registerScreenManagers() {
		ClientUtilities.UI.bindGuiFactory(Textiles.CONTAINERS.BASKET, ScreenBasket::new);
	}

}
