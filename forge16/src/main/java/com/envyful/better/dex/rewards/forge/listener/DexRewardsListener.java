package com.envyful.better.dex.rewards.forge.listener;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.forge.player.util.UtilPlayer;
import com.envyful.api.platform.PlatformProxy;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
            DexRewardsAttribute attribute = player.getAttributeNow(DexRewardsAttribute.class);

            if (attribute == null) {
                return;
            }

            for (var entry : this.mod.getConfig().getRewardStages()) {
                if (attribute.hasClaimed(entry.getId())) {
                    continue;
                }

                if (entry.getOptionalAntiClaimPermission() != null &&
                        UtilPlayer.hasPermission(player.getParent(), entry.getOptionalAntiClaimPermission())) {
                    continue;
                }

                if (entry.getRequiredDex().test(player)) {
                    continue;
                }

                PlatformProxy.sendMessage(player, this.mod.getConfig().getClaimReminderMessage());
                break;
            }
        });
    }
}
