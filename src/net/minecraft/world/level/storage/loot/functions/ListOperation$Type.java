package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.StringRepresentable;

public enum ListOperation$Type implements StringRepresentable {
   REPLACE_ALL("replace_all", ListOperation$ReplaceAll.MAP_CODEC),
   REPLACE_SECTION("replace_section", ListOperation$ReplaceSection.MAP_CODEC),
   INSERT("insert", ListOperation$Insert.MAP_CODEC),
   APPEND("append", ListOperation$Append.MAP_CODEC);

   public static final Codec<ListOperation$Type> CODEC = StringRepresentable.fromEnum(ListOperation$Type::values);
   private final String id;
   private final MapCodec<? extends ListOperation> mapCodec;

   private ListOperation$Type(final String id, final MapCodec<? extends ListOperation> mapCodec) {
      this.id = id;
      this.mapCodec = mapCodec;
   }

   public MapCodec<? extends ListOperation> mapCodec() {
      return this.mapCodec;
   }

   @Override
   public String getSerializedName() {
      return this.id;
   }
}
