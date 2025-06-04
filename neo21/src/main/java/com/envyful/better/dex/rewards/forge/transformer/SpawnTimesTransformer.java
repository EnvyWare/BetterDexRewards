package com.envyful.better.dex.rewards.forge.transformer;

import com.envyful.api.reforged.pixelmon.UtilPokemonInfo;
import com.envyful.api.text.parse.SimplePlaceholder;
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats;

public class SpawnTimesTransformer implements SimplePlaceholder {

    private final Stats pokemon;

    public static SpawnTimesTransformer of(Stats species) {
        return new SpawnTimesTransformer(species);
    }

    public SpawnTimesTransformer(Stats pokemon) {
        this.pokemon = pokemon;
    }

    @Override
    public String replace(String name) {
        return name.replace("%spawn_times%",  String.join(", ", UtilPokemonInfo.getSpawnTimes(this.pokemon)));
    }
}
