package com.envyful.better.dex.rewards.forge.transformer;

import com.envyful.api.gui.Transformer;
import com.envyful.api.reforged.pixelmon.UtilPokemonInfo;
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats;

public class CatchRateTransformer implements Transformer {

    private final Stats pokemon;

    public static CatchRateTransformer of(Stats species) {
        return new CatchRateTransformer(species);
    }

    public CatchRateTransformer(Stats pokemon) {
        this.pokemon = pokemon;
    }

    @Override
    public String transformName(String name) {
        return name.replace("%catch_rate%", String.join(System.lineSeparator(), UtilPokemonInfo.getCatchRate(this.pokemon)));
    }
}
