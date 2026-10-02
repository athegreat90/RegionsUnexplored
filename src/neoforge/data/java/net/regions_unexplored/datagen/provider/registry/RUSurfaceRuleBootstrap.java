package net.regions_unexplored.datagen.provider.registry;

import dev.worldgen.lithostitched.api.worldgen.material.LithostitchedMaterialConditions;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.material.condition.BiomeCondition;
import net.minecraft.world.level.levelgen.material.condition.MaterialCondition;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.regions_unexplored.block.type.dirt.AshenDirtBlock;
import net.regions_unexplored.registry.RUBlocks;
import net.regions_unexplored.registry.data.RUBiomes;
import net.regions_unexplored.registry.data.RUNoises;
import net.regions_unexplored.registry.data.RUSurfaceRules;
import net.regions_unexplored.registry.tag.RUBiomeTags;
import net.regions_unexplored.worldgen.rulesource.ConfigRuleSource;

import java.util.function.Supplier;

import static net.minecraft.world.level.levelgen.material.MaterialRules.*;

// TODO 26.3: SurfaceRules.RuleSource/ConditionSource merged into MaterialRule/MaterialCondition;
// the ON_FLOOR/UNDER_FLOOR/etc static fields on SurfaceRules were removed (now registered
// datapack entries under VanillaMaterialConditions, rebuilt here the same way
// VanillaMaterialConditions.bootstrap does); Lithostitched's SurfaceRules-based
// reference()/biome() helpers are gone - a sequence() of already-registered MaterialRule
// values (via MaterialRules.registerAndWrap) does the same job without a wrapper type.
public class RUSurfaceRuleBootstrap {
    private static final MaterialRule GRASS_BLOCK = block(Blocks.GRASS_BLOCK);
    private static final MaterialRule DIRT = block(Blocks.DIRT);
    private static final MaterialRule COARSE_DIRT = block(Blocks.COARSE_DIRT);
    private static final MaterialRule PODZOL = block(Blocks.PODZOL);
    private static final MaterialRule MUD = block(Blocks.MUD);

    private static final MaterialRule COBBLESTONE = block(Blocks.COBBLESTONE);
    private static final MaterialRule STONE = block(Blocks.STONE);
    private static final MaterialRule MOSSY_STONE = block(RUBlocks.MOSSY_STONE);
    private static final MaterialRule GRAVEL = block(Blocks.GRAVEL);

    private static final MaterialRule SAND = block(Blocks.SAND);
    private static final MaterialRule SANDSTONE = block(Blocks.SANDSTONE);
    private static final MaterialRule RED_SAND = block(Blocks.RED_SAND);
    private static final MaterialRule RED_SANDSTONE = block(Blocks.RED_SANDSTONE);

    private static final MaterialRule SNOW_BLOCK = block(Blocks.SNOW_BLOCK);
    private static final MaterialRule TERRACOTTA = block(Blocks.TERRACOTTA);

    private static final MaterialCondition RANDOM = noiseAbove(RUNoises.WEIGHTED, 0);

    private static final MaterialCondition ON_FLOOR = stoneDepthCheck(0, false, CaveSurface.FLOOR);
    private static final MaterialCondition UNDER_FLOOR = stoneDepthCheck(0, true, CaveSurface.FLOOR);
    private static final MaterialCondition DEEP_UNDER_FLOOR = stoneDepthCheck(0, true, 6, CaveSurface.FLOOR);
    private static final MaterialCondition VERY_DEEP_UNDER_FLOOR = stoneDepthCheck(0, true, 30, CaveSurface.FLOOR);
    private static final MaterialCondition ON_CEILING = stoneDepthCheck(0, false, CaveSurface.CEILING);
    private static final MaterialCondition UNDER_CEILING = stoneDepthCheck(0, true, CaveSurface.CEILING);

