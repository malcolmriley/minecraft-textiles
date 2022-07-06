package paragon.minecraft.wilytextiles.internal;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Interface specification for classes that listen to {@link IEventBus}.
 * 
 * @author Malcolm Riley
 */
public interface IEventBusListener {
	
	/**
	 * Use this method to register the appropriate listeners to the passed {@link IEventBus}.
	 * 
	 * This {@link IEventBus} is usually provided by {@link FMLJavaModLoadingContext#getModEventBus()}
	 * 
	 * @param bus - The {@link IEventBus} to register to.
	 */
	public void registerTo(IEventBus bus);

}
