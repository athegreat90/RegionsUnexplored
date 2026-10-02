package net.regions_unexplored.mixin;

import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(NoiseGeneratorSettings.class)
public interface NoiseGeneratorSettingsAccessor {
    @Mutable
    @Accessor("aquifers")
    void regionsUnexplored$setAquifers(Optional<Aquifer.Config> aquifers);
}
