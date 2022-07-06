package paragon.minecraft.wilytextiles.items;

import java.util.function.Predicate;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import paragon.minecraft.library.item.CheckedBlockItem;

/**
 * Extension of {@link CheckedBlockItem} that restores the behavior of {@link #getDescriptionId()} to that of {@link Item}.
 * <p>
 * The interceding superclass {@link BlockItem} overrides this method so that the description ID of the underlying {@link Block} is returned.
 * This is undesirable in cases where the {@link BlockItem} should have a different visible name than the {@link Block} that they place.
 * <p>
 * For a non-checked version, see the base class {@link ItemNameBlockItem}.
 * 
 * @see {@link ItemNameBlockItem}
 * @author Malcolm Riley
 */
public class ItemNameCheckedBlockItem extends CheckedBlockItem {
	
	/* Internal Fields */
	protected String DESCRIPTION_ID;

	public ItemNameCheckedBlockItem(Block blockIn, Properties properties, Predicate<BlockPlaceContext> checker) {
		super(blockIn, properties, checker);
	}
	
	@Override
	public String getDescriptionId() {
		return this.getOrCreateDescriptionId();
	}

}
