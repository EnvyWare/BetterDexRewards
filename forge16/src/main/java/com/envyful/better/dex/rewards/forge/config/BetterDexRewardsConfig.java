package com.envyful.better.dex.rewards.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.config.type.PositionableConfigItem;
import com.envyful.api.config.type.SQLDatabaseDetails;
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
import java.util.Map;

@ConfigPath("config/BetterDexRewards/config.yml")
@ConfigSerializable
public class BetterDexRewardsConfig extends AbstractYamlConfig {

    private SQLDatabaseDetails database = new SQLDatabaseDetails("BetterDexRewards", "0.0.0.0", 3306,
                                                                 "admin", "password", "BetterDexRewards"
    );

    private boolean requiresOriginalTrainerToReward = false;

    private ConfigInterface configInterface = new ConfigInterface();

    private PositionableConfigItem infoItem = new PositionableConfigItem(
            Items.PAPER.getRegistryName().toString(),
            1, (byte) 0, "Info",
            Lists.newArrayList(""),
            7, 1,
            Collections.emptyMap()
    );

    private PositionableConfigItem missingItem = new PositionableConfigItem(
            "4601",
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

    private PositionableConfigItem percentageItem = new PositionableConfigItem(
            "pixelmon:poke_ball",
            1, (byte) 0, "&eCurrent PokeDex Percentage",
            Lists.newArrayList("&eComplete: &a%percentage%"),
            1, 1,
            ImmutableMap.of("tooltip", new ConfigItem.NBTValue("string", ""))
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

    private int messageDelaySeconds = 60;

    private Map<String, DexCompletion> rewardStages = Maps.newHashMap(ImmutableMap.of(
            "one", new DexCompletion(1, 1, new ConfigItem(), new ConfigItem(), new ConfigItem(), 1.0,
                                     Lists.newArrayList("give %player% minecraft:diamond 1"),
                                     Lists.newArrayList("&e&l(!) &eYou have completed 1% of the dex!")
            )
    ));

    private List<String> claimReminderMessage = Lists.newArrayList(
            "&e&l(!) &eYou have a PokeDex reward level you can claim!"
    );

    private List<String> claimUpdateMessage = Lists.newArrayList(
            "&e&l(!) &eYou have a new PokeDex reward level you can claim!"
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

    public BetterDexRewardsConfig() {
    }

    public PositionableConfigItem getBackButton() {
        return this.backButton;
    }

    public PositionableConfigItem getInfoItem() {
        return this.infoItem;
    }

    public SQLDatabaseDetails getDatabase() {
        return this.database;
    }

    public boolean getRequiresOriginalTrainerToReward() {
        return this.requiresOriginalTrainerToReward;
    }

    public Map<String, DexCompletion> getRewardStages() {
        return this.rewardStages;
    }

    public List<String> getClaimReminderMessage() {
        return this.claimReminderMessage;
    }

    public List<String> getClaimUpdateMessage() {
        return this.claimUpdateMessage;
    }

    public ConfigInterface getConfigInterface() {
        return this.configInterface;
    }

    public int getMessageDelaySeconds() {
        return this.messageDelaySeconds;
    }

    public PositionableConfigItem getMissingItem() {
        return this.missingItem;
    }

    public PositionableConfigItem getRanksItem() {
        return this.ranksItem;
    }

    public PositionableConfigItem getPercentageItem() {
        return this.percentageItem;
    }

    public ConfigItem getMissingPokemonItem() {
        return this.missingPokemonItem;
    }

    public PositionableConfigItem getPreviousPageButton() {
        return this.previousPageButton;
    }

    public PositionableConfigItem getNextPageButton() {
        return this.nextPageButton;
    }

    @ConfigSerializable
    public static class DexCompletion {

        private int xPos;
        private int yPos;
        private ConfigItem displayItem;
        private ConfigItem completeItem;
        private ConfigItem toClaimItem;
        private double requiredPercentage;
        private List<String> rewardCommands;
        private List<String> rewardMessages;

        protected DexCompletion(int xPos, int yPos, ConfigItem displayItem, ConfigItem completeItem,
                                ConfigItem toClaimItem, double requiredPercentage,
                                List<String> rewardCommands, List<String> rewardMessages) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.displayItem = displayItem;
            this.completeItem = completeItem;
            this.toClaimItem = toClaimItem;
            this.requiredPercentage = requiredPercentage;
            this.rewardCommands = rewardCommands;
            this.rewardMessages = rewardMessages;
        }

        public DexCompletion() {}

        public int getxPos() {
            return this.xPos;
        }

        public int getyPos() {
            return this.yPos;
        }

        public ConfigItem getDisplayItem() {
            return this.displayItem;
        }

        public ConfigItem getCompleteItem() {
            return this.completeItem;
        }

        public ConfigItem getToClaimItem() {
            return this.toClaimItem;
        }

        public double getRequiredPercentage() {
            return this.requiredPercentage;
        }

        public List<String> getRewardCommands() {
            return this.rewardCommands;
        }

        public List<String> getRewardMessages() {
            return this.rewardMessages;
        }
    }
}
