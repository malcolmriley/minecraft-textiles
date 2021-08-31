package paragon.minecraft.wilytextiles.blocks;

import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import paragon.minecraft.wilytextiles.Textiles;

/**
 * Specialized BlockPadding that breaks when fallen upon from above a threshold height.
 * 
 * @author Malcolm Riley
 */
public class FeatherBlock extends BlockPadding {
	
	/* Internal Fields */
	protected static final float FALL_DISTANCE_BREAK_THRESHOLD = 0.5F;
	
	public FeatherBlock() {
		this(BlockPadding.createPropertiesFrom(MaterialColor.SNOW));
	}

	public FeatherBlock(Properties properties) {
		super(properties);
	}
	
	/* Supertype Override Methods */

	@Override
	public void onFallenUpon(World world, BlockPos position, Entity entity, float distance) {
		super.onFallenUpon(world, position, entity, distance);
		if (distance > FALL_DISTANCE_BREAK_THRESHOLD) {
			world.destroyBlock(position, true);
		}
	}

	@Override
	protected float getFallDistanceModifier(World world, BlockPos position, Entity fallen, float distance) {
		return (float) (1.0 - Textiles.CONFIG.getFeatherFallReduction());
	}

	@Override
	protected boolean shouldReduceFall() {
		return Textiles.CONFIG.cushionsReduceFall();
	}

}
