package paragon.minecraft.wilytextiles.generators;

import java.util.stream.IntStream;

import net.minecraft.block.Block;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.ResourceLocation;
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
import paragon.minecraft.wilytextiles.blocks.CushionBlock;
import paragon.minecraft.wilytextiles.blocks.RawFiberBlock;
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
	static final String TEXTURE_TOP = "top";
	static final String TEXTURE_BOTTOM = "bottom";
	static final String TEXTURE_CROSS = "cross";
	static final String TEXTURE_INNER = "inner";
	static final String TEXTURE_EXTRAS = "extras";
	static final String BASE_FIBERS_NAME = "fiber_";

	BlockStateGenerator(DataGenerator generator, String modid, ExistingFileHelper helper) {
		super(generator, modid, helper);
	}

	@Override
	protected void registerStatesAndModels() {
		// Raw Fibers
		final VariantBlockStateBuilder fiberBuilder = this.getVariantBuilder(Textiles.BLOCKS.RAW_FIBERS.get());
		for (int count = 1; count <= RawFiberBlock.MAX_COUNT; count += 1) {
			for (int age = 0; age <= RawFiberBlock.MAX_AGE; age += 1) {
				final ModelFile model = this.models().withExistingParent(Utilities.Strings.name(ModBlocks.Names.RAW_FIBERS, String.valueOf(count), String.valueOf(age)), this.modLoc(BASE_FIBERS_NAME + count))
					.texture(TEXTURE_ENDS, this.textureForBlock(ModBlocks.Names.RAW_FIBERS, TEXTURE_ENDS, String.valueOf(age)))
					.texture(TEXTURE_SIDES, this.textureForBlock(ModBlocks.Names.RAW_FIBERS, TEXTURE_SIDES, String.valueOf(age)));
				final PartialBlockstate state = fiberBuilder.partialState().with(SoakableBlock.COUNT, Integer.valueOf(count)).with(RawFiberBlock.AGE, age);
				fiberBuilder.addModels(state, ConfiguredModel.allYRotations(model, 0, false));
			}
		}
		
		// Retted Fibers
		final VariantBlockStateBuilder rettedBuilder = this.getVariantBuilder(Textiles.BLOCKS.RETTED_FIBERS.get());
		for (int count = 1; count <= RawFiberBlock.MAX_COUNT; count += 1) {
			final ModelFile model = this.models().withExistingParent(Utilities.Strings.name(ModBlocks.Names.RETTED_FIBERS, String.valueOf(count)), this.modLoc(BASE_FIBERS_NAME + count))
				.texture(TEXTURE_ENDS, this.textureForBlock(ModBlocks.Names.RETTED_FIBERS, TEXTURE_ENDS))
				.texture(TEXTURE_SIDES, this.textureForBlock(ModBlocks.Names.RETTED_FIBERS, TEXTURE_SIDES));
			final PartialBlockstate state = rettedBuilder.partialState().with(SoakableBlock.COUNT, Integer.valueOf(count));
			rettedBuilder.addModels(state, ConfiguredModel.allYRotations(model, 0, false));
		}
		
		// Flax Crop
		final VariantBlockStateBuilder flaxBuilder = this.getVariantBuilder(Textiles.BLOCKS.FLAX_CROP.get());
		for (int age = 0; age <= TallCrop.MAX_AGE; age += 1) {
			final ModelFile bottomModel = this.crossCropModel(ModBlocks.Names.FLAX_CROP, TEXTURE_BOTTOM, age);
			final ModelFile topModel = this.crossCropModel(ModBlocks.Names.FLAX_CROP, TEXTURE_TOP, age);
			flaxBuilder.addModels(flaxBuilder.partialState().with(TallCrop.AGE, age).with(TallCrop.BOTTOM, true), ConfiguredModel.builder().modelFile(bottomModel).build());
			flaxBuilder.addModels(flaxBuilder.partialState().with(TallCrop.AGE, age).with(TallCrop.BOTTOM, false), ConfiguredModel.builder().modelFile(topModel).build());
		}
		
		// Baskets
		this.createBasketModel(Textiles.BLOCKS.BASKET);
		this.createBasketModel(Textiles.BLOCKS.BASKET_STURDY);
		
		// Bolt of Fabric
		Textiles.BLOCKS.streamFabricBlocks().forEach(this::createFabricModel);
		
		// Packed Feathers
		this.axisBlock((RotatedPillarBlock) Textiles.BLOCKS.PACKED_FEATHERS.get());
		
		// Cushion Block
		Textiles.BLOCKS.streamCushionBlocks().forEach(this::createCushionModel);
	}
	
	/* Internal Methods */
	
	protected void createCushionModel(Block target) {
		final String baseName = target.getRegistryName().getPath();
		final ResourceLocation sidesName = this.textureForBlock(baseName, TEXTURE_SIDES);
		final VariantBlockStateBuilder builder = this.getVariantBuilder(target);
		for (Direction.Axis axis : CushionBlock.AXIS.getAllowedValues()) {
			final int xRotation = this.getXRotationFrom(axis);
			final int yRotation = this.getYRotationFrom(axis);
			IntStream.rangeClosed(1, 3).forEach(suffix -> {
				String variant = String.valueOf(suffix);
				final ResourceLocation endsName = this.textureForBlock(baseName, TEXTURE_ENDS, variant);
				ConfiguredModel.Builder<?> cubeModelBuilder = ConfiguredModel.builder()
					.modelFile(this.models().cubeColumn(Utilities.Strings.name(baseName, "full", variant), sidesName, endsName))
					.rotationX(xRotation).rotationY(yRotation);
				ConfiguredModel.Builder<?> topModelBuilder = ConfiguredModel.builder()
					.modelFile(this.models().slabTop(Utilities.Strings.name(baseName, TEXTURE_TOP, variant), sidesName, endsName, endsName))
					.rotationX(xRotation).rotationY(yRotation);
				ConfiguredModel.Builder<?> bottomModelBuilder = ConfiguredModel.builder()
					.modelFile(this.models().slab(Utilities.Strings.name(baseName, TEXTURE_BOTTOM, variant), sidesName, endsName, endsName))
					.rotationX(xRotation).rotationY(yRotation);
				builder.addModels(builder.partialState().with(CushionBlock.AXIS, axis).with(CushionBlock.TYPE, SlabType.DOUBLE), cubeModelBuilder.build());
				builder.addModels(builder.partialState().with(CushionBlock.AXIS, axis).with(CushionBlock.TYPE, SlabType.BOTTOM), bottomModelBuilder.build());
				builder.addModels(builder.partialState().with(CushionBlock.AXIS, axis).with(CushionBlock.TYPE, SlabType.TOP), topModelBuilder.build());
			});
		}
	}
	
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
	
	protected void createFabricModel(Block target) {
		final VariantBlockStateBuilder fabricBuilder = this.getVariantBuilder(target);
		for (int count = AxialMultipleBlock.MIN_COUNT; count <= AxialMultipleBlock.MAX_COUNT; count += 1) {
			ModelFile model = this.fabricBlockModel(target.getRegistryName().getPath(), count);
			fabricBuilder.addModels(fabricBuilder.partialState().with(AxialMultipleBlock.COUNT, count).with(AxialMultipleBlock.FACING, Axis.Z), ConfiguredModel.builder().modelFile(model).nextModel().modelFile(model).rotationY(180).build());
			fabricBuilder.addModels(fabricBuilder.partialState().with(AxialMultipleBlock.COUNT, count).with(AxialMultipleBlock.FACING, Axis.X), ConfiguredModel.builder().modelFile(model).rotationY(-90).nextModel().modelFile(model).rotationY(90).build());
		}
	}
	
	protected ModelFile fabricBlockModel(String parent, int count) {
		return this.models().withExistingParent(Utilities.Strings.name(parent, String.valueOf(count)), this.modLoc(Utilities.Strings.name("fabric", String.valueOf(count))))
			.texture(TEXTURE_SIDES, this.textureForBlock(parent, TEXTURE_SIDES))
			.texture(TEXTURE_ENDS, this.textureForBlock(parent, TEXTURE_ENDS))
			.texture(TEXTURE_EXTRAS, this.textureForBlock(parent, TEXTURE_EXTRAS));
	}
	
	protected ModelFile crossCropModel(String parent, String variant, int age) {
		final String name = Utilities.Strings.name(parent, variant, String.valueOf(age));
		return this.models().withExistingParent(name, this.mcLoc(TEXTURE_CROSS)).texture(TEXTURE_CROSS, this.blockFolderTexture(name));
	}
	
	/* Internal Methods */
	
	protected int getXRotationFrom(Direction.Axis axis) {
		return axis.isVertical() ? 0: 90;
	}
	
	protected int getYRotationFrom(Direction.Axis axis) {
		return Axis.Z.equals(axis) ? 180 : 90;
	}
	
	protected ResourceLocation textureForBlock(String ... elements) {
		return Textiles.createResource(this.blockFolderTexture(Utilities.Strings.name(elements)));
	}

}
