package net.regions_unexplored.world.level.feature.tree;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.regions_unexplored.world.level.feature.configuration.RUTreeConfiguration;

import java.util.List;

public abstract class RUTreeFeature implements Feature {
    protected final BlockStateProvider trunkProvider;
    protected final BlockStateProvider foliageProvider;
    protected final BlockStateProvider branchProvider;
    protected final List<TreeDecorator> decorators;
    protected final int minimumSize;
    protected final int sizeVariation;

    protected RUTreeFeature(BlockStateProvider trunkProvider, BlockStateProvider foliageProvider, BlockStateProvider branchProvider, List<TreeDecorator> decorators, int minimumSize, int sizeVariation) {
        this.trunkProvider = trunkProvider;
        this.foliageProvider = foliageProvider;
        this.branchProvider = branchProvider;
        this.decorators = decorators;
        this.minimumSize = minimumSize;
        this.sizeVariation = sizeVariation;
    }

    public RUTreeConfiguration treeConfiguration() {
        return new RUTreeConfiguration(trunkProvider, foliageProvider, branchProvider, decorators, minimumSize, sizeVariation);
    }

    protected static <F extends RUTreeFeature> MapCodec<F> treeCodec(TreeFeatureFactory<F> factory) {
        return RecordCodecBuilder.mapCodec(i -> i.group(
            BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(f -> f.trunkProvider),
            BlockStateProvider.CODEC.fieldOf("foliage_provider").forGetter(f -> f.foliageProvider),
            BlockStateProvider.CODEC.fieldOf("branch_provider").forGetter(f -> f.branchProvider),
            TreeDecorator.CODEC.listOf().optionalFieldOf("decorators", List.of()).forGetter(f -> f.decorators),
            ExtraCodecs.POSITIVE_INT.fieldOf("minimum_size").forGetter(f -> f.minimumSize),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("size_variation").forGetter(f -> f.sizeVariation)
        ).apply(i, factory::create));
    }

    public interface TreeFeatureFactory<F extends RUTreeFeature> {
        F create(BlockStateProvider trunk, BlockStateProvider foliage, BlockStateProvider branch, List<TreeDecorator> decorators, int minimumSize, int sizeVariation);
    }
}
