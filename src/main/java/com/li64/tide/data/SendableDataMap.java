package com.li64.tide.data;

import com.google.common.collect.ImmutableMap;
import com.li64.tide.Tide;
import com.li64.tide.data.fishing.FishData;
import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class SendableDataMap<T> {
    public Map<Identifier, T> data;
    public Codec<T> codec;

    public SendableDataMap(Map<Identifier, T> data, Codec<T> codec) {
        this.data = ImmutableMap.copyOf(data);
        this.codec = codec;
    }

    public T getEntry(Identifier location) {
        return data.get(location);
    }

    public Optional<T> getEntryOptional(Identifier location) {
        return Optional.ofNullable(getEntry(location));
    }

    public Stream<Map.Entry<Identifier, T>> stream() {
        return data.entrySet().parallelStream();
    }

    public Stream<T> valueStream() {
        return data.values().parallelStream();
    }

    public List<T> values() {
        return valueStream().toList();
    }

    public Set<Identifier> getKeys() {
        return data.keySet();
    }

    public int count() {
        return data.size();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(data.size());
        data.keySet().forEach(buf::writeIdentifier);
        data.values().forEach(value -> buf.writeJsonWithCodec(codec, value));
    }

    public static <T> SendableDataMap<T> decode(FriendlyByteBuf buf, Codec<T> codec) {
        int size = buf.readInt();

        ArrayList<Identifier> keys = new ArrayList<>(size);
        ArrayList<T> values = new ArrayList<>(size);

        for (int i = 0; i < size; i++) keys.add(buf.readIdentifier());
        for (int i = 0; i < size; i++) values.add(buf.readLenientJsonWithCodec(codec));

        Map<Identifier, T> map = new HashMap<>();
        for (int i = 0; i < size; i++) map.put(keys.get(i), values.get(i));

        return new SendableDataMap<>(map, codec);
    }

    public static <T> SendableDataMap<T> merge(SendableDataMap<T> map, Map<Identifier, T> other) {
        Map<Identifier, T> merged = new HashMap<>(map.data);
        merged.putAll(other);
        return new SendableDataMap<>(merged, map.codec);
    }

    public void debugLogContents(BiConsumer<Identifier, T> logger) {
        Tide.LOG.info("Logging sendable data map contents:");
        data.forEach(logger);
    }
}