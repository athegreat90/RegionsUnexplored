package net.regions_unexplored.datagen.provider.registry.configured_feature;

import net.regions_unexplored.world.level.feature.*;
import net.regions_unexplored.world.level.feature.bioshroom.*;
import net.regions_unexplored.world.level.feature.tree.*;
import net.regions_unexplored.world.level.feature.tree.nether.*;
import net.regions_unexplored.worldgen.feature.CarvedLimitedPoolFeature;
import net.regions_unexplored.worldgen.feature.RUFallenTreeFeature;
import net.regions_unexplored.worldgen.feature.RURockFeature;

import com.google.common.collect.ImmutableList;
import dev.worldgen.lithostitched.api.worldgen.feature.LithostitchedFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Vec3i;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.*;
import net.minecraft.world.level.levelgen.placement.*;
import net.regions_unexplored.block.set.NaturalSet;
import net.regions_unexplored.block.set.WoodSet;
import net.regions_unexplored.registry.RUBlocks;
import net.regions_unexplored.registry.RUFeatureTypes;
import net.regions_unexplored.registry.data.RUConfiguredFeatures;
import net.regions_unexplored.worldgen.foliageplacer.*;
import net.regions_unexplored.worldgen.rootplacer.MagnoliaRootPlacer;
import net.regions_unexplored.worldgen.rootplacer.WillowRootPlacer;
import net.regions_unexplored.worldgen.treedecorator.*;
import net.regions_unexplored.block.type.leaves.AppleLeavesBlock;
import net.regions_unexplored.block.type.wood.BambooLogBlock;
import net.regions_unexplored.world.level.feature.configuration.GiantBioshroomConfiguration;
import net.regions_unexplored.world.level.feature.configuration.RUTreeConfiguration;
import net.regions_unexplored.worldgen.trunkplacer.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Supplier;

import static net.regions_unexplored.registry.data.RUConfiguredFeatures.*;
import static net.regions_unexplored.datagen.provider.registry.placed_feature.RuTreePlacements.*;
import static net.regions_unexplored.datagen.provider.registry.util.RUFeatureUtils.*;

public class RuTreeFeatures {
    public static final TreeDecorator PINE_BRANCH = RandomBranchDecorator.create(0.1f, RUBlocks.PINE_NATURAL_SET, RUBlocks.PINE_WOOD_SET, 3);
    // TODO 26.3: TreeFeature now requires a belowTrunkProvider (vanilla uses the registered
    // minecraft:soil_beneath_tree block_state_provider here); no special soil transformation is
    // wired up for RU's own trees yet, so default to plain dirt (a no-op for most ground types).
    private static final Holder<BlockStateProvider> belowTrunkProvider = Holder.direct(BlockStateProvider.of(Blocks.DIRT));
    
