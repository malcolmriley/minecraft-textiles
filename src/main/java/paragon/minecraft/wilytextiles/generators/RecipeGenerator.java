package paragon.minecraft.wilytextiles.generators;

import java.util.Arrays;
import java.util.function.Consumer;

import net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.UpgradeRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;
import paragon.minecraft.library.datageneration.RecipeHelper;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.internal.Utilities;

/**
 * Data generation class for crafting recipes.
 * 
 * @author Malcolm Riley
 */
final class RecipeGenerator extends RecipeHelper {

	public RecipeGenerator(DataGenerator generator) {
		super(generator);
	}

	/* Supertype Override Methods */

	@Override
	public void buildCraftingRecipes(final Consumer<FinishedRecipe> registrar) {
		// Dyes from blossoms
		this.simpleShaplessMulti(registrar, Items.LIGHT_BLUE_DYE, Textiles.ITEMS.FLAX_PALE.get(), 3);
		this.simpleShaplessMulti(registrar, Items.CYAN_DYE, Textiles.ITEMS.FLAX_VIBRANT.get(), 3);
		this.simpleShaplessMulti(registrar, Items.PURPLE_DYE, Textiles.ITEMS.FLAX_PURPLE.get(), 3);
		
		// Raw Plant Fibers
		final TagKey<Item> grassTag = Utilities.Tags.forgeItemTag(BlockTagsGenerator.TAG_GRASSES);
		ShapedRecipeBuilder.shaped(Textiles.ITEMS.PLANT_FIBERS.get())
			.pattern("## ")
			.pattern(" # ")
			.pattern(" ##")
			.define('#', grassTag)
			.unlockedBy(RecipeHelper.criterionName(grassTag), RecipeHelper.has(grassTag))
			.save(registrar);
		
		ShapelessRecipeBuilder.shapeless(Textiles.ITEMS.PLANT_FIBERS.get(), 1)
			.requires(Items.DEAD_BUSH, 4)
			.unlockedBy(RecipeHelper.criterionName(Items.DEAD_BUSH), RecipeHelper.has(Items.DEAD_BUSH))
			.save(registrar, this.nameFromRecipe(Textiles.ITEMS.PLANT_FIBERS.get(), Items.DEAD_BUSH));
		
		// Retting fiber bundles
		ShapedRecipeBuilder.shaped(Textiles.ITEMS.BLOCK_RAW_FIBERS.get())
			.pattern("## ")
			.pattern("###")
			.pattern(" ##")
			.define('#', Textiles.ITEMS.PLANT_FIBERS.get())
			.unlockedBy(RecipeHelper.criterionName(Textiles.ITEMS.PLANT_FIBERS), RecipeHelper.hasItem(Textiles.ITEMS.PLANT_FIBERS))
			.save(registrar, this.nameFromRecipe(Textiles.ITEMS.BLOCK_RAW_FIBERS, Textiles.ITEMS.PLANT_FIBERS));
		
		ShapedRecipeBuilder.shaped(Textiles.ITEMS.BLOCK_RAW_FIBERS.get())
			.pattern("##")
			.pattern("##")
			.define('#', Textiles.ITEMS.FLAX_STALKS.get())
			.unlockedBy(RecipeHelper.criterionName(Textiles.ITEMS.FLAX_STALKS), RecipeHelper.hasItem(Textiles.ITEMS.FLAX_STALKS))
			.save(registrar, this.nameFromRecipe(Textiles.ITEMS.BLOCK_RAW_FIBERS, Textiles.ITEMS.FLAX_STALKS));

		// Wicker
		this.addStickMeshRecipe(Textiles.ITEMS.WICKER, 3, Items.SUGAR_CANE, registrar);
		this.addStickMeshRecipe(Textiles.ITEMS.WICKER, 3, Textiles.ITEMS.FLAX_STALKS.get(), registrar);
		
		// Silk Wisps from Cobwebs
		ShapelessRecipeBuilder.shapeless(Textiles.ITEMS.SILK_WISPS.get(), 9)
			.requires(Items.COBWEB)
			.unlockedBy(RecipeHelper.criterionName(Items.COBWEB), RecipeHelper.has(Items.COBWEB))
			.save(registrar);
		
		// Silk from Silk Wisps
		this.addLoopRecipe(Textiles.ITEMS.SILK, 1, Textiles.ITEMS.SILK_WISPS.get(), registrar);

		// Chain Mesh
		ShapedRecipeBuilder.shaped(Textiles.ITEMS.CHAIN_MESH.get(), 5)
			.pattern("XX")
			.pattern("XX")
			.define('X', Items.CHAIN)
			.unlockedBy(RecipeHelper.criterionName(Items.CHAIN), RecipeProvider.has(Tags.Items.NUGGETS_IRON))
			.save(registrar, this.nameFromRecipe(Textiles.ITEMS.CHAIN_MESH.get(), Items.IRON_NUGGET));

		// Chainmail Armor
		final String chainmailCriterion = RecipeHelper.criterionName(Textiles.ITEMS.CHAIN_MESH);
		final TriggerInstance chainmailTrigger = RecipeProvider.has(Textiles.ITEMS.CHAIN_MESH.get());
		ShapedRecipeBuilder.shaped(Items.CHAINMAIL_HELMET)
			.pattern("###")
			.pattern("# #")
			.define('#', Textiles.ITEMS.CHAIN_MESH.get())
			.unlockedBy(chainmailCriterion, chainmailTrigger)
			.save(registrar, this.nameFromRecipe(Items.CHAINMAIL_HELMET, Textiles.ITEMS.CHAIN_MESH.get()));

		ShapedRecipeBuilder.shaped(Items.CHAINMAIL_CHESTPLATE)
			.pattern("# #")
			.pattern("###")
			.pattern("###")
			.define('#', Textiles.ITEMS.CHAIN_MESH.get())
			.unlockedBy(chainmailCriterion, chainmailTrigger)
			.save(registrar, this.nameFromRecipe(Items.CHAINMAIL_CHESTPLATE, Textiles.ITEMS.CHAIN_MESH.get()));

		ShapedRecipeBuilder.shaped(Items.CHAINMAIL_BOOTS)
			.pattern("# #")
			.pattern("# #")
			.define('#', Textiles.ITEMS.CHAIN_MESH.get())
			.unlockedBy(chainmailCriterion, chainmailTrigger)
			.save(registrar, this.nameFromRecipe(Items.CHAINMAIL_BOOTS, Textiles.ITEMS.CHAIN_MESH.get()));

		ShapedRecipeBuilder.shaped(Items.CHAINMAIL_LEGGINGS)
			.pattern("###")
			.pattern("# #")
			.pattern("# #")
			.define('#', Textiles.ITEMS.CHAIN_MESH.get())
			.unlockedBy(chainmailCriterion, chainmailTrigger)
			.save(registrar, this.nameFromRecipe(Items.CHAINMAIL_LEGGINGS, Textiles.ITEMS.CHAIN_MESH.get()));
		
		// Basket
		ShapedRecipeBuilder.shaped(Textiles.BLOCKS.BASKET.get()) 
			.pattern("#I#")
			.pattern("# #")
			.pattern("###")
			.define('#', Textiles.ITEMS.WICKER.get())
			.define('I', Tags.Items.RODS_WOODEN)
			.unlockedBy(RecipeHelper.criterionName(Textiles.ITEMS.WICKER), RecipeHelper.hasItem(Textiles.ITEMS.WICKER))
			.unlockedBy(RecipeHelper.criterionName(Tags.Items.RODS_WOODEN), RecipeHelper.has(Tags.Items.RODS_WOODEN))
			.save(registrar, this.nameFromRecipe(Textiles.ITEMS.BLOCK_BASKET, Textiles.ITEMS.WICKER));
		
		// Sturdy Basket
		UpgradeRecipeBuilder.smithing(
				Ingredient.of(Textiles.ITEMS.BLOCK_BASKET.get()),
				Ingredient.of(Items.TURTLE_HELMET), 
				Textiles.ITEMS.BLOCK_BASKET_STURDY.get())
			.unlocks(RecipeHelper.criterionName(Textiles.ITEMS.BLOCK_BASKET), RecipeHelper.hasItem(Textiles.ITEMS.BLOCK_BASKET))
			.unlocks(RecipeHelper.criterionName(Items.TURTLE_HELMET), RecipeHelper.has(Items.TURTLE_HELMET))
		.save(registrar, this.nameFromPath(Textiles.ITEMS.BLOCK_BASKET_STURDY.get()));
		
		// String tag override recipes
		final String stringCriterion = RecipeHelper.criterionName(Items.STRING);
		final TriggerInstance stringTrigger = RecipeHelper.has(Tags.Items.STRING);
		
		final String stickCriterion = RecipeHelper.criterionName(Tags.Items.RODS_WOODEN);
		final TriggerInstance stickTrigger = RecipeHelper.has(Tags.Items.RODS_WOODEN);
		ShapedRecipeBuilder.shaped(Items.BOW)
			.pattern(" IS")
			.pattern("I S")
			.pattern(" IS")
			.define('I', Tags.Items.RODS_WOODEN)
			.define('S', Tags.Items.STRING)
			.unlockedBy(stringCriterion, stringTrigger)
			.save(registrar, Textiles.createResource("bow"));
		
		ShapedRecipeBuilder.shaped(Items.CROSSBOW)
			.pattern("I#I")
			.pattern("SPS")
			.pattern(" I ")
			.define('I', Tags.Items.RODS_WOODEN)
			.define('S', Tags.Items.STRING)
			.define('#', Tags.Items.INGOTS_IRON)
			.define('P', Items.TRIPWIRE_HOOK)
			.unlockedBy(stringCriterion, stringTrigger)
			.unlockedBy(stickCriterion, stickTrigger)
			.unlockedBy(RecipeHelper.criterionName(Tags.Items.INGOTS_IRON), RecipeHelper.has(Tags.Items.INGOTS_IRON))
			.unlockedBy(RecipeHelper.criterionName(Items.TRIPWIRE_HOOK), RecipeHelper.has(Items.TRIPWIRE_HOOK))
			.save(registrar, Textiles.createResource("crossbow"));
		
		ShapedRecipeBuilder.shaped(Items.FISHING_ROD)
			.pattern("  I")
			.pattern(" IS")
			.pattern("I S")
			.define('I', Tags.Items.RODS_WOODEN)
			.define('S', Tags.Items.STRING)
			.unlockedBy(stringCriterion, stringTrigger)
			.save(registrar, Textiles.createResource("fishing_rod"));
		
		ShapedRecipeBuilder.shaped(Items.LEAD)
			.pattern("SS ")
			.pattern("SO ")
			.pattern("  S")
			.define('S', Tags.Items.STRING)
			.define('O', Tags.Items.SLIMEBALLS)
			.unlockedBy(RecipeHelper.criterionName(Tags.Items.SLIMEBALLS), RecipeHelper.has(Tags.Items.SLIMEBALLS))
			.save(registrar, Textiles.createResource("lead"));
		
		ShapedRecipeBuilder.shaped(Items.LOOM)
			.pattern("SS")
			.pattern("##")
			.define('S', Tags.Items.STRING)
			.define('#', ItemTags.PLANKS)
			.unlockedBy(stringCriterion, stringTrigger)
			.save(registrar, Textiles.createResource("loom"));
		
		// Plain Fabric from Twine
		this.addLoopRecipe(Textiles.ITEMS.FABRIC_PLAIN, 2, Textiles.ITEMS.TWINE.get(), registrar);
		
		// White Fabric from Silk
		ShapedRecipeBuilder.shaped(Textiles.ITEMS.FABRIC_WHITE.get(), 2)
			.pattern("SSS")
			.pattern("SSS")
			.define('S', Textiles.ITEMS.SILK.get())
			.unlockedBy(RecipeHelper.criterionName(Textiles.ITEMS.TWINE), RecipeHelper.hasItem(Textiles.ITEMS.TWINE))
			.save(registrar, this.nameFromRecipe(Textiles.ITEMS.FABRIC_WHITE, Textiles.ITEMS.SILK));
		
		// Fabrics from Wool
		this.addFabricRecipe(Textiles.ITEMS.FABRIC_RED, Items.RED_WOOL, registrar);
		this.addFabricRecipe(Textiles.ITEMS.FABRIC_ORANGE, Items.ORANGE_WOOL, registrar);
		this.addFabricRecipe(Textiles.ITEMS.FABRIC_YELLOW, Items.YELLOW_WOOL, registrar);
		this.addFabricRecipe(Textiles.ITEMS.FABRIC_LIME, Items.LIME_WOOL, registrar);
		this.addFabricRecipe(Textiles.ITEMS.FABRIC_GREEN, Items.GREEN_WOOL, registrar);
		this.addFabricRecipe(Textiles.ITEMS.FABRIC_CYAN, Items.CYAN_WOOL, registrar);
		this.addFabricRecipe(Textiles.ITEMS.FABRIC_LIGHT_BLUE, Items.LIGHT_BLUE_WOOL, registrar);
		this.addFabricRecipe(Textiles.ITEMS.FABRIC_BLUE, Items.BLUE_WOOL, registrar);
		this.addFabricRecipe(Textiles.ITEMS.FABRIC_PURPLE, Items.PURPLE_WOOL, registrar);
		this.addFabricRecipe(Textiles.ITEMS.FABRIC_MAGENTA, Items.MAGENTA_WOOL, registrar);
		this.addFabricRecipe(Textiles.ITEMS.FABRIC_PINK, Items.PINK_WOOL, registrar);
		this.addFabricRecipe(Textiles.ITEMS.FABRIC_WHITE, Items.WHITE_WOOL, registrar);
		this.addFabricRecipe(Textiles.ITEMS.FABRIC_LIGHT_GRAY, Items.LIGHT_GRAY_WOOL, registrar);
		this.addFabricRecipe(Textiles.ITEMS.FABRIC_GRAY, Items.GRAY_WOOL, registrar);
		this.addFabricRecipe(Textiles.ITEMS.FABRIC_BLACK, Items.BLACK_WOOL, registrar);
		this.addFabricRecipe(Textiles.ITEMS.FABRIC_BROWN, Items.BROWN_WOOL, registrar);
		
		// Dyed Items
		this.addDyeRecipe(Textiles.ITEMS.FABRIC_PLAIN, Tags.Items.DYES_WHITE, Textiles.ITEMS.FABRIC_WHITE, registrar);
		this.addDyeRecipe(Textiles.ITEMS.CUSHION_PLAIN, Tags.Items.DYES_WHITE, Textiles.ITEMS.CUSHION_WHITE, registrar);

		this.addFabricDyeRecipe(Tags.Items.DYES_RED, Textiles.ITEMS.FABRIC_RED, registrar);
		this.addFabricDyeRecipe(Tags.Items.DYES_ORANGE, Textiles.ITEMS.FABRIC_ORANGE, registrar);
		this.addFabricDyeRecipe(Tags.Items.DYES_YELLOW, Textiles.ITEMS.FABRIC_YELLOW, registrar);
		this.addFabricDyeRecipe(Tags.Items.DYES_LIME, Textiles.ITEMS.FABRIC_LIME, registrar);
		this.addFabricDyeRecipe(Tags.Items.DYES_GREEN, Textiles.ITEMS.FABRIC_GREEN, registrar);
		this.addFabricDyeRecipe(Tags.Items.DYES_LIGHT_BLUE, Textiles.ITEMS.FABRIC_LIGHT_BLUE, registrar);
		this.addFabricDyeRecipe(Tags.Items.DYES_BLUE, Textiles.ITEMS.FABRIC_BLUE, registrar);
		this.addFabricDyeRecipe(Tags.Items.DYES_PURPLE, Textiles.ITEMS.FABRIC_PURPLE, registrar);
		this.addFabricDyeRecipe(Tags.Items.DYES_MAGENTA, Textiles.ITEMS.FABRIC_MAGENTA, registrar);
		this.addFabricDyeRecipe(Tags.Items.DYES_PINK, Textiles.ITEMS.FABRIC_PINK, registrar);
		this.addFabricDyeRecipe(Tags.Items.DYES_LIGHT_GRAY, Textiles.ITEMS.FABRIC_LIGHT_GRAY, registrar);
		this.addFabricDyeRecipe(Tags.Items.DYES_GRAY, Textiles.ITEMS.FABRIC_GRAY, registrar);
		this.addFabricDyeRecipe(Tags.Items.DYES_BLACK, Textiles.ITEMS.FABRIC_BLACK, registrar);
		this.addFabricDyeRecipe(Tags.Items.DYES_BROWN, Textiles.ITEMS.FABRIC_BROWN, registrar);

		this.addCushionDyeRecipe(Tags.Items.DYES_RED, Textiles.ITEMS.CUSHION_RED, registrar);
		this.addCushionDyeRecipe(Tags.Items.DYES_ORANGE, Textiles.ITEMS.CUSHION_ORANGE, registrar);
		this.addCushionDyeRecipe(Tags.Items.DYES_YELLOW, Textiles.ITEMS.CUSHION_YELLOW, registrar);
		this.addCushionDyeRecipe(Tags.Items.DYES_LIME, Textiles.ITEMS.CUSHION_LIME, registrar);
		this.addCushionDyeRecipe(Tags.Items.DYES_GREEN, Textiles.ITEMS.CUSHION_GREEN, registrar);
		this.addCushionDyeRecipe(Tags.Items.DYES_LIGHT_BLUE, Textiles.ITEMS.CUSHION_LIGHT_BLUE, registrar);
		this.addCushionDyeRecipe(Tags.Items.DYES_BLUE, Textiles.ITEMS.CUSHION_BLUE, registrar);
		this.addCushionDyeRecipe(Tags.Items.DYES_PURPLE, Textiles.ITEMS.CUSHION_PURPLE, registrar);
		this.addCushionDyeRecipe(Tags.Items.DYES_MAGENTA, Textiles.ITEMS.CUSHION_MAGENTA, registrar);
		this.addCushionDyeRecipe(Tags.Items.DYES_PINK, Textiles.ITEMS.CUSHION_PINK, registrar);
		this.addCushionDyeRecipe(Tags.Items.DYES_LIGHT_GRAY, Textiles.ITEMS.CUSHION_LIGHT_GRAY, registrar);
		this.addCushionDyeRecipe(Tags.Items.DYES_GRAY, Textiles.ITEMS.CUSHION_GRAY, registrar);
		this.addCushionDyeRecipe(Tags.Items.DYES_BLACK, Textiles.ITEMS.CUSHION_BLACK, registrar);
		this.addCushionDyeRecipe(Tags.Items.DYES_BROWN, Textiles.ITEMS.CUSHION_BROWN, registrar);
		
		// Tag-based Wool Recipes
		this.addWoolRecipesFor(DyeColor.RED, Items.RED_BED, Items.RED_BANNER, Items.RED_CARPET, Textiles.ITEMS.CUSHION_RED, registrar);
		this.addWoolRecipesFor(DyeColor.ORANGE, Items.ORANGE_BED, Items.ORANGE_BANNER, Items.ORANGE_CARPET, Textiles.ITEMS.CUSHION_ORANGE, registrar);
		this.addWoolRecipesFor(DyeColor.YELLOW, Items.YELLOW_BED, Items.YELLOW_BANNER, Items.YELLOW_CARPET, Textiles.ITEMS.CUSHION_YELLOW, registrar);
		this.addWoolRecipesFor(DyeColor.LIME, Items.LIME_BED, Items.LIME_BANNER, Items.LIME_CARPET, Textiles.ITEMS.CUSHION_LIME, registrar);
		this.addWoolRecipesFor(DyeColor.GREEN, Items.GREEN_BED, Items.GREEN_BANNER, Items.GREEN_CARPET, Textiles.ITEMS.CUSHION_GREEN, registrar);
		this.addWoolRecipesFor(DyeColor.CYAN, Items.CYAN_BED, Items.CYAN_BANNER, Items.CYAN_CARPET, Textiles.ITEMS.CUSHION_CYAN, registrar);
		this.addWoolRecipesFor(DyeColor.LIGHT_BLUE, Items.LIGHT_BLUE_BED, Items.LIGHT_BLUE_BANNER, Items.LIGHT_BLUE_CARPET, Textiles.ITEMS.CUSHION_LIGHT_BLUE, registrar);
		this.addWoolRecipesFor(DyeColor.BLUE, Items.BLUE_BED, Items.BLUE_BANNER, Items.BLUE_CARPET, Textiles.ITEMS.CUSHION_BLUE, registrar);
		this.addWoolRecipesFor(DyeColor.PURPLE, Items.PURPLE_BED, Items.PURPLE_BANNER, Items.PURPLE_CARPET, Textiles.ITEMS.CUSHION_PURPLE, registrar);
		this.addWoolRecipesFor(DyeColor.MAGENTA, Items.MAGENTA_BED, Items.MAGENTA_BANNER, Items.MAGENTA_CARPET, Textiles.ITEMS.CUSHION_MAGENTA, registrar);
		this.addWoolRecipesFor(DyeColor.PINK, Items.PINK_BED, Items.PINK_BANNER, Items.PINK_CARPET, Textiles.ITEMS.CUSHION_PINK, registrar);
		this.addWoolRecipesFor(DyeColor.WHITE, Items.WHITE_BED, Items.WHITE_BANNER, Items.WHITE_CARPET, Textiles.ITEMS.CUSHION_WHITE, registrar);
		this.addWoolRecipesFor(DyeColor.LIGHT_GRAY, Items.LIGHT_GRAY_BED, Items.LIGHT_GRAY_BANNER, Items.LIGHT_GRAY_CARPET, Textiles.ITEMS.CUSHION_LIGHT_GRAY, registrar);
		this.addWoolRecipesFor(DyeColor.GRAY, Items.GRAY_BED, Items.GRAY_BANNER, Items.GRAY_CARPET, Textiles.ITEMS.CUSHION_GRAY, registrar);
		this.addWoolRecipesFor(DyeColor.BLACK, Items.BLACK_BED, Items.BLACK_BANNER, Items.BLACK_CARPET, Textiles.ITEMS.CUSHION_BLACK, registrar);
		this.addWoolRecipesFor(DyeColor.BROWN, Items.BROWN_BED, Items.BROWN_BANNER, Items.BROWN_CARPET, Textiles.ITEMS.CUSHION_BROWN, registrar);
		
		// Flaxseed Oil
		final int seedsPerBottle = 2;
		this.applyToShapeless(ShapelessRecipeBuilder.shapeless(Textiles.ITEMS.FLAXSEED_OIL_BOTTLE.get()), Textiles.ITEMS.FLAX_SEEDS.get(), seedsPerBottle)
			.requires(Items.GLASS_BOTTLE)
			.unlockedBy(RecipeHelper.criterionName(Items.GLASS_BOTTLE), RecipeHelper.has(Items.GLASS_BOTTLE))
			.save(registrar);
		this.applyToShapeless(ShapelessRecipeBuilder.shapeless(Textiles.ITEMS.FLAXSEED_OIL_BUCKET.get()), Textiles.ITEMS.FLAX_SEEDS.get(), seedsPerBottle * 3)
			.requires(Items.BUCKET)
			.unlockedBy(RecipeHelper.criterionName(Items.BUCKET), RecipeHelper.has(Items.BUCKET))
			.save(registrar);
		this.applyToShapeless(ShapelessRecipeBuilder.shapeless(Textiles.ITEMS.FLAXSEED_OIL_BUCKET.get()), Textiles.ITEMS.FLAXSEED_OIL_BOTTLE.get(), 3)
			.requires(Items.BUCKET)
			.unlockedBy(RecipeHelper.criterionName(Items.BUCKET), RecipeHelper.has(Items.BUCKET))
			.save(registrar, this.nameFrom(Textiles.ITEMS.FLAXSEED_OIL_BUCKET.get(), "bottles"));
		this.applyToShapeless(ShapelessRecipeBuilder.shapeless(Textiles.ITEMS.FLAXSEED_OIL_BOTTLE.get(), 3), Items.GLASS_BOTTLE, 3)
			.requires(Textiles.ITEMS.FLAXSEED_OIL_BUCKET.get())
			.unlockedBy(RecipeHelper.criterionName(Textiles.ITEMS.FLAXSEED_OIL_BUCKET), RecipeHelper.hasItem(Textiles.ITEMS.FLAXSEED_OIL_BUCKET))
			.save(registrar, this.nameFrom(Textiles.ITEMS.FLAXSEED_OIL_BOTTLE.get(), "bucket"));
		
		// Wood Stain and Wood Bleach
		this.stainItemRecipe(registrar, Textiles.ITEMS.WOOD_STAIN, Items.BROWN_MUSHROOM);
		this.stainItemRecipe(registrar, Textiles.ITEMS.WOOD_BLEACH, Items.RED_MUSHROOM);
		
		// Wood Staining Recipes
		final int smallObjectStainQuantity = 5; // Objects with a planks:result crafting ratio of 3:1 or better
		final int largeObjectStainQuantity = 1; // Objects with a planks:result crafting ratio of 4:1 or worse
		this.addSpecialStainRecipes(registrar, smallObjectStainQuantity, Items.BIRCH_PLANKS, Items.JUNGLE_PLANKS, Items.ACACIA_PLANKS);
		this.addSpecialStainRecipes(registrar, smallObjectStainQuantity, Items.BIRCH_BUTTON, Items.JUNGLE_BUTTON, Items.ACACIA_BUTTON);
		this.addSpecialStainRecipes(registrar, smallObjectStainQuantity, Items.BIRCH_FENCE, Items.JUNGLE_FENCE, Items.ACACIA_FENCE);
		this.addSpecialStainRecipes(registrar, smallObjectStainQuantity, Items.BIRCH_FENCE_GATE, Items.JUNGLE_FENCE_GATE, Items.ACACIA_FENCE_GATE);
		this.addSpecialStainRecipes(registrar, smallObjectStainQuantity, Items.ACACIA_PRESSURE_PLATE, Items.JUNGLE_PRESSURE_PLATE, Items.ACACIA_PRESSURE_PLATE);
		this.addSpecialStainRecipes(registrar, smallObjectStainQuantity, Items.BIRCH_SIGN, Items.JUNGLE_SIGN, Items.ACACIA_SIGN);
		this.addSpecialStainRecipes(registrar, smallObjectStainQuantity, Items.BIRCH_SLAB, Items.JUNGLE_SLAB, Items.ACACIA_SLAB);
		this.addSpecialStainRecipes(registrar, smallObjectStainQuantity, Items.BIRCH_STAIRS, Items.JUNGLE_STAIRS, Items.ACACIA_STAIRS);
		this.addSpecialStainRecipes(registrar, largeObjectStainQuantity, Items.BIRCH_BOAT, Items.JUNGLE_BOAT, Items.ACACIA_BOAT);
		this.addSpecialStainRecipes(registrar, largeObjectStainQuantity, Items.BIRCH_DOOR, Items.JUNGLE_DOOR, Items.ACACIA_DOOR);
		this.addSpecialStainRecipes(registrar, largeObjectStainQuantity, Items.BIRCH_TRAPDOOR, Items.JUNGLE_TRAPDOOR, Items.ACACIA_TRAPDOOR);
		
		this.addStainRecipes(registrar, smallObjectStainQuantity, Items.BIRCH_PLANKS, Items.OAK_PLANKS, Items.SPRUCE_PLANKS, Items.DARK_OAK_PLANKS);
		this.addStainRecipes(registrar, smallObjectStainQuantity, Items.BIRCH_BUTTON, Items.OAK_BUTTON, Items.SPRUCE_BUTTON, Items.DARK_OAK_BUTTON);
		this.addStainRecipes(registrar, smallObjectStainQuantity, Items.BIRCH_FENCE, Items.OAK_FENCE, Items.SPRUCE_FENCE, Items.DARK_OAK_FENCE);
		this.addStainRecipes(registrar, smallObjectStainQuantity, Items.BIRCH_FENCE_GATE, Items.OAK_FENCE_GATE, Items.SPRUCE_FENCE_GATE, Items.DARK_OAK_FENCE_GATE);
		this.addStainRecipes(registrar, smallObjectStainQuantity, Items.BIRCH_PRESSURE_PLATE, Items.OAK_PRESSURE_PLATE, Items.SPRUCE_PRESSURE_PLATE, Items.DARK_OAK_PRESSURE_PLATE);
		this.addStainRecipes(registrar, smallObjectStainQuantity, Items.BIRCH_SIGN, Items.OAK_SIGN, Items.SPRUCE_SIGN, Items.DARK_OAK_SIGN);
		this.addStainRecipes(registrar, smallObjectStainQuantity, Items.BIRCH_SLAB, Items.OAK_SLAB, Items.SPRUCE_SLAB, Items.DARK_OAK_SLAB);
		this.addStainRecipes(registrar, smallObjectStainQuantity, Items.BIRCH_STAIRS, Items.OAK_STAIRS, Items.SPRUCE_STAIRS, Items.DARK_OAK_STAIRS);
		this.addStainRecipes(registrar, largeObjectStainQuantity, Items.BIRCH_BOAT, Items.OAK_BOAT, Items.SPRUCE_BOAT, Items.DARK_OAK_BOAT);
		this.addStainRecipes(registrar, largeObjectStainQuantity, Items.BIRCH_DOOR, Items.OAK_DOOR, Items.SPRUCE_DOOR, Items.DARK_OAK_DOOR);
		this.addStainRecipes(registrar, largeObjectStainQuantity, Items.BIRCH_TRAPDOOR, Items.OAK_TRAPDOOR, Items.SPRUCE_TRAPDOOR, Items.DARK_OAK_TRAPDOOR);
		
		// Packed Feathers
		ShapedRecipeBuilder.shaped(Textiles.ITEMS.BLOCK_PACKED_FEATHERS.get())
			.pattern("###")
			.pattern("###")
			.pattern("###")
			.define('#', Items.FEATHER)
			.unlockedBy(RecipeHelper.createCriterionName(Items.FEATHER), RecipeHelper.has(Items.FEATHER))
			.save(registrar);
		ShapelessRecipeBuilder.shapeless(Items.FEATHER, 9)
			.requires(Textiles.ITEMS.BLOCK_PACKED_FEATHERS.get())
			.unlockedBy(RecipeHelper.criterionName(Textiles.ITEMS.BLOCK_PACKED_FEATHERS), RecipeHelper.hasItem(Textiles.ITEMS.BLOCK_PACKED_FEATHERS))
			.save(registrar, this.nameFrom(Items.FEATHER, "unpacking"));
		
		// Plain Cushion
		ShapedRecipeBuilder.shaped(Textiles.ITEMS.CUSHION_PLAIN.get(), 2)
			.pattern("XXX")
			.pattern("S#S")
			.pattern("XXX")
			.define('X', Textiles.ITEMS.FABRIC_PLAIN.get())
			.define('S', Tags.Items.STRING)
			.define('#', Ingredient.of(Items.HAY_BLOCK, Textiles.ITEMS.BLOCK_PACKED_FEATHERS.get()))
			.unlockedBy(RecipeHelper.criterionName(Textiles.ITEMS.FABRIC_PLAIN), RecipeHelper.hasItem(Textiles.ITEMS.FABRIC_PLAIN))
			.unlockedBy(RecipeHelper.criterionName(Tags.Items.STRING), RecipeHelper.has(Tags.Items.STRING))
			.unlockedBy("has_padding", RecipeHelper.inventoryTrigger(ItemPredicate.Builder.item().of(Items.HAY_BLOCK, Textiles.ITEMS.BLOCK_PACKED_FEATHERS.get()).build()))
			.save(registrar);
	}

