package net.minecraft.client.animation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.AnimationState;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class KeyframeAnimation {
   private final AnimationDefinition definition;
   private final List<KeyframeAnimation$Entry> entries;

   private KeyframeAnimation(final AnimationDefinition definition, final List<KeyframeAnimation$Entry> entries) {
      this.definition = definition;
      this.entries = entries;
   }

   static KeyframeAnimation bake(final ModelPart root, final AnimationDefinition definition) {
      List<KeyframeAnimation$Entry> entries = new ArrayList<>();
      Function<String, ModelPart> partLookup = root.createPartLookup();

      for (Map.Entry<String, List<AnimationChannel>> entry : definition.boneAnimations().entrySet()) {
         String partName = entry.getKey();
         List<AnimationChannel> channels = entry.getValue();
         ModelPart part = partLookup.apply(partName);
         if (part == null) {
            throw new IllegalArgumentException("Cannot animate " + partName + ", which does not exist in model");
         }

         for (AnimationChannel channel : channels) {
            entries.add(new KeyframeAnimation$Entry(part, channel.target(), channel.keyframes()));
         }
      }

      return new KeyframeAnimation(definition, List.copyOf(entries));
   }

   public void applyStatic() {
      this.apply(0L, 1.0F);
   }

   public void applyWalk(final float animationPos, final float animationSpeed, final float speedFactor, final float scaleFactor) {
      long time = (long)(animationPos * 50.0F * speedFactor);
      float scale = Math.min(animationSpeed * scaleFactor, 1.0F);
      this.apply(time, scale);
   }

   public void apply(final AnimationState animationState, final float currentTime) {
      this.apply(animationState, currentTime, 1.0F);
   }

   public void apply(final AnimationState animationState, final float currentTime, final float speedFactor) {
      animationState.ifStarted(state -> this.apply((long)((float)state.getTimeInMillis(currentTime) * speedFactor), 1.0F));
   }

   public void apply(final long millisSinceStart, final float targetScale) {
      float secondsSinceStart = this.getElapsedSeconds(millisSinceStart);
      Vector3f scratchVector = new Vector3f();

      for (KeyframeAnimation$Entry entry : this.entries) {
         entry.apply(secondsSinceStart, targetScale, scratchVector);
      }
   }

   private float getElapsedSeconds(final long millisSinceStart) {
      float secondsSinceStart = (float)millisSinceStart / 1000.0F;
      return this.definition.looping() ? secondsSinceStart % this.definition.lengthInSeconds() : secondsSinceStart;
   }
}
