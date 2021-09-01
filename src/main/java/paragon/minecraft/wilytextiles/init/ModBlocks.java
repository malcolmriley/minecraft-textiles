package paragon.minecraft.wilytextiles.init;

import java.util.stream.Stream;

import net.minecraft.block.AbstractBlock.IPositionPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import paragon.minecraft.library.ContentProvider;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.blocks.BasketBlock;
import paragon.minecraft.wilytextiles.blocks.CushionBlock;
import paragon.minecraft.wilytextiles.blocks.FabricBlock;
import paragon.minecraft.wilytextiles.blocks.FeatherBlock;
import paragon.minecraft.wilytextiles.blocks.FlaxCropBlock;
import paragon.minecraft.wilytextiles.blocks.RawFiberBlock;
import paragon.minecraft.wilytextiles.blocks.RettedFiberBlock;

/**
 * Holder and initializer class for {@link Block} bearing {@link RegistryObject}.
 * <p>
 * These fields can be accessed via the static {@link ModBlocks} instance {@link Textiles#BLOCKS}.
 * 
 * @author Malcolm Riley
 */
public class ModBlocks extends ContentProvider<Block> {
	
	/** An {@link IPositionPredicate} that always returns {@code FALSE}. */
	public static final IPositionPredicate ALWAYS_FALSE = (state, reader, position) -> false;
	/** An {@link IPositionPredicate} that always returns {@code TRUE}. */
	public static final IPositionPredicate ALWAYS_TRUE = (state, reader, position) -> true;

	public ModBlocks() {
		super(ForgeRegistries.BLOCKS, Textiles.MOD_ID);
	}
	
	public final RegistryObject<Block> RAW_FIBERS = this.add(Names.RAW_FIBERS, RawFiberBlock::new);
	public final RegistryObject<Block> RETTED_FIBERS = this.add(Names.RETTED_FIBERS, RettedFiberBlock::new);
	public final RegistryObject<Block> FLAX_CROP = this.add(Names.FLAX_CROP, FlaxCropBlock::new);
	public final RegistryObject<Block> PACKED_FEATHERS = this.add(Names.PACKED_FEATHERS, FeatherBlock::new);
	public final RegistryObject<Block> BASKET = this.add(Names.BASKET, BasketBlock.Normal::new);
	public final RegistryObject<Block> BASKET_STURDY = this.add(Names.BASKET_STURDY, BasketBlock.KeepInventory::new);
	
	public final RegistryObject<Block> CUSHION_PLAIN = this.cushionBlock(Names.CUSHION_PLAIN, MaterialColor.SAND);
	public final RegistryObject<Block> CUSHION_RED = this.cushionBlock(Names.CUSHION_RED, MaterialColor.RED);
	public final RegistryObject<Block> CUSHION_ORANGE = this.cushionBlock(Names.CUSHION_ORANGE, MaterialColor.ADOBE); // Same MaterialColor as Orange Wool
	public final RegistryObject<Block> CUSHION_YELLOW = this.cushionBlock(Names.CUSHION_YELLOW, MaterialColor.YELLOW);
	public final RegistryObject<Block> CUSHION_LIME = this.cushionBlock(Names.CUSHION_LIME, MaterialColor.LIME);
	public final RegistryObject<Block> CUSHION_GREEN = this.cushionBlock(Names.CUSHION_GREEN, MaterialColor.GREEN);
	public final RegistryObject<Block> CUSHION_CYAN = this.cushionBlock(Names.CUSHION_CYAN, MaterialColor.CYAN);
	public final RegistryObject<Block> CUSHION_LIGHT_BLUE = this.cushionBlock(Names.CUSHION_LIGHT_BLUE, MaterialColor.LIGHT_BLUE);
	public final RegistryObject<Block> CUSHION_BLUE = this.cushionBlock(Names.CUSHION_BLUE, MaterialColor.BLUE);
	public final RegistryObject<Block> CUSHION_PURPLE = this.cushionBlock(Names.CUSHION_PURPLE, MaterialColor.PURPLE);
	public final RegistryObject<Block> CUSHION_MAGENTA = this.cushionBlock(Names.CUSHION_MAGENTA, MaterialColor.MAGENTA);
	public final RegistryObject<Block> CUSHION_PINK = this.cushionBlock(Names.CUSHION_PINK, MaterialColor.PINK);
	public final RegistryObject<Block> CUSHION_WHITE = this.cushionBlock(Names.CUSHION_WHITE, MaterialColor.SNOW); // Same MaterialColor as White Wool
	public final RegistryObject<Block> CUSHION_LIGHT_GRAY = this.cushionBlock(Names.CUSHION_LIGHT_GRAY, MaterialColor.LIGHT_GRAY);
	public final RegistryObject<Block> CUSHION_GRAY = this.cushionBlock(Names.CUSHION_GRAY, MaterialColor.GRAY);
	public final RegistryObject<Block> CUSHION_BLACK = this.cushionBlock(Names.CUSHION_BLACK, MaterialColor.BLACK);
	public final RegistryObject<Block> CUSHION_BROWN = this.cushionBlock(Names.CUSHION_BROWN, MaterialColor.BROWN);

