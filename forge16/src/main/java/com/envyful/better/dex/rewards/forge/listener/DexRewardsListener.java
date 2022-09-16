package com.envyful.better.dex.rewards.forge.listener;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.forge.player.util.UtilPlayer;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsConfig;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

public class DexRewardsListener {

    private final BetterDexRewards mod;

    public DexRewardsListener(BetterDexRewards mod) {
        this.mod = mod;

        Pixelmon.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPokemonCatch(PokedexEvent event) {
        ServerPlayerEntity entityPlayerMP = UtilPlayer.getOnlinePlayer(event.getPlayerUUID());

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
            ForgeEnvyPlayer player = this.mod.getPlayerManager().getPlayer(event.getPlayerUUID());
            DexRewardsAttribute attribute = player.getAttribute(BetterDexRewards.class);

            if (attribute == null) {
                return;
            }

            double percentage = attribute.getPokeDexPercentage();

            for (Map.Entry<String, BetterDexRewardsConfig.DexCompletion> entry : this.mod.getConfig().getRewardStages().entrySet()) {
                if (attribute.hasClaimed(entry.getKey())) {
                    continue;
                }

                if (entry.getValue().getOptionalAntiClaimPermission() != null &&
                        UtilPlayer.hasPermission(attribute.getParent().getParent(), entry.getValue().getOptionalAntiClaimPermission())) {
                    continue;
                }

                if (percentage < entry.getValue().getRequiredPercentage()) {
                    continue;
                }

                for (String s : this.mod.getConfig().getClaimReminderMessage()) {
                    entityPlayerMP.sendMessage(UtilChatColour.colour(s), Util.NIL_UUID);
                }

                break;
            }
        });
    }
}
