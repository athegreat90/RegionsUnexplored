package net.regions_unexplored.datagen.provider.registry.configured_feature;

import net.regions_unexplored.world.level.feature.*;
import net.regions_unexplored.world.level.feature.bioshroom.*;
import net.regions_unexplored.world.level.feature.tree.*;
import net.regions_unexplored.world.level.feature.tree.nether.*;
import net.regions_unexplored.worldgen.feature.CarvedLimitedPoolFeature;
import net.regions_unexplored.worldgen.feature.RUFallenTreeFeature;
import net.regions_unexplored.worldgen.feature.RURockFeature;

import dev.worldgen.lithostitched.api.worldgen.feature.LithostitchedFeatures;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ClampedNormalFloat;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.SimpleRandomSelectorFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.regions_unexplored.worldgen.feature.RUNetherForestVegetationFeature;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.OffsetPlacement;
import net.regions_unexplored.registry.RUBlocks;
import net.regions_unexplored.registry.RUFeatureTypes;
import net.regions_unexplored.registry.data.RUConfiguredFeatures;
import net.regions_unexplored.world.level.feature.configuration.GiantBioshroomConfiguration;
import net.regions_unexplored.world.level.feature.configuration.PointedRedstoneClusterConfiguration;
import net.regions_unexplored.world.level.feature.configuration.PointedRedstoneConfiguration;
import net.regions_unexplored.worldgen.stateprovider.RandomizedGroundCoverStateProvider;

import static net.regions_unexplored.datagen.provider.registry.util.RUFeatureUtils.*;
import static net.regions_unexplored.datagen.provider.registry.placed_feature.RuNetherPlacements.*;
import static net.regions_unexplored.registry.data.RUConfiguredFeatures.*;

