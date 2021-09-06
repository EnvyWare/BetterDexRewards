package com.envyful.better.dex.rewards.forge.listener;

import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DexRewardsListener {

    private final BetterDexRewards mod;

    public DexRewardsListener(BetterDexRewards mod) {
        this.mod = mod;

        Pixelmon.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPokemonCatch(CaptureEvent.SuccessfulCapture event) {
        if (event.)
    }

}
