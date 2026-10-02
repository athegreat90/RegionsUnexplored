package net.regions_unexplored.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.regions_unexplored.registry.data.RUBlockIds;
import net.regions_unexplored.registry.tag.RUBlockTags;
import net.regions_unexplored.worldgen.stateprovider.KeyHackStateProvider;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(TreeFeature.class)
public abstract class TreeConfigurationMixin {
    @Shadow @Mutable @Final
    public Holder<BlockStateProvider> belowTrunkProvider;

    @Inject(
        method = "<init>",
        at = @At("RETURN")
    )
    private void addRUDirt(Holder<BlockStateProvider> trunkProvider, TrunkPlacer trunkPlacer, Holder<BlockStateProvider> foliageProvider, FoliagePlacer foliagePlacer, Optional<RootPlacer> rootPlacer, FeatureSize minimumSize, List<TreeDecorator> decorators, boolean ignoreVines, Holder<BlockStateProvider> belowTrunkProvider, CallbackInfo ci) {
        // Registry holders may still be unbound while tree features are decoded.
        // Keep the fallback as a holder and resolve it only when the tree is placed.
        this.belowTrunkProvider = Holder.direct(new RuleBasedStateProvider(belowTrunkProvider, List.of(
            new RuleBasedStateProvider.Rule(BlockPredicate.matchesTag(RUBlockTags.PEAT_SUBSTRATE), Holder.direct(new KeyHackStateProvider(RUBlockIds.PEAT_DIRT))),
            new RuleBasedStateProvider.Rule(BlockPredicate.matchesTag(RUBlockTags.SILT_SUBSTRATE), Holder.direct(new KeyHackStateProvider(RUBlockIds.SILT_DIRT)))
        )));
    }
}