	/* Internal Methods */
	
	protected void stainItemRecipe(Consumer<FinishedRecipe> registrar, RegistryObject<Item> result, ItemLike input) {
		ShapelessRecipeBuilder stainBuilder = this.applyToShapeless(ShapelessRecipeBuilder.shapeless(result.get(), 3), Items.GLASS_BOTTLE, 3)
			.requires(Textiles.ITEMS.FLAXSEED_OIL_BUCKET.get())
			.unlockedBy(RecipeHelper.criterionName(Textiles.ITEMS.FLAXSEED_OIL_BUCKET.get()), RecipeHelper.has(Textiles.ITEMS.FLAXSEED_OIL_BUCKET.get()));
		this.applyToShapeless(stainBuilder, input, 2).save(registrar);
	}
	
	protected void addSpecialStainRecipes(Consumer<FinishedRecipe> registrar, int strength, Item ... spectrum) {
		this.addSpecialDyeRecipe(spectrum[0], Textiles.ITEMS.WOOD_STAIN.get(), Tags.Items.DYES_PINK, spectrum[1], strength, "special_stain", registrar);
		this.addDyeRecipe(spectrum[1], Textiles.ITEMS.WOOD_BLEACH.get(), spectrum[0], strength, "special_bleach", registrar);
		this.addStainRecipes(registrar, strength, Arrays.copyOfRange(spectrum, 1, spectrum.length));
	}
	
