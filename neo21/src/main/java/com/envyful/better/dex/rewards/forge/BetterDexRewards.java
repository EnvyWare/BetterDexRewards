package com.envyful.better.dex.rewards.forge;

import com.envyful.api.concurrency.UtilLogger;
import com.envyful.api.config.ConfigTypeSerializer;
import com.envyful.api.config.database.DatabaseDetailsConfig;
import com.envyful.api.config.database.DatabaseDetailsRegistry;
import com.envyful.api.config.type.SQLDatabaseDetails;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.database.Database;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.neoforge.chat.ComponentTextFormatter;
import com.envyful.api.neoforge.command.ForgeCommandFactory;
import com.envyful.api.neoforge.command.parser.ForgeAnnotationCommandParser;
import com.envyful.api.neoforge.concurrency.ForgeTaskBuilder;
import com.envyful.api.neoforge.gui.factory.ForgeGuiFactory;
import com.envyful.api.neoforge.platform.ForgePlatformHandler;
import com.envyful.api.neoforge.player.ForgeEnvyPlayer;
import com.envyful.api.neoforge.player.ForgePlayerManager;
import com.envyful.api.platform.PlatformProxy;
import com.envyful.api.player.Attribute;
import com.envyful.api.sqlite.config.SQLiteDatabaseDetailsConfig;
import com.envyful.better.dex.rewards.forge.command.BetterDexRewardsCommand;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsConfig;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsGraphics;
import com.envyful.better.dex.rewards.forge.config.comparator.RankComparator;
import com.envyful.better.dex.rewards.forge.config.comparator.RankComparatorRegistry;
import com.envyful.better.dex.rewards.forge.config.comparator.RankComparatorTypeSerializer;
import com.envyful.better.dex.rewards.forge.listener.DexRewardsListener;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import com.envyful.better.dex.rewards.forge.player.SQLAttributeAdapter;
import com.envyful.better.dex.rewards.forge.player.SQLiteAttributeAdapter;
import com.envyful.better.dex.rewards.forge.task.ReminderTask;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
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
        SQLiteDatabaseDetailsConfig.register();
        PlatformProxy.setHandler(ForgePlatformHandler.getInstance());
        PlatformProxy.setPlayerManager(this.playerManager);
        PlatformProxy.setTextFormatter(ComponentTextFormatter.getInstance());
        GuiFactory.setPlatformFactory(new ForgeGuiFactory());

        RankComparatorRegistry.init();
        UtilLogger.setLogger(LOGGER);
        instance = this;
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onInit(ServerAboutToStartEvent event) {
        this.reloadConfig();

        this.playerManager.registerAttribute(Attribute.builder(DexRewardsAttribute.class, ForgeEnvyPlayer.class)
                .constructor(DexRewardsAttribute::new)
                .registerAdapter(SQLDatabaseDetails.ID, new SQLAttributeAdapter())
                .registerAdapter(SQLiteDatabaseDetailsConfig.ID, new SQLiteAttributeAdapter())
        );

        this.playerManager.setGlobalSaveMode(DatabaseDetailsRegistry.getRegistry().getKey((Class<DatabaseDetailsConfig>) this.getConfig().getDatabase().getClass()));
        this.database = this.config.getDatabase().createDatabase();
        this.playerManager.getAdapter(DexRewardsAttribute.class).initialize();
        this.checkForPlaceholders();

        new DexRewardsListener(this);

        new ForgeTaskBuilder()
                .async(true)
                .delay(10L)
                .interval(10L)
                .task(new ReminderTask(this))
                .start();
    }

    public void reloadConfig() {
        try {
            ConfigTypeSerializer.register(new RankComparatorTypeSerializer(), RankComparator.class);

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
