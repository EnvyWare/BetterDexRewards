package com.envyful.better.dex.rewards.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.config.type.ExtendedConfigItem;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.registries.PixelmonItems;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import net.minecraft.item.Items;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Collections;
import java.util.List;

@ConfigPath("config/BetterDexRewards/guis.yml")
@ConfigSerializable
public class BetterDexRewardsGraphics extends AbstractYamlConfig {

    private MainUI mainUI = new MainUI();
    private MissingPokemonUI missingPokemonUI = new MissingPokemonUI();
    private RanksUI rankUI = new RanksUI();

    public BetterDexRewardsGraphics() {}

    public MainUI getMainUI() {
        return this.mainUI;
    }

    public MissingPokemonUI getMissingPokemonUI() {
        return this.missingPokemonUI;
    }

    public RanksUI getRankUI() {
        return this.rankUI;
    }

    @ConfigSerializable
    public static class MainUI {

        private ConfigInterface guiSettings = ConfigInterface.builder()
                .title("BetterDexRewards")
                .height(3)
                .fillType(ConfigInterface.FillType.BLOCK)
                .fillerItem(ConfigItem.builder()
                        .type("minecraft:black_stained_glass_pane")
                        .amount(1)
                        .name(" ")
                        .build())
                .build();

        private ExtendedConfigItem percentageItem = ExtendedConfigItem.builder()
                .type("pixelmon:poke_ball")
                .amount(1)
                .name("&eCurrent PokeDex Percentage")
                .lore(
                        "&eComplete: &a%percentage%"
                )
                .nbt("tooltip", new ConfigItem.NBTValue("string", ""))
                .positions(1, 1)
                .build();

        private ExtendedConfigItem missingItem = ExtendedConfigItem.builder()
                .type("pixelmon:pokeradar")
                .amount(1)
                .name("&eMissing Pokemon")
                .lore(
                        "",
                        "&eMissing: %missing%",
                        ""
                )
                .positions(5, 1)
                .nbt("ndex", new ConfigItem.NBTValue("short", PixelmonSpecies.UNOWN.getValueUnsafe().getDex() + ""))
                .build();

        private ExtendedConfigItem ranksItem = ExtendedConfigItem.builder()
                .type("pixelmon:master_ball")
                .amount(1)
                .name("&ePokeDex Ranks")
                .positions(3, 1)
                .build();

        private ExtendedConfigItem infoItem = ExtendedConfigItem.builder()
                .type("minecraft:paper")
                .amount(1)
                .name("&eInfo")
                .positions(7, 1)
                .build();

        public MainUI() {}

        public ConfigInterface getGuiSettings() {
            return this.guiSettings;
        }

        public ExtendedConfigItem getPercentageItem() {
            return this.percentageItem;
        }

        public ExtendedConfigItem getMissingItem() {
            return this.missingItem;
        }

        public ExtendedConfigItem getRanksItem() {
            return this.ranksItem;
        }

        public ExtendedConfigItem getInfoItem() {
            return this.infoItem;
        }
    }

    @ConfigSerializable
    public static class MissingPokemonUI {

        private ConfigInterface guiSettings = ConfigInterface.builder()
                .title("BetterDexRewards")
                .height(6)
                .fillType(ConfigInterface.FillType.BLOCK)
                .fillerItem(ConfigItem.builder()
                        .type("minecraft:black_stained_glass_pane")
                        .amount(1)
                        .name(" ")
                        .build())
                .build();

        private List<Integer> missingPokemonPositions = Lists.newArrayList(
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 10, 11, 12, 13, 14, 15, 16, 17,
                18, 19, 20, 21, 22, 23, 24, 25, 26,
                27, 28, 29, 30, 31, 32, 33, 34, 35
        );

        private ExtendedConfigItem backButton = ExtendedConfigItem.builder()
                .type("pixelmon:eject_button")
                .amount(1)
                .name("&eBack")
                .positions(4, 5)
                .build();

        private ExtendedConfigItem previousPageButton = ExtendedConfigItem.builder()
                .type("pixelmon:trade_holder_left")
                .amount(1)
                .name("&ePrevious Page")
                .positions(0, 5)
                .build();

        private ExtendedConfigItem nextPageButton = ExtendedConfigItem.builder()
                .type("pixelmon:trade_holder_left")
                .amount(1)
                .name("&eNext Page")
                .positions(8, 5)
                .build();

        private ConfigItem missingPokemonItem =
                ConfigItem.builder()
                        .type("pixelmon:ui_element")
                        .amount(1)
                        .name("&e%pokemon% §f- %pokedex%")
                        .lore(
                                "&eBiomes",
                                "&f%biomes%",
                                " ",
                                "&eTimes: %spawn_times%",
                                "&eCatch Rate: ",
                                "%catch_rate%"
                        )
                        .nbt("UIImage", new ConfigItem.NBTValue("string", "%sprite%"))
                        .nbt("UIImageR", new ConfigItem.NBTValue("float", "0"))
                        .nbt("UIImageG", new ConfigItem.NBTValue("float", "0"))
                        .nbt("UIImageB", new ConfigItem.NBTValue("float", "0"))
                        .nbt("UIImageA", new ConfigItem.NBTValue("float", "1"))
                        .build();

        public MissingPokemonUI() {}

        public ConfigInterface getGuiSettings() {
            return this.guiSettings;
        }

        public List<Integer> getMissingPokemonPositions() {
            return this.missingPokemonPositions;
        }

        public ExtendedConfigItem getBackButton() {
            return this.backButton;
        }

        public ExtendedConfigItem getPreviousPageButton() {
            return this.previousPageButton;
        }

        public ExtendedConfigItem getNextPageButton() {
            return this.nextPageButton;
        }

        public ConfigItem getMissingPokemonItem() {
            return this.missingPokemonItem;
        }
    }
    @ConfigSerializable
    public static class RanksUI {

        private ConfigInterface guiSettings = ConfigInterface.builder()
                .title("BetterDexRewards")
                .height(6)
                .fillType(ConfigInterface.FillType.BLOCK)
                .fillerItem(ConfigItem.builder()
                        .type("minecraft:black_stained_glass_pane")
                        .amount(1)
                        .name(" ")
                        .build())
                .build();

        private ExtendedConfigItem backButton = ExtendedConfigItem.builder()
                .type("pixelmon:eject_button")
                .amount(1)
                .name("&eBack")
                .positions(4, 5)
                .build();

        private ExtendedConfigItem previousPageButton = ExtendedConfigItem.builder()
                .type("pixelmon:trade_holder_left")
                .amount(1)
                .name("&ePrevious Page")
                .positions(0, 0)
                .build();

        private ExtendedConfigItem nextPageButton = ExtendedConfigItem.builder()
                .type("pixelmon:trade_holder_left")
                .amount(1)
                .name("&eNext Page")
                .positions(1, 0)
                .build();

        private int pages = 1;

        public RanksUI() {}

        public ConfigInterface getGuiSettings() {
            return this.guiSettings;
        }

        public ExtendedConfigItem getBackButton() {
            return this.backButton;
        }

        public int getPages() {
            return this.pages;
        }

        public ExtendedConfigItem getPreviousPageButton() {
            return this.previousPageButton;
        }

        public ExtendedConfigItem getNextPageButton() {
            return this.nextPageButton;
        }
    }
}
