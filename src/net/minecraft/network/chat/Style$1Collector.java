package net.minecraft.network.chat;

import java.util.Objects;
import org.jspecify.annotations.Nullable;

class Style$1Collector {
   private boolean isNotFirst;

   Style$1Collector(final Style this$0, final StringBuilder val$result) {
      this.val$result = val$resultx;
      Objects.requireNonNull(this$0);
      super();
   }

   private void prependSeparator() {
      if (this.isNotFirst) {
         this.val$result.append(',');
      }

      this.isNotFirst = true;
   }

   private void addFlagString(final String name, @Nullable final Boolean value) {
      if (value != null) {
         this.prependSeparator();
         if (!value) {
            this.val$result.append('!');
         }

         this.val$result.append(name);
      }
   }

   private void addValueString(final String name, @Nullable final Object value) {
      if (value != null) {
         this.prependSeparator();
         this.val$result.append(name);
         this.val$result.append('=');
         this.val$result.append(value);
      }
   }
}
