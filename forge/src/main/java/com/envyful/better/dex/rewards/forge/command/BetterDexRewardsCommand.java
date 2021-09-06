package com.envyful.better.dex.rewards.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.ui.BetterDexRewardsUI;
import net.minecraft.entity.player.EntityPlayerMP;

@Command(
        value = "betterdexrewards",
        description = "Root command",
        aliases = {
                "dexrewards",
                "drewards",
                "dexr",
                "dex",
                "pokedex"
        }
)
public class BetterDexRewardsCommand {

    @CommandProcessor
    public void onCommand(@Sender EntityPlayerMP player, String[] args) {
        BetterDexRewardsUI.open(BetterDexRewards.getInstance().getPlayerManager().getPlayer(player));
    }
}
