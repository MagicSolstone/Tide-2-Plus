package net.minecraft.client.multiplayer.chat;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringRepresentable;

@Environment(EnvType.CLIENT)
public interface LoggedChatEvent {
   Codec<LoggedChatEvent> CODEC = StringRepresentable.fromEnum(LoggedChatEvent$Type::values).dispatch(LoggedChatEvent::type, LoggedChatEvent$Type::codec);

   LoggedChatEvent$Type type();
}
