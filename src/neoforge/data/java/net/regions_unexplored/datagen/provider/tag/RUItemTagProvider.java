package net.regions_unexplored.datagen.provider.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.regions_unexplored.RegionsUnexplored;
import net.regions_unexplored.registry.RUBlocks;
import net.regions_unexplored.block.set.NaturalSet;
import net.regions_unexplored.block.set.WoodSet;
import net.regions_unexplored.registry.tag.RUItemTags;
import net.regions_unexplored.registry.RUItems;

import java.util.concurrent.CompletableFuture;

public class RUItemTagProvider extends ItemTagsProvider {
    public RUItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, RegionsUnexplored.MOD_ID);
    }

    // TODO 26.3: TagAppender.add now takes ResourceKey<Item> instead of Item directly
    // (IntrinsicHolderTagsProvider's convenience overload is gone); resolve via the item's
    // own built-in registry holder.
    private static ResourceKey<Item> key(Item item) {
        return item.builtInRegistryHolder().key();
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        addCommonTags(provider);

        this.tag(ItemTags.BAMBOO_BLOCKS).addTag(RUItemTags.BAMBOO_LOGS);
        this.tag(ItemTags.BIRCH_LOGS).add(key(RUBlocks.SILVER_BIRCH_WOOD_SET.getLog().asItem()));


        var ruLogs = this.tag(RUItemTags.LOGS).add(key(RUBlocks.BAMBOO_LOG.get().asItem())).add(key(RUBlocks.SMALL_OAK_LOG.get().asItem()));
        var nonFlammableWood = this.tag(ItemTags.NON_FLAMMABLE_WOOD);
        var logs = this.tag(ItemTags.LOGS);
        var logsThatBurn = this.tag(ItemTags.LOGS_THAT_BURN);
        var strippedLogs = this.tag(Tags.Items.STRIPPED_LOGS);
        var strippedWoods = this.tag(Tags.Items.STRIPPED_WOODS);
        var planks = this.tag(ItemTags.PLANKS);
        var stairs = this.tag(ItemTags.WOODEN_STAIRS);
        var slabs = this.tag(ItemTags.WOODEN_SLABS);
        var fences = this.tag(ItemTags.WOODEN_FENCES);
        var fenceGates = this.tag(ItemTags.FENCE_GATES);
        var doors = this.tag(ItemTags.WOODEN_DOORS);
        var trapdoors = this.tag(ItemTags.WOODEN_TRAPDOORS);
        var buttons = this.tag(ItemTags.WOODEN_BUTTONS);
        var pressurePlates = this.tag(ItemTags.WOODEN_PRESSURE_PLATES);
        var signs = this.tag(ItemTags.SIGNS);
        var hangingSigns = this.tag(ItemTags.HANGING_SIGNS);
        var boats = this.tag(ItemTags.BOATS);
        var chestBoats = this.tag(ItemTags.CHEST_BOATS);


        for (WoodSet set : RUBlocks.WOOD_SETS) {
            if (set.fireproof) {
                set.getAllBlocks().forEach(item -> nonFlammableWood.add(key(item)));
            }
            if (set.getLog() != null) {
                var tag = set.fireproof ? logs : logsThatBurn;
                tag.add(key(set.getLog().asItem()));
                ruLogs.add(key(set.getLog().asItem()));
            }
            if (set.getStrippedLog() != null) strippedLogs.add(key(set.getStrippedLog().asItem()));
            if (set.getStrippedWood() != null) strippedWoods.add(key(set.getStrippedWood().asItem()));
            if (set.getPlanks() != null) planks.add(key(set.getPlanks().asItem()));
            if (set.getStairs() != null) stairs.add(key(set.getStairs().asItem()));
            if (set.getSlab() != null) slabs.add(key(set.getSlab().asItem()));
            if (set.getFence() != null) fences.add(key(set.getFence().asItem()));
            if (set.getFenceGate() != null) fenceGates.add(key(set.getFenceGate().asItem()));
            if (set.getDoor() != null) doors.add(key(set.getDoor().asItem()));
            if (set.getTrapdoor() != null) trapdoors.add(key(set.getTrapdoor().asItem()));
            if (set.getButton() != null) buttons.add(key(set.getButton().asItem()));
            if (set.getPressurePlate() != null) pressurePlates.add(key(set.getPressurePlate().asItem()));
            if (set.getSign() != null) signs.add(key(set.getSign().asItem()));
            if (set.getHangingSign() != null) hangingSigns.add(key(set.getHangingSign().asItem()));
            if (set.getBoat() != null) boats.add(key(set.getBoat()));
            if (set.getChestBoat() != null) chestBoats.add(key(set.getChestBoat()));
        }
        for (Block block : RUBlocks.PAINTED_PLANKS.getAll()) {
            planks.add(key(block.asItem()));
        }
        for (Block block : RUBlocks.PAINTED_STAIRS.getAll()) {
            stairs.add(key(block.asItem()));
        }
        for (Block block : RUBlocks.PAINTED_SLABS.getAll()) {
            slabs.add(key(block.asItem()));
        }


        var branches = this.tag(RUItemTags.BRANCHES);
        var leaves = this.tag(ItemTags.LEAVES);
        var saplings = this.tag(ItemTags.SAPLINGS);
        var shrubs = this.tag(RUItemTags.SHRUBS);
        
        for (NaturalSet set : RUBlocks.NATURAL_SETS) {
            if (set.getBranch() != null) branches.add(key(set.getBranch().asItem()));
            if (set.getLeaves() != null) leaves.add(key(set.getLeaves().asItem()));
            if (set.getSapling() != null) saplings.add(key(set.getSapling().asItem()));
            if (set.getShrub() != null) shrubs.add(key(set.getShrub().asItem()));
        }
        
        this.tag(ItemTags.DIRT)
            .add(key(RUBlocks.ALPHA_GRASS_BLOCK.get().asItem()))
            .add(key(RUBlocks.ASHEN_DIRT.get().asItem()))
            .add(key(RUBlocks.CHALK_GRASS_BLOCK.get().asItem()))
            .add(key(RUBlocks.DEEPSLATE_GRASS_BLOCK.get().asItem()))
            .add(key(RUBlocks.DEEPSLATE_PRISMOSS.get().asItem()))
            .add(key(RUBlocks.DEEPSLATE_VIRIDESCENT_NYLIUM.get().asItem()))
            .add(key(RUBlocks.PEAT_COARSE_DIRT.get().asItem()))
            .add(key(RUBlocks.PEAT_PODZOL.get().asItem()))
            .add(key(RUBlocks.PEAT_DIRT.get().asItem()))
            .add(key(RUBlocks.PEAT_DIRT_PATH.get().asItem()))
            .add(key(RUBlocks.PEAT_FARMLAND.get().asItem()))
            .add(key(RUBlocks.PEAT_GRASS_BLOCK.get().asItem()))
            .add(key(RUBlocks.PEAT_MUD.get().asItem()))
            .add(key(RUBlocks.SILT_COARSE_DIRT.get().asItem()))
            .add(key(RUBlocks.SILT_PODZOL.get().asItem()))
            .add(key(RUBlocks.SILT_DIRT.get().asItem()))
            .add(key(RUBlocks.SILT_DIRT_PATH.get().asItem()))
            .add(key(RUBlocks.SILT_FARMLAND.get().asItem()))
            .add(key(RUBlocks.SILT_GRASS_BLOCK.get().asItem()))
            .add(key(RUBlocks.SILT_MUD.get().asItem()))
            .add(key(RUBlocks.PRISMOSS.get().asItem()))
            .add(key(RUBlocks.STONE_GRASS_BLOCK.get().asItem()))
            .add(key(RUBlocks.ARGILLITE_GRASS_BLOCK.get().asItem()))
            .add(key(RUBlocks.VIRIDESCENT_NYLIUM.get().asItem()));
        this.tag(Tags.Items.FLOWERS)
            .add(key(RUBlocks.HYACINTH_FLOWERS.get().asItem()))
            .add(key(RUBlocks.ORANGE_CONEFLOWER.get().asItem()))
            .add(key(RUBlocks.PURPLE_CONEFLOWER.get().asItem()))
            .add(key(RUBlocks.BLUE_MAGNOLIA_FLOWERS.get().asItem()))
            .add(key(RUBlocks.PINK_MAGNOLIA_FLOWERS.get().asItem()))
            .add(key(RUBlocks.WHITE_MAGNOLIA_FLOWERS.get().asItem()));
        this.tag(ItemTags.FOX_FOOD).add(key(RUItems.SALMONBERRY.get().asItem()));
        this.tag(ItemTags.OAK_LOGS).add(key(RUBlocks.SMALL_OAK_LOG.get().asItem())).add(key(RUBlocks.STRIPPED_SMALL_OAK_LOG.get().asItem()));
        this.tag(ItemTags.REDSTONE_ORES).add(key(RUBlocks.RAW_REDSTONE_BLOCK.get().asItem()));
        var smallFlowers = this.tag(Tags.Items.FLOWERS_SMALL)
                .add(key(RUBlocks.ALPHA_DANDELION.get().asItem()))
                .add(key(RUBlocks.ALPHA_ROSE.get().asItem()))
                .add(key(RUBlocks.ASTER.get().asItem()))
                .add(key(RUBlocks.BLEEDING_HEART.get().asItem()))
                .add(key(RUBlocks.BLUE_LUPINE.get().asItem()))
                .add(key(RUBlocks.DAISY.get().asItem()))
                .add(key(RUBlocks.DORCEL.get().asItem()))
                .add(key(RUBlocks.FELICIA_DAISY.get().asItem()))
                .add(key(RUBlocks.FIREWEED.get().asItem()))
                .add(key(RUBlocks.GLISTERING_BLOOM.get().asItem()))
                .add(key(RUBlocks.HIBISCUS.get().asItem()))
                .add(key(RUBlocks.MALLOW.get().asItem()))
                .add(key(RUBlocks.HYSSOP.get().asItem()))
                .add(key(RUBlocks.PINK_LUPINE.get().asItem()))
                .add(key(RUBlocks.POPPY_BUSH.get().asItem()))
                .add(key(RUBlocks.SALMON_POPPY_BUSH.get().asItem()))
                .add(key(RUBlocks.PURPLE_LUPINE.get().asItem()))
                .add(key(RUBlocks.RED_LUPINE.get().asItem()))
                .add(key(RUBlocks.TSUBAKI.get().asItem()))
                .add(key(RUBlocks.WARATAH.get().asItem()))
                .add(key(RUBlocks.WHITE_TRILLIUM.get().asItem()))
                .add(key(RUBlocks.WILTING_TRILLIUM.get().asItem()))
                .add(key(RUBlocks.YELLOW_LUPINE.get().asItem()));
        var snowbelles = this.tag(RUItemTags.SNOWBELLE);
        for (Block block : RUBlocks.SNOWBELLES.getAll()) {
            smallFlowers.add(key(block.asItem()));
            snowbelles.add(key(block.asItem()));
        }
        //this.tag(ItemTags.TALL_FLOWERS).add(key(RUBlocks.TASSEL.get().asItem())).add(key(RUBlocks.DAY_LILY.get().asItem()));
        this.tag(ItemTags.TRIM_MATERIALS).add(key(RUBlocks.PRISMARITE_CLUSTER.get().asItem()));
        this.tag(ItemTags.WART_BLOCKS)
            .add(key(RUBlocks.GREEN_BIOSHROOM_BLOCK.get().asItem()))
            .add(key(RUBlocks.BLUE_BIOSHROOM_BLOCK.get().asItem()))
            .add(key(RUBlocks.PINK_BIOSHROOM_BLOCK.get().asItem()))
            .add(key(RUBlocks.YELLOW_BIOSHROOM_BLOCK.get().asItem()))
            .add(key(RUBlocks.GLOWING_GREEN_BIOSHROOM_BLOCK.get().asItem()))
            .add(key(RUBlocks.GLOWING_BLUE_BIOSHROOM_BLOCK.get().asItem()))
            .add(key(RUBlocks.GLOWING_PINK_BIOSHROOM_BLOCK.get().asItem()))
            .add(key(RUBlocks.GLOWING_YELLOW_BIOSHROOM_BLOCK.get().asItem()));


        this.tag(RUItemTags.HYACINTH_BLOOMS)
                .add(key(RUBlocks.HYACINTH_BLOOM.get().asItem()))
                .add(key(RUBlocks.TALL_HYACINTH_STOCK.get().asItem()))
        ;
        this.tag(RUItemTags.PRISMARITE_CRYSTALS)
                .add(key(RUBlocks.PRISMARITE_CLUSTER.get().asItem()))
                .add(key(RUBlocks.LARGE_PRISMARITE_CLUSTER.get().asItem()))
                .add(key(RUBlocks.HANGING_PRISMARITE.get().asItem()))
        ;
        this.tag(RUItemTags.GRASS)
                .add(key(RUBlocks.FROZEN_GRASS.get().asItem()))
                .add(key(RUBlocks.SANDY_GRASS.get().asItem()))
                .add(key(RUBlocks.GRASS_SPROUTS.get().asItem()))
                .add(key(Blocks.SHORT_GRASS.asItem()))
                .add(key(Blocks.FERN.asItem()))
        ;
        this.tag(RUItemTags.ASH)
                .add(key(RUBlocks.ASH.get().asItem()))
                .add(key(RUBlocks.VOLCANIC_ASH.get().asItem()))
        ;
        this.tag(RUItemTags.BIOSHROOMS)
                .add(key(RUBlocks.BLUE_BIOSHROOM.get().asItem()))
                .add(key(RUBlocks.GREEN_BIOSHROOM.get().asItem()))
                .add(key(RUBlocks.PINK_BIOSHROOM.get().asItem()))
                .add(key(RUBlocks.YELLOW_BIOSHROOM.get().asItem()))
                .add(key(RUBlocks.TALL_BLUE_BIOSHROOM.get().asItem()))
                .add(key(RUBlocks.TALL_GREEN_BIOSHROOM.get().asItem()))
                .add(key(RUBlocks.TALL_PINK_BIOSHROOM.get().asItem()))
                .add(key(RUBlocks.TALL_YELLOW_BIOSHROOM.get().asItem()))
        ;
        this.tag(RUItemTags.ALPHA_LOGS)
            .add(key(RUBlocks.ALPHA_WOOD_SET.getLog().asItem()))
        ;
        this.tag(RUItemTags.BAMBOO_LOGS)
                .add(key(RUBlocks.BAMBOO_LOG.get().asItem()))
                .add(key(RUBlocks.STRIPPED_BAMBOO_LOG.get().asItem()))
        ;
        this.tag(RUItemTags.BAOBAB_LOGS)
                .add(key(RUBlocks.BAOBAB_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.BAOBAB_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.BAOBAB_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.BAOBAB_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.BRIMWOOD_LOGS)
                .add(key(RUBlocks.BRIMWOOD_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.BRIMWOOD_WOOD_SET.getLogMagma().asItem()))
                .add(key(RUBlocks.BRIMWOOD_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.BRIMWOOD_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.BRIMWOOD_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.BLUE_BIOSHROOM_LOGS)
                .add(key(RUBlocks.BLUE_BIOSHROOM_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.BLUE_BIOSHROOM_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.BLUE_BIOSHROOM_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.BLUE_BIOSHROOM_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.BLACKWOOD_LOGS)
                .add(key(RUBlocks.BLACKWOOD_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.BLACKWOOD_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.BLACKWOOD_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.BLACKWOOD_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.COBALT_LOGS)
                .add(key(RUBlocks.COBALT_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.COBALT_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.COBALT_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.COBALT_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.CYPRESS_LOGS)
                .add(key(RUBlocks.CYPRESS_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.CYPRESS_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.CYPRESS_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.CYPRESS_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.DEAD_LOGS)
                .add(key(RUBlocks.ASHEN_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.DEAD_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.DEAD_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.ASHEN_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.DEAD_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.DEAD_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.EUCALYPTUS_LOGS)
                .add(key(RUBlocks.EUCALYPTUS_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.EUCALYPTUS_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.EUCALYPTUS_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.EUCALYPTUS_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.GREEN_BIOSHROOM_LOGS)
                .add(key(RUBlocks.GREEN_BIOSHROOM_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.GREEN_BIOSHROOM_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.GREEN_BIOSHROOM_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.GREEN_BIOSHROOM_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.JOSHUA_LOGS)
                .add(key(RUBlocks.JOSHUA_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.JOSHUA_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.JOSHUA_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.JOSHUA_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.KAPOK_LOGS)
                .add(key(RUBlocks.KAPOK_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.KAPOK_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.KAPOK_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.KAPOK_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.LARCH_LOGS)
                .add(key(RUBlocks.LARCH_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.LARCH_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.LARCH_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.LARCH_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.MAGNOLIA_LOGS)
                .add(key(RUBlocks.MAGNOLIA_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.MAGNOLIA_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.MAGNOLIA_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.MAGNOLIA_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.MAPLE_LOGS)
                .add(key(RUBlocks.MAPLE_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.MAPLE_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.MAPLE_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.MAPLE_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.WISTERIA_LOGS)
                .add(key(RUBlocks.WISTERIA_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.WISTERIA_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.WISTERIA_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.WISTERIA_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.PALM_LOGS)
                .add(key(RUBlocks.PALM_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.PALM_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.PALM_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.PALM_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.PINE_LOGS)
                .add(key(RUBlocks.PINE_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.PINE_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.PINE_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.PINE_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.PINK_BIOSHROOM_LOGS)
                .add(key(RUBlocks.PINK_BIOSHROOM_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.PINK_BIOSHROOM_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.PINK_BIOSHROOM_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.PINK_BIOSHROOM_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.REDWOOD_LOGS)
                .add(key(RUBlocks.REDWOOD_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.REDWOOD_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.REDWOOD_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.REDWOOD_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.SOCOTRA_LOGS)
                .add(key(RUBlocks.SOCOTRA_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.SOCOTRA_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.SOCOTRA_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.SOCOTRA_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.WILLOW_LOGS)
                .add(key(RUBlocks.WILLOW_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.WILLOW_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.WILLOW_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.WILLOW_WOOD_SET.getStrippedWood().asItem()))
        ;
        this.tag(RUItemTags.YELLOW_BIOSHROOM_LOGS)
                .add(key(RUBlocks.YELLOW_BIOSHROOM_WOOD_SET.getLog().asItem()))
                .add(key(RUBlocks.YELLOW_BIOSHROOM_WOOD_SET.getStrippedLog().asItem()))
                .add(key(RUBlocks.YELLOW_BIOSHROOM_WOOD_SET.getWood().asItem()))
                .add(key(RUBlocks.YELLOW_BIOSHROOM_WOOD_SET.getStrippedWood().asItem()))
        ;
    }

    public void addCommonTags(HolderLookup.Provider provider) {
        this.tag(Tags.Items.STORAGE_BLOCKS_BONE_MEAL).add(key(RUBlocks.OVERGROWN_BONE_BLOCK.get().asItem()));
        
        var fenceGates = this.tag(Tags.Items.FENCE_GATES_WOODEN);
        var fences = this.tag(Tags.Items.FENCES_WOODEN);
        for (WoodSet set : RUBlocks.WOOD_SETS) {
            if (set.getFenceGate() != null) fenceGates.add(key(set.getFenceGate().asItem()));
            if (set.getFence() != null) fences.add(key(set.getFence().asItem()));
        }
        this.tag(Tags.Items.FOODS_FRUIT)
            .add(key(RUItems.SALMONBERRY.get()))
            .add(key(RUItems.HANGING_EARLIGHT_FRUIT.get()));
        this.tag(Tags.Items.GEMS)
            .addTag(RUItemTags.PRISMARITE_CRYSTALS);
        this.tag(Tags.Items.GLASS_BLOCKS)
            .add(key(RUBlocks.PRISMAGLASS.get().asItem()));
        this.tag(Tags.Items.MUSHROOMS)
            .add(key(RUBlocks.BLUE_BIOSHROOM.get().asItem()))
            .add(key(RUBlocks.TALL_BLUE_BIOSHROOM.get().asItem()))
            .add(key(RUBlocks.GREEN_BIOSHROOM.get().asItem()))
            .add(key(RUBlocks.TALL_GREEN_BIOSHROOM.get().asItem()))
            .add(key(RUBlocks.PINK_BIOSHROOM.get().asItem()))
            .add(key(RUBlocks.TALL_PINK_BIOSHROOM.get().asItem()))
            .add(key(RUBlocks.YELLOW_BIOSHROOM.get().asItem()))
            .add(key(RUBlocks.TALL_YELLOW_BIOSHROOM.get().asItem()))
            .add(key(RUBlocks.MYCOTOXIC_MUSHROOMS.get().asItem()));
        this.tag(Tags.Items.STONES)
            .add(key(RUBlocks.MOSSY_STONE.get().asItem()))
            .add(key(RUBlocks.ARGILLITE.get().asItem()))
            .add(key(RUBlocks.CHALK.get().asItem()));
        this.tag(Tags.Items.GRAVELS)
            .add(key(RUBlocks.ASH.get().asItem()))
            .add(key(RUBlocks.VOLCANIC_ASH.get().asItem()));
        this.tag(Tags.Items.OBSIDIANS_CRYING)
            .add(key(RUBlocks.COBALT_OBSIDIAN.get().asItem()));
    }
}
