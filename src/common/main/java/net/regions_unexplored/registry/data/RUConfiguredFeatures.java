package net.regions_unexplored.registry.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.regions_unexplored.RegionsUnexplored;

public interface RUConfiguredFeatures {
    // Trees
    ResourceKey<Feature> TREE_GIANT_BLUE_BIOSHROOM = tree("giant_blue_bioshroom");
    ResourceKey<Feature> TREE_GIANT_GREEN_BIOSHROOM = tree("giant_green_bioshroom");
    ResourceKey<Feature> TREE_GIANT_PINK_BIOSHROOM = tree("giant_pink_bioshroom");
    ResourceKey<Feature> TREE_YELLOW_BIOSHROOM_SMALL = key("nether/tree/yellow_bioshroom_small");
    ResourceKey<Feature> TREE_YELLOW_BIOSHROOM_LARGE = key("nether/tree/yellow_bioshroom_large");
    ResourceKey<Feature> TREE_GIANT_RED_MUSHROOM = tree("giant_red_mushroom");
    ResourceKey<Feature> TREE_GIANT_BROWN_MUSHROOM = tree("giant_brown_mushroom");
    ResourceKey<Feature> TREE_BRIM_WILLOW = key("nether/tree/brim_willow");
    ResourceKey<Feature> TREE_TALL_BRIM_WILLOW = key("nether/tree/tall_brim_willow");
    ResourceKey<Feature> TREE_ACACIA = tree("acacia");
    ResourceKey<Feature> TREE_ACACIA_SHRUB = tree("acacia_shrub");
    ResourceKey<Feature> TREE_ALPHA_OAK = tree("alpha_oak");
    ResourceKey<Feature> TREE_ASHEN = tree("ashen");
    ResourceKey<Feature> TREE_ASHEN_PINE = tree("ashen_pine");
    ResourceKey<Feature> TREE_BAMBOO = tree("bamboo");
    ResourceKey<Feature> TREE_MEGA_BAOBAB = tree("mega_baobab");
    ResourceKey<Feature> TREE_ULTRA_BAOBAB = tree("ultra_baobab");
    ResourceKey<Feature> TREE_FLOWERING_OAK = tree("flowering_oak");
    ResourceKey<Feature> TREE_BIG_FLOWERING_OAK = tree("big_flowering_oak");
    ResourceKey<Feature> TREE_APPLE_OAK = tree("apple_oak");
    ResourceKey<Feature> TREE_BIG_APPLE_OAK = tree("big_apple_oak");
    ResourceKey<Feature> TREE_BLACKWOOD = tree("blackwood");
    ResourceKey<Feature> TREE_BLUE_BIOSHROOM = tree("blue_bioshroom");
    ResourceKey<Feature> TREE_BIG_BLACKWOOD = tree("big_blackwood");
    ResourceKey<Feature> TREE_GIANT_BLACKWOOD = tree("giant_blackwood");
    ResourceKey<Feature> TREE_BIRCH_ASPEN = tree("birch_aspen");
    ResourceKey<Feature> TREE_BIRCH_ASPEN_2 = tree("birch_aspen_2");
    ResourceKey<Feature> TREE_COBALT = key("nether/tree/cobalt");
    ResourceKey<Feature> TREE_TALL_DARK_OAK = tree("tall_dark_oak");
    ResourceKey<Feature> TREE_MAGNOLIA = tree("magnolia");
    ResourceKey<Feature> TREE_BLUE_MAGNOLIA = tree("blue_magnolia");
    ResourceKey<Feature> TREE_PINK_MAGNOLIA = tree("pink_magnolia");
    ResourceKey<Feature> TREE_WHITE_MAGNOLIA = tree("white_magnolia");
    ResourceKey<Feature> TREE_BIG_MAGNOLIA = tree("big_magnolia");
    ResourceKey<Feature> TREE_BIG_BLUE_MAGNOLIA = tree("big_blue_magnolia");
    ResourceKey<Feature> TREE_BIG_PINK_MAGNOLIA = tree("big_pink_magnolia");
    ResourceKey<Feature> TREE_BIG_WHITE_MAGNOLIA = tree("big_white_magnolia");
    ResourceKey<Feature> TREE_CYPRESS = tree("cypress");
    ResourceKey<Feature> TREE_GIANT_CYPRESS = tree("giant_cypress");
    ResourceKey<Feature> TREE_CHERRY = tree("cherry");
    ResourceKey<Feature> TREE_DEAD_BOG = tree("dead_bog");
    ResourceKey<Feature> TREE_DEAD = tree("dead");
    ResourceKey<Feature> TREE_BIG_DEAD = tree("big_dead");
    ResourceKey<Feature> TREE_DEAD_PINE = tree("dead_pine");
    ResourceKey<Feature> TREE_DEAD_PINE_TALL = tree("dead_pine_tall");
    ResourceKey<Feature> TREE_DEAD_STRIPPED_PINE = tree("dead_stripped_pine");
    ResourceKey<Feature> TREE_DEAD_STRIPPED_PINE_TALL = tree("dead_stripped_pine_tall");
    ResourceKey<Feature> TREE_DEAD_STRIPPED_PINE_MOUNTAIN = tree("dead_stripped_pine_mountain");
    ResourceKey<Feature> TREE_SMALL_EUCALYPTUS = tree("small_eucalyptus");
    ResourceKey<Feature> TREE_EUCALYPTUS = tree("eucalyptus");
    ResourceKey<Feature> TREE_JOSHUA_SMALL = tree("joshua_small");
    ResourceKey<Feature> TREE_JOSHUA_MEDIUM = tree("joshua_medium");
    ResourceKey<Feature> TREE_JOSHUA_LARGE = tree("joshua_large");
    ResourceKey<Feature> TREE_JUNGLE = tree("jungle");
    ResourceKey<Feature> TREE_JUNGLE_AQUATIC = tree("jungle_aquatic");
    ResourceKey<Feature> TREE_BIG_JUNGLE = tree("big_jungle");
    ResourceKey<Feature> TREE_KAPOK = tree("kapok");
    ResourceKey<Feature> TREE_LARCH = tree("larch");
    ResourceKey<Feature> TREE_LARCH_PINE = tree("larch_pine");
    ResourceKey<Feature> TREE_LARCH_LARGE = tree("larch_large");
    ResourceKey<Feature> TREE_LARCH_GOLDEN = tree("larch_golden");
    ResourceKey<Feature> TREE_LARCH_GOLDEN_PINE = tree("larch_golden_pine");
    ResourceKey<Feature> TREE_LARCH_GOLDEN_LARGE = tree("larch_golden_large");
    ResourceKey<Feature> TREE_MAPLE = tree("maple");
    ResourceKey<Feature> TREE_BIG_MAPLE = tree("big_maple");
    ResourceKey<Feature> TREE_RED_MAPLE = tree("red_maple");
    ResourceKey<Feature> TREE_BIG_RED_MAPLE = tree("big_red_maple");
    ResourceKey<Feature> TREE_ORANGE_MAPLE = tree("orange_maple");
    ResourceKey<Feature> TREE_BIG_ORANGE_MAPLE = tree("big_orange_maple");
    ResourceKey<Feature> TREE_OAK_WITH_BRANCH = tree("oak_with_branch");
    ResourceKey<Feature> TREE_OAK_WITH_FLOWERS = tree("oak_with_flowers");
    ResourceKey<Feature> TREE_OAK = tree("oak");
    ResourceKey<Feature> TREE_OAK_SWAMP = tree("oak_swamp");
    ResourceKey<Feature> TREE_OAK_TALL = tree("oak_tall");
    ResourceKey<Feature> TREE_BIG_OAK = tree("big_oak");
    ResourceKey<Feature> TREE_OAK_SHRUB_SMALL = tree("oak_shrub_small");
    ResourceKey<Feature> TREE_OAK_SHRUB_LARGE = tree("oak_shrub_large");
    ResourceKey<Feature> TREE_OAK_BUSH = tree("oak_bush");
    ResourceKey<Feature> TREE_OAK_BUSH_WITH_FLOWERS = tree("oak_bush_with_flowers");
    ResourceKey<Feature> TREE_PALM = tree("palm");
    ResourceKey<Feature> TREE_TALL_PALM = tree("tall_palm");
    ResourceKey<Feature> TREE_PALM_SHRUB = tree("palm_shrub");
    ResourceKey<Feature> TREE_PINE = tree("pine");
    ResourceKey<Feature> TREE_PINE_BEES = tree("pine_bees");
    ResourceKey<Feature> TREE_PINE_SKINNY = tree("pine_skinny");
    ResourceKey<Feature> TREE_PINE_SKINNY_TALL = tree("pine_skinny_tall");
    ResourceKey<Feature> TREE_PINE_TALL = tree("pine_tall");
    ResourceKey<Feature> TREE_PINE_TUNDRA = tree("pine_tundra");
    ResourceKey<Feature> TREE_PINK_BIOSHROOM = tree("pink_bioshroom");
    ResourceKey<Feature> TREE_SPRUCE_TUNDRA = tree("spruce_tundra");
    ResourceKey<Feature> TREE_STRIPPED_PINE = tree("stripped_pine");
    ResourceKey<Feature> TREE_STRIPPED_PINE_TALL = tree("stripped_pine_tall");
    ResourceKey<Feature> TREE_STRIPPED_PINE_MOUNTAIN = tree("stripped_pine_mountain");
    ResourceKey<Feature> TREE_PINE_SHRUB = tree("pine_shrub");
    ResourceKey<Feature> TREE_LUSH_PINE = tree("lush_pine");
    ResourceKey<Feature> TREE_SAGUARO_CACTUS = tree("saguaro_cactus");
    ResourceKey<Feature> TREE_ICE_SPIRE = tree("ice_spire");
    ResourceKey<Feature> TREE_SILVER_BIRCH = tree("silver_birch");
    ResourceKey<Feature> TREE_SILVER_BIRCH_TALL = tree("silver_birch_tall");
    ResourceKey<Feature> TREE_SPRUCE_TALL = tree("spruce_tall");
    ResourceKey<Feature> TREE_SPRUCE_SHRUB = tree("spruce_shrub");
    ResourceKey<Feature> TREE_SMALL_OAK = tree("small_oak");
    ResourceKey<Feature> TREE_LARGE_SOCOTRA = tree("large_socotra");
    ResourceKey<Feature> TREE_SMALL_SOCOTRA = tree("small_socotra");
    ResourceKey<Feature> TREE_REDWOOD_SMALL = tree("redwood_small");
    ResourceKey<Feature> TREE_REDWOOD_MEDIUM = tree("redwood_medium");
    ResourceKey<Feature> TREE_REDWOOD_LARGE = tree("redwood_large");
    ResourceKey<Feature> TREE_REDWOOD_EMERGENT = tree("redwood_emergent");
    ResourceKey<Feature> TREE_WILLOW = tree("willow");
    ResourceKey<Feature> TREE_BIG_WILLOW = tree("big_willow");
    ResourceKey<Feature> TREE_WILLOW_SWAMP = tree("willow_swamp");
    ResourceKey<Feature> TREE_WISTERIA_SKY = tree("wisteria_sky");
    ResourceKey<Feature> TREE_WISTERIA_LAVENDER = tree("wisteria_lavender");
    ResourceKey<Feature> TREE_WISTERIA_SALMON = tree("wisteria_salmon");
    ResourceKey<Feature> TREE_WISTERIA_LARGE_SKY = tree("wisteria_large_sky");
    ResourceKey<Feature> TREE_WISTERIA_LARGE_LAVENDER = tree("wisteria_large_lavender");
    ResourceKey<Feature> TREE_WISTERIA_LARGE_SALMON = tree("wisteria_large_salmon");
    // Bonemealables
    ResourceKey<Feature> BONEMEAL_ALPHA_GRASS = key("bonemeal/grass/alpha");
    ResourceKey<Feature> BONEMEAL_MYCOTOXIC_NYLIUM = key("bonemeal/nylium/mycotoxic");
    ResourceKey<Feature> BONEMEAL_GLISTERING_NYLIUM = key("bonemeal/nylium/glistering");
    ResourceKey<Feature> BONEMEAL_COBALT_NYLIUM = key("bonemeal/nylium/cobalt");
    ResourceKey<Feature> BONEMEAL_BRIMSPROUT_NYLIUM = key("bonemeal/nylium/brimsprout");

    static ResourceKey<Feature> shrub(String name) {
        return key("shrub/" + name);
    }
    
    static ResourceKey<Feature> tree(String name) {
        return key("tree/" + name);
    }
    
    static ResourceKey<Feature> patch(String name) {
        return key("patch/" + name);
    }
    
    static ResourceKey<Feature> key(String name) {
        return RegionsUnexplored.key(Registries.FEATURE, name);
    }

    static ResourceKey<Feature> fromPlaced(ResourceKey<PlacedFeature> key) {
        return ResourceKey.create(Registries.FEATURE, key.identifier());
    }
}
