package net.regions_unexplored.worldgen.rulesource;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;
import net.minecraft.world.level.levelgen.material.rule.RuleEvaluator;
import net.regions_unexplored.config.RUConfigHandler;

public record ConfigRuleSource(String key, MaterialRule onEnabled, MaterialRule onDisabled) implements MaterialRule {
	public static final MapCodec<ConfigRuleSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
		Codec.STRING.fieldOf("key").forGetter(ConfigRuleSource::key),
		MaterialRule.CODEC.fieldOf("on_enabled").forGetter(ConfigRuleSource::onEnabled),
		MaterialRule.CODEC.fieldOf("on_disabled").forGetter(ConfigRuleSource::onDisabled)
	).apply(i, ConfigRuleSource::new));

	@Override
	public MapCodec<ConfigRuleSource> codec() {
		return CODEC;
	}

	@Override
	public RuleEvaluator compile(MaterialRuleContext context) {
		MaterialRule source = RUConfigHandler.COMMON.test(key) ? onEnabled : onDisabled;
		return source.compile(context);
	}
}
