package com.envyful.better.dex.rewards.forge.transformer;

import com.envyful.api.gui.Transformer;
import com.envyful.api.reforged.pixelmon.UtilPokemonInfo;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

public class CatchRateTransformer implements Transformer {

    private final EnumSpecies pokemon;

    public static CatchRateTransformer of(EnumSpecies species) {
        return new CatchRateTransformer(species);
    }

    public CatchRateTransformer(EnumSpecies pokemon) {
        this.pokemon = pokemon;
    }

    @Override
    public String transformName(String name) {
        return name.replace("%catch_rate%", String.join("\n",
                                                       UtilPokemonInfo.getCatchRate(this.pokemon.getBaseStats())));
    }
}
