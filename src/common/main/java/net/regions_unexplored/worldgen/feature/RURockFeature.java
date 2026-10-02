package net.regions_unexplored.worldgen.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.regions_unexplored.worldgen.feature.config.RockFeatureConfig;

public class RURockFeature implements Feature {
    public static final IntProvider BLOB_Y_OFFSET = new WeightedListInt(WeightedList.<IntProvider>builder()
        .add(ConstantInt.of(-1), 3)
        .add(ConstantInt.of(0),  3)
        .add(ConstantInt.of(1),  1)
    .build());

    public static final MapCodec<RURockFeature> CODEC = RockFeatureConfig.CODEC.fieldOf("config")
        .xmap(RURockFeature::new, RURockFeature::config);

    private final RockFeatureConfig config;

    public RURockFeature(RockFeatureConfig config) {
        this.config = config;
    }

    public RockFeatureConfig config() {
        return this.config;
    }

    @Override
    public MapCodec<RURockFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator generator, RandomSource random, BlockPos origin) {
        RockFeatureConfig config = this.config;

        for (int c = 0; c < config.blobCount().sample(random); ++c) {
            int xr = Math.min(random.nextInt(3), 1);
            int yr = Math.min(random.nextInt(3), 1);
            int zr = Math.min(random.nextInt(3), 1);
            float tr = (float)(xr + yr + zr) * 0.333f + 0.5f;
            for (BlockPos blockPos : BlockPos.betweenClosed(origin.offset(-xr, -yr, -zr), origin.offset(xr, yr, zr))) {
                if (!(blockPos.distSqr(origin) <= (double)(tr * tr))) continue;
                level.setBlock(blockPos, config.stateProvider().getState(level, random, origin), 3);
            }
            origin = origin.offset(
                config.blobOffsetXZ().sample(random),
                BLOB_Y_OFFSET.sample(random),
                config.blobOffsetXZ().sample(random)
            );
        }
        return true;
    }
}

