package net.regions_unexplored.world.surface;

import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.material.MaterialRules;
import net.minecraft.world.level.levelgen.material.condition.MaterialCondition;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.regions_unexplored.registry.RUBlocks;
import net.regions_unexplored.registry.data.RUNoises;
import net.regions_unexplored.registry.data.RUBiomes;

import java.util.function.Supplier;

import static net.minecraft.world.level.levelgen.material.MaterialRules.*;

public class RUSurfaceRuleBuilder {
    //FILL_BLOCKS
    private static final MaterialRule AIR = makeStateRule(Blocks.CAVE_AIR);
    private static final MaterialRule LAVA = makeStateRule(Blocks.LAVA);

    private static final MaterialRule NETHERRACK = makeStateRule(Blocks.NETHERRACK);
    private static final MaterialRule BLACKSTONE = makeStateRule(Blocks.BLACKSTONE);
    private static final MaterialRule END_STONE = makeStateRule(Blocks.END_STONE);
    private static final MaterialRule BEDROCK = makeStateRule(Blocks.BEDROCK);

    private static final MaterialRule GRAVEL = makeStateRule(Blocks.GRAVEL);
    //NETHER_BLOCKS
    private static final MaterialRule BRIMSPROUT_NYLIUM = makeStateRule(RUBlocks.BRIMSPROUT_NYLIUM.get());
    private static final MaterialRule MYCOTOXIC_NYLIUM = makeStateRule(RUBlocks.MYCOTOXIC_NYLIUM.get());
    private static final MaterialRule GLISTERING_NYLIUM = makeStateRule(RUBlocks.GLISTERING_NYLIUM.get());
    private static final MaterialRule GLISTERING_WART = makeStateRule(RUBlocks.GLISTERING_WART.get());
    private static final MaterialRule COBALT_NYLIUM = makeStateRule(RUBlocks.COBALT_NYLIUM.get());
    private static final MaterialRule SOUL_SAND = makeStateRule(Blocks.SOUL_SAND);

    // on_floor / under_floor / under_ceiling are no longer static constants on MaterialRules (26.3) -
    // they're now registered datapack entries (see VanillaMaterialConditions). Build them directly instead.
    private static final MaterialCondition ON_FLOOR = stoneDepthCheck(0, false, CaveSurface.FLOOR);
    private static final MaterialCondition UNDER_FLOOR = stoneDepthCheck(0, true, CaveSurface.FLOOR);
    private static final MaterialCondition UNDER_CEILING = stoneDepthCheck(0, true, CaveSurface.CEILING);

    //stateRule Method
    private static MaterialRule makeStateRule(Block block) {
        return state(block.defaultBlockState());
    }

