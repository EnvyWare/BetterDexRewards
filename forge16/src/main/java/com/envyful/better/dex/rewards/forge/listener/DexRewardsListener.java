package com.envyful.better.dex.rewards.forge.listener;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.forge.player.util.UtilPlayer;
import com.envyful.api.platform.PlatformProxy;
import com.envyful.api.text.Placeholder;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.DexCompletion;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DexRewardsListener {

    private final BetterDexRewards mod;

    public DexRewardsListener(BetterDexRewards mod) {
        this.mod = mod;

        Pixelmon.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPokemonCatch(PokedexEvent event) {
        var entityPlayerMP = UtilPlayer.getOnlinePlayer(event.getPlayerUUID());

        if (entityPlayerMP == null) {
            return;
        }

        if (this.mod.getConfig().isOriginalTrainerRewardsOnly()) {
            if (event.getPokemon().getOriginalTrainerUUID() != null && !event.getPokemon().isOriginalTrainer(entityPlayerMP)) {
                event.setCanceled(true);
                return;
            }
        }

        UtilConcurrency.runAsync(() -> {
            var player = this.mod.getPlayerManager().getPlayer(event.getPlayerUUID());
            var attribute = player.getAttributeNow(DexRewardsAttribute.class);

            if (attribute == null) {
                return;
            }

            var nextStage = attribute.findNextStage();

            if (nextStage != null) {
                var distance = nextStage.getRequiredDex().distance(player);
                var reminder = this.mod.getConfig().findReminder(distance);

                if (reminder != null) {
                    PlatformProxy.executeConsoleCommands(reminder.getCommands(),
                            Placeholder.simple("%player%", player.getName()),
                            Placeholder.simple("%distance%", String.valueOf(distance)),
                            Placeholder.simple("%next%", nextStage.getId()));
                }
            }

            var reward = this.findClaimableReward(player, attribute);

            if (reward == null) {
                return;
            }

            attribute.setLastReminder(System.currentTimeMillis());
            PlatformProxy.sendMessage(player, this.mod.getConfig().getClaimReminderMessage(), reward);
        });
    }

    private DexCompletion findClaimableReward(ForgeEnvyPlayer player, DexRewardsAttribute attribute) {
        for (var entry : this.mod.getConfig().getRewardStages()) {
            if (entry.getOptionalAntiClaimPermission() != null &&
                    PlatformProxy.hasPermission(player, entry.getOptionalAntiClaimPermission())) {
                continue;
            }

            if (!attribute.hasClaimed(entry) && entry.getRequiredDex().test(player)) {
                return entry;
            }
        }

        return null;
    }
}
