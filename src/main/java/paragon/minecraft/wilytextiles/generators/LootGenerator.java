package paragon.minecraft.wilytextiles.generators;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;

import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.BinomialRange;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.util.ResourceLocation;
import paragon.minecraft.library.datageneration.LootHelper;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.blocks.SoakableBlock;
import paragon.minecraft.wilytextiles.blocks.TallCrop;

final class LootGenerator extends LootHelper {

	// Tool Predicates
	private static final ILootCondition.IBuilder SILK_TOUCH = MatchTool.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))));
	private static final ILootCondition.IBuilder NO_SILK_TOUCH = SILK_TOUCH.inverted();

	LootGenerator(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void addLootTables(Consumer<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootParameterSet>> registrar) {
		registrar.accept(Pair.of(BlockLootGenerator::new, LootParameterSets.BLOCK));
		registrar.accept(Pair.of(BlockLootExtensions::new, LootParameterSets.BLOCK));
	}

	/* Block loot tables */

	private static final class BlockLootGenerator extends LootHelper.BlockLoot {

		@Override
		public void addTables() {
			// Fiber Bales
			final LootTable.Builder baleBuilder = LootTable.builder();
			for (int count = 1; count <= SoakableBlock.MAX_COUNT; count += 1) {
				baleBuilder.addLootPool(LootPool.builder()
					.acceptCondition(this.countAndAge(count, 0))
					.rolls(ConstantRange.of(count))
					.addEntry(ItemLootEntry.builder(Textiles.BLOCKS.RAW_FIBERS.get())));
				baleBuilder.addLootPool(LootPool.builder()
					.acceptCondition(this.countAndAge(count, 1))
					.rolls(ConstantRange.of(count))
					.addEntry(ItemLootEntry.builder(Textiles.BLOCKS.RAW_FIBERS.get()).weight(12))
					.addEntry(ItemLootEntry.builder(Textiles.ITEMS.TWINE.get()).weight(1).quality(3)));
				baleBuilder.addLootPool(LootPool.builder()
					.acceptCondition(this.countAndAge(count, 2))
					.rolls(RandomValueRange.of(count, count * 1.5F))
					.bonusRolls(1.0F, 2.0F)
					.addEntry(ItemLootEntry.builder(Textiles.ITEMS.TWINE.get())));
			}
			this.registerLootTable(Textiles.BLOCKS.RAW_FIBERS.get(), baleBuilder);

			// Basket Block
			this.registerLootTable(Textiles.BLOCKS.BASKET.get(), BlockLootTables.droppingWithName(Textiles.BLOCKS.BASKET.get()));

			// Flax Crop
			final LootTable.Builder flaxBuilder = LootTable.builder();
			flaxBuilder.addLootPool(LootPool.builder()
				.rolls(BinomialRange.of(1, 0.2F))
				.addEntry(ItemLootEntry.builder(Textiles.ITEMS.FLAX_SEEDS.get())));

			flaxBuilder.addLootPool(LootPool.builder()
				.acceptCondition(this.tallCropTop(TallCrop.MAX_AGE - 1))
				.rolls(BinomialRange.of(3, 0.65F))
				.bonusRolls(0, 2)
				.addEntry(ItemLootEntry.builder(Textiles.ITEMS.FLAX_SEEDS.get())));

			flaxBuilder.addLootPool(LootPool.builder()
				.acceptCondition(this.tallCropBottom(TallCrop.MAX_AGE - 1))
				.rolls(RandomValueRange.of(1.0F, 3.0F))
				.bonusRolls(0, 1)
				.addEntry(ItemLootEntry.builder(Textiles.ITEMS.FLAX_STALKS.get())));
			flaxBuilder.addLootPool(LootPool.builder()
				.acceptCondition(this.tallCropBottom(TallCrop.MAX_AGE - 1))
				.rolls(BinomialRange.of(1, 0.6F))
				.bonusRolls(0, 1)
				.addEntry(ItemLootEntry.builder(Textiles.ITEMS.FLAX_SEEDS.get())));

			flaxBuilder.addLootPool(LootPool.builder()
				.acceptCondition(this.tallCropTop(TallCrop.MAX_AGE))
				.rolls(BinomialRange.of(1, 0.35F))
				.bonusRolls(0, 1)
				.addEntry(ItemLootEntry.builder(Textiles.ITEMS.FLAX_PALE.get()).weight(10).quality(3))
				.addEntry(ItemLootEntry.builder(Textiles.ITEMS.FLAX_VIBRANT.get()).weight(6).quality(5))
				.addEntry(ItemLootEntry.builder(Textiles.ITEMS.FLAX_PURPLE.get()).weight(1).quality(7)));
			flaxBuilder.addLootPool(LootPool.builder()
				.acceptCondition(this.tallCropTop(TallCrop.MAX_AGE))
				.rolls(BinomialRange.of(2, 0.75F))
				.bonusRolls(1, 2)
				.addEntry(ItemLootEntry.builder(Textiles.ITEMS.FLAX_SEEDS.get()).weight(4))
				.addEntry(ItemLootEntry.builder(Textiles.ITEMS.FLAX_STALKS.get()).weight(1)));

			flaxBuilder.addLootPool(LootPool.builder()
				.acceptCondition(this.tallCropBottom(TallCrop.MAX_AGE))
				.rolls(BinomialRange.of(3, 0.8F))
				.bonusRolls(1, 2)
				.addEntry(ItemLootEntry.builder(Textiles.ITEMS.FLAX_SEEDS.get()).weight(1))
				.addEntry(ItemLootEntry.builder(Textiles.ITEMS.FLAX_STALKS.get()).weight(4)));
			flaxBuilder.addLootPool(LootPool.builder()
				.acceptCondition(this.tallCropBottom(TallCrop.MAX_AGE))
				.rolls(BinomialRange.of(1, 0.1F))
				.bonusRolls(1, 2)
				.addEntry(ItemLootEntry.builder(Textiles.ITEMS.FLAX_PALE.get()).weight(10).quality(3))
				.addEntry(ItemLootEntry.builder(Textiles.ITEMS.FLAX_VIBRANT.get()).weight(6).quality(5))
				.addEntry(ItemLootEntry.builder(Textiles.ITEMS.FLAX_PURPLE.get()).weight(1).quality(7)));
			this.registerLootTable(Textiles.BLOCKS.FLAX_CROP.get(), flaxBuilder);
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return Textiles.BLOCKS.iterateContent();
		}

		/* Internal Methods */

		protected BlockStateProperty.Builder tallCropTop(int age) {
			return this.tallCropProperties(age, false);
		}

		protected BlockStateProperty.Builder tallCropBottom(int age) {
			return this.tallCropProperties(age, true);
		}

		protected BlockStateProperty.Builder tallCropProperties(int age, boolean bottom) {
			return BlockStateProperty.builder(Textiles.BLOCKS.FLAX_CROP.get()).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(TallCrop.AGE, age).withBoolProp(TallCrop.BOTTOM, bottom));
		}

		protected BlockStateProperty.Builder countAndAge(int count, int age) {
			return BlockStateProperty.builder(Textiles.BLOCKS.RAW_FIBERS.get()).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(SoakableBlock.COUNT, count).withIntProp(SoakableBlock.AGE, age));
		}

	}
	
	/* Loot Extensions */
	
	public static class BlockLootExtensions extends BlockLootTables {
		
		public void accept(BiConsumer<ResourceLocation, Builder> consumer) {
			
			/* Additional Grass Drops */
			final LootTable.Builder addedGrassDrops = LootTable.builder()
				.addLootPool(LootPool.builder()
					.acceptCondition(LootGenerator.NO_SILK_TOUCH)
					.rolls(BinomialRange.of(1, 0.08F))
					.bonusRolls(0.5F, 1.5F)
					.addEntry(ItemLootEntry.builder(Textiles.ITEMS.FLAX_SEEDS.get())))
				.addLootPool(LootPool.builder()
					.acceptCondition(LootGenerator.NO_SILK_TOUCH)
					.rolls(BinomialRange.of(2, 0.06F))
					.bonusRolls(0.5F, 1.5F)
					.addEntry(ItemLootEntry.builder(Textiles.ITEMS.PLANT_FIBERS.get()))
			);
			consumer.accept(Textiles.createResource("blocks/added_grass_drops"), addedGrassDrops);
		}
		
	}

}
