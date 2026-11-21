package com.envyful.better.dex.rewards.forge.transformer;

import com.envyful.api.text.Placeholder;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.DexCompletion;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * The per-stage placeholders for stage items: `%required_dex%` (the threshold after the player's
 * bonuses), `%base_dex%` (as configured) and `%bonuses%`. `%bonuses%` is multi-line, so like every
 * EnvyWare multi-line placeholder it only expands on a lore line that is exactly `%bonuses%`; it
 * becomes one line per bonus, or nothing when the stage has none.
 *
 */
public final class StageTransformer {

    private StageTransformer() {}

    public static List<Placeholder> of(ServerPlayer player, DexRewardsAttribute attribute, DexCompletion stage) {
        var required = format(stage.getEffectiveRequiredDex(player));
        var base = format(stage.getBaseRequiredDex());

        return List.of(
                Placeholder.multiLine("%bonuses%", bonusLines(player, attribute, stage)),
                Placeholder.simple(line -> line
                        .replace("%required_dex%", required)
                        .replace("%base_dex%", base))
        );
    }

    private static List<String> bonusLines(ServerPlayer player, DexRewardsAttribute attribute, DexCompletion stage) {
        var format = BetterDexRewards.getConfig().getBonusLines();
        var lines = new ArrayList<String>();

        for (var bonus : stage.getBonuses()) {
            String line;

            if (attribute.hasClaimed(stage, bonus)) {
                line = format.getClaimed();
            } else if (bonus.meets(player)) {
                line = format.getMet();
            } else {
                line = format.getUnmet();
            }

            lines.add(line
                    .replace("%description%", bonus.getDescription())
                    .replace("%reduction%", format(bonus.getDexReduction())));
        }

        return lines;
    }

    /**
     *
     * Whole numbers without a decimal point (a count of 150 reads `150`, not `150.0`), anything else to
     * at most two places.
     *
     */
    public static String format(double value) {
        if (value == Math.rint(value)) {
            return String.valueOf((long) value);
        }

        return String.format(Locale.ROOT, "%.2f", value).replaceAll("0+$", "");
    }
}