    public static MaterialRule nether(HolderGetter<Biome> biomes) {
        MaterialCondition above31 = yBlockCheck(VerticalAnchor.absolute(31), 0);
        MaterialCondition above32 = yBlockCheck(VerticalAnchor.absolute(32), 0);
        MaterialCondition start30 = yStartCheck(VerticalAnchor.absolute(30), 0);
        MaterialCondition end35 = not(yStartCheck(VerticalAnchor.absolute(35), 0));
        MaterialCondition belowTop5 = yBlockCheck(VerticalAnchor.belowTop(5), 0);
        MaterialCondition hole = hole();
        MaterialCondition soulSandLayerNoise = noiseCondition2d(Noises.SOUL_SAND_LAYER, -0.012D);
        MaterialCondition gravelLayerNoise = noiseCondition2d(Noises.GRAVEL_LAYER, -0.012D);
        MaterialCondition patchNoise = noiseCondition2d(Noises.PATCH, -0.012D);
        MaterialCondition netherrackNoise = noiseCondition2d(Noises.NETHERRACK, 0.54D);
        MaterialCondition wartNoise = noiseCondition2d(Noises.NETHER_WART, 1.17D);
        MaterialCondition stateSelectorNoise = noiseCondition2d(Noises.NETHER_STATE_SELECTOR, 0.0D);
        MaterialRule gravelPatch =
                ifTrue(patchNoise,
                        ifTrue(start30,
                                ifTrue(end35, GRAVEL)));

        return sequence(
                //Nether Roof/Floor
                ifTrue(verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK),
                ifTrue(not(verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK),
                ifTrue(belowTop5, NETHERRACK),

                ifTrue(isBiome(biomes, RUBiomes.INFERNAL_HOLT), sequence(ifTrue(UNDER_CEILING, ifTrue(stateSelectorNoise, ifTrue(not(UNDER_FLOOR), BLACKSTONE))), ifTrue(UNDER_FLOOR, ifTrue(netherrackNoise, BLACKSTONE)))),

                ifTrue(ON_FLOOR,
                        sequence(
                                ifTrue(not(above32), ifTrue(hole, LAVA)),
                                ifTrue(isBiome(biomes, RUBiomes.BLACKSTONE_BASIN), sequence(ifTrue(netherrackNoise, BLACKSTONE), ifTrue(above31, COBALT_NYLIUM))),
                                ifTrue(isBiome(biomes, RUBiomes.GLISTERING_MEADOW), ifTrue(not(noiseCondition2d(Noises.NETHERRACK, 0.45D)), ifTrue(above31, sequence(ifTrue(wartNoise, GLISTERING_WART), GLISTERING_NYLIUM)))),
                                ifTrue(isBiome(biomes, RUBiomes.MYCOTOXIC_UNDERGROWTH), ifTrue(not(netherrackNoise), ifTrue(above31, sequence(ifTrue(wartNoise, NETHERRACK), MYCOTOXIC_NYLIUM)))),
                                ifTrue(isBiome(biomes, RUBiomes.INFERNAL_HOLT), sequence(ifTrue(netherrackNoise, BLACKSTONE), ifTrue(above31, BRIMSPROUT_NYLIUM)))
                        )
                ),
                ifTrue(isBiome(biomes, RUBiomes.BLACKSTONE_BASIN), sequence(ifTrue(UNDER_CEILING, ifTrue(stateSelectorNoise, BLACKSTONE)), ifTrue(UNDER_FLOOR, BLACKSTONE))),
                ifTrue(isBiome(biomes, RUBiomes.REMOVED_REDSTONE_ABYSS), sequence(ifTrue(UNDER_FLOOR, ifTrue(soulSandLayerNoise, sequence(ifTrue(not(hole), ifTrue(start30, ifTrue(end35, SOUL_SAND))), NETHERRACK))), ifTrue(ON_FLOOR, ifTrue(above31, ifTrue(end35, ifTrue(gravelLayerNoise, sequence(ifTrue(above32, GRAVEL), ifTrue(not(hole), GRAVEL))))))))
        );
    }

    private static MaterialRule block(Supplier<Block> block) {
        return MaterialRules.state(block.get().defaultBlockState());
    }

    public static MaterialRule end() {
        return END_STONE;
    }

    public static MaterialRule air() {
        return AIR;
    }

    private static MaterialCondition noiseAbove(ResourceKey<NormalNoise> noise, double min) {
        return noiseCondition2d(noise, min / 8.25D, Double.MAX_VALUE);
    }
    private static MaterialCondition noiseBetween(ResourceKey<NormalNoise> noise, double min, double max) {
        return noiseCondition2d(noise, min / 8.25D, max / 8.25D);
    }

    private static MaterialCondition surfaceNoiseAbove(double noise) {
        return noiseCondition2d(Noises.SURFACE, noise / 8.25D, Double.MAX_VALUE);
    }

    private static MaterialCondition shieldNoise(double min, double max) {
        return noiseCondition2d(RUNoises.SHIELD, min / 8.25D, max / 8.25D);
    }

    private static MaterialCondition shieldNoise(double noise) {
        return noiseCondition2d(RUNoises.SHIELD, noise / 8.25D, Double.MAX_VALUE);
    }

}