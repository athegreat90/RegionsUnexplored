package net.regions_unexplored.world.level.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.regions_unexplored.world.level.feature.configuration.PointedRedstoneConfiguration;
import net.regions_unexplored.world.level.feature.configuration.PointedRedstoneUtils;

import java.util.Optional;

public class PointedRedstoneFeature implements Feature {
    public static final MapCodec<PointedRedstoneFeature> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_taller_redstone").orElse(0.2F).forGetter(f -> f.chanceOfTallerRedstone),
        Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_directional_spread").orElse(0.7F).forGetter(f -> f.chanceOfDirectionalSpread),
        Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spread_radius2").orElse(0.5F).forGetter(f -> f.chanceOfSpreadRadius2),
        Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spread_radius3").orElse(0.5F).forGetter(f -> f.chanceOfSpreadRadius3)
    ).apply(i, PointedRedstoneFeature::new));

    private final float chanceOfTallerRedstone;
    private final float chanceOfDirectionalSpread;
    private final float chanceOfSpreadRadius2;
    private final float chanceOfSpreadRadius3;

    public PointedRedstoneFeature(float chanceOfTallerRedstone, float chanceOfDirectionalSpread, float chanceOfSpreadRadius2, float chanceOfSpreadRadius3) {
        this.chanceOfTallerRedstone = chanceOfTallerRedstone;
        this.chanceOfDirectionalSpread = chanceOfDirectionalSpread;
        this.chanceOfSpreadRadius2 = chanceOfSpreadRadius2;
        this.chanceOfSpreadRadius3 = chanceOfSpreadRadius3;
    }

    @Override
    public MapCodec<PointedRedstoneFeature> codec() {
        return CODEC;
    }

    public boolean place(WorldGenLevel level, ChunkGenerator generator, RandomSource random, BlockPos pos) {
        PointedRedstoneConfiguration redstoneConfiguration = new PointedRedstoneConfiguration(chanceOfTallerRedstone, chanceOfDirectionalSpread, chanceOfSpreadRadius2, chanceOfSpreadRadius3);
        Optional<Direction> optional = getTipDirection(level, pos, random);
        if (optional.isEmpty()) {
            return false;
        } else {
            BlockPos blockpos1 = pos.relative(optional.get().getOpposite());
            createPatchOfRedstoneBlocks(level, random, blockpos1, redstoneConfiguration);
            int i = random.nextFloat() < redstoneConfiguration.chanceOfTallerRedstone && PointedRedstoneUtils.isEmptyOrWater(level.getBlockState(pos.relative(optional.get()))) ? 2 : 1;
            PointedRedstoneUtils.growPointedRedstone(level, pos, optional.get(), i, false);
            return true;
        }
    }

    private static Optional<Direction> getTipDirection(WorldGenLevel level, BlockPos pos, RandomSource random) {
        boolean flag = PointedRedstoneUtils.isRedstoneBase(level.getBlockState(pos.above()));
        boolean flag1 = PointedRedstoneUtils.isRedstoneBase(level.getBlockState(pos.below()));
        if (flag && flag1) {
            return Optional.of(random.nextBoolean() ? Direction.DOWN : Direction.UP);
        } else if (flag) {
            return Optional.of(Direction.DOWN);
        } else {
            return flag1 ? Optional.of(Direction.UP) : Optional.empty();
        }
    }

    private static void createPatchOfRedstoneBlocks(WorldGenLevel level, RandomSource random, BlockPos pos, PointedRedstoneConfiguration redstoneConfiguration) {
        PointedRedstoneUtils.placeRedstoneBlockIfPossible(level, pos);

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            if (!(random.nextFloat() > redstoneConfiguration.chanceOfDirectionalSpread)) {
                BlockPos blockpos = pos.relative(direction);
                PointedRedstoneUtils.placeRedstoneBlockIfPossible(level, blockpos);
                if (!(random.nextFloat() > redstoneConfiguration.chanceOfSpreadRadius2)) {
                    BlockPos blockpos1 = blockpos.relative(Direction.getRandom(random));
                    PointedRedstoneUtils.placeRedstoneBlockIfPossible(level, blockpos1);
                    if (!(random.nextFloat() > redstoneConfiguration.chanceOfSpreadRadius3)) {
                        BlockPos blockpos2 = blockpos1.relative(Direction.getRandom(random));
                        PointedRedstoneUtils.placeRedstoneBlockIfPossible(level, blockpos2);
                    }
                }
            }
        }

    }
}
