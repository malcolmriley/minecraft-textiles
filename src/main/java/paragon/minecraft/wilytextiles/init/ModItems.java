package paragon.minecraft.wilytextiles.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import paragon.minecraft.library.BlockItemSimpleFuel;
import paragon.minecraft.library.ContentProvider;
import paragon.minecraft.library.Utilities;
import paragon.minecraft.wilytextiles.Textiles;

public class ModItems extends ContentProvider<Item> {

	/* Internal Fields */
	protected static final ItemGroup GROUP = Utilities.Game.createGroupFrom(Textiles.MOD_ID, () -> Textiles.ITEMS.FLAX_STALKS.get());
	protected static final Item.Properties DEFAULT = new Item.Properties().group(GROUP);

	public ModItems() {
		super(ForgeRegistries.ITEMS, Textiles.MOD_ID);
	}
	
	/* RegistryObject Fields */
	
	// Items
	public final RegistryObject<Item> WICKER = this.simpleItem(Names.WICKER);
	public final RegistryObject<Item> TWINE = this.simpleItem(Names.TWINE);
	public final RegistryObject<Item> FLAX_STALKS = this.simpleItem(Names.FLAX_STALKS);
	public final RegistryObject<Item> FLAX_PALE = this.simpleItem(Names.FLAX_PALE);
	public final RegistryObject<Item> FLAX_VIBRANT = this.simpleItem(Names.FLAX_VIBRANT);
	public final RegistryObject<Item> FLAX_PURPLE = this.simpleItem(Names.FLAX_PURPLE, new Item.Properties().group(GROUP).rarity(Rarity.UNCOMMON));
	public final RegistryObject<Item> CHAIN_MESH = this.simpleItem(Names.CHAIN_MESH);
	public final RegistryObject<Item> PLANT_FIBERS = this.simpleItem(Names.PLANT_FIBERS);
	public final RegistryObject<Item> SILK = this.simpleItem(Names.SILK);
	public final RegistryObject<Item> SILK_WISPS = this.simpleItem(Names.SILK_WISP);
	
	// Block Items
	public final RegistryObject<Item> BLOCK_RETTING_FIBERS = this.add(ModBlocks.Names.RAW_FIBERS, () -> new BlockItemSimpleFuel(Textiles.BLOCKS.RAW_FIBERS.get(), DEFAULT, Utilities.Time.burnTimeFor(2)));
	
	/* Internal Methods */
	
	protected RegistryObject<Item> simpleItem(final String name) {
		return this.simpleItem(name, DEFAULT);
	}
	
	protected RegistryObject<Item> simpleItem(final String name, final Item.Properties properties) {
		return this.add(name, () -> new Item(properties));
	}
	
	/* Item Names */
	
	public static class Names {

		private Names() {}

		public static final String WICKER = "wicker";
		public static final String TWINE = "twine";
		public static final String SILK = "silk";
		public static final String SILK_WISP = "silk_wisps";
		public static final String FLAX_STALKS = "flax_stalks";
		public static final String FLAX_PALE = "flax_flower_pale";
		public static final String FLAX_VIBRANT = "flax_flower_vibrant";
		public static final String FLAX_PURPLE = "flax_flower_purple";
		public static final String CHAIN_MESH = "chain_mesh";
		public static final String PLANT_FIBERS = "plant_fibers";

	}

}
