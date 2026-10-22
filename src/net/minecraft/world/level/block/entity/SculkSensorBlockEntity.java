package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener$Provider;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem$Data;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem$Listener;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem$User;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class SculkSensorBlockEntity extends BlockEntity implements GameEventListener$Provider<VibrationSystem$Listener>, VibrationSystem {
   private static final int DEFAULT_LAST_VIBRATION_FREQUENCY = 0;
   private VibrationSystem$Data vibrationData;
   private final VibrationSystem$Listener vibrationListener;
   private final VibrationSystem$User vibrationUser;
   private int lastVibrationFrequency = 0;

   protected SculkSensorBlockEntity(final BlockEntityType<?> type, final BlockPos worldPosition, final BlockState blockState) {
      super(type, worldPosition, blockState);
      this.vibrationUser = this.createVibrationUser();
      this.vibrationData = new VibrationSystem$Data();
      this.vibrationListener = new VibrationSystem$Listener(this);
   }

   public SculkSensorBlockEntity(final BlockPos worldPosition, final BlockState blockState) {
      this(BlockEntityTypes.SCULK_SENSOR, worldPosition, blockState);
   }

   public VibrationSystem$User createVibrationUser() {
      return new SculkSensorBlockEntity$VibrationUser(this, this.getBlockPos());
   }

   @Override
   protected void loadAdditional(final ValueInput input) {
      super.loadAdditional(input);
      this.lastVibrationFrequency = input.getIntOr("last_vibration_frequency", 0);
      this.vibrationData = input.<VibrationSystem$Data>read("listener", VibrationSystem$Data.CODEC).orElseGet(VibrationSystem$Data::new);
   }

   @Override
   protected void saveAdditional(final ValueOutput output) {
      super.saveAdditional(output);
      output.putInt("last_vibration_frequency", this.lastVibrationFrequency);
      output.store("listener", VibrationSystem$Data.CODEC, this.vibrationData);
   }

   @Override
   public VibrationSystem$Data getVibrationData() {
      return this.vibrationData;
   }

   @Override
   public VibrationSystem$User getVibrationUser() {
      return this.vibrationUser;
   }

   public int getLastVibrationFrequency() {
      return this.lastVibrationFrequency;
   }

   public void setLastVibrationFrequency(final int lastVibrationFrequency) {
      this.lastVibrationFrequency = lastVibrationFrequency;
   }

   public VibrationSystem$Listener getListener() {
      return this.vibrationListener;
   }
}
