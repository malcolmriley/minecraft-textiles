package paragon.minecraft.wilytextiles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import paragon.minecraft.wilytextiles.init.ModBlocks;
import paragon.minecraft.wilytextiles.init.ModConfiguration;
import paragon.minecraft.wilytextiles.init.ModContainers;
import paragon.minecraft.wilytextiles.init.ModItems;
import paragon.minecraft.wilytextiles.init.ModTileEntities;

@Mod(Textiles.MOD_ID)
public class Textiles {
	
	/* Public Fields */
	public static final String MOD_ID = "paragon_textiles";
	public static final Logger LOG = LogManager.getLogger();

	public static final ModBlocks BLOCKS = new ModBlocks();
	public static final ModItems ITEMS = new ModItems();
	public static final ModTileEntities TILE_ENTITIES = new ModTileEntities();
	public static final ModContainers CONTAINERS = new ModContainers();
	
	public static final ModConfiguration CONFIG = new ModConfiguration();

	public Textiles() {
		// Get Bus from current loading context
		final IEventBus bus = this.getBus();

		// Register content providers to the bus
		Textiles.ITEMS.registerTo(bus);
		Textiles.BLOCKS.registerTo(bus);
		Textiles.CONFIG.registerTo(bus);
		Textiles.TILE_ENTITIES.registerTo(bus);
		Textiles.CONTAINERS.registerTo(bus);
	}

	/**
	 * Creates a {@link ResourceLocation} in the domain of this mod's id, with the passed value as a path.
	 *
	 * @param value - The resource "path"
	 * @return A suitable {@link ResourceLocation} in the domain of this mod's id.
	 */
	public static ResourceLocation createResource(String value) {
		return new ResourceLocation(Textiles.MOD_ID, value);
	}

	/**
	 * Concatenates the mod ID of this mod with a period, followed by the passed string.
	 *
	 * @param value - The String to prefix with the mod ID and a period.
	 * @return The passed String, prefixed by the mod ID and a period.
	 */
	public static String createName(String value) {
		return Textiles.MOD_ID + '.' + value;
	}
	
	/* Internal Methods */
	
	/**
	 * Returns the {@link IEventBus} for this mod's loading context.
	 * 
	 * Convenience shortcut method for {@link FMLJavaModLoadingContext#get()} into {@link FMLJavaModLoadingContext#getModEventBus()}.
	 * 
	 * @return The event bus.
	 */
	protected IEventBus getBus() {
		return FMLJavaModLoadingContext.get().getModEventBus();
	}
}
