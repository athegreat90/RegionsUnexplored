package net.regions_unexplored.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.regions_unexplored.module.platform.Registrar;
import net.regions_unexplored.worldgen.stateprovider.RandomizedGroundCoverStateProvider;
import net.regions_unexplored.worldgen.stateprovider.KeyHackStateProvider;

import java.util.function.Supplier;

public interface RUBlockStateProviderTypes {
    Supplier<MapCodec<RandomizedGroundCoverStateProvider>> RANDOMIZED_GROUND_COVER = register("randomized_ground_cover", RandomizedGroundCoverStateProvider.CODEC);
    Supplier<MapCodec<KeyHackStateProvider>> KEY_HACK = register("key_hack", KeyHackStateProvider.CODEC);

    static <T extends BlockStateProvider> Supplier<MapCodec<T>> register(String name, MapCodec<T> codec) {
        Registrar.register(BuiltInRegistries.BLOCK_STATE_PROVIDER_TYPE, name, () -> codec);
        return () -> codec;
    }

    static void init() {
    }
}
