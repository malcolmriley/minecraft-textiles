package paragon.minecraft.wilytextiles.init;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.config.ModConfig;
import paragon.minecraft.library.AbstractConfiguration;

/**
 * Forge configuration object implementation.
 * <p>
 * This class manages configuration values as well some minor checks based purely on said values.
 * 
 * @author Malcolm Riley
 */
public class ModConfiguration extends AbstractConfiguration {
	
	/* Internal Fields */
	protected double BALE_PROGRESS_CHANCE = 0.65D;
	protected double FLAX_GROWTH_MODIFIER = 1.0D;
	protected int FLAX_MIN_LIGHTLEVEL = 8;
	protected boolean SHEPHERD_TRADES_FABRIC = true;
	protected boolean WANDERER_TRADES_FABRIC = true;
	protected VillagerLevel SHEPHERD_SKILL_REQUIRED = VillagerLevel.JOURNEYMAN;
	
	protected int FALL_REDUCTION_CUSHION_SEEK = 4;
	protected boolean FALL_REDUCTION_CUSHION_ENABLED = true;
	protected boolean FALL_REDUCTION_FEATHER_ENABLED = true;
	protected double FALL_DISTANCE_BREAK_FEATHER_THRESHOLD = 0.5D;
	protected double FALL_REDUCTION_FEATHER = 0.2;
	protected double FALL_REDUCTION_CUSHION = 0.2;
	
	/* Constants */
	private static final String CHANGE_REQUIRES_RESTART = "Changes to this value will require that the current World be reloaded.";

	public ModConfiguration() {
		super(ModConfig.Type.COMMON, "WilyTextiles.toml");
	}
	
	/**
	 * @return The progression rate modifier that should be applied to retting fiber bales.
	 */
	public double baleProgressModifier() {
		return this.BALE_PROGRESS_CHANCE;
	}
	
	/**
	 * @return The growth rate modifier that should be applied to the flax crop.
	 */
	public double flaxGrowthModifier() {
		return this.FLAX_GROWTH_MODIFIER;
	}
	
	/**
	 * @return The minimum light level that the flax crop can grow in.
	 */
	public int flaxMinLight() {
		return this.FLAX_MIN_LIGHTLEVEL;
	}
	
	/**
	 * @return Whether configuration permits Shepherd villager trades.
	 */
	public boolean allowShepherdTrade() {
		return this.SHEPHERD_TRADES_FABRIC;
	}
	
	/**
	 * @return The minimum level a Shepherd villager must be 
	 */
	public int getShepherdTradeThreshold() {
		return this.SHEPHERD_SKILL_REQUIRED.getLevel();
	}
	
	/**
	 * @return Whether configuration permits wandering villager trades.
	 */
	public boolean allowWandererTrade() {
		return this.WANDERER_TRADES_FABRIC;
	}
	
	/**
	 * @return The reduction to the effective fall distance applied by falling onto a bundle of feathers.
	 */
	public double getFeatherFallReduction() {
		return this.FALL_REDUCTION_FEATHER;
	}
	
	/**
	 * @return The reduction to the effective fall distance applied per cushion in a stack of cushions.
	 */
	public double getCushionFallReduction() {
		return this.FALL_REDUCTION_CUSHION;
	}
	
	/**
	 * @return The maximum number of blocks (beyond the first) that should be considered when calculating fall distance reduction for a stack of cushions
	 */
	public int getCushionFallMaxSeek() {
		return this.FALL_REDUCTION_CUSHION_SEEK;
	}
	
	/**
	 * @return Whether or not cushions reduce effective fall distance at all
	 */
	public boolean cushionsReduceFall() {
		return this.FALL_REDUCTION_CUSHION_ENABLED;
	}
	
	/**
	 * @return Whether or not bundled feather blocks reduce fall distance at all
	 */
	public boolean feathersReduceFall() {
		return this.FALL_REDUCTION_FEATHER_ENABLED;
	}
	
	/**
	 * @return The fall distance threshold beyond which landing on a feather block will cause it to break
	 */
	public double getFeatherBlockBreakThreshold() {
		return this.FALL_DISTANCE_BREAK_FEATHER_THRESHOLD;
	}
	
	/* Supertype Override Methods */

