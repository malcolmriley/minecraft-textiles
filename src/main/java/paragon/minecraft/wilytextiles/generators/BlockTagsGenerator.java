package paragon.minecraft.wilytextiles.generators;

import javax.annotation.Nullable;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.internal.Utilities;

/**
 * Data Generator class for Block tags.
 * 
 * @author Malcolm Riley
 */
final class BlockTagsGenerator extends BlockTagsProvider {

	/* Supertype Override Methods */
	
	static final String TAG_GRASSES = "grass";

	BlockTagsGenerator(DataGenerator generator, @Nullable ExistingFileHelper helper) {
		super(generator, Textiles.MOD_ID, helper);
	}

	@Override
	protected void addTags() {
		TagKey<Block> grasses = Utilities.Tags.forgeBlockTag(TAG_GRASSES);
		this.tag(grasses).add(Blocks.GRASS, Blocks.TALL_GRASS);
	}

	@Override
	public String getName() {
		return "Wily Textiles Block Tags Generator";
	}

}