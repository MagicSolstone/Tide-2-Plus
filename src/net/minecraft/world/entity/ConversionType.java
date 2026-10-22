package net.minecraft.world.entity;

import java.util.Set;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.scores.Scoreboard;

public enum ConversionType {
   SINGLE(true),
   SPLIT_ON_DEATH(false);

   private static final Set<DataComponentType<?>> COMPONENTS_TO_COPY = Set.of(DataComponents.CUSTOM_NAME, DataComponents.CUSTOM_DATA);
   private final boolean discardAfterConversion;

   private ConversionType(final boolean discardAfterConversion) {
      this.discardAfterConversion = discardAfterConversion;
   }

   public boolean shouldDiscardAfterConversion() {
      return this.discardAfterConversion;
   }

   public abstract void convert(Mob from, Mob to, ConversionParams params);

   private static void convertCommon(final Mob from, final Mob to, final ConversionParams params) {
      to.setAbsorptionAmount(from.getAbsorptionAmount());

      for (MobEffectInstance effect : from.getActiveEffects()) {
         to.addEffect(new MobEffectInstance(effect));
      }

      if (from.isBaby()) {
         to.setBaby(true);
      }

      if (from instanceof AgeableMob oldAgeable && to instanceof AgeableMob convertedAgeable) {
         convertedAgeable.setAge(oldAgeable.getAge());
         convertedAgeable.forcedAge = oldAgeable.forcedAge;
         convertedAgeable.forcedAgeTimer = oldAgeable.forcedAgeTimer;
      }

      Brain<?> oldBrain = from.getBrain();
      Brain<?> convertedBrain = to.getBrain();
      if (oldBrain.checkMemory(MemoryModuleType.ANGRY_AT, MemoryStatus.REGISTERED) && oldBrain.hasMemoryValue(MemoryModuleType.ANGRY_AT)) {
         convertedBrain.setMemory(MemoryModuleType.ANGRY_AT, oldBrain.getMemory(MemoryModuleType.ANGRY_AT));
      }

      if (params.preserveCanPickUpLoot()) {
         to.setCanPickUpLoot(from.canPickUpLoot());
      }

      to.setLeftHanded(from.isLeftHanded());
      to.setNoAi(from.isNoAi());
      if (from.isPersistenceRequired()) {
         to.setPersistenceRequired();
      }

      to.setCustomNameVisible(from.isCustomNameVisible());
      to.setSharedFlagOnFire(from.isOnFire());
      to.setPermanentlyInvulnerable(from.isPermanentlyInvulnerable());
      to.setInvulnerableTime(from.getInvulnerableTime());
      to.setNoGravity(from.isNoGravity());
      to.setPortalCooldown(from.getPortalCooldown());
      to.setSilent(from.isSilent());
      from.entityTags().forEach(to::addTag);

      for (DataComponentType<?> component : COMPONENTS_TO_COPY) {
         copyComponent(from, to, component);
      }

      if (params.team() != null) {
         Scoreboard scoreboard = to.level().getScoreboard();
         scoreboard.addPlayerToTeam(to.getStringUUID(), params.team());
         if (from.getTeam() != null && from.getTeam() == params.team()) {
            scoreboard.removePlayerFromTeam(from.getStringUUID(), from.getTeam());
         }
      }

      if (from instanceof Zombie fromZombie && fromZombie.canBreakDoors() && to instanceof Zombie toZombie) {
         toZombie.setCanBreakDoors(true);
      }
   }

   private static <T> void copyComponent(final Mob from, final Mob to, final DataComponentType<T> componentType) {
      T value = from.get(componentType);
      if (value != null) {
         to.setComponent(componentType, value);
      }
   }
}
