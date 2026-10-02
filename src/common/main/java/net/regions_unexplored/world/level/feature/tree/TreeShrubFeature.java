package net.regions_unexplored.world.level.feature.tree;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.regions_unexplored.registry.RUBlocks;
import net.regions_unexplored.registry.tag.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.regions_unexplored.world.level.feature.configuration.RUTreeConfiguration;

import java.util.List;
import java.util.Random;

public class TreeShrubFeature extends RUTreeFeature {
    public static final MapCodec<TreeShrubFeature> CODEC = treeCodec(TreeShrubFeature::new);

    public TreeShrubFeature(BlockStateProvider trunkProvider, BlockStateProvider foliageProvider, BlockStateProvider branchProvider, List<TreeDecorator> decorators, int minimumSize, int sizeVariation) {
        super(trunkProvider, foliageProvider, branchProvider, decorators, minimumSize, sizeVariation);
    }

    @Override
    public MapCodec<TreeShrubFeature> codec() {
        return CODEC;
    }

    public boolean place(WorldGenLevel level, ChunkGenerator generator, RandomSource randomSource, BlockPos pos) {
        RUTreeConfiguration treeConfiguration = treeConfiguration();
        int height_main;
        if(treeConfiguration.sizeVariation() <=0){
            height_main = treeConfiguration.minimumSize();
        }
        else{
            height_main = randomSource.nextInt(treeConfiguration.sizeVariation()) + treeConfiguration.minimumSize();
        }

        int check = 0;
        BlockPos.MutableBlockPos checkPos = pos.mutable();
        while (check <= height_main) {
            if(!checkReplaceable(level, checkPos)){
                return false;
            }
            else{
                checkPos.move(Direction.UP);
                check = check + 1;
            }
        }
        int placeCheck = 0;
        BlockPos.MutableBlockPos placePos = pos.mutable();
        while (placeCheck <= height_main) {
            placeLog(level, placePos, randomSource, treeConfiguration);
            if(placeCheck == 0){
                placeRoot(level, placePos, randomSource, treeConfiguration);
            }
            if(placeCheck == height_main){
                placeLeavesBlob(level, placePos, randomSource, treeConfiguration);
            }
            placePos.move(Direction.UP);
            placeCheck = placeCheck + 1;
        }
        return true;
    }

    public boolean placeLog(WorldGenLevel level, BlockPos pos, RandomSource randomSource, RUTreeConfiguration treeConfiguration) {
        Random random = new Random();
        if(level.isOutsideBuildHeight(pos)){
            return true;
        }
        if(level.getBlockState(pos).is(RUBlocks.PEAT_GRASS_BLOCK.get())){
            level.setBlock(pos, RUBlocks.PEAT_DIRT.get().defaultBlockState(), 2);
        }
        else if(level.getBlockState(pos).is(RUBlocks.SILT_GRASS_BLOCK.get())){
            level.setBlock(pos, RUBlocks.SILT_DIRT.get().defaultBlockState(), 2);
        }
        else if(level.getBlockState(pos).is(RUBlocks.ALPHA_GRASS_BLOCK.get())){
            level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 2);
        }
        else if(level.getBlockState(pos).is(Blocks.GRASS_BLOCK)){
            level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 2);
        }
        else if(isReplaceable(level, pos)) {
            level.setBlock(pos, treeConfiguration.trunkProvider().getState(level, randomSource, pos), 2);
        }
        else{
            return true;
        }

        if(level.getBlockState(pos.below()).is(RUBlocks.PEAT_GRASS_BLOCK.get())){
            level.setBlock(pos.below(), RUBlocks.PEAT_DIRT.get().defaultBlockState(), 2);
        }
        else if(level.getBlockState(pos.below()).is(RUBlocks.SILT_GRASS_BLOCK.get())){
            level.setBlock(pos.below(), RUBlocks.SILT_DIRT.get().defaultBlockState(), 2);
        }
        else if(level.getBlockState(pos.below()).is(RUBlocks.ALPHA_GRASS_BLOCK.get())){
            level.setBlock(pos.below(), Blocks.DIRT.defaultBlockState(), 2);
        }
        else if(level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK)){
            level.setBlock(pos.below(), Blocks.DIRT.defaultBlockState(), 2);
        }
        return true;
    }

    public void placeRoot(WorldGenLevel level, BlockPos pos, RandomSource randomSource, RUTreeConfiguration treeConfiguration) {
        Random random = new Random();
        int rd = random.nextInt(2)+4;
        int i = 0;
        BlockPos.MutableBlockPos placePos = pos.mutable();
        while(i<=rd){
            if(level.getBlockState(placePos).canBeReplaced()&&level.getBlockState(placePos.above()).is(BlockTags.DIRT)){
                level.setBlock(placePos, Blocks.HANGING_ROOTS.defaultBlockState(), 2);
                break;
            }
            else if(level.getBlockState(placePos).is(BlockTags.DIRT)||level.getBlockState(placePos).is(BlockTags.REPLACEABLE_BY_TREES)||level.isEmptyBlock(placePos)){
                placeLog(level, placePos, randomSource, treeConfiguration);
            }
            else{
                break;
            }
            placePos.move(Direction.DOWN);
            i++;
        }
    }
    public void placeLeavesBlob(WorldGenLevel level, BlockPos pos, RandomSource randomSource, RUTreeConfiguration treeConfiguration) {
        placeLeavesBlock(level, pos, randomSource, treeConfiguration);
        placeLeavesBlock(level, pos.north(), randomSource, treeConfiguration);
        placeLeavesBlock(level, pos.south(), randomSource, treeConfiguration);
        placeLeavesBlock(level, pos.east(), randomSource, treeConfiguration);
        placeLeavesBlock(level, pos.west(), randomSource, treeConfiguration);
        placeLeavesBlock(level, pos.above(), randomSource, treeConfiguration);
    }

    public boolean placeLeavesBlock(WorldGenLevel level, BlockPos pos, RandomSource randomSource, RUTreeConfiguration treeConfiguration) {
        Random random = new Random();
        if(level.isOutsideBuildHeight(pos)){
            return true;
        }
        if(level.getBlockState(pos).canBeReplaced()) {
            level.setBlock(pos, treeConfiguration.foliageProvider().getState(level, randomSource, pos).setValue(LeavesBlock.DISTANCE, 1), 2);
        }
        return true;
    }

    public boolean checkReplaceable(WorldGenLevel level, BlockPos pos) {
        if(level.isOutsideBuildHeight(pos)){
            return false;
        }
        if(!isReplaceable(level, pos)) {
            return false;
        }
        return true;
    }
    
    public static boolean isReplaceableDirtBlock(BlockState state) {
        return state.is(RUBlockTags.TREE_GRASS_REPLACEABLES);
    }


    public static boolean isReplaceableDirt(LevelSimulatedReader reader, BlockPos pos) {
        return reader.isStateAtPosition(pos, TreeShrubFeature::isReplaceableDirtBlock);
    }

    public static boolean isReplaceableBlock(BlockState state) {
        return state.is(RUBlockTags.REPLACEABLE_BLOCKS);
    }

    public static boolean isReplaceable(LevelSimulatedReader reader, BlockPos pos) {
        return reader.isStateAtPosition(pos, TreeShrubFeature::isReplaceableBlock);
    }
}