package com.li64.tide.data.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.Level;

import java.time.Instant;

public record CatchTimestamp(Instant date, long ticks) {
    public static final Codec<CatchTimestamp> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("date").xmap(Instant::parse, Instant::toString).forGetter(CatchTimestamp::date),
            Codec.LONG.fieldOf("ticks").forGetter(CatchTimestamp::ticks)
    ).apply(instance, CatchTimestamp::new));

    public static CatchTimestamp now(Level level) {
        return new CatchTimestamp(Instant.now(), level.getOverworldClockTime());
    }
}
