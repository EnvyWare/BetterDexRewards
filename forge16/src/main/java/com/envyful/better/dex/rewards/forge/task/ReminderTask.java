package com.envyful.better.dex.rewards.forge.task;

import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.forge.player.util.UtilPlayer;
import com.envyful.api.platform.PlatformProxy;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;

import java.util.concurrent.TimeUnit;

public class ReminderTask implements Runnable {

    private final BetterDexRewards mod;

    public ReminderTask(BetterDexRewards mod) {
        this.mod = mod;
    }

    @Override
    public void run() {
        for (ForgeEnvyPlayer onlinePlayer : this.mod.getPlayerManager().getOnlinePlayers()) {
            DexRewardsAttribute attribute = onlinePlayer.getAttributeNow(DexRewardsAttribute.class);

            if (attribute == null) {
                continue;
            }

            if ((System.currentTimeMillis() - attribute.getLastReminder()) <=
                    TimeUnit.SECONDS.toMillis(this.mod.getConfig().getMessageDelaySeconds())) {
                continue;
            }

            if (!this.canClaimReward(onlinePlayer, attribute)) {
                continue;
            }

            attribute.setLastReminder(System.currentTimeMillis());
            PlatformProxy.sendMessage(onlinePlayer, this.mod.getConfig().getClaimReminderMessage());
        }
    }

    private boolean canClaimReward(ForgeEnvyPlayer player, DexRewardsAttribute attribute) {
        double pokeDexPercentage = attribute.getPokeDexPercentage();

        for (var entry :
                this.mod.getConfig().getRewardStages()) {
            if (entry.getOptionalAntiClaimPermission() != null &&
                    UtilPlayer.hasPermission(player.getParent(), entry.getOptionalAntiClaimPermission())) {
                continue;
            }

            if (entry.getRequiredDex().test(player) && !attribute.hasClaimed(entry.getId())) {
                return true;
            }
        }

        return false;
    }
}
