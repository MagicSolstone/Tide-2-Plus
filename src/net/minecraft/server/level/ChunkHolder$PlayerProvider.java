package net.minecraft.server.level;

import java.util.List;
import net.minecraft.world.level.ChunkPos;

public interface ChunkHolder$PlayerProvider {
   List<ServerPlayer> getPlayers(ChunkPos pos, boolean borderOnly);
}
