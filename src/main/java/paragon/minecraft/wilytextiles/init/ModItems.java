package paragon.minecraft.wilytextiles.init;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import paragon.minecraft.library.ContentProvider;
import paragon.minecraft.library.Utilities;
import paragon.minecraft.library.item.BlockItemSimpleFuel;
import paragon.minecraft.library.item.CheckedBlockNamedItem;
import paragon.minecraft.library.item.ItemSimpleFuel;
import paragon.minecraft.library.item.LazyInitBlockItem;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.blocks.FlaxCropBlock;

/**
 * Holder and initializer class for {@link Item} bearing {@link RegistryObject} instances.
 * 
 * @author Malcolm Riley
 */
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
	public final RegistryObject<Item> FLAXSEED_OIL_BOTTLE = this.bottledItem(Names.FLAXSEED_OIL_BOTTLE);
	public final RegistryObject<Item> FLAXSEED_OIL_BUCKET = this.add(Names.FLAXSEED_OIL_BUCKET, () -> new ItemSimpleFuel(this.defaultWithContainer(Items.BUCKET).maxStackSize(1), Utilities.Time.burnTimeFor(6)));
	public final RegistryObject<Item> WOOD_STAIN = this.bottledItem(Names.WOOD_STAIN);
	public final RegistryObject<Item> WOOD_BLEACH = this.bottledItem(Names.WOOD_BLEACH);
	
	// Block Items
	public final RegistryObject<Item> BLOCK_RAW_FIBERS = this.burnableBlockItem(Textiles.BLOCKS.RAW_FIBERS, 2);
	public final RegistryObject<Item> BLOCK_RETTED_FIBERS = this.burnableBlockItem(Textiles.BLOCKS.RETTED_FIBERS, 2);
	public final RegistryObject<Item> BLOCK_BASKET = this.simpleBlockItem(Textiles.BLOCKS.BASKET);
	public final RegistryObject<Item> BLOCK_BASKET_STURDY = this.simpleBlockItem(Textiles.BLOCKS.BASKET_STURDY, this.defaultProperties().maxStackSize(1));
	public final RegistryObject<Item> BLOCK_PACKED_FEATHERS = this.simpleBlockItem(Textiles.BLOCKS.PACKED_FEATHERS);
	
	public final RegistryObject<Item> FLAX_SEEDS = this.add(Names.FLAX_SEEDS, () -> new CheckedBlockNamedItem(Textiles.BLOCKS.FLAX_CROP.get(), DEFAULT, FlaxCropBlock::canPlaceAt));
	
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
	
	public final RegistryObject<Item> CUSHION_PLAIN = this.simpleBlockItem(Textiles.BLOCKS.CUSHION_PLAIN);
	public final RegistryObject<Item> CUSHION_RED = this.simpleBlockItem(Textiles.BLOCKS.CUSHION_RED);
	public final RegistryObject<Item> CUSHION_ORANGE = this.simpleBlockItem(Textiles.BLOCKS.CUSHION_ORANGE);
	public final RegistryObject<Item> CUSHION_YELLOW = this.simpleBlockItem(Textiles.BLOCKS.CUSHION_YELLOW);
	public final RegistryObject<Item> CUSHION_LIME = this.simpleBlockItem(Textiles.BLOCKS.CUSHION_LIME);
	public final RegistryObject<Item> CUSHION_GREEN = this.simpleBlockItem(Textiles.BLOCKS.CUSHION_GREEN);
	public final RegistryObject<Item> CUSHION_CYAN = this.simpleBlockItem(Textiles.BLOCKS.CUSHION_CYAN);
	public final RegistryObject<Item> CUSHION_LIGHT_BLUE = this.simpleBlockItem(Textiles.BLOCKS.CUSHION_LIGHT_BLUE);
	public final RegistryObject<Item> CUSHION_BLUE = this.simpleBlockItem(Textiles.BLOCKS.CUSHION_BLUE);
	public final RegistryObject<Item> CUSHION_PURPLE = this.simpleBlockItem(Textiles.BLOCKS.CUSHION_PURPLE);
	public final RegistryObject<Item> CUSHION_MAGENTA = this.simpleBlockItem(Textiles.BLOCKS.CUSHION_MAGENTA);
	public final RegistryObject<Item> CUSHION_PINK = this.simpleBlockItem(Textiles.BLOCKS.CUSHION_PINK);
	public final RegistryObject<Item> CUSHION_WHITE = this.simpleBlockItem(Textiles.BLOCKS.CUSHION_WHITE);
	public final RegistryObject<Item> CUSHION_LIGHT_GRAY = this.simpleBlockItem(Textiles.BLOCKS.CUSHION_LIGHT_GRAY);
	public final RegistryObject<Item> CUSHION_GRAY = this.simpleBlockItem(Textiles.BLOCKS.CUSHION_GRAY);
	public final RegistryObject<Item> CUSHION_BLACK = this.simpleBlockItem(Textiles.BLOCKS.CUSHION_BLACK);
	public final RegistryObject<Item> CUSHION_BROWN = this.simpleBlockItem(Textiles.BLOCKS.CUSHION_BROWN);
	
	/* Public Methods */

	
	/**
	 * Returns a {@link Stream} over all cushion-type {@link Block} known to the mod.
	 * <p>
	 * The returned {@link Stream} is first filtered by checking the underlying {@link RegistryObject} for the presence of the {@link Block} (that is, whether the item is actually registered).
	 * 
	 * @return A {@link Stream} of all cushion-type {@link Block}.
	 */
	public Stream<Item> streamCushionItems() {
		return this.filterUnregistered(Stream.of(
			this.CUSHION_PLAIN,
			this.CUSHION_RED,
			this.CUSHION_ORANGE,
			this.CUSHION_YELLOW,
			this.CUSHION_LIME,
			this.CUSHION_GREEN,
			this.CUSHION_CYAN,
			this.CUSHION_LIGHT_BLUE,
			this.CUSHION_BLUE,
			this.CUSHION_PURPLE,
			this.CUSHION_MAGENTA,
			this.CUSHION_PINK,
			this.CUSHION_WHITE,
			this.CUSHION_LIGHT_GRAY,
			this.CUSHION_GRAY,
			this.CUSHION_BLACK,
			this.CUSHION_BROWN
		));
	}
	
	/**
	 * Returns a {@link Stream} over all fabric-type {@link Item} known to the mod.
	 * <p>
	 * The returned {@link Stream} is first filtered by checking the underlying {@link RegistryObject} for the presence of the {@link Item} (that is, whether the item is actually registered).
	 * 
	 * @return A {@link Stream} of all fabric-type {@link Items}.
	 */
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
	
	/**
	 * Generates a new basic {@link Item} using the default properties, with {@link Items#GLASS_BOTTLE} set as a container item.
	 * 
	 * @param name - The registry name for the {@link Item}
	 * @return A {@link RegistryObject} holding the {@link Item}.
	 */
	protected RegistryObject<Item> bottledItem(final String name) {
		return this.simpleItem(name, this.defaultWithContainer(Items.GLASS_BOTTLE));
	}
	
	/**
	 * Generates a new basic {@link Item} using the default properties.
	 * 
	 * @param name - The registry name for the {@link Item}
	 * @return A {@link RegistryObject} holding the {@link Item}.
	 */
	protected RegistryObject<Item> simpleItem(final String name) {
		return this.simpleItem(name, DEFAULT);
	}
	
	/**
	 * Generates a new basic {@link Item} using the provided {@link Item.Properties}.
	 * 
	 * @param name - The registry name for the {@link Item}
	 * @param properties - The {@link Item.Properties} to use for the {@link Item}
	 * @return A {@link RegistryObject} holding the {@link Item}.
	 */
	protected RegistryObject<Item> simpleItem(final String name, final Item.Properties properties) {
		return this.add(name, () -> new Item(properties));
	}
	
	/**
	 * Creates a {@link RegistryObject} that will instantiate a new {@link BlockItem} lazily using the provided {@link RegistryObject} and default properties.
	 * <p>
	 * The registry name of the resulting {@link BlockItem} will use the id of the {@link Block} held by the provided {@link RegistryObject}.
	 * 
	 * @param block - A {@link RegistryObject} holding the target {@link Block}, to be queried lazily
	 * @return A {@link RegistryObject} holding the requisite {@link BlockItem}.
	 */
	protected RegistryObject<Item> simpleBlockItem(RegistryObject<Block> block) {
		return this.simpleBlockItem(block, this.DEFAULT);
	}
	
	/**
	 * Creates a {@link RegistryObject} that will instantiate a new {@link BlockItemSimpleFuel} lazily using the provided item-count burn time and default properties.
	 * 
	 * @param block - A {@link RegistryObject} holding the target {@link Block}, to be queried lazily
	 * @param burnTime - The number of items that the {@link BlockItem} can smelt
	 * @return A {@link RegistryObject} holding the requisite {@link BlockItem}.
	 */
	protected RegistryObject<Item> burnableBlockItem(RegistryObject<Block> block, int burnTime) {
		return this.add(block.getId().getPath(), () -> new BlockItemSimpleFuel(block.get(), DEFAULT, Utilities.Time.burnTimeFor(burnTime)));
	}
	
	/**
	 * Creates a {@link RegistryObject} that will instantiate a new {@link BlockItem} lazily using the provided {@link RegistryObject}.
	 * <p>
	 * The registry name of the resulting {@link BlockItem} will use the id of the {@link Block} held by the provided {@link RegistryObject}.
	 * 
	 * @param block - A {@link RegistryObject} holding the target {@link Block}, to be queried lazily
	 * @param properties - The {@link Item.Properties} to use for the resulting {@link BlockItem}
	 * @return A {@link RegistryObject} holding the requisite {@link BlockItem}.
	 */
	protected RegistryObject<Item> simpleBlockItem(RegistryObject<Block> block, Item.Properties properties) {
		return this.simpleBlockItem(block.getId().getPath(), block, properties);
	}
	
	/**
	 * Creates a {@link RegistryObject} that will instantiate a new {@link BlockItem} lazily using the provided {@link Supplier}
	 * 
	 * @param name - The registry name for the {@link BlockItem}
	 * @param blockSupplier - A supplier of the target {@link Block}, to be called lazily
	 * @param properties - The {@link Item.Properties} to use for the resulting {@link BlockItem}
	 * @return A {@link RegistryObject} holding the requisite {@link BlockItem}.
	 */
	protected RegistryObject<Item> simpleBlockItem(String name, Supplier<Block> blockSupplier, Item.Properties properties) {
		return this.add(name, () -> new LazyInitBlockItem(blockSupplier, properties));
	}
	
	/**
	 * Returns a new, default {@link Item.Properties} instance via {@link #defaultProperties()} and sets the container item to the provided {@link IItemProvider}.
	 * 
	 * @param container - The {@link IItemProvider} to use as a container.
	 * @return A new, default {@link Item.Properties} instance with the provided container item.
	 */
	private Item.Properties defaultWithContainer(IItemProvider container) {
		return this.defaultProperties().containerItem(container.asItem());
	}
	
	/**
	 * @return A new, default {@link Item.Properties} instance using the appropriate {@link ItemGroup} for the mod.
	 */
	private Item.Properties defaultProperties() {
		return new Item.Properties().group(GROUP);
	}
	
	/* Item Names */
	
	/**
	 * Holder class for {@link Item} registry names.
	 * 
	 * @author Malcolm Riley
	 */
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
		public static final String FLAXSEED_OIL_BOTTLE = "flaxseed_oil_bottle";
		public static final String FLAXSEED_OIL_BUCKET = "flaxseed_oil_bucket";
		public static final String WOOD_STAIN = "wood_stain";
		public static final String WOOD_BLEACH = "wood_bleach";

	}

}
