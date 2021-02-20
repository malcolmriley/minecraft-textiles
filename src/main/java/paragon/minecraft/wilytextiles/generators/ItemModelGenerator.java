package paragon.minecraft.wilytextiles.generators;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import paragon.minecraft.library.datageneration.ItemModelHelper;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.init.ModItems;

final class ItemModelGenerator extends ItemModelHelper {

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

}