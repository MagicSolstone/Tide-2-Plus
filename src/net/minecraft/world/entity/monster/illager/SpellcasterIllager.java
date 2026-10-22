package net.minecraft.world.entity.monster.illager;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData$Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public abstract class SpellcasterIllager extends AbstractIllager {
   private static final EntityDataAccessor<Byte> DATA_SPELL_CASTING_ID = SynchedEntityData.defineId(SpellcasterIllager.class, EntityDataSerializers.BYTE);
   private static final int DEFAULT_SPELLCASTING_TICKS = 0;
   protected int spellCastingTickCount = 0;
   private SpellcasterIllager$IllagerSpell currentSpell = SpellcasterIllager$IllagerSpell.NONE;

   protected SpellcasterIllager(final EntityType<? extends SpellcasterIllager> type, final Level level) {
      super(type, level);
   }

   @Override
   protected void defineSynchedData(final SynchedEntityData$Builder entityData) {
      super.defineSynchedData(entityData);
      entityData.define(DATA_SPELL_CASTING_ID, (byte)0);
   }

   @Override
   protected void readAdditionalSaveData(final ValueInput input) {
      super.readAdditionalSaveData(input);
      this.spellCastingTickCount = input.getIntOr("SpellTicks", 0);
   }

   @Override
   protected void addAdditionalSaveData(final ValueOutput output) {
      super.addAdditionalSaveData(output);
      output.putInt("SpellTicks", this.spellCastingTickCount);
   }

   @Override
   public AbstractIllager$IllagerArmPose getArmPose() {
      if (this.isCastingSpell()) {
         return AbstractIllager$IllagerArmPose.SPELLCASTING;
      } else {
         return this.isCelebrating() ? AbstractIllager$IllagerArmPose.CELEBRATING : AbstractIllager$IllagerArmPose.CROSSED;
      }
   }

   public boolean isCastingSpell() {
      return this.level().isClientSide() ? this.entityData.get(DATA_SPELL_CASTING_ID) > 0 : this.spellCastingTickCount > 0;
   }

   public void setIsCastingSpell(final SpellcasterIllager$IllagerSpell spell) {
      this.currentSpell = spell;
      this.entityData.set(DATA_SPELL_CASTING_ID, (byte)spell.id);
   }

   protected SpellcasterIllager$IllagerSpell getCurrentSpell() {
      return !this.level().isClientSide() ? this.currentSpell : SpellcasterIllager$IllagerSpell.byId(this.entityData.get(DATA_SPELL_CASTING_ID));
   }

   @Override
   protected void customServerAiStep(final ServerLevel level) {
      super.customServerAiStep(level);
      if (this.spellCastingTickCount > 0) {
         this.spellCastingTickCount--;
      }
   }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide() && this.isCastingSpell()) {
         SpellcasterIllager$IllagerSpell spell = this.getCurrentSpell();
         float red = (float)spell.spellColor[0];
         float green = (float)spell.spellColor[1];
         float blue = (float)spell.spellColor[2];
         float bodyAngle = this.yBodyRot * (float) (Math.PI / 180.0) + Mth.cos(this.tickCount * 0.6662F) * 0.25F;
         float cos = Mth.cos(bodyAngle);
         float sin = Mth.sin(bodyAngle);
         double handDistance = 0.6 * this.getScale();
         double handHeight = 1.8 * this.getScale();
         this.level()
            .addParticle(
               ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, red, green, blue),
               this.getX() + cos * handDistance,
               this.getY() + handHeight,
               this.getZ() + sin * handDistance,
               0.0,
               0.0,
               0.0
            );
         this.level()
            .addParticle(
               ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, red, green, blue),
               this.getX() - cos * handDistance,
               this.getY() + handHeight,
               this.getZ() - sin * handDistance,
               0.0,
               0.0,
               0.0
            );
      }
   }

   protected int getSpellCastingTime() {
      return this.spellCastingTickCount;
   }

   protected abstract SoundEvent getCastingSoundEvent();
}
