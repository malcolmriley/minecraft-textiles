package paragon.minecraft.wilytextiles.generators;

import java.util.function.Consumer;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
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

		// Chain Mesh
		ShapedRecipeBuilder.shapedRecipe(Textiles.ITEMS.CHAIN_MESH.get(), 5)
			.patternLine("XX")
			.patternLine("XX")
			.key('X', Items.CHAIN)
			.addCriterion(RecipeHelper.criterionName(Items.CHAIN), RecipeProvider.hasItem(Tags.Items.NUGGETS_IRON))
			.build(registrar, this.nameFromRecipe(Textiles.ITEMS.CHAIN_MESH.get(), Items.IRON_NUGGET));

		// Chainmail Armor
		final String chainmailCriterion = RecipeHelper.criterionName(Textiles.ITEMS.CHAIN_MESH);
		final InventoryChangeTrigger.Instance chainmailTrigger = RecipeProvider.hasItem(Textiles.ITEMS.CHAIN_MESH.get());
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
