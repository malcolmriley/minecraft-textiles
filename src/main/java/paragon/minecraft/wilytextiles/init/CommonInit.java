package paragon.minecraft.wilytextiles.init;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.internal.Utilities;

/**
 * Holder class for miscellaneous initializers.
 * 
 * @author Malcolm Riley
 */
public class CommonInit {
	
	private CommonInit() { }

	public static void addCompostables() {
		// Low-value compostables
		final float LOW_VALUE_COMPOST = 0.3F;
		Utilities.Misc.streamPresent(
			Textiles.ITEMS.FLAX_PALE,
			Textiles.ITEMS.FLAX_PURPLE,
			Textiles.ITEMS.FLAX_VIBRANT,
			Textiles.ITEMS.FLAX_SEEDS,
			Textiles.ITEMS.PLANT_FIBERS
		).forEach(element -> CommonInit.addCompostable(element, LOW_VALUE_COMPOST));
		
		// High-value compostables
		final float HIGH_VALUE_COMPOST = 0.65F;
		Utilities.Misc.streamPresent(
			Textiles.ITEMS.FLAX_STALKS,
			Textiles.ITEMS.BLOCK_RAW_FIBERS,
			Textiles.ITEMS.BLOCK_RETTED_FIBERS
		).forEach(element -> CommonInit.addCompostable(element, HIGH_VALUE_COMPOST));
		
	}
	
	/* Internal Methods */
	
	protected static void addCompostable(ItemLike target, float compostChance) {
		ComposterBlock.COMPOSTABLES.put(target, compostChance);
	}

}
