package net.minecraft.client.renderer.item.properties.numeric;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public enum Time$TimeSource implements StringRepresentable {
   RANDOM("random"),
   DAYTIME("daytime"),
   MOON_PHASE("moon_phase");

   public static final Codec<Time$TimeSource> CODEC = StringRepresentable.fromEnum(Time$TimeSource::values);
   private final String name;

   private Time$TimeSource(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   public abstract float get(final ClientLevel level, final ItemStack itemStack, final ItemOwner owner, final RandomSource random);
}
