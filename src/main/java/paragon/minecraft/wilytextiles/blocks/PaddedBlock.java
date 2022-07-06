package paragon.minecraft.wilytextiles.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A block of padding material. Aligned to a particular axis.
 * <p>
 * Entities traversing it sink in slightly, and entities falling upon it receive reduced fall damage and a very limited bounce.
 *
 * @author Malcolm Riley
 */
public abstract class PaddedBlock extends RotatedPillarBlock {

	/* Internal Fields */
	protected static final int SHAPE_OFFSET = 2;
	protected static final VoxelShape PARTIAL_FULL = Block.box(0, 0, 0, 16, 16 - SHAPE_OFFSET, 16);
	protected static final float VELOCITY_REDUCTION = 0.5F;
	protected static final float NOBOUNCE_VELOCITY_REDUCTION = 0.1F;

	public PaddedBlock(Properties properties) {
		super(properties);
	}

	/* Public Methods */

	public static BlockBehaviour.Properties createPropertiesFrom(MaterialColor color) {
		return BlockBehaviour.Properties.of(Material.CLOTH_DECORATION, color).sound(SoundType.WOOL).strength(0.85F, 0.1F);
	}

	/* Supertype Override Methods */

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos position, CollisionContext context) {
		return PARTIAL_FULL;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos position, CollisionContext context) {
		return Shapes.block();
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos position, CollisionContext context) {
		return Shapes.block();
	}

	@Override
	public void fallOn(Level world, BlockState state, BlockPos position, Entity entity, float distance) {
		float distanceModified = this.shouldReduceFall() ? distance * this.getFallDistanceModifier(world, position, entity, distance) : distance;
		super.fallOn(world, state, position, entity, distanceModified);
	}

	@Override
	public void updateEntityAfterFallOn(BlockGetter world, Entity entity) {
		entity.setDeltaMovement(PaddedBlock.calculateLandingVelocity(entity.getDeltaMovement(), entity, entity.isSuppressingBounce(), VELOCITY_REDUCTION, NOBOUNCE_VELOCITY_REDUCTION));
	}

	/* Internal Methods */
	
	protected abstract float getFallDistanceModifier(Level world, BlockPos position, Entity fallen, float distance);
	protected abstract boolean shouldReduceFall();
	
	protected static Vec3 calculateLandingVelocity(Vec3 original, Entity entity, boolean suppressBounce, float normalReduction, float noBounceReduction) {
		float horizontalReduction = suppressBounce ? noBounceReduction : normalReduction;
		float verticalReduction = suppressBounce ? 0.0F : noBounceReduction;
		return original.multiply(original.x * horizontalReduction, -verticalReduction, original.z * horizontalReduction);
	}
	
	protected static Direction.Axis getAxisFrom(BlockState state) {
		return state.getValue(PaddedBlock.AXIS);
	}

}
