package net.minecraft.util.debug;

import net.minecraft.server.level.ServerLevel;

public interface DebugValueSource {
   void registerDebugValues(ServerLevel level, DebugValueSource$Registration registration);
}
