package com.envyful.better.dex.rewards.forge.ui;

import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.api.forge.config.UtilConfigItem;
import com.envyful.api.gui.Transformer;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsConfig;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import com.envyful.better.dex.rewards.forge.transformer.CompletionTransformer;
import com.envyful.better.dex.rewards.forge.transformer.PlaceholderAPITransformer;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

public class DexRewardsMainUI {

    public static void open(EnvyPlayer<EntityPlayerMP> player) {
        if (BetterDexRewards.getInstance().getConfig() == null) {
            return;
        }

        ConfigInterface config = BetterDexRewards.getInstance().getConfig().getConfigInterface();
        Pane pane = GuiFactory.paneBuilder()
                .topLeftX(0)
                .topLeftY(0)
                .width(9)
                .height(3)
                .build();

        DexRewardsAttribute attribute = player.getAttribute(BetterDexRewards.class);

        if (attribute == null) {
            return;
        }

        for (ConfigItem fillerItem : config.getFillerItems()) {
            pane.add(GuiFactory.displayable(UtilConfigItem.fromConfigItem(
                    fillerItem,
                    getTransformers(
                            player.getParent(),
                            attribute
                    )
            )));
        }

        BetterDexRewardsConfig actualConfig = BetterDexRewards.getInstance().getConfig();

        UtilConfigItem.addConfigItem(pane, getTransformers(player.getParent(), attribute),
                                     actualConfig.getPercentageItem());

        System.out.println("HELLO :)");

        UtilConfigItem.addConfigItem(pane, actualConfig.getRanksItem(), getTransformers(player.getParent(), attribute),
                                     (envyPlayer, clickType) -> UtilForgeConcurrency.runSync(() -> BetterDexRewardsUI.open((EnvyPlayer<EntityPlayerMP>) envyPlayer))
        );

        UtilConfigItem.addConfigItem(pane, actualConfig.getMissingItem(), getTransformers(player.getParent(),
                                                                                     attribute),
                                     (envyPlayer, clickType) -> UtilForgeConcurrency.runSync(() -> DexRewardsMissingUI.open((EnvyPlayer<EntityPlayerMP>) envyPlayer))
        );

        UtilConfigItem.addConfigItem(pane, BetterDexRewards.getInstance().getConfig().getInfoItem());

        GuiFactory.guiBuilder()
                .addPane(pane)
                .setCloseConsumer(envyPlayer -> {})
                .setPlayerManager(BetterDexRewards.getInstance().getPlayerManager())
                .height(3)
                .title(UtilChatColour.translateColourCodes('&', config.getTitle()))
                .build().open(player);
    }

    private static List<Transformer> getTransformers(EntityPlayerMP player, DexRewardsAttribute attribute) {
        if (!BetterDexRewards.getInstance().isPlaceholders()) {
            return Lists.newArrayList(CompletionTransformer.of(attribute.getPokeDexPercentage()));
        }

        return Lists.newArrayList(
                CompletionTransformer.of(attribute.getPokeDexPercentage()),
                PlaceholderAPITransformer.of(player)
        );
    }
}
