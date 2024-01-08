package com.envyful.better.dex.rewards.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.type.ExtendedConfigItem;
import com.envyful.api.config.type.SQLDatabaseDetails;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.envyful.api.config.yaml.DefaultConfig;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.config.ConfigReward;
import com.envyful.api.forge.config.ConfigRewardPool;
import com.envyful.api.player.SaveMode;
import com.envyful.api.type.Pair;
import com.google.common.collect.Lists;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.io.IOException;
import java.util.List;

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

    private transient List<DexCompletion> rewardStages = Lists.newArrayList();

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

    public BetterDexRewardsConfig() throws IOException {
        super();

        this.rewardStages.addAll(YamlConfigFactory.getInstances(com.envyful.better.dex.rewards.forge.config.DexCompletion.class, "config/BetterDexRewards/rewards/",
                DefaultConfig.onlyNew("one.yml", com.envyful.better.dex.rewards.forge.config.DexCompletion.builder()
                        .id("one")
                        .page(1)
                        .requiredPercentage(20)
                        .displayItem(ExtendedConfigItem.builder()
                                .type("pixelmon:poke_ball")
                                .name("&e&lDex Reward &7- &e%percentage%%")
                                .positions(Pair.of(2, 2))
                                .lore(
                                        "&7&m---------------------",
                                        "&e&l* &e&lRewards:",
                                        "&e&l* &e&l- &e&l1x Diamond",
                                        "&7&m---------------------"
                                )
                                .build())
                        .completeItem(ExtendedConfigItem.builder()
                                .type("minecraft:barrier")
                                .name("&e&lClaimed!")
                                .positions(Pair.of(2, 2))
                                .lore()
                                .build())
                        .toClaimItem(ExtendedConfigItem.builder()
                                .type("pixelmon:poke_ball")
                                .name("&e&lDex Reward &7- &e%percentage%%")
                                .positions(Pair.of(2, 2))
                                .lore(
                                        "&7&m---------------------",
                                        "&e&l* &e&lRewards:",
                                        "&e&l* &e&l- &e&l1x Diamond",
                                        "&7&m---------------------"
                                )
                                .build())
                        .rewards(ConfigRewardPool.builder(ConfigReward.builder()
                                .commands(Lists.newArrayList("give %player% minecraft:diamond 1"))
                                .messages(Lists.newArrayList("&e&l(!) &eYou've claimed your Dex reward!"))
                                .build()).build())
                        .build()
                )));
    }

    public SaveMode getSaveMode() {
        return this.saveMode;
    }

    public SQLDatabaseDetails getDatabase() {
        return this.database;
    }

    public List<DexCompletion> getRewardStages() {
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
}
