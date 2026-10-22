package net.minecraft.client.renderer.item.properties.numeric;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.GlobalPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public enum CompassAngleState$CompassTarget implements StringRepresentable {
   NONE("none"),
   LODESTONE("lodestone"),
   SPAWN("spawn"),
   RECOVERY("recovery");

   public static final Codec<CompassAngleState$CompassTarget> CODEC = StringRepresentable.fromEnum(CompassAngleState$CompassTarget::values);
   private final String name;

   private CompassAngleState$CompassTarget(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   @Nullable
   public abstract GlobalPos get(final ClientLevel level, final ItemStack itemStack, @Nullable final ItemOwner entity);
}
