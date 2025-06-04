package com.envyful.better.dex.rewards.forge.ui;

import com.envyful.api.neoforge.config.UtilConfigItem;
import com.envyful.api.neoforge.player.ForgeEnvyPlayer;
import com.envyful.api.text.Placeholder;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsGraphics;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import com.envyful.better.dex.rewards.forge.transformer.CompletionTransformer;
import com.envyful.better.dex.rewards.forge.transformer.PlaceholderAPITransformer;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class DexRewardsMainUI {

    public static void open(ForgeEnvyPlayer player) {
        if (BetterDexRewards.getInstance().getConfig() == null) {
            return;
        }

        DexRewardsAttribute attribute = player.getAttributeNow(DexRewardsAttribute.class);

        if (attribute == null) {
            return;
        }

        BetterDexRewardsGraphics.MainUI config = BetterDexRewards.getInstance().getGraphics().getMainUI();
        List<Placeholder> transformers = getTransformers(player.getParent(), attribute);

        var pane = config.getGuiSettings().toPane(transformers.toArray(new Placeholder[0]));

        UtilConfigItem.builder().extendedConfigItem(player, pane, config.getPercentageItem(), transformers.toArray(new Placeholder[0]));
        UtilConfigItem.builder()
                .asyncClick()
                .clickHandler((envyPlayer, clickType) -> BetterDexRewardsUI.open(player, 1))
                .extendedConfigItem(player, pane, config.getRanksItem(), transformers.toArray(new Placeholder[0]));

        UtilConfigItem.builder()
                .clickHandler((envyPlayer, clickType) -> DexRewardsMissingUI.open(player))
                .extendedConfigItem(player, pane, config.getMissingItem());

        UtilConfigItem.builder().extendedConfigItem(player, pane, config.getInfoItem());

        pane.open(player, config.getGuiSettings(), transformers.toArray(new Placeholder[0]));
    }

    private static List<Placeholder> getTransformers(ServerPlayer player, DexRewardsAttribute attribute) {
        if (!BetterDexRewards.getInstance().isPlaceholders()) {
            return Lists.newArrayList(CompletionTransformer.of(attribute.getPokeDexPercentage()));
        }

        return Lists.newArrayList(
                CompletionTransformer.of(attribute.getPokeDexPercentage()),
                PlaceholderAPITransformer.of(player)
        );
    }
}