	public final RegistryObject<Block> FABRIC_PLAIN = this.textileBlock(Names.FABRIC_PLAIN, MaterialColor.SAND);
	public final RegistryObject<Block> FABRIC_RED = this.textileBlock(Names.FABRIC_RED, MaterialColor.RED);
	public final RegistryObject<Block> FABRIC_ORANGE = this.textileBlock(Names.FABRIC_ORANGE, MaterialColor.ADOBE); // Same MaterialColor as Orange Wool
	public final RegistryObject<Block> FABRIC_YELLOW = this.textileBlock(Names.FABRIC_YELLOW, MaterialColor.YELLOW);
	public final RegistryObject<Block> FABRIC_LIME = this.textileBlock(Names.FABRIC_LIME, MaterialColor.LIME);
	public final RegistryObject<Block> FABRIC_GREEN = this.textileBlock(Names.FABRIC_GREEN, MaterialColor.GREEN);
	public final RegistryObject<Block> FABRIC_CYAN = this.textileBlock(Names.FABRIC_CYAN, MaterialColor.CYAN);
	public final RegistryObject<Block> FABRIC_LIGHT_BLUE = this.textileBlock(Names.FABRIC_LIGHT_BLUE, MaterialColor.LIGHT_BLUE);
	public final RegistryObject<Block> FABRIC_BLUE = this.textileBlock(Names.FABRIC_BLUE, MaterialColor.BLUE);
	public final RegistryObject<Block> FABRIC_PURPLE = this.textileBlock(Names.FABRIC_PURPLE, MaterialColor.PURPLE);
	public final RegistryObject<Block> FABRIC_MAGENTA = this.textileBlock(Names.FABRIC_MAGENTA, MaterialColor.MAGENTA);
	public final RegistryObject<Block> FABRIC_PINK = this.textileBlock(Names.FABRIC_PINK, MaterialColor.PINK);
	public final RegistryObject<Block> FABRIC_WHITE = this.textileBlock(Names.FABRIC_WHITE, MaterialColor.SNOW); // Same MaterialColor as White Wool
	public final RegistryObject<Block> FABRIC_LIGHT_GRAY = this.textileBlock(Names.FABRIC_LIGHT_GRAY, MaterialColor.LIGHT_GRAY);
	public final RegistryObject<Block> FABRIC_GRAY = this.textileBlock(Names.FABRIC_GRAY, MaterialColor.GRAY);
	public final RegistryObject<Block> FABRIC_BLACK = this.textileBlock(Names.FABRIC_BLACK, MaterialColor.BLACK);
	public final RegistryObject<Block> FABRIC_BROWN = this.textileBlock(Names.FABRIC_BROWN, MaterialColor.BROWN);
	
	/* Internal Methods */
	
