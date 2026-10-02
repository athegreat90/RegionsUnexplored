package net.regions_unexplored.block.type.grass;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.regions_unexplored.block.RUBlockUtils;
import net.regions_unexplored.registry.tag.RUBlockTags;

public class NetherPlantBlock extends VegetationBlock {
    private final float height;
    private final VoxelShape shape;

    public NetherPlantBlock(float height, Properties properties) {
        super(properties);
        this.height = height;
        this.shape = RUBlockUtils.column(12, 0, height);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.shape.move(state.getOffset(pos));
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
        return state.is(RUBlockTags.SUPPORTS_NETHER_PLANTS);
    }
}
