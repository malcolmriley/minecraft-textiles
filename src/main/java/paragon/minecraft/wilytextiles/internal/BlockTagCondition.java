package paragon.minecraft.wilytextiles.internal;

import java.util.Objects;
import java.util.Optional;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import paragon.minecraft.wilytextiles.Textiles;

public class BlockTagCondition implements ILootCondition {
	
	/* Internal Fields */
	private final Optional<INamedTag<Block>> BLOCK_TAG;
	
	/* Shared Fields */
	protected static Optional<LootConditionType> TYPE = Optional.empty();
	
	/* Constants */
	protected static final String CONDITION_NAME = "block_has_tag";
	
	protected BlockTagCondition(INamedTag<Block> tag) {
		this.BLOCK_TAG = Optional.ofNullable(tag);
	}
	
	/* ILootCondition Compliance Methods */

	@Override
	public boolean test(LootContext context) {
		BlockState state = context.get(LootParameters.BLOCK_STATE);
		return this.BLOCK_TAG.isPresent() && Objects.nonNull(state) ? state.isIn(this.BLOCK_TAG.get()) : false;
	}

	@Override
	public LootConditionType func_230419_b_() {
		return BlockTagCondition.TYPE.orElse(null);
	}
	
	/* Serializer */
	
	protected static class Serializer implements ILootSerializer<BlockTagCondition> {
		
		/* Constants */
		protected static final String FIELD_TAG = "tag";

		@Override
		public void serialize(JsonObject serialized, BlockTagCondition instance, JsonSerializationContext context) {
			if (instance.BLOCK_TAG.isPresent()) {
				serialized.addProperty(FIELD_TAG, instance.BLOCK_TAG.get().getName().toString());
			}
		}

		@Override
		public BlockTagCondition deserialize(JsonObject serialized, JsonDeserializationContext context) {
			return new BlockTagCondition(BlockTags.createOptional(new ResourceLocation(JSONUtils.getString(serialized, FIELD_TAG))));
		}
		
	}
	
	/* Registration Class */
	
	@EventBusSubscriber(bus = Bus.MOD)
	public static final class Registrar {
		
		@SubscribeEvent
		public static void onRegisterLootModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
			LootConditionType type = Registrar.registerType(CONDITION_NAME, new BlockTagCondition.Serializer());
			BlockTagCondition.TYPE = Optional.ofNullable(type);
		}
		
		/* Internal Methods */
		
		protected static LootConditionType registerType(String name, ILootSerializer<? extends ILootCondition> serializer) {
			return Registry.register(Registry.LOOT_CONDITION_TYPE, Textiles.createResource(name), new LootConditionType(serializer));
		}

	}

}
