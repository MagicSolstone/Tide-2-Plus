package net.minecraft.client.renderer.item.properties.numeric;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class CompassAngle implements RangeSelectItemModelProperty {
   public static final MapCodec<CompassAngle> MAP_CODEC = CompassAngleState.MAP_CODEC.xmap(CompassAngle::new, c -> c.state);
   private final CompassAngleState state;

   public CompassAngle(final boolean wobble, final CompassAngleState$CompassTarget compassTarget) {
      this(new CompassAngleState(wobble, compassTarget));
   }

   private CompassAngle(final CompassAngleState state) {
      this.state = state;
   }

   @Override
   public float get(final ItemStack itemStack, @Nullable final ClientLevel level, @Nullable final ItemOwner owner, final int seed) {
      return this.state.get(itemStack, level, owner, seed);
   }

   @Override
   public MapCodec<CompassAngle> type() {
      return MAP_CODEC;
   }
}
