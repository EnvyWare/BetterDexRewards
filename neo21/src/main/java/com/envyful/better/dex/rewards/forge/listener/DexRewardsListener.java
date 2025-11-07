package com.envyful.better.dex.rewards.forge.listener;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.neoforge.player.ForgeEnvyPlayer;
import com.envyful.api.neoforge.player.util.UtilPlayer;
import com.envyful.api.platform.PlatformProxy;
import com.envyful.api.text.Placeholder;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.api.event.PlayerRankupEvent;
import com.envyful.better.dex.rewards.forge.config.DexCompletion;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokedex.event.PokedexEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;

public class DexRewardsListener {

    private final BetterDexRewards mod;

    public DexRewardsListener(BetterDexRewards mod) {
        this.mod = mod;

        Pixelmon.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPokemonCatch(PokedexEvent.Pre event) {
        var entityPlayerMP = UtilPlayer.getOnlinePlayer(event.getPlayerUUID());

        if (entityPlayerMP == null) {
            return;
        }

        if (BetterDexRewards.getConfig().isOriginalTrainerRewardsOnly()) {
            if (event.getPokemon().getOriginalTrainerUUID() != null && !event.getPokemon().isOriginalTrainer(entityPlayerMP)) {
                event.setCanceled(true);
                return;
            }
        }
    }

    @SubscribeEvent
    public void onPokedexEvent(PokedexEvent.Post event) {
        UtilConcurrency.runAsync(() -> {
            var player = BetterDexRewards.getPlayerManager().getPlayer(event.getPlayerUUID());
            var attribute = player.getAttributeNow(DexRewardsAttribute.class);
            var nextStage = attribute.findNextStage();

            if (nextStage != null) {
                var distance = nextStage.getRequiredDex().distance(player);
                var reminder = BetterDexRewards.getConfig().findReminder(distance);

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

            NeoForge.EVENT_BUS.post(new PlayerRankupEvent(player.getParent(), reward));

            attribute.setLastReminder(System.currentTimeMillis());
            PlatformProxy.sendMessage(player, this.mod.getConfig().getClaimReminderMessage(), reward);
        });
    }

    private DexCompletion findClaimableReward(ForgeEnvyPlayer player, DexRewardsAttribute attribute) {
        for (var entry : BetterDexRewards.getConfig().getRewardStages()) {
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