	/**
	 * Returns a {@link Stream} over all cushion-type {@link Block} known to the mod.
	 * <p>
	 * The returned {@link Stream} is first filtered by checking the underlying {@link RegistryObject} for the presence of the {@link Block} (that is, whether the item is actually registered).
	 * 
	 * @return A {@link Stream} of all cushion-type {@link Block}.
	 */
	public Stream<Block> streamCushionBlocks() {
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
	 * Returns a {@link Stream} over all fabric-type {@link Block} known to the mod.
	 * <p>
	 * The returned {@link Stream} is first filtered by checking the underlying {@link RegistryObject} for the presence of the {@link Block} (that is, whether the item is actually registered).
	 * 
	 * @return A {@link Stream} of all fabric-type {@link Block}.
	 */
	public Stream<Block> streamFabricBlocks() {
		return this.filterUnregistered(Stream.of(
			Textiles.BLOCKS.FABRIC_PLAIN,
			Textiles.BLOCKS.FABRIC_RED,
			Textiles.BLOCKS.FABRIC_ORANGE,
			Textiles.BLOCKS.FABRIC_YELLOW,
			Textiles.BLOCKS.FABRIC_LIME,
			Textiles.BLOCKS.FABRIC_GREEN,
			Textiles.BLOCKS.FABRIC_CYAN,
			Textiles.BLOCKS.FABRIC_LIGHT_BLUE,
			Textiles.BLOCKS.FABRIC_BLUE,
			Textiles.BLOCKS.FABRIC_PURPLE,
			Textiles.BLOCKS.FABRIC_MAGENTA,
			Textiles.BLOCKS.FABRIC_PINK,
			Textiles.BLOCKS.FABRIC_WHITE,
			Textiles.BLOCKS.FABRIC_LIGHT_GRAY,
			Textiles.BLOCKS.FABRIC_GRAY,
			Textiles.BLOCKS.FABRIC_BLACK,
			Textiles.BLOCKS.FABRIC_BROWN
		));
	}
	
	/**
	 * Convenience method for creating a "textile" {@link Block}.
	 * 
	 * @param name - The registry name for the desired {@link Block}
	 * @param color - The {@link MaterialColor} to use for the {@link Block}
	 * @return A {@link RegistryObject} holding the desired {@link Block}
	 */
	public RegistryObject<Block> textileBlock(String name, MaterialColor color) {
		return this.add(name, () -> new FabricBlock(color, ALWAYS_FALSE));
	}
	
	/**
	 * Convenience method for creating a "cushion" {@link Block}.
	 * 
	 * @param name - The registry name for the desired {@link Block}
	 * @param color - The {@link MaterialColor} to use for the {@link Block}
	 * @return A {@link RegistryObject} holding the desired {@link Block}
	 */
	public RegistryObject<Block> cushionBlock(String name, MaterialColor color) {
		return this.add(name, () -> new CushionBlock(color));
	}
	
	/* Internal Methods */
	
	/* Block Names */
	
	public static class Names {

		private Names() {}

		public static final String RAW_FIBERS = "raw_fibers";
		public static final String RETTED_FIBERS = "retted_fibers";
		public static final String FLAX_CROP = "crop_flax";
		public static final String BASKET = "basket";
		public static final String BASKET_STURDY = "basket_sturdy";
		public static final String PACKED_FEATHERS = "packed_feathers";
		
		public static final String CUSHION_PLAIN = "cushion_plain";
		public static final String CUSHION_RED = "cushion_red";
		public static final String CUSHION_ORANGE = "cushion_orange";
		public static final String CUSHION_YELLOW = "cushion_yellow";
		public static final String CUSHION_LIME = "cushion_lime";
		public static final String CUSHION_GREEN = "cushion_green";
		public static final String CUSHION_CYAN = "cushion_cyan";
		public static final String CUSHION_LIGHT_BLUE = "cushion_light_blue";
		public static final String CUSHION_BLUE = "cushion_blue";
		public static final String CUSHION_PURPLE = "cushion_purple";
		public static final String CUSHION_MAGENTA = "cushion_magenta";
		public static final String CUSHION_PINK = "cushion_pink";
		public static final String CUSHION_WHITE = "cushion_white";
		public static final String CUSHION_LIGHT_GRAY = "cushion_light_gray";
		public static final String CUSHION_GRAY = "cushion_gray";
		public static final String CUSHION_BLACK = "cushion_black";
		public static final String CUSHION_BROWN = "cushion_brown";
		
		public static final String FABRIC_PLAIN = "fabric_plain";
		public static final String FABRIC_RED = "fabric_red";
		public static final String FABRIC_ORANGE = "fabric_orange";
		public static final String FABRIC_YELLOW = "fabric_yellow";
		public static final String FABRIC_LIME = "fabric_lime";
		public static final String FABRIC_GREEN = "fabric_green";
		public static final String FABRIC_CYAN = "fabric_cyan";
		public static final String FABRIC_LIGHT_BLUE = "fabric_light_blue";
		public static final String FABRIC_BLUE = "fabric_blue";
		public static final String FABRIC_PURPLE = "fabric_purple";
		public static final String FABRIC_MAGENTA = "fabric_magenta";
		public static final String FABRIC_PINK = "fabric_pink";
		public static final String FABRIC_WHITE = "fabric_white";
		public static final String FABRIC_LIGHT_GRAY = "fabric_light_gray";
		public static final String FABRIC_GRAY = "fabric_gray";
		public static final String FABRIC_BLACK = "fabric_black";
		public static final String FABRIC_BROWN = "fabric_brown";

	}
}
