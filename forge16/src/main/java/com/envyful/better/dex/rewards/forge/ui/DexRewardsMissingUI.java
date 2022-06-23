package com.envyful.better.dex.rewards.forge.ui;

import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.config.UtilConfigItem;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonDexFormattedTransformer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonDexTransformer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonNameTransformer;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsGraphics;
import com.envyful.better.dex.rewards.forge.transformer.SpeciesSpriteTransformer;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.List;

public class DexRewardsMissingUI {

    public static void open(EnvyPlayer<ServerPlayerEntity> player) {
        open(player, 0, false);
    }

    public static void open(EnvyPlayer<ServerPlayerEntity> player, int startPos, boolean backwards) {
        BetterDexRewardsGraphics.MissingPokemonUI config = BetterDexRewards.getInstance().getGraphics().getMissingPokemonUI();

        Pane pane = GuiFactory.paneBuilder()
                .topLeftX(0)
                .topLeftY(0)
                .width(9)
                .height(config.getGuiSettings().getHeight())
                .build();

        for (ConfigItem fillerItem : config.getGuiSettings().getFillerItems()) {
            if (!fillerItem.isEnabled()) {
                continue;
            }

            pane.add(GuiFactory.displayable(UtilConfigItem.fromConfigItem(fillerItem)));
        }

        UtilConfigItem.addConfigItem(pane, config.getBackButton(), (envyPlayer, clickType) -> DexRewardsMainUI.open(player));

        List<Species> values = Lists.newArrayList(PixelmonSpecies.getAll());
        PlayerPartyStorage storage = StorageProxy.getParty(player.getParent());

        int i = 0;
        int speciesPosition = startPos;

        while (i < config.getMissingPokemonPositions().size()) {
            Species species = values.get(speciesPosition);

            if (species.is(PixelmonSpecies.MISSINGNO) || storage.playerPokedex.hasSeen(species)) {
                if (backwards) {
                    --speciesPosition;
                } else {
                    ++speciesPosition;
                }

                continue;
            }

            int position = config.getMissingPokemonPositions().get(backwards ? config.getMissingPokemonPositions().size() - i : i);
            pane.set(position % 9, position / 9, GuiFactory.displayable((UtilConfigItem.fromConfigItem(
                    config.getMissingPokemonItem(),
                    Lists.newArrayList(
                            PokemonDexTransformer.of(species),
                            PokemonNameTransformer.of(species),
                            PokemonDexFormattedTransformer.of(species),
                            SpeciesSpriteTransformer.of(species)
                    )
            ))));
            ++i;

            if (backwards) {
                --speciesPosition;
            } else {
                ++speciesPosition;
            }
        }

        GuiFactory.guiBuilder()
                .addPane(pane)
                .setPlayerManager(BetterDexRewards.getInstance().getPlayerManager())
                .height(config.getGuiSettings().getHeight())
                .title(UtilChatColour.colour(config.getGuiSettings().getTitle()).getString())
                .build()
                .open(player);
    }
}
