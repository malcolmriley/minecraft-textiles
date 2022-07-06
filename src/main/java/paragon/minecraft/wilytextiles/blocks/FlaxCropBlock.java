package paragon.minecraft.wilytextiles.blocks;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
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
	 * @param context - The {@link BlockPlaceContext} to use for the check
	 * @return Whether the Flax Crop {@link BlockItem} should be able to be placed.
	 */
	public static boolean canPlaceAt(BlockPlaceContext context) {
		return !context.getLevel().getBlockState(context.getClickedPos().below()).is(Textiles.BLOCKS.FLAX_CROP.get());
	}
	
	/**
	 * Returns the recommended default {@link BlockBehaviour.Properties} for the {@link FlaxCropBlock} block, used by the no-arg constructor.
	 * 
	 * @return The recommended default {@link BlockBehaviour.Properties}.
	 */
	public static Properties createDefaultProperties() {
		return Properties.of(Material.PLANT).sound(SoundType.CROP).strength(0.45F)
			.noCollission() // The frisson of incorrectly-spelled collision
			.isViewBlocking(ModBlocks.ALWAYS_FALSE);
	}
	
	/* Supertype Override Methods */

	@Override
	protected boolean shouldGrow(ServerLevel world, BlockState state, BlockPos position, Random RNG) {
		return this.isLightAdequate(world, position) && RNG.nextDouble() < Textiles.CONFIG.flaxGrowthModifier();
	}

	@Override
	public boolean isBonemealSuccess(Level level, Random RNG, BlockPos position, BlockState state) {
		return true;
	}
	
	/* Internal Methods */
	
	/**
	 * Method to check whether the light value at the provided {@link BlockPos} is adequate for flax growth.
	 * 
	 * @param world - The {@link ServerLevel} that should be used to query light values
	 * @param position - The {@link BlockPos} to examine the light value of
	 * @return Whether the light value at the provided {@link BlockPos} is adequate for flax growth.
	 */
	protected boolean isLightAdequate(ServerLevel world, BlockPos position) {
		return world.getLightEmission(position) >= Textiles.CONFIG.flaxMinLight();
	}

}
