package net.minecraft.network.chat;

import com.mojang.serialization.Codec;

public interface HoverEvent {
   Codec<HoverEvent> CODEC = HoverEvent$Action.CODEC.dispatch("action", HoverEvent::action, action -> action.codec);

   HoverEvent$Action action();
}
