package paragon.minecraft.wilytextiles.generators;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import paragon.minecraft.library.Utilities;
import paragon.minecraft.wilytextiles.Textiles;

/**
 * Data generator class for Item Tags.
 * 
 * @author Malcolm Riley
 */
final class ItemTagsGenerator extends ItemTagsProvider {
	
	/* Constants */
	static final String DOMAIN_MINECRAFT = "minecraft";
	static final String TAG_WOOL = "wool";
	static final String TAG_CUSHION = "cushions";

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
		this.getOrCreateBuilder(Tags.Items.STRING)
			.addTag(twine)
			.addTag(silk);
		
		// Flax seeds are seeds, believe it or not
		final INamedTag<Item> seeds = Utilities.Tags.forgeItemTag("seeds");
		this.getOrCreateBuilder(seeds).add(Textiles.ITEMS.FLAX_SEEDS.get());
		
		// Flax blossoms are flowers
		this.getOrCreateBuilder(ItemTags.SMALL_FLOWERS)
			.add(Textiles.ITEMS.FLAX_PALE.get())
			.add(Textiles.ITEMS.FLAX_VIBRANT.get())
			.add(Textiles.ITEMS.FLAX_PURPLE.get());
		
		// Colored Wool Tags
		this.tagAsWool(Textiles.ITEMS.FABRIC_PLAIN);
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_RED, DyeColor.RED);
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_ORANGE, DyeColor.ORANGE);
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_YELLOW, DyeColor.YELLOW);
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_LIME, DyeColor.LIME);
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_GREEN, DyeColor.GREEN);
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_CYAN, DyeColor.CYAN);
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_LIGHT_BLUE, DyeColor.LIGHT_BLUE);
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_BLUE, DyeColor.BLUE);
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_PURPLE, DyeColor.PURPLE);
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_MAGENTA, DyeColor.MAGENTA);
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_PINK, DyeColor.PINK);
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_WHITE, DyeColor.WHITE);
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_LIGHT_GRAY, DyeColor.LIGHT_GRAY);
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_GRAY, DyeColor.GRAY);
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_BLACK, DyeColor.BLACK);
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_BROWN, DyeColor.BROWN);
		
		// Flax Stalks are Flax Crops
		this.getOrCreateBuilder(Utilities.Tags.forgeItemTag("crops")).add(Textiles.ITEMS.FLAX_STALKS.get());
		this.getOrCreateBuilder(Utilities.Tags.forgeItemTag("crops/flax")).add(Textiles.ITEMS.FLAX_STALKS.get());
		
		// Copy grass block tags to items
		this.copy(Utilities.Tags.forgeBlockTag(BlockTagsGenerator.TAG_GRASSES), Utilities.Tags.forgeItemTag(BlockTagsGenerator.TAG_GRASSES));
		
		// Cushion Tagging
		TagsProvider.Builder<Item> cushionTags = this.getOrCreateBuilder(Utilities.Tags.itemTag(modId, TAG_CUSHION));
		Textiles.ITEMS.streamCushionItems().forEach(cushionTags::add);
	}
	
	/* Internal Methods */
	
	protected void tagAsWool(RegistryObject<Item> target) {
		// Some mods use minecraft:wool, and some use forge:wool. Best to generate for both.
		this.getOrCreateBuilder(ItemTags.WOOL).add(target.get());
		this.getOrCreateBuilder(Utilities.Tags.forgeItemTag(TAG_WOOL)).add(target.get());
	}
	
	protected void tagAsColoredWool(RegistryObject<Item> target, DyeColor woolColor) {
		this.tagAsWool(target);
		final String colorName = woolColor.getString();
		this.getOrCreateBuilder(Utilities.Tags.forgeItemTag(TAG_WOOL, colorName)).add(target.get());
		this.getOrCreateBuilder(Utilities.Tags.itemTag(DOMAIN_MINECRAFT, TAG_WOOL, colorName)).add(target.get());
	}
}