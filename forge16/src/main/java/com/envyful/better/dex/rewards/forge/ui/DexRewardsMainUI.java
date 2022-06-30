package com.envyful.better.dex.rewards.forge.ui;

import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.api.forge.config.UtilConfigItem;
import com.envyful.api.gui.Transformer;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsGraphics;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import com.envyful.better.dex.rewards.forge.transformer.CompletionTransformer;
import com.envyful.better.dex.rewards.forge.transformer.PlaceholderAPITransformer;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.List;

public class DexRewardsMainUI {

    public static void open(EnvyPlayer<ServerPlayerEntity> player) {
        if (BetterDexRewards.getInstance().getConfig() == null) {
            return;
        }

        BetterDexRewardsGraphics.MainUI config = BetterDexRewards.getInstance().getGraphics().getMainUI();
        Pane pane = GuiFactory.paneBuilder()
                .topLeftX(0)
                .topLeftY(0)
                .width(9)
                .height(config.getGuiSettings().getHeight())
                .build();

        DexRewardsAttribute attribute = player.getAttribute(BetterDexRewards.class);

        if (attribute == null) {
            return;
        }

        for (ConfigItem fillerItem : config.getGuiSettings().getFillerItems()) {
            if (!fillerItem.isEnabled()) {
                continue;
            }

            pane.add(GuiFactory.displayable(UtilConfigItem.fromConfigItem(
                    fillerItem,
                    getTransformers(
                            player.getParent(),
                            attribute
                    )
            )));
        }

        UtilConfigItem.addConfigItem(pane, getTransformers(player.getParent(), attribute), config.getPercentageItem());
        UtilConfigItem.addConfigItem(pane, config.getRanksItem(), getTransformers(player.getParent(), attribute),
                                     (envyPlayer, clickType) -> UtilForgeConcurrency.runSync(() -> BetterDexRewardsUI.open((EnvyPlayer<ServerPlayerEntity>) envyPlayer)));
        UtilConfigItem.addConfigItem(pane, config.getMissingItem(), getTransformers(player.getParent(), attribute),
                                     (envyPlayer, clickType) -> UtilForgeConcurrency.runSync(() -> DexRewardsMissingUI.open((EnvyPlayer<ServerPlayerEntity>) envyPlayer)));
        UtilConfigItem.addConfigItem(pane, config.getInfoItem());

        GuiFactory.guiBuilder()
                .addPane(pane)
                .setCloseConsumer(envyPlayer -> {})
                .setPlayerManager(BetterDexRewards.getInstance().getPlayerManager())
                .height(config.getGuiSettings().getHeight())
                .title(UtilChatColour.colour(config.getGuiSettings().getTitle()))
                .build()
                .open(player);
    }

    private static List<Transformer> getTransformers(ServerPlayerEntity player, DexRewardsAttribute attribute) {
        if (!BetterDexRewards.getInstance().isPlaceholders()) {
            return Lists.newArrayList(CompletionTransformer.of(attribute.getPokeDexPercentage()));
        }

        return Lists.newArrayList(
                CompletionTransformer.of(attribute.getPokeDexPercentage()),
                PlaceholderAPITransformer.of(player)
        );
    }
}
