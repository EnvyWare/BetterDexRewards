package com.envyful.better.dex.rewards.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.envyful.better.dex.rewards.forge.ui.BetterDexRewardsUI;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigPath("config/BetterDexRewards/guis.yml")
@ConfigSerializable
public class BetterDexRewardsGraphics extends AbstractYamlConfig {

    private BetterDexRewardsUI uiSettings = new BetterDexRewardsUI();

    public BetterDexRewardsGraphics() {}

    public BetterDexRewardsUI getUiSettings() {
        return uiSettings;
    }
}
