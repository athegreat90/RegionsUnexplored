package net.regions_unexplored.datagen.provider;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.regions_unexplored.datagen.provider.loot.RUBlockLootProvider;

import java.util.List;
import java.util.Set;

public class RULootTableProvider extends LootTableProvider {
    public RULootTableProvider() {
        super(Set.of(), List.of(new SubProviderEntry(RUBlockLootProvider::new, LootContextParamSets.BLOCK)));
    }
}
