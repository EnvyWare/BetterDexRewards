package com.envyful.better.dex.rewards.forge.transformer;

import com.envyful.api.gui.Transformer;
import com.envyful.api.reforged.pixelmon.UtilPokemonInfo;
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats;

public class SpawnTimesTransformer implements Transformer {

    private final Stats pokemon;

    public static SpawnTimesTransformer of(Stats species) {
        return new SpawnTimesTransformer(species);
    }

    public SpawnTimesTransformer(Stats pokemon) {
        this.pokemon = pokemon;
    }

    @Override
    public String transformName(String name) {
        return name.replace("%spawn_times%",  String.join(", ", UtilPokemonInfo.getSpawnTimes(this.pokemon)));
    }
}
