package net.regions_unexplored.worldgen.stateprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.regions_unexplored.block.type.base.BonemealableSegmentedBlock;

public class RandomizedGroundCoverStateProvider implements BlockStateProvider {
	public static final MapCodec<RandomizedGroundCoverStateProvider> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
		BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(RandomizedGroundCoverStateProvider::block)
	).apply(i, RandomizedGroundCoverStateProvider::new));

	private final Block block;

	public RandomizedGroundCoverStateProvider(Block block) {
		BlockState state = block.defaultBlockState();
		if (!(state.hasProperty(BonemealableSegmentedBlock.FACING) && state.hasProperty(BonemealableSegmentedBlock.AMOUNT))) {
			throw new IllegalStateException("randomized_ground_cover state provider requires a block with FACING and AMOUNT properties");
		}
		this.block = block;
	}

	private Block block() {
		return this.block;
	}

	@Override
	public MapCodec<RandomizedGroundCoverStateProvider> codec() {
		return CODEC;
	}

	@Override
	public BlockState getState(LevelAccessor level, RandomSource random, BlockPos pos) {
		return this.block.defaultBlockState()
			.setValue(BonemealableSegmentedBlock.AMOUNT, random.nextIntBetweenInclusive(1, 4))
			.setValue(BonemealableSegmentedBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random));
	}
}
