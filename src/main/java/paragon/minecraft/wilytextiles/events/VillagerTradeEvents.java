package paragon.minecraft.wilytextiles.events;

import java.util.List;

import net.minecraft.entity.merchant.villager.VillagerTrades.ITrade;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import paragon.minecraft.wilytextiles.Textiles;

@EventBusSubscriber
public final class VillagerTradeEvents {
	
	/* Constants */
	
	// Villager trade values derived by comparison to standard Wool trades
	private static final int FABRIC_BOUGHT_PER_EMERALD = 16;
	private static final int FABRIC_SOLD_PER_EMERALD = 2;
	private static final int FABRIC_MAX_TRADES = 12;
	private static final int FABRIC_XP = 2;
	private static final float FABRIC_MULTIPLIER = 0.05F;
	
	/* Event Handler Methods */
	
	@SubscribeEvent
	public static void onVillagerTrade(final VillagerTradesEvent event) {
		if (Textiles.CONFIG.allowShepherdTrade()) {
			// Documentation claims that the this should never be null. Indeed, it is populated with empty NonNullList directly before event fires.
			List<ITrade> skillLevelTrades = event.getTrades().get(Textiles.CONFIG.getShepherdTradeThreshold());
			Textiles.ITEMS.streamFabricItems().map(VillagerTradeEvents::villagerSellsItem).forEach(skillLevelTrades::add);
		}
	}
	
	@SubscribeEvent
	public static void onWanderingTrade(final WandererTradesEvent event) {
		if (Textiles.CONFIG.allowWandererTrade()) {
			Textiles.ITEMS.streamFabricItems().forEach(item -> {
				event.getGenericTrades().add(VillagerTradeEvents.villagerSellsItem(item));
				event.getRareTrades().add(VillagerTradeEvents.villagerBuysItem(item));
			});
		}
	}
	
	/* Internal Methods */
	
	protected static ITrade villagerBuysItem(Item instance) {
		return new BasicTrade(new ItemStack(instance, FABRIC_BOUGHT_PER_EMERALD), new ItemStack(Items.EMERALD), FABRIC_MAX_TRADES, FABRIC_XP, FABRIC_MULTIPLIER);
	}
	
	protected static ITrade villagerSellsItem(Item instance) {
		return new BasicTrade(1, new ItemStack(instance, FABRIC_SOLD_PER_EMERALD), FABRIC_MAX_TRADES, FABRIC_XP, FABRIC_MULTIPLIER);
	}
}
