package net.regions_unexplored.block.type.nether.plant;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.regions_unexplored.block.RUBlockUtils;
import net.regions_unexplored.registry.tag.*;

public class BrimsproutBlock extends VegetationBlock {
    protected static final VoxelShape SHAPE = RUBlockUtils.column(12, 0, 13);

    public BrimsproutBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
        return state.is(RUBlockTags.SUPPORTS_INFERNAL_PLANT);
    }
}
