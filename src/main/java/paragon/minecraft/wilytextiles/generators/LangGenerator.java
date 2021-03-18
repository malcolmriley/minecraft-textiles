package paragon.minecraft.wilytextiles.generators;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import paragon.minecraft.wilytextiles.Textiles;

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
		
		// Items
		this.add(Textiles.ITEMS.CHAIN_MESH.get(), "Chain Mesh");
		this.add(Textiles.ITEMS.FLAX_STALKS.get(), "Flax Stalks");
		this.add(Textiles.ITEMS.FLAX_PALE.get(), "Pale Flax Blossom");
		this.add(Textiles.ITEMS.FLAX_VIBRANT.get(), "Vibrant Flax Blossom");
		this.add(Textiles.ITEMS.FLAX_PURPLE.get(), "Exquisite Flax Blossom");
		this.add(Textiles.ITEMS.FLAX_SEEDS.get(), "Flax Seeds");
		this.add(Textiles.ITEMS.TWINE.get(), "Twine");
		this.add(Textiles.ITEMS.SILK.get(), "Silk Thread");
		this.add(Textiles.ITEMS.SILK_WISPS.get(), "Silk Wisps");
		this.add(Textiles.ITEMS.WICKER.get(), "Wicker Patch");
		this.add(Textiles.ITEMS.PLANT_FIBERS.get(), "Raw Plant Fibers");
		
		// Blocks
		this.add(Textiles.BLOCKS.RAW_FIBERS.get(), "Raw Fiber Bale");
		this.add(Textiles.BLOCKS.FLAX_CROP.get(), "Flax Flowers");
		this.add(Textiles.BLOCKS.BASKET.get(), "Basket");
	}

}