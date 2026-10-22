package net.minecraft.world.scores;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

public record PlayerTeam$Packed(
   String name,
   Optional<Component> displayName,
   Optional<TeamColor> color,
   boolean allowFriendlyFire,
   boolean seeFriendlyInvisibles,
   Component memberNamePrefix,
   Component memberNameSuffix,
   Team$Visibility nameTagVisibility,
   Team$Visibility deathMessageVisibility,
   Team$CollisionRule collisionRule,
   List<String> players
) {
   public static final Codec<PlayerTeam$Packed> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.STRING.fieldOf("Name").forGetter(PlayerTeam$Packed::name),
            ComponentSerialization.CODEC.optionalFieldOf("DisplayName").forGetter(PlayerTeam$Packed::displayName),
            TeamColor.CODEC.optionalFieldOf("TeamColor").forGetter(PlayerTeam$Packed::color),
            Codec.BOOL.optionalFieldOf("AllowFriendlyFire", true).forGetter(PlayerTeam$Packed::allowFriendlyFire),
            Codec.BOOL.optionalFieldOf("SeeFriendlyInvisibles", true).forGetter(PlayerTeam$Packed::seeFriendlyInvisibles),
            ComponentSerialization.CODEC.optionalFieldOf("MemberNamePrefix", CommonComponents.EMPTY).forGetter(PlayerTeam$Packed::memberNamePrefix),
            ComponentSerialization.CODEC.optionalFieldOf("MemberNameSuffix", CommonComponents.EMPTY).forGetter(PlayerTeam$Packed::memberNameSuffix),
            Team$Visibility.CODEC.optionalFieldOf("NameTagVisibility", Team$Visibility.ALWAYS).forGetter(PlayerTeam$Packed::nameTagVisibility),
            Team$Visibility.CODEC.optionalFieldOf("DeathMessageVisibility", Team$Visibility.ALWAYS).forGetter(PlayerTeam$Packed::deathMessageVisibility),
            Team$CollisionRule.CODEC.optionalFieldOf("CollisionRule", Team$CollisionRule.ALWAYS).forGetter(PlayerTeam$Packed::collisionRule),
            Codec.STRING.listOf().optionalFieldOf("Players", List.of()).forGetter(PlayerTeam$Packed::players)
         )
         .apply(i, PlayerTeam$Packed::new)
   );
}
