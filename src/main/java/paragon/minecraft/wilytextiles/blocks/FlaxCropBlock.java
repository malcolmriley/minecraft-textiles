package paragon.minecraft.wilytextiles.blocks;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.init.ModBlocks;

/**
 * Subclass implementation of {@link TallCropBlock} to be used for the Flax crop.
 * 
 * @author Malcolm Riley
 */
public class FlaxCropBlock extends TallCropBlock {
	
	public FlaxCropBlock() {
		this(FlaxCropBlock.createDefaultProperties());
	}

	public FlaxCropBlock(Properties builder) {
		super(builder);
	}
	
	/* Public Methods */
	
	/**
	 * Predicate method intended to be used to determine whether the flax crop {@link BlockItem} (the seeds) can be placed at the current position.
	 * <p>
	 * This method is needed separately from the block-staying check methods because the top block of the tall block relies on being able to STAY on another flax block, 
	 * but shouldn't be able to be planted there.
	 * 
	 * @param context - The {@link BlockItemUseContext} to use for the check
	 * @return Whether the Flax Crop {@link BlockItem} should be able to be placed.
	 */
	public static boolean canPlaceAt(BlockItemUseContext context) {
		return !context.getWorld().getBlockState(context.getPos().down()).isIn(Textiles.BLOCKS.FLAX_CROP.get());
	}
	
	/**
	 * Returns the recommended default {@link AbstractBlock.Properties} for the {@link FlaxCropBlock} block, used by the no-arg constructor.
	 * 
	 * @return The recommended default {@link AbstractBlock.Properties}.
	 */
	public static AbstractBlock.Properties createDefaultProperties() {
		return AbstractBlock.Properties.create(Material.PLANTS).sound(SoundType.PLANT).hardnessAndResistance(0.45F).notSolid().setOpaque(ModBlocks.ALWAYS_FALSE);
	}
	
	/* Supertype Override Methods */

	@Override
	protected boolean shouldGrow(ServerWorld world, BlockState state, BlockPos position, Random RNG) {
		return this.isLightAdequate(world, position) && RNG.nextDouble() < Textiles.CONFIG.flaxGrowthModifier();
	}
	
	/* Internal Methods */
	
	/**
	 * Method to check whether the light value at the provided {@link BlockPos} is adequate for flax growth.
	 * 
	 * @param world - The {@link ServerWorld} that should be used to query light values
	 * @param position - The {@link BlockPos} to examine the light value of
	 * @return Whether the light value at the provided {@link BlockPos} is adequate for flax growth.
	 */
	protected boolean isLightAdequate(ServerWorld world, BlockPos position) {
		return world.getLight(position) >= Textiles.CONFIG.flaxMinLight();
	}

}
