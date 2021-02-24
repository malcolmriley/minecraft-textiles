package paragon.minecraft.wilytextiles.generators;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder.PartialBlockstate;
import net.minecraftforge.common.data.ExistingFileHelper;
import paragon.minecraft.library.Utilities;
import paragon.minecraft.library.datageneration.BlockStateHelper;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.blocks.SoakableBlock;

public final class BlockStateGenerator extends BlockStateHelper {
	
	/* Package-Available Fields */
	static final String FIBER_TEXTURE_SIDES = "sides";
	static final String FIBER_TEXTURE_ENDS = "ends";
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
					.texture(FIBER_TEXTURE_ENDS, this.blockFolderTexture(Utilities.Strings.name(FIBER_TEXTURE_BASE, FIBER_TEXTURE_ENDS, String.valueOf(age))))
					.texture(FIBER_TEXTURE_SIDES, this.blockFolderTexture(Utilities.Strings.name(FIBER_TEXTURE_BASE, FIBER_TEXTURE_SIDES, String.valueOf(age))));
				final PartialBlockstate state = baseFiberState.with(SoakableBlock.COUNT, Integer.valueOf(count)).with(SoakableBlock.AGE, age);
				fiberBuilder.addModels(state, ConfiguredModel.allYRotations(model, 0, false));
			}
		}
	}

}
