package paragon.minecraft.wilytextiles.blocks;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.init.ModBlocks;

public class RawFiberBlock extends SoakableBlock {

	/* BlockState Properties */
	public static final int MAX_AGE = 2;
	public static final IntegerProperty AGE = BlockStateProperties.AGE_0_2;

	public RawFiberBlock() {
		this(RawFiberBlock.createDefaultProperties());
	}

	public RawFiberBlock(Properties properties) {
		super(properties.tickRandomly());
	}

	/* Public Methods */
	
	/**
	 * Returns the recommended default {@link AbstractBlock.Properties} for the {@link RawFiberBlock} block, used by the no-arg constructor.
	 * 
	 * @return The recommended default {@link AbstractBlock.Properties}.
	 */
	public static AbstractBlock.Properties createDefaultProperties() {
		return AbstractBlock.Properties.create(Material.PLANTS).sound(SoundType.VINE).hardnessAndResistance(0.3F).notSolid().setOpaque(ModBlocks.ALWAYS_FALSE);
	}

	/* Supertype Override Methods */

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos position, Random RNG) {
		if (this.shouldAge(state, world, position, RNG) && state.get(BlockStateProperties.WATERLOGGED).booleanValue()) {
			int age = state.get(RawFiberBlock.AGE).intValue();
			if (age < 2) {
				age += 1;
				world.setBlockState(position, state.with(RawFiberBlock.AGE, age));
			}
		}
	}

	@Override
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
		return super.isReplaceable(state, useContext) && (state.get(AGE).intValue() == 0);
	}

	@Override
	public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(RawFiberBlock.AGE);
	}

	@Override
	protected BlockState createDefaultState() {
		return super.createDefaultState().with(RawFiberBlock.AGE, Integer.valueOf(0));
	}

	/* Internal Methods */

	/**
	 * Method for determining whether a retting fiber bale should grow based purely on configuration-sensitive values (random chance).
	 *
	 * @param state - The {@link BlockState} of the provided raw fiber bale (currently unused)
	 * @param world - The {@link ServerWorld} that the raw fiber bale exists within (currently unused)
	 * @param position - The {@link BlockPos} of the fiber bale
	 * @param random - A reference to a {@link Random} instance
	 * @return Whether the fiber bale should cure based purely on configuration-sensitive values.
	 */
	protected boolean shouldAge(BlockState state, ServerWorld world, BlockPos position, Random random) {
		return random.nextDouble() < Textiles.CONFIG.baleProgressModifier();
	}

}
