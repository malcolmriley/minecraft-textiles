package paragon.minecraft.wilytextiles.generators;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder.PartialBlockstate;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import paragon.minecraft.library.Utilities;
import paragon.minecraft.library.datageneration.BlockStateHelper;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.blocks.AxialMultipleBlock;
import paragon.minecraft.wilytextiles.blocks.BlockBasket;
import paragon.minecraft.wilytextiles.blocks.SoakableBlock;
import paragon.minecraft.wilytextiles.blocks.TallCrop;
import paragon.minecraft.wilytextiles.init.ModBlocks;

/**
 * Data Generation class for JSON Block model files and JSON BlockState files.
 * 
 * @author Malcolm Riley
 */
final class BlockStateGenerator extends BlockStateHelper {
	
	/* Package-Available Fields */
	static final String TEXTURE_SIDES = "sides";
	static final String TEXTURE_SIDES_ALT = "sides_alt";
	static final String TEXTURE_ENDS = "ends";
	static final String TEXTURE_BOTTOM = "bottom";
	static final String TEXTURE_INNER = "inner";
	static final String TEXTURE_EXTRAS = "extras";
	static final String FIBER_TEXTURE_BASE = "retting_fiber";
	static final String BASE_FIBERS_NAME = "fiber_";

	BlockStateGenerator(DataGenerator generator, String modid, ExistingFileHelper helper) {
		super(generator, modid, helper);
	}

	@Override
	protected void registerStatesAndModels() {
		// Retting Fibers
		final VariantBlockStateBuilder fiberBuilder = this.getVariantBuilder(Textiles.BLOCKS.RAW_FIBERS.get());
		final PartialBlockstate baseFiberState = fiberBuilder.partialState();
		for (int count = 1; count <= SoakableBlock.MAX_COUNT; count += 1) {
			for (int age = 0; age <= SoakableBlock.MAX_AGE; age += 1) {
				final ModelFile model = this.models().withExistingParent(Utilities.Strings.name(FIBER_TEXTURE_BASE, String.valueOf(count), String.valueOf(age)), this.modLoc(BASE_FIBERS_NAME + count))
					.texture(TEXTURE_ENDS, this.blockFolderTexture(Utilities.Strings.name(FIBER_TEXTURE_BASE, TEXTURE_ENDS, String.valueOf(age))))
					.texture(TEXTURE_SIDES, this.blockFolderTexture(Utilities.Strings.name(FIBER_TEXTURE_BASE, TEXTURE_SIDES, String.valueOf(age))));
				final PartialBlockstate state = baseFiberState.with(SoakableBlock.COUNT, Integer.valueOf(count)).with(SoakableBlock.AGE, age);
				fiberBuilder.addModels(state, ConfiguredModel.allYRotations(model, 0, false));
			}
		}
		
		// Flax Crop
		final VariantBlockStateBuilder flaxBuilder = this.getVariantBuilder(Textiles.BLOCKS.FLAX_CROP.get());
		for (int age = 0; age <= TallCrop.MAX_AGE; age += 1) {
			final ModelFile bottomModel = this.crossCropModel(ModBlocks.Names.FLAX_CROP, "bottom", age);
			final ModelFile topModel = this.crossCropModel(ModBlocks.Names.FLAX_CROP, "top", age);
			flaxBuilder.addModels(flaxBuilder.partialState().with(TallCrop.AGE, age).with(TallCrop.BOTTOM, true), ConfiguredModel.builder().modelFile(bottomModel).build());
			flaxBuilder.addModels(flaxBuilder.partialState().with(TallCrop.AGE, age).with(TallCrop.BOTTOM, false), ConfiguredModel.builder().modelFile(topModel).build());
		}
		
		// Baskets
		this.createBasketModel(Textiles.BLOCKS.BASKET);
		this.createBasketModel(Textiles.BLOCKS.BASKET_STURDY);
		
		// Bolt of Fabric
		this.createFabricModel(Textiles.BLOCKS.FABRIC_PLAIN, "plain");
		this.createFabricModel(Textiles.BLOCKS.FABRIC_RED, "red");
		this.createFabricModel(Textiles.BLOCKS.FABRIC_ORANGE, "orange");
		this.createFabricModel(Textiles.BLOCKS.FABRIC_YELLOW, "yellow");
		this.createFabricModel(Textiles.BLOCKS.FABRIC_LIME, "lime");
		this.createFabricModel(Textiles.BLOCKS.FABRIC_GREEN, "green");
		this.createFabricModel(Textiles.BLOCKS.FABRIC_CYAN, "cyan");
		this.createFabricModel(Textiles.BLOCKS.FABRIC_LIGHT_BLUE, "light_blue");
		this.createFabricModel(Textiles.BLOCKS.FABRIC_BLUE, "blue");
		this.createFabricModel(Textiles.BLOCKS.FABRIC_PURPLE, "purple");
		this.createFabricModel(Textiles.BLOCKS.FABRIC_MAGENTA, "magenta");
		this.createFabricModel(Textiles.BLOCKS.FABRIC_PINK, "pink");
		this.createFabricModel(Textiles.BLOCKS.FABRIC_WHITE, "white");
		this.createFabricModel(Textiles.BLOCKS.FABRIC_LIGHT_GRAY, "light_gray");
		this.createFabricModel(Textiles.BLOCKS.FABRIC_GRAY, "gray");
		this.createFabricModel(Textiles.BLOCKS.FABRIC_BLACK, "black");
		this.createFabricModel(Textiles.BLOCKS.FABRIC_BROWN, "brown");
		
	}
	
