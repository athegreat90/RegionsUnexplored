package net.regions_unexplored.lithostitched;

import dev.worldgen.lithostitched.api.event.AddBiomeInjectorsEvent;
import dev.worldgen.lithostitched.api.event.AddRegionsEvent;
import dev.worldgen.lithostitched.api.event.AddRegionsEvent.RegionConsumer;
import dev.worldgen.lithostitched.api.event.AddWorldgenModifiersEvent;
import dev.worldgen.lithostitched.api.util.InjectionType;
import dev.worldgen.lithostitched.api.worldgen.biomeinjector.BiomeInjector;
import dev.worldgen.lithostitched.api.worldgen.biomeinjector.ParameterBuilder;
import dev.worldgen.lithostitched.api.worldgen.modifier.WorldgenModifier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.material.NetherMaterialRules;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunctions;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.regions_unexplored.RegionsUnexplored;
import net.regions_unexplored.config.RUConfigHandler;
import net.regions_unexplored.config.state.common.BiomeTarget;
import net.regions_unexplored.registry.data.RUBiomes;
import net.regions_unexplored.registry.data.RUDensityFunctions;
import net.regions_unexplored.mixin.NoiseGeneratorSettingsAccessor;
import net.regions_unexplored.registry.data.RURegions;
import net.regions_unexplored.world.surface.RUSurfaceRuleBuilder;

public class RULithostitched {
    public static void init() {
        AddWorldgenModifiersEvent.EVENT.register((registries, consumer) -> {
            var materialRules = registries.lookupOrThrow(Registries.MATERIAL_RULE);
            var biomes = registries.lookupOrThrow(Registries.BIOME);
            consumer.accept(
                RegionsUnexplored.id("surface_rule/nether"),
                WorldgenModifier.builder().setMaterialRule(
                    materialRules.getOrThrow(NetherMaterialRules.NETHER),
                    Holder.direct(RUSurfaceRuleBuilder.nether(biomes)),
                    InjectionType.PREPEND
                )
            );
            var features = registries.lookupOrThrow(Registries.PLACED_FEATURE);
            consumer.accept(
                RegionsUnexplored.id("inferno/no_water_springs"),
                WorldgenModifier.builder().removeFeatures(
                    biomes.getOrThrow(RUBiomes.INFERNO),
                    features.getOrThrow(MiscOverworldPlacements.SPRING_WATER),
                    GenerationStep.Decoration.FLUID_SPRINGS
                )
            );
            
            // Lithostitched 2.0.4 contains WrapAquifersModifier, but does not register
            // its codec or wire it into generation. Apply the same wrapper directly
            // to the Overworld's aquifer configuration before samplers are compiled.
            var overworld = registries.lookupOrThrow(Registries.LEVEL_STEM).getValue(LevelStem.OVERWORLD);
            if (overworld != null && overworld.generator() instanceof NoiseBasedChunkGenerator generator) {
                var settings = generator.generatorSettings().value();
                var weight = registries.lookupOrThrow(Registries.DENSITY_FUNCTION).getValueOrThrow(RUDensityFunctions.INFERNO_WEIGHT);
                ((NoiseGeneratorSettingsAccessor) (Object) settings).regionsUnexplored$setAquifers(settings.aquifers().map(config -> new Aquifer.Config(
                    config.barrierNoise(),
                    DensityFunctions.rangeChoice(weight, 0.001f, 64, DensityFunctions.constant(0), config.fluidLevelFloodednessNoise()),
                    config.fluidLevelSpreadNoise(), config.lavaNoise(), config.exclusion(), config.surfaceLevel()
                )));
            }
        });

        AddRegionsEvent.EVENT.register((registries, consumer) -> {
	        Registry<Biome> registry = registries.lookupOrThrow(Registries.BIOME);
            for (var entry : RUConfigHandler.COMMON.biomeGroups.groups.entrySet()) {
                addRegion(consumer, registry, entry.getKey(), entry.getValue());
            }
            for (var entry : RUConfigHandler.COMMON.biomePlacements.placements.entrySet()) {
                addRegion(consumer, registry, entry.getKey().identifier().getPath(), entry.getValue());
            }
        });
        
        AddBiomeInjectorsEvent.EVENT.register((registries, consumer) -> {
            var registry = registries.lookupOrThrow(Registries.BIOME);
            for (var entry : RUConfigHandler.COMMON.biomePlacements.placements.entrySet()) {
                ResourceKey<Biome> biome = entry.getKey();
                BiomeTarget target = entry.getValue();
                BiomeInjector injector;
                if (!target.canGenerate()) continue;
                
                // Weighted
                if (target.getWeight().orElse(0) > 0 && target.canReplace.isPresent()) {
                    injector = BiomeInjector.builder(target.dimension).replacePartially(
                        BiomeTarget.getTargets(registry, target.canReplace.get()),
                        registry.getOrThrow(biome),
                        target.getParameters().region(RURegions.key(biome))
                    );
                }
                // Toggled
                else if (target.canReplace.isPresent()) {
                    ParameterBuilder parameters = target.getParameters();
	                target.group.ifPresent(g -> parameters.region(RURegions.key(g)));
                    
                    injector = BiomeInjector.builder(target.dimension).replacePartially(
                        BiomeTarget.getTargets(registry, target.canReplace.get()),
                        registry.getOrThrow(biome),
                        parameters
                    );
                }
                // Special
                else {
                    injector = BiomeTarget.createSpecialInjector(registries, registry.getOrThrow(biome), target);
                }
                
                if (injector != null) {
                    consumer.accept(biome.identifier(), injector);
                }
            }
        });
    }
    
    private static void addRegion(RegionConsumer consumer, Registry<Biome> registry, String name, BiomeTarget target) {
        int weight = target.getWeight().orElse(0);
        if (weight <= 0 || target.canReplace.isEmpty()) return;
        consumer.accept(
            RURegions.key(name),
            target.dimension,
            BiomeTarget.getTargets(registry, target.canReplace.get()),
            weight
        );
    }
}
