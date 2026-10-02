package net.regions_unexplored.worldgen.feature.config;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomBlockProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.regions_unexplored.worldgen.treedecorator.AttachedToLogsDecorator;

import java.util.List;

public record FallenTreeConfig(BlockStateProvider trunkProvider, IntProvider logLength, List<TreeDecorator> stumpDecorators, List<TreeDecorator> logDecorators) {
	public static final List<TreeDecorator> DEFAULT_LOG_DECORATORS = List.of(
		new AttachedToLogsDecorator(
			0.2f,
			BlockStateProvider.of(Blocks.MOSS_CARPET),
			List.of(Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST),
			true
		),
		new AttachedToLogsDecorator(
			0.05f,
			new RandomBlockProvider(HolderSet.direct(Blocks.BROWN_MUSHROOM.builtInRegistryHolder(), Blocks.RED_MUSHROOM.builtInRegistryHolder())),
			List.of(Direction.UP),
			false
		)
	);
	
	public static final Codec<FallenTreeConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
		BlockStateProvider.CODEC.xmap(Holder::value, Holder::direct).fieldOf("trunk_provider").forGetter(FallenTreeConfig::trunkProvider),
		IntProviders.codec(0, 16).fieldOf("log_length").forGetter(FallenTreeConfig::logLength),
		TreeDecorator.CODEC.listOf().fieldOf("stump_decorators").forGetter(FallenTreeConfig::stumpDecorators),
		TreeDecorator.CODEC.listOf().fieldOf("log_decorators").forGetter(FallenTreeConfig::logDecorators)
	).apply(i, FallenTreeConfig::new));
	
	public static FallenTreeConfig of(BlockState trunk, int minLength, int maxLength) {
		return new FallenTreeConfig(BlockStateProvider.of(trunk), UniformInt.of(minLength, maxLength), List.of(), DEFAULT_LOG_DECORATORS);
	}

	public static FallenTreeConfig of(BlockState trunk, int minLength, int maxLength, List<TreeDecorator> logDecorators) {
		return new FallenTreeConfig(BlockStateProvider.of(trunk), UniformInt.of(minLength, maxLength), List.of(), logDecorators);
	}
}
