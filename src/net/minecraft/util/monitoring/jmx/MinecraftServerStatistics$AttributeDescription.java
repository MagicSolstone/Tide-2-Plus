package net.minecraft.util.monitoring.jmx;

import java.util.function.Supplier;
import javax.management.MBeanAttributeInfo;

final class MinecraftServerStatistics$AttributeDescription {
   private final String name;
   private final Supplier<Object> getter;
   private final String description;
   private final Class<?> type;

   private MinecraftServerStatistics$AttributeDescription(final String name, final Supplier<Object> getter, final String description, final Class<?> type) {
      this.name = name;
      this.getter = getter;
      this.description = description;
      this.type = type;
   }

   private MBeanAttributeInfo asMBeanAttributeInfo() {
      return new MBeanAttributeInfo(this.name, this.type.getSimpleName(), this.description, true, false, false);
   }
}
