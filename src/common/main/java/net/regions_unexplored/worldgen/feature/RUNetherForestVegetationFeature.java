package net.regions_unexplored.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record RUNetherForestVegetationFeature(Holder<BlockStateProvider> stateProvider, int spreadWidth, int spreadHeight) implements Feature {
    public static final MapCodec<RUNetherForestVegetationFeature> CODEC = RecordCodecBuilder.mapCodec(
        i -> i.group(
                BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(RUNetherForestVegetationFeature::stateProvider),
                Codec.INT.fieldOf("spread_width").forGetter(RUNetherForestVegetationFeature::spreadWidth),
                Codec.INT.fieldOf("spread_height").forGetter(RUNetherForestVegetationFeature::spreadHeight)
            )
            .apply(i, RUNetherForestVegetationFeature::new)
    );

    public RUNetherForestVegetationFeature(BlockStateProvider stateProvider, int spreadWidth, int spreadHeight) {
        this(Holder.direct(stateProvider), spreadWidth, spreadHeight);
    }

    @Override
    public MapCodec<RUNetherForestVegetationFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        if (!level.getBlockState(origin.below()).is(BlockTags.NYLIUM)) {
            return false;
        }

        int y = origin.getY();
        if (y < level.getMinY() + 1 || y + 1 > level.getMaxY()) {
            return false;
        }

        boolean placedAny = false;
        for (int i = 0; i < spreadWidth * spreadWidth; i++) {
            BlockPos pos = origin.offset(
                random.nextInt(spreadWidth) - random.nextInt(spreadWidth),
                random.nextInt(spreadHeight) - random.nextInt(spreadHeight),
                random.nextInt(spreadWidth) - random.nextInt(spreadWidth)
            );
            BlockState state = stateProvider.value().getState(level, random, pos);
            if (level.isEmptyBlock(pos) && pos.getY() > level.getMinY() && state.canSurvive(level, pos)) {
                level.setBlock(pos, state, 2);
                placedAny = true;
            }
        }

        return placedAny;
    }
}