    public static void bootstrap(BootstrapContext<MaterialRule> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        MaterialCondition aboveWater = waterBlockCheck(0, 0);
        MaterialCondition notUnderwater = waterBlockCheck(-1, 0);
        MaterialCondition notUnderDeepWater = waterStartCheck(-6, -1);
        MaterialCondition deepslateGradient = verticalGradient("deepslate", VerticalAnchor.absolute(0), VerticalAnchor.absolute(8));

        MaterialRule grassOrDirtIfUnderwater = sequence(ifTrue(aboveWater, GRASS_BLOCK), DIRT);
        MaterialRule sandOrSandstoneIfCeiling = sequence(ifTrue(ON_CEILING, SANDSTONE), SAND);
        MaterialRule redSandOrRedSandstoneIfCeiling = sequence(ifTrue(ON_CEILING, RED_SANDSTONE), RED_SAND);
        MaterialRule gravelOrStoneIfCeiling = sequence(ifTrue(ON_CEILING, STONE), GRAVEL);

        MaterialRule powderSnowUnderRule = ifTrue(
            LithostitchedMaterialConditions.allOf(noiseCondition2d(Noises.POWDER_SNOW, 0.45D, 0.58D), aboveWater),
            block(Blocks.POWDER_SNOW)
        );
        MaterialRule powderSnowSurfaceRule = ifTrue(
            LithostitchedMaterialConditions.allOf(noiseCondition2d(Noises.POWDER_SNOW, 0.35D, 0.6D), aboveWater),
            block(Blocks.POWDER_SNOW)
        );

        var swamp = registerAndWrap(context, RUSurfaceRules.SWAMP, ifTrue(
            LithostitchedMaterialConditions.allOf(
                isBiome(biomes, RUBiomes.FEN),
                aboveY(62),
                belowY(63),
                noiseCondition2d(Noises.SWAMP, 0)
            ),
            block(Blocks.WATER)
        ));

        String prefix = "surface_and_under_surface";
        var surfaceAndUnderSurface = registerAndWrap(context, RUSurfaceRules.SURFACE_AND_UNDER_SURFACE, sequence(
            biome(context, biomes, prefix, sequence(
                ifTrue(belowY(138), DIRT),
                ifTrue(aboveY(186), SNOW_BLOCK),
                ifTrue(noiseBetween(Noises.CALCITE, -0.0125D, 0.0125D), block(Blocks.CALCITE)),
                STONE
            ), RUBiomes.REMOVED_MOUNTAINS),
            biomes(context, biomes, prefix + "/common/snow", ifTrue(notUnderwater, SNOW_BLOCK), RUBiomes.ICY_HEIGHTS, RUBiomes.SPIRES),
            biome(context, biomes, prefix, TERRACOTTA, RUBiomes.REMOVED_ARID_MOUNTAINS),
            biome(context, biomes, prefix, ifTrue(noiseAbove(0.25), TERRACOTTA), RUBiomes.BAOBAB_SAVANNA),
            biome(context, biomes, prefix, ifTrue(noiseAbove(RUNoises.SHIELD, 0), sandOrSandstoneIfCeiling), RUBiomes.JOSHUA_DESERT),
            biome(context, biomes, prefix, sandOrSandstoneIfCeiling, RUBiomes.SAGUARO_DESERT),
            biome(context, biomes, prefix, sequence(
                ifTrue(noiseBetween(RUNoises.SHIELD, -0.025, 0.025d), TERRACOTTA),
                ifTrue(noiseBetween(RUNoises.SHIELD,-0.06d, 0.06d), ifTrue(RANDOM, TERRACOTTA)),
                ifTrue(noiseAbove(RUNoises.SHIELD, 0.025d), redSandOrRedSandstoneIfCeiling)
            ), RUBiomes.OUTBACK),
            biome(context, biomes, prefix, sequence(
                ifTrue(belowY(66), gravelOrStoneIfCeiling),
                ifTrue(ON_FLOOR, waterSelector(RUBlocks.ALPHA_GRASS_BLOCK.get(), Blocks.DIRT)),
                DIRT
            ), RUBiomes.ALPHA_GROVE),
            biome(context, biomes, prefix, ifTrue(belowY(64), sandOrSandstoneIfCeiling), RUBiomes.TROPICS),
            biome(context, biomes, prefix, ifTrue(noiseAbove(Noises.SURFACE, 0.0D), MUD), RUBiomes.MARSH), // setup for CarvedLimitedPoolFeature
            biome(context, biomes, prefix, configSelector(RUBlocks.PEAT_MUD, Blocks.MUD), RUBiomes.MUDDY_RIVER),
            biomes(context, biomes, prefix + "/common/gravel", gravelOrStoneIfCeiling, RUBiomes.COLD_RIVER, RUBiomes.GRAVEL_BEACH),
            biomes(context, biomes, prefix + "/common/sand", sandOrSandstoneIfCeiling, RUBiomes.ROCKY_REEF, RUBiomes.TROPICAL_RIVER, RUBiomes.GRASSY_BEACH)
        ));

        prefix = "surface";
        var grassSurface = registerAndWrap(context, RUSurfaceRules.key("overworld/surface/common/grass"), waterSelector(GRASS_BLOCK, DIRT));

        var surface = registerAndWrap(context, RUSurfaceRules.SURFACE, sequence(
            biome(context, biomes, prefix, sequence(
                powderSnowSurfaceRule,
                ifTrue(aboveWater, SNOW_BLOCK)
            ), RUBiomes.FROZEN_PINE_TAIGA),
            surfaceAndUnderSurface,
            biome(context, biomes, prefix, sequence(
                ifTrue(
                    noiseAbove(0.21),
                    sequence(
                        ifTrue(noiseAbove(Noises.SWAMP, 0.25D), COBBLESTONE),
                        ifTrue(noiseAbove(Noises.SWAMP, 0.0D), GRAVEL),
                        STONE
                    )
                ),
                ifTrue(
                    noiseAbove(-0.06),
                    sequence(
                        ifTrue(noiseAbove(Noises.SWAMP, 0.25D), GRASS_BLOCK),
                        ifTrue(noiseAbove(Noises.SWAMP, -0.25D), COARSE_DIRT),
                        COBBLESTONE
                    )
                )
            ), RUBiomes.TOWERING_CLIFFS),
            biome(context, biomes, prefix, sequence(
                ifTrue(
                    noiseAbove(RUNoises.SHIELD, 0.2),
                    sequence(
                        ifTrue(noiseAbove(Noises.SWAMP, 0.25D), STONE),
                        ifTrue(noiseAbove(Noises.SWAMP, 0.0D), MOSSY_STONE),
                        STONE
                    )
                ),
                ifTrue(
                    noiseAbove(RUNoises.SHIELD, 0),
                    sequence(
                        ifTrue(noiseAbove(Noises.SWAMP, 0.25D), GRASS_BLOCK),
                        ifTrue(noiseAbove(Noises.SWAMP, -0.25D), COARSE_DIRT),
                        MOSSY_STONE
                    )
                )
            ), RUBiomes.MAPLE_FOREST),
            biome(context, biomes, prefix, sequence(
                ifTrue(noiseAbove(RUNoises.SHIELD, 0.4), COARSE_DIRT),
                ifTrue(noiseAbove(RUNoises.SHIELD, 0.2), PODZOL)
            ), RUBiomes.OLD_GROWTH_FOREST),
            biome(context, biomes, prefix, sequence(
                ifTrue(noiseAbove(0.2), configSelector(RUBlocks.PEAT_COARSE_DIRT, Blocks.COARSE_DIRT)),
                ifTrue(noiseAbove(-0.12), configSelector(RUBlocks.PEAT_PODZOL, Blocks.PODZOL))
            ), RUBiomes.PINE_TAIGA),
            biome(context, biomes, prefix, ifTrue(noiseAbove(0.15), ifTrue(
                LithostitchedMaterialConditions.anyOf(noiseAbove(RUNoises.SHIELD, 0.25), LithostitchedMaterialConditions.allOf(noiseAbove(RUNoises.SHIELD, 0.15), RANDOM)),
                configSelector(RUBlocks.SILT_COARSE_DIRT, Blocks.COARSE_DIRT))
            ), RUBiomes.DRY_BUSHLAND),
            biome(context, biomes, prefix, ifTrue(noiseAbove(0.15), ifTrue(
                LithostitchedMaterialConditions.anyOf(noiseAbove(RUNoises.SHIELD, 0.25), LithostitchedMaterialConditions.allOf(noiseAbove(RUNoises.SHIELD, 0.15), RANDOM)),
                configSelector(RUBlocks.SILT_PODZOL, Blocks.PODZOL))
            ), RUBiomes.AUTUMNAL_MAPLE_FOREST),
            biome(context, biomes, prefix, ifTrue(noiseAbove(0.15), configSelector(RUBlocks.PEAT_COARSE_DIRT, Blocks.COARSE_DIRT)), RUBiomes.FEN),
            biome(context, biomes, prefix, ifTrue(
                LithostitchedMaterialConditions.anyOf(noiseAbove(0.2), LithostitchedMaterialConditions.allOf(noiseAbove(-0.06), RANDOM)),
                block(Blocks.MYCELIUM)
            ), RUBiomes.FUNGAL_FEN),
            biome(context, biomes, prefix, sequence(
                ifTrue(
                    LithostitchedMaterialConditions.allOf(noiseBetween(RUNoises.SURFACE_MEDIUM, 0.18, 0.42), RANDOM),
                    sequence(
                        ifTrue(noiseAbove(RUNoises.WEIGHTED, 0.3), state(AshenDirtBlock.getSmouldering())),
                        block(RUBlocks.ASHEN_DIRT)
                    )
                ),
                ifTrue(noiseAbove(RUNoises.SURFACE_MEDIUM, 0.3), block(RUBlocks.ASH)),
                block(RUBlocks.ASHEN_DIRT)
            ), RUBiomes.ASHEN_WOODLAND),

            biome(context, biomes, prefix, ifTrue(
                LithostitchedMaterialConditions.allOf(belowY(64), noiseAbove(Noises.SWAMP, 0)),
                configSelector(RUBlocks.PEAT_MUD, Blocks.MUD)
            ), RUBiomes.BAYOU),
            biome(context, biomes, prefix, ifTrue(
                LithostitchedMaterialConditions.anyOf(noiseAbove(-0.11), noiseCondition2d(Noises.SWAMP, 0)),
                block(Blocks.MUD)
            ), RUBiomes.OLD_GROWTH_BAYOU),
            biome(context, biomes, prefix, ifTrue(LithostitchedMaterialConditions.allOf(belowY(65), noiseAbove(Noises.SWAMP, 0)), sandOrSandstoneIfCeiling), RUBiomes.TROPICS),
            biome(context, biomes, prefix, ifTrue(noiseAbove(0.2), COARSE_DIRT), RUBiomes.BAOBAB_SAVANNA),
            biome(context, biomes, prefix, COARSE_DIRT, RUBiomes.PINE_SLOPES),
            biome(context, biomes, prefix, PODZOL, RUBiomes.REDWOODS),
            biome(context, biomes, prefix, ifTrue(
                LithostitchedMaterialConditions.anyOf(noiseAbove(RUNoises.SURFACE_MEDIUM, 0.3), LithostitchedMaterialConditions.allOf(noiseAbove(RUNoises.SURFACE_MEDIUM, 0.25), RANDOM)),
                COARSE_DIRT
            ), RUBiomes.TUNDRA),
            biome(context, biomes, prefix, sequence(
                ifTrue(
                    LithostitchedMaterialConditions.anyOf(LithostitchedMaterialConditions.slope(3, Integer.MAX_VALUE), not(aboveWater)),
                    block(RUBlocks.CHALK)
                ),
                block(RUBlocks.CHALK_GRASS_BLOCK)
            ), RUBiomes.CHALK_CLIFFS),
            biomes(context, biomes, prefix + "/common/peat", waterSelector(
                configSelector(RUBlocks.PEAT_GRASS_BLOCK, Blocks.GRASS_BLOCK),
                configSelector(RUBlocks.PEAT_DIRT, Blocks.DIRT)
            ), RUBiomeTags.SURFACE_PEAT),
            biomes(context, biomes, prefix + "/common/silt", waterSelector(
                configSelector(RUBlocks.SILT_GRASS_BLOCK, Blocks.GRASS_BLOCK),
                configSelector(RUBlocks.SILT_DIRT, Blocks.DIRT)
            ), RUBiomeTags.SURFACE_SILT)
            , grassSurface
        ));

        prefix = "under_surface";
        var dirtUnderSurface = registerAndWrap(context, RUSurfaceRules.key("overworld/under_surface/common/dirt"), DIRT);

        var underSurface = registerAndWrap(context, RUSurfaceRules.UNDER_SURFACE, sequence(
            biomes(context, biomes, prefix + "/common/powder_snow", powderSnowUnderRule, RUBiomes.ICY_HEIGHTS, RUBiomes.SPIRES, RUBiomes.FROZEN_PINE_TAIGA),
            surfaceAndUnderSurface,
            biome(context, biomes, prefix, ifTrue(noiseAbove(0.225), STONE), RUBiomes.TOWERING_CLIFFS),
            biome(context, biomes, prefix, ifTrue(noiseAbove(RUNoises.SHIELD, 0.175), STONE), RUBiomes.MAPLE_FOREST),
            biome(context, biomes, prefix, STONE, RUBiomes.HYACINTH_DEEPS),
            biome(context, biomes, prefix, block(RUBlocks.ASHEN_DIRT), RUBiomes.ASHEN_WOODLAND),
            biome(context, biomes, prefix, ifTrue(
                LithostitchedMaterialConditions.allOf(noiseAbove(-0.12), belowY(64), noiseAbove(Noises.SWAMP, 0)),
                MUD
            ), RUBiomes.OLD_GROWTH_BAYOU),
            biomes(context, biomes, prefix + "/common/peat", configSelector(RUBlocks.PEAT_DIRT, Blocks.DIRT), RUBiomeTags.SURFACE_PEAT),
            biomes(context, biomes, prefix + "/common/silt", configSelector(RUBlocks.SILT_DIRT, Blocks.DIRT), RUBiomeTags.SURFACE_SILT),
            dirtUnderSurface
        ));

        var caves = sequence(
            biome(context, biomes, "caves", ifTrue(
                LithostitchedMaterialConditions.allOf(noiseAbove(Noises.SWAMP, 0), RANDOM),
                block(RUBlocks.RAW_REDSTONE_BLOCK)
            ), RUBiomes.REDSTONE_CAVES),
            biome(context, biomes, "caves", ifTrue(
                ON_FLOOR,
                sequence(
                    ifTrue(deepslateGradient, waterSelector(RUBlocks.DEEPSLATE_VIRIDESCENT_NYLIUM.get(), Blocks.DEEPSLATE)),
                    waterSelector(RUBlocks.VIRIDESCENT_NYLIUM.get(), Blocks.STONE)
                )
            ), RUBiomes.BIOSHROOM_CAVES),
            biome(context, biomes, "caves", ifTrue(
                LithostitchedMaterialConditions.allOf(ON_FLOOR, noiseAbove(RUNoises.SHIELD, -0.12)),
                waterSelector(RUBlocks.ARGILLITE_GRASS_BLOCK.get(), RUBlocks.ARGILLITE.get())
            ), RUBiomes.ANCIENT_DELTA),
            biome(context, biomes, "caves", ifTrue(
                LithostitchedMaterialConditions.allOf(ON_FLOOR, LithostitchedMaterialConditions.anyOf(noiseAbove(0.06))),
                sequence(
                    ifTrue(deepslateGradient, waterSelector(RUBlocks.DEEPSLATE_PRISMOSS.get(), Blocks.DEEPSLATE)),
                    waterSelector(RUBlocks.PRISMOSS.get(), Blocks.STONE)
                )
            ), RUBiomes.PRISMACHASM)
        );

        context.register(RUSurfaceRules.OVERWORLD, sequence(
            ifTrue(
                LithostitchedMaterialConditions.allOf(
                    abovePreliminarySurface(),
                    inBiomeTag(context, biomes, RUBiomeTags.ALL_OVERWORLD)
                ),
                sequence(
                    ifTrue(
                        ON_FLOOR,
                        sequence(
                            swamp,
                            ifTrue(
                                notUnderwater,
                                surface
                            ),
                            ifTrue(
                                isBiome(biomes, RUBiomes.HYACINTH_DEEPS),
                                sequence(
                                    ifTrue(noiseCondition2d(Noises.SWAMP, 0.15), MOSSY_STONE),
                                    GRAVEL
                                )
                            )
                        )
                    ),
                    ifTrue(notUnderDeepWater, sequence(
                        ifTrue(VERY_DEEP_UNDER_FLOOR, sequence(
                            ifTrue(isBiome(biomes, RUBiomes.CHALK_CLIFFS), block(RUBlocks.CHALK)),
                            ifTrue(isBiome(biomes, RUBiomes.REMOVED_ARID_MOUNTAINS, RUBiomes.BAOBAB_SAVANNA), TERRACOTTA)
                        )),
                        ifTrue(UNDER_FLOOR, underSurface),
                        ifTrue(DEEP_UNDER_FLOOR, ifTrue(inBiomeTag(context, biomes, RUBiomeTags.SURFACE_SAND), SANDSTONE)),
                        ifTrue(VERY_DEEP_UNDER_FLOOR, sequence(
                            ifTrue(isBiome(biomes, RUBiomes.SAGUARO_DESERT), SANDSTONE),
                            ifTrue(isBiome(biomes, RUBiomes.ICY_HEIGHTS, RUBiomes.SPIRES), block(Blocks.PACKED_ICE))
                        ))
                    )),
                    ifTrue(ON_FLOOR, sequence(
                        ifTrue(isBiome(biomes, RUBiomes.ROCKY_REEF), sandOrSandstoneIfCeiling),
                        ifTrue(isBiome(biomes, RUBiomes.MUDDY_RIVER), configSelector(RUBlocks.PEAT_MUD, Blocks.MUD))
                    ))
                )
            ),
            ifTrue(
                LithostitchedMaterialConditions.allOf(UNDER_FLOOR, inBiomeTag(context, biomes, RUBiomeTags.COLLECTION_CAVES)),
                caves
            ),
            ifTrue(
                LithostitchedMaterialConditions.allOf(UNDER_CEILING, isBiome(biomes, RUBiomes.ANCIENT_DELTA)),
                block(RUBlocks.ARGILLITE)
            )
        ));
    }