	/* Internal Methods */
	
	protected void createBasketModel(RegistryObject<Block> target) {
		String baseName = target.getId().getPath();
		final VariantBlockStateBuilder basketBuilder = this.getVariantBuilder(target.get());
		final ModelFile uprightModel = this.basketBlockModel(baseName, "upright");
		final ModelFile sideModel = this.basketBlockModel(baseName, "side");
		for (Direction facing : BlockBasket.FACING.getAllowedValues()) {
			if (facing == Direction.UP) {
				ConfiguredModel.Builder<?> builder = ConfiguredModel.builder().modelFile(uprightModel).nextModel().modelFile(uprightModel).rotationY(90);
				basketBuilder.addModels(basketBuilder.partialState().with(BlockBasket.FACING, facing), builder.build());
			}
			else {
				ConfiguredModel.Builder<?> builder = ConfiguredModel.builder().modelFile(sideModel);
				basketBuilder.addModels(basketBuilder.partialState().with(BlockBasket.FACING, facing), builder.rotationY((int)facing.getHorizontalAngle()).build());
			}
		}
	}
	
	protected ModelFile basketBlockModel(String name, String variant) {
		return this.models().withExistingParent(Utilities.Strings.name(name, variant), Textiles.createResource(Utilities.Strings.name("basket_base", variant)))
			.texture(TEXTURE_SIDES, this.blockFolderTexture(Utilities.Strings.name(name, TEXTURE_SIDES)))
			.texture(TEXTURE_SIDES_ALT, this.blockFolderTexture(Utilities.Strings.name(name, TEXTURE_SIDES_ALT)))
			.texture(TEXTURE_BOTTOM, this.blockFolderTexture(Utilities.Strings.name(name, TEXTURE_BOTTOM)))
			.texture(TEXTURE_INNER, this.blockFolderTexture(Utilities.Strings.name(name, TEXTURE_INNER)));
	}
	
	protected void createFabricModel(RegistryObject<Block> target, String color) {
		final VariantBlockStateBuilder fabricBuilder = this.getVariantBuilder(target.get());
		for (int count = AxialMultipleBlock.MIN_COUNT; count <= AxialMultipleBlock.MAX_COUNT; count += 1) {
			ModelFile model = this.fabricBlockModel("fabric", color, count);
			fabricBuilder.addModels(fabricBuilder.partialState().with(AxialMultipleBlock.COUNT, count).with(AxialMultipleBlock.FACING, Axis.Z), ConfiguredModel.builder().modelFile(model).nextModel().modelFile(model).rotationY(180).build());
			fabricBuilder.addModels(fabricBuilder.partialState().with(AxialMultipleBlock.COUNT, count).with(AxialMultipleBlock.FACING, Axis.X), ConfiguredModel.builder().modelFile(model).rotationY(-90).nextModel().modelFile(model).rotationY(90).build());
		}
	}
	
	protected ModelFile fabricBlockModel(String parent, String color, int count) {
		return this.models().withExistingParent(Utilities.Strings.name(parent, color, String.valueOf(count)), this.modLoc(Utilities.Strings.name(parent, String.valueOf(count))))
			.texture(TEXTURE_SIDES, this.blockFolderTexture(Utilities.Strings.name(parent, color, TEXTURE_SIDES)))
			.texture(TEXTURE_ENDS, this.blockFolderTexture(Utilities.Strings.name(parent, color, TEXTURE_ENDS)))
			.texture(TEXTURE_EXTRAS, this.blockFolderTexture(Utilities.Strings.name(parent, color, TEXTURE_EXTRAS)));
	}
	
	protected ModelFile crossCropModel(String parent, String variant, int age) {
		final String name = Utilities.Strings.name(parent, variant, String.valueOf(age));
		return this.models().withExistingParent(name, this.mcLoc("cross")).texture("cross", this.blockFolderTexture(name));
	}

}
