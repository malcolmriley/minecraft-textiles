package paragon.minecraft.wilytextiles.internal;

import java.util.Objects;
import java.util.Optional;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import paragon.minecraft.wilytextiles.Textiles;

public class BlockTagCondition implements LootItemCondition {
	
	/* Internal Fields */
	private final Optional<TagKey<Block>> BLOCK_TAG;
	
	/* Shared Fields */
	protected static Optional<LootItemConditionType> TYPE = Optional.empty();
	
	/* Constants */
	protected static final String CONDITION_NAME = "block_has_tag";
	
	protected BlockTagCondition(TagKey<Block> tag) {
		this.BLOCK_TAG = Optional.ofNullable(tag);
	}
	
	/* ILootCondition Compliance Methods */

	@Override
	public boolean test(LootContext context) {
		BlockState state = context.getParam(LootContextParams.BLOCK_STATE);
		return this.BLOCK_TAG.isPresent() && Objects.nonNull(state) ? state.is(this.BLOCK_TAG.get()) : false;
	}

	@Override
	public LootItemConditionType getType() {
		return BlockTagCondition.TYPE.orElse(null);
	}
	
	/* Serializer */
	
	protected static class BlockTagConditionSerializer implements Serializer<BlockTagCondition> {
		
		/* Constants */
		protected static final String FIELD_TAG = "tag";

		@Override
		public void serialize(JsonObject serialized, BlockTagCondition instance, JsonSerializationContext context) {
			if (instance.BLOCK_TAG.isPresent()) {
				serialized.addProperty(FIELD_TAG, instance.BLOCK_TAG.get().location().toString());
			}
		}

		@Override
		public BlockTagCondition deserialize(JsonObject serialized, JsonDeserializationContext context) {
			return new BlockTagCondition(TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(GsonHelper.getAsString(serialized, FIELD_TAG))));
		}
		
	}
	
	/* Registration Class */
	
	@EventBusSubscriber(bus = Bus.MOD)
	public static final class Registrar {
		
		@SubscribeEvent
		public static void onRegisterLootModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
			LootItemConditionType type = Registrar.registerType(CONDITION_NAME, new BlockTagCondition.BlockTagConditionSerializer());
			BlockTagCondition.TYPE = Optional.ofNullable(type);
		}
		
		/* Internal Methods */
		
		protected static LootItemConditionType registerType(String name, Serializer<? extends LootItemCondition> serializer) {
			return Registry.register(Registry.LOOT_CONDITION_TYPE, Textiles.createResource(name), new LootItemConditionType(serializer));
		}

	}

}
