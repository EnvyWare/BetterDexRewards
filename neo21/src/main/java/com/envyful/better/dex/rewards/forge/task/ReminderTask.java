package com.envyful.better.dex.rewards.forge.task;

import com.envyful.api.neoforge.player.ForgeEnvyPlayer;
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
            if (!onlinePlayer.hasAttribute(DexRewardsAttribute.class)) {
                continue;
            }

            var attribute = onlinePlayer.getAttributeNow(DexRewardsAttribute.class);

            if ((System.currentTimeMillis() - attribute.getLastReminder()) <=
                    TimeUnit.SECONDS.toMillis(this.mod.getConfig().getMessageDelaySeconds())) {
                continue;
            }

            var claimableReward = attribute.findClaimableStage();

            if (claimableReward == null) {
                continue;
            }

            attribute.setLastReminder(System.currentTimeMillis());
            PlatformProxy.sendMessage(onlinePlayer, this.mod.getConfig().getClaimReminderMessage(), claimableReward);
        }
    }
}
