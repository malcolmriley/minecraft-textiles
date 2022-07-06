package paragon.minecraft.wilytextiles.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.internal.ContentProvider;
import paragon.minecraft.wilytextiles.tileentities.TEBasket;

/**
 * Holder and initializer class for {@link BlockEntityType} bearing {@link RegistryObject} instances.
 * 
 * @author Malcolm Riley
 *
 */
public class ModTileEntities extends ContentProvider<BlockEntityType<?>> {

	public ModTileEntities() {
		super(ForgeRegistries.BLOCK_ENTITIES, Textiles.MOD_ID);
	}
	
	/* RegistryObject Fields */
	
	public final RegistryObject<BlockEntityType<?>> BASKET = this.add(Names.BASKET, () -> BlockEntityType.Builder.of(TEBasket::new, Textiles.BLOCKS.BASKET.get(), Textiles.BLOCKS.BASKET_STURDY.get()).build(null));
	
	/* Tile Entity Type Names */
	
	/**
	 * Holder class for {@link BlockEntityType} names.
	 * 
	 * @author Malcolm Riley
	 */
	public static class Names {

		private Names() {}

		protected static final String PREFIX = "te_";
		
		public static final String BASKET = PREFIX + ModBlocks.Names.BASKET;

	}

}
