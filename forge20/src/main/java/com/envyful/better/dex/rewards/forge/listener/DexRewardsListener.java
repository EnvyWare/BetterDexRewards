package com.envyful.better.dex.rewards.forge.listener;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.forge.player.util.UtilPlayer;
import com.envyful.api.platform.PlatformProxy;
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

        if (BetterDexRewards.getConfig().isOriginalTrainerRewardsOnly()) {
            if (event.getPokemon().getOriginalTrainerUUID() != null) {
                if (!(event.getPokemon().getOriginalTrainerUUID().equals(entityPlayerMP.getUUID()))) {
                    event.setCanceled(true);
                    return;
                }
            }
        }

        UtilConcurrency.runAsync(() -> {
            var player = this.mod.getPlayerManager().getPlayer(event.getPlayerUUID());
            DexRewardsAttribute attribute = player.getAttributeNow(DexRewardsAttribute.class);

            if (attribute == null) {
                return;
            }

            var reward = this.findClaimableReward(player, attribute);

            if (reward == null) {
                return;
            }

            attribute.setLastReminder(System.currentTimeMillis());
            PlatformProxy.sendMessage(player, BetterDexRewards.getConfig().getClaimReminderMessage(), reward);
        });
    }

    private DexCompletion findClaimableReward(ForgeEnvyPlayer player, DexRewardsAttribute attribute) {
        for (var entry : BetterDexRewards.getConfig().getRewardStages()) {
            if (entry.getOptionalAntiClaimPermission() != null &&
                    PlatformProxy.hasPermission(player, entry.getOptionalAntiClaimPermission())) {
                continue;
            }

            if (entry.getRequiredDex().test(player) && !attribute.hasClaimed(entry.getId())) {
                return entry;
            }
        }

        return null;
    }
}