	@Override
	protected ForgeConfigSpec buildSpec(Builder builder) {
		builder.push("General");
		this.defineValue(value -> this.BALE_PROGRESS_CHANCE = value, builder
			.comment("The chance that the \"age\" property of retting fiber bales will progress with each growth opportunity.", "Lower values mean a lower chance of progress (slower progress), whereas higher values mean a higher chance (quicker progress).")
			.defineInRange("retting_bale_tick_age_chance", 0.65D, 0.0D, 1.0D));
		this.defineValue(value -> this.FLAX_GROWTH_MODIFIER = value, builder
			.comment("A global modifier for the probability that the \"age\" value of the flax crop will increase with each growth opportunity.", "Lower values mean a lower chance of increase (slower growth), whereas higher values mean a higher chance of increase (quicker growth).")
			.defineInRange("flax_crop_growth_modifier", 1.0D, 0.0D, 1.0D));
		this.defineValue(value -> this.FLAX_MIN_LIGHTLEVEL = value, builder
			.comment("The minimum light level that Flax needs in order to grow.", "The light level at the crop's position must equal or exceed this value, else no growth will occur.")
			.defineInRange("flax_min_lightlevel", 8, 0, 15));
		this.defineValue(value -> this.SHEPHERD_TRADES_FABRIC = value, builder
			.comment("Whether Shepherd villagers should sell Fabric if they are sufficiently skilled.", CHANGE_REQUIRES_RESTART)
			.worldRestart()
			.define("shepherd_trade_fabrics", true));
		this.defineValue(value -> this.SHEPHERD_SKILL_REQUIRED = value, builder
			.comment("The minimum skill level that Shepherd villagers must reach before Fabric trades become available.", "This value will have no effect if the shepherd trading feature is disabled.", CHANGE_REQUIRES_RESTART)
			.worldRestart()
			.defineEnum("shepherd_trade_skill_threshold", VillagerLevel.JOURNEYMAN));
		this.defineValue(value -> this.WANDERER_TRADES_FABRIC = value, builder
			.comment("Whether the Wandering Trader will occasionally deal in Fabrics.", CHANGE_REQUIRES_RESTART)
			.worldRestart()
			.define("wandering_trader_trade_fabrics", true));
		this.defineValue(value -> this.FALL_REDUCTION_CUSHION_ENABLED = value, builder
			.comment("Whether cushion blocks reduce effective fall distance when landed upon.")
			.define("cushion_fall_reduction_enabled", true));
		this.defineValue(value -> this.FALL_REDUCTION_FEATHER_ENABLED = value, builder
			.comment("Whether bundled feather blocks reduce effective fall distance when landed upon.")
			.define("featherblock_fall_reduction_enabled", true));
		this.defineValue(value -> this.FALL_REDUCTION_CUSHION = value, builder
			.comment(
				"The amount each cushion in a stack of cushions will reduce effective fall distance by when landed upon.", 
				"The formula used is: (EffectiveDistance) = (RealDistance) * (1.0 - (Reduction)^(CushionQuantity))", 
				"Therefore, if this value is set to 0.2 and there are three cushions in the stack, the effective fall distance will be roughly halved.",
				"This value will have no effect if the cushion fall reduction feature is disabled.")
			.defineInRange("cushion_fall_reduction_per", 0.2, 0.0, 1.0));
		this.defineValue(value -> this.FALL_REDUCTION_CUSHION_SEEK = value, builder
			.comment(
				"The maximum number of additional blocks to consider when calculating fall damage reduction for the Cushion block. Use in conjuction with the cushion fall reduction value for fine-tuning this feature.",
				"This is the maximum number of whole block spaces BENEATH the fallen-upon cushion that will be examined. Use caution when supplying larger values.",
				"This value will have no effect if the cushion fall reduction feature is disabled.")
			.defineInRange("cushion_fall_reduction_seek", 4, 0, 16));
		this.defineValue(value -> this.FALL_REDUCTION_FEATHER = value, builder
			.comment(
				"The flat amount of effective fall distance reduction applied by landing on a bundled feather block.",
				"The formula used is (EffectiveDistance) = (RealDistance) * (1.0 - (Reduction))",
				"Therefore, a value of 0.3 will reduce the effective fall distance by 30%.",
				"This value will have no effect if the feather fall reduction feature is disabled.")
			.defineInRange("featherblock_fall_reduction", 0.2, 0.0, 1.0));
		this.defineValue(value -> this.FALL_DISTANCE_BREAK_FEATHER_THRESHOLD = value, builder
			.comment("The fall-distance threshold, in blocks, beyond which falling upon a feather bundle block will cause it to break.")
			.defineInRange("featherblock_fall_break_threshold", 0.5D, 0.0D, Double.MAX_VALUE));
		builder.pop();
		return builder.build();
	}
	
	/* Villager Skill enum */
	
	/**
	 * Enumerated type representing villager leveling for greater clarity, as it reflects the ingame and wiki nomenclature.
	 * 
	 * @author Malcolm Riley
	 */
	public static enum VillagerLevel {
		NOVICE(1),
		APPRENTICE(2),
		JOURNEYMAN(3),
		EXPERT(4),
		MASTER(5);
		
		private final int LEVEL;
		
		private VillagerLevel(int level) {
			this.LEVEL = level;
		}
		
		/**
		 * @return The actual integer level value for this {@link VillagerLevel} instance.
		 */
		public int getLevel() {
			return this.LEVEL;
		}
	}
	

}
