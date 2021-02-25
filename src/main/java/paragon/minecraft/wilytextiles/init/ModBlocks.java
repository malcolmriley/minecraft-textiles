package paragon.minecraft.wilytextiles.init;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import paragon.minecraft.library.ContentProvider;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.blocks.SoakableBlock;
import paragon.minecraft.wilytextiles.blocks.TallCrop;

public class ModBlocks extends ContentProvider<Block> {

	public ModBlocks() {
		super(ForgeRegistries.BLOCKS, Textiles.MOD_ID);
	}
	
	public final RegistryObject<Block> RAW_FIBERS = this.add(Names.RAW_FIBERS, () -> new SoakableBlock(AbstractBlock.Properties.create(Material.PLANTS).sound(SoundType.VINE).hardnessAndResistance(0.3F).notSolid().setOpaque((state, reader, position) -> false)));
	public final RegistryObject<Block> FLAX_CROP = this.add(Names.FLAX_CROP, () -> new TallCrop(AbstractBlock.Properties.create(Material.PLANTS).sound(SoundType.PLANT).hardnessAndResistance(0.6F).notSolid()));
	
	/* Block Names */
	
	public static class Names {

		private Names() {}

		public static final String RAW_FIBERS = "raw_fibers";
		public static final String FLAX_CROP = "crop_flax";

	}
}
