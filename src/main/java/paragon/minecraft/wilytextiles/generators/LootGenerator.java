package paragon.minecraft.wilytextiles.generators;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import paragon.minecraft.library.datageneration.LootHelper;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.blocks.AxialMultipleBlock;
import paragon.minecraft.wilytextiles.blocks.SoakableBlock;
import paragon.minecraft.wilytextiles.blocks.TallCropBlock;

/**
 * Data generator class for mod loot tables.
 * 
 * @author Malcolm Riley
 */
final class LootGenerator extends LootHelper {

	// Tool Predicates
	private static final LootItemCondition.Builder SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
	private static final LootItemCondition.Builder NO_SILK_TOUCH = SILK_TOUCH.invert();

	LootGenerator(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void addLootTables(Consumer<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootContextParamSet>> registrar) {
		registrar.accept(Pair.of(BlockLootGenerator::new, LootContextParamSets.BLOCK));
		registrar.accept(Pair.of(BlockLootExtensions::new, LootContextParamSets.BLOCK));
	}

	/* Block loot tables */

	private static final class BlockLootGenerator extends BlockLootTables {

		@Override
		public void addTables() {
			// Raw Fiber Bales
			final LootTable.Builder baleBuilder = LootTable.lootTable();
			for (int count = 1; count <= SoakableBlock.MAX_COUNT; count += 1) {
				baleBuilder.withPool(LootPool.lootPool()
					.when(this.count(Textiles.BLOCKS.RAW_FIBERS, count))
					.setRolls(ConstantValue.exactly(count))
					.add(LootItem.lootTableItem(Textiles.BLOCKS.RAW_FIBERS.get())));
			}
			this.addLootFor(Textiles.BLOCKS.RAW_FIBERS, baleBuilder);

			// Retted Fiber Bales
			final LootTable.Builder rettedBales = LootTable.lootTable();
			for (int count = 1; count <= SoakableBlock.MAX_COUNT; count += 1) {
				rettedBales.withPool(LootPool.lootPool()
					.when(this.count(Textiles.BLOCKS.RETTED_FIBERS, count))
					.setRolls(UniformGenerator.between(count, count * 1.5F))
					.setBonusRolls(UniformGenerator.between(1.0F, 2.0F))
					.add(LootItem.lootTableItem(Textiles.ITEMS.TWINE.get())));
			}
			this.addLootFor(Textiles.BLOCKS.RETTED_FIBERS, rettedBales);

			// Basket Blocks
			this.addLootFor(Textiles.BLOCKS.BASKET, BlockLootTables.createNameableBlockEntityTable(Textiles.BLOCKS.BASKET.get()));
			this.addLootFor(Textiles.BLOCKS.BASKET_STURDY, BlockLootTables.dropRetainingInventory(Textiles.BLOCKS.BASKET_STURDY, Textiles.TILE_ENTITIES.BASKET));

			// Flax Crop
			final LootTable.Builder flaxBuilder = LootTable.lootTable();
			flaxBuilder.withPool(LootPool.lootPool()
				.setRolls(BinomialDistributionGenerator.binomial(1, 0.2F))
				.add(LootItem.lootTableItem(Textiles.ITEMS.FLAX_SEEDS.get())));

			flaxBuilder.withPool(LootPool.lootPool()
				.when(this.tallCropTop(TallCropBlock.MAX_AGE - 1))
				.setRolls(BinomialDistributionGenerator.binomial(3, 0.65F))
				.setBonusRolls(UniformGenerator.between(0, 2))
				.add(LootItem.lootTableItem(Textiles.ITEMS.FLAX_SEEDS.get())));

			flaxBuilder.withPool(LootPool.lootPool()
				.when(this.tallCropBottom(TallCropBlock.MAX_AGE - 1))
				.setRolls(UniformGenerator.between(1.0F, 3.0F))
				.setBonusRolls(UniformGenerator.between(0, 1))
				.add(LootItem.lootTableItem(Textiles.ITEMS.FLAX_STALKS.get())));
			flaxBuilder.withPool(LootPool.lootPool()
				.when(this.tallCropBottom(TallCropBlock.MAX_AGE - 1))
				.setRolls(BinomialDistributionGenerator.binomial(1, 0.6F))
				.setBonusRolls(UniformGenerator.between(0, 1))
				.add(LootItem.lootTableItem(Textiles.ITEMS.FLAX_SEEDS.get())));

			flaxBuilder.withPool(LootPool.lootPool()
				.when(this.tallCropTop(TallCropBlock.MAX_AGE))
				.setRolls(BinomialDistributionGenerator.binomial(1, 0.35F))
				.setBonusRolls(UniformGenerator.between(0, 1))
				.add(LootItem.lootTableItem(Textiles.ITEMS.FLAX_PALE.get()).setWeight(10).setQuality(3))
				.add(LootItem.lootTableItem(Textiles.ITEMS.FLAX_VIBRANT.get()).setWeight(6).setQuality(5))
				.add(LootItem.lootTableItem(Textiles.ITEMS.FLAX_PURPLE.get()).setWeight(1).setQuality(7)));
			flaxBuilder.withPool(LootPool.lootPool()
				.when(this.tallCropTop(TallCropBlock.MAX_AGE))
				.setRolls(BinomialDistributionGenerator.binomial(2, 0.75F))
				.setBonusRolls(UniformGenerator.between(1, 2))
				.add(LootItem.lootTableItem(Textiles.ITEMS.FLAX_SEEDS.get()).setWeight(4))
				.add(LootItem.lootTableItem(Textiles.ITEMS.FLAX_STALKS.get()).setWeight(1)));

			flaxBuilder.withPool(LootPool.lootPool()
				.when(this.tallCropBottom(TallCropBlock.MAX_AGE))
				.setRolls(BinomialDistributionGenerator.binomial(3, 0.8F))
				.setBonusRolls(UniformGenerator.between(1, 2))
				.add(LootItem.lootTableItem(Textiles.ITEMS.FLAX_SEEDS.get()).setWeight(1))
				.add(LootItem.lootTableItem(Textiles.ITEMS.FLAX_STALKS.get()).setWeight(4)));
			flaxBuilder.withPool(LootPool.lootPool()
				.when(this.tallCropBottom(TallCropBlock.MAX_AGE))
				.setRolls(BinomialDistributionGenerator.binomial(1, 0.1F))
				.setBonusRolls(UniformGenerator.between(1, 2))
				.add(LootItem.lootTableItem(Textiles.ITEMS.FLAX_PALE.get()).setWeight(10).setQuality(3))
				.add(LootItem.lootTableItem(Textiles.ITEMS.FLAX_VIBRANT.get()).setWeight(6).setQuality(5))
				.add(LootItem.lootTableItem(Textiles.ITEMS.FLAX_PURPLE.get()).setWeight(1).setQuality(7)));
			this.addLootFor(Textiles.BLOCKS.FLAX_CROP, flaxBuilder);
			
			// Packed Feathers
			final LootTable.Builder featherBuilder = LootTable.lootTable().withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(Items.FEATHER))
				.setRolls(ConstantValue.exactly(9)));
			this.addLootFor(Textiles.BLOCKS.PACKED_FEATHERS, featherBuilder);
			
			// Fabric Blocks
			Textiles.BLOCKS.streamFabricBlocks().forEach(this::fabricBlockLoot);
			
			// Cushion Blocks
			Textiles.BLOCKS.streamCushionBlocks().forEach(this::cushionBlockLoot);
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return Textiles.BLOCKS.iterateContent();
		}
		