    private static MaterialRule configSelector(Supplier<Block> ruBlock, Block vanillaBlock) {
        return new ConfigRuleSource("custom_dirts", block(ruBlock), block(vanillaBlock));
    }

    private static MaterialRule waterSelector(Block aboveWater, Block underWater) {
        return waterSelector(block(aboveWater), block(underWater));
    }

    private static MaterialRule waterSelector(MaterialRule aboveWater, MaterialRule underWater) {
        return sequence(
            ifTrue(waterBlockCheck(0, 0), aboveWater),
            underWater
        );
    }

    private static MaterialCondition noiseAbove(double min) {
        return noiseCondition2d(Noises.SURFACE, min, Double.MAX_VALUE);
    }

    private static MaterialCondition noiseBetween(double min, double max) {
        return noiseCondition2d(Noises.SURFACE, min, max);
    }

    private static MaterialCondition noiseAbove(ResourceKey<NormalNoise> noise, double min) {
        return noiseCondition2d(noise, min, Double.MAX_VALUE);
    }

    private static MaterialCondition noiseBetween(ResourceKey<NormalNoise> noise, double min, double max) {
        return noiseCondition2d(noise, min, max);
    }

    private static MaterialCondition aboveY(int y) {
        return yBlockCheck(VerticalAnchor.absolute(y), 0);
    }

