package com.envyful.better.dex.rewards.forge.config.comparator;

import com.envyful.better.dex.rewards.forge.config.comparator.type.CountRankComparator;
import com.envyful.better.dex.rewards.forge.config.comparator.type.PercentageRankComparator;
import com.google.common.collect.Maps;

import java.util.Map;

public class RankComparatorRegistry {

    private static final Map<String, Class<? extends RankComparator>> REGISTERED = Maps.newConcurrentMap();

    public static void init() {
        register("count", CountRankComparator.class);
        register("percentage", PercentageRankComparator.class);
    }

    public static void register(String id, Class<? extends RankComparator> comparator) {
        REGISTERED.put(id, comparator);
    }

    public static Class<? extends RankComparator> getComparator(String id) {
        return REGISTERED.get(id);
    }
}
