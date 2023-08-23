package com.envyful.better.dex.rewards.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.config.type.ExtendedConfigItem;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
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

        private ConfigInterface guiSettings = new ConfigInterface(
                "BetterDexRewards", 3, ConfigInterface.FillType.BLOCK.name(),
                ImmutableMap.of("one", ConfigItem.builder()
                                .type("minecraft:black_stained_glass_pane")
                                .amount(1)
                                .name(" ")
                                .build())
        );

        private ExtendedConfigItem percentageItem = new ExtendedConfigItem(
                "pixelmon:poke_ball",
                1, (byte) 0, "&eCurrent PokeDex Percentage",
                Lists.newArrayList("&eComplete: &a%percentage%"),
                1, 1,
                ImmutableMap.of("tooltip", new ConfigItem.NBTValue("string", ""))
        );

        private ExtendedConfigItem missingItem = new ExtendedConfigItem(
                "pixelmon:pokeradar",
                1, (byte) 0, "&eMissing Pokemon",
                Lists.newArrayList(""),
                5, 1,
                ImmutableMap.of("ndex", new ConfigItem.NBTValue("short", PixelmonSpecies.UNOWN.getValueUnsafe().getDex() +
                        ""))
        );

        private ExtendedConfigItem ranksItem = new ExtendedConfigItem(
                "pixelmon:master_ball",
                1, (byte) 0, "&ePokeDex Ranks",
                Lists.newArrayList(""),
                3, 1,
                Collections.emptyMap()
        );

        private ExtendedConfigItem infoItem = new ExtendedConfigItem(
                "minecraft:paper",
                1, (byte) 0, "Info",
                Lists.newArrayList(""),
                7, 1,
                Collections.emptyMap()
        );

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

        private ConfigInterface guiSettings = new ConfigInterface(
                "BetterDexRewards", 6, ConfigInterface.FillType.BLOCK.name(),
                ImmutableMap.of("one", ConfigItem.builder()
                        .type("minecraft:black_stained_glass_pane")
                        .amount(1)
                        .name(" ")
                        .build())
        );

        private List<Integer> missingPokemonPositions = Lists.newArrayList(
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 10, 11, 12, 13, 14, 15, 16, 17,
                18, 19, 20, 21, 22, 23, 24, 25, 26,
                27, 28, 29, 30, 31, 32, 33, 34, 35
        );

        private ExtendedConfigItem backButton = new ExtendedConfigItem(
                "pixelmon:eject_button",
                1, (byte) 0, "&eBack",
                Lists.newArrayList(),
                4, 5, Collections.emptyMap()
        );

        private ExtendedConfigItem previousPageButton = new ExtendedConfigItem(
                "pixelmon:trade_holder_left",
                1, (byte) 0, "&ePrevious Page",
                Lists.newArrayList(),
                0, 5, Collections.emptyMap()
        );

        private ExtendedConfigItem nextPageButton = new ExtendedConfigItem(
                "pixelmon:trade_holder_left",
                1, (byte) 0, "&eNext Page",
                Lists.newArrayList(),
                8, 5, Collections.emptyMap()
        );

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

        private ConfigInterface guiSettings = new ConfigInterface(
                "BetterDexRewards", 6, ConfigInterface.FillType.BLOCK.name(),
                ImmutableMap.of("one", ConfigItem.builder()
                        .type("minecraft:black_stained_glass_pane")
                        .amount(1)
                        .name(" ")
                        .build())
        );

        private ExtendedConfigItem backButton = new ExtendedConfigItem(
                "pixelmon:eject_button",
                1, (byte) 0, "&eBack",
                Lists.newArrayList(),
                4, 5, Collections.emptyMap()
        );

        private ExtendedConfigItem previousPageButton = new ExtendedConfigItem(
                "pixelmon:trade_holder_left",
                1, (byte) 0, "&ePrevious Page",
                Lists.newArrayList(),
                0, 0, Collections.emptyMap()
        );

        private ExtendedConfigItem nextPageButton = new ExtendedConfigItem(
                "pixelmon:trade_holder_right",
                1, (byte) 0, "&eNext Page",
                Lists.newArrayList(),
                1, 0, Collections.emptyMap()
        );

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
