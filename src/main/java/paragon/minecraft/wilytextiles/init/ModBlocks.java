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

public class ModBlocks extends ContentProvider<Block> {
	
	private final IPositionPredicate ALWAYS_FALSE = (state, reader, position) -> false;

	public ModBlocks() {
		super(ForgeRegistries.BLOCKS, Textiles.MOD_ID);
	}
	
	public final RegistryObject<Block> RAW_FIBERS = this.add(Names.RAW_FIBERS, () -> new SoakableBlock(AbstractBlock.Properties.create(Material.PLANTS).sound(SoundType.VINE).hardnessAndResistance(0.3F).notSolid().setOpaque(ALWAYS_FALSE)));
	public final RegistryObject<Block> FLAX_CROP = this.add(Names.FLAX_CROP, () -> new TallCrop(AbstractBlock.Properties.create(Material.PLANTS).sound(SoundType.PLANT).hardnessAndResistance(0.6F).notSolid().setOpaque(ALWAYS_FALSE)));
	public final RegistryObject<Block> BOLT_RED = this.textileBlock(Names.BOLT_RED, MaterialColor.RED);
	
	public final RegistryObject<Block> BASKET = this.add(Names.BASKET, () -> new BlockBasket(AbstractBlock.Properties.create(Material.LEAVES).sound(SoundType.BAMBOO).hardnessAndResistance(0.8F)));
	
	/* Internal Methods */
	
	public RegistryObject<Block> textileBlock(String name, MaterialColor color) {
		return this.add(name, () -> new AxialMultipleBlock(AbstractBlock.Properties.create(Material.WOOL, color).sound(SoundType.CLOTH).hardnessAndResistance(0.08F).notSolid().setOpaque(ALWAYS_FALSE)));
	}
	
	/* Block Names */
	
	public static class Names {

		private Names() {}

		public static final String RAW_FIBERS = "raw_fibers";
		public static final String FLAX_CROP = "crop_flax";
		public static final String BASKET = "basket";
		public static final String BOLT_RED = "bolt_red";

	}
}
