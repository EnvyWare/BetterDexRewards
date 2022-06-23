package com.envyful.better.dex.rewards.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.config.type.PositionableConfigItem;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

        private ConfigInterface guiSettings = new ConfigInterface(
                "BetterDexRewards", 3, ConfigInterface.FillType.BLOCK.name(),
                ImmutableMap.of("one", new ConfigItem(
                        "minecraft:black_stained_glass_pane", 1, (byte)0, " ", Lists.newArrayList(), Maps.newHashMap()
                ))
        );

        private PositionableConfigItem percentageItem = new PositionableConfigItem(
                "pixelmon:poke_ball",
                1, (byte) 0, "&eCurrent PokeDex Percentage",
                Lists.newArrayList("&eComplete: &a%percentage%"),
                1, 1,
                ImmutableMap.of("tooltip", new ConfigItem.NBTValue("string", ""))
        );

        private PositionableConfigItem missingItem = new PositionableConfigItem(
                "pixelmon:pokeradar",
                1, (byte) 0, "&eMissing Pokemon",
                Lists.newArrayList(""),
                5, 1,
                ImmutableMap.of("ndex", new ConfigItem.NBTValue("short", PixelmonSpecies.UNOWN.getValueUnsafe().getDex() +
                        ""))
        );

        private PositionableConfigItem ranksItem = new PositionableConfigItem(
                "4414",
                1, (byte) 0, "&ePokeDex Ranks",
                Lists.newArrayList(""),
                3, 1,
                Collections.emptyMap()
        );

        private PositionableConfigItem infoItem = new PositionableConfigItem(
                Items.PAPER.getRegistryName().toString(),
                1, (byte) 0, "Info",
                Lists.newArrayList(""),
                7, 1,
                Collections.emptyMap()
        );

        public MainUI() {}

        public ConfigInterface getGuiSettings() {
            return this.guiSettings;
        }

        public PositionableConfigItem getPercentageItem() {
            return this.percentageItem;
        }

        public PositionableConfigItem getMissingItem() {
            return this.missingItem;
        }

        public PositionableConfigItem getRanksItem() {
            return this.ranksItem;
        }

        public PositionableConfigItem getInfoItem() {
            return this.infoItem;
        }
    }

    @ConfigSerializable
    public static class MissingPokemonUI {

        private ConfigInterface guiSettings = new ConfigInterface(
                "BetterDexRewards", 6, ConfigInterface.FillType.BLOCK.name(),
                ImmutableMap.of("one", new ConfigItem(
                        "minecraft:black_stained_glass_pane", 1, (byte)0, " ", Lists.newArrayList(), Maps.newHashMap()
                ))
        );

        private List<Integer> missingPokemonPositions = Lists.newArrayList(
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 10, 11, 12, 13, 14, 15, 16, 17,
                18, 19, 20, 21, 22, 23, 24, 25, 26,
                27, 28, 29, 30, 31, 32, 33, 34, 35
        );

        private PositionableConfigItem backButton = new PositionableConfigItem(
                PixelmonItems.trade_holder_left.getRegistryName().toString(),
                1, (byte) 0, "&eBack",
                Lists.newArrayList(),
                4, 5, Collections.emptyMap()
        );

        private PositionableConfigItem previousPageButton = new PositionableConfigItem(
                PixelmonItems.trade_holder_left.getRegistryName().toString(),
                1, (byte) 0, "&ePrevious Page",
                Lists.newArrayList(),
                0, 5, Collections.emptyMap()
        );

        private PositionableConfigItem nextPageButton = new PositionableConfigItem(
                PixelmonItems.trade_holder_left.getRegistryName().toString(),
                1, (byte) 0, "&eNext Page",
                Lists.newArrayList(),
                8, 5, Collections.emptyMap()
        );

        private ConfigItem missingPokemonItem = new ConfigItem(
                "pixelmon:ui_element", 1, (byte) 0, "&e%pokemon% §f- %pokedex%",
                Lists.newArrayList(
                        "&eBiomes",
                        "&f%biomes%",
                        " ",
                        "&eTimes: %spawn_times%",
                        "&eCatch Rate: ",
                        "%catch_rate%"
                ),
                ImmutableMap.of(
                        "UIImage", new ConfigItem.NBTValue("string", "%sprite%"),
                        "UIImageR", new ConfigItem.NBTValue("float", "0"),
                        "UIImageG", new ConfigItem.NBTValue("float", "0"),
                        "UIImageB", new ConfigItem.NBTValue("float", "0"),
                        "UIImageA", new ConfigItem.NBTValue("float", "1")
                )
        );

        public MissingPokemonUI() {}

        public ConfigInterface getGuiSettings() {
            return this.guiSettings;
        }

        public List<Integer> getMissingPokemonPositions() {
            return this.missingPokemonPositions;
        }

        public PositionableConfigItem getBackButton() {
            return this.backButton;
        }

        public PositionableConfigItem getPreviousPageButton() {
            return this.previousPageButton;
        }

        public PositionableConfigItem getNextPageButton() {
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
                ImmutableMap.of("one", new ConfigItem(
                        "minecraft:black_stained_glass_pane", 1, (byte)0, " ", Lists.newArrayList(), Maps.newHashMap()
                ))
        );

        private PositionableConfigItem backButton = new PositionableConfigItem(
                PixelmonItems.trade_holder_left.getRegistryName().toString(),
                1, (byte) 0, "&eBack",
                Lists.newArrayList(),
                4, 5, Collections.emptyMap()
        );

        public RanksUI() {}

        public ConfigInterface getGuiSettings() {
            return this.guiSettings;
        }

        public PositionableConfigItem getBackButton() {
            return this.backButton;
        }
    }
}
