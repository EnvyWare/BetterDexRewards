package com.envyful.better.dex.rewards.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import net.minecraft.entity.player.EntityPlayerMP;

@Command(
        value = "betterdexrewards",
        description = "Root command",
        aliases = {
                "dexrewards",
                "drewards",
                "dexr"
        }
)
public class BetterDexRewardsCommand {

    @CommandProcessor
    public void onCommand(@Sender EntityPlayerMP player, String[] args) {
        //TODO: open UI
    }
}
