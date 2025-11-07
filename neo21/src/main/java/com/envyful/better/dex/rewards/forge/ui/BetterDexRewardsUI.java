package com.envyful.better.dex.rewards.forge.ui;

import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.config.type.ExtendedConfigItem;
import com.envyful.api.neoforge.config.UtilConfigItem;
import com.envyful.api.neoforge.player.ForgeEnvyPlayer;
import com.envyful.api.neoforge.player.util.UtilPlayer;
import com.envyful.api.text.Placeholder;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import com.envyful.better.dex.rewards.forge.transformer.CompletionTransformer;
import com.envyful.better.dex.rewards.forge.transformer.PlaceholderAPITransformer;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class BetterDexRewardsUI {

    private ConfigInterface guiSettings = ConfigInterface.builder()
            .title("BetterDexRewards")
            .height(6)
            .fillType(ConfigInterface.FillType.BLOCK)
            .fillerItem(ConfigItem.builder()
                    .type("minecraft:black_stained_glass_pane")
                    .amount(1)
                    .name(" ")
                    .build())
            .build();

    private ExtendedConfigItem previousPageButton = ExtendedConfigItem.builder()
            .type("pixelmon:trade_holder_left")
            .amount(1)
            .name("&ePrevious Page")
            .positions(0, 0)
            .build();

    private ExtendedConfigItem nextPageButton = ExtendedConfigItem.builder()
            .type("pixelmon:trade_holder_left")
            .amount(1)
            .name("&eNext Page")
            .positions(1, 0)
            .build();

    private ExtendedConfigItem infoItem = ExtendedConfigItem.builder()
            .type("minecraft:paper")
            .amount(1)
            .name("&eInfo")
            .positions(7, 1)
            .build();

    private ExtendedConfigItem percentageItem = ExtendedConfigItem.builder()
            .type("pixelmon:poke_ball")
            .amount(1)
            .name("&eCurrent PokeDex Percentage")
            .lore(
                    "&eComplete: &a%percentage%"
            )
            .nbt("tooltip", new ConfigItem.NBTValue("string", ""))
            .positions(1, 1)
            .build();

    private int pages = 1;

    public void open(ForgeEnvyPlayer player, int page) {
        var attribute = player.getAttributeNow(DexRewardsAttribute.class);
        var pane = this.guiSettings.toPane();
        var percentagePlaceholder = CompletionTransformer.of(attribute.getPokeDexPercentage());

        this.nextPageButton.convertToBuilder(player, pane)
                .asyncClick()
                .clickHandler((envyPlayer, clickType) -> open(player, page == this.pages ? 1 : page + 1))
                .build();

        this.previousPageButton.convertToBuilder(player, pane)
                .asyncClick()
                .clickHandler((envyPlayer, clickType) -> open(player, page == 1 ? this.pages : page - 1))
                .build();

        this.percentageItem.convertToBuilder(player, pane, getTransformers(player.getParent(), attribute).toArray(new Placeholder[0])).build();
        this.infoItem.convertToBuilder(player, pane).build();

        for (var entry : BetterDexRewards.getConfig().getRewardStages()) {
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
                            for (String msg : BetterDexRewards.getConfig().getAlreadyClaimed()) {
                                envyPlayer.message(msg);
                            }
                            return;
                        }

                        if (entry.getOptionalAntiClaimPermission() != null && player.hasPermission(entry.getOptionalAntiClaimPermission())) {
                            for (String msg : BetterDexRewards.getConfig().getAlreadyClaimed()) {
                                envyPlayer.message(msg);
                            }
                            return;
                        }

                        if (entry.getRequiredDex().test(player)) {
                            entry.getRewards().give(player.getParent());
                            attribute.claimReward(finalId);
                            open(player, page);
                        } else {
                            for (String msg : BetterDexRewards.getConfig().getInsufficientPercentage()) {
                                envyPlayer.message(msg);
                            }
                        }
                    })
                    .extendedConfigItem(player, pane, configItem,
                            Placeholder.simple(name -> name.replace("%dex%", String.valueOf(attribute.getPokeDexPercentage()))), percentagePlaceholder);
        }

        pane.open(player, this.guiSettings);
    }

    private static List<Placeholder> getTransformers(ServerPlayer player, DexRewardsAttribute attribute) {
        if (!BetterDexRewards.isPlaceholders()) {
            return Lists.newArrayList(CompletionTransformer.of(attribute.getPokeDexPercentage()));
        }

        return Lists.newArrayList(
                CompletionTransformer.of(attribute.getPokeDexPercentage()),
                PlaceholderAPITransformer.of(player)
        );
    }
}
