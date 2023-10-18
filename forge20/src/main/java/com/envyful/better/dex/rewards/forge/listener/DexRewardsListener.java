package com.envyful.better.dex.rewards.forge.listener;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.player.util.UtilPlayer;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
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
            if (event.getPokemon().getOriginalTrainerUUID() != null) {
                if (!(event.getPokemon().getOriginalTrainerUUID().equals(entityPlayerMP.getUUID()))) {
                    event.setCanceled(true);
                    return;
                }
            }
        }

        UtilConcurrency.runAsync(() -> {
            var player = this.mod.getPlayerManager().getPlayer(event.getPlayerUUID());
            var attribute = player.getAttribute(DexRewardsAttribute.class);

            if (attribute == null) {
                return;
            }

            var percentage = attribute.getPokeDexPercentage();

            for (var entry : this.mod.getConfig().getRewardStages().entrySet()) {
                if (attribute.hasClaimed(entry.getKey())) {
                    continue;
                }

                if (entry.getValue().getOptionalAntiClaimPermission() != null &&
                        UtilPlayer.hasPermission(player.getParent(), entry.getValue().getOptionalAntiClaimPermission())) {
                    continue;
                }

                if (percentage < entry.getValue().getRequiredPercentage()) {
                    continue;
                }

                for (String s : this.mod.getConfig().getClaimReminderMessage()) {
                    entityPlayerMP.sendSystemMessage(UtilChatColour.colour(s));
                }

                break;
            }
        });
    }
}
