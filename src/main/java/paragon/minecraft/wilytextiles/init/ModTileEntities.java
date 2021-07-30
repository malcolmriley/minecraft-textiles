package paragon.minecraft.wilytextiles.init;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import paragon.minecraft.library.ContentProvider;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.tileentities.TEBasket;

/**
 * Holder and initializer class for {@link TileEntityType} bearing {@link RegistryObject} instances.
 * 
 * @author Malcolm Riley
 *
 */
public class ModTileEntities extends ContentProvider<TileEntityType<?>> {

	public ModTileEntities() {
		super(ForgeRegistries.TILE_ENTITIES, Textiles.MOD_ID);
	}
	
	/* RegistryObject Fields */
	
	public final RegistryObject<TileEntityType<?>> BASKET = this.add(Names.BASKET, () -> TileEntityType.Builder.create(TEBasket::new, Textiles.BLOCKS.BASKET.get()).build(null));
	
	/* Tile Entity Type Names */
	
	/**
	 * Holder class for {@link TileEntityType} names.
	 * 
	 * @author Malcolm Riley
	 */
	public static class Names {

		private Names() {}

		protected static final String PREFIX = "te_";
		
		public static final String BASKET = PREFIX + ModBlocks.Names.BASKET;

	}

}
