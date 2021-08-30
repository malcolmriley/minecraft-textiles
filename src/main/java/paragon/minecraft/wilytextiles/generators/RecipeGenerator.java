package paragon.minecraft.wilytextiles.generators;

import java.util.Arrays;
import java.util.function.Consumer;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.data.SmithingRecipeBuilder;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.RegistryObject;
import paragon.minecraft.library.Utilities;
import paragon.minecraft.library.datageneration.RecipeHelper;
import paragon.minecraft.wilytextiles.Textiles;

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
	public void registerRecipes(final Consumer<IFinishedRecipe> registrar) {
		// Dyes from blossoms
		this.simpleShaplessMulti(registrar, Items.LIGHT_BLUE_DYE, Textiles.ITEMS.FLAX_PALE.get(), 3);
		this.simpleShaplessMulti(registrar, Items.CYAN_DYE, Textiles.ITEMS.FLAX_VIBRANT.get(), 3);
		this.simpleShaplessMulti(registrar, Items.PURPLE_DYE, Textiles.ITEMS.FLAX_PURPLE.get(), 3);
		
		// Raw Plant Fibers
		final INamedTag<Item> grassTag = Utilities.Tags.forgeItemTag(BlockTagsGenerator.TAG_GRASSES);
		ShapedRecipeBuilder.shapedRecipe(Textiles.ITEMS.PLANT_FIBERS.get())
			.patternLine("## ")
			.patternLine(" # ")
			.patternLine(" ##")
			.key('#', grassTag)
			.addCriterion(RecipeHelper.criterionName(grassTag), RecipeHelper.hasItem(grassTag))
			.build(registrar);
		
		ShapelessRecipeBuilder.shapelessRecipe(Textiles.ITEMS.PLANT_FIBERS.get(), 1)
			.addIngredient(Items.DEAD_BUSH, 4)
			.addCriterion(RecipeHelper.criterionName(Items.DEAD_BUSH), RecipeHelper.hasItem(Items.DEAD_BUSH))
			.build(registrar, this.nameFromRecipe(Textiles.ITEMS.PLANT_FIBERS.get(), Items.DEAD_BUSH));
		
		// Retting fiber bundles
		ShapedRecipeBuilder.shapedRecipe(Textiles.ITEMS.BLOCK_RETTING_FIBERS.get())
			.patternLine("## ")
			.patternLine("###")
			.patternLine(" ##")
			.key('#', Textiles.ITEMS.PLANT_FIBERS.get())
			.addCriterion(RecipeHelper.criterionName(Textiles.ITEMS.PLANT_FIBERS), RecipeHelper.hasItem(Textiles.ITEMS.PLANT_FIBERS))
			.build(registrar, this.nameFromRecipe(Textiles.ITEMS.BLOCK_RETTING_FIBERS, Textiles.ITEMS.PLANT_FIBERS));
		
		ShapedRecipeBuilder.shapedRecipe(Textiles.ITEMS.BLOCK_RETTING_FIBERS.get())
			.patternLine("##")
			.patternLine("##")
			.key('#', Textiles.ITEMS.FLAX_STALKS.get())
			.addCriterion(RecipeHelper.criterionName(Textiles.ITEMS.FLAX_STALKS), RecipeHelper.hasItem(Textiles.ITEMS.FLAX_STALKS))
			.build(registrar, this.nameFromRecipe(Textiles.ITEMS.BLOCK_RETTING_FIBERS, Textiles.ITEMS.FLAX_STALKS));

		// Wicker
		this.addStickMeshRecipe(Textiles.ITEMS.WICKER, 3, Items.SUGAR_CANE, registrar);
		this.addStickMeshRecipe(Textiles.ITEMS.WICKER, 3, Textiles.ITEMS.FLAX_STALKS.get(), registrar);
		
		// Silk Wisps from Cobwebs
		ShapelessRecipeBuilder.shapelessRecipe(Textiles.ITEMS.SILK_WISPS.get(), 9)
			.addIngredient(Items.COBWEB)
			.addCriterion(RecipeHelper.criterionName(Items.COBWEB), RecipeHelper.hasItem(Items.COBWEB))
			.build(registrar);
		
		// Silk from Silk Wisps
		this.addLoopRecipe(Textiles.ITEMS.SILK, 1, Textiles.ITEMS.SILK_WISPS.get(), registrar);

		// Chain Mesh
		ShapedRecipeBuilder.shapedRecipe(Textiles.ITEMS.CHAIN_MESH.get(), 5)
			.patternLine("XX")
			.patternLine("XX")
			.key('X', Items.CHAIN)
			.addCriterion(RecipeHelper.criterionName(Items.CHAIN), RecipeProvider.hasItem(Tags.Items.NUGGETS_IRON))
			.build(registrar, this.nameFromRecipe(Textiles.ITEMS.CHAIN_MESH.get(), Items.IRON_NUGGET));

		// Chainmail Armor
		final String chainmailCriterion = RecipeHelper.criterionName(Textiles.ITEMS.CHAIN_MESH);
		final ICriterionInstance chainmailTrigger = RecipeProvider.hasItem(Textiles.ITEMS.CHAIN_MESH.get());
		ShapedRecipeBuilder.shapedRecipe(Items.CHAINMAIL_HELMET)
			.patternLine("###")
			.patternLine("# #")
			.key('#', Textiles.ITEMS.CHAIN_MESH.get())
			.addCriterion(chainmailCriterion, chainmailTrigger)
			.build(registrar, this.nameFromRecipe(Items.CHAINMAIL_HELMET, Textiles.ITEMS.CHAIN_MESH.get()));

		ShapedRecipeBuilder.shapedRecipe(Items.CHAINMAIL_CHESTPLATE)
			.patternLine("# #")
			.patternLine("###")
			.patternLine("###")
			.key('#', Textiles.ITEMS.CHAIN_MESH.get())
			.addCriterion(chainmailCriterion, chainmailTrigger)
			.build(registrar, this.nameFromRecipe(Items.CHAINMAIL_CHESTPLATE, Textiles.ITEMS.CHAIN_MESH.get()));

		ShapedRecipeBuilder.shapedRecipe(Items.CHAINMAIL_BOOTS)
			.patternLine("# #")
			.patternLine("# #")
			.key('#', Textiles.ITEMS.CHAIN_MESH.get())
			.addCriterion(chainmailCriterion, chainmailTrigger)
			.build(registrar, this.nameFromRecipe(Items.CHAINMAIL_BOOTS, Textiles.ITEMS.CHAIN_MESH.get()));

		ShapedRecipeBuilder.shapedRecipe(Items.CHAINMAIL_LEGGINGS)
			.patternLine("###")
			.patternLine("# #")
			.patternLine("# #")
			.key('#', Textiles.ITEMS.CHAIN_MESH.get())
			.addCriterion(chainmailCriterion, chainmailTrigger)
			.build(registrar, this.nameFromRecipe(Items.CHAINMAIL_LEGGINGS, Textiles.ITEMS.CHAIN_MESH.get()));
		
		// Basket
		ShapedRecipeBuilder.shapedRecipe(Textiles.BLOCKS.BASKET.get()) 
			.patternLine("#I#")
			.patternLine("# #")
			.patternLine("###")
			.key('#', Textiles.ITEMS.WICKER.get())
			.key('I', Tags.Items.RODS_WOODEN)
			.addCriterion(RecipeHelper.criterionName(Textiles.ITEMS.WICKER), RecipeHelper.hasItem(Textiles.ITEMS.WICKER))
			.addCriterion(RecipeHelper.criterionName(Tags.Items.RODS_WOODEN), RecipeHelper.hasItem(Tags.Items.RODS_WOODEN))
			.build(registrar, this.nameFromRecipe(Textiles.ITEMS.BLOCK_BASKET, Textiles.ITEMS.WICKER));
		
		// Sturdy Basket
		SmithingRecipeBuilder.smithingRecipe(
				Ingredient.fromItems(Textiles.ITEMS.BLOCK_BASKET.get()),
				Ingredient.fromItems(Items.TURTLE_HELMET), 
				Textiles.ITEMS.BLOCK_BASKET_STURDY.get())
			.addCriterion(RecipeHelper.criterionName(Textiles.ITEMS.BLOCK_BASKET), RecipeHelper.hasItem(Textiles.ITEMS.BLOCK_BASKET))
			.addCriterion(RecipeHelper.criterionName(Items.TURTLE_HELMET), RecipeHelper.hasItem(Items.TURTLE_HELMET))
		.build(registrar, this.nameFromPath(Textiles.ITEMS.BLOCK_BASKET_STURDY.get()));
		
		// String tag override recipes
		final String stringCriterion = RecipeHelper.criterionName(Items.STRING);
		final ICriterionInstance stringTrigger = RecipeHelper.hasItem(Tags.Items.STRING);
		
		final String stickCriterion = RecipeHelper.criterionName(Tags.Items.RODS_WOODEN);
		final ICriterionInstance stickTrigger = RecipeHelper.hasItem(Tags.Items.RODS_WOODEN);
		ShapedRecipeBuilder.shapedRecipe(Items.BOW)
			.patternLine(" IS")
			.patternLine("I S")
			.patternLine(" IS")
			.key('I', Tags.Items.RODS_WOODEN)
			.key('S', Tags.Items.STRING)
			.addCriterion(stringCriterion, stringTrigger)
			.build(registrar, Textiles.createResource("bow"));
		
		ShapedRecipeBuilder.shapedRecipe(Items.CROSSBOW)
			.patternLine("I#I")
			.patternLine("SPS")
			.patternLine(" I ")
			.key('I', Tags.Items.RODS_WOODEN)
			.key('S', Tags.Items.STRING)
			.key('#', Tags.Items.INGOTS_IRON)
			.key('P', Items.TRIPWIRE_HOOK)
			.addCriterion(stringCriterion, stringTrigger)
			.addCriterion(stickCriterion, stickTrigger)
			.addCriterion(RecipeHelper.criterionName(Tags.Items.INGOTS_IRON), RecipeHelper.hasItem(Tags.Items.INGOTS_IRON))
			.addCriterion(RecipeHelper.criterionName(Items.TRIPWIRE_HOOK), RecipeHelper.hasItem(Items.TRIPWIRE_HOOK))
			.build(registrar, Textiles.createResource("crossbow"));
		
		ShapedRecipeBuilder.shapedRecipe(Items.FISHING_ROD)
			.patternLine("  I")
			.patternLine(" IS")
			.patternLine("I S")
			.key('I', Tags.Items.RODS_WOODEN)
			.key('S', Tags.Items.STRING)
			.addCriterion(stringCriterion, stringTrigger)
			.build(registrar, Textiles.createResource("fishing_rod"));
		
		ShapedRecipeBuilder.shapedRecipe(Items.LEAD)
			.patternLine("SS ")
			.patternLine("SO ")
			.patternLine("  S")
			.key('S', Tags.Items.STRING)
			.key('O', Tags.Items.SLIMEBALLS)
			.addCriterion(RecipeHelper.criterionName(Tags.Items.SLIMEBALLS), RecipeHelper.hasItem(Tags.Items.SLIMEBALLS))
			.build(registrar, Textiles.createResource("lead"));
		
		ShapedRecipeBuilder.shapedRecipe(Items.LOOM)
			.patternLine("SS")
			.patternLine("##")
			.key('S', Tags.Items.STRING)
			.key('#', ItemTags.PLANKS)
			.addCriterion(stringCriterion, stringTrigger)
			.build(registrar, Textiles.createResource("loom"));
		
		// Plain Fabric from Twine
		this.addLoopRecipe(Textiles.ITEMS.FABRIC_PLAIN, 2, Textiles.ITEMS.TWINE.get(), registrar);
		
		// White Fabric from Silk
		ShapedRecipeBuilder.shapedRecipe(Textiles.ITEMS.FABRIC_WHITE.get(), 2)
			.patternLine("SSS")
			.patternLine("SSS")
			.key('S', Textiles.ITEMS.SILK.get())
			.addCriterion(RecipeHelper.criterionName(Textiles.ITEMS.TWINE), RecipeHelper.hasItem(Textiles.ITEMS.TWINE))
			.build(registrar, this.nameFromRecipe(Textiles.ITEMS.FABRIC_WHITE, Textiles.ITEMS.SILK));
		
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
		
		// Dyed Fabrics
		this.addFabricDyeRecipe(Textiles.ITEMS.FABRIC_PLAIN, Tags.Items.DYES_WHITE, Textiles.ITEMS.FABRIC_WHITE, registrar);

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
		
		// Tag-based Wool Recipes
		this.addWoolRecipesFor(DyeColor.RED, Items.RED_BED, Items.RED_BANNER, Items.RED_CARPET, registrar);
		this.addWoolRecipesFor(DyeColor.ORANGE, Items.ORANGE_BED, Items.ORANGE_BANNER, Items.ORANGE_CARPET, registrar);
		this.addWoolRecipesFor(DyeColor.YELLOW, Items.YELLOW_BED, Items.YELLOW_BANNER, Items.YELLOW_CARPET, registrar);
		this.addWoolRecipesFor(DyeColor.LIME, Items.LIME_BED, Items.LIME_BANNER, Items.LIME_CARPET, registrar);
		this.addWoolRecipesFor(DyeColor.GREEN, Items.GREEN_BED, Items.GREEN_BANNER, Items.GREEN_CARPET, registrar);
		this.addWoolRecipesFor(DyeColor.CYAN, Items.CYAN_BED, Items.CYAN_BANNER, Items.CYAN_CARPET, registrar);
		this.addWoolRecipesFor(DyeColor.LIGHT_BLUE, Items.LIGHT_BLUE_BED, Items.LIGHT_BLUE_BANNER, Items.LIGHT_BLUE_CARPET, registrar);
		this.addWoolRecipesFor(DyeColor.BLUE, Items.BLUE_BED, Items.BLUE_BANNER, Items.BLUE_CARPET, registrar);
		this.addWoolRecipesFor(DyeColor.PURPLE, Items.PURPLE_BED, Items.PURPLE_BANNER, Items.PURPLE_CARPET, registrar);
		this.addWoolRecipesFor(DyeColor.MAGENTA, Items.MAGENTA_BED, Items.MAGENTA_BANNER, Items.MAGENTA_CARPET, registrar);
		this.addWoolRecipesFor(DyeColor.PINK, Items.PINK_BED, Items.PINK_BANNER, Items.PINK_CARPET, registrar);
		this.addWoolRecipesFor(DyeColor.WHITE, Items.WHITE_BED, Items.WHITE_BANNER, Items.WHITE_CARPET, registrar);
		this.addWoolRecipesFor(DyeColor.LIGHT_GRAY, Items.LIGHT_GRAY_BED, Items.LIGHT_GRAY_BANNER, Items.LIGHT_GRAY_CARPET, registrar);
		this.addWoolRecipesFor(DyeColor.GRAY, Items.GRAY_BED, Items.GRAY_BANNER, Items.GRAY_CARPET, registrar);
		this.addWoolRecipesFor(DyeColor.BLACK, Items.BLACK_BED, Items.BLACK_BANNER, Items.BLACK_CARPET, registrar);
		this.addWoolRecipesFor(DyeColor.BROWN, Items.BROWN_BED, Items.BROWN_BANNER, Items.BROWN_CARPET, registrar);
		
		// Flaxseed Oil
		final int seedsPerBottle = 2;
		this.applyToShapeless(ShapelessRecipeBuilder.shapelessRecipe(Textiles.ITEMS.FLAXSEED_OIL_BOTTLE.get()), Textiles.ITEMS.FLAX_SEEDS.get(), seedsPerBottle)
			.addIngredient(Items.GLASS_BOTTLE)
			.addCriterion(RecipeHelper.criterionName(Items.GLASS_BOTTLE), RecipeHelper.hasItem(Items.GLASS_BOTTLE))
			.build(registrar);
		this.applyToShapeless(ShapelessRecipeBuilder.shapelessRecipe(Textiles.ITEMS.FLAXSEED_OIL_BUCKET.get()), Textiles.ITEMS.FLAX_SEEDS.get(), seedsPerBottle * 3)
			.addIngredient(Items.BUCKET)
			.addCriterion(RecipeHelper.criterionName(Items.BUCKET), RecipeHelper.hasItem(Items.BUCKET))
			.build(registrar);
		this.applyToShapeless(ShapelessRecipeBuilder.shapelessRecipe(Textiles.ITEMS.FLAXSEED_OIL_BUCKET.get()), Textiles.ITEMS.FLAXSEED_OIL_BOTTLE.get(), 3)
			.addIngredient(Items.BUCKET)
			.addCriterion(RecipeHelper.criterionName(Items.BUCKET), RecipeHelper.hasItem(Items.BUCKET))
			.build(registrar, this.nameFrom(Textiles.ITEMS.FLAXSEED_OIL_BUCKET.get(), "bottles"));
		this.applyToShapeless(ShapelessRecipeBuilder.shapelessRecipe(Textiles.ITEMS.FLAXSEED_OIL_BOTTLE.get(), 3), Items.GLASS_BOTTLE, 3)
			.addIngredient(Textiles.ITEMS.FLAXSEED_OIL_BUCKET.get())
			.addCriterion(RecipeHelper.criterionName(Textiles.ITEMS.FLAXSEED_OIL_BUCKET), RecipeHelper.hasItem(Textiles.ITEMS.FLAXSEED_OIL_BUCKET))
			.build(registrar, this.nameFrom(Textiles.ITEMS.FLAXSEED_OIL_BOTTLE.get(), "bucket"));
		
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
	}

	/* Internal Methods */
	
	protected void stainItemRecipe(Consumer<IFinishedRecipe> registrar, RegistryObject<Item> result, IItemProvider input) {
		ShapelessRecipeBuilder stainBuilder = this.applyToShapeless(ShapelessRecipeBuilder.shapelessRecipe(result.get(), 3), Items.GLASS_BOTTLE, 3)
			.addIngredient(Textiles.ITEMS.FLAXSEED_OIL_BUCKET.get())
			.addCriterion(RecipeHelper.criterionName(Textiles.ITEMS.FLAXSEED_OIL_BUCKET.get()), RecipeHelper.hasItem(Textiles.ITEMS.FLAXSEED_OIL_BUCKET.get()));
		this.applyToShapeless(stainBuilder, input, 2).build(registrar);
	}
	
	protected void addSpecialStainRecipes(Consumer<IFinishedRecipe> registrar, int strength, Item ... spectrum) {
		this.addSpecialDyeRecipe(spectrum[0], Textiles.ITEMS.WOOD_STAIN.get(), Tags.Items.DYES_PINK, spectrum[1], strength, "special_stain", registrar);
		this.addDyeRecipe(spectrum[1], Textiles.ITEMS.WOOD_BLEACH.get(), spectrum[0], strength, "special_bleach", registrar);
		this.addStainRecipes(registrar, strength, Arrays.copyOfRange(spectrum, 1, spectrum.length));
	}
	
	protected void addStainRecipes(Consumer<IFinishedRecipe> registrar, int strength, Item ... spectrum) {
		this.addDyeSpectrumRecipes(Textiles.ITEMS.WOOD_STAIN.get(), Textiles.ITEMS.WOOD_BLEACH.get(), strength, registrar, spectrum);
	}
	
	protected void addDyeSpectrumRecipes(IItemProvider forwardReagent, IItemProvider reverseReagent, int strength, Consumer<IFinishedRecipe> registrar, Item ... spectrum) {
		if (spectrum.length >= 2) {
			for (int index = 1; index < spectrum.length; index += 1) {
				Item previous = spectrum[index - 1];
				Item current = spectrum[index];
				this.addDyeRecipe(previous, forwardReagent, current, strength, "stain", registrar);
				this.addDyeRecipe(current, reverseReagent, previous, strength, "bleach", registrar);
			}
		}
	}
	
	protected void addFabricDyeRecipe(INamedTag<Item> dye, RegistryObject<Item> output, Consumer<IFinishedRecipe> registrar) {
		this.addFabricDyeRecipe(Textiles.ITEMS.FABRIC_WHITE, dye, output, registrar);
	}
	
	protected void addFabricDyeRecipe(RegistryObject<Item> input, INamedTag<Item> dye, RegistryObject<Item> output, Consumer<IFinishedRecipe> registrar) {
		this.addDyeRecipe(input.get(), dye, output.get(), 1, "dye", registrar);
	}
	
	protected void addFabricRecipe(RegistryObject<Item> result, IItemProvider ingredient, Consumer<IFinishedRecipe> registrar) {
		this.addLoopRecipe(result, 10, ingredient, registrar);
	}
	
	protected void addStickMeshRecipe(RegistryObject<Item> result, int quantity, IItemProvider ingredient, Consumer<IFinishedRecipe> registrar) {
		ShapedRecipeBuilder.shapedRecipe(result.get(), quantity)
			.patternLine("RIR")
			.patternLine("IRI")
			.patternLine("RIR")
			.key('R', ingredient)
			.key('I', Tags.Items.RODS_WOODEN)
			.addCriterion(RecipeHelper.criterionName(Tags.Items.RODS_WOODEN), RecipeHelper.hasItem(Tags.Items.RODS_WOODEN))
			.addCriterion(RecipeHelper.criterionName(ingredient), RecipeProvider.hasItem(ingredient))
			.build(registrar, this.nameFromRecipe(result.get(), ingredient));
	}
	
	protected void addLoopRecipe(RegistryObject<Item> result, int quantity, IItemProvider ingredient, Consumer<IFinishedRecipe> registrar) {
		final INamedTag<Item> coreItem = Tags.Items.RODS_WOODEN;
		ShapedRecipeBuilder.shapedRecipe(result.get(), quantity)
			.patternLine("###")
			.patternLine("#I#")
			.patternLine("###")
			.key('#', ingredient)
			.key('I', coreItem)
			.addCriterion(RecipeHelper.criterionName(ingredient), RecipeHelper.hasItem(ingredient))
			.addCriterion(RecipeHelper.criterionName(coreItem), RecipeHelper.hasItem(coreItem))
			.build(registrar);
	}
	
	protected void addSpecialDyeRecipe(IItemProvider input, IItemProvider dye, INamedTag<Item> augment, IItemProvider result, int quantity, String processName, Consumer<IFinishedRecipe> registrar) {
		Consumer<ShapelessRecipeBuilder> doubleDye = builder -> {
			this.applyItemDye(builder, dye);
			this.applyTagDye(builder, augment);
		};
		this.addDyeRecipe(input, doubleDye, result, quantity, processName, registrar);
	}
	
	protected void addDyeRecipe(IItemProvider input, IItemProvider dye, IItemProvider result, int quantity, String processName, Consumer<IFinishedRecipe> registrar) {
		this.addDyeRecipe(input, builder -> this.applyItemDye(builder, dye), result, quantity, processName, registrar);
	}
	
	protected void addDyeRecipe(IItemProvider input, INamedTag<Item> dye, IItemProvider result, int quantity, String processName, Consumer<IFinishedRecipe> registrar) {
		this.addDyeRecipe(input, builder -> this.applyTagDye(builder, dye), result, quantity, processName, registrar);
	}
	
	protected void addDyeRecipe(IItemProvider input, Consumer<ShapelessRecipeBuilder> dyeApplicationFunction, IItemProvider result, int quantity, String processName, Consumer<IFinishedRecipe> registrar) {
		ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapelessRecipe(result, quantity);
		dyeApplicationFunction.accept(builder);
		builder.addCriterion(RecipeHelper.criterionName(input), RecipeHelper.hasItem(input));
		for (int count = 0; count < quantity; count += 1) {
			builder.addIngredient(input);
		}
		builder.build(registrar, this.nameFrom(result, processName));
	}
	
	protected void applyTagDye(ShapelessRecipeBuilder builder, INamedTag<Item> dye) {
		builder.addIngredient(dye).addCriterion(RecipeHelper.criterionName(dye), RecipeHelper.hasItem(dye));
	}
	
	protected void applyItemDye(ShapelessRecipeBuilder builder, IItemProvider dye) {
		builder.addIngredient(dye).addCriterion(RecipeHelper.criterionName(dye), RecipeHelper.hasItem(dye));
	}
	
	protected void addWoolRecipesFor(DyeColor color, Item bed, Item banner, Item carpet, Consumer<IFinishedRecipe> registrar) {
		final INamedTag<Item> woolTag = Utilities.Tags.forgeItemTag(ItemTagsGenerator.TAG_WOOL, color.getString());
		final ICriterionInstance hasWool =  RecipeHelper.hasItem(woolTag);
		final String hasWoolName = RecipeHelper.criterionName(woolTag);
		
		// Beds
		ShapedRecipeBuilder.shapedRecipe(bed)
			.patternLine("WWW")
			.patternLine("###")
			.key('W', woolTag)
			.key('#', ItemTags.PLANKS)
			.addCriterion(hasWoolName, hasWool)
			.build(registrar, nameFromPath(bed));
		
		// Banners
		ShapedRecipeBuilder.shapedRecipe(banner)
			.patternLine("###")
			.patternLine("###")
			.patternLine(" I ")
			.key('#', woolTag)
			.key('I', Tags.Items.RODS_WOODEN)
			.addCriterion(hasWoolName, hasWool)
			.addCriterion(RecipeHelper.criterionName(Tags.Items.RODS_WOODEN), RecipeHelper.hasItem(Tags.Items.RODS_WOODEN))
			.build(registrar, nameFromPath(banner));
		
		// Carpets
		ShapedRecipeBuilder.shapedRecipe(carpet, 3)
			.patternLine("##")
			.key('#', woolTag)
			.addCriterion(hasWoolName, hasWool)
			.build(registrar, nameFromPath(carpet));
	}

	protected void simpleShaplessMulti(final Consumer<IFinishedRecipe> registrar, Item output, Item input, int quantity) {
		ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapelessRecipe(output, 1);
		this.applyToShapeless(builder, input, quantity).build(registrar, this.nameFromRecipe(output, input));
	}
	
	protected ShapelessRecipeBuilder applyToShapeless(ShapelessRecipeBuilder builder, IItemProvider ingredient, int quantity) {
		for (int count = 0; count < quantity; count += 1) {
			builder.addIngredient(ingredient);
		}
		return builder.addCriterion(RecipeHelper.criterionName(ingredient), RecipeProvider.hasItem(ingredient));
	}
	
	protected ResourceLocation nameFromPath(IItemProvider output) {
		return Textiles.createResource(RecipeHelper.registryPathOf(output));
	}
	
	protected ResourceLocation nameFrom(RegistryObject<Item> output, String source) {
		return this.nameFrom(output.get(), source);
	}
	
	protected ResourceLocation nameFrom(IItemProvider output, String source) {
		return RecipeHelper.nameFrom(Textiles::createResource, output, source);
	}
	
	protected ResourceLocation nameFromRecipe(RegistryObject<Item> output, RegistryObject<Item> input) {
		return this.nameFromRecipe(output.get(), input.get());
	}

	protected ResourceLocation nameFromRecipe(IItemProvider output, IItemProvider input) {
		return RecipeHelper.nameFromIngredients(Textiles::createResource, output, input);
	}

}
