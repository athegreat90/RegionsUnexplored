package net.regions_unexplored.world.level.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.regions_unexplored.registry.RUBlocks;

public class HyacinthPlantsFeature implements Feature {
   public static final MapCodec<HyacinthPlantsFeature> CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability")
       .xmap(HyacinthPlantsFeature::new, f -> f.probability);

   private final float probability;

   public HyacinthPlantsFeature(float probability) {
      this.probability = probability;
   }

   @Override
   public MapCodec<HyacinthPlantsFeature> codec() {
      return CODEC;
   }

   public boolean place(WorldGenLevel worldgenlevel, ChunkGenerator generator, RandomSource randomsource, BlockPos blockpos) {
      BlockState blockstate = Blocks.SEAGRASS.defaultBlockState();
      int i = randomsource.nextInt(8) - randomsource.nextInt(8);
      int j = randomsource.nextInt(8) - randomsource.nextInt(8);
      int k = worldgenlevel.getHeight(Heightmap.Types.OCEAN_FLOOR, blockpos.getX() + i, blockpos.getZ() + j);
      BlockPos blockpos1 = new BlockPos(blockpos.getX() + i, k, blockpos.getZ() + j);
      if (worldgenlevel.getBlockState(blockpos1).is(Blocks.WATER)) {
         if (blockstate.canSurvive(worldgenlevel, blockpos1)) {
            if(randomsource.nextInt(7)==0){
               blockstate = RUBlocks.HYACINTH_BLOOM.get().defaultBlockState();
            }
            else{
               blockstate = Blocks.SEAGRASS.defaultBlockState();
            }
               worldgenlevel.setBlock(blockpos1, blockstate, 2);
         }
      }
      return true;
   }
}