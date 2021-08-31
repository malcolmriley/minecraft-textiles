package paragon.minecraft.wilytextiles.blocks;

import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Specialized BlockPadding that breaks when fallen upon from above a threshold height.
 * 
 * @author Malcolm Riley
 */
public class FeatherBlock extends BlockPadding {
	
	/* Internal Fields */
	protected static final float FALL_DISTANCE_BREAK_THRESHOLD = 0.5F;
	protected static final float FALL_REDUCTION = 0.8F;
	
	public FeatherBlock() {
		this(BlockPadding.createPropertiesFrom(MaterialColor.SNOW));
	}

	public FeatherBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void onFallenUpon(World world, BlockPos position, Entity entity, float distance) {
		super.onFallenUpon(world, position, entity, distance);
		if (distance > FALL_DISTANCE_BREAK_THRESHOLD) {
			world.destroyBlock(position, true);
		}
	}

	@Override
	protected float getFallReduction(World world, BlockPos position, Entity fallen, float distance) {
		return FALL_REDUCTION;
	}

}
