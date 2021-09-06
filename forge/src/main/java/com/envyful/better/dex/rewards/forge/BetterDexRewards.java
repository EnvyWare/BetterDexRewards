package com.envyful.better.dex.rewards.forge;

import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.IOException;

@Mod(
        modid = "betterdexrewards",
        name = "BetterDexRewards Forge",
        version = BetterDexRewards.VERSION,
        acceptableRemoteVersions = "*"
)
public class BetterDexRewards {

    protected static final String VERSION = "0.1.0";

    private static BetterDexRewards instance;

    private ForgeCommandFactory commandFactory = new ForgeCommandFactory();

    private BetterDexRewardsConfig config;

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        instance = this;

        this.reloadConfig();
    }

    public void reloadConfig() {
        try {
            this.config = YamlConfigFactory.getInstance(BetterDexRewardsConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
    }

    public static BetterDexRewards getInstance() {
        return instance;
    }
}
