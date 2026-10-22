package net.minecraft.world.scores;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;

public enum Team$Visibility implements StringRepresentable {
   ALWAYS("always", 0),
   NEVER("never", 1),
   HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2),
   HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3);

   public static final Codec<Team$Visibility> CODEC = StringRepresentable.fromEnum(Team$Visibility::values);
   private static final IntFunction<Team$Visibility> BY_ID = ByIdMap.continuous(v -> v.id, values(), ByIdMap$OutOfBoundsStrategy.ZERO);
   public static final StreamCodec<ByteBuf, Team$Visibility> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, v -> v.id);
   public final String name;
   public final int id;

   private Team$Visibility(final String name, final int id) {
      this.name = name;
      this.id = id;
   }

   public Component getDisplayName() {
      return Component.translatable("team.visibility." + this.name);
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
