package net.minecraft.commands.arguments;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;

public class NbtPathArgument implements ArgumentType<NbtPathArgument$NbtPath> {
   private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo.bar", "foo[0]", "[0]", "[]", "{foo=bar}");
   public static final SimpleCommandExceptionType ERROR_INVALID_NODE = new SimpleCommandExceptionType(Component.translatable("arguments.nbtpath.node.invalid"));
   public static final SimpleCommandExceptionType ERROR_DATA_TOO_DEEP = new SimpleCommandExceptionType(Component.translatable("arguments.nbtpath.too_deep"));
   public static final DynamicCommandExceptionType ERROR_NOTHING_FOUND = new DynamicCommandExceptionType(
      path -> Component.translatableEscape("arguments.nbtpath.nothing_found", path)
   );
   private static final DynamicCommandExceptionType ERROR_EXPECTED_LIST = new DynamicCommandExceptionType(
      node -> Component.translatableEscape("commands.data.modify.expected_list", node)
   );
   private static final DynamicCommandExceptionType ERROR_INVALID_INDEX = new DynamicCommandExceptionType(
      node -> Component.translatableEscape("commands.data.modify.invalid_index", node)
   );
   private static final char INDEX_MATCH_START = '[';
   private static final char INDEX_MATCH_END = ']';
   private static final char KEY_MATCH_START = '{';
   private static final char KEY_MATCH_END = '}';
   private static final char QUOTED_KEY_START = '"';
   private static final char SINGLE_QUOTED_KEY_START = '\'';

   public static NbtPathArgument nbtPath() {
      return new NbtPathArgument();
   }

   public static NbtPathArgument$NbtPath getPath(final CommandContext<CommandSourceStack> context, final String name) {
      return (NbtPathArgument$NbtPath)context.getArgument(name, NbtPathArgument$NbtPath.class);
   }

   public NbtPathArgument$NbtPath parse(final StringReader reader) throws CommandSyntaxException {
      List<NbtPathArgument$Node> nodes = Lists.newArrayList();
      int start = reader.getCursor();
      Object2IntMap<NbtPathArgument$Node> nodeToOriginalPosition = new Object2IntOpenHashMap();
      boolean firstNode = true;

      while (reader.canRead() && reader.peek() != ' ') {
         NbtPathArgument$Node node = parseNode(reader, firstNode);
         nodes.add(node);
         nodeToOriginalPosition.put(node, reader.getCursor() - start);
         firstNode = false;
         if (reader.canRead()) {
            char next = reader.peek();
            if (next != ' ' && next != '[' && next != '{') {
               reader.expect('.');
            }
         }
      }

      return new NbtPathArgument$NbtPath(
         reader.getString().substring(start, reader.getCursor()), nodes.toArray(new NbtPathArgument$Node[0]), nodeToOriginalPosition
      );
   }

   private static NbtPathArgument$Node parseNode(final StringReader reader, final boolean firstNode) throws CommandSyntaxException {
      return (NbtPathArgument$Node)(switch (reader.peek()) {
         case '"', '\'' -> readObjectNode(reader, reader.readString());
         case '[' -> {
            reader.skip();
            int next = reader.peek();
            if (next == 123) {
               CompoundTag pattern = TagParser.parseCompoundAsArgument(reader);
               reader.expect(']');
               yield new NbtPathArgument$MatchElementNode(pattern);
            } else if (next == 93) {
               reader.skip();
               yield NbtPathArgument$AllElementsNode.INSTANCE;
            } else {
               int index = reader.readInt();
               reader.expect(']');
               yield new NbtPathArgument$IndexedElementNode(index);
            }
         }
         case '{' -> {
            if (!firstNode) {
               throw ERROR_INVALID_NODE.createWithContext(reader);
            }

            CompoundTag pattern = TagParser.parseCompoundAsArgument(reader);
            yield new NbtPathArgument$MatchRootObjectNode(pattern);
         }
         default -> readObjectNode(reader, readUnquotedName(reader));
      });
   }

   private static NbtPathArgument$Node readObjectNode(final StringReader reader, final String name) throws CommandSyntaxException {
      if (name.isEmpty()) {
         throw ERROR_INVALID_NODE.createWithContext(reader);
      } else if (reader.canRead() && reader.peek() == '{') {
         CompoundTag pattern = TagParser.parseCompoundAsArgument(reader);
         return new NbtPathArgument$MatchObjectNode(name, pattern);
      } else {
         return new NbtPathArgument$CompoundChildNode(name);
      }
   }

   private static String readUnquotedName(final StringReader reader) throws CommandSyntaxException {
      int start = reader.getCursor();

      while (reader.canRead() && isAllowedInUnquotedName(reader.peek())) {
         reader.skip();
      }

      if (reader.getCursor() == start) {
         throw ERROR_INVALID_NODE.createWithContext(reader);
      } else {
         return reader.getString().substring(start, reader.getCursor());
      }
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }

   private static boolean isAllowedInUnquotedName(final char c) {
      return c != ' ' && c != '"' && c != '\'' && c != '[' && c != ']' && c != '.' && c != '{' && c != '}';
   }

   private static Predicate<Tag> createTagPredicate(final CompoundTag pattern) {
      return tag -> NbtUtils.compareNbt(pattern, tag, true);
   }
}
