package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;

public class ChainedJsonException$Entry {
   @Nullable
   private String filename;
   private final List<String> jsonKeys = Lists.newArrayList();

   private ChainedJsonException$Entry() {
   }

   private void addJsonKey(final String name) {
      this.jsonKeys.add(0, name);
   }

   @Nullable
   public String getFilename() {
      return this.filename;
   }

   public String getJsonKeys() {
      return StringUtils.join(this.jsonKeys, "->");
   }

   @Override
   public String toString() {
      if (this.filename != null) {
         return this.jsonKeys.isEmpty() ? this.filename : this.filename + " " + this.getJsonKeys();
      } else {
         return this.jsonKeys.isEmpty() ? "(Unknown file)" : "(Unknown file) " + this.getJsonKeys();
      }
   }
}
