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

public enum Team$CollisionRule implements StringRepresentable {
   ALWAYS("always", 0),
   NEVER("never", 1),
   PUSH_OTHER_TEAMS("pushOtherTeams", 2),
   PUSH_OWN_TEAM("pushOwnTeam", 3);

   public static final Codec<Team$CollisionRule> CODEC = StringRepresentable.fromEnum(Team$CollisionRule::values);
   private static final IntFunction<Team$CollisionRule> BY_ID = ByIdMap.continuous(r -> r.id, values(), ByIdMap$OutOfBoundsStrategy.ZERO);
   public static final StreamCodec<ByteBuf, Team$CollisionRule> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, r -> r.id);
   public final String name;
   public final int id;

   private Team$CollisionRule(final String name, final int id) {
      this.name = name;
      this.id = id;
   }

   public Component getDisplayName() {
      return Component.translatable("team.collision." + this.name);
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