	protected void addStainRecipes(Consumer<FinishedRecipe> registrar, int strength, Item ... spectrum) {
		this.addDyeSpectrumRecipes(Textiles.ITEMS.WOOD_STAIN.get(), Textiles.ITEMS.WOOD_BLEACH.get(), strength, registrar, spectrum);
	}
	
	protected void addDyeSpectrumRecipes(ItemLike forwardReagent, ItemLike reverseReagent, int strength, Consumer<FinishedRecipe> registrar, Item ... spectrum) {
		if (spectrum.length >= 2) {
			for (int index = 1; index < spectrum.length; index += 1) {
				Item previous = spectrum[index - 1];
				Item current = spectrum[index];
				this.addDyeRecipe(previous, forwardReagent, current, strength, "stain", registrar);
				this.addDyeRecipe(current, reverseReagent, previous, strength, "bleach", registrar);
			}
		}
	}
	
	protected void addFabricRecipe(RegistryObject<Item> result, ItemLike ingredient, Consumer<FinishedRecipe> registrar) {
		this.addLoopRecipe(result, 10, ingredient, registrar);
	}

	
	protected void addCushionDyeRecipe(TagKey<Item> dye, RegistryObject<Item> output, Consumer<FinishedRecipe> registrar) {
		this.addDyeRecipe(Textiles.ITEMS.CUSHION_WHITE, dye, output, registrar);
	}
	
