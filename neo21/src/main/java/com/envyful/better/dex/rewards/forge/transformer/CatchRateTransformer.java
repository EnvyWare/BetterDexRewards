package com.envyful.better.dex.rewards.forge.transformer;

import com.envyful.api.reforged.pixelmon.UtilPokemonInfo;
import com.envyful.api.text.parse.SimplePlaceholder;
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats;

public class CatchRateTransformer implements SimplePlaceholder {

    private final Stats pokemon;

    public static CatchRateTransformer of(Stats species) {
        return new CatchRateTransformer(species);
    }

    public CatchRateTransformer(Stats pokemon) {
        this.pokemon = pokemon;
    }

    @Override
    public String replace(String name) {
        return name.replace("%catch_rate%", String.join(System.lineSeparator(), String.format("%.2f", UtilPokemonInfo.getCatchRatePercentage(this.pokemon))));
    }
}
