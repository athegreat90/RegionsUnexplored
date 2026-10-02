package net.regions_unexplored.datagen.provider.registry;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import static net.regions_unexplored.registry.data.RUNoises.*;

public class RUNoiseBootstrap {
    public static void bootstrap(BootstrapContext<NormalNoise> context) {
        register(context, WEIGHTED, 0, 1);
        register(context, SHIELD, -5, 1, 1, 1);
        register(context, SURFACE_MEDIUM, -6, 1, 2, 1.5, 1.0);
        register(context, TREE_DENSITY, -7, 2.5);
        register(context, FLOWER_DENSITY, -6, 1.75);
    }

    private static void register(BootstrapContext<NormalNoise> context, ResourceKey<NormalNoise> key, int i, double v, double... doubles) {
        double[] amplitudes = new double[doubles.length + 1];
        amplitudes[0] = v;
        System.arraycopy(doubles, 0, amplitudes, 1, doubles.length);
        context.register(key, NormalNoise.createParity(i, amplitudes));
    }
}
