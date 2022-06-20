package com.envyful.better.dex.rewards.forge;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.database.Database;
import com.envyful.api.database.impl.SimpleHikariDatabase;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.concurrency.ForgeTaskBuilder;
import com.envyful.api.forge.gui.factory.ForgeGuiFactory;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.forge.player.ForgePlayerManager;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.player.attribute.PlayerAttribute;
import com.envyful.better.dex.rewards.forge.command.BetterDexRewardsCommand;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsConfig;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsQueries;
import com.envyful.better.dex.rewards.forge.listener.DexRewardsListener;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import com.envyful.better.dex.rewards.forge.task.ReminderTask;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Mod("betterdexrewards")
public class BetterDexRewards {

    private static BetterDexRewards instance;

    private ForgeCommandFactory commandFactory = new ForgeCommandFactory();
    private ForgePlayerManager playerManager = new ForgePlayerManager();

    private Database database;
    private BetterDexRewardsConfig config;
    private boolean placeholders;

    public BetterDexRewards() {
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onInit(FMLServerAboutToStartEvent event) {
        this.reloadConfig();
        GuiFactory.setPlatformFactory(new ForgeGuiFactory());

        this.playerManager.registerAttribute(this, DexRewardsAttribute.class);

        this.checkForPlaceholders();

        new DexRewardsListener(this);

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

    @SubscribeEvent
    public void onServerStart(RegisterCommandsEvent event) {
        this.commandFactory.registerCommand(event.getDispatcher(), new BetterDexRewardsCommand());
    }

    private void checkForPlaceholders() {
        try {
            Class.forName("com.envyful.papi.forge.ForgePlaceholderAPI");
            this.placeholders = true;
        } catch (ClassNotFoundException e) {
            this.placeholders = false;
        }
    }

    @SubscribeEvent
    public void onServerShutdown(FMLServerStoppingEvent event) {
        for (ForgeEnvyPlayer onlinePlayer : this.playerManager.getOnlinePlayers()) {
            PlayerAttribute<BetterDexRewards> attribute = onlinePlayer.getAttribute(BetterDexRewards.class);

            if (attribute != null) {
                attribute.save();
            }
        }
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

    public boolean isPlaceholders() {
        return this.placeholders;
    }
}
