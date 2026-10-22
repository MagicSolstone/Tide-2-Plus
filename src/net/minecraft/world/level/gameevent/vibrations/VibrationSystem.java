package net.minecraft.world.level.gameevent.vibrations;

import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import java.util.List;
import java.util.function.ToIntFunction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.level.gameevent.GameEvent;

public interface VibrationSystem {
   List<ResourceKey<GameEvent>> RESONANCE_EVENTS = List.of(
      GameEvent.RESONATE_1.key(),
      GameEvent.RESONATE_2.key(),
      GameEvent.RESONATE_3.key(),
      GameEvent.RESONATE_4.key(),
      GameEvent.RESONATE_5.key(),
      GameEvent.RESONATE_6.key(),
      GameEvent.RESONATE_7.key(),
      GameEvent.RESONATE_8.key(),
      GameEvent.RESONATE_9.key(),
      GameEvent.RESONATE_10.key(),
      GameEvent.RESONATE_11.key(),
      GameEvent.RESONATE_12.key(),
      GameEvent.RESONATE_13.key(),
      GameEvent.RESONATE_14.key(),
      GameEvent.RESONATE_15.key()
   );
   int NO_VIBRATION_FREQUENCY = 0;
   ToIntFunction<ResourceKey<GameEvent>> VIBRATION_FREQUENCY_FOR_EVENT = Util.make(new Reference2IntOpenHashMap(), map -> {
      map.defaultReturnValue(0);
      map.put(GameEvent.STEP.key(), 1);
      map.put(GameEvent.SWIM.key(), 1);
      map.put(GameEvent.FLAP.key(), 1);
      map.put(GameEvent.PROJECTILE_LAND.key(), 2);
      map.put(GameEvent.HIT_GROUND.key(), 2);
      map.put(GameEvent.SPLASH.key(), 2);
      map.put(GameEvent.BOUNCE.key(), 2);
      map.put(GameEvent.ITEM_INTERACT_FINISH.key(), 3);
      map.put(GameEvent.PROJECTILE_SHOOT.key(), 3);
      map.put(GameEvent.INSTRUMENT_PLAY.key(), 3);
      map.put(GameEvent.ENTITY_ACTION.key(), 4);
      map.put(GameEvent.ELYTRA_GLIDE.key(), 4);
      map.put(GameEvent.UNEQUIP.key(), 4);
      map.put(GameEvent.ENTITY_DISMOUNT.key(), 5);
      map.put(GameEvent.EQUIP.key(), 5);
      map.put(GameEvent.ENTITY_INTERACT.key(), 6);
      map.put(GameEvent.SHEAR.key(), 6);
      map.put(GameEvent.ENTITY_MOUNT.key(), 6);
      map.put(GameEvent.ENTITY_DAMAGE.key(), 7);
      map.put(GameEvent.DRINK.key(), 8);
      map.put(GameEvent.EAT.key(), 8);
      map.put(GameEvent.CONTAINER_CLOSE.key(), 9);
      map.put(GameEvent.BLOCK_CLOSE.key(), 9);
      map.put(GameEvent.BLOCK_DEACTIVATE.key(), 9);
      map.put(GameEvent.BLOCK_DETACH.key(), 9);
      map.put(GameEvent.CONTAINER_OPEN.key(), 10);
      map.put(GameEvent.BLOCK_OPEN.key(), 10);
      map.put(GameEvent.BLOCK_ACTIVATE.key(), 10);
      map.put(GameEvent.BLOCK_ATTACH.key(), 10);
      map.put(GameEvent.PRIME_FUSE.key(), 10);
      map.put(GameEvent.NOTE_BLOCK_PLAY.key(), 10);
      map.put(GameEvent.BLOCK_CHANGE.key(), 11);
      map.put(GameEvent.BLOCK_DESTROY.key(), 12);
      map.put(GameEvent.FLUID_PICKUP.key(), 12);
      map.put(GameEvent.BLOCK_PLACE.key(), 13);
      map.put(GameEvent.FLUID_PLACE.key(), 13);
      map.put(GameEvent.ENTITY_PLACE.key(), 14);
      map.put(GameEvent.LIGHTNING_STRIKE.key(), 14);
      map.put(GameEvent.TELEPORT.key(), 14);
      map.put(GameEvent.ENTITY_DIE.key(), 15);
      map.put(GameEvent.EXPLODE.key(), 15);

      for (int i = 1; i <= 15; i++) {
         map.put(getResonanceEventByFrequency(i), i);
      }
   });

   VibrationSystem$Data getVibrationData();

   VibrationSystem$User getVibrationUser();

   static int getGameEventFrequency(final Holder<GameEvent> event) {
      return event.unwrapKey().map(VibrationSystem::getGameEventFrequency).orElse(0);
   }

   static int getGameEventFrequency(final ResourceKey<GameEvent> event) {
      return VIBRATION_FREQUENCY_FOR_EVENT.applyAsInt(event);
   }

   static ResourceKey<GameEvent> getResonanceEventByFrequency(final int vibrationFrequency) {
      return RESONANCE_EVENTS.get(vibrationFrequency - 1);
   }

   static int getRedstoneStrengthForDistance(final float distance, final int listenerRadius) {
      double powerScale = 15.0 / listenerRadius;
      return Math.max(1, 15 - Mth.floor(powerScale * distance));
   }
}
