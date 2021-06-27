package paragon.minecraft.wilytextiles.generators;

import java.util.function.Consumer;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
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
	}

	/* Internal Methods */

	protected void simpleShaplessMulti(final Consumer<IFinishedRecipe> registrar, Item output, Item input, int quantity) {
		ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapelessRecipe(output, 1);
		for (int count = 0; count < quantity; count += 1) {
			builder.addIngredient(input);
		}
		builder.addCriterion(CRITERION_PREFIX + input.getRegistryName().getPath(), RecipeProvider.hasItem(input));
		builder.build(registrar, this.nameFromRecipe(output, input));
	}

	protected ResourceLocation nameFromRecipe(Item output, Item input) {
		return RecipeHelper.nameFromIngredients(Textiles::createResource, output, input);
	}

}
