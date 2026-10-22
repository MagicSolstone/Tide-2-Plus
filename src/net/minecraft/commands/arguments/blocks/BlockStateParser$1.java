package net.minecraft.commands.arguments.blocks;

import java.util.function.Consumer;
import net.minecraft.world.level.block.state.properties.Property$Value;

class BlockStateParser$1 implements Consumer<Property$Value<?>> {
   private boolean separate;

   BlockStateParser$1(final StringBuilder val$result) {
      this.val$result = val$resultx;
      this.separate = false;
   }

   public void accept(final Property$Value<?> value) {
      if (this.separate) {
         this.val$result.append(',');
      }

      this.val$result.append(value.property().getName());
      this.val$result.append('=');
      this.val$result.append(value.valueName());
      this.separate = true;
   }
}
