package com.envyful.better.dex.rewards.forge.ui;

import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.config.type.ExtendedConfigItem;
import com.envyful.api.neoforge.config.UtilConfigItem;
import com.envyful.api.neoforge.player.ForgeEnvyPlayer;
import com.envyful.api.text.Placeholder;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.api.event.PlayerClaimBonusEvent;
import com.envyful.better.dex.rewards.forge.api.event.PlayerClaimRewardEvent;
import com.envyful.better.dex.rewards.forge.config.DexCompletion;
import com.envyful.better.dex.rewards.forge.config.DexRewardBonus;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import com.envyful.better.dex.rewards.forge.transformer.CompletionTransformer;
import com.envyful.better.dex.rewards.forge.transformer.PlaceholderAPITransformer;
import com.envyful.better.dex.rewards.forge.transformer.StageTransformer;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class BetterDexRewardsUI {

    private ConfigInterface guiSettings = ConfigInterface.builder()
            .title("BetterDexRewards")
            .height(3)
            .fillType(ConfigInterface.FillType.BLOCK)
            .fillerItem(ConfigItem.builder()
                    .type("minecraft:black_stained_glass_pane")
                    .amount(1)
                    .name(" ")
                    .build())
            .build();

    private ExtendedConfigItem previousPageButton = ExtendedConfigItem.builder()
            .type("pixelmon:trade_holder_left")
            .disable()
            .amount(1)
            .name("&ePrevious Page")
            .positions(0, 0)
            .build();

    private ExtendedConfigItem nextPageButton = ExtendedConfigItem.builder()
            .type("pixelmon:trade_holder_left")
            .disable()
            .amount(1)
            .name("&eNext Page")
            .positions(1, 0)
            .build();

    private ExtendedConfigItem infoItem = ExtendedConfigItem.builder()
            .type("minecraft:paper")
            .amount(1)
            .disable()
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
            .positions(4, 0)
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

            if (attribute.isBlockedFromClaiming(entry)) {
                configItem = entry.getCompleteItem();
            } else if (attribute.hasClaimed(entry)) {
                configItem = attribute.hasPendingBonus(entry) ? entry.getToClaimItem() : entry.getCompleteItem();
            } else if (!entry.meetsRequiredDex(player) || !entry.meetsRequirements(player.getParent())) {
                configItem = entry.getDisplayItem();
            } else {
                configItem = entry.getToClaimItem();
            }

            var placeholders = Lists.<Placeholder>newArrayList(StageTransformer.of(player.getParent(), attribute, entry));
            placeholders.add(Placeholder.simple(name -> name.replace("%dex%", String.valueOf(attribute.getPokeDexPercentage()))));
            placeholders.add(percentagePlaceholder);

            UtilConfigItem.builder()
                    .singleClick()
                    .clickHandler((envyPlayer, clickType) -> {
                        if (attribute.isBlockedFromClaiming(entry)) {
                            envyPlayer.message(BetterDexRewards.getConfig().getAlreadyClaimed());
                            return;
                        }

                        if (attribute.hasClaimed(entry)) {
                            var pending = attribute.getUnclaimedMetBonuses(entry);

                            if (pending.isEmpty()) {
                                envyPlayer.message(BetterDexRewards.getConfig().getAlreadyClaimed());
                                return;
                            }

                            this.claimBonuses(player, attribute, entry, pending);
                            open(player, page);
                            return;
                        }

                        if (!entry.meetsRequiredDex(player)) {
                            envyPlayer.message(BetterDexRewards.getConfig().getInsufficientPercentage());
                            return;
                        }

                        var unmet = entry.getUnmetRequirements(player.getParent());

                        if (!unmet.isEmpty()) {
                            unmet.forEach(requirement -> envyPlayer.message(requirement.getFailedMessage()));
                            return;
                        }

                        // Taken before paying, so the bonuses paid are the ones that lowered the threshold
                        var bonuses = attribute.getUnclaimedMetBonuses(entry);

                        NeoForge.EVENT_BUS.post(new PlayerClaimRewardEvent(player.getParent(), entry));
                        entry.getRewards().give(player.getParent());
                        attribute.claimReward(entry.getId());
                        this.claimBonuses(player, attribute, entry, bonuses);
                        open(player, page);
                    })
                    .extendedConfigItem(player, pane, configItem, placeholders.toArray(new Placeholder[0]));
        }

        pane.open(player, this.guiSettings);
    }

    private void claimBonuses(ForgeEnvyPlayer player, DexRewardsAttribute attribute, DexCompletion stage, List<DexRewardBonus> bonuses) {
        for (var bonus : bonuses) {
            NeoForge.EVENT_BUS.post(new PlayerClaimBonusEvent(player.getParent(), stage, bonus));
            bonus.give(player.getParent());
            attribute.claimBonus(stage, bonus);
        }
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
