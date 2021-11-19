package com.envyful.better.dex.rewards.forge;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.database.Database;
import com.envyful.api.database.impl.SimpleHikariDatabase;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.concurrency.ForgeTaskBuilder;
import com.envyful.api.forge.gui.factory.ForgeGuiFactory;
import com.envyful.api.forge.player.ForgePlayerManager;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.better.dex.rewards.forge.command.BetterDexRewardsCommand;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsConfig;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsQueries;
import com.envyful.better.dex.rewards.forge.listener.DexRewardsListener;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import com.envyful.better.dex.rewards.forge.task.ReminderTask;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Mod(
        modid = "betterdexrewards",
        name = "BetterDexRewards Forge",
        version = BetterDexRewards.VERSION,
        acceptableRemoteVersions = "*"
)
public class BetterDexRewards {

    protected static final String VERSION = "1.5.0";

    private static BetterDexRewards instance;

    private ForgeCommandFactory commandFactory = new ForgeCommandFactory();
    private ForgePlayerManager playerManager = new ForgePlayerManager();

    private Database database;
    private BetterDexRewardsConfig config;

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        instance = this;

        this.reloadConfig();
        GuiFactory.setPlatformFactory(new ForgeGuiFactory());

        this.playerManager.registerAttribute(this, DexRewardsAttribute.class);

        UtilConcurrency.runAsync(() -> {
            this.database = new SimpleHikariDatabase(this.config.getDatabase());

            try (Connection connection = this.database.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(BetterDexRewardsQueries.CREATE_TABLE)) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        new ForgeTaskBuilder()
                .async(true)
                .delay(10L)
                .interval(10L)
                .task(new ReminderTask(this))
                .start();
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
        new DexRewardsListener(this);

        this.commandFactory.registerCommand(event.getServer(), new BetterDexRewardsCommand());
    }

    public static BetterDexRewards getInstance() {
        return instance;
    }

    public ForgePlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public Database getDatabase() {
        return this.database;
    }

    public BetterDexRewardsConfig getConfig() {
        return this.config;
    }
}
