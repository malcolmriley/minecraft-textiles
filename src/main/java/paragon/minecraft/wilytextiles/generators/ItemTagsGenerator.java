package paragon.minecraft.wilytextiles.generators;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
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
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_RED, "red");
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_ORANGE, "orange");
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_YELLOW, "yellow");
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_LIME, "lime");
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_GREEN, "green");
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_CYAN, "cyan");
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_LIGHT_BLUE, "light_blue");
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_BLUE, "blue");
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_PURPLE, "purple");
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_MAGENTA, "magenta");
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_PINK, "pink");
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_WHITE, "white");
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_LIGHT_GRAY, "light_gray");
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_GRAY, "gray");
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_BLACK, "black");
		this.tagAsColoredWool(Textiles.ITEMS.FABRIC_BROWN, "brown");
		
		// Flax Stalks are Flax Crops
		this.getOrCreateBuilder(Utilities.Tags.forgeItemTag("crops")).add(Textiles.ITEMS.FLAX_STALKS.get());
		this.getOrCreateBuilder(Utilities.Tags.forgeItemTag("crops/flax")).add(Textiles.ITEMS.FLAX_STALKS.get());
		
		// Copy grass block tags to items
		this.copy(Utilities.Tags.forgeBlockTag(BlockTagsGenerator.TAG_GRASSES), Utilities.Tags.forgeItemTag(BlockTagsGenerator.TAG_GRASSES));
	}
	
	/* Internal Methods */
	
	protected void tagAsWool(RegistryObject<Item> target) {
		// Some mods use minecraft:wool, and some use forge:wool. Best to generate for both.
		this.getOrCreateBuilder(ItemTags.WOOL).add(target.get());
		this.getOrCreateBuilder(Utilities.Tags.forgeItemTag(TAG_WOOL)).add(target.get());
	}
	
	protected void tagAsColoredWool(RegistryObject<Item> target, String coloredWool) {
		this.tagAsWool(target);
		this.getOrCreateBuilder(Utilities.Tags.forgeItemTag(TAG_WOOL, coloredWool)).add(target.get());
		this.getOrCreateBuilder(Utilities.Tags.itemTag(DOMAIN_MINECRAFT, TAG_WOOL, coloredWool)).add(target.get());
	}
}