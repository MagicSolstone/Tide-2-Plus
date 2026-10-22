package net.minecraft.world.level.storage.loot.functions;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.stream.Stream;
import org.slf4j.Logger;

public class ListOperation$Append implements ListOperation {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final ListOperation$Append INSTANCE = new ListOperation$Append();
   public static final MapCodec<ListOperation$Append> MAP_CODEC = MapCodec.unit(() -> INSTANCE);

   private ListOperation$Append() {
   }

   @Override
   public ListOperation$Type mode() {
      return ListOperation$Type.APPEND;
   }

   @Override
   public <T> List<T> apply(final List<T> original, final List<T> replacement, final int maxSize) {
      if (original.size() + replacement.size() > maxSize) {
         LOGGER.error("Contents overflow in section append");
         return original;
      } else {
         return Stream.concat(original.stream(), replacement.stream()).toList();
      }
   }
}
