package net.regions_unexplored.registry.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;
import net.regions_unexplored.RegionsUnexplored;

public interface RUSurfaceRules {
    ResourceKey<MaterialRule> OVERWORLD = key("overworld");

    ResourceKey<MaterialRule> CAVES = key("overworld/caves");
    ResourceKey<MaterialRule> SWAMP = key("overworld/swamp");
    ResourceKey<MaterialRule> SURFACE = key("overworld/surface");
    ResourceKey<MaterialRule> SURFACE_AND_UNDER_SURFACE = key("overworld/surface_and_under_surface");
    ResourceKey<MaterialRule> UNDER_SURFACE = key("overworld/under_surface");

    ResourceKey<MaterialRule> NETHER = key("nether");

    static ResourceKey<MaterialRule> key(String name) {
        return RegionsUnexplored.key(Registries.MATERIAL_RULE, name);
    }
}
