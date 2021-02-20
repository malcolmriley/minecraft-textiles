package paragon.minecraft.wilytextiles.generators;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import paragon.minecraft.wilytextiles.Textiles;

final class BlockTagsGenerator extends BlockTagsProvider {

	/* Supertype Override Methods */

	BlockTagsGenerator(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, Textiles.MOD_ID, helper);
	}

	@Override
	protected void registerTags() {

	}

}