		protected void cushionBlockLoot(Block target) {
			this.add(target, BlockLoot.createSlabItemTable(target));
		}
		
		protected void fabricBlockLoot(Block target) {
			LootTable.Builder builder = LootTable.lootTable();
			for (int count = AxialMultipleBlock.MIN_COUNT; count <= AxialMultipleBlock.MAX_COUNT; count += 1) {
				builder.withPool(LootPool.lootPool()
					.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(target).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AxialMultipleBlock.COUNT, count)))
					.setRolls(ConstantValue.exactly(count))
					.add(LootItem.lootTableItem(target)));
			}
			this.add(target, builder);
		}

		protected LootItemBlockStatePropertyCondition.Builder tallCropTop(int age) {
			return this.tallCropProperties(age, false);
		}

		protected LootItemBlockStatePropertyCondition.Builder tallCropBottom(int age) {
			return this.tallCropProperties(age, true);
		}

		protected LootItemBlockStatePropertyCondition.Builder tallCropProperties(int age, boolean bottom) {
			return LootItemBlockStatePropertyCondition.hasBlockStateProperties(Textiles.BLOCKS.FLAX_CROP.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TallCropBlock.AGE, age).hasProperty(TallCropBlock.BOTTOM, bottom));
		}

		protected LootItemBlockStatePropertyCondition.Builder count(RegistryObject<Block> block, int count) {
			return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SoakableBlock.COUNT, count));
		}

	}
	
	/* Loot Extensions */
	
	public static class BlockLootExtensions extends BlockLootTables {
		
		public void accept(BiConsumer<ResourceLocation, Builder> consumer) {
			
			/* Additional Grass Drops */
			final LootTable.Builder addedGrassDrops = LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.when(LootGenerator.NO_SILK_TOUCH)
					.setRolls(BinomialDistributionGenerator.binomial(1, 0.08F))
					.setBonusRolls(UniformGenerator.between(0.5F, 1.5F))
					.add(LootItem.lootTableItem(Textiles.ITEMS.FLAX_SEEDS.get())))
				.withPool(LootPool.lootPool()
					.when(LootGenerator.NO_SILK_TOUCH)
					.setRolls(BinomialDistributionGenerator.binomial(2, 0.06F))
					.setBonusRolls(UniformGenerator.between(0.5F, 1.5F))
					.add(LootItem.lootTableItem(Textiles.ITEMS.PLANT_FIBERS.get()))
			);
			consumer.accept(Textiles.createResource("blocks/added_grass_drops"), addedGrassDrops);
		}

		@Override
		public void addTables() {
			
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return () -> null;
		}
		
	}

}
