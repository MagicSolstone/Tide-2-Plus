package net.minecraft.world.timeline;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.KeyframeTrack$Builder;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.modifier.AttributeModifier;
import net.minecraft.world.clock.ClockTimeMarker;
import net.minecraft.world.clock.WorldClock;

public class Timeline$Builder {
   private final Holder<WorldClock> clock;
   private Optional<Integer> periodTicks = Optional.empty();
   private final Builder<EnvironmentAttribute<?>, AttributeTrack<?, ?>> tracks = ImmutableMap.builder();
   private final Builder<ResourceKey<ClockTimeMarker>, Timeline$TimeMarkerInfo> timeMarkers = ImmutableMap.builder();

   private Timeline$Builder(final Holder<WorldClock> clock) {
      this.clock = clock;
   }

   public Timeline$Builder setPeriodTicks(final int periodTicks) {
      this.periodTicks = Optional.of(periodTicks);
      return this;
   }

   public <Value, Argument> Timeline$Builder addModifierTrack(
      final EnvironmentAttribute<Value> attribute, final AttributeModifier<Value, Argument> modifier, final Consumer<KeyframeTrack$Builder<Argument>> builder
   ) {
      attribute.type().checkAllowedModifier(modifier);
      KeyframeTrack$Builder<Argument> argumentTrack = new KeyframeTrack$Builder<>();
      builder.accept(argumentTrack);
      this.tracks.put(attribute, new AttributeTrack<>(modifier, argumentTrack.build()));
      return this;
   }

   public <Value> Timeline$Builder addTrack(final EnvironmentAttribute<Value> attribute, final Consumer<KeyframeTrack$Builder<Value>> builder) {
      return this.addModifierTrack(attribute, AttributeModifier.override(), builder);
   }

   public Timeline$Builder addTimeMarker(final ResourceKey<ClockTimeMarker> id, final int ticks) {
      return this.addTimeMarker(id, ticks, false);
   }

   public Timeline$Builder addTimeMarker(final ResourceKey<ClockTimeMarker> id, final int ticks, final boolean showInCommands) {
      this.timeMarkers.put(id, new Timeline$TimeMarkerInfo(ticks, showInCommands));
      return this;
   }

   public Timeline build() {
      return new Timeline(this.clock, this.periodTicks, this.tracks.build(), this.timeMarkers.build());
   }
}
