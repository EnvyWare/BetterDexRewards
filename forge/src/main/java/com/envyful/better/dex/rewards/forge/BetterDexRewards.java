package com.envyful.better.dex.rewards.forge;

import com.envyful.api.forge.command.ForgeCommandFactory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

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

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        instance = this;
    }

    public void reloadConfig() {

    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
    }

    public static BetterDexRewards getInstance() {
        return instance;
    }
}
