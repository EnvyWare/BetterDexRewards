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
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import javax.swing.text.Position;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ConfigPath("config/BetterDexRewards/config.yml")
@ConfigSerializable
public class BetterDexRewardsConfig extends AbstractYamlConfig {

    private SQLDatabaseDetails database = new SQLDatabaseDetails("BetterDexRewards", "0.0.0.0", 3306,
                                                                 "admin", "password", "BetterDexRewards"
    );

    private ConfigInterface configInterface = new ConfigInterface();

    private PositionableConfigItem infoItem = new PositionableConfigItem(
            Item.getIdFromItem(Items.PAPER) + "",
            1, (byte) 0, "Info",
            Lists.newArrayList(""),
            7, 1,
            Collections.emptyMap()
    );

    private PositionableConfigItem missingItem = new PositionableConfigItem(
            Item.getIdFromItem(PixelmonItems.itemPixelmonSprite) + "",
            1, (byte) 0, "&eMissing Pokemon",
            Lists.newArrayList(""),
            5, 1,
            ImmutableMap.of("ndex", new ConfigItem.NBTValue("byte", EnumSpecies.Unown.getNationalPokedexInteger() + ""))
    );

    private PositionableConfigItem ranksItem = new PositionableConfigItem(
            Item.getIdFromItem(PixelmonItems.itemFinder) + "",
            1, (byte) 0, "&ePokeDex Ranks",
            Lists.newArrayList(""),
            3, 1,
            Collections.emptyMap()
    );

    private PositionableConfigItem percentageItem = new PositionableConfigItem(
            Item.getIdFromItem(PixelmonItemsPokeballs.pokeBall) + "",
            1, (byte) 0, "&eCurrent PokeDex Percentage",
            Lists.newArrayList("&eComplete: &a%percentage%"),
            1, 1,
            ImmutableMap.of("tooltip", new ConfigItem.NBTValue("string", ""))
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

    public BetterDexRewardsConfig() {
    }

    public PositionableConfigItem getInfoItem() {
        return this.infoItem;
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
