package com.envyful.better.dex.rewards.forge.ui;

import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.api.forge.config.UtilConfigItem;
import com.envyful.api.forge.items.ItemBuilder;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.api.reforged.pixelmon.storage.UtilPixelmonPlayer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonDexFormattedTransformer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonDexTransformer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonNameTransformer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonSpriteTransformer;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import com.envyful.better.dex.rewards.forge.transformer.BiomesTransformer;
import com.envyful.better.dex.rewards.forge.transformer.CatchRateTransformer;
import com.envyful.better.dex.rewards.forge.transformer.SpawnTimesTransformer;
import com.google.common.collect.Lists;
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

        UtilConfigItem.addConfigItem(pane, BetterDexRewards.getInstance().getConfig().getBackButton(),
                                     (envyPlayer, clickType) -> {
                                         ((EntityPlayerMP) envyPlayer.getParent()).closeScreen();

                                         UtilForgeConcurrency.runSync(() -> {
                                             DexRewardsMainUI.open(player);
                                         });
                                     }
        );

        DexRewardsAttribute attribute = player.getAttribute(BetterDexRewards.class);

        attribute.setPage(page);

        List<PokedexEntry> values = Lists.newArrayList(Pokedex.fullPokedex.values());
        int counter = 0;
        PlayerPartyStorage storage = UtilPixelmonPlayer.getParty(player.getParent());

        for (int i = page * 36; counter < 36 && i < (Pokedex.pokedexSize - storage.pokedex.countCaught()); i++) {
            PokedexEntry pokedexEntry = values.get(i);
            EnumSpecies species = EnumSpecies.getFromDex(pokedexEntry.natPokedexNum);

            if (storage.pokedex.hasCaught(species)) {
                continue;
            }

            int pos = counter;
            ++counter;

            pane.set(pos % 9, pos / 9, GuiFactory.displayableBuilder(ItemStack.class)
                    .itemStack(UtilConfigItem.fromConfigItem(
                            BetterDexRewards.getInstance().getConfig().getMissingPokemonItem(),
                            Lists.newArrayList(
                                    PokemonDexTransformer.of(species),
                                    PokemonNameTransformer.of(species),
                                    PokemonDexFormattedTransformer.of(species),
                                    PokemonSpriteTransformer.of(species),
                                    BiomesTransformer.of(species),
                                    CatchRateTransformer.of(species),
                                    SpawnTimesTransformer.of(species)
                            )
                    ))
                    .build());
        }

        UtilConfigItem.addConfigItem(pane, BetterDexRewards.getInstance().getConfig().getPreviousPageButton(),
                                     (envyPlayer, clickType) -> movePage(envyPlayer, -1)
        );

        UtilConfigItem.addConfigItem(pane, BetterDexRewards.getInstance().getConfig().getNextPageButton(),
                                     (envyPlayer, clickType) -> movePage(envyPlayer, +1)
        );

        GuiFactory.guiBuilder()
                .addPane(pane)
                .setCloseConsumer(envyPlayer -> {})
                .setPlayerManager(BetterDexRewards.getInstance().getPlayerManager())
                .height(config.getHeight())
                .title(UtilChatColour.translateColourCodes('&', config.getTitle()))
                .build().open(player);
    }

    private static void movePage(EnvyPlayer<?> player, int direction) {
        DexRewardsAttribute attribute = player.getAttribute(BetterDexRewards.class);
        PlayerPartyStorage storage = UtilPixelmonPlayer.getParty((EntityPlayerMP) player.getParent());

        if ((attribute.getPage() >= ((Pokedex.pokedexSize - storage.pokedex.countCaught()) / 36)) && direction > 0) {
            open((EnvyPlayer<EntityPlayerMP>) player);
        } else if (attribute.getPage() == 0 && direction < 0) {
            open((EnvyPlayer<EntityPlayerMP>) player, ((Pokedex.pokedexSize - storage.pokedex.countCaught()) / 36));
        } else {
            open((EnvyPlayer<EntityPlayerMP>) player, attribute.getPage() + direction);
        }
    }
}
