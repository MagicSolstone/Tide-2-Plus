package net.minecraft.data;

import java.util.Objects;

public class DataGenerator$PackGenerator {
   private final boolean toRun;
   private final String providerPrefix;
   private final PackOutput output;

   private DataGenerator$PackGenerator(final DataGenerator this$0, final boolean toRun, final String providerPrefix, final PackOutput output) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.toRun = toRun;
      this.providerPrefix = providerPrefix;
      this.output = output;
   }

   public <T extends DataProvider> T addProvider(final DataProvider$Factory<T> factory) {
      T provider = factory.create(this.output);
      String providerId = this.providerPrefix + "/" + provider.getName();
      if (!this.this$0.allProviderIds.add(providerId)) {
         throw new IllegalStateException("Duplicate provider: " + providerId);
      } else {
         if (this.toRun) {
            this.this$0.providersToRun.put(providerId, provider);
         }

         return provider;
      }
   }
}
