package paragon.minecraft.wilytextiles.init;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractBlock.IPositionPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import paragon.minecraft.library.ContentProvider;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.blocks.AxialMultipleBlock;
import paragon.minecraft.wilytextiles.blocks.BlockBasket;
import paragon.minecraft.wilytextiles.blocks.SoakableBlock;
import paragon.minecraft.wilytextiles.blocks.TallCrop;

/**
 * Holder and initializer class for {@link Block} bearing {@link RegistryObject}.
 * <p>
 * These fields can be accessed via the static {@link ModBlocks} instance {@link Textiles#BLOCKS}.
 * 
 * @author Malcolm Riley
 */
public class ModBlocks extends ContentProvider<Block> {
	
	/** An {@link IPositionPredicate} that always returns {@code FALSE}. */
	private final IPositionPredicate ALWAYS_FALSE = (state, reader, position) -> false;

	public ModBlocks() {
		super(ForgeRegistries.BLOCKS, Textiles.MOD_ID);
	}
	
	public final RegistryObject<Block> RAW_FIBERS = this.add(Names.RAW_FIBERS, () -> new SoakableBlock(AbstractBlock.Properties.create(Material.PLANTS).sound(SoundType.VINE).hardnessAndResistance(0.3F).notSolid().setOpaque(ALWAYS_FALSE)));
	public final RegistryObject<Block> FLAX_CROP = this.add(Names.FLAX_CROP, () -> new TallCrop(AbstractBlock.Properties.create(Material.PLANTS).sound(SoundType.PLANT).hardnessAndResistance(0.6F).notSolid().setOpaque(ALWAYS_FALSE)));

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
	
	public final RegistryObject<Block> BASKET = this.add(Names.BASKET, () -> new BlockBasket(AbstractBlock.Properties.create(Material.LEAVES).sound(SoundType.BAMBOO).hardnessAndResistance(0.8F)));
	
	/* Internal Methods */
	
	/**
	 * Convenience method for creating a "textile" {@link Block} - an {@link AxialMultipleBlock} with the {@link Material#WOOL} material and {@link SoundType#CLOTH}, very low hardness, non-solid and non-opaque.
	 * 
	 * @param name - The registry name for the desired {@link Block}
	 * @param color - The {@link MaterialColor} to use for the {@link Block}
	 * @return A {@link RegistryObject} holding the desired {@link Block}
	 */
	public RegistryObject<Block> textileBlock(String name, MaterialColor color) {
		return this.add(name, () -> new AxialMultipleBlock(AbstractBlock.Properties.create(Material.WOOL, color).sound(SoundType.CLOTH).hardnessAndResistance(0.08F).notSolid().setOpaque(ALWAYS_FALSE)));
	}
	
	/* Block Names */
	
	public static class Names {

		private Names() {}

		public static final String RAW_FIBERS = "raw_fibers";
		public static final String FLAX_CROP = "crop_flax";
		public static final String BASKET = "basket";
		
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
