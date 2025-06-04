package com.envyful.better.dex.rewards.forge.ui;

import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ExtendedConfigItem;
import com.envyful.api.neoforge.config.UtilConfigItem;
import com.envyful.api.neoforge.player.ForgeEnvyPlayer;
import com.envyful.api.neoforge.player.util.UtilPlayer;
import com.envyful.api.text.Placeholder;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsGraphics;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import com.envyful.better.dex.rewards.forge.transformer.CompletionTransformer;

public class BetterDexRewardsUI {

    public static void open(ForgeEnvyPlayer player, int page) {
        DexRewardsAttribute attribute = player.getAttributeNow(DexRewardsAttribute.class);

        if (attribute == null) {
            return;
        }

        BetterDexRewardsGraphics.RanksUI dexRewardsConfig = BetterDexRewards.getInstance().getGraphics().getRankUI();
        ConfigInterface config = dexRewardsConfig.getGuiSettings();
        var pane = config.toPane();
        var percentagePlaceholder = CompletionTransformer.of(attribute.getPokeDexPercentage());

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

        for (var entry : BetterDexRewards.getInstance().getConfig().getRewardStages()) {
            if (entry.getPage() != page) {
                continue;
            }

            ExtendedConfigItem configItem;

            if (attribute.hasClaimed(entry)) {
                configItem = entry.getCompleteItem();
            } else if (entry.getOptionalAntiClaimPermission() != null &&
                    UtilPlayer.hasPermission(player.getParent(), entry.getOptionalAntiClaimPermission())) {
                configItem = entry.getCompleteItem();
            } else if (!entry.getRequiredDex().test(player)) {
                configItem = entry.getDisplayItem();
            } else {
                configItem = entry.getToClaimItem();
            }

            final String finalId = entry.getId();

            UtilConfigItem.builder()
                    .singleClick()
                    .clickHandler((envyPlayer, clickType) -> {
                        if (attribute.hasClaimed(entry)) {
                            for (String msg : BetterDexRewards.getInstance().getConfig().getAlreadyClaimed()) {
                                envyPlayer.message(msg);
                            }
                            return;
                        }

                        if (entry.getOptionalAntiClaimPermission() != null &&
                                UtilPlayer.hasPermission(player.getParent(), entry.getOptionalAntiClaimPermission())) {
                            for (String msg : BetterDexRewards.getInstance().getConfig().getAlreadyClaimed()) {
                                envyPlayer.message(msg);
                            }
                            return;
                        }

                        if (entry.getRequiredDex().test(player)) {
                            entry.getRewards().give(player.getParent());
                            attribute.claimReward(finalId);
                            open(player, page);
                        } else {
                            for (String msg : BetterDexRewards.getInstance().getConfig().getInsufficientPercentage()) {
                                envyPlayer.message(msg);
                            }
                        }
                    })
                    .extendedConfigItem(player, pane, configItem,
                            Placeholder.simple(name -> name.replace("%dex%", String.valueOf(attribute.getPokeDexPercentage()))), percentagePlaceholder);
        }

        pane.open(player, config);
    }
}
