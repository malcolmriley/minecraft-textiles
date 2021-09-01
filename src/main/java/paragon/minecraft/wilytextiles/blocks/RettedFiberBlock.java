package paragon.minecraft.wilytextiles.blocks;

/**
 * Specialized implementation for the thoroughly retted flax fiber bales.
 * 
 * @author Malcolm Riley
 */
public class RettedFiberBlock extends SoakableBlock {
	
	public RettedFiberBlock() {
		this(RawFiberBlock.createDefaultProperties());
	}

	public RettedFiberBlock(Properties properties) {
		super(properties);
	}

}
