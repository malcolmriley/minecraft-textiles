package paragon.minecraft.wilytextiles.generators;

import java.util.function.Consumer;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import paragon.minecraft.wilytextiles.Textiles;

final class RecipeGenerator extends RecipeProvider {
	
	/* Internal Fields */
	private static final String CRITERION_PREFIX = "has_";

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
	}
	
	/* Internal Methods */
	
	protected void simpleShaplessMulti(final Consumer<IFinishedRecipe> registrar, Item output, Item input, int quantity) {
		ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapelessRecipe(output, 1);
		for (int count = 0; count < quantity; count += 1) {
			builder.addIngredient(input);
		}
		builder.addCriterion(CRITERION_PREFIX, RecipeProvider.hasItem(input));
		builder.build(registrar, nameFromRecipe(output, input));
	}
	
	protected static ResourceLocation nameFromRecipe(Item output, Item input) {
		return Textiles.createResource(output.getRegistryName().getPath() + "_from_" + input.getRegistryName().getPath());
	}

}
