package paragon.minecraft.wilytextiles.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import paragon.minecraft.wilytextiles.Textiles;

/**
 * Specialized BlockPadding that breaks when fallen upon from above a threshold height.
 * 
 * @author Malcolm Riley
 */
public class FeatherBlock extends PaddedBlock {
	
	public FeatherBlock() {
		this(PaddedBlock.createPropertiesFrom(MaterialColor.SNOW));
	}

	public FeatherBlock(Properties properties) {
		super(properties);
	}
	
	/* Supertype Override Methods */

	@Override
	public void fallOn(Level world, BlockState state, BlockPos position, Entity entity, float distance) {
		super.fallOn(world, state, position, entity, distance);
		if (distance > Textiles.CONFIG.getFeatherBlockBreakThreshold()) {
			world.destroyBlock(position, true);
		}
	}

	@Override
	protected float getFallDistanceModifier(Level world, BlockPos position, Entity fallen, float distance) {
		return (float) (1.0 - Textiles.CONFIG.getFeatherFallReduction());
	}

	@Override
	protected boolean shouldReduceFall() {
		return Textiles.CONFIG.cushionsReduceFall();
	}

}