    private static MaterialCondition belowY(int y) {
        return not(yBlockCheck(VerticalAnchor.absolute(y), 0));
    }

    private static MaterialCondition inBiomeTag(BootstrapContext<MaterialRule> context, HolderGetter<Biome> biomes, TagKey<Biome> tag) {
        HolderSet<Biome> biomeSet = biomes.getOrThrow(tag);
        return new BiomeCondition(biomeSet);
    }

    private static MaterialRule biome(BootstrapContext<MaterialRule> context, HolderGetter<Biome> biomes, String prefix, MaterialRule source, ResourceKey<Biome> biome) {
        return registerAndWrap(context, RUSurfaceRules.key("overworld/" + prefix + "/" + biome.identifier().getPath()), ifTrue(
            isBiome(biomes, biome),
            source
        ));
    }

    @SafeVarargs
    private static MaterialRule biomes(BootstrapContext<MaterialRule> context, HolderGetter<Biome> biomes, String name, MaterialRule source, ResourceKey<Biome>... biomeKeys) {
        return registerAndWrap(context, RUSurfaceRules.key("overworld/" + name), ifTrue(
            isBiome(biomes, biomeKeys),
            source
        ));
    }

    private static MaterialRule biomes(BootstrapContext<MaterialRule> context, HolderGetter<Biome> biomes, String name, MaterialRule source, TagKey<Biome> tag) {
        return registerAndWrap(context, RUSurfaceRules.key("overworld/" + name), ifTrue(
            inBiomeTag(context, biomes, tag),
            source
        ));
    }

    private static MaterialRule block(Supplier<Block> block) {
        return block(block.get());
    }

    private static MaterialRule block(Block block) {
        return state(block.defaultBlockState());
    }
}
