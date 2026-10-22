package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import java.util.List;

public class ListOperation$ReplaceAll implements ListOperation {
   public static final ListOperation$ReplaceAll INSTANCE = new ListOperation$ReplaceAll();
   public static final MapCodec<ListOperation$ReplaceAll> MAP_CODEC = MapCodec.unit(() -> INSTANCE);

   private ListOperation$ReplaceAll() {
   }

   @Override
   public ListOperation$Type mode() {
      return ListOperation$Type.REPLACE_ALL;
   }

   @Override
   public <T> List<T> apply(final List<T> original, final List<T> replacement, final int maxSize) {
      return replacement;
   }
}