	protected void addFabricDyeRecipe(TagKey<Item> dye, RegistryObject<Item> output, Consumer<FinishedRecipe> registrar) {
		this.addDyeRecipe(Textiles.ITEMS.FABRIC_WHITE, dye, output, registrar);
	}
	
	protected void addDyeRecipe(RegistryObject<Item> input, TagKey<Item> dye, RegistryObject<Item> output, Consumer<FinishedRecipe> registrar) {
		this.addDyeRecipe(input.get(), dye, output.get(), 1, "dye", registrar);
	}
	
	protected void addStickMeshRecipe(RegistryObject<Item> result, int quantity, ItemLike ingredient, Consumer<FinishedRecipe> registrar) {
		ShapedRecipeBuilder.shaped(result.get(), quantity)
			.pattern("RIR")
			.pattern("IRI")
			.pattern("RIR")
			.define('R', ingredient)
			.define('I', Tags.Items.RODS_WOODEN)
			.unlockedBy(RecipeHelper.criterionName(Tags.Items.RODS_WOODEN), RecipeHelper.has(Tags.Items.RODS_WOODEN))
			.unlockedBy(RecipeHelper.criterionName(ingredient), RecipeProvider.has(ingredient))
			.save(registrar, this.nameFromRecipe(result.get(), ingredient));
	}
	
