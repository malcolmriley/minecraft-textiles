package paragon.minecraft.wilytextiles.generators;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import paragon.minecraft.library.Utilities;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.init.ModItems;

final class ItemModelGenerator extends ItemModelProvider {

	/* Internal Fields */
	protected static final String TEXTURE_DEFAULT = "layer0";
	protected static final String ITEM_PREFIX = ITEM_FOLDER + Utilities.Strings.DELIMITER_PATH;
	protected static final String BLOCK_PREFIX = BLOCK_FOLDER + Utilities.Strings.DELIMITER_PATH;

	protected static final ModelFile GENERATED = new UncheckedModelFile(ITEM_PREFIX + "generated");

	ItemModelGenerator(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, Textiles.MOD_ID, helper);
	}

	/* Supertype Override Methods */

	@Override
	protected void registerModels() {
		String[] simpleItems = new String[] {
			ModItems.Names.CHAIN_MESH,
			ModItems.Names.FLAX_STALKS,
			ModItems.Names.FLAX_PALE,
			ModItems.Names.FLAX_VIBRANT,
			ModItems.Names.FLAX_PURPLE,
			ModItems.Names.TWINE,
			ModItems.Names.WICKER
		};
		for (String iterated : simpleItems) {
			this.simpleItem(iterated);
		}
	}

	/* Internal Methods */

	protected void blockItem(String name) {
		this.getBuilder(name).parent(this.getExistingFile(this.modLoc(BLOCK_PREFIX + name)));
	}

	protected void simpleItem(String name) {
		this.getBuilder(name).parent(GENERATED).texture(TEXTURE_DEFAULT, this.modLoc(ITEM_PREFIX + name));
	}

}