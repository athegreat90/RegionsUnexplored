package net.regions_unexplored.datagen.provider.registry.configured_feature;

import net.regions_unexplored.world.level.feature.*;
import net.regions_unexplored.world.level.feature.bioshroom.*;
import net.regions_unexplored.world.level.feature.tree.*;
import net.regions_unexplored.world.level.feature.tree.nether.*;
import net.regions_unexplored.worldgen.feature.CarvedLimitedPoolFeature;
import net.regions_unexplored.worldgen.feature.RUFallenTreeFeature;
import net.regions_unexplored.worldgen.feature.RURockFeature;

import dev.worldgen.lithostitched.api.worldgen.feature.LithostitchedFeatures;
import dev.worldgen.lithostitched.api.worldgen.placementcondition.LithostitchedPlacementConditions;
import dev.worldgen.lithostitched.api.worldgen.placementmodifier.LithostitchedPlacementModifiers;
import dev.worldgen.lithostitched.api.worldgen.stateprovider.LithostitchedStateProviders;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.InclusiveRange;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.BlockColumnFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.RandomSelectorFeature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.WeightedRandomSelectorFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.*;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.util.random.WeightedList;
import net.regions_unexplored.datagen.provider.registry.util.RUFeatureUtils;
import net.regions_unexplored.registry.RUBlocks;
import net.regions_unexplored.registry.RUFeatureTypes;
import net.regions_unexplored.registry.data.RUBiomes;
import net.regions_unexplored.registry.data.RUPlacedFeatures;
import net.regions_unexplored.registry.tag.RUBlockTags;
import net.regions_unexplored.worldgen.stateprovider.RandomizedGroundCoverStateProvider;
import net.regions_unexplored.block.type.food.DuskmelonBlock;
import net.regions_unexplored.block.type.food.SalmonBerryBushBlock;
import net.regions_unexplored.block.type.grass.AshenGrassBlock;

import java.util.List;

import static net.regions_unexplored.datagen.provider.registry.util.RUFeatureUtils.*;
import static net.regions_unexplored.datagen.provider.registry.placed_feature.RuVegetationPlacements.*;
import static net.regions_unexplored.registry.data.RUConfiguredFeatures.*;

public class RuVegetationFeatures {
    //GRASS
    public static final ResourceKey<Feature> PATCH_FERN = patch("fern");
    public static final ResourceKey<Feature> PATCH_SHORT_GRASS = patch("short_grass");
    public static final ResourceKey<Feature> PATCH_TALL_GRASS = patch("tall_grass");

    public static final ResourceKey<Feature> PATCH_SANDY_GRASS = patch("sandy_grass");
    //FLOWERS
    public static final ResourceKey<Feature> SINGLE_TASSEL = key("single/tassel");
    public static final ResourceKey<Feature> PATCH_BARLEY = patch("barley");
    //BIOSHROOM
    public static final ResourceKey<Feature> PATCH_BLUE_BIOSHROOM = patch("blue_bioshroom");
    public static final ResourceKey<Feature> PATCH_GREEN_BIOSHROOM = patch("green_bioshroom");
    public static final ResourceKey<Feature> PATCH_PINK_BIOSHROOM = patch("pink_bioshroom");
    //OTHER
    public static final ResourceKey<Feature> PATCH_ELEPHANT_EAR = patch("elephant_ear");

