package paragon.minecraft.wilytextiles.generators;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.init.ModItems;

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
		this.add(ModItems.Names.CHAIN_MESH, "Chain Mesh");
		this.add(ModItems.Names.FLAX_STALKS, "Flax Stalks");
		this.add(ModItems.Names.TWINE, "Twine");
		this.add(ModItems.Names.WICKER, "Wicker Patch");
	}

}