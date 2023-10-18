package com.envyful.better.dex.rewards.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.config.type.ConfigRandomWeightedSet;
import com.envyful.api.config.type.SQLDatabaseDetails;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.envyful.api.forge.config.ConfigReward;
import com.envyful.api.forge.config.ConfigRewardPool;
import com.envyful.api.player.SaveMode;
import com.google.common.collect.Lists;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.List;
import java.util.Map;

@ConfigPath("config/BetterDexRewards/config.yml")
@ConfigSerializable
public class BetterDexRewardsConfig extends AbstractYamlConfig {

    @Comment("""
            The setting to tell the mod how to save the player data.
            The options are:
            - JSON
            - MYSQL
            """)
    private SaveMode saveMode = SaveMode.JSON;

    @Comment("""
            The MySQL database details.
            This will only be used if the save mode is set to MYSQL
            
            NOTE: DO NOT SHARE THESE WITH ANYONE YOU DO NOT TRUST
            """)
    private SQLDatabaseDetails database = new SQLDatabaseDetails("BetterDexRewards", "0.0.0.0", 3306,
                                                                 "admin", "password", "BetterDexRewards"
    );

    @Comment("""
            The delay in seconds between reminders that the player has an unclaimed reward
            """)
    private int messageDelaySeconds = 60;

    @Comment("""
            The setting to prevent pokemon that are not originally captured by the player
            counting towards their pokedex
            """)
    private boolean originalTrainerRewardsOnly = false;

    private Map<String, DexCompletion> rewardStages = Map.of(
            "one", new DexCompletion(1, 1, ConfigItem.builder().build(), ConfigItem.builder().build(), ConfigItem.builder().build(), 1.0,
                                     ConfigRewardPool.builder(new ConfigReward(
                                                     Lists.newArrayList("give %player% minecraft:diamond 1"),
                                                     Lists.newArrayList("You've completed 1% of the dex!")))
                                             .minRolls(1).maxRolls(1)
                                             .rewards(new ConfigRandomWeightedSet<>(new ConfigRandomWeightedSet.WeightedObject<>(1,
                                                     new ConfigReward(Lists.newArrayList("Hey %player%"), Lists.newArrayList("Hey %player%")))))
                                             .build()
            )
    );

    private List<String> claimReminderMessage = Lists.newArrayList(
            "&e&l(!) &eYou have a PokeDex reward level you can claim!"
    );

    private List<String> claimUpdateMessage = Lists.newArrayList(
            "&e&l(!) &eYou have a new PokeDex reward level you can claim!"
    );

    private List<String> alreadyClaimed = Lists.newArrayList(
            "&c&l(!) &cYou've already claimed that tier"
    );

    private List<String> insufficientPercentage = Lists.newArrayList(
            "&c&l(!) &cYou do not have enough dex percentage to claim this"
    );

    public BetterDexRewardsConfig() {
    }

    public SaveMode getSaveMode() {
        return this.saveMode;
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

    public int getMessageDelaySeconds() {
        return this.messageDelaySeconds;
    }

    public boolean isOriginalTrainerRewardsOnly() {
        return this.originalTrainerRewardsOnly;
    }

    public List<String> getAlreadyClaimed() {
        return this.alreadyClaimed;
    }

    public List<String> getInsufficientPercentage() {
        return this.insufficientPercentage;
    }

    @ConfigSerializable
    public static class DexCompletion {

        private int xPos;
        private int yPos;
        private int page = 1;
        private ConfigItem displayItem;
        private ConfigItem completeItem;
        private ConfigItem toClaimItem;
        private double requiredPercentage;
        private ConfigRewardPool rewards;
        private String optionalAntiClaimPermission = null;

        protected DexCompletion(int xPos, int yPos, ConfigItem displayItem, ConfigItem completeItem,
                                ConfigItem toClaimItem, double requiredPercentage, ConfigRewardPool rewards) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.displayItem = displayItem;
            this.completeItem = completeItem;
            this.toClaimItem = toClaimItem;
            this.requiredPercentage = requiredPercentage;
            this.rewards = rewards;
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

        public ConfigRewardPool getRewards() {
            return this.rewards;
        }

        public String getOptionalAntiClaimPermission() {
            return this.optionalAntiClaimPermission;
        }

        public int getPage() {
            return this.page;
        }
    }
}
