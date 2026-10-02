package net.regions_unexplored.registry;

import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.regions_unexplored.module.platform.Registrar;
import net.regions_unexplored.world.level.feature.*;
import net.regions_unexplored.world.level.feature.bioshroom.*;
import net.regions_unexplored.world.level.feature.tree.*;
import net.regions_unexplored.world.level.feature.tree.nether.*;
import net.regions_unexplored.worldgen.feature.CarvedLimitedPoolFeature;
import net.regions_unexplored.worldgen.feature.RUFallenTreeFeature;
import net.regions_unexplored.worldgen.feature.RURockFeature;

public interface RUFeatureTypes {
    // Trees
    Supplier<MapCodec<GiantBlueBioshroomFeature>> GIANT_BLUE_BIOSHROOM = register("giant_blue_bioshroom", GiantBlueBioshroomFeature.CODEC);
    Supplier<MapCodec<GiantGreenBioshroomFeature>> GIANT_GREEN_BIOSHROOM = register("giant_green_bioshroom", GiantGreenBioshroomFeature.CODEC);
    Supplier<MapCodec<GiantPinkBioshroomFeature>> GIANT_PINK_BIOSHROOM = register("giant_pink_bioshroom", GiantPinkBioshroomFeature.CODEC);
    Supplier<MapCodec<GiantYellowBioshroomFeature>> GIANT_YELLOW_BIOSHROOM = register("giant_yellow_bioshroom", GiantYellowBioshroomFeature.CODEC);
    Supplier<MapCodec<YellowBioshroomShrubFeature>> SMALL_YELLOW_BIOSHROOM = register("small_yellow_bioshroom", YellowBioshroomShrubFeature.CODEC);
    Supplier<MapCodec<AshenTreeFeature>> ASHEN_TREE = register("ashen_tree", AshenTreeFeature.CODEC);
    Supplier<MapCodec<AspenTreeFeature>> ASPEN_TREE = register("aspen_tree", AspenTreeFeature.CODEC);
    Supplier<MapCodec<BambooTreeFeature>> BAMBOO_TREE = register("bamboo_tree", BambooTreeFeature.CODEC);
    Supplier<MapCodec<MegaBaobabTreeFeature>> MEGA_BAOBAB_TREE = register("mega_baobab_tree", MegaBaobabTreeFeature.CODEC);
    Supplier<MapCodec<UltraBaobabTreeFeature>> ULTRA_BAOBAB_TREE = register("ultra_baobab_tree", UltraBaobabTreeFeature.CODEC);
    Supplier<MapCodec<BlackwoodTreeFeature>> BLACKWOOD_TREE = register("blackwood_tree", BlackwoodTreeFeature.CODEC);
    Supplier<MapCodec<CobaltShrubFeature>> COBALT_TREE = register("cobalt_tree", CobaltShrubFeature.CODEC);
    Supplier<MapCodec<CypressTreeFeature>> CYPRESS_TREE = register("cypress_tree", CypressTreeFeature.CODEC);
    Supplier<MapCodec<DeadTreeFeature>> DEAD_TREE = register("dead_tree", DeadTreeFeature.CODEC);
    Supplier<MapCodec<SmallEucalyptusTreeFeature>> SMALL_EUCALYPTUS_TREE = register("small_eucalyptus_tree", SmallEucalyptusTreeFeature.CODEC);
    Supplier<MapCodec<EucalyptusTreeFeature>> EUCALYPTUS_TREE = register("eucalyptus_tree", EucalyptusTreeFeature.CODEC);
    Supplier<MapCodec<GiantCypressTreeFeature>> GIANT_CYPRESS_TREE = register("giant_cypress_tree", GiantCypressTreeFeature.CODEC);
    Supplier<MapCodec<LarchTreeFeature>> LARCH_TREE = register("larch_tree", LarchTreeFeature.CODEC);
    Supplier<MapCodec<LargeJoshuaTreeFeature>> LARGE_JOSHUA_TREE = register("large_joshua_tree", LargeJoshuaTreeFeature.CODEC);
    Supplier<MapCodec<KapokTreeFeature>> KAPOK_TREE = register("kapok_tree", KapokTreeFeature.CODEC);
    Supplier<MapCodec<MediumJoshuaTreeFeature>> MEDIUM_JOSHUA_TREE = register("medium_joshua_tree", MediumJoshuaTreeFeature.CODEC);
    Supplier<MapCodec<PalmTreeFeature>> PALM_TREE = register("palm_tree", PalmTreeFeature.CODEC);
    Supplier<MapCodec<LushPineTreeFeature>> LUSH_PINE_TREE = register("lush_pine_tree", LushPineTreeFeature.CODEC);
    Supplier<MapCodec<SmallJoshuaTreeFeature>> SMALL_JOSHUA_TREE = register("small_joshua_tree", SmallJoshuaTreeFeature.CODEC);
    Supplier<MapCodec<SmallOakTreeFeature>> SMALL_OAK_TREE = register("small_oak_tree", SmallOakTreeFeature.CODEC);
    Supplier<MapCodec<StrippedPineTreeFeature>> STRIPPED_PINE_TREE = register("stripped_pine_tree", StrippedPineTreeFeature.CODEC);
    Supplier<MapCodec<SaguaroCactusFeature>> SAGUARO_CACTUS = register("saguaro_cactus", SaguaroCactusFeature.CODEC);
    Supplier<MapCodec<SakuraTreeFeature>> SAKURA_TREE = register("sakura_tree", SakuraTreeFeature.CODEC);
    Supplier<MapCodec<LargeSocotraTreeFeature>> LARGE_SOCOTRA_TREE = register("large_socotra_tree", LargeSocotraTreeFeature.CODEC);
    Supplier<MapCodec<SmallSocotraTreeFeature>> SMALL_SOCOTRA_TREE = register("small_socotra_tree", SmallSocotraTreeFeature.CODEC);
    Supplier<MapCodec<BrimWillowFeature>> BRIM_WILLOW = register("brim_willow", BrimWillowFeature.CODEC);
    Supplier<MapCodec<TallBrimWillowFeature>> TALL_BRIM_WILLOW = register("tall_brim_willow", TallBrimWillowFeature.CODEC);
    Supplier<MapCodec<TreeShrubFeature>> TREE_SHRUB = register("tree_shrub", TreeShrubFeature.CODEC);
    // Not trees
    Supplier<MapCodec<PointedRedstoneFeature>> POINTED_REDSTONE = register("pointed_redstone", PointedRedstoneFeature.CODEC);
    Supplier<MapCodec<PointedRedstoneClusterFeature>> POINTED_REDSTONE_CLUSTER = register("pointed_redstone_cluster", PointedRedstoneClusterFeature.CODEC);
    Supplier<MapCodec<HangingPrismariteFeature>> HANGING_PRISMARITE = register("hanging_prismarite", HangingPrismariteFeature.CODEC);
    Supplier<MapCodec<BasaltBlobFeature>> BASALT_BLOB = register("basalt_blob", BasaltBlobFeature.CODEC);
    Supplier<MapCodec<GiantLilyPadFeature>> GIANT_LILY = register("giant_lily", GiantLilyPadFeature.CODEC);
    Supplier<MapCodec<FloorIcicleFeature>> ICICLE_UP = register("icicle_up", FloorIcicleFeature.CODEC);
    Supplier<MapCodec<IceSpireFeature>> SPIRE = register("spire", IceSpireFeature.CODEC);
    Supplier<MapCodec<MarshFeature>> MARSH = register("marsh", MarshFeature.CODEC);
    Supplier<MapCodec<WaterEdgeFeature>> WATER_EDGE = register("water_edge", WaterEdgeFeature.CODEC);
    Supplier<MapCodec<RockPillarFeature>> ROCK_PILLAR = register("rock_pillar", RockPillarFeature.CODEC);
    Supplier<MapCodec<HyacinthStockFeature>> TALL_HYACINTH_STOCK = register("tall_hyacinth_stock", HyacinthStockFeature.CODEC);
    Supplier<MapCodec<HyacinthPlantsFeature>> HYACINTH_PLANTS = register("hyacinth_plants", HyacinthPlantsFeature.CODEC);
    Supplier<MapCodec<SeaRockFeature>> OCEAN_ROCK = register("ocean_rock", SeaRockFeature.CODEC);
    Supplier<MapCodec<NetherRockFeature>> NETHER_ROCK = register("nether_rock", NetherRockFeature.CODEC);
    Supplier<MapCodec<GlisteringIvyFeature>> GLISTERING_IVY = register("glistering_ivy", GlisteringIvyFeature.CODEC);
    Supplier<MapCodec<HangingEarlightFeature>> HANGING_EARLIGHT = register("hanging_earlight", HangingEarlightFeature.CODEC);
    Supplier<MapCodec<ObsidianSpireFeature>> OBSIDIAN_SPIRE = register("obsidian_spire", ObsidianSpireFeature.CODEC);
    // 0.6+ features
    Supplier<MapCodec<RURockFeature>> ROCK = register("rock", RURockFeature.CODEC);
    Supplier<MapCodec<RUFallenTreeFeature>> FALLEN_TREE = register("fallen_tree", RUFallenTreeFeature.CODEC);
    Supplier<MapCodec<CarvedLimitedPoolFeature>> CARVED_LIMITED_POOL = register("carved_limited_pool", CarvedLimitedPoolFeature.CODEC);

    private static <F extends Feature> Supplier<MapCodec<F>> register(String name, MapCodec<F> codec) {
        return Registrar.register(BuiltInRegistries.FEATURE_TYPE, name, () -> codec);
    }

    static void init() {
    }
}
