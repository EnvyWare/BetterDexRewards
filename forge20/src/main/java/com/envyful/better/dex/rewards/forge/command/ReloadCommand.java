package com.envyful.better.dex.rewards.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;

@Command(
        value = "reload",
        description = "Reloads the config",
        aliases = {
                "r"
        }
)
@Permissible("better.dex.rewards.command.reload")
@Child
public class ReloadCommand {

    @CommandProcessor
    public void onCommand(@Sender CommandSource sender, String[] args) {
        BetterDexRewards.getInstance().reloadConfig();
        sender.sendSystemMessage(Component.literal("Reloaded config"));
    }
}
