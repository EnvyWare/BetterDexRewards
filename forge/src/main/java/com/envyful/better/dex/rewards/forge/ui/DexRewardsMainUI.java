package com.envyful.better.dex.rewards.forge.ui;

import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.items.ItemBuilder;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class DexRewardsMainUI {

    public static void open(EnvyPlayer<EntityPlayerMP> player) {
        if (BetterDexRewards.getInstance().getConfig() == null) {
            FMLCommonHandler.instance().getFMLLogger().error("CONFIG DID NOT LOAD CORRECTLY");
            return;
        }

        ConfigInterface config = BetterDexRewards.getInstance().getConfig().getConfigInterface();
        Pane pane = GuiFactory.paneBuilder()
                .topLeftX(0)
                .topLeftY(0)
                .width(9)
                .height(3)
                .build();


        for (ConfigItem fillerItem : config.getFillerItems()) {
            pane.add(GuiFactory.displayableBuilder(ItemStack.class)
                    .itemStack(new ItemBuilder()
                            .type(Item.getByNameOrId(fillerItem.getType()))
                            .name(fillerItem.getName())
                            .lore(fillerItem.getLore())
                            .damage(fillerItem.getDamage())
                            .amount(fillerItem.getAmount())
                            .build())
                    .build());
        }

        DexRewardsAttribute attribute = player.getAttribute(BetterDexRewards.class);
        double percentage = attribute.getPokeDexPercentage();

        pane.set(1, 1, GuiFactory.displayableBuilder(ItemStack.class)
                .itemStack(new ItemBuilder()
                        .type(PixelmonItemsPokeballs.pokeBall)
                        .amount(1)
                        .name(UtilChatColour.translateColourCodes('&', "&eCurrent PokeDex Percentage"))
                        .lore(
                                UtilChatColour.translateColourCodes('&',
                                                "&eComplete: &a" + String.format("%.2f", percentage) + "%")
                        )
                        .nbt("tooltip", new NBTTagString(""))
                        .build())
                .build());

        pane.set(3, 1, GuiFactory.displayableBuilder(ItemStack.class)
                .itemStack(new ItemBuilder()
                        .type(PixelmonItems.itemFinder)
                        .amount(1)
                        .name(UtilChatColour.translateColourCodes('&', "&ePokeDex Ranks"))
                        .lore()
                        .build())
                .clickHandler((envyPlayer, clickType) -> BetterDexRewardsUI.open((EnvyPlayer<EntityPlayerMP>) envyPlayer))
                .build());

        pane.set(5, 1, GuiFactory.displayableBuilder(ItemStack.class)
                .itemStack(new ItemBuilder()
                        .type(PixelmonItems.itemPixelmonSprite)
                        .amount(1)
                        .name(UtilChatColour.translateColourCodes('&', "&eMissing Pokemon"))
                        .lore()
                        .nbt("ndex", new NBTTagShort((short) EnumSpecies.Unown.getNationalPokedexInteger()))
                        .build())
                .clickHandler((envyPlayer, clickType) -> DexRewardsMissingUI.open((EnvyPlayer<EntityPlayerMP>) envyPlayer))
                .build());

        ConfigItem infoItem = BetterDexRewards.getInstance().getConfig().getInfoItem();

        pane.set(7, 1, GuiFactory.displayableBuilder(ItemStack.class)
                .itemStack(new ItemBuilder()
                        .type(Item.getByNameOrId(infoItem.getType()))
                        .amount(infoItem.getAmount())
                        .name(UtilChatColour.translateColourCodes('&', infoItem.getName()))
                        .lore(infoItem.getLore())
                        .damage(infoItem.getDamage())
                        .build())
                .build());

        GuiFactory.guiBuilder()
                .addPane(pane)
                .setCloseConsumer(envyPlayer -> {
                })
                .setPlayerManager(BetterDexRewards.getInstance().getPlayerManager())
                .height(3)
                .title(UtilChatColour.translateColourCodes('&', config.getTitle()))
                .build().open(player);
    }
}
