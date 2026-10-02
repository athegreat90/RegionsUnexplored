package net.regions_unexplored.world.level.feature;

import net.minecraft.core.Holder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.regions_unexplored.registry.RUBlocks;
import net.regions_unexplored.registry.tag.*;
import net.regions_unexplored.block.type.aquatic.TallHyacinthStockBlock;
import net.regions_unexplored.block.properties.type.TallHyacinthStockShape;
import net.regions_unexplored.world.level.feature.configuration.HyacinthStockConfiguration;

import java.util.Random;

public class HyacinthStockFeature implements Feature {
    public static final MapCodec<HyacinthStockFeature> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        BlockStateProvider.CODEC.xmap(Holder::value, Holder::direct).fieldOf("stock_provider").forGetter(f -> f.stockProvider),
        Codec.INT.fieldOf("minimum_size").forGetter(f -> f.minimumSize),
        Codec.INT.fieldOf("size_variation").forGetter(f -> f.sizeVariation)
    ).apply(i, HyacinthStockFeature::new));

    private final BlockStateProvider stockProvider;
    private final int minimumSize;
    private final int sizeVariation;

    public HyacinthStockFeature(BlockStateProvider stockProvider, int minimumSize, int sizeVariation) {
        this.stockProvider = stockProvider;
        this.minimumSize = minimumSize;
        this.sizeVariation = sizeVariation;
    }

    @Override
    public MapCodec<HyacinthStockFeature> codec() {
        return CODEC;
    }

    public boolean place(WorldGenLevel level, ChunkGenerator generator, RandomSource randomSource, BlockPos pos) {
        HyacinthStockConfiguration stockConfiguration = new HyacinthStockConfiguration(stockProvider, minimumSize, sizeVariation);
        int height_main = randomSource.nextInt(stockConfiguration.sizeVariation) + stockConfiguration.minimumSize;

        if(!level.getBlockState(pos.below()).isFaceSturdy(level, pos, Direction.UP)){
           return false;
        }

        int placeCheck = 0;
        BlockPos.MutableBlockPos placePos = pos.mutable();
        while (placeCheck <= height_main) {
            if(level.getBlockState(placePos).is(Blocks.WATER)){
                placePlant(level, placePos, randomSource, stockConfiguration);
            }
            else{
                break;
            }
            placePos.move(Direction.UP);
            placeCheck = placeCheck + 1;
        }
        return true;
    }

    public boolean placePlant(WorldGenLevel level, BlockPos pos, RandomSource randomSource, HyacinthStockConfiguration stockConfiguration) {
        Random random = new Random();
        if(level.isOutsideBuildHeight(pos)){
            return true;
        }
        if(!level.getBlockState(pos.below()).is(RUBlocks.TALL_HYACINTH_STOCK.get())){
            level.setBlock(pos, stockConfiguration.stockProvider.getState(level, randomSource, pos), 2);
        }
        if(level.getBlockState(pos.below())== RUBlocks.TALL_HYACINTH_STOCK.get().defaultBlockState()){
            level.setBlock(pos, stockConfiguration.stockProvider.getState(level, randomSource, pos).setValue(TallHyacinthStockBlock.TALL_HYACINTH_STOCK_SHAPE, TallHyacinthStockShape.TIP), 2);
            level.setBlock(pos.below(), stockConfiguration.stockProvider.getState(level, randomSource, pos.below()).setValue(TallHyacinthStockBlock.TALL_HYACINTH_STOCK_SHAPE, TallHyacinthStockShape.BASE_FRUSTUM), 2);
        }
        if(level.getBlockState(pos.below().below())== RUBlocks.TALL_HYACINTH_STOCK.get().defaultBlockState().setValue(TallHyacinthStockBlock.TALL_HYACINTH_STOCK_SHAPE, TallHyacinthStockShape.BASE_FRUSTUM)){
            level.setBlock(pos, stockConfiguration.stockProvider.getState(level, randomSource, pos).setValue(TallHyacinthStockBlock.TALL_HYACINTH_STOCK_SHAPE, TallHyacinthStockShape.TIP), 2);
            level.setBlock(pos.below(), stockConfiguration.stockProvider.getState(level, randomSource, pos.below()).setValue(TallHyacinthStockBlock.TALL_HYACINTH_STOCK_SHAPE, TallHyacinthStockShape.FRUSTUM), 2);
            level.setBlock(pos.below().below(), stockConfiguration.stockProvider.getState(level, randomSource, pos.below().below()).setValue(TallHyacinthStockBlock.TALL_HYACINTH_STOCK_SHAPE, TallHyacinthStockShape.BASE), 2);
        }
        if(level.getBlockState(pos.below().below())== RUBlocks.TALL_HYACINTH_STOCK.get().defaultBlockState().setValue(TallHyacinthStockBlock.TALL_HYACINTH_STOCK_SHAPE, TallHyacinthStockShape.FRUSTUM)){
            level.setBlock(pos, stockConfiguration.stockProvider.getState(level, randomSource, pos).setValue(TallHyacinthStockBlock.TALL_HYACINTH_STOCK_SHAPE, TallHyacinthStockShape.TIP), 2);
            level.setBlock(pos.below(), stockConfiguration.stockProvider.getState(level, randomSource, pos.below()).setValue(TallHyacinthStockBlock.TALL_HYACINTH_STOCK_SHAPE, TallHyacinthStockShape.FRUSTUM), 2);
            level.setBlock(pos.below().below(), stockConfiguration.stockProvider.getState(level, randomSource, pos.below().below()).setValue(TallHyacinthStockBlock.TALL_HYACINTH_STOCK_SHAPE, TallHyacinthStockShape.MIDDLE), 2);
        }
        return true;
    }
    
    public static boolean isReplaceableDirtBlock(BlockState state) {
        return state.is(RUBlockTags.TREE_GRASS_REPLACEABLES);
    }


    public static boolean isReplaceableDirt(LevelSimulatedReader reader, BlockPos pos) {
        return reader.isStateAtPosition(pos, HyacinthStockFeature::isReplaceableDirtBlock);
    }

    public static boolean isReplaceableBlock(BlockState state) {
        return state.is(RUBlockTags.REPLACEABLE_BLOCKS);
    }

    public static boolean isReplaceable(LevelSimulatedReader reader, BlockPos pos) {
        return reader.isStateAtPosition(pos, HyacinthStockFeature::isReplaceableBlock);
    }
}