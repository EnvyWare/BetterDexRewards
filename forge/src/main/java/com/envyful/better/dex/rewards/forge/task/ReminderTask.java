package com.envyful.better.dex.rewards.forge.task;

import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
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
            DexRewardsAttribute attribute = onlinePlayer.getAttribute(BetterDexRewards.class);

            if (attribute == null) {
                continue;
            }

            if ((System.currentTimeMillis() - attribute.getLastReminder()) <=
                    TimeUnit.SECONDS.toMillis(this.mod.getConfig().getMessageDelaySeconds())) {
                continue;
            }

            attribute.setLastReminder(System.currentTimeMillis());

            for (String s : this.mod.getConfig().getClaimReminderMessage()) {
                onlinePlayer.message(UtilChatColour.translateColourCodes('&', s));
            }
        }
    }
}
