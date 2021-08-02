package paragon.minecraft.wilytextiles.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

/**
 * Specific implementation of {@link AxialMultipleBlock} for use by Fabric blocks.
 * 
 * @author Malcolm Riley
 */
public class FabricBlock extends AxialMultipleBlock {
	
	/* Constants */
	private static final float DEFAULT_HARDNESS = 0.08F;
	
	/* Internal Fields */
	private static final int SHAPE_INSET = 2;
	public static final VoxelShape SHAPE_X_1 = Block.makeCuboidShape(0 + SHAPE_INSET, 0, 0, 16 - SHAPE_INSET, 16.0 / 3.0, 16);
	public static final VoxelShape SHAPE_X_2 = Block.makeCuboidShape(0 + SHAPE_INSET, 0, 0, 16 - SHAPE_INSET, 32.0 / 3.0, 16);
	public static final VoxelShape SHAPE_X_3 = Block.makeCuboidShape(0 + SHAPE_INSET, 0, 0, 16 - SHAPE_INSET, 16, 16);
	
	public static final VoxelShape SHAPE_Z_1 = Block.makeCuboidShape(0, 0, 0 + SHAPE_INSET, 16, 16.0 / 3.0, 16 - SHAPE_INSET);
	public static final VoxelShape SHAPE_Z_2 = Block.makeCuboidShape(0, 0, 0 + SHAPE_INSET, 16, 32.0 / 3.0, 16 - SHAPE_INSET);
	public static final VoxelShape SHAPE_Z_3 = Block.makeCuboidShape(0, 0, 0 + SHAPE_INSET, 16, 16, 16 - SHAPE_INSET);

	public FabricBlock(final Properties properties) {
		super(properties);
	}
	
	public FabricBlock(final MaterialColor color, final IPositionPredicate opacityPredicate) {
		this(FabricBlock.createPropertiesFrom(color, opacityPredicate));
	}
	
	/* Supertype Override Methods */

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos position, ISelectionContext context) {
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
	 * @param opacityPredicate - An opacity checking {@link IPositionPredicate}
	 * @return A suitable {@link AbstractBlock.Properties} instance to use.
	 */
	protected static AbstractBlock.Properties createPropertiesFrom(final MaterialColor color, final IPositionPredicate opacityPredicate) {
		return AbstractBlock.Properties.create(Material.CARPET, color).sound(SoundType.CLOTH).hardnessAndResistance(DEFAULT_HARDNESS).setOpaque(opacityPredicate);
	}

}
