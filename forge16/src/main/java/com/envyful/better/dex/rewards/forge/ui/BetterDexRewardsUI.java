package com.envyful.better.dex.rewards.forge.ui;

import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.api.forge.config.UtilConfigItem;
import com.envyful.api.forge.server.UtilForgeServer;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsConfig;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsGraphics;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class BetterDexRewardsUI {

    public static void open(EnvyPlayer<ServerPlayerEntity> player) {
        DexRewardsAttribute attribute = player.getAttribute(BetterDexRewards.class);

        if (attribute == null) {
            return;
        }

        BetterDexRewardsGraphics.RanksUI dexRewardsConfig = BetterDexRewards.getInstance().getGraphics().getRankUI();
        ConfigInterface config = dexRewardsConfig.getGuiSettings();
        Pane pane = GuiFactory.paneBuilder()
                .topLeftX(0)
                .topLeftY(0)
                .width(9)
                .height(config.getHeight())
                .build();

        for (ConfigItem fillerItem : config.getFillerItems()) {
            if (!fillerItem.isEnabled()) {
                continue;
            }

            pane.add(GuiFactory.displayable(UtilConfigItem.fromConfigItem(fillerItem)));
        }

        UtilConfigItem.addConfigItem(pane, BetterDexRewards.getInstance().getConfig().getBackButton(),
                (envyPlayer, clickType) -> DexRewardsMainUI.open(player));

        double percentage = attribute.getPokeDexPercentage();

        for (Map.Entry<String, BetterDexRewardsConfig.DexCompletion> entry : BetterDexRewards.getInstance().getConfig().getRewardStages().entrySet()) {
            ConfigItem configItem = null;

            if (attribute.hasClaimed(entry.getKey())) {
                configItem = entry.getValue().getCompleteItem();
            } else if (percentage < entry.getValue().getRequiredPercentage()) {
                configItem = entry.getValue().getDisplayItem();
            } else {
                configItem = entry.getValue().getToClaimItem();
            }

            final String finalId = entry.getKey();

            pane.set(entry.getValue().getxPos(), entry.getValue().getyPos(),
                     GuiFactory.displayableBuilder(ItemStack.class)
                             .itemStack(UtilConfigItem.fromConfigItem(configItem))
                             .clickHandler((envyPlayer, clickType) -> {
                                 if (attribute.hasClaimed(finalId)) {
                                     return;
                                 }

                                 if (percentage >= entry.getValue().getRequiredPercentage()) {
                                     attribute.claimReward(finalId);
                                     for (String rewardMessage : entry.getValue().getRewardMessages()) {
                                         envyPlayer.message(UtilChatColour.translateColourCodes('&', rewardMessage));
                                     }

                                     UtilForgeConcurrency.runSync(() -> {
                                         for (String rewardCommand : entry.getValue().getRewardCommands()) {
                                             UtilForgeServer.executeCommand(rewardCommand
                                                                                    .replace(
                                                                                            "%player%",
                                                                                            ((ServerPlayerEntity) envyPlayer.getParent()).getName().getString()
                                                                                    ));
                                         }

                                         ((ServerPlayerEntity) envyPlayer.getParent()).closeContainer();
                                     });
                                 }
                             })
                             .build()
            );
        }

        GuiFactory.guiBuilder()
                .addPane(pane)
                .setCloseConsumer(envyPlayer -> {})
                .setPlayerManager(BetterDexRewards.getInstance().getPlayerManager())
                .height(config.getHeight())
                .title(UtilChatColour.colour(config.getTitle()).getString())
                .build().open(player);
    }
}