	protected void addLoopRecipe(RegistryObject<Item> result, int quantity, ItemLike ingredient, Consumer<FinishedRecipe> registrar) {
		final TagKey<Item> coreItem = Tags.Items.RODS_WOODEN;
		ShapedRecipeBuilder.shaped(result.get(), quantity)
			.pattern("###")
			.pattern("#I#")
			.pattern("###")
			.define('#', ingredient)
			.define('I', coreItem)
			.unlockedBy(RecipeHelper.criterionName(ingredient), RecipeHelper.has(ingredient))
			.unlockedBy(RecipeHelper.criterionName(coreItem), RecipeHelper.has(coreItem))
			.save(registrar);
	}
	
	protected void addSpecialDyeRecipe(ItemLike input, ItemLike dye, TagKey<Item> augment, ItemLike result, int quantity, String processName, Consumer<FinishedRecipe> registrar) {
		Consumer<ShapelessRecipeBuilder> doubleDye = builder -> {
			this.applyItemDye(builder, dye);
			this.applyTagDye(builder, augment);
		};
		this.addDyeRecipe(input, doubleDye, result, quantity, processName, registrar);
	}
	
	protected void addDyeRecipe(ItemLike input, ItemLike dye, ItemLike result, int quantity, String processName, Consumer<FinishedRecipe> registrar) {
		this.addDyeRecipe(input, builder -> this.applyItemDye(builder, dye), result, quantity, processName, registrar);
	}
	
