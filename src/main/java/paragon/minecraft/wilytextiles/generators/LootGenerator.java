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
import net.minecraft.item.Items;
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
import net.minecraftforge.fml.RegistryObject;
import paragon.minecraft.library.datageneration.LootHelper;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.blocks.AxialMultipleBlock;
import paragon.minecraft.wilytextiles.blocks.SoakableBlock;
import paragon.minecraft.wilytextiles.blocks.TallCrop;

/**
 * Data generator class for mod loot tables.
 * 
 * @author Malcolm Riley
 */
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
			// Raw Fiber Bales
			final LootTable.Builder baleBuilder = LootTable.builder();
			for (int count = 1; count <= SoakableBlock.MAX_COUNT; count += 1) {
				baleBuilder.addLootPool(LootPool.builder()
					.acceptCondition(this.count(Textiles.BLOCKS.RAW_FIBERS, count))
					.rolls(ConstantRange.of(count))
					.addEntry(ItemLootEntry.builder(Textiles.BLOCKS.RAW_FIBERS.get())));
			}
			this.registerLootTable(Textiles.BLOCKS.RAW_FIBERS.get(), baleBuilder);

			// Retted Fiber Bales
			final LootTable.Builder rettedBales = LootTable.builder();
			for (int count = 1; count <= SoakableBlock.MAX_COUNT; count += 1) {
				rettedBales.addLootPool(LootPool.builder()
					.acceptCondition(this.count(Textiles.BLOCKS.RETTED_FIBERS, count))
					.rolls(RandomValueRange.of(count, count * 1.5F))
					.bonusRolls(1.0F, 2.0F)
					.addEntry(ItemLootEntry.builder(Textiles.ITEMS.TWINE.get())));
			}
			this.registerLootTable(Textiles.BLOCKS.RETTED_FIBERS.get(), rettedBales);

			// Basket Block
			this.registerLootTable(Textiles.BLOCKS.BASKET.get(), BlockLootTables.droppingWithName(Textiles.BLOCKS.BASKET.get()));
			this.registerLootTable(Textiles.BLOCKS.BASKET_STURDY.get(), BlockLootTables.droppingWithContents(Textiles.BLOCKS.BASKET_STURDY.get()));

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
			
			// Fabric Blocks
			this.fabricBlockLoot(Textiles.BLOCKS.FABRIC_PLAIN);
			this.fabricBlockLoot(Textiles.BLOCKS.FABRIC_RED);
			this.fabricBlockLoot(Textiles.BLOCKS.FABRIC_ORANGE);
			this.fabricBlockLoot(Textiles.BLOCKS.FABRIC_YELLOW);
			this.fabricBlockLoot(Textiles.BLOCKS.FABRIC_LIME);
			this.fabricBlockLoot(Textiles.BLOCKS.FABRIC_GREEN);
			this.fabricBlockLoot(Textiles.BLOCKS.FABRIC_CYAN);
			this.fabricBlockLoot(Textiles.BLOCKS.FABRIC_LIGHT_BLUE);
			this.fabricBlockLoot(Textiles.BLOCKS.FABRIC_BLUE);
			this.fabricBlockLoot(Textiles.BLOCKS.FABRIC_PURPLE);
			this.fabricBlockLoot(Textiles.BLOCKS.FABRIC_MAGENTA);
			this.fabricBlockLoot(Textiles.BLOCKS.FABRIC_PINK);
			this.fabricBlockLoot(Textiles.BLOCKS.FABRIC_WHITE);
			this.fabricBlockLoot(Textiles.BLOCKS.FABRIC_LIGHT_GRAY);
			this.fabricBlockLoot(Textiles.BLOCKS.FABRIC_GRAY);
			this.fabricBlockLoot(Textiles.BLOCKS.FABRIC_BLACK);
			this.fabricBlockLoot(Textiles.BLOCKS.FABRIC_BROWN);
			
			// Packed Feathers
			final LootTable.Builder featherBuilder = LootTable.builder().addLootPool(LootPool.builder()
				.addEntry(ItemLootEntry.builder(Items.FEATHER))
				.rolls(ConstantRange.of(9)));
			this.registerLootTable(Textiles.BLOCKS.PACKED_FEATHERS.get(), featherBuilder);
			
			// Cushion Blocks
			Textiles.BLOCKS.streamCushionBlocks().forEach(cushion -> {
				this.registerLootTable(cushion, BlockLoot.droppingSlab(cushion));
			});
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return Textiles.BLOCKS.iterateContent();
		}
		
		protected void fabricBlockLoot(RegistryObject<Block> target) {
			LootTable.Builder builder = LootTable.builder();
			for (int count = AxialMultipleBlock.MIN_COUNT; count <= AxialMultipleBlock.MAX_COUNT; count += 1) {
				builder.addLootPool(LootPool.builder()
					.acceptCondition(BlockStateProperty.builder(target.get()).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(AxialMultipleBlock.COUNT, count)))
					.rolls(ConstantRange.of(count))
					.addEntry(ItemLootEntry.builder(target.get())));
			}
			this.registerLootTable(target.get(), builder);
		}

		protected BlockStateProperty.Builder tallCropTop(int age) {
			return this.tallCropProperties(age, false);
		}

		protected BlockStateProperty.Builder tallCropBottom(int age) {
			return this.tallCropProperties(age, true);
		}

		protected BlockStateProperty.Builder tallCropProperties(int age, boolean bottom) {
			return BlockStateProperty.builder(Textiles.BLOCKS.FLAX_CROP.get()).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(TallCrop.AGE, age).withBoolProp(TallCrop.BOTTOM, bottom));
		}

		protected BlockStateProperty.Builder count(RegistryObject<Block> block, int count) {
			return BlockStateProperty.builder(block.get()).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(SoakableBlock.COUNT, count));
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
