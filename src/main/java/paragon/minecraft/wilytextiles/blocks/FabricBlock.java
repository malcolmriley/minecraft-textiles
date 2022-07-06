package paragon.minecraft.wilytextiles.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Specific implementation of {@link AxialMultipleBlock} for use by Fabric blocks.
 *
 * @author Malcolm Riley
 */
public class FabricBlock extends AxialMultipleBlock {

	/* Constants */
	private static final float DEFAULT_HARDNESS = 0.08F;
	private static final int FIRE_ENCOURAGEMENT = 30; // Uses wool block "encouragement" value from FireBlock class (referred to as "spread speed" by current mappings)
	private static final int FIRE_FLAMMABILITY = 60; // Uses wool block "flammability" value from FireBlock class

	/* Internal Fields */
	private static final int SHAPE_INSET = 2;
	public static final VoxelShape SHAPE_X_1 = Block.box(0 + SHAPE_INSET, 0, 0, 16 - SHAPE_INSET, 16.0 / 3.0, 16);
	public static final VoxelShape SHAPE_X_2 = Block.box(0 + SHAPE_INSET, 0, 0, 16 - SHAPE_INSET, 32.0 / 3.0, 16);
	public static final VoxelShape SHAPE_X_3 = Block.box(0 + SHAPE_INSET, 0, 0, 16 - SHAPE_INSET, 16, 16);

	public static final VoxelShape SHAPE_Z_1 = Block.box(0, 0, 0 + SHAPE_INSET, 16, 16.0 / 3.0, 16 - SHAPE_INSET);
	public static final VoxelShape SHAPE_Z_2 = Block.box(0, 0, 0 + SHAPE_INSET, 16, 32.0 / 3.0, 16 - SHAPE_INSET);
	public static final VoxelShape SHAPE_Z_3 = Block.box(0, 0, 0 + SHAPE_INSET, 16, 16, 16 - SHAPE_INSET);

	public FabricBlock(final Properties properties) {
		super(properties);
	}

	public FabricBlock(final MaterialColor color, final StatePredicate alwaysFalse) {
		this(FabricBlock.createPropertiesFrom(color, alwaysFalse));
	}

	/* Supertype Override Methods */

	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return FIRE_FLAMMABILITY;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return FIRE_ENCOURAGEMENT;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos position, CollisionContext context) {
		int count = AxialMultipleBlock.getCountFrom(state);
		Axis axis = AxialMultipleBlock.getAxisFrom(state);
		switch (count) {
			case 9:
			case 8:
			case 7:
				return axis.equals(Axis.X) ? SHAPE_X_3 : SHAPE_Z_3;
			case 6:
			case 5:
			case 4:
				return axis.equals(Axis.X) ? SHAPE_X_2 : SHAPE_Z_2;
		}
		return axis.equals(Axis.X) ? SHAPE_X_1 : SHAPE_Z_1;
	}

	/* Internal Methods */

	/**
	 * Method to create default {@link AbstractBlock.Properties} for the {@link FabricBlock} using the provided parameters.
	 * <p>
	 * Sets the material to {@link Material#CARPET} (for flammability flag and fluid response), the sound to {@link SoundType#CLOTH},
	 * sets the hardness and resistance to {@link #DEFAULT_HARDNESS}, as well as setting the block to be non-solid.
	 *
	 * @param color - The {@link MaterialColor} to use for the {@link FabricBlock}
	 * @param opacityPredicate - An opacity checking {@link StatePredicate}
	 * @return A suitable {@link BlockBehaviour.Properties} instance to use.
	 */
	protected static Properties createPropertiesFrom(final MaterialColor color, final StatePredicate opacityPredicate) {
		return Properties.of(Material.CLOTH_DECORATION, color).sound(SoundType.WOOL).explosionResistance(DEFAULT_HARDNESS).isViewBlocking(opacityPredicate);
	}

}
