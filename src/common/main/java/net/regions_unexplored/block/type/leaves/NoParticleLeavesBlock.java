package net.regions_unexplored.block.type.leaves;

import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.sounds.AmbientLeavesBlockSoundPlayer;

public class NoParticleLeavesBlock extends LeavesBlock {
	public NoParticleLeavesBlock(Properties properties) {
		super(AmbientLeavesBlockSoundPlayer.noAmbientSound(), properties);
	}
}