    public static void bootstrap(BootstrapContext<Feature> context) {
        var features = context.lookup(Registries.FEATURE);
        
        net.minecraft.util.random.WeightedList.Builder<BlockState> duskMelon = net.minecraft.util.random.WeightedList.builder();
        duskMelon.add(RUBlocks.DUSKMELON.get().defaultBlockState().setValue(DuskmelonBlock.AGE, 1), 3).add(RUBlocks.DUSKMELON.get().defaultBlockState().setValue(DuskmelonBlock.AGE, 2), 2);

        registerPlaced(context, SINGLE_BLACKWOOD_BIOSHROOMS, new SimpleBlockFeature(
            new WeightedStateProvider(net.minecraft.util.random.WeightedList.<BlockState>builder()
                .add(RUBlocks.BLUE_BIOSHROOM.get().defaultBlockState(), 3)
                .add(RUBlocks.PINK_BIOSHROOM.get().defaultBlockState(), 3)
                .add(RUBlocks.TALL_BLUE_BIOSHROOM.get().defaultBlockState(), 1)
                .add(RUBlocks.TALL_PINK_BIOSHROOM.get().defaultBlockState(), 1))
        ));
        //---------------------FEATURES---------------------//
        //SIMPLE_RANDOM_SELECTOR
        
        registerSelector(context, PATCH_TALL_FLOWERS, builder -> builder
            .add(simplePatch(RUBlocks.TASSEL.get()))
            .add(simplePatch(RUBlocks.DAY_LILY.get()))
            .add(simplePatch(RUBlocks.MEADOW_SAGE.get()))
            .add(simplePatch(Blocks.LILAC))
            .add(simplePatch(Blocks.ROSE_BUSH))
            .add(simplePatch(Blocks.PEONY))
            .add(simplePatch(Blocks.LILY_OF_THE_VALLEY))
        );
        //SIMPLE_BLOCK
        registerPlaced(context, SINGLE_ASTER, simple(RUBlocks.ASTER.get()));
        registerPlaced(context, SINGLE_CORPSE_FLOWER, simple(RUBlocks.CORPSE_FLOWER.get()));
        registerPlaced(context, SINGLE_DAY_LILY, simple(RUBlocks.DAY_LILY.get()));
        registerPlaced(context, SINGLE_DUSKTRAP, simple(RUBlocks.DUSKTRAP.get()));
        registerPlaced(context, SINGLE_MEADOW_SAGE, simple(RUBlocks.MEADOW_SAGE.get()));
        registerPlaced(context, SINGLE_BARREL_CACTUS, simple(RUBlocks.BARREL_CACTUS.get()));
        register(context, SINGLE_TASSEL, simple(RUBlocks.TASSEL.get()));
        registerPlaced(context, SINGLE_SNOWBELLE, simple(RUBlocks.SNOWBELLES.getWhite().get()));
        //RANDOM_PATCH
        registerPlaced(context, PATCH_ASHEN_GRASS, randomPatch(BlockStateProvider.of(RUBlocks.ASHEN_GRASS.get()), 32, BlockPredicate.matchesBlocks(Vec3i.ZERO.below(), List.of(RUBlocks.ASHEN_DIRT.get()))));
        registerPlaced(context, PATCH_ASHEN_GRASS_SMOULDERING, randomPatch(BlockStateProvider.of(RUBlocks.ASHEN_GRASS.get().defaultBlockState().setValue(AshenGrassBlock.SMOULDERING, true)), 64, BlockPredicate.matchesBlocks(Vec3i.ZERO.below(), List.of(RUBlocks.ASH.get(), Blocks.BASALT, Blocks.POLISHED_BASALT))));
        registerPlaced(context, PATCH_ASH_VENTS, randomPatch(96, 6, 0, Holder.direct(new PlacedFeature(
            Holder.direct(new RandomSelectorFeature(
                List.of(new WeightedPlacedFeature(
                    inlinePlaced(new SimpleBlockFeature(new WeightedStateProvider(WeightedList.<BlockState>builder().add(Blocks.BASALT.defaultBlockState(), 1).add(Blocks.SMOOTH_BASALT.defaultBlockState(), 1)))),
                    0.9f
                )),
                inlinePlaced(new BlockColumnFeature(
                    List.of(
                        BlockColumnFeature.layer(UniformInt.of(0, 4), BlockStateProvider.of(Blocks.BASALT)),
                        BlockColumnFeature.layer(ConstantInt.of(1), BlockStateProvider.of(RUBlocks.ASH_VENT.get()))
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
                RUFeatureUtils.airAndBlocksBelow(RUBlocks.ASH.get()),
                OffsetPlacement.vertical(ConstantInt.of(-1))
            )
        ))));
        registerPlaced(context, PATCH_FLOWERS_TUNDRA, randomPatch(new NoiseProvider(
            923586L,
            NormalNoise.createParity(-7, 2, 1.3),
            1.5f,
            List.of(
               Blocks.LILY_OF_THE_VALLEY.defaultBlockState(),
               RUBlocks.BLEEDING_HEART.get().defaultBlockState(),
               Blocks.POPPY.defaultBlockState()
            )
        ), 48));
        registerPlaced(context, PATCH_FLOWERS_WISTERIA_GROVE, randomPatch(new NoiseProvider(
            530167L,
            NormalNoise.createParity(-3, 1),
            0.1f,
            List.of(
                RUBlocks.HYSSOP.get().defaultBlockState(),
                RUBlocks.FIREWEED.get().defaultBlockState(),
                RUBlocks.DAISY.get().defaultBlockState()
            )
        ), 8));
        
        register(context, PATCH_SHORT_GRASS, randomPatch(weightedStates(pair(Blocks.SHORT_GRASS), pair(RUBlocks.GRASS_SPROUTS.get())), 64));
        register(context, PATCH_SANDY_GRASS, randomPatch(BlockStateProvider.of(RUBlocks.SANDY_GRASS.get()), 64));
        registerPlaced(context, PATCH_DESERT_SHRUB_ON_GRASS, randomPatch(BlockStateProvider.of(RUBlocks.SHORT_DEAD_GRASS.get()), 64, BlockPredicate.matchesTag(Vec3i.ZERO.below(), BlockTags.DIRT)));
        var smallDesertShrub = registerPlaced(context, PATCH_DESERT_SHRUB_ON_SAND, randomPatch(BlockStateProvider.of(RUBlocks.SHORT_DEAD_GRASS.get()), 64, BlockPredicate.matchesTag(Vec3i.ZERO.below(), BlockTags.SAND)));
        var deadSteppeShrub = registerPlaced(context, PATCH_STEPPE_SHRUB_ON_SAND, randomPatch(BlockStateProvider.of(RUBlocks.TALL_DEAD_GRASS.get()), 64, BlockPredicate.matchesTag(Vec3i.ZERO.below(), BlockTags.SAND)));

        register(context, PATCH_BARLEY, randomPatch(BlockStateProvider.of(RUBlocks.BARLEY.get()), 144));
        registerPlaced(context, PATCH_BLADED_GRASS, randomPatch(weightedStates(pair(RUBlocks.BLADED_GRASS.get(), 4), pair(RUBlocks.BLADED_TALL_GRASS.get()), pair(Blocks.SHORT_GRASS, 4)), 64));
        registerPlaced(context, PATCH_CAVE_HYSSOP, randomPatch(BlockStateProvider.of(RUBlocks.CAVE_HYSSOP.get().defaultBlockState()), 32));
        registerPlaced(context, PATCH_CLOVER, randomPatch(96, 6, 2, PlacementUtils.inlinePlaced(new SimpleBlockFeature(new RandomizedGroundCoverStateProvider(RUBlocks.CLOVER.get())), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE))));
        register(context, PATCH_FERN, randomPatch(BlockStateProvider.of(Blocks.FERN), 32));
        registerPlaced(context, PATCH_PRISMOSS_SPROUT, randomPatch(BlockStateProvider.of(RUBlocks.PRISMOSS_SPROUT.get()), 32));
        registerPlaced(context, PATCH_REDSTONE_BUD, randomPatch(BlockStateProvider.of(RUBlocks.REDSTONE_BUD.get()), 128));
        registerPlaced(context, PATCH_REDSTONE_BULB, randomPatch(BlockStateProvider.of(RUBlocks.REDSTONE_BULB.get()), 64));
        registerPlaced(context, PATCH_FERN_REDWOODS, randomPatch(weightedStates(
            pair(Blocks.FERN, 5),
            pair(Blocks.LARGE_FERN, 2),
            pair(Blocks.SHORT_GRASS, 2),
            pair(RUBlocks.GRASS_SPROUTS.get())
        ), 128));
        registerPlaced(context, PATCH_GRASSES_STEPPE, randomPatch(new DualNoiseProvider(
            new InclusiveRange<>(2, 3),
            NormalNoise.createParity(-6, 2.5),
            0.7f,
            9987,
            NormalNoise.createParity(-2, 1),
            1,
            List.of(
                state(Blocks.SHORT_GRASS),
                state(RUBlocks.GRASS_SPROUTS),
                state(RUBlocks.TALL_DEAD_GRASS),
                state(RUBlocks.SHORT_DEAD_GRASS)
            )
        ), 32));
        registerPlaced(context, PATCH_FROZEN_GRASS, randomPatch(BlockStateProvider.of(RUBlocks.FROZEN_GRASS.get().defaultBlockState()), 32));
        registerPlaced(context, PATCH_GRASS_SPROUTS, randomPatch(BlockStateProvider.of(RUBlocks.GRASS_SPROUTS.get().defaultBlockState()), 32));
        register(context, PATCH_TALL_GRASS, randomPatch(BlockStateProvider.of(Blocks.TALL_GRASS.defaultBlockState()), 32));
        registerPlaced(context, PATCH_WINDSWEPT_GRASS, randomPatch(BlockStateProvider.of(RUBlocks.WINDSWEPT_GRASS.get().defaultBlockState()), 32));
        //FLOWER
        registerPlaced(context, PATCH_SNOWY_FLOWERS, randomPatch(new WeightedStateProvider(net.minecraft.util.random.WeightedList.<BlockState>builder().add(RUBlocks.BLEEDING_HEART.get().defaultBlockState(), 3).add(Blocks.LILY_OF_THE_VALLEY.defaultBlockState(), 2)), 127));
        registerPlaced(context, PATCH_BAMBOO_FLOWERS, randomPatch(new WeightedStateProvider(net.minecraft.util.random.WeightedList.<BlockState>builder().add(RUBlocks.FIREWEED.get().defaultBlockState(), 3).add(RUBlocks.TSUBAKI.get().defaultBlockState(), 2).add(RUBlocks.PINK_LUPINE.get().defaultBlockState(), 3).add(Blocks.PINK_TULIP.defaultBlockState(), 3)), 127));
        registerPlaced(context, PATCH_ALPHA_DANDELION, randomPatch(RUBlocks.ALPHA_DANDELION, 96, 6, 2));
        registerPlaced(context, PATCH_ALPHA_ROSE, randomPatch(RUBlocks.ALPHA_ROSE, 96, 6, 2));
        registerPlaced(context, PATCH_AZURE_DAISY, randomPatch(32, 4, 2, PlacementUtils.inlinePlaced(new SimpleBlockFeature(new WeightedStateProvider(net.minecraft.util.random.WeightedList.<BlockState>builder().add(Blocks.AZURE_BLUET.defaultBlockState(), 1).add(Blocks.OXEYE_DAISY.defaultBlockState(), 1).add(RUBlocks.FELICIA_DAISY.get().defaultBlockState(), 2))), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE))));
        registerPlaced(context, PATCH_DAISY, randomPatch(RUBlocks.DAISY, 8, 1, 2));
        registerPlaced(context, PATCH_ELEPHANT_EAR_UNRECOVERABLY_DENSE, simple(RUBlocks.ELEPHANT_EAR.get()));
        register(context, PATCH_ELEPHANT_EAR, randomPatch(RUBlocks.ELEPHANT_EAR, 8, 6, 2));
        registerPlaced(context, PATCH_WARATAH, randomPatch(RUBlocks.WARATAH, 8, 1, 2));
        registerPlaced(context, PATCH_DAISIES, randomPatch(new DualNoiseProvider(
            new InclusiveRange<>(2),
            NormalNoise.createParity(-7, 2.5),
            0.6f,
            9987,
            NormalNoise.createParity(-4, 1),
            1,
            List.of(
                state(RUBlocks.DAISY),
                state(Blocks.SHORT_GRASS),
                state(RUBlocks.FELICIA_DAISY)
            )
        ), 32));
        registerPlaced(context, PATCH_LUPINES, randomPatch(new DualNoiseProvider(
            new InclusiveRange<>(2),
            NormalNoise.createParity(-7, 1),
            0.75f,
            9989,
            NormalNoise.createParity(-4, 1),
            1,
            List.of(
                state(RUBlocks.YELLOW_LUPINE),
                state(RUBlocks.RED_LUPINE),
                state(RUBlocks.BLUE_LUPINE),
                state(RUBlocks.PURPLE_LUPINE),
                state(RUBlocks.PINK_LUPINE)
            )
        ), 64));

        registerPlaced(context, PATCH_POPPIES, randomPatch(
            new NoiseProvider(498625, NormalNoise.createParity(-6, 1.3D), 1F, List.of(
                RUBlocks.POPPY_BUSH.get().defaultBlockState(),
                Blocks.POPPY.defaultBlockState(),
                RUBlocks.SALMON_POPPY.get().defaultBlockState(),
                RUBlocks.SALMON_POPPY_BUSH.get().defaultBlockState()
            )),
            64
        ));
        registerPlaced(context, PATCH_PRAIRIE_FLOWERS, randomPatch(32, 4, 2, PlacementUtils.inlinePlaced(new SimpleBlockFeature(new WeightedStateProvider(net.minecraft.util.random.WeightedList.<BlockState>builder().add(RUBlocks.POPPY_BUSH.get().defaultBlockState(), 3).add(RUBlocks.RED_LUPINE.get().defaultBlockState(), 2).add(RUBlocks.YELLOW_LUPINE.get().defaultBlockState(), 1))), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE))));
        registerPlaced(context, PATCH_ORANGE_CONEFLOWER, randomPatch(36, 4, 2,
            PlacementUtils.inlinePlaced(new SimpleBlockFeature(new RandomizedGroundCoverStateProvider(RUBlocks.ORANGE_CONEFLOWER.get())), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE))
        ));
        registerPlaced(context, PATCH_PURPLE_CONEFLOWER, randomPatch(36, 4, 2,
            PlacementUtils.inlinePlaced(new SimpleBlockFeature(new RandomizedGroundCoverStateProvider(RUBlocks.PURPLE_CONEFLOWER.get())), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE))
        ));
        registerPlaced(context, PATCH_SHRUBLAND_FLOWERS, randomPatch(32, 4, 2, PlacementUtils.inlinePlaced(new SimpleBlockFeature(new WeightedStateProvider(net.minecraft.util.random.WeightedList.<BlockState>builder().add(RUBlocks.RED_LUPINE.get().defaultBlockState(), 1).add(RUBlocks.BLUE_LUPINE.get().defaultBlockState(), 1))), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE))));
        registerPlaced(context, PATCH_WILLOW_FLOWERS, randomPatch(32, 4, 2, PlacementUtils.inlinePlaced(new SimpleBlockFeature(new WeightedStateProvider(net.minecraft.util.random.WeightedList.<BlockState>builder().add(RUBlocks.FELICIA_DAISY.get().defaultBlockState(), 2).add(RUBlocks.BLUE_LUPINE.get().defaultBlockState(), 2).add(Blocks.ALLIUM.defaultBlockState(), 1).add(Blocks.CORNFLOWER.defaultBlockState(), 2))), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE))));
        registerPlaced(context, PATCH_SMALL_FLOWERS, randomPatch(96, 6, 2, PlacementUtils.inlinePlaced(new SimpleBlockFeature(new NoiseProvider(2345L, NormalNoise.createParity(0, 1.0D), 0.075F, List.of(RUBlocks.MALLOW.get().defaultBlockState(), RUBlocks.YELLOW_LUPINE.get().defaultBlockState(), Blocks.PINK_TULIP.defaultBlockState(), RUBlocks.POPPY_BUSH.get().defaultBlockState(), Blocks.OXEYE_DAISY.defaultBlockState(), RUBlocks.RED_LUPINE.get().defaultBlockState(), Blocks.CORNFLOWER.defaultBlockState(), RUBlocks.PINK_LUPINE.get().defaultBlockState(), RUBlocks.TSUBAKI.get().defaultBlockState(), Blocks.ORANGE_TULIP.defaultBlockState(), RUBlocks.WARATAH.get().defaultBlockState(), Blocks.WHITE_TULIP.defaultBlockState(), RUBlocks.HYSSOP.get().defaultBlockState(), Blocks.ALLIUM.defaultBlockState(), RUBlocks.BLUE_LUPINE.get().defaultBlockState(), RUBlocks.BLEEDING_HEART.get().defaultBlockState(), RUBlocks.SALMON_POPPY_BUSH.get().defaultBlockState(), RUBlocks.WHITE_TRILLIUM.get().defaultBlockState(), Blocks.BLUE_ORCHID.defaultBlockState(), RUBlocks.FIREWEED.get().defaultBlockState(), RUBlocks.DAISY.get().defaultBlockState(), RUBlocks.PURPLE_LUPINE.get().defaultBlockState(), Blocks.RED_TULIP.defaultBlockState(), RUBlocks.FELICIA_DAISY.get().defaultBlockState(), Blocks.POPPY.defaultBlockState(), Blocks.LILY_OF_THE_VALLEY.defaultBlockState()))), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE))));
        registerPlaced(context, PATCH_TULIPS, randomPatch(new WeightedStateProvider(net.minecraft.util.random.WeightedList.<BlockState>builder().add(Blocks.WHITE_TULIP.defaultBlockState(), 4).add(Blocks.PINK_TULIP.defaultBlockState(), 2).add(Blocks.ORANGE_TULIP.defaultBlockState(), 2).add(Blocks.RED_TULIP.defaultBlockState(), 2)), 127));
        registerPlaced(context, PATCH_FLOWERS_AUTUMNAL_MAPLE_FOREST, randomPatch(
            new NoiseProvider(
                809256L,
                    NormalNoise.createParity(-5, 2),
                1.3f,
                List.of(
                    RUBlocks.WHITE_TRILLIUM.get().defaultBlockState(),
                    Blocks.LILY_OF_THE_VALLEY.defaultBlockState(),
                    Blocks.DANDELION.defaultBlockState()
                )
            ),
            24,
            BlockPredicate.not(BlockPredicate.matchesTag(Vec3i.ZERO.below(), RUBlockTags.DIRT_AND_PODZOL))
        ));
        registerPlaced(context, PATCH_WILTING_TRILLIUM, randomPatch(RUBlocks.WILTING_TRILLIUM, 32, 4, 2));
        registerPlaced(context, PATCH_TSUBAKI, randomPatch(RUBlocks.TSUBAKI, 32, 1, 2));
        registerPlaced(context, PATCH_HIBISCUS, randomPatch(RUBlocks.HIBISCUS, 14, 1, 2));
        registerPlaced(context, PATCH_MALLOW, randomPatch(RUBlocks.MALLOW, 16, 6, 2));
        registerPlaced(context, PATCH_HYSSOP, randomPatch(RUBlocks.HYSSOP, 16, 1, 2));
        
        var tassel = registerPlaced(context, PATCH_TASSEL, randomPatch(RUBlocks.TASSEL, 96, 7, 3));
        var lilac = registerPlaced(context, PATCH_LILAC, randomPatch(() -> Blocks.LILAC, 96, 7, 3));
        var peony = registerPlaced(context, PATCH_PEONY, randomPatch(() -> Blocks.PEONY, 96, 7, 3));
        registerSelector(context, PATCH_TALL_FLOWERS_WISTERIA_GROVE, b -> b
            .add(direct(tassel))
            .add(direct(lilac))
            .add(direct(peony))
        );
        
        //FOOD_PLANTS
        registerPlaced(context, PATCH_SALMONBERRY_BUSH, randomPatch(
            BlockStateProvider.of(RUBlocks.SALMONBERRY_BUSH.get().defaultBlockState().setValue(SalmonBerryBushBlock.AGE, 3)),
            127,
            BlockPredicate.matchesBlocks(Vec3i.ZERO.below(), List.of(Blocks.GRASS_BLOCK, Blocks.PODZOL, RUBlocks.PEAT_GRASS_BLOCK.get(), RUBlocks.SILT_GRASS_BLOCK.get(), RUBlocks.PEAT_PODZOL.get(), RUBlocks.SILT_PODZOL.get()))
        ));
        registerPlaced(context, SINGLE_DUSKMELON, new SimpleBlockFeature(new WeightedStateProvider(duskMelon)));
        //BIOSHROOM
        var blueBioshroomPatch = register(context, PATCH_BLUE_BIOSHROOM, randomPatch(weightedStates(pair(RUBlocks.TALL_BLUE_BIOSHROOM, 1), pair(RUBlocks.BLUE_BIOSHROOM, 10)), 16));
        var greenBioshroomPatch = register(context, PATCH_GREEN_BIOSHROOM, randomPatch(weightedStates(pair(RUBlocks.TALL_GREEN_BIOSHROOM, 1), pair(RUBlocks.GREEN_BIOSHROOM, 10)), 16));
        var pinkBioshroomPatch = register(context, PATCH_PINK_BIOSHROOM, randomPatch(weightedStates(pair(RUBlocks.TALL_PINK_BIOSHROOM, 1), pair(RUBlocks.PINK_BIOSHROOM, 10)), 16));
        
        registerSelector(context, PATCH_CAVE_BIOSHROOMS, builder -> builder
            .add(direct(blueBioshroomPatch), 7)
            .add(direct(greenBioshroomPatch), 7)
            .add(direct(pinkBioshroomPatch), 1)
        );
        //OTHER
        var floweringLilySmall = registerPlaced(context, PATCH_FLOWERING_LILY_PAD, randomPatch(RUBlocks.FLOWERING_LILY_PAD, 10, 7, 3));
        var floweringLilyGiant = registerPlaced(context, SPECIAL_GIANT_LILY, new GiantLilyPadFeature());
        
        registerPlaced(context, PATCH_DROPLEAF, randomPatch(16, 4, 2,
            PlacementUtils.inlinePlaced(new BlockColumnFeature(
                List.of(
                    BlockColumnFeature.layer(new WeightedListInt(net.minecraft.util.random.WeightedList.<IntProvider>builder().add(UniformInt.of(0, 19), 2).add(UniformInt.of(0, 2), 3).add(UniformInt.of(0, 6), 10).build()), BlockStateProvider.of(RUBlocks.DROPLEAF_PLANT.get())),
                    BlockColumnFeature.layer(ConstantInt.of(1), new RandomizedIntStateProvider(BlockStateProvider.holderOf(RUBlocks.DROPLEAF.get()), "age", UniformInt.of(22, 24)))
                ), Direction.DOWN, BlockPredicate.ONLY_IN_AIR_PREDICATE, true
            ),
            BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.hasSturdyFace(Direction.UP, Direction.DOWN), BlockPredicate.matchesTag(BlockTags.AIR))))));
        registerPlaced(context, PATCH_DUCKWEED, randomPatch(RUBlocks.DUCKWEED, 24, 4, 0));
        
        
        
        registerSelector(context, RUPlacedFeatures.VANILLA_BADLANDS_STEPPE_GRASS, builder -> builder
            .add(direct(smallDesertShrub))
            .add(direct(deadSteppeShrub))
        );
        registerPlaced(context, RUPlacedFeatures.VANILLA_BASALT_DELTAS_ASH_VENTS, simple(RUBlocks.ASH_VENT.get()));
        registerSelector(context, RUPlacedFeatures.VANILLA_FOREST_FLOWERS, builder -> builder
            .add(simplePatch(RUBlocks.TASSEL.get()))
            .add(simplePatch(RUBlocks.DAY_LILY.get()))
            .add(simplePatch(RUBlocks.MEADOW_SAGE.get()))
        );
        registerPlaced(context, RUPlacedFeatures.VANILLA_BIRCH_ORANGE_CONEFLOWERS, randomPatch(new RandomizedGroundCoverStateProvider(RUBlocks.ORANGE_CONEFLOWER.get()), 127));
        registerSelector(context, RUPlacedFeatures.VANILLA_SAVANNA_BUSHES, builder -> builder
            .add(direct(features.getOrThrow(TREE_ACACIA_SHRUB)), 2)
            .add(direct(features.getOrThrow(TREE_OAK_SHRUB_SMALL)), 1)
        );
        registerSelector(context, RUPlacedFeatures.VANILLA_MANGROVE_FLOWERING_LILIES, builder -> builder
            .add(direct(floweringLilySmall), 4)
            .add(direct(floweringLilyGiant), 1)
        );
        
        registerPlaced(context, RUPlacedFeatures.VANILLA_SWAMP_TREES, LithostitchedFeatures.placeUntilFailure(
            HolderSet.direct(
                direct(Holder.direct(new WeightedRandomSelectorFeature(
                    WeightedList.<Holder<PlacedFeature>>builder().add(direct(features.getOrThrow(TREE_WILLOW_SWAMP)), 2).add(direct(features.getOrThrow(TREE_OAK_SWAMP)), 3).build()
                ))),
                Holder.direct(new PlacedFeature(
                    Holder.direct(new SimpleBlockFeature(new WeightedStateProvider(WeightedList.<BlockState>builder().add(RUBlocks.GREEN_BIOSHROOM.get().defaultBlockState(), 1).add(RUBlocks.BLUE_BIOSHROOM.get().defaultBlockState(), 1)))),
                    List.of(
                        RarityFilter.onAverageOnceEvery(2),
                        new OffsetPlacement(UniformInt.of(-1, 1), UniformInt.of(2, 3), UniformInt.of(-1, 1)),
                        RUFeatureUtils.airAndBlocksBelow(Blocks.OAK_LOG, RUBlocks.WILLOW_WOOD_SET.getLog()),
                        LithostitchedPlacementModifiers.condition(LithostitchedPlacementConditions.offset(
                            LithostitchedPlacementConditions.inBiome(context.lookup(Registries.BIOME).getOrThrow(RUBiomes.BIOSHROOM_CAVES)),
                            BlockPos.ZERO.below(64)
                        ))
                    )
                ))
            )
        ));
        registerPlaced(context, RUPlacedFeatures.VANILLA_TAIGA_PURPLE_CONEFLOWERS, randomPatch(new RandomizedGroundCoverStateProvider(RUBlocks.PURPLE_CONEFLOWER.get()), 127));
    }
    
    private static Holder<PlacedFeature> simplePatch(Block block) {
        return PlacementUtils.inlinePlaced(randomPatch(BlockStateProvider.of(block), 127));
    }
    
    private static SimpleBlockFeature simple(Block block) {
        return new SimpleBlockFeature(BlockStateProvider.of(block));
    }

    private static SimpleBlockFeature simple(BlockState state) {
        return new SimpleBlockFeature(BlockStateProvider.of(state));
    }
}
