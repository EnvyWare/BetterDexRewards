package com.envyful.better.dex.rewards.forge.ui;

import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.items.ItemBuilder;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.api.reforged.pixelmon.UtilPokemonInfo;
import com.envyful.api.reforged.pixelmon.sprite.UtilSprite;
import com.envyful.api.reforged.pixelmon.storage.UtilPixelmonPlayer;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import com.pixelmonmod.pixelmon.pokedex.PokedexEntry;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class DexRewardsMissingUI {

    public static void open(EnvyPlayer<EntityPlayerMP> player) {
        open(player, 0);
    }

    public static void open(EnvyPlayer<EntityPlayerMP> player, int page) {
        ConfigInterface config = BetterDexRewards.getInstance().getConfig().getConfigInterface();
        Pane pane = GuiFactory.paneBuilder()
                .topLeftX(0)
                .topLeftY(0)
                .width(9)
                .height(6)
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
        PlayerPartyStorage party = UtilPixelmonPlayer.getParty(player.getParent());

        attribute.setPage(page);

        List<PokedexEntry> values = Lists.newArrayList(Pokedex.fullPokedex.values());

        for (int i = page * 36; i < Math.min((page + 1) * 36, Pokedex.pokedexSize); i++) {
            PokedexEntry pokedexEntry = values.get(i);
            EnumSpecies species = EnumSpecies.getFromDex(pokedexEntry.natPokedexNum);
            int pos = i % 36;
            BaseStats baseStats = species.getBaseStats();

            pane.set(pos % 9, pos / 9, GuiFactory.displayableBuilder(ItemStack.class)
                    .itemStack(
                            new ItemBuilder(UtilSprite.getPixelmonSprite(species))
                                    .name("§b" + species.getLocalizedName() + " §f- " + species.getNationalPokedexNumber())
                                    .lore(
                                            "§bBiomes",
                                            String.join(", ", UtilPokemonInfo.getSpawnBiomes(baseStats)),
                                            " ",
                                            "§bTimes: " + String.join(", ", UtilPokemonInfo.getSpawnTimes(baseStats)),
                                            "§bCatch Rate: " + UtilPokemonInfo.getCatchRate(baseStats)
                                    )
                                    .build())
                    .build());
        }

        pane.set(0, 5, GuiFactory.displayableBuilder(ItemStack.class)
                .itemStack(new ItemBuilder()
                        .type(PixelmonItems.LtradeHolderLeft)
                        .name(UtilChatColour.translateColourCodes('&', "&eBack"))
                        .build())
                .clickHandler((envyPlayer, clickType) -> movePage(envyPlayer, -1))
                .build());

        pane.set(8, 5, GuiFactory.displayableBuilder(ItemStack.class)
                .itemStack(new ItemBuilder()
                        .type(PixelmonItems.tradeHolderRight)
                        .name(UtilChatColour.translateColourCodes('&', "&eForward"))
                        .build())
                .clickHandler((envyPlayer, clickType) -> movePage(envyPlayer, +1))
                .build());

        GuiFactory.guiBuilder()
                .addPane(pane)
                .setCloseConsumer(envyPlayer -> {
                })
                .setPlayerManager(BetterDexRewards.getInstance().getPlayerManager())
                .height(6)
                .title(UtilChatColour.translateColourCodes('&', config.getTitle()))
                .build().open(player);
    }

    private static void movePage(EnvyPlayer<?> player, int direction) {
        DexRewardsAttribute attribute = player.getAttribute(BetterDexRewards.class);

        if (attribute.getPage() >= (Pokedex.pokedexSize / 36) && direction > 1) {
            open((EnvyPlayer<EntityPlayerMP>) player, 0);
        } else if (attribute.getPage() == 0 && direction < 0) {
            open((EnvyPlayer<EntityPlayerMP>) player, (Pokedex.pokedexSize / 36));
        } else {
            open((EnvyPlayer<EntityPlayerMP>) player, attribute.getPage() + direction);
        }
    }
}
