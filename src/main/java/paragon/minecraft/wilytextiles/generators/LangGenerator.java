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
		this.addItem(Textiles.ITEMS.CHAIN_MESH, "Chain Mesh");
		this.addItem(Textiles.ITEMS.FLAX_STALKS, "Flax Stalks");
		this.addItem(Textiles.ITEMS.FLAX_PALE, "Pale Flax Blossoms");
		this.addItem(Textiles.ITEMS.FLAX_VIBRANT, "Vibrant Flax Blossoms");
		this.addItem(Textiles.ITEMS.FLAX_PURPLE, "Exquisite Flax Blossoms");
		this.addItem(Textiles.ITEMS.FLAX_SEEDS, "Flax Seeds");
		this.addItem(Textiles.ITEMS.TWINE, "Twine");
		this.addItem(Textiles.ITEMS.SILK, "Silk Thread");
		this.addItem(Textiles.ITEMS.SILK_WISPS, "Silk Wisps");
		this.addItem(Textiles.ITEMS.WICKER, "Wicker Patch");
		this.addItem(Textiles.ITEMS.PLANT_FIBERS, "Raw Plant Fibers");
		this.addItem(Textiles.ITEMS.FLAXSEED_OIL_BOTTLE, "Flaxseed Oil Bottle");
		this.addItem(Textiles.ITEMS.FLAXSEED_OIL_BUCKET, "Flaxseed Oil Bucket");
		this.addItem(Textiles.ITEMS.WOOD_STAIN, "Wood Stain");
		this.addItem(Textiles.ITEMS.WOOD_BLEACH, "Wood Bleach");
		
		// Blocks
		this.addBlock(Textiles.BLOCKS.RAW_FIBERS, "Raw Fiber Bale");
		this.addBlock(Textiles.BLOCKS.RETTED_FIBERS, "Retted Fiber Bale");
		this.addBlock(Textiles.BLOCKS.FLAX_CROP, "Flax Flowers");
		this.addBlock(Textiles.BLOCKS.BASKET, "Basket");
		this.addBlock(Textiles.BLOCKS.BASKET_STURDY, "Sturdy Basket");
		this.addBlock(Textiles.BLOCKS.PACKED_FEATHERS, "Bundle of Feathers");

		this.addBlock(Textiles.BLOCKS.FABRIC_PLAIN, "Plain Fabric");
		this.addBlock(Textiles.BLOCKS.FABRIC_RED, "Red Fabric");
		this.addBlock(Textiles.BLOCKS.FABRIC_ORANGE, "Orange Fabric");
		this.addBlock(Textiles.BLOCKS.FABRIC_YELLOW, "Yellow Fabric");
		this.addBlock(Textiles.BLOCKS.FABRIC_LIME, "Lime Fabric");
		this.addBlock(Textiles.BLOCKS.FABRIC_GREEN, "Green Fabric");
		this.addBlock(Textiles.BLOCKS.FABRIC_CYAN, "Cyan Fabric");
		this.addBlock(Textiles.BLOCKS.FABRIC_LIGHT_BLUE, "Light Blue Fabric");
		this.addBlock(Textiles.BLOCKS.FABRIC_BLUE, "Blue Fabric");
		this.addBlock(Textiles.BLOCKS.FABRIC_PURPLE, "Purple Fabric");
		this.addBlock(Textiles.BLOCKS.FABRIC_MAGENTA, "Magenta Fabric");
		this.addBlock(Textiles.BLOCKS.FABRIC_PINK, "Pink Fabric");
		this.addBlock(Textiles.BLOCKS.FABRIC_WHITE, "White Fabric");
		this.addBlock(Textiles.BLOCKS.FABRIC_LIGHT_GRAY, "Light Gray Fabric");
		this.addBlock(Textiles.BLOCKS.FABRIC_GRAY, "Gray Fabric");
		this.addBlock(Textiles.BLOCKS.FABRIC_BLACK, "Black Fabric");
		this.addBlock(Textiles.BLOCKS.FABRIC_BROWN, "Brown Fabric");

		this.addBlock(Textiles.BLOCKS.CUSHION_PLAIN, "Plain Cushion");
		this.addBlock(Textiles.BLOCKS.CUSHION_RED, "Red Cushion");
		this.addBlock(Textiles.BLOCKS.CUSHION_ORANGE, "Orange Cushion");
		this.addBlock(Textiles.BLOCKS.CUSHION_YELLOW, "Yellow Cushion");
		this.addBlock(Textiles.BLOCKS.CUSHION_LIME, "Lime Cushion");
		this.addBlock(Textiles.BLOCKS.CUSHION_GREEN, "Green Cushion");
		this.addBlock(Textiles.BLOCKS.CUSHION_CYAN, "Cyan Cushion");
		this.addBlock(Textiles.BLOCKS.CUSHION_LIGHT_BLUE, "Light Blue Cushion");
		this.addBlock(Textiles.BLOCKS.CUSHION_BLUE, "Blue Cushion");
		this.addBlock(Textiles.BLOCKS.CUSHION_PURPLE, "Purple Cushion");
		this.addBlock(Textiles.BLOCKS.CUSHION_MAGENTA, "Magenta Cushion");
		this.addBlock(Textiles.BLOCKS.CUSHION_PINK, "Pink Cushion");
		this.addBlock(Textiles.BLOCKS.CUSHION_WHITE, "White Cushion");
		this.addBlock(Textiles.BLOCKS.CUSHION_LIGHT_GRAY, "Light Gray Cushion");
		this.addBlock(Textiles.BLOCKS.CUSHION_GRAY, "Gray Cushion");
		this.addBlock(Textiles.BLOCKS.CUSHION_BLACK, "Black Cushion");
		this.addBlock(Textiles.BLOCKS.CUSHION_BROWN, "Brown Cushion");
	}

}