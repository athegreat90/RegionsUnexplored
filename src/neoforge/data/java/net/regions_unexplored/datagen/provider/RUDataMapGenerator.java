package net.regions_unexplored.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class RUDataMapGenerator extends DataMapProvider {
    // TODO 26.3: NeoForge's Compostable data map (net.neoforged.neoforge.registries.datamaps.builtin.Compostable)
    // was removed - compostability is now the vanilla Compostable data component
    // (net.minecraft.world.item.component.Compostable), set via
    // Item.Properties.component(DataComponents.COMPOSTABLE, ...) at item-build time rather than
    // generated as data. See CompostableBlocksFabric for the matching Fabric-side stub.
    public RUDataMapGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
    }
}
