package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;

class ListTag$1 implements TagType$VariableSize<ListTag> {
   public ListTag load(final DataInput input, final NbtAccounter accounter) throws IOException {
      accounter.pushDepth();

      ListTag var3;
      try {
         var3 = loadList(input, accounter);
      } finally {
         accounter.popDepth();
      }

      return var3;
   }

   private static ListTag loadList(final DataInput input, final NbtAccounter accounter) throws IOException {
      accounter.accountBytes(36L);
      byte typeId = input.readByte();
      int count = readListCount(input);
      if (typeId == 0 && count > 0) {
         throw new NbtFormatException("Missing type on ListTag");
      } else {
         accounter.accountBytes(4L, count);
         TagType<?> type = TagTypes.getType(typeId);
         ListTag list = new ListTag(new ArrayList<>(count));

         for (int i = 0; i < count; i++) {
            list.addAndUnwrap(type.load(input, accounter));
         }

         return list;
      }
   }

   @Override
   public StreamTagVisitor$ValueResult parse(final DataInput input, final StreamTagVisitor output, final NbtAccounter accounter) throws IOException {
      accounter.pushDepth();

      StreamTagVisitor$ValueResult var4;
      try {
         var4 = parseList(input, output, accounter);
      } finally {
         accounter.popDepth();
      }

      return var4;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private static StreamTagVisitor$ValueResult parseList(final DataInput input, final StreamTagVisitor output, final NbtAccounter accounter) throws IOException {
      accounter.accountBytes(36L);
      TagType<?> elementType = TagTypes.getType(input.readByte());
      int count = readListCount(input);
      switch (output.visitList(elementType, count)) {
         case HALT:
            return StreamTagVisitor$ValueResult.HALT;
         case BREAK:
            elementType.skip(input, count, accounter);
            return output.visitContainerEnd();
         default:
            accounter.accountBytes(4L, count);
            int i = 0;

            while (true) {
               label41: {
                  if (i < count) {
                     switch (output.visitElement(elementType, i)) {
                        case HALT:
                           return StreamTagVisitor$ValueResult.HALT;
                        case BREAK:
                           elementType.skip(input, accounter);
                           break;
                        case SKIP:
                           elementType.skip(input, accounter);
                           break label41;
                        default:
                           switch (elementType.parse(input, output, accounter)) {
                              case HALT:
                                 return StreamTagVisitor$ValueResult.HALT;
                              case BREAK:
                                 break;
                              default:
                                 break label41;
                           }
                     }
                  }

                  int amountToSkip = count - 1 - i;
                  if (amountToSkip > 0) {
                     elementType.skip(input, amountToSkip, accounter);
                  }

                  return output.visitContainerEnd();
               }

               i++;
            }
      }
   }

   private static int readListCount(final DataInput input) throws IOException {
      int count = input.readInt();
      if (count < 0) {
         throw new NbtFormatException("ListTag length cannot be negative: " + count);
      } else {
         return count;
      }
   }

   @Override
   public void skip(final DataInput input, final NbtAccounter accounter) throws IOException {
      accounter.pushDepth();

      try {
         TagType<?> type = TagTypes.getType(input.readByte());
         int count = input.readInt();
         type.skip(input, count, accounter);
      } finally {
         accounter.popDepth();
      }
   }

   @Override
   public String getName() {
      return "LIST";
   }

   @Override
   public String getPrettyName() {
      return "TAG_List";
   }
}
