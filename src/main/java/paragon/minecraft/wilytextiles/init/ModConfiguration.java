package paragon.minecraft.wilytextiles.init;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.config.ModConfig;
import paragon.minecraft.library.AbstractConfiguration;

public class ModConfiguration extends AbstractConfiguration {
	
	/* Internal Fields */
	protected double BALE_PROGRESS_CHANCE = 0.65D;
	protected double FLAX_GROWTH_MODIFIER = 1.0D;
	protected int FLAX_MIN_LIGHTLEVEL = 8;
	protected boolean SHEPHERD_TRADES_FABRIC = true;
	protected boolean WANDERER_TRADES_FABRIC = true;
	protected VillagerLevel SHEPHERD_SKILL_REQUIRED = VillagerLevel.JOURNEYMAN;

	public ModConfiguration() {
		super(ModConfig.Type.COMMON, "WilyTextiles.toml");
	}
	
	public boolean shouldBaleAge(BlockState state, ServerWorld world, BlockPos position, Random random) {
		return random.nextDouble() < this.BALE_PROGRESS_CHANCE;
	}

	public boolean shouldFlaxGrow(BlockState state, ServerWorld world, BlockPos position, Random random) {
		return this.isLightAdequateForFlax(world, position) && random.nextDouble() < this.FLAX_GROWTH_MODIFIER ;
	}
	
	public boolean allowShepherdTrade() {
		return this.SHEPHERD_TRADES_FABRIC;
	}
	
	public int getShepherdTradeThreshold() {
		return this.SHEPHERD_SKILL_REQUIRED.getLevel();
	}
	
	public boolean allowWandererTrade() {
		return this.WANDERER_TRADES_FABRIC;
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
			.comment("Whether Shepherd villagers should sell Fabric if they are sufficiently skilled.", "Changes to this value will require a restart.")
			.worldRestart()
			.define("shepherd_trade_fabrics", true));
		this.defineValue(value -> this.SHEPHERD_SKILL_REQUIRED = value, builder
			.comment("The minimum skill level that Shepherd villagers must reach before Fabric trades become available.", "This value will have no effect if the shepherd trading feature is disabled.", "Changes to this value will require a restart.")
			.worldRestart()
			.defineEnum("shepherd_trade_skill_threshold", VillagerLevel.JOURNEYMAN));
		this.defineValue(value -> this.WANDERER_TRADES_FABRIC = value, builder
			.comment("Whether the Wandering Trader will occasionally deal in Fabrics.", "Changes to this value will require a restart.")
			.worldRestart()
			.define("wandering_trader_trade_fabrics", true));
		builder.pop();
		return builder.build();
	}
	
	/* Villager Skill enum */
	
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
		
		public int getLevel() {
			return this.LEVEL;
		}
	}
	
	/* Internal Methods */
	
	protected boolean isLightAdequateForFlax(IBlockReader world, BlockPos position) {
		return world.getLightValue(position) >= this.FLAX_MIN_LIGHTLEVEL;
	}
	

}
