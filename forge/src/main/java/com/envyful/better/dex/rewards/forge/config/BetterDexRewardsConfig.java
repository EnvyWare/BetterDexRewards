package com.envyful.better.dex.rewards.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.config.type.SQLDatabaseDetails;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.Map;

@ConfigPath("config/BetterDexRewards/config.yml")
@ConfigSerializable
public class BetterDexRewardsConfig extends AbstractYamlConfig {

    private SQLDatabaseDetails database = new SQLDatabaseDetails("BetterDexRewards", "0.0.0.0", 3306,
            "admin", "password", "BetterDexRewards");

    private ConfigInterface configInterface = new ConfigInterface();

    private Map<String, DexCompletion> rewardStages = Maps.newHashMap(ImmutableMap.of(
            "one", new DexCompletion(1, 1, new ConfigItem(), 1.0,
                    Lists.newArrayList("give %player% minecraft:diamond 1"),
                    Lists.newArrayList("&e&l(!) &eYou have completed 1% of the dex!"))
    ));

    private List<String> claimReminderMessage = Lists.newArrayList(
            "&e&l(!) &eYou have a PokeDex reward level you can claim!"
    );

    private List<String> claimUpdateMessage = Lists.newArrayList(
            "&e&l(!) &eYou have a new PokeDex reward level you can claim!"
    );

    public BetterDexRewardsConfig() {
    }

    public SQLDatabaseDetails getDatabase() {
        return this.database;
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

    @ConfigSerializable
    public static class DexCompletion {

        private int xPos;
        private int yPos;
        private ConfigItem displayItem;
        private double requiredPercentage;
        private List<String> rewardCommands;
        private List<String> rewardMessages;

        public DexCompletion(int xPos, int yPos, ConfigItem displayItem, double requiredPercentage,
                             List<String> rewardCommands, List<String> rewardMessages) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.displayItem = displayItem;
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
