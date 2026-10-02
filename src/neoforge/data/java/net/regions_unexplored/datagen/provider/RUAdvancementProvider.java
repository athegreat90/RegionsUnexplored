package net.regions_unexplored.datagen.provider;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.predicates.*;
import net.minecraft.advancements.triggers.*;
import net.minecraft.advancements.triggers.InventoryChangeTrigger.TriggerInstance;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.regions_unexplored.RegionsUnexplored;
import net.regions_unexplored.registry.RUBlocks;
import net.regions_unexplored.registry.data.RUBiomes;
import net.regions_unexplored.registry.RUItems;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import static net.minecraft.advancements.triggers.ConsumeItemTrigger.TriggerInstance.*;
import static net.minecraft.advancements.triggers.InventoryChangeTrigger.TriggerInstance.hasItems;

// TODO 26.3: AdvancementProvider/AdvancementSubProvider moved from the old
// PackOutput+Consumer<AdvancementHolder> "saver" model to the registry BootstrapContext model
// (same as worldgen features/biomes) - AdvancementSubProvider is now an abstract class taking
// BootstrapContext<Advancement> in its constructor, generate() takes no params, and
// Advancement.Builder.save(BootstrapContext<Advancement>, String) replaces
// save(Consumer<AdvancementHolder>, Identifier). Wired into RUDatagen's BOOTSTRAPS via
// .add(Registries.ADVANCEMENT, RUAdvancementProvider::new) instead of generator.addProvider(...).
public class RUAdvancementProvider extends AdvancementSubProvider {
    private final HolderGetter<Biome> biomeGetter;
    private final HolderGetter<Block> blockGetter;
    private final HolderGetter<Item> itemGetter;

    public RUAdvancementProvider(BootstrapContext<Advancement> output) {
        super(output);
        this.biomeGetter = output.lookup(Registries.BIOME);
        this.blockGetter = output.lookup(Registries.BLOCK);
        this.itemGetter = output.lookup(Registries.ITEM);
    }

    @Override
    public void generate() {
        AdvancementHolder root = Advancement.Builder.advancement()
                .rootDisplay(
                        RUBlocks.EUCALYPTUS_NATURAL_SET.getSapling().asItem(),
                        Component.translatable("advancements.regions_unexplored.title"),
                        Component.translatable("advancements.regions_unexplored.description"),
                        RegionsUnexplored.id("textures/gui/advancements/backgrounds/argillite.png"),
                        AdvancementType.TASK,
                        false,
                        false,
                        false
                )
                .addCriterion("load_in_world", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inDimension(Level.OVERWORLD)))
                .save(this.output, RegionsUnexplored.stringId("parent"));

        //SURFACE

        Advancement.Builder advancementSurface = biomeAdvancement(root, RUBlocks.HIBISCUS.get(), "pioneer", AdvancementType.TASK);
        Advancement.Builder advancementAll = biomeAdvancement(root, Items.LEATHER_BOOTS, "regions_explored", AdvancementType.CHALLENGE);
        Advancement.Builder advancementNether = biomeAdvancement(root, RUBlocks.BRIMWOOD_NATURAL_SET.getSapling(), "eternal_expedition", AdvancementType.TASK);
        Advancement.Builder advancementCaves = biomeAdvancement(root, RUBlocks.DROPLEAF.get(), "spelunker", AdvancementType.TASK);

        for (ResourceKey<Biome> biome : RUBiomes.ALL_BIOMES) {
            if (RUBiomes.REMOVED_BIOMES.contains(biome)) continue;

            addBiome(advancementAll, this.biomeGetter, biome);
            if (RUBiomes.NETHER_BIOMES.contains(biome)) {
                addBiome(advancementNether, this.biomeGetter, biome);
            } else {
                if (RUBiomes.CAVE_BIOMES.contains(biome)) {
                    addBiome(advancementCaves, this.biomeGetter, biome);
                } else {
                    addBiome(advancementSurface, this.biomeGetter, biome);
                }
            }
        }

        var pioneer = save(this.output, advancementSurface, "pioneer");
        var regionsExplored = save(this.output, advancementAll, "regions_explored");
        var eternalExpedition = save(this.output, advancementNether, "eternal_expedition");
        var spelunker = save(this.output, advancementCaves, "spelunker");


