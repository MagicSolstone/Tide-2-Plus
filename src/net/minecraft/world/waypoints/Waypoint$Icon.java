package net.minecraft.world.waypoints;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.scores.TeamColor;

public class Waypoint$Icon {
   public static final Codec<Waypoint$Icon> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ResourceKey.codec(WaypointStyleAssets.ROOT_ID).fieldOf("style").forGetter(icon -> icon.style),
            ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("color").forGetter(icon -> icon.color)
         )
         .apply(i, Waypoint$Icon::new)
   );
   public static final StreamCodec<ByteBuf, Waypoint$Icon> STREAM_CODEC = StreamCodec.composite(
      ResourceKey.streamCodec(WaypointStyleAssets.ROOT_ID),
      icon -> icon.style,
      ByteBufCodecs.optional(ByteBufCodecs.RGB_COLOR),
      icon -> icon.color,
      Waypoint$Icon::new
   );
   public static final Waypoint$Icon NULL = new Waypoint$Icon();
   public ResourceKey<WaypointStyleAsset> style = WaypointStyleAssets.DEFAULT;
   public Optional<Integer> color = Optional.empty();

   public Waypoint$Icon() {
   }

   private Waypoint$Icon(final ResourceKey<WaypointStyleAsset> style, final Optional<Integer> color) {
      this.style = style;
      this.color = color;
   }

   public boolean hasData() {
      return this.style != WaypointStyleAssets.DEFAULT || this.color.isPresent();
   }

   public Waypoint$Icon cloneAndAssignStyle(final LivingEntity livingEntity) {
      ResourceKey<WaypointStyleAsset> overrideStyle = this.getOverrideStyle();
      Optional<Integer> colorOverride = this.color
         .or(
            () -> Optional.ofNullable(livingEntity.getTeam())
               .flatMap(t -> t.getColor())
               .map(teamColor -> teamColor == TeamColor.BLACK ? -13619152 : teamColor.rgb())
         );
      return overrideStyle == this.style && colorOverride.isEmpty() ? this : new Waypoint$Icon(overrideStyle, colorOverride);
   }

   public void copyFrom(final Waypoint$Icon other) {
      this.color = other.color;
      this.style = other.style;
   }

   private ResourceKey<WaypointStyleAsset> getOverrideStyle() {
      return this.style != WaypointStyleAssets.DEFAULT ? this.style : WaypointStyleAssets.DEFAULT;
   }
}
