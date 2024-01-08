package com.envyful.better.dex.rewards.forge.config.comparator;

import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class RankComparatorTypeSerializer implements TypeSerializer<RankComparator> {

    @Override
    public RankComparator deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (node.raw() == null){
            return null;
        }

        String id = node.node("id").getString();
        var comparatorType = RankComparatorRegistry.getComparator(id);

        if (comparatorType == null) {
            BetterDexRewards.getLogger().info("Comparator Type " + id + " is not registered: skipping...");
            return null;
        }

        return ObjectMapper.factory().get(comparatorType).load(node);
    }

    @Override
    public void serialize(Type type, RankComparator obj, ConfigurationNode target) throws SerializationException {
        if (obj == null){
            target.raw(null);
            return;
        }

        target.node("id").set(obj.id());
        save(obj, target);
    }

    public <T extends RankComparator> void save(T value, ConfigurationNode target) throws SerializationException {
        Class<T> clazz = (Class<T>) value.getClass();
        ObjectMapper.factory().get(clazz).save(value, target);
    }
}