    public static void bootstrap(BootstrapContext<Feature> context) {
        var giantBlueBioshroom = register(context, TREE_GIANT_BLUE_BIOSHROOM, new GiantBlueBioshroomFeature(BlockStateProvider.of(RUBlocks.BLUE_BIOSHROOM_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.BLUE_BIOSHROOM_BLOCK.get().defaultBlockState()), BlockStateProvider.of(RUBlocks.GLOWING_BLUE_BIOSHROOM_BLOCK.get().defaultBlockState()), 7, 7));
        var giantGreenBioshroom = register(context, TREE_GIANT_GREEN_BIOSHROOM, new GiantGreenBioshroomFeature(BlockStateProvider.of(RUBlocks.GREEN_BIOSHROOM_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.GREEN_BIOSHROOM_BLOCK.get().defaultBlockState()), BlockStateProvider.of(RUBlocks.GLOWING_GREEN_BIOSHROOM_BLOCK.get().defaultBlockState()), 8, 5));
        var giantPinkBioshroom = register(context, TREE_GIANT_PINK_BIOSHROOM, new GiantPinkBioshroomFeature(BlockStateProvider.of(RUBlocks.PINK_BIOSHROOM_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.PINK_BIOSHROOM_BLOCK.get().defaultBlockState()), BlockStateProvider.of(RUBlocks.GLOWING_PINK_BIOSHROOM_BLOCK.get().defaultBlockState()), 7, 8));
        var giantBrownBioshroom = register(context, TREE_GIANT_BROWN_MUSHROOM, new HugeBrownMushroomFeature(Holder.direct(BlockStateProvider.of(Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState().setValue(HugeMushroomBlock.UP, true).setValue(HugeMushroomBlock.DOWN, Boolean.valueOf(false)))), Holder.direct(BlockStateProvider.of(Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.valueOf(false)).setValue(HugeMushroomBlock.DOWN, Boolean.valueOf(false)))), 3, BlockPredicate.matchesTag(BlockTags.HUGE_BROWN_MUSHROOM_CAN_PLACE_ON)));
        var giantRedMushroom = register(context, TREE_GIANT_RED_MUSHROOM, new HugeRedMushroomFeature(Holder.direct(BlockStateProvider.of(Blocks.RED_MUSHROOM_BLOCK.defaultBlockState().setValue(HugeMushroomBlock.DOWN, false))), Holder.direct(BlockStateProvider.of(Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.valueOf(false)).setValue(HugeMushroomBlock.DOWN, Boolean.valueOf(false)))), 2, BlockPredicate.matchesTag(BlockTags.HUGE_RED_MUSHROOM_CAN_PLACE_ON)));

        registerSelector(context, TREE_GROUP_BIOSHROOM_CAVES, builder -> builder
            .add(direct(giantBlueBioshroom), 1)
            .add(direct(giantGreenBioshroom), 1)
        );
        
        var ashen = register(context, TREE_ASHEN, new AshenTreeFeature(BlockStateProvider.of(RUBlocks.ASHEN_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.ASHEN_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.DEAD_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 12, 5));
        var ashenPine = register(context, TREE_ASHEN_PINE, new AshenTreeFeature(BlockStateProvider.of(RUBlocks.ASHEN_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.PINE_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.DEAD_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 12, 7));
        registerPlaced(context, TREE_GROUP_ASHEN_WOODLAND, new SimpleRandomSelectorFeature(HolderSet.direct(direct(ashen), direct(ashenPine))));
        
        var acacia = register(context, TREE_ACACIA, new TreeFeature.Builder(BlockStateProvider.of(Blocks.ACACIA_LOG), new ForkingTrunkPlacer(5, 2, 2), BlockStateProvider.of(Blocks.ACACIA_LEAVES), new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)), new TwoLayersFeatureSize(1, 0, 2), belowTrunkProvider).ignoreVines().build());
        var acaciaShrub = register(context, TREE_ACACIA_SHRUB, bushSmall(Blocks.ACACIA_LOG, Blocks.ACACIA_LEAVES));
        
        register(context, TREE_ALPHA_OAK, new TreeFeature.Builder(BlockStateProvider.of(RUBlocks.ALPHA_WOOD_SET.getLog().defaultBlockState()), new StraightTrunkPlacer(4, 2, 0), BlockStateProvider.of(RUBlocks.ALPHA_NATURAL_SET.getLeaves().defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1), belowTrunkProvider).ignoreVines().build());
        
        register(context, TREE_BAMBOO, new BambooTreeFeature(new WeightedStateProvider(net.minecraft.util.random.WeightedList.<BlockState>builder().add(RUBlocks.BAMBOO_LOG.get().defaultBlockState(), 1).add(RUBlocks.BAMBOO_LOG.get().defaultBlockState().setValue(BambooLogBlock.LEAVES, true), 2)), BlockStateProvider.of(RUBlocks.BAMBOO_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.OAK_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 12, 8));
        
        register(context, TREE_FLOWERING_OAK, new TreeFeature.Builder(BlockStateProvider.of(Blocks.OAK_LOG.defaultBlockState()),new StraightTrunkPlacer(4, 3, 0),new WeightedStateProvider(net.minecraft.util.random.WeightedList.<BlockState>builder().add(Blocks.OAK_LEAVES.defaultBlockState(), 3).add(RUBlocks.FLOWERING_NATURAL_SET.getLeaves().defaultBlockState(), 1)),new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1), belowTrunkProvider).ignoreVines().build());
        register(context, TREE_BIG_FLOWERING_OAK, new TreeFeature.Builder(BlockStateProvider.of(Blocks.OAK_LOG.defaultBlockState()),new FancyTrunkPlacer(8, 11, 0),new WeightedStateProvider(net.minecraft.util.random.WeightedList.<BlockState>builder().add(Blocks.OAK_LEAVES.defaultBlockState(), 3).add(RUBlocks.FLOWERING_NATURAL_SET.getLeaves().defaultBlockState(), 1)),new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)), belowTrunkProvider).ignoreVines().build());
        
        var appleOak = register(context, TREE_APPLE_OAK, new TreeFeature.Builder(
            BlockStateProvider.of(Blocks.OAK_LOG),
            new StraightTrunkPlacer(4, 2, 2),
            AppleLeavesBlock.createStateProvider(29),
            new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
            new TwoLayersFeatureSize(1, 0, 1)
        , belowTrunkProvider).ignoreVines().build());
        var bigAppleOak = register(context, TREE_BIG_APPLE_OAK, new TreeFeature.Builder(
            BlockStateProvider.of(Blocks.OAK_LOG),
            new FancyTrunkPlacer(5, 4, 4),
            AppleLeavesBlock.createStateProvider(49),
            new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
            new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
        , belowTrunkProvider).decorators(List.of(new RandomBranchDecorator(0.1f, RUBlocks.OAK_NATURAL_SET.getBranch(), Blocks.OAK_LOG, 2, Optional.of(Holder.direct(AppleLeavesBlock.createStateProvider(2)))))).ignoreVines().build());
        
        var baobabMega = register(context, TREE_MEGA_BAOBAB, new MegaBaobabTreeFeature(BlockStateProvider.of(RUBlocks.BAOBAB_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.BAOBAB_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.BAOBAB_NATURAL_SET.getBranch()), List.of(), 5, 5));
        var baobabUltra = register(context, TREE_ULTRA_BAOBAB, new UltraBaobabTreeFeature(BlockStateProvider.of(RUBlocks.BAOBAB_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.BAOBAB_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.BAOBAB_NATURAL_SET.getBranch()), List.of(), 12, 6));
        var oakShrubSmall = register(context, TREE_OAK_SHRUB_SMALL, bushSmall(Blocks.OAK_LOG, Blocks.OAK_LEAVES));
        
        registerSelector(context, TREE_GROUP_BAOBAB_SAVANNA, builder -> builder
            .add(direct(baobabMega), 2)
            .add(direct(baobabUltra), 1)
            .add(direct(acaciaShrub), 3)
            .add(direct(oakShrubSmall), 4)
        );
        
        var blackwood = register(context, TREE_BLACKWOOD, new TreeFeature.Builder(BlockStateProvider.of(RUBlocks.BLACKWOOD_WOOD_SET.getLog().defaultBlockState()), new StraightTrunkPlacer(12, 4, 2), BlockStateProvider.of(RUBlocks.BLACKWOOD_NATURAL_SET.getLeaves().defaultBlockState()), new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(2, 2), UniformInt.of(5, 5)), new TwoLayersFeatureSize(2, 0, 2), belowTrunkProvider).ignoreVines().build());
        var bigBlackwood = register(context, TREE_BIG_BLACKWOOD, new BlackwoodTreeFeature(
            BlockStateProvider.of(RUBlocks.BLACKWOOD_WOOD_SET.getLog().defaultBlockState()),
            BlockStateProvider.of(RUBlocks.BLACKWOOD_NATURAL_SET.getLeaves().defaultBlockState()),
            BlockStateProvider.of(RUBlocks.BLACKWOOD_NATURAL_SET.getBranch().defaultBlockState()), List.of(),
            19,
            5
        ));
        var tallDarkOak = register(context, TREE_TALL_DARK_OAK, new TreeFeature.Builder(BlockStateProvider.of(Blocks.DARK_OAK_LOG), new DarkOakTrunkPlacer(8, 4, 1), BlockStateProvider.of(Blocks.DARK_OAK_LEAVES), new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)), new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()), belowTrunkProvider).ignoreVines().build());
        
        var blueBioshroom = register(context, TREE_BLUE_BIOSHROOM, new TreeFeature.Builder(
            BlockStateProvider.of(RUBlocks.BLUE_BIOSHROOM_WOOD_SET.getLog()),
            new StraightTrunkPlacer(1, 1, 1),
            BlockStateProvider.of(RUBlocks.BLUE_BIOSHROOM_BLOCK.get()),
            new BioshroomFoliagePlacer(BlockStateProvider.of(Blocks.SHROOMLIGHT)),
            new TwoLayersFeatureSize(0, 0, 0)
        , belowTrunkProvider).ignoreVines().build());
        var pinkBioshroom = register(context, TREE_PINK_BIOSHROOM, new TreeFeature.Builder(
            BlockStateProvider.of(RUBlocks.PINK_BIOSHROOM_WOOD_SET.getLog()),
            new StraightTrunkPlacer(1, 1, 1),
            BlockStateProvider.of(RUBlocks.PINK_BIOSHROOM_BLOCK.get()),
            new BioshroomFoliagePlacer(BlockStateProvider.of(Blocks.SHROOMLIGHT)),
            new TwoLayersFeatureSize(0, 0, 0)
        , belowTrunkProvider).ignoreVines().build());
        
        registerPlaced(context, TREE_GROUP_BLACKWOOD_TAIGA_PRIMARY, LithostitchedFeatures.placed(direct(bigBlackwood)));
        registerPlaced(context, TREE_GROUP_BLACKWOOD_TAIGA_SECONDARY, LithostitchedFeatures.placed(direct(blackwood)));
        registerSelector(context, TREE_GROUP_BLACKWOOD_TAIGA_TERTIARY, builder -> builder
           .add(Holder.direct(new PlacedFeature(tallDarkOak, List.of(
               BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.DARK_OAK_SAPLING))
           ))), 18)
           .add(Holder.direct(new PlacedFeature(blueBioshroom, List.of(
               HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
               SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR, Integer.MIN_VALUE, -24),
               BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                   BlockPredicate.wouldSurvive(RUBlocks.BLUE_BIOSHROOM.get()),
                   BlockPredicate.ONLY_IN_AIR_PREDICATE
               ))
           ))))
           .add(Holder.direct(new PlacedFeature(pinkBioshroom, List.of(
               HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
               SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR, Integer.MIN_VALUE, -24),
               BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                   BlockPredicate.wouldSurvive(RUBlocks.PINK_BIOSHROOM.get()),
                   BlockPredicate.ONLY_IN_AIR_PREDICATE
               ))
           ))))
       );
       
       var birchAspen = register(context, TREE_BIRCH_ASPEN, new AspenTreeFeature(BlockStateProvider.of(Blocks.BIRCH_LOG.defaultBlockState()), BlockStateProvider.of(Blocks.BIRCH_LEAVES.defaultBlockState()), BlockStateProvider.of(RUBlocks.BIRCH_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 4, 3));
       var birchAspen2 = register(context, TREE_BIRCH_ASPEN_2, new TreeFeature.Builder(
           BlockStateProvider.of(Blocks.BIRCH_LOG),
           new AspenTrunkPlacer(BiasedToBottomInt.of(6, 11), 0.5f),
           BlockStateProvider.of(Blocks.BIRCH_LEAVES),
           new AspenFoliagePlacer(),
           new TwoLayersFeatureSize(1, 0, 1)
       , belowTrunkProvider).decorators(List.of(new BeehiveDecorator(0.5f), new RandomBranchDecorator(0.1f, RUBlocks.BIRCH_NATURAL_SET.getBranch(), Blocks.BIRCH_LOG, 4, Optional.empty()))).build());



       var magnolia = register(context, TREE_MAGNOLIA, new SakuraTreeFeature(BlockStateProvider.of(RUBlocks.MAGNOLIA_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.MAGNOLIA_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.MAGNOLIA_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 1, 4));
       var blueMagnolia = register(context, TREE_BLUE_MAGNOLIA, new SakuraTreeFeature(BlockStateProvider.of(RUBlocks.MAGNOLIA_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.BLUE_MAGNOLIA_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.MAGNOLIA_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 1, 4));
       var pinkMagnolia = register(context, TREE_PINK_MAGNOLIA, new SakuraTreeFeature(BlockStateProvider.of(RUBlocks.MAGNOLIA_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.PINK_MAGNOLIA_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.MAGNOLIA_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 1, 4));
       var whiteMagnolia = register(context, TREE_WHITE_MAGNOLIA, new SakuraTreeFeature(BlockStateProvider.of(RUBlocks.MAGNOLIA_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.WHITE_MAGNOLIA_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.MAGNOLIA_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 1, 5));
       register(context, TREE_BIG_MAGNOLIA, new TreeFeature.Builder(BlockStateProvider.of(RUBlocks.MAGNOLIA_WOOD_SET.getLog().defaultBlockState()),new FancyTrunkPlacer(8, 11, 0), BlockStateProvider.of(RUBlocks.MAGNOLIA_NATURAL_SET.getLeaves().defaultBlockState()),new SakuraFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(5), 0.25F), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)), belowTrunkProvider).ignoreVines().build());
       register(context, TREE_BIG_BLUE_MAGNOLIA, new TreeFeature.Builder(BlockStateProvider.of(RUBlocks.MAGNOLIA_WOOD_SET.getLog().defaultBlockState()),new FancyTrunkPlacer(8, 11, 0), BlockStateProvider.of(RUBlocks.BLUE_MAGNOLIA_NATURAL_SET.getLeaves().defaultBlockState()),new SakuraFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(5), 0.25F), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)), belowTrunkProvider).ignoreVines().build());
       register(context, TREE_BIG_PINK_MAGNOLIA, new TreeFeature.Builder(BlockStateProvider.of(RUBlocks.MAGNOLIA_WOOD_SET.getLog().defaultBlockState()),new FancyTrunkPlacer(8, 11, 0), BlockStateProvider.of(RUBlocks.PINK_MAGNOLIA_NATURAL_SET.getLeaves().defaultBlockState()),new SakuraFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(5), 0.25F), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)), belowTrunkProvider).ignoreVines().build());
       register(context, TREE_BIG_WHITE_MAGNOLIA, new TreeFeature.Builder(BlockStateProvider.of(RUBlocks.MAGNOLIA_WOOD_SET.getLog().defaultBlockState()),new FancyTrunkPlacer(8, 11, 0), BlockStateProvider.of(RUBlocks.WHITE_MAGNOLIA_NATURAL_SET.getLeaves().defaultBlockState()),new SakuraFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(5), 0.25F), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)), belowTrunkProvider).ignoreVines().build());
       
       registerPlaced(context, TREE_GROUP_POPPY_FIELDS, LithostitchedFeatures.placed(direct(magnolia)));
       
       registerSelector(context, TREE_GROUP_MAGNOLIA_WOODLAND, builder -> builder
           .add(direct(magnolia), 1)
           .add(direct(pinkMagnolia), 1)
           .add(direct(whiteMagnolia), 1)
        );
       
       var cypress = register(context, TREE_CYPRESS, new CypressTreeFeature(BlockStateProvider.of(RUBlocks.CYPRESS_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.CYPRESS_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.CYPRESS_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 17, 4));
       var giantCypress = register(context, TREE_GIANT_CYPRESS, new GiantCypressTreeFeature(BlockStateProvider.of(RUBlocks.CYPRESS_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.CYPRESS_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.CYPRESS_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 25, 5));
       registerPlaced(context, TREE_GROUP_OLD_GROWTH_BAYOU, new RandomSelectorFeature(
           List.of(new WeightedPlacedFeature(direct(cypress), 0.4f)),
           direct(giantCypress)
       ));

       register(context, TREE_CHERRY, new TreeFeature.Builder(BlockStateProvider.of(Blocks.CHERRY_LOG), new CherryTrunkPlacer(7, 1, 0, new WeightedListInt(net.minecraft.util.random.WeightedList.<IntProvider>builder().add(ConstantInt.of(1), 1).add(ConstantInt.of(2), 1).add(ConstantInt.of(3), 1).build()), UniformInt.of(2, 4), UniformInt.of(-4, -3), UniformInt.of(-1, 0)), BlockStateProvider.of(Blocks.CHERRY_LEAVES), new CherryFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(5), 0.25F, 0.5F, 0.16666667F, 0.33333334F), new TwoLayersFeatureSize(1, 0, 2), belowTrunkProvider).ignoreVines().build());

       var deadBog = register(context, TREE_DEAD_BOG, new DeadTreeFeature(BlockStateProvider.of(RUBlocks.DEAD_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.DEAD_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.DEAD_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 6, 2));

       registerRedirector(context, TREE_GROUP_MARSH, deadBog);
       
       register(context, TREE_DEAD, new TreeFeature.Builder(BlockStateProvider.of(RUBlocks.DEAD_WOOD_SET.getLog().defaultBlockState()), new StraightTrunkPlacer(6, 2, 0), BlockStateProvider.of(RUBlocks.DEAD_NATURAL_SET.getLeaves().defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1), belowTrunkProvider).ignoreVines().build());
       register(context, TREE_BIG_DEAD, new TreeFeature.Builder(BlockStateProvider.of(RUBlocks.DEAD_WOOD_SET.getLog().defaultBlockState()),new FancyTrunkPlacer(12, 3, 0), BlockStateProvider.of(RUBlocks.DEAD_NATURAL_SET.getLeaves().defaultBlockState()),new FancyFoliagePlacer(ConstantInt.of(1), ConstantInt.of(2), 3), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)), belowTrunkProvider).ignoreVines().build());
       
       var deadPine = register(context, TREE_DEAD_PINE, new TreeFeature.Builder(
           log(RUBlocks.PINE_WOOD_SET),
           new StraightTrunkPlacer(10, 4, 0),
           leaves(RUBlocks.DEAD_PINE_NATURAL_SET),
           new FancyPineFoliagePlacer(UniformInt.of(0, 1)),
           new TwoLayersFeatureSize(3, 0, 1)
       , belowTrunkProvider).decorators(List.of(PINE_BRANCH)).build());

       var deadPineTall = register(context, TREE_DEAD_PINE_TALL, new TreeFeature.Builder(
           log(RUBlocks.PINE_WOOD_SET),
           new StraightTrunkPlacer(14, 5, 0),
           leaves(RUBlocks.DEAD_PINE_NATURAL_SET),
           new FancyPineFoliagePlacer(UniformInt.of(0, 1)),
           new TwoLayersFeatureSize(3, 0, 1)
       , belowTrunkProvider).decorators(List.of(PINE_BRANCH)).build());

       var deadPineStripped = register(context, TREE_DEAD_STRIPPED_PINE, new StrippedPineTreeFeature(BlockStateProvider.of(RUBlocks.PINE_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.DEAD_PINE_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.PINE_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 10, 4));
       var deadPineStrippedTall = register(context, TREE_DEAD_STRIPPED_PINE_TALL, new StrippedPineTreeFeature(BlockStateProvider.of(RUBlocks.PINE_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.DEAD_PINE_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.PINE_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 14, 5));
       var deadPineStrippedMountain = register(context, TREE_DEAD_STRIPPED_PINE_MOUNTAIN, new StrippedPineTreeFeature(BlockStateProvider.of(RUBlocks.PINE_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.DEAD_PINE_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.PINE_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 15, 7));

       var eucalyptusSmall = register(context, TREE_SMALL_EUCALYPTUS, new SmallEucalyptusTreeFeature(BlockStateProvider.of(RUBlocks.EUCALYPTUS_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.EUCALYPTUS_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.EUCALYPTUS_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 13, 8));
       var eucalyptus = register(context, TREE_EUCALYPTUS, new EucalyptusTreeFeature(BlockStateProvider.of(RUBlocks.EUCALYPTUS_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.EUCALYPTUS_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.EUCALYPTUS_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 14, 8));
       
       var joshuaSmall = register(context, TREE_JOSHUA_SMALL, new SmallJoshuaTreeFeature(BlockStateProvider.of(RUBlocks.JOSHUA_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.JOSHUA_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.JOSHUA_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 1, 1));
       var joshuaMedium = register(context, TREE_JOSHUA_MEDIUM, new MediumJoshuaTreeFeature());
       var joshuaLarge = register(context, TREE_JOSHUA_LARGE, new LargeJoshuaTreeFeature());

       registerPlaced(context, TREE_GROUP_JOSHUA_DESERT, new WeightedRandomSelectorFeature(WeightedList.<Holder<PlacedFeature>>builder()
           .add(direct(joshuaSmall), 4)
           .add(direct(joshuaMedium), 3)
           .add(direct(joshuaLarge), 2)
       .build()));

       var jungle = register(context, TREE_JUNGLE, new TreeFeature.Builder(BlockStateProvider.of(Blocks.JUNGLE_LOG.defaultBlockState()), new StraightTrunkPlacer(6, 5, 0), BlockStateProvider.of(Blocks.JUNGLE_LEAVES.defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1), belowTrunkProvider).decorators(ImmutableList.of(new LeaveVineDecorator(0.25f))).build());
       var bigJungle = register(context, TREE_BIG_JUNGLE, new TreeFeature.Builder(BlockStateProvider.of(Blocks.JUNGLE_LOG.defaultBlockState()), new FancyTrunkPlacer(9, 11, 0), BlockStateProvider.of(Blocks.JUNGLE_LEAVES.defaultBlockState()), new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)), belowTrunkProvider).ignoreVines().decorators(ImmutableList.of(new LeaveVineDecorator(0.25f))).build());

       var kapok = register(context, TREE_KAPOK, new KapokTreeFeature(BlockStateProvider.of(RUBlocks.KAPOK_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.KAPOK_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.KAPOK_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 20, 7));
        
       
        TreeDecorator larchBranch = RandomBranchDecorator.create(0.1f, RUBlocks.LARCH_NATURAL_SET, RUBlocks.LARCH_WOOD_SET, 3);
        var larch = register(context, TREE_LARCH, new TreeFeature.Builder(
            log(RUBlocks.LARCH_WOOD_SET),
            new StraightTrunkPlacer(6, 3, 3),
            leaves(RUBlocks.LARCH_NATURAL_SET),
            new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(1, 2), UniformInt.of(1, 2)),
            new TwoLayersFeatureSize(2, 0, 2)
        , belowTrunkProvider).decorators(List.of(larchBranch)).ignoreVines().build());
        var larchPine = register(context, TREE_LARCH_PINE, new TreeFeature.Builder(
            log(RUBlocks.LARCH_WOOD_SET),
            new StraightTrunkPlacer(8, 4, 2),
            leaves(RUBlocks.LARCH_NATURAL_SET),
            new FancyPineFoliagePlacer(UniformInt.of(1, 2)),
            new TwoLayersFeatureSize(2, 0, 2)
        , belowTrunkProvider).decorators(List.of(larchBranch)).ignoreVines().build());
        var larchLarge = register(context, TREE_LARCH_LARGE, new LarchTreeFeature(BlockStateProvider.of(RUBlocks.LARCH_WOOD_SET.getLog()), BlockStateProvider.of(RUBlocks.LARCH_NATURAL_SET.getLeaves()), BlockStateProvider.of(RUBlocks.LARCH_NATURAL_SET.getBranch()), List.of(), 18, 5));
       
       
        TreeDecorator goldenLarchBranch = RandomBranchDecorator.create(0.1f, RUBlocks.LARCH_NATURAL_SET.getBranch(), RUBlocks.LARCH_WOOD_SET.getLog(), RUBlocks.GOLDEN_LARCH_NATURAL_SET.getLeaves(), 3);
        var goldenLarch = register(context, TREE_LARCH_GOLDEN, new TreeFeature.Builder(
            log(RUBlocks.LARCH_WOOD_SET),
            new StraightTrunkPlacer(6, 3, 3),
            leaves(RUBlocks.GOLDEN_LARCH_NATURAL_SET),
            new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(1, 2), UniformInt.of(1, 2)),
            new TwoLayersFeatureSize(2, 0, 2)
        , belowTrunkProvider).decorators(List.of(goldenLarchBranch)).ignoreVines().build());
        var goldenLarchPine = register(context, TREE_LARCH_GOLDEN_PINE, new TreeFeature.Builder(
            log(RUBlocks.LARCH_WOOD_SET),
            new StraightTrunkPlacer(8, 4, 2),
            leaves(RUBlocks.GOLDEN_LARCH_NATURAL_SET),
            new FancyPineFoliagePlacer(UniformInt.of(1, 2)),
            new TwoLayersFeatureSize(2, 0, 2)
        , belowTrunkProvider).decorators(List.of(goldenLarchBranch)).ignoreVines().build());
        var goldenLarchLarge = register(context, TREE_LARCH_GOLDEN_LARGE, new LarchTreeFeature(BlockStateProvider.of(RUBlocks.LARCH_WOOD_SET.getLog()), BlockStateProvider.of(RUBlocks.GOLDEN_LARCH_NATURAL_SET.getLeaves()), BlockStateProvider.of(RUBlocks.LARCH_NATURAL_SET.getBranch()), List.of(), 18, 5));
       
       
       var maple = register(context, TREE_MAPLE, mapleSmall(RUBlocks.MAPLE_NATURAL_SET, RUBlocks.MAPLE_LEAF_LITTER));
       var bigMaple = register(context, TREE_BIG_MAPLE, new TreeFeature.Builder(BlockStateProvider.of(RUBlocks.MAPLE_WOOD_SET.getLog().defaultBlockState()),new FancyTrunkPlacer(8, 11, 0), BlockStateProvider.of(RUBlocks.MAPLE_NATURAL_SET.getLeaves().defaultBlockState()),new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)), belowTrunkProvider).decorators(List.of(PlaceOnGroundDecorator.leafLitter(RUBlocks.MAPLE_LEAF_LITTER.get(), 64))).ignoreVines().build());
       var redMaple = register(context, TREE_RED_MAPLE, mapleSmall(RUBlocks.RED_MAPLE_NATURAL_SET, RUBlocks.RED_MAPLE_LEAF_LITTER));
       var bigRedMaple = register(context, TREE_BIG_RED_MAPLE, new TreeFeature.Builder(BlockStateProvider.of(RUBlocks.MAPLE_WOOD_SET.getLog().defaultBlockState()),new FancyTrunkPlacer(8, 11, 0), BlockStateProvider.of(RUBlocks.RED_MAPLE_NATURAL_SET.getLeaves().defaultBlockState()),new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)), belowTrunkProvider).decorators(List.of(PlaceOnGroundDecorator.leafLitter(RUBlocks.RED_MAPLE_LEAF_LITTER.get(), 64))).ignoreVines().build());
       var orangeMaple = register(context, TREE_ORANGE_MAPLE, mapleSmall(RUBlocks.ORANGE_MAPLE_NATURAL_SET, RUBlocks.ORANGE_MAPLE_LEAF_LITTER));
       var bigOrangeMaple = register(context, TREE_BIG_ORANGE_MAPLE, new TreeFeature.Builder(BlockStateProvider.of(RUBlocks.MAPLE_WOOD_SET.getLog().defaultBlockState()),new FancyTrunkPlacer(8, 11, 0), BlockStateProvider.of(RUBlocks.ORANGE_MAPLE_NATURAL_SET.getLeaves().defaultBlockState()),new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)), belowTrunkProvider).decorators(List.of(PlaceOnGroundDecorator.leafLitter(RUBlocks.ORANGE_MAPLE_LEAF_LITTER.get(), 64))).ignoreVines().build());

       var silverBirch = register(context, TREE_SILVER_BIRCH, new AspenTreeFeature(BlockStateProvider.of(RUBlocks.SILVER_BIRCH_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.SILVER_BIRCH_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.SILVER_BIRCH_NATURAL_SET.getBranch().defaultBlockState()), List.of(PlaceOnGroundDecorator.leafLitter(RUBlocks.SILVER_BIRCH_LEAF_LITTER.get(), 48)), 4, 4));
       var silverBirchTall = register(context, TREE_SILVER_BIRCH_TALL, new AspenTreeFeature(BlockStateProvider.of(RUBlocks.SILVER_BIRCH_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.SILVER_BIRCH_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.SILVER_BIRCH_NATURAL_SET.getBranch().defaultBlockState()), List.of(PlaceOnGroundDecorator.leafLitter(RUBlocks.SILVER_BIRCH_LEAF_LITTER.get(), 48)), 5, 5));
       
       
       registerPlaced(context, TREE_GROUP_SILVER_BIRCH_FOREST, new WeightedRandomSelectorFeature(WeightedList.<Holder<PlacedFeature>>builder()
           .add(direct(silverBirch), 3)
           .add(direct(silverBirchTall), 1)
           .build()
       ));
       
       registerPlaced(context, TREE_GROUP_AUTUMNAL_MAPLE_FOREST, new WeightedRandomSelectorFeature(WeightedList.<Holder<PlacedFeature>>builder()
           .add(direct(maple), 2)
           .add(direct(redMaple), 2)
           .add(direct(orangeMaple), 2)
           .add(direct(silverBirch), 2)
           .add(direct(bigRedMaple))
           .add(direct(bigOrangeMaple))
       .build()));

       register(context, TREE_OAK_WITH_FLOWERS, new AspenTreeFeature(BlockStateProvider.of(Blocks.OAK_LOG.defaultBlockState()), new WeightedStateProvider(net.minecraft.util.random.WeightedList.<BlockState>builder().add(Blocks.OAK_LEAVES.defaultBlockState(), 3).add(RUBlocks.FLOWERING_NATURAL_SET.getLeaves().defaultBlockState(), 1)), BlockStateProvider.of(RUBlocks.OAK_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 5, 5));
       
       var oakWithBranch = register(context, TREE_OAK_WITH_BRANCH, mapleSmall(Blocks.OAK_LOG, Blocks.OAK_LEAVES, RUBlocks.OAK_NATURAL_SET.getBranch(), null));
       var oak = register(context, TREE_OAK, new TreeFeature.Builder(BlockStateProvider.of(Blocks.OAK_LOG.defaultBlockState()), new StraightTrunkPlacer(5, 3, 0), BlockStateProvider.of(Blocks.OAK_LEAVES.defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1), belowTrunkProvider).decorators(List.of(new BeehiveDecorator(0.005f))).ignoreVines().build());
       var tallOak = register(context, TREE_OAK_TALL, new TreeFeature.Builder(BlockStateProvider.of(Blocks.OAK_LOG.defaultBlockState()), new StraightTrunkPlacer(6, 4, 0), BlockStateProvider.of(Blocks.OAK_LEAVES.defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1), belowTrunkProvider).decorators(List.of(new BeehiveDecorator(0.005f))).ignoreVines().build());
       var bigOak = register(context, TREE_BIG_OAK, new TreeFeature.Builder(BlockStateProvider.of(Blocks.OAK_LOG.defaultBlockState()), new FancyTrunkPlacer(7, 10, 0), BlockStateProvider.of(Blocks.OAK_LEAVES.defaultBlockState()), new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)), belowTrunkProvider).ignoreVines().build());
       var oakBush = register(context, TREE_OAK_BUSH, new TreeFeature.Builder(BlockStateProvider.of(Blocks.OAK_LOG.defaultBlockState()), new StraightTrunkPlacer(1, 0, 0), BlockStateProvider.of(Blocks.OAK_LEAVES.defaultBlockState()), new BushFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 2), new TwoLayersFeatureSize(0, 0, 0), belowTrunkProvider).build());
       var oakBushWithFlowers = register(context, TREE_OAK_BUSH_WITH_FLOWERS, new TreeFeature.Builder(BlockStateProvider.of(Blocks.OAK_LOG.defaultBlockState()), new StraightTrunkPlacer(1, 0, 0), new WeightedStateProvider(net.minecraft.util.random.WeightedList.<BlockState>builder().add(Blocks.OAK_LEAVES.defaultBlockState(), 3).add(RUBlocks.FLOWERING_NATURAL_SET.getLeaves().defaultBlockState(), 1)), new BushFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 2), new TwoLayersFeatureSize(0, 0, 0), belowTrunkProvider).build());
       var smallOak = register(context, TREE_SMALL_OAK, new SmallOakTreeFeature(BlockStateProvider.of(RUBlocks.SMALL_OAK_LOG.get()), BlockStateProvider.of(Blocks.OAK_LEAVES), BlockStateProvider.of(RUBlocks.OAK_NATURAL_SET.getBranch()), List.of(), 5, 4));
       
       registerPlaced(context, TREE_GROUP_ARID_MOUNTAINS, LithostitchedFeatures.placed(direct(oakShrubSmall)));
       
       registerSelector(context, TREE_GROUP_EUCALYPTUS_FOREST, builder -> builder
           .add(direct(eucalyptus), 1)
           .add(direct(eucalyptusSmall), 1)
           .add(direct(oakBush), 1)
       );
       
       registerSelector(context, TREE_GROUP_PRAIRIE, builder -> builder
           .add(direct(oak), 2)
           .add(direct(bigOak), 1)
       );
       
       registerSelector(context, TREE_GROUP_WINDSWEPT_MAPLE_FOREST, builder -> builder
           .add(direct(maple), 3)
           .add(direct(bigMaple), 1)
           .add(direct(oakWithBranch), 1)
           .add(direct(birchAspen), 1)
       );
       
       registerSelector(context, TREE_GROUP_ORCHARD, builder -> builder
           .add(direct(appleOak), 7)
           .add(direct(bigAppleOak), 2)
           .add(direct(bigOak), 1)
       );
       registerSelector(context, TREE_GROUP_OLD_GROWTH_FOREST, builder -> builder
           .add(direct(bigOak), 4)
           .add(direct(smallOak), 4)
           .add(direct(tallOak), 2)
           .add(direct(oakBush), 1)
       );
       
       registerSelector(context, TREE_GROUP_COLD_BOREAL_TAIGA, builder -> builder
           .add(direct(larch), 6)
           .add(direct(larchPine), 2)
           .add(direct(birchAspen), 1)
           .add(direct(oakBush), 1)
       );
       
       registerSelector(context, TREE_GROUP_BOREAL_TAIGA, builder -> builder
           .add(direct(larch), 6)
           .add(direct(larchPine), 2)
           .add(direct(goldenLarch), 3)
           .add(direct(goldenLarchPine), 1)
           .add(direct(birchAspen), 1)
       );
       
        registerSelector(context, TREE_GROUP_OLD_GROWTH_BOREAL_TAIGA, builder -> builder
            .add(direct(larchLarge), 7)
            .add(direct(larch), 3)
            .add(direct(larchPine), 2)
            .add(direct(goldenLarchLarge), 2)
            .add(direct(goldenLarch), 1)
            .add(direct(birchAspen), 1)
            .add(direct(oakBush), 1)
        );
        
       registerSelector(context, TREE_GROUP_OLD_GROWTH_GOLDEN_BOREAL_TAIGA, builder -> builder
           .add(direct(goldenLarchLarge), 7)
           .add(direct(goldenLarch), 3)
           .add(direct(goldenLarchPine), 2)
           .add(direct(larchLarge), 2)
           .add(direct(larch), 1)
           .add(direct(birchAspen), 1)
           .add(direct(oakBush), 1)
       );
       
       var oakShrubLarge = register(context, TREE_OAK_SHRUB_LARGE, new TreeFeature.Builder(
           BlockStateProvider.of(Blocks.OAK_LOG.defaultBlockState()),
           new StraightTrunkPlacer(1, 0, 0),
           BlockStateProvider.of(Blocks.OAK_LEAVES.defaultBlockState()),
           new PineFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), ConstantInt.of(3)),
           new TwoLayersFeatureSize(0, 0, 0)
       , belowTrunkProvider).build());
       
       registerPlaced(context, TREE_GROUP_TUNDRA_BUSHES, new WeightedRandomSelectorFeature(WeightedList.<Holder<PlacedFeature>>builder()
           .add(direct(oakShrubSmall), 3)
           .add(direct(oakShrubLarge), 2)
           .build()));
       
       var jungleAquatic = register(context, TREE_JUNGLE_AQUATIC, new TreeFeature.Builder(
           BlockStateProvider.of(Blocks.JUNGLE_LOG),
           new MagnoliaTrunkPlacer(UniformInt.of(3, 6), UniformInt.of(4, 5), UniformInt.of(2, 4)),
           BlockStateProvider.of(Blocks.JUNGLE_LEAVES),
           new MagnoliaFoliagePlacer(),
           Optional.of(new MagnoliaRootPlacer(ConstantInt.ZERO, Holder.direct(BlockStateProvider.of(Blocks.JUNGLE_LOG)), Optional.empty())),
           new TwoLayersFeatureSize(1, 0, 1),
           belowTrunkProvider).ignoreVines().build());
       
       var palm = register(context, TREE_PALM, new PalmTreeFeature(BlockStateProvider.of(RUBlocks.PALM_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.PALM_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.PALM_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 8, 5));
       var palmTall = register(context, TREE_TALL_PALM, new PalmTreeFeature(BlockStateProvider.of(RUBlocks.PALM_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.PALM_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.PALM_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 12, 5));
       var palmShrub = register(context, TREE_PALM_SHRUB, new PalmTreeFeature(BlockStateProvider.of(RUBlocks.PALM_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.PALM_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.PALM_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 2, 1));
       
       registerSelector(context, TREE_GROUP_GRASSY_BEACH, builder -> builder
           .add(direct(palm), 2)
           .add(direct(palmTall), 1)
       );
       
       registerSelector(context, TREE_GROUP_RAINFOREST, builder -> builder
           .add(direct(palmTall), 4)
           .add(direct(kapok), 2)
           .add(direct(bigJungle), 4)
           .add(direct(palmShrub), 1)
           .add(direct(oakBushWithFlowers), 4)
       );
       
       registerSelector(context, TREE_GROUP_SPARSE_RAINFOREST, builder -> builder
           .add(direct(palm), 4)
           .add(direct(kapok), 2)
           .add(direct(bigJungle), 4)
           .add(direct(palmShrub), 1)
           .add(direct(oakBush), 4)
       );
       
       registerSelector(context, TREE_GROUP_TROPICS, builder -> builder
           .add(direct(palm), 2)
           .add(direct(palmShrub), 1)
           .add(direct(jungle), 1)
           .add(direct(bigJungle), 1)
           .add(direct(oakBushWithFlowers), 3)
       );
       
       registerSelector(context, TREE_GROUP_ROCKY_REEF, builder -> builder
           .add(direct(jungleAquatic), 3)
           .add(direct(palm), 2)
       );
       
       registerPlaced(context, TREE_GROUP_GRASSLAND, LithostitchedFeatures.placed(direct(oakShrubSmall)));
       registerPlaced(context, TREE_GROUP_CHALK_CLIFFS, LithostitchedFeatures.placed(direct(oakBushWithFlowers)));
       
       registerPlaced(context, TREE_GROUP_TROPICAL_RIVER, LithostitchedFeatures.placed(direct(palm)));

       var pine = register(context, TREE_PINE, new TreeFeature.Builder(
           log(RUBlocks.PINE_WOOD_SET),
           new StraightTrunkPlacer(10, 4, 0),
           leaves(RUBlocks.PINE_NATURAL_SET),
           new FancyPineFoliagePlacer(ConstantInt.of(0), UniformInt.of(0, 1), 0),
           new TwoLayersFeatureSize(3, 0, 1)
       , belowTrunkProvider).decorators(List.of(PINE_BRANCH, new BeehiveDecorator(0.001f))).build());
       
       var pineBees = register(context, TREE_PINE_BEES, new TreeFeature.Builder(
           log(RUBlocks.PINE_WOOD_SET),
           new StraightTrunkPlacer(9, 3, 0),
           leaves(RUBlocks.PINE_NATURAL_SET),
           new FancyPineFoliagePlacer(ConstantInt.of(0), UniformInt.of(1, 2), 0),
           new TwoLayersFeatureSize(3, 0, 1)
       , belowTrunkProvider).decorators(List.of(PINE_BRANCH, new BeehiveDecorator(1f))).build());

       var pineSkinny = register(context, TREE_PINE_SKINNY, new TreeFeature.Builder(
            log(RUBlocks.PINE_WOOD_SET),
            new StraightTrunkPlacer(10, 4, 0),
            leaves(RUBlocks.PINE_NATURAL_SET),
            new SkinnyPineFoliagePlacer(ConstantInt.of(0), UniformInt.of(0, 1), 5),
            new TwoLayersFeatureSize(3, 0, 1)
       , belowTrunkProvider).decorators(List.of(PINE_BRANCH, new BeehiveDecorator(0.001f))).build());

       var pineTall = register(context, TREE_PINE_TALL, new TreeFeature.Builder(
           log(RUBlocks.PINE_WOOD_SET),
           new StraightTrunkPlacer(14, 5, 0),
           leaves(RUBlocks.PINE_NATURAL_SET),
           new FancyPineFoliagePlacer(ConstantInt.of(0), UniformInt.of(0, 1), 0),
           new TwoLayersFeatureSize(3, 0, 1)
       , belowTrunkProvider).decorators(List.of(PINE_BRANCH, new BeehiveDecorator(0.001f))).build());

       var pineSkinnyTall = register(context, TREE_PINE_SKINNY_TALL, new TreeFeature.Builder(
           log(RUBlocks.PINE_WOOD_SET),
           new StraightTrunkPlacer(14, 5, 0),
           leaves(RUBlocks.PINE_NATURAL_SET),
           new SkinnyPineFoliagePlacer(ConstantInt.of(0), UniformInt.of(0, 1), 5),
           new TwoLayersFeatureSize(3, 0, 1)
       , belowTrunkProvider).decorators(List.of(PINE_BRANCH, new BeehiveDecorator(0.001f))).build());
       
       registerPlaced(context, TREE_GROUP_PINE_TAIGA_PRIMARY, new WeightedRandomSelectorFeature(WeightedList.<Holder<PlacedFeature>>builder()
           .add(direct(pine), 12)
           .add(direct(pineSkinny), 3)
           .add(direct(pineTall), 4)
           .add(direct(pineSkinnyTall), 1)
        .build()));
       
       registerPlaced(context, TREE_GROUP_HIGHLAND_FIELDS, new WeightedRandomSelectorFeature(WeightedList.<Holder<PlacedFeature>>builder()
           .add(direct(pineBees), 4)
           .add(direct(pine), 1)
        .build()));

       var pineStripped = register(context, TREE_STRIPPED_PINE, new StrippedPineTreeFeature(BlockStateProvider.of(RUBlocks.PINE_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.PINE_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.PINE_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 10, 4));
       var pineStrippedTall = register(context, TREE_STRIPPED_PINE_TALL, new StrippedPineTreeFeature(BlockStateProvider.of(RUBlocks.PINE_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.PINE_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.PINE_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 14, 5));
       var pineStrippedMountain = register(context, TREE_STRIPPED_PINE_MOUNTAIN, new StrippedPineTreeFeature(BlockStateProvider.of(RUBlocks.PINE_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.PINE_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.PINE_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 15, 7));
       var pineShrub = register(context, TREE_PINE_SHRUB, new TreeShrubFeature(BlockStateProvider.of(RUBlocks.PINE_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.PINE_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.PINE_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 1, 2));

       var lushPine = register(context, TREE_LUSH_PINE, new LushPineTreeFeature(BlockStateProvider.of(RUBlocks.PINE_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.PINE_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.PINE_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 19, 4));
       
       
       registerSelector(context, TREE_GROUP_FEN, builder -> builder
           .add(direct(pine), 4)
           .add(direct(pineStripped), 4)
           .add(direct(deadBog), 1)
       );
       
       registerRedirector(context, TREE_GROUP_MOUNTAINS, pine);
       registerRedirector(context, TREE_GROUP_PINE_SLOPES, pineStripped);
       registerRedirector(context, TREE_GROUP_PINE_TAIGA_SECONDARY, pineShrub);
       
       registerSelector(context, TREE_GROUP_TOWERING_CLIFFS, builder -> builder
           .add(direct(pineStrippedMountain), 19)
           .add(direct(deadPineStrippedMountain), 1)
       );
       
       registerSelector(context, TREE_GROUP_ICY_HEIGHTS, builder -> builder
           .add(direct(pineStrippedMountain), 40)
           .add(direct(pineShrub), 9)
           .add(direct(deadPineStrippedMountain), 1)
       );
       
       registerSelector(context, TREE_GROUP_FROZEN_PINE_TAIGA, builder -> builder
           .add(direct(pineStripped), 7)
           .add(direct(pineStrippedTall), 2)
           .add(direct(pineShrub), 1)
       );
       
       registerSelector(context, TREE_GROUP_FUNGAL_FEN, builder -> builder
           .add(direct(lushPine), 2)
           .add(direct(giantPinkBioshroom), 1)
           .add(direct(giantRedMushroom), 1)
           .add(direct(giantBrownBioshroom), 1)
       );
       
       var saguaroCactus = register(context, TREE_SAGUARO_CACTUS, new SaguaroCactusFeature(BlockStateProvider.of(RUBlocks.SAGUARO_CACTUS.get().defaultBlockState()), BlockStateProvider.of(RUBlocks.SAGUARO_CACTUS_NATURAL_SET.getSapling().defaultBlockState()), BlockStateProvider.of(RUBlocks.REDWOOD_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 7, 2));
       
       registerPlaced(context, TREE_GROUP_SAGUARO_DESERT, LithostitchedFeatures.placed(direct(saguaroCactus)));
       
       register(context, TREE_ICE_SPIRE, new IceSpireFeature(BlockStateProvider.of(Blocks.PACKED_ICE.defaultBlockState()), BlockStateProvider.of(Blocks.ICE.defaultBlockState()), BlockStateProvider.of(Blocks.BLUE_ICE), List.of(), 14, 9));
       
       var spruceTall = register(context, TREE_SPRUCE_TALL, new TreeFeature.Builder(BlockStateProvider.of(Blocks.SPRUCE_LOG.defaultBlockState()), new StraightTrunkPlacer(13, 2, 2), BlockStateProvider.of(Blocks.SPRUCE_LEAVES.defaultBlockState()), new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(2, 2), UniformInt.of(5, 5)), new TwoLayersFeatureSize(2, 0, 2), belowTrunkProvider).ignoreVines().build());
       var spruceShrub = register(context, TREE_SPRUCE_SHRUB, new TreeShrubFeature(BlockStateProvider.of(Blocks.SPRUCE_LOG.defaultBlockState()), BlockStateProvider.of(Blocks.SPRUCE_LEAVES.defaultBlockState()), BlockStateProvider.of(RUBlocks.OAK_NATURAL_SET.getBranch().defaultBlockState()), List.of(), 1, 0));
       
       var spruceTundra = register(context, TREE_SPRUCE_TUNDRA, new TreeFeature.Builder(
           BlockStateProvider.of(RUBlocks.PINE_WOOD_SET.getLog()),
           new StraightTrunkPlacer(6, 3, 1),
           BlockStateProvider.of(Blocks.SPRUCE_LEAVES),
           new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(1, 2), UniformInt.of(1, 2)),
           new TwoLayersFeatureSize(2, 0, 2)
       , belowTrunkProvider).decorators(List.of(PINE_BRANCH)).ignoreVines().build());
       
       var spruceFancyTundra = register(context, TREE_PINE_TUNDRA, new TreeFeature.Builder(
           BlockStateProvider.of(RUBlocks.PINE_WOOD_SET.getLog()),
           new StraightTrunkPlacer(8, 4, 0),
           BlockStateProvider.of(Blocks.SPRUCE_LEAVES),
           new FancyPineFoliagePlacer(UniformInt.of(1, 2)),
           new TwoLayersFeatureSize(2, 0, 2)
       , belowTrunkProvider).decorators(List.of(PINE_BRANCH)).ignoreVines().build());
       
       registerSelector(context, TREE_GROUP_SHRUBLAND, builder -> builder
           .add(direct(oakShrubSmall), 65)
           .add(direct(spruceShrub), 65)
           .add(direct(oakBush), 19)
           .add(direct(spruceTall), 1)
       );
       
       registerSelector(context, TREE_GROUP_COLD_DECIDUOUS_FOREST, builder -> builder
           .add(direct(oakWithBranch), 1)
           .add(direct(bigOak), 2)
           .add(direct(bigRedMaple), 2)
           .add(direct(spruceTall), 4)
       );
       
       
       registerSelector(context, TREE_GROUP_MAPLE_FOREST, builder -> builder
           .add(direct(maple), 4)
           .add(direct(redMaple), 1)
           .add(direct(bigMaple), 1)
           .add(direct(spruceTall), 4)
       );
       
       registerSelector(context, TREE_GROUP_TUNDRA, builder -> builder
           .add(direct(spruceTundra), 4)
           .add(direct(spruceFancyTundra), 1)
       );
       
       registerPlaced(context, TREE_GROUP_SPIRES, LithostitchedFeatures.placed(direct(spruceTall)));
       
       var socotraLarge = register(context, TREE_LARGE_SOCOTRA, new LargeSocotraTreeFeature(BlockStateProvider.of(RUBlocks.SOCOTRA_WOOD_SET.getLog().defaultBlockState()), BlockStateProvider.of(RUBlocks.SOCOTRA_NATURAL_SET.getLeaves().defaultBlockState()), BlockStateProvider.of(RUBlocks.SOCOTRA_NATURAL_SET.getBranch()), List.of(), 8, 5));
       var socotraSmall = register(context, TREE_SMALL_SOCOTRA, new SmallSocotraTreeFeature());
       
       registerSelector(context, TREE_GROUP_DRY_BUSHLAND, builder -> builder
           .add(direct(socotraLarge), 1)
           .add(direct(socotraSmall), 1)
           .add(direct(acacia), 1)
           .add(direct(acaciaShrub), 3)
           .add(direct(oakShrubSmall), 3)
       );
       
       var redwoodSmall = register(context, TREE_REDWOOD_SMALL, new TreeFeature.Builder(
           BlockStateProvider.of(RUBlocks.REDWOOD_WOOD_SET.getLog().defaultBlockState()),
           new RedwoodTrunkPlacer(
               UniformInt.of(20, 40),
               List.of(ConstantInt.of(2), weightedInts(pair(2, 4), pair(3, 1)), weightedInts(pair(0, 4), pair(1, 1))),
               3,
               List.of()
           ),
           BlockStateProvider.of(RUBlocks.REDWOOD_NATURAL_SET.getLeaves().defaultBlockState()),
           new RedwoodFoliagePlacer(ConstantInt.of(2), ConstantInt.ZERO, 0),
           new TwoLayersFeatureSize(6, 1, 0), belowTrunkProvider)
           .decorators(List.of(RandomBranchDecorator.create(0.06f, RUBlocks.REDWOOD_NATURAL_SET, RUBlocks.REDWOOD_WOOD_SET, 3)))
           .build()
       );
       var redwoodMedium = register(context, TREE_REDWOOD_MEDIUM, new TreeFeature.Builder(
           BlockStateProvider.of(RUBlocks.REDWOOD_WOOD_SET.getLog().defaultBlockState()),
           new RedwoodTrunkPlacer(
               UniformInt.of(30, 45),
               List.of(ConstantInt.of(2), weightedInts(pair(2, 4), pair(3, 1)), ConstantInt.of(2)),
               3,
               List.of(UniformInt.of(15, 20), UniformInt.of(3, 7), BiasedToBottomInt.of(0, 3))
           ),
           BlockStateProvider.of(RUBlocks.REDWOOD_NATURAL_SET.getLeaves().defaultBlockState()),
           new RedwoodFoliagePlacer(ConstantInt.of(2), ConstantInt.ZERO, 0),
           new TwoLayersFeatureSize(6, 1, 0), belowTrunkProvider)
           .decorators(List.of(RandomBranchDecorator.create(0.06f, RUBlocks.REDWOOD_NATURAL_SET, RUBlocks.REDWOOD_WOOD_SET, 3)))
           .build()
       );
       var redwoodLarge = register(context, TREE_REDWOOD_LARGE, new TreeFeature.Builder(
           BlockStateProvider.of(RUBlocks.REDWOOD_WOOD_SET.getLog().defaultBlockState()),
           new RedwoodTrunkPlacer(
               UniformInt.of(40, 55),
               List.of(ConstantInt.of(1), weightedInts(pair(2, 4), pair(3, 1)), weightedInts(pair(2, 4), pair(3, 1)), ConstantInt.of(1)),
               3,
               List.of(UniformInt.of(35, 42), UniformInt.of(19, 21), UniformInt.of(10, 12), UniformInt.of(3, 6))
           ),
           BlockStateProvider.of(RUBlocks.REDWOOD_NATURAL_SET.getLeaves().defaultBlockState()),
           new RedwoodFoliagePlacer(ConstantInt.of(2), ConstantInt.ZERO, 0),
           new TwoLayersFeatureSize(8, 1, 0), belowTrunkProvider)
           .decorators(List.of(RandomBranchDecorator.create(0.12f, RUBlocks.REDWOOD_NATURAL_SET, RUBlocks.REDWOOD_WOOD_SET, 3)))
           .build()
       );
       var redwoodEmergent = register(context, TREE_REDWOOD_EMERGENT, new TreeFeature.Builder(
           BlockStateProvider.of(RUBlocks.REDWOOD_WOOD_SET.getLog().defaultBlockState()),
           new RedwoodTrunkPlacer(
               BiasedToBottomInt.of(60, 90),
               List.of(ConstantInt.of(2), weightedInts(pair(3, 4), pair(4, 1)), weightedInts(pair(3, 4), pair(4, 1)), ConstantInt.of(1)),
               3,
               List.of(UniformInt.of(40, 55), UniformInt.of(25, 35), UniformInt.of(10, 20), UniformInt.of(4, 8))
           ),
           BlockStateProvider.of(RUBlocks.REDWOOD_NATURAL_SET.getLeaves().defaultBlockState()),
           new RedwoodFoliagePlacer(ConstantInt.of(2), ConstantInt.ZERO, 0),
           new TwoLayersFeatureSize(8, 1, 0), belowTrunkProvider)
           .decorators(List.of(RandomBranchDecorator.create(0.12f, RUBlocks.REDWOOD_NATURAL_SET, RUBlocks.REDWOOD_WOOD_SET, 3)))
           .build()
       );

       registerPlaced(context, TREE_GROUP_REDWOODS_PRIMARY, new WeightedRandomSelectorFeature(WeightedList.<Holder<PlacedFeature>>builder()
           .add(direct(redwoodMedium), 14)
           .add(direct(redwoodLarge), 4)
           .add(direct(redwoodEmergent), 1)
        .build()));
       registerRedirector(context, TREE_GROUP_REDWOODS_SECONDARY, redwoodSmall);
       registerRedirector(context, TREE_GROUP_REDWOODS_TERTIARY, oakShrubLarge);
       
       registerRedirector(context, TREE_GROUP_SPARSE_REDWOODS_PRIMARY, redwoodMedium);
       registerRedirector(context, TREE_GROUP_SPARSE_REDWOODS_SECONDARY, oakBush);
       

       var willow = register(context, TREE_WILLOW, new TreeFeature.Builder(
           log(RUBlocks.WILLOW_WOOD_SET),
           new StraightTrunkPlacer(8, 2, 0),
           leaves(RUBlocks.WILLOW_NATURAL_SET),
           new BlobFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), 3),
           WillowRootPlacer.create(RUBlocks.WILLOW_WOOD_SET, 0.5f),
           new TwoLayersFeatureSize(1, 0, 1),
           belowTrunkProvider).build());
       
       var willowBig = register(context, TREE_BIG_WILLOW, new TreeFeature.Builder(
           log(RUBlocks.WILLOW_WOOD_SET),
           new FancyTrunkPlacer(9, 9, 0),
           leaves(RUBlocks.WILLOW_NATURAL_SET),
           new WillowFoliagePlacer(0.25F),
           WillowRootPlacer.create(RUBlocks.WILLOW_WOOD_SET, 0.5f),
           new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)),
           belowTrunkProvider).ignoreVines().build());
       
       var willowSwamp = register(context, TREE_WILLOW_SWAMP, new TreeFeature.Builder(
           log(RUBlocks.WILLOW_WOOD_SET),
           new StraightTrunkPlacer(7, 2, 1),
           leaves(RUBlocks.WILLOW_NATURAL_SET),
           new BlobFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), 3),
           WillowRootPlacer.create(RUBlocks.WILLOW_WOOD_SET, 1),
           new TwoLayersFeatureSize(1, 0, 1),
           belowTrunkProvider).decorators(ImmutableList.of(new LeaveVineDecorator(0.25f))).build());
        
        var oakSwamp = register(context, TREE_OAK_SWAMP, new TreeFeature.Builder(
            BlockStateProvider.of(Blocks.OAK_LOG),
            new StraightTrunkPlacer(7, 2, 1),
            BlockStateProvider.of(Blocks.OAK_LEAVES),
            new BlobFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), 3),
            WillowRootPlacer.create(Blocks.OAK_LOG, 0.5f),
            new TwoLayersFeatureSize(1, 0, 1),
           belowTrunkProvider).decorators(ImmutableList.of(new LeaveVineDecorator(0.25f))).build());
       
       
       registerSelector(context, TREE_GROUP_BAYOU, builder -> builder
           .add(direct(cypress), 2)
           .add(direct(willowSwamp), 2)
           .add(direct(oakBush), 1)
       );
       
       BlockStateProvider wisteriaLog = BlockStateProvider.of(RUBlocks.WISTERIA_WOOD_SET.getLog());
       var wisteriaSky = register(context, TREE_WISTERIA_SKY, new TreeFeature.Builder(
           wisteriaLog,
           new MagnoliaTrunkPlacer(UniformInt.of(2, 5), UniformInt.of(4, 5), UniformInt.of(2, 3)),
           BlockStateProvider.of(RUBlocks.SKY_WISTERIA_NATURAL_SET.getLeaves().defaultBlockState()),
           new MagnoliaFoliagePlacer(),
           Optional.of(new MagnoliaRootPlacer(ConstantInt.ZERO, Holder.direct(wisteriaLog), Optional.empty())),
           new TwoLayersFeatureSize(1, 0, 1),
           belowTrunkProvider).ignoreVines().decorators(List.of(
           HangingVinesDecorator.create(RUBlocks.SKY_WISTERIA_NATURAL_SET, 0.3f),
           new BeehiveDecorator(0.002f)
       )).build());
       var wisteriaLavender = register(context, TREE_WISTERIA_LAVENDER, new TreeFeature.Builder(
           wisteriaLog,
           new MagnoliaTrunkPlacer(UniformInt.of(2, 5), UniformInt.of(4, 5), UniformInt.of(3, 4)),
           BlockStateProvider.of(RUBlocks.LAVENDER_WISTERIA_NATURAL_SET.getLeaves().defaultBlockState()),
           new MagnoliaFoliagePlacer(),
           Optional.of(new MagnoliaRootPlacer(ConstantInt.ZERO, Holder.direct(wisteriaLog), Optional.empty())),
           new TwoLayersFeatureSize(1, 0, 1),
           belowTrunkProvider).ignoreVines().decorators(List.of(
           HangingVinesDecorator.create(RUBlocks.LAVENDER_WISTERIA_NATURAL_SET, 0.3f),
           new BeehiveDecorator(0.002f)
       )).build());
       var wisteriaSalmon = register(context, TREE_WISTERIA_SALMON, new TreeFeature.Builder(
           wisteriaLog,
           new MagnoliaTrunkPlacer(UniformInt.of(2, 5), UniformInt.of(4, 5), UniformInt.of(2, 3)),
           BlockStateProvider.of(RUBlocks.SALMON_WISTERIA_NATURAL_SET.getLeaves().defaultBlockState()),
           new MagnoliaFoliagePlacer(),
           Optional.of(new MagnoliaRootPlacer(ConstantInt.ZERO, Holder.direct(wisteriaLog), Optional.empty())),
           new TwoLayersFeatureSize(1, 0, 1),
           belowTrunkProvider).ignoreVines().decorators(List.of(
           HangingVinesDecorator.create(RUBlocks.SALMON_WISTERIA_NATURAL_SET, 0.3f),
           new BeehiveDecorator(0.002f)
       )).build());
       
       var wisteriaLargeSky = register(context, TREE_WISTERIA_LARGE_SKY, new TreeFeature.Builder(
           wisteriaLog,
           new FancyTrunkPlacer(9, 9, 0),
           BlockStateProvider.of(RUBlocks.SKY_WISTERIA_NATURAL_SET.getLeaves().defaultBlockState()),
           new WillowFoliagePlacer(0.5F),
           new TwoLayersFeatureSize(1, 0, 1)
       , belowTrunkProvider).ignoreVines().decorators(List.of(
           HangingVinesDecorator.create(RUBlocks.SKY_WISTERIA_NATURAL_SET, 0.4f),
           new BeehiveDecorator(0.002f),
           RandomBranchDecorator.create(0.1f, RUBlocks.WISTERIA_NATURAL_SET, RUBlocks.WISTERIA_WOOD_SET, 3, Holder.direct(BlockStateProvider.of(RUBlocks.SKY_WISTERIA_NATURAL_SET.getLeaves())))
       )).build());
       var wisteriaLargeLavender = register(context, TREE_WISTERIA_LARGE_LAVENDER, new TreeFeature.Builder(
           wisteriaLog,
           new FancyTrunkPlacer(9, 9, 0),
           BlockStateProvider.of(RUBlocks.LAVENDER_WISTERIA_NATURAL_SET.getLeaves().defaultBlockState()),
           new WillowFoliagePlacer(0.5F),
           new TwoLayersFeatureSize(1, 0, 1)
       , belowTrunkProvider).ignoreVines().decorators(List.of(
           HangingVinesDecorator.create(RUBlocks.LAVENDER_WISTERIA_NATURAL_SET, 0.4f),
           new BeehiveDecorator(0.002f),
           RandomBranchDecorator.create(0.1f, RUBlocks.WISTERIA_NATURAL_SET, RUBlocks.WISTERIA_WOOD_SET, 3, Holder.direct(BlockStateProvider.of(RUBlocks.LAVENDER_WISTERIA_NATURAL_SET.getLeaves())))
       )).build());
       var wisteriaLargeSalmon = register(context, TREE_WISTERIA_LARGE_SALMON, new TreeFeature.Builder(
           wisteriaLog,
           new FancyTrunkPlacer(9, 9, 0),
           BlockStateProvider.of(RUBlocks.SALMON_WISTERIA_NATURAL_SET.getLeaves().defaultBlockState()),
           new WillowFoliagePlacer(0.5F),
           new TwoLayersFeatureSize(1, 0, 1)
       , belowTrunkProvider).ignoreVines().decorators(List.of(
           HangingVinesDecorator.create(RUBlocks.SALMON_WISTERIA_NATURAL_SET, 0.4f),
           new BeehiveDecorator(0.002f),
           RandomBranchDecorator.create(0.1f, RUBlocks.WISTERIA_NATURAL_SET, RUBlocks.WISTERIA_WOOD_SET, 3, Holder.direct(BlockStateProvider.of(RUBlocks.SALMON_WISTERIA_NATURAL_SET.getLeaves())))
       )).build());
       
       registerSelector(context, TREE_GROUP_WILLOW_FOREST, builder -> builder
           .add(direct(willow), 5)
           .add(direct(willowBig), 3)
           .add(direct(smallOak), 1)
           .add(direct(blueMagnolia), 1)
       );
       
       registerSelector(context, TREE_GROUP_WISTERIA_GROVE, builder -> builder
           .add(direct(wisteriaSky), 1)
           .add(direct(wisteriaLavender), 1)
           .add(direct(wisteriaSalmon), 1)
           .add(direct(wisteriaLargeSky), 4)
           .add(direct(wisteriaLargeLavender), 4)
           .add(direct(wisteriaLargeSalmon), 4)
       );
    }

    private static BlockStateProvider log(WoodSet wood) {
       return BlockStateProvider.of(wood.getLog());
    }
    
    private static BlockStateProvider leaves(NaturalSet leaves) {
      return BlockStateProvider.of(leaves.getLeaves());
    }
    
    private static Feature bushSmall(Block log, Block leaves) {
       return new TreeFeature.Builder(
           BlockStateProvider.of(log),
           new StraightTrunkPlacer(1, 0, 0),
           BlockStateProvider.of(leaves),
           new PineFoliagePlacer(ConstantInt.of(1), ConstantInt.of(0), ConstantInt.of(2)),
           new TwoLayersFeatureSize(0, 0, 0),
           belowTrunkProvider
       ).build();
    }

    private static Feature mapleSmall(NaturalSet natural, Supplier<Block> leafLitter) {
        return mapleSmall(RUBlocks.MAPLE_WOOD_SET.getLog(), natural.getLeaves(), RUBlocks.MAPLE_NATURAL_SET.getBranch(), leafLitter);
    }

    private static Feature mapleSmall(Block log, Block leaves, Block branch, Supplier<Block> leafLitter) {
        List<TreeDecorator> decorators = new ArrayList<>();
        decorators.add(GroupBranchDecorator.createWithoutLeaves(1, branch, log, 3));
        if (leafLitter != null) {
            decorators.add(PlaceOnGroundDecorator.leafLitter(leafLitter.get(), 48));
        }

        return new TreeFeature.Builder(
            BlockStateProvider.of(log),
            new StraightTrunkPlacer(7, 2, 2),
            BlockStateProvider.of(leaves),
            new MapleFoliagePlacer(),
            new TwoLayersFeatureSize(1, 0, 1),
            belowTrunkProvider
        ).decorators(decorators).build();
    }
}
