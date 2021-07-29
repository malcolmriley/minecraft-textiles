package paragon.minecraft.wilytextiles.generators;

import java.util.Arrays;
import java.util.function.Consumer;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.RegistryObject;
import paragon.minecraft.library.Utilities;
import paragon.minecraft.library.datageneration.RecipeHelper;
import paragon.minecraft.wilytextiles.Textiles;

final class RecipeGenerator extends RecipeHelper {

	// Existing criterion fields from vanilla
	private static final String CRITERION_STICKS = "has_stick";
	private static final String CRITERION_SUGARCANE = "has_reeds";

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
			.addCriterion(RecipeHelper.criterionName(Textiles.ITEMS.PLANT_FIBERS), RecipeHelper.hasItem(Textiles.ITEMS.PLANT_FIBERS.get()))
			.build(registrar, this.nameFromRecipe(Textiles.ITEMS.BLOCK_RETTING_FIBERS.get(), Textiles.ITEMS.PLANT_FIBERS.get()));
		
		ShapedRecipeBuilder.shapedRecipe(Textiles.ITEMS.BLOCK_RETTING_FIBERS.get())
			.patternLine("##")
			.patternLine("##")
			.key('#', Textiles.ITEMS.FLAX_STALKS.get())
			.addCriterion(RecipeHelper.criterionName(Textiles.ITEMS.FLAX_STALKS.get()), RecipeHelper.hasItem(Textiles.ITEMS.FLAX_STALKS.get()))
			.build(registrar, this.nameFromRecipe(Textiles.ITEMS.BLOCK_RETTING_FIBERS.get(), Textiles.ITEMS.FLAX_STALKS.get()));

		// Wicker
		ShapedRecipeBuilder.shapedRecipe(Textiles.ITEMS.WICKER.get(), 3)
			.patternLine("RIR")
			.patternLine("IRI")
			.patternLine("RIR")
			.key('R', Items.SUGAR_CANE)
			.key('I', Tags.Items.RODS_WOODEN)
			.addCriterion(CRITERION_STICKS, RecipeProvider.hasItem(Tags.Items.RODS_WOODEN))
			.addCriterion(CRITERION_SUGARCANE, RecipeProvider.hasItem(Items.SUGAR_CANE))
			.build(registrar, this.nameFromRecipe(Textiles.ITEMS.WICKER.get(), Items.SUGAR_CANE));
		
		// Silk Wisps from Cobwebs
		ShapelessRecipeBuilder.shapelessRecipe(Textiles.ITEMS.SILK_WISPS.get(), 9)
			.addIngredient(Items.COBWEB)
			.addCriterion(RecipeHelper.criterionName(Items.COBWEB), RecipeHelper.hasItem(Items.COBWEB))
			.build(registrar);
		
		// Silk from Silk Wisps
		ShapedRecipeBuilder.shapedRecipe(Textiles.ITEMS.SILK.get())
			.patternLine("SSS")
			.patternLine("SIS")
			.patternLine("SSS")
			.key('S', Textiles.ITEMS.SILK_WISPS.get())
			.key('I', Tags.Items.RODS_WOODEN)
			.addCriterion(RecipeHelper.criterionName(Tags.Items.RODS_WOODEN), RecipeProvider.hasItem(Tags.Items.RODS_WOODEN))
			.addCriterion(RecipeHelper.criterionName(Textiles.ITEMS.SILK_WISPS.get()), RecipeProvider.hasItem(Textiles.ITEMS.SILK_WISPS.get()))
			.build(registrar, this.nameFromRecipe(Textiles.ITEMS.SILK.get(), Textiles.ITEMS.SILK_WISPS.get()));

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
			.build(registrar, this.nameFromRecipe(Textiles.ITEMS.BLOCK_BASKET.get(), Textiles.ITEMS.WICKER.get()));
		
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
			.build(registrar, Utilities.Strings.minecraftResource("bow"));
		
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
			.build(registrar, Utilities.Strings.minecraftResource("crossbow"));
		
		ShapedRecipeBuilder.shapedRecipe(Items.FISHING_ROD)
			.patternLine("  I")
			.patternLine(" IS")
			.patternLine("I S")
			.key('I', Tags.Items.RODS_WOODEN)
			.key('S', Tags.Items.STRING)
			.addCriterion(stringCriterion, stringTrigger)
			.build(registrar, Utilities.Strings.minecraftResource("fishing_rod"));
		
		ShapedRecipeBuilder.shapedRecipe(Items.LEAD)
			.patternLine("SS ")
			.patternLine("SO ")
			.patternLine("  S")
			.key('S', Tags.Items.STRING)
			.key('O', Tags.Items.SLIMEBALLS)
			.addCriterion(RecipeHelper.criterionName(Tags.Items.SLIMEBALLS), RecipeHelper.hasItem(Tags.Items.SLIMEBALLS))
			.build(registrar, Utilities.Strings.minecraftResource("lead"));
		
