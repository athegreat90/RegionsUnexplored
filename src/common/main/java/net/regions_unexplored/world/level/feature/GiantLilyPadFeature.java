package net.regions_unexplored.world.level.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.regions_unexplored.block.type.aquatic.GiantLilyPadBlock;

public class GiantLilyPadFeature implements Feature {
    public static final MapCodec<GiantLilyPadFeature> CODEC = MapCodec.unit(GiantLilyPadFeature::new);

    @Override
    public MapCodec<GiantLilyPadFeature> codec() {
        return CODEC;
    }

    public boolean place(WorldGenLevel level, ChunkGenerator generator, RandomSource random, BlockPos pos) {
        return GiantLilyPadBlock.tryPlace(level, pos, random);
    }
}