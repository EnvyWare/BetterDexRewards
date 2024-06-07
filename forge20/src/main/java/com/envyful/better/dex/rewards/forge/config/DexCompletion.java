package com.envyful.better.dex.rewards.forge.config;

import com.envyful.api.config.type.ExtendedConfigItem;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.envyful.api.forge.config.ConfigReward;
import com.envyful.api.forge.config.ConfigRewardPool;
import com.envyful.api.text.parse.SimplePlaceholder;
import com.envyful.better.dex.rewards.forge.config.comparator.RankComparator;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class DexCompletion extends AbstractYamlConfig implements SimplePlaceholder {

    private String id;
    private int page = 1;
    private ExtendedConfigItem displayItem;
    private ExtendedConfigItem completeItem;
    private ExtendedConfigItem toClaimItem;
    private RankComparator requiredDex;
    private ConfigRewardPool<ConfigReward> rewards;
    private String optionalAntiClaimPermission = null;

    protected DexCompletion(Builder builder) {
        this.id = builder.id;
        this.page = builder.page;
        this.displayItem = builder.displayItem;
        this.completeItem = builder.completeItem;
        this.toClaimItem = builder.toClaimItem;
        this.requiredDex = builder.requiredDex;
        this.rewards = builder.rewards;
        this.optionalAntiClaimPermission = builder.optionalAntiClaimPermission;
    }

    public DexCompletion() {
    }

    public String getId() {
        return this.id;
    }

    public ExtendedConfigItem getDisplayItem() {
        return this.displayItem;
    }

    public ExtendedConfigItem getCompleteItem() {
        return this.completeItem;
    }

    public ExtendedConfigItem getToClaimItem() {
        return this.toClaimItem;
    }

    public RankComparator getRequiredDex() {
        return this.requiredDex;
    }

    public ConfigRewardPool<ConfigReward> getRewards() {
        return this.rewards;
    }

    public String getOptionalAntiClaimPermission() {
        return this.optionalAntiClaimPermission;
    }

    public int getPage() {
        return this.page;
    }

    @Override
    public String replace(String s) {
        return s.replace("%id%", this.id);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String id;
        private int page;
        private ExtendedConfigItem displayItem;
        private ExtendedConfigItem completeItem;
        private ExtendedConfigItem toClaimItem;
        private RankComparator requiredDex;
        private ConfigRewardPool<ConfigReward> rewards;
        private String optionalAntiClaimPermission;

        public Builder() {
            super();
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder page(int page) {
            this.page = page;
            return this;
        }

        public Builder displayItem(ExtendedConfigItem displayItem) {
            this.displayItem = displayItem;
            return this;
        }

        public Builder completeItem(ExtendedConfigItem completeItem) {
            this.completeItem = completeItem;
            return this;
        }

        public Builder toClaimItem(ExtendedConfigItem toClaimItem) {
            this.toClaimItem = toClaimItem;
            return this;
        }

        public Builder requiredDex(RankComparator requiredDex) {
            this.requiredDex = requiredDex;
            return this;
        }

        public Builder rewards(ConfigRewardPool<ConfigReward> rewards) {
            this.rewards = rewards;
            return this;
        }

        public Builder optionalAntiClaimPermission(String optionalAntiClaimPermission) {
            this.optionalAntiClaimPermission = optionalAntiClaimPermission;
            return this;
        }

        public DexCompletion build() {
            return new DexCompletion(this);
        }
    }
}
