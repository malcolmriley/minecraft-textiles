package paragon.minecraft.wilytextiles.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import paragon.minecraft.library.ContentProvider;
import paragon.minecraft.library.Utilities;
import paragon.minecraft.wilytextiles.Textiles;

public class ModItems extends ContentProvider<Item> {

	/* Internal Fields */
	protected static final ItemGroup GROUP = Utilities.Game.createFrom(Textiles.MOD_ID, Textiles.ITEMS.FLAX_STALKS);
	protected static final Item.Properties DEFAULT = new Item.Properties().group(GROUP);

	public ModItems() {
		super(ForgeRegistries.ITEMS, Textiles.MOD_ID);
	}
	
	/* RegistryObject Fields */
	
	public final RegistryObject<Item> WICKER = this.simpleItem(Names.WICKER);
	public final RegistryObject<Item> TWINE = this.simpleItem(Names.TWINE);
	public final RegistryObject<Item> FLAX_STALKS = this.simpleItem(Names.FLAX_STALKS);
	public final RegistryObject<Item> CHAIN_MESH = this.simpleItem(Names.CHAIN_MESH);
	
	/* Internal Methods */
	
	protected RegistryObject<Item> simpleItem(String name) {
		return this.add(name, () -> new Item(DEFAULT));
	}
	
	/* Item Names */
	
	public static class Names {

		private Names() {}

		public static final String WICKER = "wicker";
		public static final String TWINE = "twine";
		public static final String FLAX_STALKS = "flax_stalks";
		public static final String CHAIN_MESH = "chain_mesh";

	}

}