	protected void addDyeRecipe(ItemLike input, TagKey<Item> dye, ItemLike result, int quantity, String processName, Consumer<FinishedRecipe> registrar) {
		this.addDyeRecipe(input, builder -> this.applyTagDye(builder, dye), result, quantity, processName, registrar);
	}
	
	protected void addDyeRecipe(ItemLike input, Consumer<ShapelessRecipeBuilder> dyeApplicationFunction, ItemLike result, int quantity, String processName, Consumer<FinishedRecipe> registrar) {
		ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(result, quantity);
		dyeApplicationFunction.accept(builder);
		builder.unlockedBy(RecipeHelper.criterionName(input), RecipeHelper.has(input));
		for (int count = 0; count < quantity; count += 1) {
			builder.requires(input);
		}
		builder.save(registrar, this.nameFrom(result, processName));
	}
	
	protected void applyTagDye(ShapelessRecipeBuilder builder, TagKey<Item> dye) {
		builder.requires(dye).unlockedBy(RecipeHelper.criterionName(dye), RecipeHelper.has(dye));
	}
	
	protected void applyItemDye(ShapelessRecipeBuilder builder, ItemLike dye) {
		builder.requires(dye).unlockedBy(RecipeHelper.criterionName(dye), RecipeHelper.has(dye));
	}
	
