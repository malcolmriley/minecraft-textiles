package paragon.minecraft.wilytextiles.blocks;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import paragon.minecraft.wilytextiles.Textiles;

/**
 * Specialized implementation for the raw flax fiber blocks.
 * 
 * @author Malcolm Riley
 */
public class RawFiberBlock extends SoakableBlock {

	/* BlockState Properties */
	public static final int MAX_AGE = 2;
	public static final IntegerProperty AGE = BlockStateProperties.AGE_2;

	public RawFiberBlock() {
		this(RawFiberBlock.createDefaultProperties());
	}

	public RawFiberBlock(BlockBehaviour.Properties properties) {
		super(properties.randomTicks());
	}

	/* Public Methods */
	
	/**
	 * Returns the recommended default {@link BlockBehaviour.Properties} for the {@link RawFiberBlock} block, used by the no-arg constructor.
	 * 
	 * @return The recommended default {@link BlockBehaviour.Properties}.
	 */
	public static BlockBehaviour.Properties createDefaultProperties() {
		return BlockBehaviour.Properties.of(Material.PLANT).sound(SoundType.CROP).strength(0.3F).noOcclusion();
	}

	/* Supertype Override Methods */

	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos position, Random RNG) {
		boolean isWaterlogged = SoakableBlock.getWaterlogStateFrom(state);
		if (this.shouldAge(state, world, position, RNG) && isWaterlogged) {
			int age = state.getValue(RawFiberBlock.AGE).intValue();
			if (age < 2) {
				age += 1;
				world.setBlockAndUpdate(position, state.setValue(RawFiberBlock.AGE, age));
			}
			else if (Textiles.BLOCKS.RETTED_FIBERS.isPresent()) {
				int count = SoakableBlock.getCountFrom(state);
				final BlockState newState = Textiles.BLOCKS.RETTED_FIBERS.get().defaultBlockState()
					.setValue(SoakableBlock.COUNT, count)
					.setValue(SoakableBlock.WATERLOGGED, isWaterlogged);
				world.setBlockAndUpdate(position, newState);
			}
		}
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
		return super.canBeReplaced(state, useContext) && (state.getValue(AGE).intValue() == 0);
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(RawFiberBlock.AGE);
	}

	@Override
	protected BlockState createDefaultState() {
		return super.createDefaultState().setValue(RawFiberBlock.AGE, Integer.valueOf(0));
	}

	/* Internal Methods */

	/**
	 * Method for determining whether a retting fiber bale should grow based purely on configuration-sensitive values (random chance).
	 *
	 * @param state - The {@link BlockState} of the provided raw fiber bale (currently unused)
	 * @param world - The {@link ServerLevel} that the raw fiber bale exists within (currently unused)
	 * @param position - The {@link BlockPos} of the fiber bale
	 * @param random - A reference to a {@link Random} instance
	 * @return Whether the fiber bale should cure based purely on configuration-sensitive values.
	 */
	protected boolean shouldAge(BlockState state, ServerLevel world, BlockPos position, Random random) {
		return random.nextDouble() < Textiles.CONFIG.baleProgressModifier();
	}

}
