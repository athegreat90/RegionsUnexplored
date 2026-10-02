package net.regions_unexplored.datagen.provider.registry.util;

import com.mojang.datafixers.util.Pair;
import dev.worldgen.lithostitched.api.worldgen.blockpredicate.LithostitchedBlockPredicates;
import dev.worldgen.lithostitched.api.worldgen.feature.LithostitchedFeatures;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.WeightedRandomSelectorFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.regions_unexplored.block.set.NaturalSet;
import net.regions_unexplored.registry.data.RUConfiguredFeatures;
import net.regions_unexplored.registry.data.RUNoises;
import net.regions_unexplored.registry.tag.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class RUFeatureUtils {

    // Block Predicates

    public static final BlockPredicate DIRT_OR_PODZOL_BELOW = BlockPredicate.matchesTag(Vec3i.ZERO.below(), RUBlockTags.DIRT_AND_PODZOL);

    // Registration

    public static Holder.Reference<Feature> register(BootstrapContext<Feature> context, ResourceKey<Feature> key, Feature feature) {
        return context.register(key, feature);
    }

    public static Holder.Reference<Feature> registerPlaced(BootstrapContext<Feature> context, ResourceKey<PlacedFeature> key, Feature feature) {
        return context.register(RUConfiguredFeatures.fromPlaced(key), feature);
    }

    public static void registerSelector(BootstrapContext<Feature> context, ResourceKey<PlacedFeature> key, UnaryOperator<WeightedList.Builder<Holder<PlacedFeature>>> operator) {
        WeightedList<Holder<PlacedFeature>> weighted = operator.apply(WeightedList.builder()).build();
        context.register(RUConfiguredFeatures.fromPlaced(key), new WeightedRandomSelectorFeature(weighted));
    }

    public static void registerSingle(BootstrapContext<Feature> context, ResourceKey<PlacedFeature> key, Block block) {
        context.register(RUConfiguredFeatures.fromPlaced(key), new SimpleBlockFeature(BlockStateProvider.of(block)));
    }

    public static void registerRedirector(BootstrapContext<Feature> context, ResourceKey<PlacedFeature> key, Holder.Reference<Feature> feature) {
        context.register(RUConfiguredFeatures.fromPlaced(key), LithostitchedFeatures.placed(direct(feature)));
    }

    public static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, PlacementModifier... placement) {
        register(context, key, RUConfiguredFeatures.fromPlaced(key), placement);
    }

    public static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<Feature> feature, PlacementModifier... placement) {
        register(context, key, context.lookup(Registries.FEATURE).getOrThrow(feature), placement);
    }

    public static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<Feature> feature, PlacementModifier... placement) {
        context.register(key, new PlacedFeature(feature, List.of(placement)));
    }

    public static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, PlacementBuilder builder) {
        register(context, key, RUConfiguredFeatures.fromPlaced(key), builder);
    }

    public static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<Feature> feature, PlacementBuilder builder) {
        register(context, key, context.lookup(Registries.FEATURE).getOrThrow(feature), builder.build());
    }

    public static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<Feature> feature, PlacementBuilder builder) {
        context.register(key, new PlacedFeature(feature, builder.build()));
    }

    public static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<Feature> feature, List<PlacementModifier> placement) {
        context.register(key, new PlacedFeature(feature, placement));
    }


    public static Holder<PlacedFeature> direct(Holder<Feature> feature) {
        return Holder.direct(new PlacedFeature(feature, List.of()));
    }

    // Features

    public static Feature block(Block block) {
        return new SimpleBlockFeature(BlockStateProvider.of(block));
    }

    public static Holder<PlacedFeature> inlinePlaced(Feature feature) {
        return Holder.direct(new PlacedFeature(Holder.direct(feature), List.of()));
    }

    public static Feature randomPatch(Supplier<Block> block, int tries, int radiusXZ, int radiusY) {
        return randomPatch(BlockStateProvider.of(block.get()), tries, radiusXZ, radiusY);
    }

    public static Feature randomPatch(BlockStateProvider stateProvider, int tries) {
        return randomPatch(stateProvider, tries, 7, 3);
    }

    public static Feature randomPatch(int tries, int radiusXZ, int radiusY, Holder<PlacedFeature> feature) {
        PlacedFeature outerPlacedFeature = new PlacedFeature(
            Holder.direct(LithostitchedFeatures.placed(feature)),
            List.of(
                CountPlacement.of(tries),
                OffsetPlacement.ofTriangle(radiusXZ, radiusY),
                BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE)
            )
        );
        return LithostitchedFeatures.placed(Holder.direct(outerPlacedFeature));
    }

    public static Feature randomPatch(BlockStateProvider stateProvider, int tries, int radiusXZ, int radiusY) {
        PlacedFeature outerPlacedFeature = new PlacedFeature(
            Holder.direct(new SimpleBlockFeature(stateProvider)),
            List.of(
                CountPlacement.of(tries),
                OffsetPlacement.ofTriangle(radiusXZ, radiusY),
                BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE)
            )
        );
        return LithostitchedFeatures.placed(Holder.direct(outerPlacedFeature));
    }

    public static Feature randomPatch(BlockStateProvider stateProvider, int tries, BlockPredicate predicate) {
        PlacedFeature outerPlacedFeature = new PlacedFeature(
            Holder.direct(new SimpleBlockFeature(stateProvider)),
            List.of(
                CountPlacement.of(tries),
                OffsetPlacement.ofTriangle(7, 3),
                BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, predicate))
            )
        );
        return LithostitchedFeatures.placed(Holder.direct(outerPlacedFeature));
    }
    // Placement Modifiers

    public static PlacementModifier noiseCount(int slope, int offset, float scale) {
        return noiseCount(RUNoises.FLOWER_DENSITY, slope, offset, scale);
    }

    public static PlacementModifier noiseCount(ResourceKey<NormalNoise> noise, int slope, int offset, float scale) {
        return dev.worldgen.lithostitched.api.worldgen.placementmodifier.LithostitchedPlacementModifiers.noiseSlope(noise, slope, offset, scale, 0);
    }

    public static PlacementModifier count(int count) {
        return CountPlacement.of(count);
    }

    public static PlacementModifier rarityFilter(int chance) {
        return RarityFilter.onAverageOnceEvery(chance);
    }

    public static PlacementModifier inSquare() {
        return InSquarePlacement.spread();
    }

    public static PlacementModifier notSubmerged() {
        return SurfaceWaterDepthFilter.forMaxDepth(0);
    }

    public static PlacementModifier airAndBlocksBelow(Block... blocks) {
        return BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesBlocks(Vec3i.ZERO.below(), List.of(blocks))));
    }

    public static PlacementModifier[] surfaceSpread(double count, Heightmap.Types heightmap) {
        return spread(count, 0, heightmap);
    }

    public static PlacementModifier[] spread(double count, int maxWaterDepth, Heightmap.Types heightmap) {
        return new PlacementModifier[] {
            count >= 1 ? CountPlacement.of((int) count) : RarityFilter.onAverageOnceEvery((int) (1 / count)),
            InSquarePlacement.spread(),
            SurfaceWaterDepthFilter.forMaxDepth(maxWaterDepth),
            HeightmapPlacement.onHeightmap(heightmap),
            BiomeFilter.biome()
        };
    }

    public static PlacementModifier[] surfaceSpread(double count, Heightmap.Types heightmap, Block survivesBelow) {
        return spread(count, 0, heightmap, survivesBelow);
    }

    public static PlacementModifier[] spread(double count, int maxWaterDepth, Heightmap.Types heightmap, Block survivesBelow) {
        return spread(
            count >= 1 ? CountPlacement.of((int) count) : RarityFilter.onAverageOnceEvery((int) (1 / count)),
            maxWaterDepth, heightmap, survivesBelow
        );
    }

    public static PlacementModifier[] spread(PlacementModifier count, int maxWaterDepth, Heightmap.Types heightmap, Block survivesBelow) {
        return new PlacementModifier[] {
            count,
            InSquarePlacement.spread(),
            SurfaceWaterDepthFilter.forMaxDepth(maxWaterDepth),
            HeightmapPlacement.onHeightmap(heightmap),
            PlacementUtils.filteredByBlockSurvival(survivesBelow),
            BiomeFilter.biome()
        };
    }

    // Block State (Providers)

    public static BlockState state(Supplier<Block> block) {
        return block.get().defaultBlockState();
    }

    public static BlockState state(Block block) {
        return block.defaultBlockState();
    }

    @SafeVarargs
    public static BlockStateProvider weightedStates(Pair<BlockState, Integer>... entries) {
        net.minecraft.util.random.WeightedList.Builder<BlockState> builder = net.minecraft.util.random.WeightedList.builder();
        for (var pair : entries) {
            builder.add(pair.getFirst(), pair.getSecond());
        }
        return new WeightedStateProvider(builder.build());
    }

    @SafeVarargs
    public static WeightedListInt weightedInts(Pair<Integer, Integer>... entries) {
        net.minecraft.util.random.WeightedList.Builder<IntProvider> builder = net.minecraft.util.random.WeightedList.builder();
        for (var pair : entries) {
            builder.add(ConstantInt.of(pair.getFirst()), pair.getSecond());
        }
        return new WeightedListInt(builder.build());
    }

    public static Pair<BlockState, Integer> pair(Supplier<Block> block) {
        return pair(block.get().defaultBlockState(), 1);
    }

    public static Pair<BlockState, Integer> pair(Supplier<Block> block, int weight) {
        return pair(block.get().defaultBlockState(), weight);
    }

    public static Pair<BlockState, Integer> pair(Block block) {
        return pair(block.defaultBlockState(), 1);
    }

    public static Pair<BlockState, Integer> pair(Block block, int weight) {
        return pair(block.defaultBlockState(), weight);
    }

    public static <T> Pair<T, Integer> pair(T state) {
        return pair(state, 1);
    }

    public static <T> Pair<T, Integer> pair(T object, int weight) {
        return Pair.of(object, weight);
    }

    public static PlacementBuilder placement() {
        return new PlacementBuilder();
    }

    public static PlacementBuilder placement(float count, Heightmap.Types heightmap) {
        return new PlacementBuilder().count(count).heightmap(heightmap);
    }
    public static PlacementBuilder placementNether(int count) {
        return new PlacementBuilder(false).count(CountOnEveryLayerPlacement.of(count));
    }

    public static PlacementBuilder placementCave(int count, Direction direction) {
        return new PlacementBuilder()
            .count(count)
            .add(PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT)
            .add(EnvironmentScanPlacement.scanningFor(direction, BlockPredicate.hasSturdyFace(direction.getOpposite()), BlockPredicate.matchesTag(BlockTags.AIR), 12))
            .add(OffsetPlacement.vertical(ConstantInt.of(-direction.getStepY())))
            .filter(BlockPredicate.ONLY_IN_AIR_PREDICATE)
        ;
    }

    public static PlacementBuilder placementTree(float count, NaturalSet set) {
        return placementTree(count, set.getSapling());
    }

    public static PlacementBuilder placementTree(float count, Block sapling) {
        return new PlacementBuilder().count(count).notSubmerged().heightmap(Heightmap.Types.OCEAN_FLOOR).filter(sapling);
    }

    public static PlacementBuilder placementSnowyTree(float count, Block sapling) {
        return new PlacementBuilder().count(count).notSubmerged().heightmap(Heightmap.Types.OCEAN_FLOOR).filter(BlockPredicate.anyOf(
            BlockPredicate.wouldSurvive(sapling),
            BlockPredicate.matchesBlocks(Vec3i.ZERO.below(), List.of(Blocks.SNOW_BLOCK))
        ));
    }

    public static class PlacementBuilder {
        PlacementModifier count = null;
        List<PlacementModifier> extras = new ArrayList<>();
        boolean applyInSquare;

        public PlacementBuilder() {
            this(true);
        }

        public PlacementBuilder(boolean applyInSquare) {
            this.applyInSquare = applyInSquare;
        }

        public PlacementBuilder count(float count) {
            if (count == 1) {
                return this;
            } else if (count < 1) {
                return count(RarityFilter.onAverageOnceEvery((int) (1 / count)));
            }
            return count(CountPlacement.of((int) count));
        }

        public PlacementBuilder count(PlacementModifier count) {
            this.count = count;
            return this;
        }

        public PlacementBuilder add(PlacementModifier modifier) {
            this.extras.add(modifier);
            return this;
        }

        public PlacementBuilder atHeight(VerticalAnchor y) {
            this.extras.add(HeightRangePlacement.of(ConstantHeight.of(y)));
            return this;
        }

        public PlacementBuilder atHeight(VerticalAnchor min, VerticalAnchor max) {
            this.extras.add(HeightRangePlacement.uniform(min, max));
            return this;
        }

        public PlacementBuilder notSubmerged() {
            return maxWaterDepth(0);
        }

        public PlacementBuilder maxWaterDepth(int maxDepth) {
            this.extras.add(SurfaceWaterDepthFilter.forMaxDepth(maxDepth));
            return this;
        }

        public PlacementBuilder heightmap(Heightmap.Types heightmap) {
            this.extras.add(HeightmapPlacement.onHeightmap(heightmap));
            return this;
        }

        public PlacementBuilder filter(Block survivesBelow) {
            this.extras.add(PlacementUtils.filteredByBlockSurvival(survivesBelow));
            return this;
        }

        public PlacementBuilder filter(BlockPredicate predicate) {
            this.extras.add(BlockPredicateFilter.forPredicate(predicate));
            return this;
        }

        public PlacementBuilder notInStructure() {
            this.extras.add(BlockPredicateFilter.forPredicate(BlockPredicate.not(LithostitchedBlockPredicates.inStructure(4))));
            return this;
        }

        public List<PlacementModifier> build() {
            List<PlacementModifier> modifiers = new ArrayList<>();
            if (count != null) modifiers.add(count);
            if (applyInSquare) modifiers.add(inSquare());
            modifiers.addAll(extras);
            modifiers.add(BiomeFilter.biome());
            return modifiers;
        }
    }
}
