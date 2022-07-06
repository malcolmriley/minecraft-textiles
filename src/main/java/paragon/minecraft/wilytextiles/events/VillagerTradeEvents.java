package paragon.minecraft.wilytextiles.events;

import java.util.List;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import paragon.minecraft.wilytextiles.Textiles;

/**
 * Event manager used to register testificate trades.
 * <p>
 * Responds to both {@link VillagerTradesEvent} and {@link WandererTradesEvent} to add trades to each type of villager.
 * 
 * @author Malcolm Riley
 */
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
	
	/**
	 * Receiver method for responding to the {@link VillagerTradesEvent} in order to register trades for the Shepherd villager.
	 * 
	 * @param event - The {@link VillagerTradesEvent} received
	 */
	@SubscribeEvent
	public static void onVillagerTrade(final VillagerTradesEvent event) {
		if (Textiles.CONFIG.allowShepherdTrade() && event.getType().equals(VillagerProfession.SHEPHERD)) {
			// Documentation claims that the this should never be null. Indeed, it is populated with empty NonNullList directly before event fires.
			List<ItemListing> skillLevelTrades = event.getTrades().get(Textiles.CONFIG.getShepherdTradeThreshold());
			Textiles.ITEMS.streamFabricItems().map(VillagerTradeEvents::villagerSellsItem).forEach(skillLevelTrades::add);
		}
	}
	
	/**
	 * Receiver method for responding to the {@link WandererTradesEvent} in order to register trades for the Wandering Trader.
	 * 
	 * @param event - The {@link WandererTradesEvent} received
	 */
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
	
	/**
	 * Generates an {@link ItemListing} instance from the provided fabric {@link Item}, using the constants:
	 * <li> {@link #FABRIC_BOUGHT_PER_EMERALD}: The quantity that must be sold to the villager to receive an emerald
	 * <li> {@link #FABRIC_MAX_TRADES}: The maximum daily trade cap
	 * <li> {@link #FABRIC_XP}: The XP the villager gains for executing this trade
	 * <li> {@link #FABRIC_MULTIPLIER}: The "economic" scalar applied per trade (affects internal simulated supply/demand mechanism)
	 * 
	 * @param fabricItem - The object being traded for emeralds.
	 * @return A suitable {@link ItemListing} instance, instantiated based on the specified values.
	 */
	protected static ItemListing villagerBuysItem(Item fabricItem) {
		return new BasicItemListing(new ItemStack(fabricItem, FABRIC_BOUGHT_PER_EMERALD), new ItemStack(Items.EMERALD), FABRIC_MAX_TRADES, FABRIC_XP, FABRIC_MULTIPLIER);
	}
	
	/**
	 * Generates an {@link ItemListing} instance from the provided fabric {@link Item}, using the constants:
	 * <li> {@link #FABRIC_SOLD_PER_EMERALD}: The quantity of fabric being sold by the villager per emerald
	 * <li> {@link #FABRIC_MAX_TRADES}: The maximum daily trade cap
	 * <li> {@link #FABRIC_XP}: The XP the villager gains for executing this trade
	 * <li> {@link #FABRIC_MULTIPLIER}: The "economic" scalar applied per trade (affects internal simulated supply/demand mechanism)
	 * 
	 * @param fabricItem - The object being traded for emeralds.
	 * @return A suitable {@link ItemListing} instance, instantiated based on the specified values.
	 */
	protected static ItemListing villagerSellsItem(Item fabricItem) {
		return new BasicItemListing(1, new ItemStack(fabricItem, FABRIC_SOLD_PER_EMERALD), FABRIC_MAX_TRADES, FABRIC_XP, FABRIC_MULTIPLIER);
	}
}