        AdvancementHolder EVERY_BIT_OF_THE_RAINBOW = Advancement.Builder.advancement()
                .parent(pioneer)
                .display(
                        RUBlocks.SNOWBELLES.getWhite().get().asItem(),
                        Component.translatable("advancements.regions_unexplored.every_bit_of_the_rainbow.title"),
                        Component.translatable("advancements.regions_unexplored.every_bit_of_the_rainbow.description"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("white_snowbelle", hasItems(RUBlocks.SNOWBELLES.getWhite().get()))
                .addCriterion("black_snowbelle", hasItems(RUBlocks.SNOWBELLES.getBlack().get()))
                .addCriterion("blue_snowbelle", hasItems(RUBlocks.SNOWBELLES.getBlue().get()))
                .addCriterion("green_snowbelle", hasItems(RUBlocks.SNOWBELLES.getGreen().get()))
                .addCriterion("pink_snowbelle", hasItems(RUBlocks.SNOWBELLES.getPink().get()))
                .addCriterion("brown_snowbelle", hasItems(RUBlocks.SNOWBELLES.getBrown().get()))
                .addCriterion("cyan_snowbelle", hasItems(RUBlocks.SNOWBELLES.getCyan().get()))
                .addCriterion("gray_snowbelle", hasItems(RUBlocks.SNOWBELLES.getGray().get()))
                .addCriterion("magenta_snowbelle", hasItems(RUBlocks.SNOWBELLES.getMagenta().get()))
                .addCriterion("red_snowbelle", hasItems(RUBlocks.SNOWBELLES.getRed().get()))
                .addCriterion("orange_snowbelle", hasItems(RUBlocks.SNOWBELLES.getOrange().get()))
                .addCriterion("yellow_snowbelle", hasItems(RUBlocks.SNOWBELLES.getYellow().get()))
                .addCriterion("purple_snowbelle", hasItems(RUBlocks.SNOWBELLES.getPurple().get()))
                .addCriterion("lime_snowbelle", hasItems(RUBlocks.SNOWBELLES.getLime().get()))
                .addCriterion("light_gray_snowbelle", hasItems(RUBlocks.SNOWBELLES.getLightGray().get()))
                .addCriterion("light_blue_snowbelle", hasItems(RUBlocks.SNOWBELLES.getLightBlue().get()))
                .save(this.output, RegionsUnexplored.stringId("every_bit_of_the_rainbow"));


        AdvancementHolder FROM_THE_TOPS = Advancement.Builder.advancement()
                .parent(pioneer)
                .display(
                        RUBlocks.KAPOK_VINES.get().asItem(),
                        Component.translatable("advancements.regions_unexplored.from_the_tree_tops.title"),
                        Component.translatable("advancements.regions_unexplored.from_the_tree_tops.description"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("kapok_vines", EnterBlockTrigger.TriggerInstance.entersBlock(net.minecraft.core.HolderSet.direct(RUBlocks.KAPOK_VINES_PLANT.get().builtInRegistryHolder())))
                .save(this.output, RegionsUnexplored.stringId("from_the_tree_tops"));


        AdvancementHolder LIGHT_AS_A_FROG = Advancement.Builder.advancement()
                .parent(pioneer)
                .display(
                        RUBlocks.FLOWERING_LILY_PAD.get().asItem(),
                        Component.translatable("advancements.regions_unexplored.light_as_a_frog.title"),
                        Component.translatable("advancements.regions_unexplored.light_as_a_frog.description"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("lily_pad", EnterBlockTrigger.TriggerInstance.entersBlock(net.minecraft.core.HolderSet.direct(RUBlocks.GIANT_LILY_PAD.get().builtInRegistryHolder())))
                .save(this.output, RegionsUnexplored.stringId("light_as_a_frog"));

        //NETHER

        AdvancementHolder DOWNER = Advancement.Builder.advancement()
                .parent(eternalExpedition)
                .display(
                        RUBlocks.DORCEL.get().asItem(),
                        Component.translatable("advancements.regions_unexplored.downer.title"),
                        Component.translatable("advancements.regions_unexplored.downer.description"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("dorcel", EnterBlockTrigger.TriggerInstance.entersBlock(net.minecraft.core.HolderSet.direct(RUBlocks.DORCEL.get().builtInRegistryHolder())))
                .save(this.output, RegionsUnexplored.stringId("downer"));

        AdvancementHolder LIGHT_SNACK = Advancement.Builder.advancement()
                .parent(eternalExpedition)
                .display(
                        RUBlocks.HANGING_EARLIGHT.get().asItem(),
                        Component.translatable("advancements.regions_unexplored.light_snack.title"),
                        Component.translatable("advancements.regions_unexplored.light_snack.description"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("earlight", usedItem(this.itemGetter, RUItems.HANGING_EARLIGHT_FRUIT.get()))
                .save(this.output, RegionsUnexplored.stringId("light_snack"));

        //CAVE

        AdvancementHolder BLIND_AS_A_BAT = Advancement.Builder.advancement()
                .parent(spelunker)
                .display(
                        RUBlocks.DUSKMELON.get().asItem(),
                        Component.translatable("advancements.regions_unexplored.blind_as_a_bat.title"),
                        Component.translatable("advancements.regions_unexplored.blind_as_a_bat.description"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("duskmelon", usedItem(this.itemGetter, RUItems.DUSKMELON_SLICE.get()))
                .save(this.output, RegionsUnexplored.stringId("blind_as_a_bat"));

        AdvancementHolder THIS_BLEEDS_RED = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        RUBlocks.SOCOTRA_WOOD_SET.getLog().asItem(),
                        Component.translatable("advancements.regions_unexplored.this_tree_bleeds_red.title"),
                        Component.translatable("advancements.regions_unexplored.this_tree_bleeds_red.description"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("socotra_log", hasItems(RUBlocks.SOCOTRA_WOOD_SET.getLog()))
                .save(this.output, RegionsUnexplored.stringId("this_tree_bleeds_red"));

        AdvancementHolder GOT_WOOD = Advancement.Builder.advancement()
                .parent(THIS_BLEEDS_RED)
                .display(
                        RUBlocks.REDWOOD_WOOD_SET.getLog().asItem(),
                        Component.translatable("advancements.regions_unexplored.got_wood.title"),
                        Component.translatable("advancements.regions_unexplored.got_wood.description"),
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion("bamboo_log", hasItems(RUBlocks.BAMBOO_LOG.get()))
                .addCriterion("small_oak_log", hasItems(RUBlocks.SMALL_OAK_LOG.get()))
                .addCriterion("ashen_log", hasItems(RUBlocks.ASHEN_WOOD_SET.getLog()))
                .addCriterion("silver_birch_log", hasItems(RUBlocks.SILVER_BIRCH_WOOD_SET.getLog()))
                .addCriterion("alpha_log", hasItems(RUBlocks.ALPHA_WOOD_SET.getLog()))
                .addCriterion("baobab_log", hasItems(RUBlocks.BAOBAB_WOOD_SET.getLog()))
                .addCriterion("blackwood_log", hasItems(RUBlocks.BLACKWOOD_WOOD_SET.getLog()))
                .addCriterion("brimwood_log", hasItems(RUBlocks.BRIMWOOD_WOOD_SET.getLog()))
                .addCriterion("cobalt_log", hasItems(RUBlocks.COBALT_WOOD_SET.getLog()))
                .addCriterion("cypress_log", hasItems(RUBlocks.CYPRESS_WOOD_SET.getLog()))
                .addCriterion("dead_log", hasItems(RUBlocks.DEAD_WOOD_SET.getLog()))
                .addCriterion("eucalyptus_log", hasItems(RUBlocks.EUCALYPTUS_WOOD_SET.getLog()))
                .addCriterion("joshua_log", hasItems(RUBlocks.JOSHUA_WOOD_SET.getLog()))
                .addCriterion("kapok_log", hasItems(RUBlocks.KAPOK_WOOD_SET.getLog()))
                .addCriterion("larch_log", hasItems(RUBlocks.LARCH_WOOD_SET.getLog()))
                .addCriterion("magnolia_log", hasItems(RUBlocks.MAGNOLIA_WOOD_SET.getLog()))
                .addCriterion("maple_log", hasItems(RUBlocks.MAPLE_WOOD_SET.getLog()))
                .addCriterion("mauve_log", hasItems(RUBlocks.WISTERIA_WOOD_SET.getLog()))
                .addCriterion("palm_log", hasItems(RUBlocks.PALM_WOOD_SET.getLog()))
                .addCriterion("pine_log", hasItems(RUBlocks.PINE_WOOD_SET.getLog()))
                .addCriterion("redwood_log", hasItems(RUBlocks.REDWOOD_WOOD_SET.getLog()))
                .addCriterion("socotra_log", hasItems(RUBlocks.SOCOTRA_WOOD_SET.getLog()))
                .addCriterion("willow_log", hasItems(RUBlocks.WILLOW_WOOD_SET.getLog()))
                .addCriterion("blue_bioshroom_stem", hasItems(RUBlocks.BLUE_BIOSHROOM_WOOD_SET.getLog()))
                .addCriterion("green_bioshroom_stem", hasItems(RUBlocks.GREEN_BIOSHROOM_WOOD_SET.getLog()))
                .addCriterion("pink_bioshroom_stem", hasItems(RUBlocks.PINK_BIOSHROOM_WOOD_SET.getLog()))
                .addCriterion("yellow_bioshroom_stem", hasItems(RUBlocks.YELLOW_BIOSHROOM_WOOD_SET.getLog()))
                .save(this.output, RegionsUnexplored.stringId("got_wood"));

        AdvancementHolder MYCOLOGIST = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        RUBlocks.BLUE_BIOSHROOM.get().asItem(),
                        Component.translatable("advancements.regions_unexplored.mycologist.title"),
                        Component.translatable("advancements.regions_unexplored.mycologist.description"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("blue_bioshroom", hasItems(RUBlocks.BLUE_BIOSHROOM.get()))
                .addCriterion("pink_bioshroom", hasItems(RUBlocks.PINK_BIOSHROOM.get()))
                .addCriterion("yellow_bioshroom", hasItems(RUBlocks.YELLOW_BIOSHROOM.get()))
                .addCriterion("green_bioshroom", hasItems(RUBlocks.GREEN_BIOSHROOM.get()))
                .save(this.output, RegionsUnexplored.stringId("mycologist"));

        AdvancementHolder ANCIENT_SPECIMENS = Advancement.Builder.advancement()
                .parent(MYCOLOGIST)
                .display(
                        RUBlocks.BLUE_BIOSHROOM_WOOD_SET.getLog().asItem(),
                        Component.translatable("advancements.regions_unexplored.ancient_specimens.title"),
                        Component.translatable("advancements.regions_unexplored.ancient_specimens.description"),
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion("blue_bioshroom_stem", hasItems(RUBlocks.BLUE_BIOSHROOM_WOOD_SET.getLog()))
                .addCriterion("pink_bioshroom_stem", hasItems(RUBlocks.PINK_BIOSHROOM_WOOD_SET.getLog()))
                .addCriterion("yellow_bioshroom_stem", hasItems(RUBlocks.YELLOW_BIOSHROOM_WOOD_SET.getLog()))
                .addCriterion("green_bioshroom_stem", hasItems(RUBlocks.GREEN_BIOSHROOM_WOOD_SET.getLog()))
                .save(this.output, RegionsUnexplored.stringId("ancient_specimens"));

        CompoundTag beaconNbt = new CompoundTag();
        beaconNbt.putInt("levels", 0);

        AdvancementHolder RGBEACON = Advancement.Builder.advancement()
            .parent(Identifier.withDefaultNamespace("nether/create_beacon"))
            .display(display(RUBlocks.PRISMAGLASS.get(), "rgbeacon", AdvancementType.GOAL))
            .addCriterion("place_prismaglass_on_beacon", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllOfCondition.allOf(
                LocationCheck.checkLocation(blockPredicate(this.blockGetter, RUBlocks.PRISMAGLASS.get(), b -> b)),
                LocationCheck.checkLocation(blockPredicate(this.blockGetter, Blocks.BEACON, b -> b), BlockPos.ZERO.below()),
                LocationCheck.checkLocation(blockPredicate(this.blockGetter, Blocks.BEACON, b -> b.hasNbt(beaconNbt)), BlockPos.ZERO.below()).invert()
            )))
        .save(this.output, RegionsUnexplored.stringId("rgbeacon"));
    }

    private static LocationPredicate.Builder blockPredicate(HolderGetter<Block> blockGetter, Block block, UnaryOperator<BlockPredicate.Builder> operator) {
        return LocationPredicate.Builder.location().setBlock(operator.apply(BlockPredicate.Builder.block().of(blockGetter, block)));
    }

    private static Advancement.Builder biomeAdvancement(AdvancementHolder parent, ItemLike item, String name, AdvancementType type) {
        return Advancement.Builder.advancement()
            .parent(parent)
            .display(
                item.asItem(),
                Component.translatable("advancements.regions_unexplored." + name + ".title"),
                Component.translatable("advancements.regions_unexplored." + name + ".description"),
                type,
                true,
                true,
                type == AdvancementType.CHALLENGE
            );
    }

    private static Advancement.Builder addBiome(Advancement.Builder builder, HolderGetter<Biome> biomeGetter, ResourceKey<Biome> biome) {
        return builder.addCriterion(
            biome.identifier().getPath(),
            PlayerTrigger.TriggerInstance.located(
                LocationPredicate.Builder.inBiome(biomeGetter.getOrThrow(biome))
            )
        );
    }

    private static AdvancementHolder save(BootstrapContext<Advancement> output, Advancement.Builder builder, String name) {
        return builder.save(output, RegionsUnexplored.stringId(name));
    }


    private static DisplayInfo display(ItemLike icon, String name, AdvancementType type) {
        return new DisplayInfo(
            new ItemStackTemplate(icon.asItem()),
            text(name, "title"),
            text(name, "description"),
            Optional.empty(),
            type,
            true,
            true,
            false
        );
    }

    private static Component text(String name, String suffix) {
        return Component.translatable(String.format("advancements.regions_unexplored.%s.%s", name, suffix));
    }
}
