package net.minecraft.client.renderer.item.properties.numeric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.LodestoneTracker;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
enum CompassAngleState$CompassTarget$2 {
   private CompassAngleState$CompassTarget$2(final String name) {
   }

   @Nullable
   @Override
   public GlobalPos get(final ClientLevel level, final ItemStack itemStack, @Nullable final ItemOwner owner) {
      LodestoneTracker tracker = itemStack.get(DataComponents.LODESTONE_TRACKER);
      return tracker != null ? tracker.target().orElse(null) : null;
   }
}
