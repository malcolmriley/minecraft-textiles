package paragon.minecraft.wilytextiles.generators;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder.PartialBlockstate;
import net.minecraftforge.common.data.ExistingFileHelper;
import paragon.minecraft.library.Utilities;
import paragon.minecraft.library.datageneration.BlockStateHelper;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.blocks.AxialMultipleBlock;
import paragon.minecraft.wilytextiles.blocks.BlockBasket;
import paragon.minecraft.wilytextiles.blocks.SoakableBlock;
import paragon.minecraft.wilytextiles.blocks.TallCrop;
import paragon.minecraft.wilytextiles.init.ModBlocks;

final class BlockStateGenerator extends BlockStateHelper {
	
	/* Package-Available Fields */
	static final String TEXTURE_SIDES = "sides";
	static final String TEXTURE_ENDS = "ends";
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
		
		// Basket
		final VariantBlockStateBuilder basketBuilder = this.getVariantBuilder(Textiles.BLOCKS.BASKET.get());
		final ModelFile uprightModel = this.models().getExistingFile(Textiles.createResource("basket_upright"));
		final ModelFile sideModel = this.models().getExistingFile(Textiles.createResource("basket_side"));
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
		
		// Bolt of Fabric
		final VariantBlockStateBuilder fabricBuilder = this.getVariantBuilder(Textiles.BLOCKS.FABRIC_RED.get());
		for (int count = AxialMultipleBlock.MIN_COUNT; count <= AxialMultipleBlock.MAX_COUNT; count += 1) {
			ModelFile model = this.fabricBlockModel("fabric", "red", count);
			fabricBuilder.addModels(fabricBuilder.partialState().with(AxialMultipleBlock.COUNT, count).with(AxialMultipleBlock.FACING, Axis.Z), ConfiguredModel.builder().modelFile(model).nextModel().modelFile(model).rotationY(180).build());
			fabricBuilder.addModels(fabricBuilder.partialState().with(AxialMultipleBlock.COUNT, count).with(AxialMultipleBlock.FACING, Axis.X), ConfiguredModel.builder().modelFile(model).rotationY(-90).nextModel().modelFile(model).rotationY(90).build());
		}
	}
	
	/* Internal Methods */
	
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
