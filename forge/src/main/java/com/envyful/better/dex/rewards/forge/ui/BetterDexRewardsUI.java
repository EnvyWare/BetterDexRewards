package com.envyful.better.dex.rewards.forge.ui;

import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.api.forge.items.ItemBuilder;
import com.envyful.api.forge.server.UtilForgeServer;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsConfig;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class BetterDexRewardsUI {

    public static void open(EnvyPlayer<EntityPlayerMP> player) {
        ConfigInterface config = BetterDexRewards.getInstance().getConfig().getConfigInterface();
        Pane pane = GuiFactory.paneBuilder()
                .topLeftX(0)
                .topLeftY(0)
                .width(9)
                .height(config.getHeight())
                .build();


        for (ConfigItem fillerItem : config.getFillerItems()) {
            pane.add(GuiFactory.displayableBuilder(ItemStack.class)
                            .itemStack(new ItemBuilder()
                                    .type(Item.getByNameOrId(fillerItem.getType()))
                                    .name(fillerItem.getName())
                                    .lore(fillerItem.getLore())
                                    .damage(fillerItem.getDamage())
                                    .amount(fillerItem.getAmount())
                                    .build())
                    .build());
        }

        DexRewardsAttribute attribute = player.getAttribute(BetterDexRewards.class);
        double percentage = attribute.getPokeDexPercentage();

        for (Map.Entry<String, BetterDexRewardsConfig.DexCompletion> entry : BetterDexRewards.getInstance()
                .getConfig().getRewardStages().entrySet()) {
            ConfigItem configItem = null;

            if (attribute.hasClaimed(entry.getKey())) {
                configItem = entry.getValue().getCompleteItem();
            } else if (percentage < entry.getValue().getRequiredPercentage()) {
                configItem = entry.getValue().getDisplayItem();
            } else {
                configItem = entry.getValue().getCompleteItem();
            }

            pane.set(entry.getValue().getxPos(), entry.getValue().getyPos(),
                    GuiFactory.displayableBuilder(ItemStack.class)
                            .itemStack(new ItemBuilder()
                                    .type(Item.getByNameOrId(configItem.getType()))
                                    .amount(configItem.getAmount())
                                    .damage(configItem.getDamage())
                                    .name(configItem.getName())
                                    .lore(configItem.getLore())
                                    .build())
                            .clickHandler((envyPlayer, clickType) -> {
                                if (percentage >= entry.getValue().getRequiredPercentage()) {
                                    for (String rewardMessage : entry.getValue().getRewardMessages()) {
                                        envyPlayer.message(UtilChatColour.translateColourCodes('&', rewardMessage));
                                    }

                                    UtilForgeConcurrency.runSync(() -> {
                                        for (String rewardCommand : entry.getValue().getRewardCommands()) {
                                            UtilForgeServer.executeCommand(rewardCommand
                                                    .replace("%player%",
                                                            ((EntityPlayerMP) envyPlayer.getParent()).getName()));
                                        }
                                    });
                                }
                            })
                    .build());

        }

        GuiFactory.guiBuilder()
                .addPane(pane)
                .setCloseConsumer(envyPlayer -> {})
                .setPlayerManager(BetterDexRewards.getInstance().getPlayerManager())
                .height(config.getHeight())
                .build().open(player);
    }
}
