package paragon.minecraft.wilytextiles.generators;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import paragon.minecraft.library.datageneration.ItemModelHelper;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.init.ModBlocks;
import paragon.minecraft.wilytextiles.init.ModItems;

final class ItemModelGenerator extends ItemModelHelper {

	ItemModelGenerator(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, Textiles.MOD_ID, helper);
	}

	/* Supertype Override Methods */

	@Override
	protected void registerModels() {
		// Simple Items
		String[] simpleItems = new String[] {
			ModItems.Names.CHAIN_MESH,
			ModItems.Names.FLAX_STALKS,
			ModItems.Names.FLAX_PALE,
			ModItems.Names.FLAX_VIBRANT,
			ModItems.Names.FLAX_PURPLE,
			ModItems.Names.TWINE,
			ModItems.Names.SILK,
			ModItems.Names.SILK_WISP,
			ModItems.Names.WICKER,
			ModItems.Names.PLANT_FIBERS,
			ModItems.Names.FLAX_SEEDS,
			ModItems.Names.WOOD_BLEACH,
			ModItems.Names.WOOD_STAIN
		};
		for (String iterated : simpleItems) {
			this.simpleItem(iterated);
		}
		// Block Items
		this.blockItem(ModBlocks.Names.RAW_FIBERS, BlockStateGenerator.FIBER_TEXTURE_BASE + "_1_0");
		this.blockItem(ModBlocks.Names.BASKET, "basket_upright");
		
		// Fabric Blocks
		String[] fabrics = new String[] {
			ModBlocks.Names.FABRIC_PLAIN,
			ModBlocks.Names.FABRIC_RED,
			ModBlocks.Names.FABRIC_ORANGE,
			ModBlocks.Names.FABRIC_YELLOW,
			ModBlocks.Names.FABRIC_LIME,
			ModBlocks.Names.FABRIC_GREEN,
			ModBlocks.Names.FABRIC_CYAN,
			ModBlocks.Names.FABRIC_LIGHT_BLUE,
			ModBlocks.Names.FABRIC_BLUE,
			ModBlocks.Names.FABRIC_PURPLE,
			ModBlocks.Names.FABRIC_MAGENTA,
			ModBlocks.Names.FABRIC_PINK,
			ModBlocks.Names.FABRIC_WHITE,
			ModBlocks.Names.FABRIC_LIGHT_GRAY,
			ModBlocks.Names.FABRIC_GRAY,
			ModBlocks.Names.FABRIC_BLACK,
			ModBlocks.Names.FABRIC_BROWN
		};
		for (String iterated : fabrics) {
			this.blockItem(iterated, iterated + "_1");
		}
	}

}