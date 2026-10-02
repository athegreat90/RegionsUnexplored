package net.regions_unexplored.datagen.provider.registry.configured_feature;

import net.regions_unexplored.world.level.feature.*;
import net.regions_unexplored.world.level.feature.bioshroom.*;
import net.regions_unexplored.world.level.feature.tree.*;
import net.regions_unexplored.world.level.feature.tree.nether.*;
import net.regions_unexplored.worldgen.feature.CarvedLimitedPoolFeature;
import net.regions_unexplored.worldgen.feature.RUFallenTreeFeature;
import net.regions_unexplored.worldgen.feature.RURockFeature;

import dev.worldgen.lithostitched.api.worldgen.blockpredicate.LithostitchedBlockPredicates;
import dev.worldgen.lithostitched.api.worldgen.feature.LithostitchedFeatures;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.InclusiveRange;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomBlockProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.material.Fluids;
import net.regions_unexplored.datagen.provider.registry.placed_feature.RuMiscOverworldPlacements;
import net.regions_unexplored.registry.RUBlocks;
import net.regions_unexplored.registry.RUFeatureTypes;
import net.regions_unexplored.registry.data.RUConfiguredFeatures;
import net.regions_unexplored.block.type.wood.AspenLogBlock;
import net.regions_unexplored.world.level.feature.configuration.PointedRedstoneClusterConfiguration;
import net.regions_unexplored.world.level.feature.configuration.PointedRedstoneConfiguration;
import net.regions_unexplored.worldgen.feature.config.CarvedLimitedPoolFeatureConfig;
import net.regions_unexplored.worldgen.feature.config.FallenTreeConfig;
import net.regions_unexplored.worldgen.feature.config.RockFeatureConfig;
import net.regions_unexplored.worldgen.treedecorator.AttachedToLogsDecorator;

import java.util.List;

import static net.regions_unexplored.datagen.provider.registry.util.RUFeatureUtils.direct;

import static net.regions_unexplored.registry.data.RUConfiguredFeatures.*;
import static net.regions_unexplored.datagen.provider.registry.placed_feature.RuMiscOverworldPlacements.*;
import static net.regions_unexplored.datagen.provider.registry.util.RUFeatureUtils.*;

public class RuMiscOverworldFeatures {
    //FALLEN_TREES
    public static final ResourceKey<Feature> FALLEN_OAK = key("tree/fallen/oak");
    public static final ResourceKey<Feature> FALLEN_PINE = key("tree/fallen/pine");
    public static final ResourceKey<Feature> FALLEN_SNOW_PINE = key("tree/fallen/snow_pine");
    //OTHER_FEATURES
    public static final ResourceKey<Feature> SPECIAL_MOSS_PATCH_WITH_WATER = key("special/moss_patch_with_water");

    public static final ResourceKey<Feature> ROCK_COBBLESTONE = key("rock/cobblestone");
    public static final ResourceKey<Feature> ROCK_MIXED_COBBLESTONE = key("rock/mixed_cobblestone");
    public static final ResourceKey<Feature> ROCK_MIXED_COBBLESTONE_LARGE = key("rock/mixed_cobblestone_large");
    public static final ResourceKey<Feature> ROCK_MIXED_STONE = key("rock/mixed_stone");
    public static final ResourceKey<Feature> ROCK_STONE_LARGE = key("rock/stone_large");
    public static final ResourceKey<Feature> ROCK_MOSSY_STONE_LARGE = key("rock/mossy_stone_large");

