package com.envyful.better.dex.rewards.forge;

import com.envyful.api.concurrency.UtilLogger;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.database.Database;
import com.envyful.api.database.impl.SimpleHikariDatabase;
import com.envyful.api.database.sql.UtilSql;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.command.parser.ForgeAnnotationCommandParser;
import com.envyful.api.forge.concurrency.ForgeTaskBuilder;
import com.envyful.api.forge.gui.factory.ForgeGuiFactory;
import com.envyful.api.forge.player.ForgePlayerManager;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.player.SaveMode;
import com.envyful.api.player.save.impl.JsonSaveManager;
import com.envyful.better.dex.rewards.forge.command.BetterDexRewardsCommand;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsConfig;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsGraphics;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsQueries;
import com.envyful.better.dex.rewards.forge.config.comparator.RankComparatorRegistry;
import com.envyful.better.dex.rewards.forge.listener.DexRewardsListener;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import com.envyful.better.dex.rewards.forge.task.ReminderTask;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Mod("betterdexrewards")
public class BetterDexRewards {

    private static final Logger LOGGER = LogManager.getLogger("betterdexrewards");

    private static BetterDexRewards instance;

    private final ForgePlayerManager playerManager = new ForgePlayerManager();
    private final ForgeCommandFactory commandFactory = new ForgeCommandFactory(ForgeAnnotationCommandParser::new, playerManager);

    private Database database;
    private BetterDexRewardsConfig config;
    private BetterDexRewardsGraphics graphics;
    private boolean placeholders;

    public BetterDexRewards() {
        RankComparatorRegistry.init();
        UtilLogger.setLogger(LOGGER);
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }

    @SubscribeEvent
    public void onInit(FMLServerAboutToStartEvent event) {
        this.reloadConfig();
        GuiFactory.setPlatformFactory(new ForgeGuiFactory());

        if (this.config.getSaveMode() == SaveMode.JSON) {
            this.playerManager.setSaveManager(new JsonSaveManager<>(playerManager));
        }

        this.playerManager.registerAttribute(DexRewardsAttribute.class);

        this.checkForPlaceholders();

        new DexRewardsListener(this);

        if (this.config.getSaveMode() == SaveMode.MYSQL) {
            this.database = new SimpleHikariDatabase(this.config.getDatabase());

            UtilSql.update(this.database)
                    .query(BetterDexRewardsQueries.CREATE_TABLE)
                    .executeAsync();
        }

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
            this.graphics = YamlConfigFactory.getInstance(BetterDexRewardsGraphics.class);
        } catch (IOException e) {
            getLogger().error("Error loading configs", e);
        }
    }

    @SubscribeEvent
    public void onServerStart(RegisterCommandsEvent event) {
        this.commandFactory.registerCommand(event.getDispatcher(), this.commandFactory.parseCommand(new BetterDexRewardsCommand()));
    }

    private void checkForPlaceholders() {
        try {
            Class.forName("com.envyful.papi.forge.ForgePlaceholderAPI");
            this.placeholders = true;
        } catch (ClassNotFoundException e) {
            this.placeholders = false;
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

    public BetterDexRewardsGraphics getGraphics() {
        return this.graphics;
    }

    public boolean isPlaceholders() {
        return this.placeholders;
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