	protected void addWoolRecipesFor(DyeColor color, Item bed, Item banner, Item carpet, RegistryObject<Item> cushion, Consumer<FinishedRecipe> registrar) {
		final TagKey<Item> woolTag = Utilities.Tags.forgeItemTag(ItemTagsGenerator.TAG_WOOL, color.getName());
		final TriggerInstance hasWool =  RecipeHelper.has(woolTag);
		final String hasWoolName = RecipeHelper.criterionName(woolTag);
		
		// Beds
		ShapedRecipeBuilder.shaped(bed)
			.pattern("WWW")
			.pattern("###")
			.define('W', woolTag)
			.define('#', ItemTags.PLANKS)
			.unlockedBy(hasWoolName, hasWool)
			.save(registrar, nameFromPath(bed));
		
		// Banners
		ShapedRecipeBuilder.shaped(banner)
			.pattern("###")
			.pattern("###")
			.pattern(" I ")
			.define('#', woolTag)
			.define('I', Tags.Items.RODS_WOODEN)
			.unlockedBy(hasWoolName, hasWool)
			.unlockedBy(RecipeHelper.criterionName(Tags.Items.RODS_WOODEN), RecipeHelper.has(Tags.Items.RODS_WOODEN))
			.save(registrar, nameFromPath(banner));
		
		// Carpets
		ShapedRecipeBuilder.shaped(carpet, 3)
			.pattern("##")
			.define('#', woolTag)
			.unlockedBy(hasWoolName, hasWool)
			.save(registrar, nameFromPath(carpet));
		
		// Cushions
		ShapedRecipeBuilder.shaped(cushion.get(), 2)
			.pattern("XXX")
			.pattern("S#S")
			.pattern("XXX")
			.define('X', woolTag)
			.define('S', Tags.Items.STRING)
			.define('#', Ingredient.of(Items.HAY_BLOCK, Textiles.ITEMS.BLOCK_PACKED_FEATHERS.get()))
			.unlockedBy(hasWoolName, hasWool)
			.unlockedBy(RecipeHelper.criterionName(Tags.Items.STRING), RecipeHelper.has(Tags.Items.STRING))
			.unlockedBy("has_padding", RecipeHelper.inventoryTrigger(ItemPredicate.Builder.item().of(Items.HAY_BLOCK, Textiles.ITEMS.BLOCK_PACKED_FEATHERS.get()).build()))
			.save(registrar);
	}