    public static void bootstrap(BootstrapContext<Feature> context) {
        HolderGetter<Feature> holderGetter = context.lookup(Registries.FEATURE);
        RuleTest stoneOreTest = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateOreTest = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        HolderSet<Block> replaceables = context.lookup(Registries.BLOCK).getOrThrow(BlockTags.DRIPSTONE_REPLACEABLE);
        List<BlockReplacement> ORE_REDSTONE_TARGET_LIST = List.of(new BlockReplacement(stoneOreTest, Blocks.REDSTONE_ORE.defaultBlockState()), new BlockReplacement(deepslateOreTest, Blocks.DEEPSLATE_REDSTONE_ORE.defaultBlockState()));

        //---------------------FEATURES---------------------//
        //FALLEN_TREES
        registerPlaced(context, FALLEN_LARCH, new RUFallenTreeFeature(FallenTreeConfig.of(RUBlocks.LARCH_WOOD_SET.getLog().defaultBlockState(), 7, 12)));
        registerPlaced(context, FALLEN_MAPLE, new RUFallenTreeFeature(FallenTreeConfig.of(RUBlocks.MAPLE_WOOD_SET.getLog().defaultBlockState(), 6, 8)));
        register(context, FALLEN_OAK, new RUFallenTreeFeature(FallenTreeConfig.of(Blocks.OAK_LOG.defaultBlockState(), 6, 8)));
        register(context, FALLEN_PINE, new RUFallenTreeFeature(FallenTreeConfig.of(RUBlocks.PINE_WOOD_SET.getLog().defaultBlockState(), 7, 12)));
        register(context, FALLEN_SNOW_PINE, new RUFallenTreeFeature(FallenTreeConfig.of(RUBlocks.PINE_WOOD_SET.getStrippedLog().defaultBlockState(), 7, 12, List.of(new AttachedToLogsDecorator(0.4f, BlockStateProvider.of(Blocks.SNOW), List.of(Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST), true)))));
        registerPlaced(context, FALLEN_SILVER_BIRCH, new RUFallenTreeFeature(FallenTreeConfig.of(RUBlocks.SILVER_BIRCH_WOOD_SET.getLog().defaultBlockState().setValue(AspenLogBlock.IS_BASE, true), 6, 10)));
        //CAVE_FEATURES
        registerPlaced(context, SPECIAL_POINTED_REDSTONE, new SimpleRandomSelectorFeature(HolderSet.direct(PlacementUtils.inlinePlaced(new PointedRedstoneFeature(0.5F, 0.7F, 0.5F, 0.5F), EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12), OffsetPlacement.vertical(ConstantInt.of(1))), PlacementUtils.inlinePlaced(new PointedRedstoneFeature(0.5F, 0.7F, 0.5F, 0.5F), EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12), OffsetPlacement.vertical(ConstantInt.of(-1))))));
        registerPlaced(context, SPECIAL_LARGE_POINTED_REDSTONE, LithostitchedFeatures.largeDripstone(Holder.direct(BlockStateProvider.of(RUBlocks.RAW_REDSTONE_BLOCK.get())), context.lookup(Registries.BLOCK).getOrThrow(BlockTags.BASE_STONE_OVERWORLD), 30, UniformInt.of(1, 6), UniformFloat.of(0.4F, 2.0F), 0.33F, UniformFloat.of(0.3F, 0.9F), UniformFloat.of(0.4F, 1.0F), UniformFloat.of(0.0F, 0.3F), 4, 0.6F));
        registerPlaced(context, SPECIAL_POINTED_REDSTONE_CLUSTER, new PointedRedstoneClusterFeature(new PointedRedstoneClusterConfiguration(RUBlocks.RAW_REDSTONE_BLOCK.get().defaultBlockState(), RUBlocks.REDSTONE_SPIKE.get().defaultBlockState(), replaceables, 12, UniformInt.of(3, 6), UniformInt.of(2, 8), 1, 3, UniformInt.of(2, 4), UniformFloat.of(0.3F, 0.7F), ClampedNormalFloat.of(0.1F, 0.3F, 0.1F, 0.9F), 0.1F, 3, 8)));
        registerPlaced(context, SPECIAL_ORE_REDSTONE_LARGE, new OreFeature(ORE_REDSTONE_TARGET_LIST, 20));

        registerPlaced(context, PATCH_PRISMARITE_CLUSTER, randomPatch(new WeightedStateProvider(WeightedList.<BlockState>builder().add(RUBlocks.LARGE_PRISMARITE_CLUSTER.get().defaultBlockState(), 1).add(RUBlocks.PRISMARITE_CLUSTER.get().defaultBlockState(), 5)), 32));
        registerPlaced(context, SPECIAL_HANGING_PRISMARITE_CLUSTER, new HangingPrismariteFeature());

        registerPlaced(context, SPECIAL_CALCITE_POOL, new WaterloggedVegetationPatchFeature(context.lookup(Registries.BLOCK).getOrThrow(BlockTags.LUSH_GROUND_REPLACEABLE), Holder.direct(BlockStateProvider.of(Blocks.CALCITE)), PlacementUtils.inlinePlaced(holderGetter.getOrThrow(CaveFeatures.POINTED_DRIPSTONE)), CaveSurface.FLOOR, ConstantInt.of(3), 0.8F, 5, 0.1F, UniformInt.of(4, 7), 0.7F));

        registerPlaced(context, SPECIAL_LAVA_FALL, new SpringFeature(
            Fluids.LAVA.defaultFluidState(),
            false,
            4,
            1,
            context.lookup(Registries.BLOCK).getOrThrow(BlockTags.BASE_STONE_OVERWORLD)
        ));
        registerPlaced(context, SPECIAL_INFERNO_LAVA_DELTA, new DiskFeature(
            Holder.direct(new RuleBasedStateProvider(BlockStateProvider.holderOf(Blocks.MAGMA_BLOCK), List.of(
                new RuleBasedStateProvider.Rule(
                    BlockPredicate.allOf(BlockPredicate.not(BlockPredicate.solid(Direction.UP)), LithostitchedBlockPredicates.randomChance(0.25f)),
                    BlockStateProvider.holderOf(Blocks.LAVA)
                )
            ))),
            LithostitchedBlockPredicates.grid(
                1,
                1,
                BlockPredicate.not(BlockPredicate.anyOf(BlockPredicate.solid(), BlockPredicate.matchesBlocks(Blocks.LAVA))),
                0
            ),
            UniformInt.of(3, 4),
            0
        ));





        registerPlaced(context, PATCH_ASH_VENTS_INFERNO, randomPatch(144, 5, 1, Holder.direct(new PlacedFeature(
            Holder.direct(new RandomSelectorFeature(
                List.of(new WeightedPlacedFeature(
                    inlinePlaced(new SimpleBlockFeature(
                        weightedStates(pair(Blocks.BASALT, 8), pair(Blocks.SMOOTH_BASALT, 8), pair(Blocks.MAGMA_BLOCK))
                    )),
                    0.7f
                )),
                inlinePlaced(new BlockColumnFeature(
                    List.of(
                        new BlockColumnFeature.Layer(UniformInt.of(0, 4), Holder.direct(BlockStateProvider.of(Blocks.BASALT))),
                        new BlockColumnFeature.Layer(new WeightedListInt(WeightedList.<IntProvider>builder().add(ConstantInt.of(0), 9).add(ConstantInt.of(1), 1).build()), Holder.direct(BlockStateProvider.of(RUBlocks.ASH_VENT.get())))
                    ),
                    Direction.UP,
                    BlockPredicate.allOf(
                        BlockPredicate.matchesTag(BlockTags.AIR),
                        BlockPredicate.not(BlockPredicate.anyOf(
                            BlockPredicate.matchesBlocks(Vec3i.ZERO.north(), List.of(RUBlocks.ASH_VENT.get())),
                            BlockPredicate.matchesBlocks(Vec3i.ZERO.east(), List.of(RUBlocks.ASH_VENT.get())),
                            BlockPredicate.matchesBlocks(Vec3i.ZERO.south(), List.of(RUBlocks.ASH_VENT.get())),
                            BlockPredicate.matchesBlocks(Vec3i.ZERO.west(), List.of(RUBlocks.ASH_VENT.get()))
                        ))
                    ),
                    true
                ))
            )),
            List.of(
                BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                    BlockPredicate.ONLY_IN_AIR_PREDICATE,
                    BlockPredicate.hasSturdyFace(Direction.DOWN, Direction.UP))
                ),
                OffsetPlacement.vertical(ConstantInt.of(-1))
            )
        ))));




        registerPlaced(context, SPECIAL_BASALT_BLOB, new BasaltBlobFeature(ConstantInt.of(1), UniformInt.of(1, 4)));
        //OTHER_FEATURES
        register(context, SPECIAL_MOSS_PATCH_WITH_WATER, new WaterloggedVegetationPatchFeature(context.lookup(Registries.BLOCK).getOrThrow(BlockTags.LUSH_GROUND_REPLACEABLE), Holder.direct(BlockStateProvider.of(Blocks.MOSS_BLOCK)), PlacementUtils.inlinePlaced(holderGetter.getOrThrow(RuVegetationFeatures.PATCH_SHORT_GRASS)), CaveSurface.FLOOR, ConstantInt.of(3), 0.8F, 5, 0.1F, UniformInt.of(4, 7), 0.7F));
        registerPlaced(context, SPECIAL_MARSH, new MarshFeature());
        registerPlaced(context, SPECIAL_WATER_EDGE, new WaterEdgeFeature());
        registerPlaced(context, SPECIAL_CARVED_LIMITED_POOL, new CarvedLimitedPoolFeature(new CarvedLimitedPoolFeatureConfig(3, ConstantInt.of(4), BlockPredicate.matchesBlocks(Blocks.MUD), BlockPredicate.matchesBlocks(Blocks.MUD), BlockStateProvider.of(Blocks.DIRT), BlockStateProvider.of(Blocks.GRASS_BLOCK))));
        registerPlaced(context, SPECIAL_ICICLE_UP, new FloorIcicleFeature());
        registerPlaced(context, PATCH_SILT_PODZOL_PUMPKINS, randomPatch(
            new WeightedStateProvider(WeightedList.<BlockState>builder()
                .add(Blocks.PUMPKIN.defaultBlockState(), 96)
                .add(Blocks.CARVED_PUMPKIN.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, Direction.NORTH), 1)
                .add(Blocks.CARVED_PUMPKIN.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, Direction.SOUTH), 1)
                .add(Blocks.CARVED_PUMPKIN.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, Direction.EAST), 1)
                .add(Blocks.CARVED_PUMPKIN.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, Direction.WEST), 1)
            ),
            16,
            BlockPredicate.matchesBlocks(Vec3i.ZERO.below(), List.of(RUBlocks.SILT_PODZOL.get())))
        );
        
        var rockCobblestone = register(context, ROCK_COBBLESTONE, new RURockFeature(RockFeatureConfig.create(Blocks.COBBLESTONE)));
        var rockMixedCobblestone = register(context, ROCK_MIXED_COBBLESTONE,
            new RURockFeature(RockFeatureConfig.create(new RandomBlockProvider(HolderSet.direct(Blocks.COBBLESTONE.builtInRegistryHolder(), Blocks.MOSSY_COBBLESTONE.builtInRegistryHolder()))))
        );
        var rockMixedCobblestoneLarge = register(context, ROCK_MIXED_COBBLESTONE_LARGE,
            new RURockFeature(RockFeatureConfig.createLarge(new RandomBlockProvider(HolderSet.direct(Blocks.COBBLESTONE.builtInRegistryHolder(), Blocks.MOSSY_COBBLESTONE.builtInRegistryHolder()))))
        );
        var rockMixedStone = register(context, ROCK_MIXED_STONE, new RURockFeature(RockFeatureConfig.create(new RandomBlockProvider(HolderSet.direct(Blocks.STONE.builtInRegistryHolder(), RUBlocks.MOSSY_STONE.get().builtInRegistryHolder())))));
        var rockStoneLarge = register(context, ROCK_STONE_LARGE, new RURockFeature(RockFeatureConfig.createLarge(Blocks.STONE)));
        var rockMossyStoneLarge = register(context, ROCK_MOSSY_STONE_LARGE, new RURockFeature(RockFeatureConfig.createLarge(RUBlocks.MOSSY_STONE.get())));
        
        registerSelector(context, ROCK_GROUP_ICY_HEIGHTS, builder -> builder
            .add(direct(rockMixedStone), 2)
            .add(direct(rockMixedCobblestone), 1)
        );
        
        registerSelector(context, ROCK_GROUP_HIGHLAND_FIELDS, builder -> builder
            .add(direct(rockCobblestone), 1)
            .add(direct(rockStoneLarge), 2)
            .add(direct(rockMossyStoneLarge), 3)
        );
        
        registerSelector(context, ROCK_GROUP_WINDSWEPT_MAPLE_FOREST, builder -> builder
            .add(direct(rockMixedCobblestone), 1)
            .add(direct(rockMixedStone), 2)
        );
        
        registerSelector(context, ROCK_GROUP_ROCKY_MEADOW, builder -> builder
            .add(direct(rockMixedStone), 1)
            .add(direct(rockStoneLarge), 2)
            .add(direct(rockMossyStoneLarge), 2)
        );
        
        registerSelector(context, RuMiscOverworldPlacements.ROCK_GROUP_TUNDRA, builder -> builder
            .add(direct(rockCobblestone), 1)
            .add(direct(rockMixedCobblestoneLarge), 2)
        );

        register(context, RUConfiguredFeatures.BONEMEAL_ALPHA_GRASS, new SimpleBlockFeature(BlockStateProvider.of(RUBlocks.ALPHA_ROSE.get())));
    }
}
