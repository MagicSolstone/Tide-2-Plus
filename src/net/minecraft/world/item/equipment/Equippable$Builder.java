package net.minecraft.world.item.equipment;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;

public class Equippable$Builder {
   private final EquipmentSlot slot;
   private Holder<SoundEvent> equipSound = SoundEvents.ARMOR_EQUIP_GENERIC;
   private Optional<ResourceKey<EquipmentAsset>> assetId = Optional.empty();
   private Optional<Identifier> cameraOverlay = Optional.empty();
   private Optional<HolderSet<EntityType<?>>> allowedEntities = Optional.empty();
   private boolean dispensable = true;
   private boolean swappable = true;
   private boolean damageOnHurt = true;
   private boolean equipOnInteract;
   private boolean canBeSheared;
   private Holder<SoundEvent> shearingSound = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.SHEARS_SNIP);

   private Equippable$Builder(final EquipmentSlot slot) {
      this.slot = slot;
   }

   public Equippable$Builder setEquipSound(final Holder<SoundEvent> equipSound) {
      this.equipSound = equipSound;
      return this;
   }

   public Equippable$Builder setAsset(final ResourceKey<EquipmentAsset> assetId) {
      this.assetId = Optional.of(assetId);
      return this;
   }

   public Equippable$Builder setCameraOverlay(final Identifier cameraOverlay) {
      this.cameraOverlay = Optional.of(cameraOverlay);
      return this;
   }

   public Equippable$Builder setAllowedEntities(final EntityType<?>... allowedEntities) {
      return this.setAllowedEntities(HolderSet.direct(EntityType::builtInRegistryHolder, allowedEntities));
   }

   public Equippable$Builder setAllowedEntities(final HolderSet<EntityType<?>> allowedEntities) {
      this.allowedEntities = Optional.of(allowedEntities);
      return this;
   }

   public Equippable$Builder setDispensable(final boolean dispensable) {
      this.dispensable = dispensable;
      return this;
   }

   public Equippable$Builder setSwappable(final boolean swappable) {
      this.swappable = swappable;
      return this;
   }

   public Equippable$Builder setDamageOnHurt(final boolean damageOnHurt) {
      this.damageOnHurt = damageOnHurt;
      return this;
   }

   public Equippable$Builder setEquipOnInteract(final boolean equipOnInteract) {
      this.equipOnInteract = equipOnInteract;
      return this;
   }

   public Equippable$Builder setCanBeSheared(final boolean canBeSheared) {
      this.canBeSheared = canBeSheared;
      return this;
   }

   public Equippable$Builder setShearingSound(final Holder<SoundEvent> shearingSound) {
      this.shearingSound = shearingSound;
      return this;
   }

   public Equippable build() {
      return new Equippable(
         this.slot,
         this.equipSound,
         this.assetId,
         this.cameraOverlay,
         this.allowedEntities,
         this.dispensable,
         this.swappable,
         this.damageOnHurt,
         this.equipOnInteract,
         this.canBeSheared,
         this.shearingSound
      );
   }
}
