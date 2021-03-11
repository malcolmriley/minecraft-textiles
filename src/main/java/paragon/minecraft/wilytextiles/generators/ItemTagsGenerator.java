package paragon.minecraft.wilytextiles.generators;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import paragon.minecraft.library.Utilities;
import paragon.minecraft.wilytextiles.Textiles;

final class ItemTagsGenerator extends ItemTagsProvider {

	ItemTagsGenerator(DataGenerator generator, ExistingFileHelper helper, BlockTagsProvider provider) {
		super(generator, provider, Textiles.MOD_ID, helper);
	}

	/* Supertype Override Methods */

	@Override
	protected void registerTags() {
		// Twine and Silk are String subtypes
		final String stringTag = "string";
		final INamedTag<Item> twine = Utilities.Tags.forgeItemTag(stringTag, "twine");
		this.getOrCreateBuilder(twine).add(Textiles.ITEMS.TWINE.get());
		final INamedTag<Item> silk = Utilities.Tags.forgeItemTag(stringTag, "silk");
		this.getOrCreateBuilder(silk).add(Textiles.ITEMS.SILK.get());
		this.getOrCreateBuilder(Tags.Items.STRING).addTag(twine).addTag(silk);
	}

}