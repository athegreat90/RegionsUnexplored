package net.regions_unexplored.world.level.feature.bioshroom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.regions_unexplored.world.level.feature.configuration.GiantBioshroomConfiguration;

public abstract class GiantBioshroomFeature implements Feature {
    protected final BlockStateProvider stemProvider;
    protected final BlockStateProvider capProvider;
    protected final BlockStateProvider glowBlockProvider;
    protected final int minimumSize;
    protected final int sizeVariation;

    protected GiantBioshroomFeature(BlockStateProvider stemProvider, BlockStateProvider capProvider, BlockStateProvider glowBlockProvider, int minimumSize, int sizeVariation) {
        this.stemProvider = stemProvider;
        this.capProvider = capProvider;
        this.glowBlockProvider = glowBlockProvider;
        this.minimumSize = minimumSize;
        this.sizeVariation = sizeVariation;
    }

    public GiantBioshroomConfiguration bioshroomConfiguration() {
        return new GiantBioshroomConfiguration(stemProvider, capProvider, glowBlockProvider, minimumSize, sizeVariation);
    }

    protected static <F extends GiantBioshroomFeature> MapCodec<F> bioshroomCodec(BioshroomFeatureFactory<F> factory) {
        return RecordCodecBuilder.mapCodec(i -> i.group(
            BlockStateProvider.CODEC.fieldOf("stem_provider").forGetter(f -> f.stemProvider),
            BlockStateProvider.CODEC.fieldOf("cap_provider").forGetter(f -> f.capProvider),
            BlockStateProvider.CODEC.fieldOf("glow_block_provider").forGetter(f -> f.glowBlockProvider),
            Codec.INT.fieldOf("minimum_size").forGetter(f -> f.minimumSize),
            Codec.INT.fieldOf("size_variation").forGetter(f -> f.sizeVariation)
        ).apply(i, factory::create));
    }

    public interface BioshroomFeatureFactory<F extends GiantBioshroomFeature> {
        F create(BlockStateProvider stem, BlockStateProvider cap, BlockStateProvider glow, int minimumSize, int sizeVariation);
    }
}
