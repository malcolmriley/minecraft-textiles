package paragon.minecraft.wilytextiles.generators;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;

import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.util.ResourceLocation;
import paragon.minecraft.library.datageneration.LootHelper;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.blocks.SoakableBlock;

final class LootGenerator extends LootHelper {

	LootGenerator(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void addLootTables(Consumer<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootParameterSet>> registrar) {
		registrar.accept(Pair.of(BlockLootGenerator::new, LootParameterSets.BLOCK));
	}

	/* Block loot tables */
	
	private static final class BlockLootGenerator extends BlockLootTables {

		@Override
		public void accept(BiConsumer<ResourceLocation, Builder> registrar) {
			// Fiber Bales
			final LootTable.Builder baleBuilder = LootTable.builder();
			for (int count = 1; count <= SoakableBlock.MAX_COUNT; count += 1) {
				baleBuilder.addLootPool(LootPool.builder().acceptCondition(this.countAndAge(count, 0)).rolls(ConstantRange.of(count))
					.addEntry(ItemLootEntry.builder(Textiles.BLOCKS.RAW_FIBERS.get()))
				);
				baleBuilder.addLootPool(LootPool.builder().acceptCondition(this.countAndAge(count, 1)).rolls(ConstantRange.of(count))
					.addEntry(ItemLootEntry.builder(Textiles.BLOCKS.RAW_FIBERS.get()).weight(12))
					.addEntry(ItemLootEntry.builder(Textiles.ITEMS.TWINE.get()).weight(1).quality(3))
				);
				baleBuilder.addLootPool(LootPool.builder().acceptCondition(this.countAndAge(count, 2)).rolls(RandomValueRange.of(count, count * 1.5F))
					.addEntry(ItemLootEntry.builder(Textiles.ITEMS.TWINE.get()))
					.bonusRolls(1.0F, 2.0F)
				);
			}
			registrar.accept(Textiles.BLOCKS.RAW_FIBERS.get().getLootTable(), baleBuilder);
		}
		
		/* Internal Methods */
		
		protected BlockStateProperty.Builder countAndAge(int count, int age) {
			return BlockStateProperty.builder(Textiles.BLOCKS.RAW_FIBERS.get()).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(SoakableBlock.COUNT, count).withIntProp(SoakableBlock.AGE, age));
		}

	}

}
