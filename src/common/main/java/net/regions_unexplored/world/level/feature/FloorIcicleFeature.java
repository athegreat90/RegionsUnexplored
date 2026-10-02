package net.regions_unexplored.world.level.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.properties.SpeleothemThickness;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.regions_unexplored.block.type.base.SpeleothemBlock;
import net.regions_unexplored.registry.RUBlocks;

public class FloorIcicleFeature implements Feature {
    public static final MapCodec<FloorIcicleFeature> CODEC = MapCodec.unit(FloorIcicleFeature::new);

    @Override
    public MapCodec<FloorIcicleFeature> codec() {
        return CODEC;
    }

    public boolean place(WorldGenLevel level, ChunkGenerator generator, RandomSource randomSource, BlockPos pos) {
        BlockPos.MutableBlockPos checkPos = pos.mutable();
        BlockPos.MutableBlockPos placePos = pos.mutable();
        int size = randomSource.nextInt(4)+2;


        for(int i=0; i<size; i++){
            if(!level.getBlockState(checkPos).canBeReplaced()){
                return false;
            }
        }
        for(int i=0; i<size; i++){
            if(i==0){
                level.setBlock(placePos, RUBlocks.ICICLE.get().defaultBlockState().setValue(SpeleothemBlock.TIP_DIRECTION, Direction.UP).setValue(SpeleothemBlock.THICKNESS, SpeleothemThickness.TIP), 2);
            }
            else if(i==1){
                level.setBlock(placePos.below(), RUBlocks.ICICLE.get().defaultBlockState().setValue(SpeleothemBlock.TIP_DIRECTION, Direction.UP).setValue(SpeleothemBlock.THICKNESS, SpeleothemThickness.FRUSTUM), 2);
                level.setBlock(placePos, RUBlocks.ICICLE.get().defaultBlockState().setValue(SpeleothemBlock.TIP_DIRECTION, Direction.UP).setValue(SpeleothemBlock.THICKNESS, SpeleothemThickness.TIP), 2);
            }
            else if(i==2){
                level.setBlock(placePos.below(2), RUBlocks.ICICLE.get().defaultBlockState().setValue(SpeleothemBlock.TIP_DIRECTION, Direction.UP).setValue(SpeleothemBlock.THICKNESS, SpeleothemThickness.BASE), 2);
                level.setBlock(placePos.below(), RUBlocks.ICICLE.get().defaultBlockState().setValue(SpeleothemBlock.TIP_DIRECTION, Direction.UP).setValue(SpeleothemBlock.THICKNESS, SpeleothemThickness.FRUSTUM), 2);
                level.setBlock(placePos, RUBlocks.ICICLE.get().defaultBlockState().setValue(SpeleothemBlock.TIP_DIRECTION, Direction.UP).setValue(SpeleothemBlock.THICKNESS, SpeleothemThickness.TIP), 2);
            }
            else {
                level.setBlock(placePos.below(2), RUBlocks.ICICLE.get().defaultBlockState().setValue(SpeleothemBlock.TIP_DIRECTION, Direction.UP).setValue(SpeleothemBlock.THICKNESS, SpeleothemThickness.MIDDLE), 2);
                level.setBlock(placePos.below(), RUBlocks.ICICLE.get().defaultBlockState().setValue(SpeleothemBlock.TIP_DIRECTION, Direction.UP).setValue(SpeleothemBlock.THICKNESS, SpeleothemThickness.FRUSTUM), 2);
                level.setBlock(placePos, RUBlocks.ICICLE.get().defaultBlockState().setValue(SpeleothemBlock.TIP_DIRECTION, Direction.UP).setValue(SpeleothemBlock.THICKNESS, SpeleothemThickness.TIP), 2);
            }
            placePos.move(Direction.UP);
        }
        return true;
    }
}