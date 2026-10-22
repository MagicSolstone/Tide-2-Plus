package net.minecraft.network.protocol.game;

import io.netty.handler.codec.DecoderException;
import java.lang.runtime.SwitchBootstraps;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.PositionPath;

public sealed interface VecDelta permits VecDelta$Linear, VecDelta$Stepped {
   VecDelta ZERO = new VecDelta$Linear((short)0, (short)0, (short)0);

   int stepCount();

   boolean hasDeltaX();

   boolean hasDeltaZ();

   PositionPath decode(VecDeltaCodec positionCodec);

   static VecDelta read(final FriendlyByteBuf input, final int stepCount) {
      if (stepCount <= 0) {
         short xa = input.readShort();
         short ya = input.readShort();
         short za = input.readShort();
         return new VecDelta$Linear(xa, ya, za);
      } else {
         int maxSteps = input.readableBytes() / 7;
         if (stepCount > maxSteps) {
            throw new DecoderException("VecDelta with size " + stepCount + " is bigger than allowed " + maxSteps);
         } else {
            List<VecDelta$Stepped$DeltaStep> steps = new ArrayList<>(stepCount);

            for (int i = 0; i < stepCount; i++) {
               int ticks = input.readVarInt();
               short xa = input.readShort();
               short ya = input.readShort();
               short za = input.readShort();
               steps.add(new VecDelta$Stepped$DeltaStep(xa, ya, za, ticks));
            }

            return new VecDelta$Stepped(steps);
         }
      }
   }

   static void write(final FriendlyByteBuf output, final VecDelta delta) {
      Objects.requireNonNull(delta);
      VecDelta var2 = delta;
      byte var3 = 0;

      while (true) {
         switch (SwitchBootstraps.typeSwitch<"typeSwitch",VecDelta$Linear,VecDelta$Stepped>(var2, var3)) {
            case 0:
               VecDelta$Linear var4 = (VecDelta$Linear)var2;
               VecDelta$Linear var21 = var4;

               try {
                  var22 = var21.xa();
               } catch (Throwable var15) {
                  throw new MatchException(var15.toString(), var15);
               }

               short var16 = var22;
               if (true) {
                  var21 = var4;

                  try {
                     var24 = var21.ya();
                  } catch (Throwable var14) {
                     throw new MatchException(var14.toString(), var14);
                  }

                  var16 = var24;
                  if (true) {
                     var21 = var4;

                     try {
                        var26 = var21.za();
                     } catch (Throwable var13) {
                        throw new MatchException(var13.toString(), var13);
                     }

                     var16 = var26;
                     if (true) {
                        output.writeShort(var16);
                        output.writeShort(var16);
                        output.writeShort(var16);
                        break;
                     }
                  }
               }

               var3 = 1;
               continue;
            case 1:
               VecDelta$Stepped var8 = (VecDelta$Stepped)var2;
               VecDelta$Stepped var10000 = var8;

               try {
                  var20 = var10000.steps();
               } catch (Throwable var12) {
                  throw new MatchException(var12.toString(), var12);
               }

               for (VecDelta$Stepped$DeltaStep step : var20) {
                  output.writeVarInt(step.ticks);
                  output.writeShort(step.xa);
                  output.writeShort(step.ya);
                  output.writeShort(step.za);
               }
               break;
            default:
               throw new MatchException(null, null);
         }

         return;
      }
   }
}
