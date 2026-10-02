package net.regions_unexplored.util;

import net.minecraft.core.Direction;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.CopyPropertiesProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BlockCompatUtil {
    private static final Map<Block, Block> STRIPPABLES = new LinkedHashMap<>();
    private static final Map<Block, BlockState> SHOVELLED = new LinkedHashMap<>();
    private static BlockTransformer axe;
    private static BlockTransformer shovel;

    public static void registerStrippableBlock(Block log, Block strippedLog) {
        STRIPPABLES.put(log, strippedLog);
        axe = null;
    }

    public static void registerShovelled(Block block, BlockState shovelledBlock) {
        SHOVELLED.put(block, shovelledBlock);
        shovel = null;
    }


    public static BlockTransformer axeTransformer() {
        if (axe == null) {
            var rules = RuleBasedStateProvider.builder();
            STRIPPABLES.forEach((block, stripped) -> rules.ifTrueThenProvide(BlockPredicate.matchesBlocks(block), new CopyPropertiesProvider(stripped)));
            axe = new BlockTransformer(List.of(BlockTransformer.BlockTransformData.builder(rules.build()).sound(SoundEvents.AXE_STRIP).build()));
        }
        return axe;
    }

    public static BlockTransformer shovelTransformer() {
        if (shovel == null) {
            var rules = RuleBasedStateProvider.builder();
            SHOVELLED.forEach((block, state) -> rules.ifTrueThenProvide(BlockPredicate.allOf(
                BlockPredicate.matchesBlocks(block), BlockPredicate.not(BlockPredicate.solid(Direction.UP))), state));
            shovel = new BlockTransformer(List.of(BlockTransformer.BlockTransformData.builder(rules.build())
                .sound(SoundEvents.SHOVEL_FLATTEN).disallowedFaces(List.of(Direction.DOWN)).build()));
        }
        return shovel;
    }

    public static void registerFlammableBlock(@Nullable Block block, int spreadSpeed, int flammability) {
        if (block == null) return;
        FireBlock fireblock = (FireBlock) Blocks.FIRE;
        fireblock.setFlammable(block, spreadSpeed, flammability);
    }
}
