package net.minecraft.network.protocol.game;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.PositionPath;
import net.minecraft.world.entity.PositionStep;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record VecDelta$Stepped(List<VecDelta$Stepped$DeltaStep> steps) implements VecDelta {
   private static final int MIN_BYTES_PER_STEP = 7;

   @Override
   public int stepCount() {
      return this.steps.size();
   }

   @Override
   public boolean hasDeltaX() {
      for (VecDelta$Stepped$DeltaStep step : this.steps) {
         if (step.xa != 0) {
            return true;
         }
      }

      return false;
   }

   @Override
   public boolean hasDeltaZ() {
      for (VecDelta$Stepped$DeltaStep step : this.steps) {
         if (step.za != 0) {
            return true;
         }
      }

      return false;
   }

   @Override
   public PositionPath decode(final VecDeltaCodec positionCodec) {
      if (this.steps.isEmpty()) {
         return PositionPath.of(positionCodec.getBase());
      } else {
         List<PositionStep> output = new ArrayList<>(this.steps.size());
         Vec3 originalBase = positionCodec.getBase();

         for (VecDelta$Stepped$DeltaStep e : this.steps) {
            Vec3 pos = positionCodec.decode(e.xa, e.ya, e.za);
            output.add(new PositionStep(pos, e.ticks));
            positionCodec.setBase(pos);
         }

         positionCodec.setBase(originalBase);
         return PositionPath.stepped(output);
      }
   }

   @Nullable
   public static VecDelta$Stepped tryEncode(final VecDeltaCodec positionCodec, final List<PositionStep> steps) {
      if (steps.isEmpty()) {
         return new VecDelta$Stepped(List.of());
      } else {
         List<VecDelta$Stepped$DeltaStep> output = new ArrayList<>(steps.size());
         Vec3 originalBase = positionCodec.getBase();

         for (PositionStep step : steps) {
            Vec3 pos = step.position();
            long xa = positionCodec.encodeX(pos);
            long ya = positionCodec.encodeY(pos);
            long za = positionCodec.encodeZ(pos);
            if (VecDeltaCodec.isDeltaTooBig(xa, ya, za)) {
               positionCodec.setBase(originalBase);
               return null;
            }

            output.add(new VecDelta$Stepped$DeltaStep((short)xa, (short)ya, (short)za, step.tickOffset()));
            positionCodec.setBase(pos);
         }

         positionCodec.setBase(originalBase);
         return new VecDelta$Stepped(output);
      }
   }
}
