package com.envyful.better.dex.rewards.forge.ui;

import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.api.forge.config.UtilConfigInterface;
import com.envyful.api.forge.config.UtilConfigItem;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.forge.player.util.UtilPlayer;
import com.envyful.api.forge.server.UtilForgeServer;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsConfig;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsGraphics;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class BetterDexRewardsUI {

    public static void open(ForgeEnvyPlayer player) {
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

        UtilConfigInterface.fillBackground(pane, config);
        UtilConfigItem.addConfigItem(pane, dexRewardsConfig.getBackButton(),
                (envyPlayer, clickType) -> UtilForgeConcurrency.runSync(() -> DexRewardsMainUI.open(player)));
        double percentage = attribute.getPokeDexPercentage();

        for (Map.Entry<String, BetterDexRewardsConfig.DexCompletion> entry : BetterDexRewards.getInstance().getConfig().getRewardStages().entrySet()) {
            ConfigItem configItem = null;

            if (attribute.hasClaimed(entry.getKey())) {
                configItem = entry.getValue().getCompleteItem();
            } else if (entry.getValue().getOptionalAntiClaimPermission() != null &&
                    UtilPlayer.hasPermission(player.getParent(), entry.getValue().getOptionalAntiClaimPermission())) {
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
                                     for (String msg : BetterDexRewards.getInstance().getConfig().getAlreadyClaimed()) {
                                         envyPlayer.message(msg);
                                     }
                                     return;
                                 }

                                 if (entry.getValue().getOptionalAntiClaimPermission() != null &&
                                         UtilPlayer.hasPermission(player.getParent(), entry.getValue().getOptionalAntiClaimPermission())) {
                                     for (String msg : BetterDexRewards.getInstance().getConfig().getAlreadyClaimed()) {
                                         envyPlayer.message(msg);
                                     }
                                     return;
                                 }

                                 if (percentage >= entry.getValue().getRequiredPercentage()) {
                                     attribute.claimReward(finalId, entry.getValue().getRewardCommands());
                                     for (String rewardMessage : entry.getValue().getRewardMessages()) {
                                         envyPlayer.message(UtilChatColour.colour(rewardMessage));
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
                                 } else {
                                     for (String msg : BetterDexRewards.getInstance().getConfig().getInsufficientPercentage()) {
                                         envyPlayer.message(msg);
                                     }
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
                .title(UtilChatColour.colour(config.getTitle()))
                .build().open(player);
    }
}
