package paragon.minecraft.wilytextiles.generators;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@EventBusSubscriber(bus = Bus.MOD)
public class Generators {
	
	/* Package-Shared */
	static final Gson GSON_INSTANCE = new GsonBuilder().setPrettyPrinting().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).disableHtmlEscaping().create();

	private Generators() {}

	@SubscribeEvent
	public static void onGenerateData(GatherDataEvent event) {
		final DataGenerator generator = event.getGenerator();
		final ExistingFileHelper helper = event.getExistingFileHelper();
		if (event.includeClient()) {
			generator.addProvider(new LangGenerator(generator, helper));
			generator.addProvider(new ItemModelGenerator(generator, helper));
		}
		if (event.includeServer()) {
			generator.addProvider(new RecipeGenerator(generator));
		}
	}

}
