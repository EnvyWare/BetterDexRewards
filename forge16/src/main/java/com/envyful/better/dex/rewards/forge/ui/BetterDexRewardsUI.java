package com.envyful.better.dex.rewards.forge.ui;

import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.config.UtilConfigInterface;
import com.envyful.api.forge.config.UtilConfigItem;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.forge.player.util.UtilPlayer;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.api.text.parse.SimplePlaceholder;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsConfig;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsGraphics;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class BetterDexRewardsUI {

    public static void open(ForgeEnvyPlayer player, int page) {
        DexRewardsAttribute attribute = player.getAttributeNow(DexRewardsAttribute.class);

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

        UtilConfigItem.builder()
                .asyncClick()
                .clickHandler((envyPlayer, clickType) -> DexRewardsMainUI.open(player))
                .extendedConfigItem(player, pane, dexRewardsConfig.getBackButton());

        UtilConfigItem.builder()
                .asyncClick()
                .clickHandler((envyPlayer, clickType) -> open(player, page == dexRewardsConfig.getPages() ? 1 : page + 1))
                .extendedConfigItem(player, pane, dexRewardsConfig.getNextPageButton());

        UtilConfigItem.builder()
                .asyncClick()
                .clickHandler((envyPlayer, clickType) -> open(player, page == 1 ? dexRewardsConfig.getPages() : page - 1))
                .extendedConfigItem(player, pane, dexRewardsConfig.getPreviousPageButton());

        double percentage = attribute.getPokeDexPercentage();

        for (Map.Entry<String, BetterDexRewardsConfig.DexCompletion> entry : BetterDexRewards.getInstance().getConfig().getRewardStages().entrySet()) {
            ConfigItem configItem = null;

            if (entry.getValue().getPage() != page) {
                continue;
            }

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
                             .itemStack(UtilConfigItem.fromConfigItem(configItem, (SimplePlaceholder) name -> name.replace("%dex%", String.valueOf(attribute.getPokeDexPercentage()))))
                             .singleClick()
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
                                     entry.getValue().getRewards().give(player.getParent());
                                     attribute.claimReward(finalId);
                                     open(player, page);
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
                .setPlayerManager(BetterDexRewards.getInstance().getPlayerManager())
                .height(config.getHeight())
                .title(UtilChatColour.colour(config.getTitle()))
                .build().open(player);
    }
}
