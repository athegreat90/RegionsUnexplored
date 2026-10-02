package net.regions_unexplored.datagen.provider.registry.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.attribute.BackgroundMusic;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.regions_unexplored.datagen.provider.registry.configured_feature.RUShrubFeatures;
import net.regions_unexplored.datagen.provider.registry.util.RUBiomeUtils;
import net.regions_unexplored.datagen.provider.registry.placed_feature.RuAquaticPlacements;
import net.regions_unexplored.datagen.provider.registry.placed_feature.RuMiscOverworldPlacements;
import net.regions_unexplored.datagen.provider.registry.placed_feature.RuTreePlacements;
import net.regions_unexplored.datagen.provider.registry.placed_feature.RuVegetationPlacements;
import net.regions_unexplored.registry.data.RUBiomes;

import static net.regions_unexplored.datagen.provider.registry.util.RUBiomeUtils.*;

public class WetBiomes {
    private static MobSpawnSettings.Builder baseSwampSpawning(boolean hasWolfSpawns) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);
        spawnBuilder.addSpawn(EntityTypes.SLIME, 1, 1, 1);
        spawnBuilder.addSpawn(EntityTypes.FROG, 10, 2, 5);
        spawnBuilder.addSpawn(EntityTypes.TROPICAL_FISH, 25, 8, 8);
        if (hasWolfSpawns) {
            spawnBuilder.addSpawn(EntityTypes.WOLF, 4, 2, 4);
        }
        return spawnBuilder;
    }
    private static MobSpawnSettings.Builder baseJungleSpawning(boolean hasWolfSpawns) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(spawnBuilder);
        spawnBuilder.addSpawn(EntityTypes.CHICKEN, 10, 4, 4);
        spawnBuilder.addSpawn(EntityTypes.PARROT, 40, 1, 2);
        spawnBuilder.addSpawn(EntityTypes.OCELOT, MobCategory.MONSTER, 2, net.minecraft.util.valueproviders.ConstantInt.of(1));
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);
        if (hasWolfSpawns) {
	        spawnBuilder.addSpawn(EntityTypes.WOLF, 8, 2, 4);
        }
        return spawnBuilder;
    }
    
    private static BiomeGenerationSettings.Builder baseSwampGeneration(HolderGetter<PlacedFeature> featureGetter, HolderGetter<WorldCarver> carverGetter, boolean hasLilyPads, boolean hasFlowers) {
        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(featureGetter, carverGetter);
        BiomeDefaultFeatures.addFossilDecoration(builder);
        RUBiomeUtils.globalOverworldGeneration(builder);
        BiomeDefaultFeatures.addDefaultOres(builder);
        BiomeDefaultFeatures.addSwampClayDisk(builder);
        if (hasFlowers) {
            builder.addFeature(Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_SWAMP);
        }
        builder.addFeature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH);
        if (hasLilyPads) {
            builder.addFeature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_WATERLILY);
        }
        builder.addFeature(Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_SWAMP);
        builder.addFeature(Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_SWAMP);
        BiomeDefaultFeatures.addDefaultMushrooms(builder);
        BiomeDefaultFeatures.addSwampExtraVegetation(builder);
        builder.addFeature(Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_SWAMP);
        return builder;
    }

    private static BiomeGenerationSettings.Builder baseJungleGeneration(HolderGetter<PlacedFeature> featureGetter, HolderGetter<WorldCarver> carverGetter, boolean sparseMelons) {
        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(featureGetter, carverGetter);
        RUBiomeUtils.globalOverworldGeneration(builder);
        BiomeDefaultFeatures.addDefaultOres(builder);
        BiomeDefaultFeatures.addDefaultSoftDisks(builder);
        BiomeDefaultFeatures.addJungleGrass(builder);
        BiomeDefaultFeatures.addDefaultMushrooms(builder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(builder, true);
        BiomeDefaultFeatures.addJungleVines(builder);
        if(sparseMelons) {
            BiomeDefaultFeatures.addSparseJungleMelons(builder);
        }
        else{
            BiomeDefaultFeatures.addJungleMelons(builder);
        }
        return builder;
    }

    public static Biome bayou(HolderGetter<PlacedFeature> featureGetter, HolderGetter<WorldCarver> carverGetter) {
        BiomeSpecialEffects.Builder effectBuilder = new BiomeSpecialEffects.Builder()
            .waterColor(0x437c4a)
            .foliageColorOverride(0x71934c)
            .grassColorOverride(0x7ca254);

        //add features
        BiomeGenerationSettings.Builder builder = baseSwampGeneration(featureGetter, carverGetter, false, true);

        //add RU features
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuTreePlacements.TREE_GROUP_BAYOU);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RUShrubFeatures.get(RUBiomes.BAYOU));
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_FLOWERING_LILY_PAD);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_ELEPHANT_EAR_SPARSE);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_FERN);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_SHORT_GRASS);


        //add mob spawns
        MobSpawnSettings.Builder spawnBuilder = baseSwampSpawning(true);

        return biomeBuilder(1, 1)
            .setAttribute(EnvironmentAttributes.CREATURE_WORLD_GEN_SPAWN_PROBABILITY, 0.03F)
            .setAttribute(EnvironmentAttributes.SKY_COLOR, rgb(0xa2c1b5))
            .setAttribute(EnvironmentAttributes.FOG_COLOR, rgb(0xb1ccb5))
            .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, rgb(0x60894a))
            .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_SWAMP))
            .specialEffects(effectBuilder.build())
            .mobSpawnSettings(spawnBuilder.build())
            .generationSettings(builder.build())
            .build();
    }

    public static Biome eucalyptusForest(HolderGetter<PlacedFeature> featureGetter, HolderGetter<WorldCarver> carverGetter) {
        BiomeSpecialEffects.Builder effectBuilder = effectBuilder()
            .foliageColorOverride(8828203)
            .grassColorOverride(9680182);

        //add features
        BiomeGenerationSettings.Builder builder = baseJungleGeneration(featureGetter, carverGetter, true);

        //add RU features
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuTreePlacements.TREE_GROUP_EUCALYPTUS_FOREST);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RUShrubFeatures.get(RUBiomes.EUCALYPTUS_FOREST));
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_WARATAH);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_ELEPHANT_EAR_SPARSE);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_TALL_GRASS);
        //add mob spawns
        MobSpawnSettings.Builder spawnBuilder = baseJungleSpawning(true);

        return biomeBuilder(1.3f, 0.85f)
            .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_JUNGLE))
            .specialEffects(effectBuilder.build())
            .mobSpawnSettings(spawnBuilder.build())
            .generationSettings(builder.build())
            .build();
    }

    public static Biome fen(HolderGetter<PlacedFeature> featureGetter, HolderGetter<WorldCarver> carverGetter) {
        BiomeSpecialEffects.Builder effectBuilder = effectBuilder()
            .waterColor(0x4e8bb3)
            .foliageColorOverride(8754506)
            .grassColorOverride(10858333);

        //add features
        BiomeGenerationSettings.Builder builder = baseSwampGeneration(featureGetter, carverGetter, false, true);

        //add RU features
        builder.addFeature(Decoration.RAW_GENERATION, RuMiscOverworldPlacements.SPECIAL_WATER_EDGE);

        builder.addFeature(Decoration.VEGETAL_DECORATION, RuTreePlacements.TREE_GROUP_FEN);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuMiscOverworldPlacements.FALLEN_OAK_DENSE);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RUShrubFeatures.get(RUBiomes.FEN));
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_DUCKWEED);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuAquaticPlacements.PATCH_CATTAIL_DENSE);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_FERN_DENSE);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_SHORT_GRASS_DENSE);

        //add mob spawns
        MobSpawnSettings.Builder spawnBuilder = baseSwampSpawning(true);

        return biomeBuilder(0.85f, 0.7f)
            .setAttribute(EnvironmentAttributes.CREATURE_WORLD_GEN_SPAWN_PROBABILITY, 0.03F)
            .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, rgb(0x3b6683))
            .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_SWAMP))
            .specialEffects(effectBuilder.build())
            .mobSpawnSettings(spawnBuilder.build())
            .generationSettings(builder.build())
            .build();
    }

    public static Biome marsh(HolderGetter<PlacedFeature> featureGetter, HolderGetter<WorldCarver> carverGetter) {
        BiomeSpecialEffects.Builder effectBuilder = effectBuilder()
            .waterColor(0x477bb7)
            .foliageColorOverride(0x80c16c)
            .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.SWAMP)
            .grassColorOverride(0x7dbf61);

        //add features
        BiomeGenerationSettings.Builder builder = baseSwampGeneration(featureGetter, carverGetter, true, true);

        //add RU features
        builder.addFeature(GenerationStep.Decoration.RAW_GENERATION, RuMiscOverworldPlacements.SPECIAL_CARVED_LIMITED_POOL);
        builder.addFeature(Decoration.RAW_GENERATION, RuMiscOverworldPlacements.SPECIAL_MARSH);
        builder.addFeature(Decoration.RAW_GENERATION, RuMiscOverworldPlacements.SPECIAL_MOSS_PATCH_WITH_WATER_DENSE);

        builder.addFeature(Decoration.VEGETAL_DECORATION, RuTreePlacements.TREE_GROUP_MARSH);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_DUCKWEED);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_SHORT_GRASS);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_TALL_GRASS);

        //add mob spawns
        MobSpawnSettings.Builder spawnBuilder = baseSwampSpawning(false);
        spawnBuilder.addSpawn(EntityTypes.AXOLOTL, 5, 2, 6);

        return biomeBuilder(1, 1)
            .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, rgb(0x2f4d5e))
            .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_SWAMP))
            .specialEffects(effectBuilder.build())
            .mobSpawnSettings(spawnBuilder.build())
            .generationSettings(builder.build())
            .build();
    }
    public static Biome fungalFen(HolderGetter<PlacedFeature> featureGetter, HolderGetter<WorldCarver> carverGetter) {
        BiomeSpecialEffects.Builder effectBuilder = new BiomeSpecialEffects.Builder()
            .waterColor(6338444)
            .foliageColorOverride(10667597)
            .grassColorOverride(8173383);

        //add features
        BiomeGenerationSettings.Builder builder = baseSwampGeneration(featureGetter, carverGetter, true, false);

        //add RU features
        builder.addFeature(Decoration.LOCAL_MODIFICATIONS, RuMiscOverworldPlacements.SPECIAL_WATER_EDGE);

        builder.addFeature(Decoration.VEGETAL_DECORATION, RuTreePlacements.TREE_GROUP_FUNGAL_FEN);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RUShrubFeatures.get(RUBiomes.FUNGAL_FEN));

        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_PINK_BIOSHROOM);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_FERN);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_SHORT_GRASS);


        //add mob spawns
        MobSpawnSettings.Builder spawnBuilder = baseSwampSpawning(true);
        spawnBuilder.addSpawn(EntityTypes.MOOSHROOM, 8, 4, 8);
        spawnBuilder.addSpawn(EntityTypes.AXOLOTL, 4, 2, 4);

        return biomeBuilder(1.15f, 1)
            .setAttribute(EnvironmentAttributes.CREATURE_WORLD_GEN_SPAWN_PROBABILITY, 0.03F)
            .setAttribute(EnvironmentAttributes.FOG_COLOR, rgb(0xc0e1d1))
            .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, rgb(0x00644a))
            .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_SWAMP))
            .specialEffects(effectBuilder.build())
            .mobSpawnSettings(spawnBuilder.build())
            .generationSettings(builder.build())
            .build();
    }

    public static Biome oldGrowthBayou(HolderGetter<PlacedFeature> featureGetter, HolderGetter<WorldCarver> carverGetter) {
        BiomeSpecialEffects.Builder effectBuilder = new BiomeSpecialEffects.Builder()
            .waterColor(-12354486)
            .foliageColorOverride(-9333940)
            .grassColorOverride(-8609196);

        //add features
        BiomeGenerationSettings.Builder builder = baseSwampGeneration(featureGetter, carverGetter, false, true);

        //add RU features
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuTreePlacements.TREE_GROUP_OLD_GROWTH_BAYOU);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RUShrubFeatures.get(RUBiomes.OLD_GROWTH_BAYOU));
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.SPECIAL_GIANT_LILY);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_FLOWERING_LILY_PAD);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_ELEPHANT_EAR_DENSE);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_FERN_DENSE);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_SHORT_GRASS_DENSE);
        
        //add mob spawns
        MobSpawnSettings.Builder spawnBuilder = baseSwampSpawning(false);

        return biomeBuilder(1.2f, 1)
            .setAttribute(EnvironmentAttributes.SKY_COLOR, rgb(0x87ad82))
            .setAttribute(EnvironmentAttributes.FOG_COLOR, rgb(0x87ad82))
            .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, rgb(0x60894a))
            .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_SWAMP))
            .specialEffects(effectBuilder.build())
            .mobSpawnSettings(spawnBuilder.build())
            .generationSettings(builder.build())
            .build();
    }
    
    public static Biome rainforest(HolderGetter<PlacedFeature> featureGetter, HolderGetter<WorldCarver> carverGetter) {
        BiomeSpecialEffects.Builder effectBuilder = new BiomeSpecialEffects.Builder()
            .waterColor(2202835)
            .foliageColorOverride(-11032271)
            .grassColorOverride(-9718455);
        
        //add features
        BiomeGenerationSettings.Builder builder = baseJungleGeneration(featureGetter, carverGetter, true);
        
        //add RU features
        builder.addFeature(Decoration.RAW_GENERATION, RuMiscOverworldPlacements.SPECIAL_MOSS_PATCH_WITH_WATER);
        
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuTreePlacements.TREE_GROUP_RAINFOREST);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RUShrubFeatures.get(RUBiomes.RAINFOREST));
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_HIBISCUS);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_ELEPHANT_EAR_DENSE);
        
        //add mob spawns
        MobSpawnSettings.Builder spawnBuilder = baseJungleSpawning(false);
        
        return biomeBuilder(0.95f, 0.9f)
            .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, rgb(0x0a57a6))
            .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_JUNGLE))
            .specialEffects(effectBuilder.build())
            .mobSpawnSettings(spawnBuilder.build())
            .generationSettings(builder.build())
            .build();
    }

    public static Biome sparseRainforest(HolderGetter<PlacedFeature> featureGetter, HolderGetter<WorldCarver> carverGetter) {
        BiomeSpecialEffects.Builder effectBuilder = new BiomeSpecialEffects.Builder()
            .waterColor(2202835)
            .foliageColorOverride(-11032271)
            .grassColorOverride(-9718455);

        //add features
        BiomeGenerationSettings.Builder builder = baseJungleGeneration(featureGetter, carverGetter, false);

        //add RU features
        builder.addFeature(Decoration.RAW_GENERATION, RuMiscOverworldPlacements.SPECIAL_MOSS_PATCH_WITH_WATER_SPARSE);
        
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuTreePlacements.TREE_GROUP_SPARSE_RAINFOREST);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RUShrubFeatures.get(RUBiomes.RAINFOREST));
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_HIBISCUS);
        builder.addFeature(Decoration.VEGETAL_DECORATION, RuVegetationPlacements.PATCH_ELEPHANT_EAR_DENSE);

        //add mob spawns
        MobSpawnSettings.Builder spawnBuilder = baseJungleSpawning(true);

        return biomeBuilder(0.95f, 0.9f)
            .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, rgb(0x0a57a6))
            .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_JUNGLE))
            .specialEffects(effectBuilder.build())
            .mobSpawnSettings(spawnBuilder.build())
            .generationSettings(builder.build())
            .build();
    }
}
