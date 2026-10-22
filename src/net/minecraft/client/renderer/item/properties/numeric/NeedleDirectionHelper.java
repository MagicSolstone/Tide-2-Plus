package net.minecraft.client.renderer.item.properties.numeric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class NeedleDirectionHelper {
   private final boolean wobble;

   protected NeedleDirectionHelper(final boolean wobble) {
      this.wobble = wobble;
   }

   public float get(final ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable final ItemOwner owner, final int seed) {
      if (owner == null) {
         return 0.0F;
      } else {
         if (clientLevel == null && owner.level() instanceof ClientLevel level) {
            clientLevel = level;
         }

         return clientLevel == null ? 0.0F : this.calculate(itemStack, clientLevel, seed, owner);
      }
   }

   protected abstract float calculate(final ItemStack itemStack, final ClientLevel level, final int seed, final ItemOwner owner);

   protected boolean wobble() {
      return this.wobble;
   }

   protected NeedleDirectionHelper$Wobbler newWobbler(final float factor) {
      return this.wobble ? standardWobbler(factor) : nonWobbler();
   }

   public static NeedleDirectionHelper$Wobbler standardWobbler(final float factor) {
      return new NeedleDirectionHelper$1(factor);
   }

   public static NeedleDirectionHelper$Wobbler nonWobbler() {
      return new NeedleDirectionHelper$2();
   }
}
