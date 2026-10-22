package net.minecraft.nbt;

import com.google.common.collect.Maps;
import java.io.DataInput;
import java.io.IOException;
import java.util.Map;

class CompoundTag$1 implements TagType$VariableSize<CompoundTag> {
   public CompoundTag load(final DataInput input, final NbtAccounter accounter) throws IOException {
      accounter.pushDepth();

      CompoundTag var3;
      try {
         var3 = loadCompound(input, accounter);
      } finally {
         accounter.popDepth();
      }

      return var3;
   }

   private static CompoundTag loadCompound(final DataInput input, final NbtAccounter accounter) throws IOException {
      accounter.accountBytes(48L);
      Map<String, Tag> values = Maps.newHashMap();

      byte tagType;
      while ((tagType = input.readByte()) != 0) {
         String key = readString(input, accounter);
         Tag tag = CompoundTag.readNamedTagData(TagTypes.getType(tagType), key, input, accounter);
         if (values.put(key, tag) == null) {
            accounter.accountBytes(36L);
         }
      }

      return new CompoundTag(values);
   }

   @Override
   public StreamTagVisitor$ValueResult parse(final DataInput input, final StreamTagVisitor output, final NbtAccounter accounter) throws IOException {
      accounter.pushDepth();

      StreamTagVisitor$ValueResult var4;
      try {
         var4 = parseCompound(input, output, accounter);
      } finally {
         accounter.popDepth();
      }

      return var4;
   }

   private static StreamTagVisitor$ValueResult parseCompound(final DataInput input, final StreamTagVisitor output, final NbtAccounter accounter) throws IOException {
      accounter.accountBytes(48L);

      byte tagTypeId;
      label35:
      while ((tagTypeId = input.readByte()) != 0) {
         TagType<?> tagType = TagTypes.getType(tagTypeId);
         switch (output.visitEntry(tagType)) {
            case HALT:
               return StreamTagVisitor$ValueResult.HALT;
            case BREAK:
               StringTag.skipString(input);
               tagType.skip(input, accounter);
               break label35;
            case SKIP:
               StringTag.skipString(input);
               tagType.skip(input, accounter);
               break;
            default:
               String key = readString(input, accounter);
               switch (output.visitEntry(tagType, key)) {
                  case HALT:
                     return StreamTagVisitor$ValueResult.HALT;
                  case BREAK:
                     tagType.skip(input, accounter);
                     break label35;
                  case SKIP:
                     tagType.skip(input, accounter);
                     break;
                  default:
                     accounter.accountBytes(36L);
                     switch (tagType.parse(input, output, accounter)) {
                        case HALT:
                           return StreamTagVisitor$ValueResult.HALT;
                        case BREAK:
                     }
               }
         }
      }

      if (tagTypeId != 0) {
         while ((tagTypeId = input.readByte()) != 0) {
            StringTag.skipString(input);
            TagTypes.getType(tagTypeId).skip(input, accounter);
         }
      }

      return output.visitContainerEnd();
   }

   private static String readString(final DataInput input, final NbtAccounter accounter) throws IOException {
      String key = input.readUTF();
      accounter.accountBytes(28L);
      accounter.accountBytes(2L, key.length());
      return key;
   }

   @Override
   public void skip(final DataInput input, final NbtAccounter accounter) throws IOException {
      accounter.pushDepth();

      byte tagTypeId;
      try {
         while ((tagTypeId = input.readByte()) != 0) {
            StringTag.skipString(input);
            TagTypes.getType(tagTypeId).skip(input, accounter);
         }
      } finally {
         accounter.popDepth();
      }
   }

   @Override
   public String getName() {
      return "COMPOUND";
   }

   @Override
   public String getPrettyName() {
      return "TAG_Compound";
   }
}
