package net.minecraft.world.level.timers;

import com.google.common.primitives.UnsignedLong;

public record TimerQueue$Event<T>(long triggerTime, UnsignedLong sequentialId, String id, TimerCallback<T> callback) {
}
