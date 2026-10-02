package net.regions_unexplored.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;
import net.regions_unexplored.module.platform.Registrar;
import net.regions_unexplored.worldgen.rulesource.ConfigRuleSource;

import java.util.function.Supplier;

public interface RURuleSources {
    Supplier<MapCodec<ConfigRuleSource>> CONFIG = register("config", ConfigRuleSource.CODEC);

    static <T extends MaterialRule> Supplier<MapCodec<T>> register(String name, MapCodec<T> codec) {
        return Registrar.register(BuiltInRegistries.MATERIAL_RULE_TYPE, name, () -> codec);
    }

    static void init() {
    }
}
