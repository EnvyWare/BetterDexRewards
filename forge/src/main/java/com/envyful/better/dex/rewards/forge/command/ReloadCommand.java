package com.envyful.better.dex.rewards.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;

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
    public void onCommand(@Sender ICommandSender sender, String[] args) {
        BetterDexRewards.getInstance().reloadConfig();
        sender.sendMessage(new TextComponentString("Reloaded config"));
    }
}
