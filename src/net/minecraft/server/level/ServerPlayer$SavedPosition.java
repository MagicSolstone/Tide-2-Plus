package net.minecraft.server.level;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public record ServerPlayer$SavedPosition(Optional<ResourceKey<Level>> dimension, Optional<Vec3> position, Optional<Vec2> rotation) {
   public static final MapCodec<ServerPlayer$SavedPosition> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Level.RESOURCE_KEY_CODEC.optionalFieldOf("Dimension").forGetter(ServerPlayer$SavedPosition::dimension),
            Vec3.CODEC.optionalFieldOf("Pos").forGetter(ServerPlayer$SavedPosition::position),
            Vec2.CODEC.optionalFieldOf("Rotation").forGetter(ServerPlayer$SavedPosition::rotation)
         )
         .apply(i, ServerPlayer$SavedPosition::new)
   );
   public static final ServerPlayer$SavedPosition EMPTY = new ServerPlayer$SavedPosition(Optional.empty(), Optional.empty(), Optional.empty());
}
