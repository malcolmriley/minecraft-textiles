package paragon.minecraft.wilytextiles.internal;

import java.util.List;
import java.util.Objects;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import paragon.minecraft.wilytextiles.Textiles;

public class AddLootModifier extends LootModifier {
	
	/* Internal Fields */
	protected final ResourceLocation LOOT_TABLE;
	protected boolean isModifying = false;

	protected AddLootModifier(LootItemCondition[] conditionsIn, ResourceLocation lootTable) {
		super(conditionsIn);
		this.LOOT_TABLE = lootTable;
	}
	
	/* Abstract Method Implementation */

	@Override
	protected List<ItemStack> doApply(List<ItemStack> generated, LootContext context) {
		if (this.isModifying) return generated;
		LootTable discovered = context.getLootTable(this.LOOT_TABLE);
		if (Objects.nonNull(discovered)) {
			this.isModifying = true;
			generated.addAll(discovered.getRandomItems(context));
			this.isModifying = false;
		}
		return generated;
	}
	
	/* Serializer Implementation */
	
	protected static class Serializer extends GlobalLootModifierSerializer<AddLootModifier> {
		
		/* Constants */
		protected static final String FIELD_LOOT_TABLE = "added_loot";
		protected static final String NAME = "add_block_loot";
		
		protected static GlobalLootModifierSerializer<AddLootModifier> create() {
			return new Serializer().setRegistryName(Textiles.createResource(NAME));
		}
		
		/* Supertype Override Methods */

		@Override
		public AddLootModifier read(ResourceLocation location, JsonObject serialized, LootItemCondition[] conditions) {
			return new AddLootModifier(conditions, new ResourceLocation(GsonHelper.getAsString(serialized, Serializer.FIELD_LOOT_TABLE)));
		}

		@Override
		public JsonObject write(AddLootModifier instance) {
			JsonObject serialized = new JsonObject();
			serialized.addProperty(Serializer.FIELD_LOOT_TABLE, instance.LOOT_TABLE.toString());
			return serialized;
		}
		
	}
	
	/* Registration Class */
	
	@EventBusSubscriber(bus = Bus.MOD)
	public static final class Registrar {
		
		@SubscribeEvent
		public static void onRegisterLootModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
			event.getRegistry().register(AddLootModifier.Serializer.create());
		}

	}

}
