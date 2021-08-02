package paragon.minecraft.wilytextiles.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
	
	/* Internal Fields */
	private static final int SHAPE_INSET = 2;
	public static final VoxelShape SHAPE_X_1 = Block.makeCuboidShape(0 + SHAPE_INSET, 0, 0, 16 - SHAPE_INSET, 16.0 / 3.0, 16);
	public static final VoxelShape SHAPE_X_2 = Block.makeCuboidShape(0 + SHAPE_INSET, 0, 0, 16 - SHAPE_INSET, 32.0 / 3.0, 16);
	public static final VoxelShape SHAPE_X_3 = Block.makeCuboidShape(0 + SHAPE_INSET, 0, 0, 16 - SHAPE_INSET, 16, 16);
	
	public static final VoxelShape SHAPE_Z_1 = Block.makeCuboidShape(0, 0, 0 + SHAPE_INSET, 16, 16.0 / 3.0, 16 - SHAPE_INSET);
	public static final VoxelShape SHAPE_Z_2 = Block.makeCuboidShape(0, 0, 0 + SHAPE_INSET, 16, 32.0 / 3.0, 16 - SHAPE_INSET);
	public static final VoxelShape SHAPE_Z_3 = Block.makeCuboidShape(0, 0, 0 + SHAPE_INSET, 16, 16, 16 - SHAPE_INSET);

	public FabricBlock(Properties properties) {
		super(properties);
	}

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

}
