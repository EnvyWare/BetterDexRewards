package com.envyful.better.dex.rewards.forge.ui;

import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.config.UtilConfigInterface;
import com.envyful.api.forge.config.UtilConfigItem;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.api.text.Placeholder;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsGraphics;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import com.envyful.better.dex.rewards.forge.transformer.CompletionTransformer;
import com.envyful.better.dex.rewards.forge.transformer.PlaceholderAPITransformer;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.List;

public class DexRewardsMainUI {

    public static void open(ForgeEnvyPlayer player) {
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

        List<Placeholder> transformers = getTransformers(player.getParent(), attribute);

        UtilConfigInterface.fillBackground(pane, config.getGuiSettings(), transformers.toArray(new Placeholder[0]));

        UtilConfigItem.builder().extendedConfigItem(player, pane, config.getPercentageItem(), transformers.toArray(new Placeholder[0]));
        UtilConfigItem.builder()
                .asyncClick()
                .clickHandler((envyPlayer, clickType) -> BetterDexRewardsUI.open(player, 1))
                .extendedConfigItem(player, pane, config.getRanksItem(), transformers.toArray(new Placeholder[0]));

        UtilConfigItem.builder()
                .clickHandler((envyPlayer, clickType) -> DexRewardsMissingUI.open(player))
                .extendedConfigItem(player, pane, config.getMissingItem());

        UtilConfigItem.builder().extendedConfigItem(player, pane, config.getInfoItem());

        GuiFactory.guiBuilder()
                .addPane(pane)
                .setPlayerManager(BetterDexRewards.getInstance().getPlayerManager())
                .height(config.getGuiSettings().getHeight())
                .title(UtilChatColour.colour(config.getGuiSettings().getTitle()))
                .build()
                .open(player);
    }

    private static List<Placeholder> getTransformers(ServerPlayerEntity player, DexRewardsAttribute attribute) {
        if (!BetterDexRewards.getInstance().isPlaceholders()) {
            return Lists.newArrayList(CompletionTransformer.of(attribute.getPokeDexPercentage()));
        }

        return Lists.newArrayList(
                CompletionTransformer.of(attribute.getPokeDexPercentage()),
                PlaceholderAPITransformer.of(player)
        );
    }
}
