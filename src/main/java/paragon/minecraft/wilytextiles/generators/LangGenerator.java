package paragon.minecraft.wilytextiles.generators;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.init.ModBlocks;

/**
 * Data generator class for default localization files.
 * 
 * @author Malcolm Riley
 */
final class LangGenerator extends LanguageProvider {

	/* Constants */
	private static final String LOCALE_DEFAULT = "en_us";

	LangGenerator(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, Textiles.MOD_ID, LOCALE_DEFAULT);
	}

	/* Supertype Override Methods */

	@Override
	protected void addTranslations() {
		// ItemGroup
		this.add("itemGroup." + Textiles.MOD_ID, "Wily Textiles");
		
		// Container Titles
		this.add("container." + ModBlocks.Names.BASKET, "Basket");
		
		// Items
		this.add(Textiles.ITEMS.CHAIN_MESH.get(), "Chain Mesh");
		this.add(Textiles.ITEMS.FLAX_STALKS.get(), "Flax Stalks");
		this.add(Textiles.ITEMS.FLAX_PALE.get(), "Pale Flax Blossoms");
		this.add(Textiles.ITEMS.FLAX_VIBRANT.get(), "Vibrant Flax Blossoms");
		this.add(Textiles.ITEMS.FLAX_PURPLE.get(), "Exquisite Flax Blossoms");
		this.add(Textiles.ITEMS.FLAX_SEEDS.get(), "Flax Seeds");
		this.add(Textiles.ITEMS.TWINE.get(), "Twine");
		this.add(Textiles.ITEMS.SILK.get(), "Silk Thread");
		this.add(Textiles.ITEMS.SILK_WISPS.get(), "Silk Wisps");
		this.add(Textiles.ITEMS.WICKER.get(), "Wicker Patch");
		this.add(Textiles.ITEMS.PLANT_FIBERS.get(), "Raw Plant Fibers");
		this.add(Textiles.ITEMS.FLAXSEED_OIL_BOTTLE.get(), "Flaxseed Oil Bottle");
		this.add(Textiles.ITEMS.FLAXSEED_OIL_BUCKET.get(), "Flaxseed Oil Bucket");
		this.add(Textiles.ITEMS.WOOD_STAIN.get(), "Wood Stain");
		this.add(Textiles.ITEMS.WOOD_BLEACH.get(), "Wood Bleach");
		
		// Blocks
		this.add(Textiles.BLOCKS.RAW_FIBERS.get(), "Raw Fiber Bale");
		this.add(Textiles.BLOCKS.FLAX_CROP.get(), "Flax Flowers");
		this.add(Textiles.BLOCKS.BASKET.get(), "Basket");
		this.add(Textiles.BLOCKS.BASKET_STURDY.get(), "Sturdy Basket");

		this.add(Textiles.BLOCKS.FABRIC_PLAIN.get(), "Plain Fabric");
		this.add(Textiles.BLOCKS.FABRIC_RED.get(), "Red Fabric");
		this.add(Textiles.BLOCKS.FABRIC_ORANGE.get(), "Orange Fabric");
		this.add(Textiles.BLOCKS.FABRIC_YELLOW.get(), "Yellow Fabric");
		this.add(Textiles.BLOCKS.FABRIC_LIME.get(), "Lime Fabric");
		this.add(Textiles.BLOCKS.FABRIC_GREEN.get(), "Green Fabric");
		this.add(Textiles.BLOCKS.FABRIC_CYAN.get(), "Cyan Fabric");
		this.add(Textiles.BLOCKS.FABRIC_LIGHT_BLUE.get(), "Light Blue Fabric");
		this.add(Textiles.BLOCKS.FABRIC_BLUE.get(), "Blue Fabric");
		this.add(Textiles.BLOCKS.FABRIC_PURPLE.get(), "Purple Fabric");
		this.add(Textiles.BLOCKS.FABRIC_MAGENTA.get(), "Magenta Fabric");
		this.add(Textiles.BLOCKS.FABRIC_PINK.get(), "Pink Fabric");
		this.add(Textiles.BLOCKS.FABRIC_WHITE.get(), "White Fabric");
		this.add(Textiles.BLOCKS.FABRIC_LIGHT_GRAY.get(), "Light Gray Fabric");
		this.add(Textiles.BLOCKS.FABRIC_GRAY.get(), "Gray Fabric");
		this.add(Textiles.BLOCKS.FABRIC_BLACK.get(), "Black Fabric");
		this.add(Textiles.BLOCKS.FABRIC_BROWN.get(), "Brown Fabric");
	}

}