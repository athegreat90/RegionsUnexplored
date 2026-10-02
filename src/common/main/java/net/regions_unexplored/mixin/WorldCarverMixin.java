package net.regions_unexplored.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.regions_unexplored.registry.RUBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// TODO 26.3: WorldCarver.carveBlock no longer exists - WorldCarver is now an interface and the
// grass/mycelium-preservation logic that used to live there moved into
// NoiseBasedChunkGenerator.applyCarvingMask - but the actual BlockState.is(Block) calls run
// inside the lambda applyCarvingMask passes to CarvingMask.visit, which javac compiles to a
// separate synthetic static method (lambda$applyCarvingMask$0), not applyCarvingMask itself.
// Retargeted there; its 3 BlockState.is(Block) calls are ordinal 0 = GRASS_BLOCK, 1 = MYCELIUM,
// 2 = DIRT.
@Mixin(NoiseBasedChunkGenerator.class)
public abstract class WorldCarverMixin {
	@WrapOperation(
		method = "lambda$applyCarvingMask$0",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z",
			ordinal = 0
		)
	)
	private static boolean fixGrassQuery(BlockState state, Object block, Operation<Boolean> operation) {
		return operation.call(state, block) || operation.call(state, RUBlocks.PEAT_GRASS_BLOCK.get()) || operation.call(state, RUBlocks.SILT_GRASS_BLOCK.get());
	}

	@WrapOperation(
		method = "lambda$applyCarvingMask$0",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z",
			ordinal = 2
		)
	)
	private static boolean fixDirtQuery(BlockState state, Object block, Operation<Boolean> operation) {
		return operation.call(state, block) || operation.call(state, RUBlocks.PEAT_DIRT.get()) || operation.call(state, RUBlocks.SILT_DIRT.get());
	}
}
