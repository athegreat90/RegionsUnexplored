package net.regions_unexplored.datagen;

import dev.worldgen.lithostitched.api.registry.LithostitchedRegistries;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.regions_unexplored.RegionsUnexplored;
import net.regions_unexplored.datagen.provider.client.RUModelProvider;
import net.regions_unexplored.datagen.provider.registry.*;
import net.regions_unexplored.datagen.provider.*;
import net.regions_unexplored.datagen.provider.tag.*;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = RegionsUnexplored.MOD_ID)
public class RUDatagen {
    private static final RegistrySetBuilder BOOTSTRAPS = new RegistrySetBuilder()
        .add(Registries.FEATURE, RUConfiguredFeatureBootstrap::bootstrap)
        .add(Registries.PLACED_FEATURE, RUPlacedFeatureBootstrap::bootstrap)
        .add(Registries.BIOME, RUBiomeBootstrap::bootstrap)
        .add(Registries.NOISE, RUNoiseBootstrap::bootstrap)
        .add(Registries.DAMAGE_TYPE, RUDamageTypeBootstrap::bootstrap)
        .add(Registries.TEMPLATE_POOL, RUTemplatePoolBootstrap::bootstrap)
        .add(Registries.PROCESSOR_LIST, context -> {
            // Datagen's vanilla lookup does not include dependency datapacks. This is
            // Lithostitched's empty injection target, referenced by our mansion modifier.
            context.register(dev.worldgen.lithostitched.api.worldgen.processor.LithostitchedProcessorLists.WOODLAND_MANSION,
                new net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList(java.util.List.of()));
            RUProcessorListBootstrap.bootstrap(context);
        })
        .add(LithostitchedRegistries.WORLDGEN_MODIFIER, RUWorldgenModifierBootstrap::bootstrap)
        .add(Registries.MATERIAL_RULE, RUSurfaceRuleBootstrap::bootstrap)
    ;

    private static final RegistrySetBuilder RELOADABLE_BOOTSTRAPS = new RegistrySetBuilder()
        .add(Registries.CONTEXT_INT_PROVIDER, context -> net.regions_unexplored.block.compat.CompostableBlocks.COMPOSTABLES.values().stream().distinct().forEach(chance ->
            context.register(net.regions_unexplored.item.RUItemComponents.compostingChance(chance),
                net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders.binomial(1, chance).value())))
        .add(Registries.ADVANCEMENT, context -> new RUAdvancementProvider(context).generate())
        .add(Registries.LOOT_TABLE, new RULootTableProvider())
        .add(RURecipeProvider.create());
    
    @SubscribeEvent
    public static void gatherDataClient(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        event.createWorldRegistryObjects(BOOTSTRAPS, Set.of(RegionsUnexplored.MOD_ID));
        event.createReloadableRegistryObjects(RELOADABLE_BOOTSTRAPS, Set.of(RegionsUnexplored.MOD_ID));
        CompletableFuture<HolderLookup.Provider> registries = event.getWorldLookupProvider();
        
        generator.addProvider(true, new RUModelProvider(output));

        generator.addProvider(true, new RULanguageProvider(output));
        
        generator.addProvider(true, new RUBlockTagProvider(output, registries));
        generator.addProvider(true, new RUItemTagProvider(output, registries));
        generator.addProvider(true, new RUEntityTypeTagProvider(output, registries));
        generator.addProvider(true, new RUBiomeTagProvider(output, registries));
        generator.addProvider(true, new RUTemplatePoolTagProvider(output, registries));
        generator.addProvider(true, new RUProcessorListTagProvider(output, registries));

        generator.addProvider(true, new RUDataMapGenerator(output, registries));
    }
}
