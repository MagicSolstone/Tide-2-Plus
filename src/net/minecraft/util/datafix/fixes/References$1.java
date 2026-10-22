package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL.TypeReference;

class References$1 implements TypeReference {
   References$1(final String val$id) {
      this.val$id = val$idx;
   }

   public String typeName() {
      return this.val$id;
   }

   @Override
   public String toString() {
      return "@" + this.val$id;
   }
}
