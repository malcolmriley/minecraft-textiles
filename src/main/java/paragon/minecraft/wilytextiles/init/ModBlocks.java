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

public class ModBlocks extends ContentProvider<Block> {

	public ModBlocks() {
		super(ForgeRegistries.BLOCKS, Textiles.MOD_ID);
	}
	
	public final RegistryObject<Block> RAW_FIBERS = this.add(Names.RAW_FIBERS, () -> new SoakableBlock(AbstractBlock.Properties.create(Material.PLANTS).sound(SoundType.VINE).hardnessAndResistance(0.3F).notSolid().setOpaque((state, reader, position) -> false)));
	
	/* Block Names */
	
	public static class Names {

		private Names() {}

		public static final String RAW_FIBERS = "raw_fibers";

	}
}
