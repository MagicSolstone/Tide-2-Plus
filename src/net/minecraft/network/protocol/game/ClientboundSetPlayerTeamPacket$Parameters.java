package net.minecraft.network.protocol.game;

import java.util.Optional;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.PlayerTeam$OptionFlags;
import net.minecraft.world.scores.Team$CollisionRule;
import net.minecraft.world.scores.Team$Visibility;
import net.minecraft.world.scores.TeamColor;

public record ClientboundSetPlayerTeamPacket$Parameters(
   Component displayName,
   Component playerPrefix,
   Component playerSuffix,
   Team$Visibility nameTagVisibility,
   Team$CollisionRule collisionRule,
   Optional<TeamColor> color,
   @PlayerTeam$OptionFlags byte options
) {
   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetPlayerTeamPacket$Parameters> STREAM_CODEC = StreamCodec.composite(
      ComponentSerialization.TRUSTED_STREAM_CODEC,
      ClientboundSetPlayerTeamPacket$Parameters::displayName,
      ComponentSerialization.TRUSTED_STREAM_CODEC,
      ClientboundSetPlayerTeamPacket$Parameters::playerPrefix,
      ComponentSerialization.TRUSTED_STREAM_CODEC,
      ClientboundSetPlayerTeamPacket$Parameters::playerSuffix,
      Team$Visibility.STREAM_CODEC,
      ClientboundSetPlayerTeamPacket$Parameters::nameTagVisibility,
      Team$CollisionRule.STREAM_CODEC,
      ClientboundSetPlayerTeamPacket$Parameters::collisionRule,
      ByteBufCodecs.optional(TeamColor.STREAM_CODEC),
      ClientboundSetPlayerTeamPacket$Parameters::color,
      ByteBufCodecs.BYTE,
      ClientboundSetPlayerTeamPacket$Parameters::options,
      ClientboundSetPlayerTeamPacket$Parameters::new
   );

   public ClientboundSetPlayerTeamPacket$Parameters(final PlayerTeam team) {
      this(
         team.getDisplayName(),
         team.getPlayerPrefix(),
         team.getPlayerSuffix(),
         team.getNameTagVisibility(),
         team.getCollisionRule(),
         team.getColor(),
         team.packOptions()
      );
   }
}
