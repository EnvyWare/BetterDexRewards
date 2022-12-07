package com.envyful.better.dex.rewards.forge.transformer;

import com.envyful.api.gui.Transformer;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnSet;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.api.util.helpers.BiomeHelper;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import net.minecraft.world.biome.Biome;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BiomesTransformer implements Transformer {

    private final Stats pokemon;

    public static BiomesTransformer of(Stats species) {
        return new BiomesTransformer(species);
    }

    public BiomesTransformer(Stats pokemon) {
        this.pokemon = pokemon;
    }

    @Override
    public String transformName(String name) {
        return name.replace("%biomes%", String.join("§7,§f ", getSpawnBiomes(this.pokemon)));
    }

    public static List<String> getSpawnBiomes(Stats pokemon) {
        List<String> names = Lists.newArrayList();

        for (SpawnSet next : PixelmonSpawning.getAll().values().stream().flatMap(Collection::stream).collect(Collectors.toList())) {
            for (SpawnInfo spawnInfo : next.spawnInfos) {
                if (!(spawnInfo instanceof SpawnInfoPokemon)) {
                    continue;
                }

                SpawnInfoPokemon spawnInfoPokemon = (SpawnInfoPokemon)spawnInfo;

                if (!spawnInfoPokemon.getSpecies().equals(pokemon.getParentSpecies()) || spawnInfoPokemon.spawnSpecificBossRate != null) {
                    continue;
                }

                for (Biome biome : spawnInfoPokemon.condition.biomes) {
                    names.add(BiomeHelper.getLocalizedBiomeName(biome).getString());
                }
            }
        }

        return names;
    }
}
