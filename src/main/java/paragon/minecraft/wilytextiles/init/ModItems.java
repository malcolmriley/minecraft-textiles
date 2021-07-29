package paragon.minecraft.wilytextiles.init;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import paragon.minecraft.library.ContentProvider;
import paragon.minecraft.library.Utilities;
import paragon.minecraft.library.item.BlockItemSimpleFuel;
import paragon.minecraft.library.item.CheckedBlockNamedItem;
import paragon.minecraft.library.item.LazyInitBlockItem;
import paragon.minecraft.wilytextiles.Textiles;

public class ModItems extends ContentProvider<Item> {

	/* Internal Fields */
	protected final ItemGroup GROUP = Utilities.Game.createGroupFrom(Textiles.MOD_ID, () -> Textiles.ITEMS.FLAX_STALKS.get());
	protected final Item.Properties DEFAULT = this.defaultProperties();

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
	public final RegistryObject<Item> FLAX_PURPLE = this.simpleItem(Names.FLAX_PURPLE, this.defaultProperties().rarity(Rarity.UNCOMMON));
	public final RegistryObject<Item> CHAIN_MESH = this.simpleItem(Names.CHAIN_MESH);
	public final RegistryObject<Item> PLANT_FIBERS = this.simpleItem(Names.PLANT_FIBERS);
	public final RegistryObject<Item> SILK = this.simpleItem(Names.SILK);
	public final RegistryObject<Item> SILK_WISPS = this.simpleItem(Names.SILK_WISP);
	public final RegistryObject<Item> WOOD_STAIN = this.bottledItem(Names.WOOD_STAIN);
	public final RegistryObject<Item> WOOD_BLEACH = this.bottledItem(Names.WOOD_BLEACH);
	
	// Block Items
	public final RegistryObject<Item> BLOCK_RETTING_FIBERS = this.add(ModBlocks.Names.RAW_FIBERS, () -> new BlockItemSimpleFuel(Textiles.BLOCKS.RAW_FIBERS.get(), DEFAULT, Utilities.Time.burnTimeFor(2)));
	public final RegistryObject<Item> BLOCK_BASKET = this.simpleBlockItem(Textiles.BLOCKS.BASKET);
	public final RegistryObject<Item> FLAX_SEEDS = this.add(Names.FLAX_SEEDS, () -> new CheckedBlockNamedItem(Textiles.BLOCKS.FLAX_CROP.get(), DEFAULT, context -> !context.getWorld().getBlockState(context.getPos().down()).isIn(Textiles.BLOCKS.FLAX_CROP.get())));
	
	public final RegistryObject<Item> FABRIC_PLAIN = this.simpleBlockItem(Textiles.BLOCKS.FABRIC_PLAIN);
	public final RegistryObject<Item> FABRIC_RED = this.simpleBlockItem(Textiles.BLOCKS.FABRIC_RED);
	public final RegistryObject<Item> FABRIC_ORANGE = this.simpleBlockItem(Textiles.BLOCKS.FABRIC_ORANGE);
	public final RegistryObject<Item> FABRIC_YELLOW = this.simpleBlockItem(Textiles.BLOCKS.FABRIC_YELLOW);
	public final RegistryObject<Item> FABRIC_LIME = this.simpleBlockItem(Textiles.BLOCKS.FABRIC_LIME);
	public final RegistryObject<Item> FABRIC_GREEN = this.simpleBlockItem(Textiles.BLOCKS.FABRIC_GREEN);
	public final RegistryObject<Item> FABRIC_CYAN = this.simpleBlockItem(Textiles.BLOCKS.FABRIC_CYAN);
	public final RegistryObject<Item> FABRIC_LIGHT_BLUE = this.simpleBlockItem(Textiles.BLOCKS.FABRIC_LIGHT_BLUE);
	public final RegistryObject<Item> FABRIC_BLUE = this.simpleBlockItem(Textiles.BLOCKS.FABRIC_BLUE);
	public final RegistryObject<Item> FABRIC_PURPLE = this.simpleBlockItem(Textiles.BLOCKS.FABRIC_PURPLE);
	public final RegistryObject<Item> FABRIC_MAGENTA = this.simpleBlockItem(Textiles.BLOCKS.FABRIC_MAGENTA);
	public final RegistryObject<Item> FABRIC_PINK = this.simpleBlockItem(Textiles.BLOCKS.FABRIC_PINK);
	public final RegistryObject<Item> FABRIC_WHITE = this.simpleBlockItem(Textiles.BLOCKS.FABRIC_WHITE);
	public final RegistryObject<Item> FABRIC_LIGHT_GRAY = this.simpleBlockItem(Textiles.BLOCKS.FABRIC_LIGHT_GRAY);
	public final RegistryObject<Item> FABRIC_GRAY = this.simpleBlockItem(Textiles.BLOCKS.FABRIC_GRAY);
	public final RegistryObject<Item> FABRIC_BLACK = this.simpleBlockItem(Textiles.BLOCKS.FABRIC_BLACK);
	public final RegistryObject<Item> FABRIC_BROWN = this.simpleBlockItem(Textiles.BLOCKS.FABRIC_BROWN);
	
	/* Public Methods */
	
	public Stream<Item> streamFabricItems() {
		return Stream.of(
			this.FABRIC_PLAIN,
			this.FABRIC_RED,
			this.FABRIC_ORANGE,
			this.FABRIC_YELLOW,
			this.FABRIC_LIME,
			this.FABRIC_GREEN,
			this.FABRIC_CYAN,
			this.FABRIC_LIGHT_BLUE,
			this.FABRIC_BLUE,
			this.FABRIC_PURPLE,
			this.FABRIC_MAGENTA,
			this.FABRIC_PINK,
			this.FABRIC_WHITE,
			this.FABRIC_LIGHT_GRAY,
			this.FABRIC_GRAY,
			this.FABRIC_BLACK,
			this.FABRIC_BROWN
		).filter(RegistryObject::isPresent).map(RegistryObject::get);
	}
	
	/* Internal Methods */
	
	protected RegistryObject<Item> bottledItem(final String name) {
		return this.simpleItem(name, this.defaultProperties().containerItem(Items.GLASS_BOTTLE));
	}
	
	protected RegistryObject<Item> simpleItem(final String name) {
		return this.simpleItem(name, DEFAULT);
	}
	
	protected RegistryObject<Item> simpleItem(final String name, final Item.Properties properties) {
		return this.add(name, () -> new Item(properties));
	}
	
	protected RegistryObject<Item> simpleBlockItem(RegistryObject<Block> block) {
		return this.simpleBlockItem(block.getId().getPath(), block);
	}
	
	protected RegistryObject<Item> simpleBlockItem(String name, Supplier<Block> blockSupplier) {
		return this.add(name, () -> new LazyInitBlockItem(blockSupplier, DEFAULT));
	}
	
	private Item.Properties defaultProperties() {
		return new Item.Properties().group(GROUP);
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
		public static final String FLAX_SEEDS = "flax_seeds";
		public static final String CHAIN_MESH = "chain_mesh";
		public static final String PLANT_FIBERS = "plant_fibers";
		public static final String WOOD_STAIN = "wood_stain";
		public static final String WOOD_BLEACH = "wood_bleach";

	}

}