public class RuNetherFeatures {
    public static void bootstrap(BootstrapContext<Feature> context) {
        HolderSet<Block> replaceables = context.lookup(Registries.BLOCK).getOrThrow(BlockTags.DRIPSTONE_REPLACEABLE);
        
        var bioshroomYellowLarge = register(context, TREE_YELLOW_BIOSHROOM_LARGE, new GiantYellowBioshroomFeature(BlockStateProvider.of(RUBlocks.YELLOW_BIOSHROOM_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.YELLOW_BIOSHROOM_BLOCK.get().defaultBlockState()), BlockStateProvider.of(RUBlocks.GLOWING_YELLOW_BIOSHROOM_BLOCK.get().defaultBlockState()), 5, 4));
        var bioshroomYellowSmall = register(context, TREE_YELLOW_BIOSHROOM_SMALL, new YellowBioshroomShrubFeature());
        registerSelector(context, TREE_GROUP_MYCOTOXIC_UNDERGROWTH, builder -> builder
            .add(direct(bioshroomYellowLarge), 3)
            .add(direct(bioshroomYellowSmall), 1)
        );
        registerPlaced(context, PATCH_YELLOW_BIOSHROOMS, randomPatch(weightedStates(pair(RUBlocks.TALL_YELLOW_BIOSHROOM, 1), pair(RUBlocks.YELLOW_BIOSHROOM, 10)), 16));
        registerPlaced(context, PATCH_MYCOTOXIC_MUSHROOMS, randomPatch(new RandomizedGroundCoverStateProvider(RUBlocks.MYCOTOXIC_MUSHROOMS.get()), 32));
        registerPlaced(context, PATCH_MYCOTOXIC_GRASS, randomPatch(BlockStateProvider.of(RUBlocks.MYCOTOXIC_GRASS.get()), 32));
        registerPlaced(context, PATCH_MYCOTOXIC_DAISY, randomPatch(BlockStateProvider.of(RUBlocks.MYCOTOXIC_DAISY.get()), 16));

        var brimWillow = register(context, TREE_BRIM_WILLOW, new BrimWillowFeature());
        var tallBrimWillow = register(context, TREE_TALL_BRIM_WILLOW, new TallBrimWillowFeature());
        registerSelector(context, TREE_GROUP_INFERNAL_HOLT, builder -> builder
            .add(direct(brimWillow), 1)
            .add(direct(tallBrimWillow), 1)
        );
        registerPlaced(context, PATCH_BRIMSPROUT, randomPatch(BlockStateProvider.of(RUBlocks.BRIMSPROUT.get().defaultBlockState()), 32));
        registerPlaced(context, PATCH_INFERNAL_HOLT_FIRE, randomPatch(BlockStateProvider.of(Blocks.FIRE.defaultBlockState()), 12, BlockPredicate.wouldSurvive(RUBlocks.BRIMSPROUT.get())));
        registerSingle(context, SINGLE_DORCEL, RUBlocks.DORCEL.get());
        registerSingle(context, SINGLE_BRIMWOOD_SHRUB, RUBlocks.BRIMWOOD_NATURAL_SET.getShrub());

        registerPlaced(context, GLISTERING_MEADOW_ROCK, new NetherRockFeature());
        registerPlaced(context, PATCH_GLISTERING_IVY, new GlisteringIvyFeature());
        registerPlaced(context, PATCH_GLISTERING_SPROUT, randomPatch(BlockStateProvider.of(RUBlocks.GLISTERING_SPROUT.get().defaultBlockState()), 32));
        registerPlaced(context, PATCH_GLISTERING_FERN, new SimpleBlockFeature(BlockStateProvider.of(RUBlocks.GLISTERING_FERN.get().defaultBlockState())));
        registerPlaced(context, PATCH_GLISTERING_BLOOM, new SimpleBlockFeature(BlockStateProvider.of(RUBlocks.GLISTERING_BLOOM.get().defaultBlockState())));
        registerPlaced(context, PATCH_GLISTER_SPIRE, randomPatch(BlockStateProvider.of(RUBlocks.GLISTER_SPIRE.get().defaultBlockState()), 16));
        registerPlaced(context, PATCH_GLISTER_BULB, new SimpleBlockFeature(BlockStateProvider.of(RUBlocks.GLISTER_BULB.get().defaultBlockState())));

        registerPlaced(context, PATCH_HANGING_EARLIGHT, new HangingEarlightFeature());
        registerPlaced(context, PATCH_COBALT_EARLIGHT, randomPatch(BlockStateProvider.of(RUBlocks.COBALT_EARLIGHT.get().defaultBlockState()), 6));
        registerPlaced(context, TALL_COBALT_EARLIGHT, new SimpleBlockFeature(BlockStateProvider.of(RUBlocks.TALL_COBALT_EARLIGHT.get().defaultBlockState())));
        registerPlaced(context, PATCH_COBALT_ROOTS, randomPatch(BlockStateProvider.of(RUBlocks.COBALT_ROOTS.get().defaultBlockState()), 32));
        registerPlaced(context, OBSIDIAN_SPIRE, new ObsidianSpireFeature());
        var cobalt = register(context, TREE_COBALT, new CobaltShrubFeature());
        registerRedirector(context, TREE_GROUP_BLACKSTONE_BASIN, cobalt);
        
        registerPlaced(context, POINTED_REDSTONE, new SimpleRandomSelectorFeature(HolderSet.direct(PlacementUtils.inlinePlaced(new PointedRedstoneFeature(0.5F, 0.7F, 0.5F, 0.5F), EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), OffsetPlacement.vertical(ConstantInt.of(1))), PlacementUtils.inlinePlaced(new PointedRedstoneFeature(0.5F, 0.7F, 0.5F, 0.5F), EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), OffsetPlacement.vertical(ConstantInt.of(-1))))));
        registerPlaced(context, LARGE_POINTED_REDSTONE, LithostitchedFeatures.largeDripstone(BlockStateProvider.holderOf(RUBlocks.RAW_REDSTONE_BLOCK.get()), context.lookup(Registries.BLOCK).getOrThrow(BlockTags.BASE_STONE_OVERWORLD), 30, UniformInt.of(1, 6), UniformFloat.of(0.4F, 2.0F), 0.33F, UniformFloat.of(0.3F, 0.9F), UniformFloat.of(0.4F, 1.0F), UniformFloat.of(0.0F, 0.3F), 4, 0.6F));
        registerPlaced(context, POINTED_REDSTONE_CLUSTER, new PointedRedstoneClusterFeature(new PointedRedstoneClusterConfiguration(RUBlocks.RAW_REDSTONE_BLOCK.get().defaultBlockState(), RUBlocks.REDSTONE_SPIKE.get().defaultBlockState(), replaceables, 12, UniformInt.of(3, 6), UniformInt.of(2, 8), 1, 3, UniformInt.of(2, 4), UniformFloat.of(0.3F, 0.7F), ClampedNormalFloat.of(0.0F, 0.0F, 0.0F, 0.0F), 0.1F, 3, 8)));

        //BONEMEALS

        register(context, RUConfiguredFeatures.BONEMEAL_MYCOTOXIC_NYLIUM, bonemeal(weightedStates(pair(RUBlocks.MYCOTOXIC_GRASS.get(), 95), pair(RUBlocks.YELLOW_BIOSHROOM.get(), 5))));
        register(context, RUConfiguredFeatures.BONEMEAL_GLISTERING_NYLIUM, bonemeal(weightedStates(pair(RUBlocks.GLISTERING_SPROUT.get(), 4), pair(RUBlocks.GLISTERING_BLOOM.get(), 1))));
        register(context, RUConfiguredFeatures.BONEMEAL_COBALT_NYLIUM, bonemeal(weightedStates(pair(RUBlocks.COBALT_ROOTS.get(), 99), pair(RUBlocks.COBALT_EARLIGHT.get(), 1))));
        register(context, RUConfiguredFeatures.BONEMEAL_BRIMSPROUT_NYLIUM, bonemeal(weightedStates(pair(RUBlocks.BRIMSPROUT.get(), 99), pair(RUBlocks.DORCEL.get(), 1))));
    }

    private static RUNetherForestVegetationFeature bonemeal(BlockStateProvider provider) {
        return new RUNetherForestVegetationFeature(provider, 3, 1);
    }
}
