package net.regions_unexplored.item;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Compostable;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;
import net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat;
import net.regions_unexplored.RegionsUnexplored;
import net.regions_unexplored.block.compat.CompostableBlocks;
import net.regions_unexplored.block.compat.FurnaceBurnTimes;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class RUItemComponents {
    private RUItemComponents() {}

    public static ResourceKey<ContextIntProvider> compostingChance(float chance) {
        return RegionsUnexplored.key(Registries.CONTEXT_INT_PROVIDER, "composting/chance_" + Math.round(chance * 100));
    }

    public static void modify(BiConsumer<Item, Consumer<DataComponentMap.Builder>> modifier) {
        CompostableBlocks.COMPOSTABLES.forEach((item, chance) -> modifier.accept(item.asItem(),
            builder -> builder.set(DataComponents.COMPOSTABLE, new Compostable(compostingChance(chance)))));
        FurnaceBurnTimes.BURN_TIME_300.forEach(item -> fuel(modifier, item, 300));
        FurnaceBurnTimes.BURN_TIME_200.forEach(item -> fuel(modifier, item, 200));
        FurnaceBurnTimes.BURN_TIME_150.forEach(item -> fuel(modifier, item, 150));
        FurnaceBurnTimes.BURN_TIME_100.forEach(item -> fuel(modifier, item, 100));
    }

    private static void fuel(BiConsumer<Item, Consumer<DataComponentMap.Builder>> modifier, Item item, int ticks) {
        modifier.accept(item, builder -> builder.set(DataComponents.COOKING_FUEL,
            new CookingFuel(new ResolvableInt.Constant(ticks), new ResolvableFloat.Constant(1))));
    }
}
