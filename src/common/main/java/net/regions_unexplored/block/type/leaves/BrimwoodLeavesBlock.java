package net.regions_unexplored.block.type.leaves;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.sounds.AmbientLeavesBlockSoundPlayer;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.regions_unexplored.block.RUBlockUtils;

public class BrimwoodLeavesBlock extends LeavesBlock {
    public BrimwoodLeavesBlock(BlockBehaviour.Properties properties) {
        super(AmbientLeavesBlockSoundPlayer.noAmbientSound(), properties.mapColor(MapColor.COLOR_BROWN).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(BrimwoodLeavesBlock::ocelotOrParrot).isSuffocating(BrimwoodLeavesBlock::never).isViewBlocking(BrimwoodLeavesBlock::never).pushReaction(PushReaction.POPPED).isRedstoneConductor(RUBlockUtils::never));
    }

    private static boolean never(BlockState state, BlockGetter getter, BlockPos pos) {
        return false;
    }

    private static boolean never(BlockState state, BlockGetter getter, BlockPos pos, AABB box) {
        return false;
    }

    private static Boolean ocelotOrParrot(BlockState state, BlockGetter getter, BlockPos pos, EntityType<?> type) {
        return type == EntityTypes.OCELOT || type == EntityTypes.PARROT;
    }
}