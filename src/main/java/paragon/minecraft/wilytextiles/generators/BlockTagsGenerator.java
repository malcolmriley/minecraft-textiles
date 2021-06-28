package paragon.minecraft.wilytextiles.generators;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraftforge.common.data.ExistingFileHelper;
import paragon.minecraft.library.Utilities;
import paragon.minecraft.wilytextiles.Textiles;

final class BlockTagsGenerator extends BlockTagsProvider {

	/* Supertype Override Methods */
	
	static final String TAG_GRASSES = "grass";

	BlockTagsGenerator(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, Textiles.MOD_ID, helper);
	}

	@Override
	protected void registerTags() {
		INamedTag<Block> grasses = Utilities.Tags.forgeBlockTag(TAG_GRASSES);
		this.getOrCreateBuilder(grasses).add(Blocks.GRASS, Blocks.TALL_GRASS);
	}

}