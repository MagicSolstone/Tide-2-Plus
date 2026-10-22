package net.minecraft.gametest.framework;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.attribute.EnvironmentAttributeSystem$Builder;
import net.minecraft.world.timeline.Timeline;

public record TestEnvironmentDefinition$Timelines(List<Holder<Timeline>> timelines) implements TestEnvironmentDefinition<EnvironmentAttributeSystem> {
   public static final MapCodec<TestEnvironmentDefinition$Timelines> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(Timeline.CODEC.listOf().fieldOf("timelines").forGetter(TestEnvironmentDefinition$Timelines::timelines))
         .apply(i, TestEnvironmentDefinition$Timelines::new)
   );

   public EnvironmentAttributeSystem setup(final ServerLevel level) {
      EnvironmentAttributeSystem$Builder builder = EnvironmentAttributeSystem.builder().addDefaultLayers(level);

      for (Holder<Timeline> timeline : this.timelines) {
         builder.addTimelineLayer(timeline, level.clockManager());
      }

      return level.setEnvironmentAttributes(builder.build());
   }

   public void teardown(final ServerLevel level, final EnvironmentAttributeSystem saveData) {
      level.setEnvironmentAttributes(saveData);
   }

   @Override
   public MapCodec<TestEnvironmentDefinition$Timelines> codec() {
      return CODEC;
   }
}