	protected void simpleShaplessMulti(final Consumer<FinishedRecipe> registrar, Item output, Item input, int quantity) {
		ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(output, 1);
		this.applyToShapeless(builder, input, quantity).save(registrar, this.nameFromRecipe(output, input));
	}
	
	protected ShapelessRecipeBuilder applyToShapeless(ShapelessRecipeBuilder builder, ItemLike ingredient, int quantity) {
		for (int count = 0; count < quantity; count += 1) {
			builder.requires(ingredient);
		}
		return builder.unlockedBy(RecipeHelper.criterionName(ingredient), RecipeProvider.has(ingredient));
	}
	
	protected ResourceLocation nameFromPath(ItemLike output) {
		return Textiles.createResource(RecipeHelper.registryPathOf(output));
	}
	
	protected ResourceLocation nameFrom(RegistryObject<Item> output, String source) {
		return this.nameFrom(output.get(), source);
	}
	
	protected ResourceLocation nameFrom(ItemLike output, String source) {
		return RecipeHelper.nameFrom(Textiles::createResource, output, source);
	}
	
	protected ResourceLocation nameFromRecipe(RegistryObject<Item> output, RegistryObject<Item> input) {
		return this.nameFromRecipe(output.get(), input.get());
	}

	protected ResourceLocation nameFromRecipe(ItemLike output, ItemLike input) {
		return RecipeHelper.nameFromIngredients(Textiles::createResource, output, input);
	}

}
