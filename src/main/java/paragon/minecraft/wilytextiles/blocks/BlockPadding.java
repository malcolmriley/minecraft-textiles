package paragon.minecraft.wilytextiles.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * A block of padding material. Aligned to a particular axis.
 * <p>
 * Entities traversing it sink in slightly, and entities falling upon it receive reduced fall damage and a very limited bounce.
 *
 * @author Malcolm Riley
 */
public class BlockPadding extends RotatedPillarBlock {

	/* BlockState Properties */
	protected static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

	/* Internal Fields */
	protected static final int SHAPE_OFFSET = 2;
	protected static final VoxelShape SHAPE_FULL = Block.makeCuboidShape(0, 0, 0, 16, 16 - SHAPE_OFFSET, 16);
	protected static final float FALL_REDUCTION = 0.8F;
	protected static final float VELOCITY_REDUCTION = 0.5F;
	protected static final float NOBOUNCE_VELOCITY_REDUCTION = 0.1F;

	public BlockPadding(Properties properties) {
		super(properties);
	}

	/* Public Methods */

	public static AbstractBlock.Properties createPropertiesFrom(MaterialColor color) {
		return AbstractBlock.Properties.create(Material.CARPET, color).sound(SoundType.CLOTH).speedFactor(0.75F).hardnessAndResistance(0.1F);
	}

	/* Supertype Override Methods */

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos position, ISelectionContext context) {
		return SHAPE_FULL;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos position) {
		return VoxelShapes.fullCube();
	}

	@Override
	public VoxelShape getRayTraceShape(BlockState state, IBlockReader reader, BlockPos position, ISelectionContext context) {
		return VoxelShapes.fullCube();
	}

	@Override
	public void onFallenUpon(World world, BlockPos pos, Entity entity, float distance) {
		super.onFallenUpon(world, pos, entity, distance * FALL_REDUCTION);
	}

	@Override
	public void onLanded(IBlockReader world, Entity entity) {
		Vector3d motion = entity.getMotion();
		float horizontalReduction = entity.isSuppressingBounce() ? NOBOUNCE_VELOCITY_REDUCTION : VELOCITY_REDUCTION;
		float verticalReduction = entity.isSuppressingBounce() ? 0.0F : NOBOUNCE_VELOCITY_REDUCTION;
		entity.setMotion(motion.mul(motion.x * horizontalReduction, -verticalReduction, motion.z * horizontalReduction));
	}

	/* Internal Methods */
	
	protected static Direction.Axis getAxisFrom(BlockState state) {
		return state.get(BlockPadding.AXIS);
	}

}