		ShapedRecipeBuilder.shapedRecipe(Items.LOOM)
			.patternLine("SS")
			.patternLine("##")
			.key('S', Tags.Items.STRING)
			.key('#', ItemTags.PLANKS)
			.addCriterion(stringCriterion, stringTrigger)
			.build(registrar, Utilities.Strings.minecraftResource("loom"));
		
		// Plain Fabric from Twine
		ShapedRecipeBuilder.shapedRecipe(Textiles.ITEMS.FABRIC_PLAIN.get(), 2)
			.patternLine("SSS")
			.patternLine("SIS")
			.patternLine("SSS")
			.key('S', Textiles.ITEMS.TWINE.get())
			.key('I', Items.STICK)
			.addCriterion(RecipeHelper.criterionName(Textiles.ITEMS.TWINE), RecipeHelper.hasItem(Textiles.ITEMS.TWINE))
			.addCriterion(stickCriterion, stickTrigger)
			.build(registrar);
		
		// White Fabric from Silk
		ShapedRecipeBuilder.shapedRecipe(Textiles.ITEMS.FABRIC_WHITE.get(), 2)
			.patternLine("SSS")
			.patternLine("SSS")
			.key('S', Textiles.ITEMS.SILK.get())
			.addCriterion(RecipeHelper.criterionName(Textiles.ITEMS.TWINE), RecipeHelper.hasItem(Textiles.ITEMS.TWINE))
			.build(registrar, this.nameFromRecipe(Textiles.ITEMS.FABRIC_WHITE.get(), Textiles.ITEMS.SILK.get()));
		
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
		
		// Wood Staining Recipes
		final int stainStrength = 5;
		this.addSpecialStainRecipes(registrar, stainStrength, Items.BIRCH_PLANKS, Items.JUNGLE_PLANKS, Items.ACACIA_PLANKS);
		this.addStainRecipes(registrar, stainStrength, Items.BIRCH_PLANKS, Items.OAK_PLANKS, Items.SPRUCE_PLANKS, Items.DARK_OAK_PLANKS);
	}

	/* Internal Methods */
	
	protected void addSpecialStainRecipes(Consumer<IFinishedRecipe> registrar, int strength, Item ... spectrum) {
		this.addSpecialDyeRecipe(spectrum[0], Textiles.ITEMS.WOOD_STAIN.get(), Tags.Items.DYES_PINK, spectrum[1], strength, "special_stain", registrar);
		this.addDyeRecipe(spectrum[1], Textiles.ITEMS.WOOD_BLEACH.get(), spectrum[0], strength, "special_bleach", registrar);
		this.addStainRecipes(registrar, strength, Arrays.copyOfRange(spectrum, 1, spectrum.length));
	}
	
	protected void addStainRecipes(Consumer<IFinishedRecipe> registrar, int strength, Item ... spectrum) {
		this.addDyeSpectrumRecipes(Textiles.ITEMS.WOOD_STAIN.get(), Textiles.ITEMS.WOOD_BLEACH.get(), strength, registrar, spectrum);
	}
	
	protected void addDyeSpectrumRecipes(IItemProvider forwardReagent, IItemProvider reverseReagent, int strength, Consumer<IFinishedRecipe> registrar, Item ... spectrum) {
		if (spectrum.length > 2) {
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
		final INamedTag<Item> coreItem = Tags.Items.RODS_WOODEN;
		ShapedRecipeBuilder.shapedRecipe(result.get(), 10)
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
			.build(registrar);
		
		// Banners
		ShapedRecipeBuilder.shapedRecipe(banner)
			.patternLine("###")
			.patternLine("###")
			.patternLine(" I ")
			.key('#', woolTag)
			.key('I', Tags.Items.RODS_WOODEN)
			.addCriterion(hasWoolName, hasWool)
			.addCriterion(RecipeHelper.criterionName(Tags.Items.RODS_WOODEN), RecipeHelper.hasItem(Tags.Items.RODS_WOODEN))
			.build(registrar);
		
		// Carpets
		ShapedRecipeBuilder.shapedRecipe(carpet, 3)
			.patternLine("##")
			.key('#', woolTag)
			.addCriterion(hasWoolName, hasWool)
			.build(registrar);
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
	
	protected ResourceLocation nameFrom(IItemProvider output, String source) {
		return RecipeHelper.nameFrom(Textiles::createResource, output, source);
	}

	protected ResourceLocation nameFromRecipe(Item output, Item input) {
		return RecipeHelper.nameFromIngredients(Textiles::createResource, output, input);
	}

}
