package com.envyful.better.dex.rewards.forge.task;

import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.platform.PlatformProxy;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.DexCompletion;
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
            if (!onlinePlayer.hasAttribute(DexRewardsAttribute.class)) {
                continue;
            }

            var attribute = onlinePlayer.getAttributeNow(DexRewardsAttribute.class);

            if ((System.currentTimeMillis() - attribute.getLastReminder()) <=
                    TimeUnit.SECONDS.toMillis(this.mod.getConfig().getMessageDelaySeconds())) {
                continue;
            }

            var claimableReward = this.findClaimableReward(onlinePlayer, attribute);

            if (claimableReward == null) {
                continue;
            }

            attribute.setLastReminder(System.currentTimeMillis());
            PlatformProxy.sendMessage(onlinePlayer, this.mod.getConfig().getClaimReminderMessage(), claimableReward);
        }
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
