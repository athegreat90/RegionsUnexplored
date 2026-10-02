package net.regions_unexplored.worldgen.stateprovider;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class KeyHackStateProvider implements BlockStateProvider {
	public static final MapCodec<KeyHackStateProvider> CODEC = ResourceKey.codec(Registries.BLOCK)
		.fieldOf("block").xmap(KeyHackStateProvider::new, provider -> provider.block);
	private final ResourceKey<Block> block;

	public KeyHackStateProvider(ResourceKey<Block> block) {
		this.block = block;
	}

	@Override
	public MapCodec<KeyHackStateProvider> codec() {
		return CODEC;
	}

	@Override
	public BlockState getState(LevelAccessor level, RandomSource random, BlockPos pos) {
		return BuiltInRegistries.BLOCK.getValue(this.block).defaultBlockState();
	}
}
