package paragon.minecraft.wilytextiles.init;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.config.ModConfig;
import paragon.minecraft.library.AbstractConfiguration;

public class ModConfiguration extends AbstractConfiguration {
	
	/* Internal Fields */
	protected static final ThreadLocalRandom RNG = ThreadLocalRandom.current();
	protected double BALE_PROGRESS_CHANCE = 0.65D;

	public ModConfiguration() {
		super(ModConfig.Type.COMMON, "WilyTextiles.toml");
	}
	
	public boolean shouldBaleAge() {
		return RNG.nextDouble() < this.BALE_PROGRESS_CHANCE;
	}
	
	/* Supertype Override Methods */

	@Override
	protected ForgeConfigSpec buildSpec(Builder builder) {
		builder.push("General");
		this.defineValue(value -> this.BALE_PROGRESS_CHANCE = value, builder
			.comment("The chance that the \"age\" property of retting fiber bales will progress with each growth opportunity.", "Lower values mean a lower chance (slower progress), whereas higher values mean a higher chance (quicker progress).")
			.defineInRange("retting_bale_tick_age_chance", 0.65D, 0.0D, 1.0D));
		builder.pop();
		return builder.build();
	}

}
