package net.minecraft.network.protocol.game;

import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.function.BiPredicate;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfo$Template;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class ClientboundCommandsPacket implements Packet<ClientGamePacketListener> {
   private static final StreamCodec<FriendlyByteBuf, ClientboundCommandsPacket$Entry> ENTRY_STREAM_CODEC = StreamCodec.of(
      (output, entry) -> entry.write(output), ClientboundCommandsPacket::readNode
   );
   public static final StreamCodec<FriendlyByteBuf, ClientboundCommandsPacket> STREAM_CODEC = StreamCodec.composite(
      ENTRY_STREAM_CODEC.apply(ByteBufCodecs.list()), p -> p.entries, ByteBufCodecs.VAR_INT, p -> p.rootIndex, ClientboundCommandsPacket::new
   );
   private static final byte MASK_TYPE = 3;
   private static final byte FLAG_EXECUTABLE = 4;
   private static final byte FLAG_REDIRECT = 8;
   private static final byte FLAG_CUSTOM_SUGGESTIONS = 16;
   private static final byte FLAG_RESTRICTED = 32;
   private static final byte TYPE_ROOT = 0;
   private static final byte TYPE_LITERAL = 1;
   private static final byte TYPE_ARGUMENT = 2;
   private final int rootIndex;
   private final List<ClientboundCommandsPacket$Entry> entries;

   public <S> ClientboundCommandsPacket(final RootCommandNode<S> root, final ClientboundCommandsPacket$NodeInspector<S> inspector) {
      Object2IntMap<CommandNode<S>> nodeToId = enumerateNodes(root);
      this.entries = createEntries(nodeToId, inspector);
      this.rootIndex = nodeToId.getInt(root);
   }

   private ClientboundCommandsPacket(final List<ClientboundCommandsPacket$Entry> entries, final int rootIndex) {
      this.entries = entries;
      this.rootIndex = rootIndex;
      validateEntries(this.entries);
   }

   private static void validateEntries(
      final List<ClientboundCommandsPacket$Entry> entries, final BiPredicate<ClientboundCommandsPacket$Entry, IntSet> validator
   ) {
      IntSet elementsToCheck = new IntOpenHashSet(IntSets.fromTo(0, entries.size()));

      while (!elementsToCheck.isEmpty()) {
         boolean worked = elementsToCheck.removeIf(index -> validator.test(entries.get(index), elementsToCheck));
         if (!worked) {
            throw new IllegalStateException("Server sent an impossible command tree");
         }
      }
   }

   private static void validateEntries(final List<ClientboundCommandsPacket$Entry> entries) {
      validateEntries(entries, ClientboundCommandsPacket$Entry::canBuild);
      validateEntries(entries, ClientboundCommandsPacket$Entry::canResolve);
   }

   private static <S> Object2IntMap<CommandNode<S>> enumerateNodes(final RootCommandNode<S> root) {
      Object2IntMap<CommandNode<S>> nodeToId = new Object2IntOpenHashMap();
      Queue<CommandNode<S>> queue = new ArrayDeque<>();
      queue.add(root);

      CommandNode<S> node;
      while ((node = queue.poll()) != null) {
         if (!nodeToId.containsKey(node)) {
            int id = nodeToId.size();
            nodeToId.put(node, id);
            queue.addAll(node.getChildren());
            if (node.getRedirect() != null) {
               queue.add(node.getRedirect());
            }
         }
      }

      return nodeToId;
   }

   private static <S> List<ClientboundCommandsPacket$Entry> createEntries(
      final Object2IntMap<CommandNode<S>> nodeToId, final ClientboundCommandsPacket$NodeInspector<S> inspector
   ) {
      ObjectArrayList<ClientboundCommandsPacket$Entry> result = new ObjectArrayList(nodeToId.size());
      result.size(nodeToId.size());
      ObjectIterator var3 = Object2IntMaps.fastIterable(nodeToId).iterator();

      while (var3.hasNext()) {
         it.unimi.dsi.fastutil.objects.Object2IntMap.Entry<CommandNode<S>> entry = (it.unimi.dsi.fastutil.objects.Object2IntMap.Entry<CommandNode<S>>)var3.next();
         result.set(entry.getIntValue(), createEntry((CommandNode<S>)entry.getKey(), inspector, nodeToId));
      }

      return result;
   }

   private static ClientboundCommandsPacket$Entry readNode(final FriendlyByteBuf input) {
      byte flags = input.readByte();
      int[] children = input.readVarIntArray();
      int redirect = (flags & 8) != 0 ? input.readVarInt() : 0;
      ClientboundCommandsPacket$NodeStub stub = read(input, flags);
      return new ClientboundCommandsPacket$Entry(stub, flags, redirect, children);
   }

   @Nullable
   private static ClientboundCommandsPacket$NodeStub read(final FriendlyByteBuf input, final byte flags) {
      int type = flags & 3;
      if (type == 2) {
         String name = input.readUtf();
         int id = input.readVarInt();
         ArgumentTypeInfo<?, ?> argumentType = BuiltInRegistries.COMMAND_ARGUMENT_TYPE.byId(id);
         if (argumentType == null) {
            return null;
         } else {
            ArgumentTypeInfo$Template<?> argument = argumentType.deserializeFromNetwork(input);
            Identifier suggestionId = (flags & 16) != 0 ? input.readIdentifier() : null;
            return new ClientboundCommandsPacket$ArgumentNodeStub(name, argument, suggestionId);
         }
      } else if (type == 1) {
         String id = input.readUtf();
         return new ClientboundCommandsPacket$LiteralNodeStub(id);
      } else {
         return null;
      }
   }

   private static <S> ClientboundCommandsPacket$Entry createEntry(
      final CommandNode<S> node, final ClientboundCommandsPacket$NodeInspector<S> inspector, final Object2IntMap<CommandNode<S>> ids
   ) {
      int flags = 0;
      int redirect;
      if (node.getRedirect() != null) {
         flags |= 8;
         redirect = ids.getInt(node.getRedirect());
      } else {
         redirect = 0;
      }

      if (inspector.isExecutable(node)) {
         flags |= 4;
      }

      if (inspector.isRestricted(node)) {
         flags |= 32;
      }

      ClientboundCommandsPacket$NodeStub nodeStub;
      switch (node) {
         case RootCommandNode<S> ignored:
            flags |= 0;
            nodeStub = null;
            break;
         case ArgumentCommandNode<S, ?> arg:
            Identifier suggestionId = inspector.suggestionId(arg);
            nodeStub = new ClientboundCommandsPacket$ArgumentNodeStub(arg.getName(), ArgumentTypeInfos.unpack(arg.getType()), suggestionId);
            flags |= 2;
            if (suggestionId != null) {
               flags |= 16;
            }
            break;
         case LiteralCommandNode<S> literal:
            nodeStub = new ClientboundCommandsPacket$LiteralNodeStub(literal.getLiteral());
            flags |= 1;
            break;
         default:
            throw new UnsupportedOperationException("Unknown node type " + node);
      }

      int[] childrenIds = node.getChildren().stream().mapToInt(ids::getInt).toArray();
      return new ClientboundCommandsPacket$Entry(nodeStub, flags, redirect, childrenIds);
   }

   @Override
   public PacketType<ClientboundCommandsPacket> type() {
      return GamePacketTypes.CLIENTBOUND_COMMANDS;
   }

   public void handle(final ClientGamePacketListener listener) {
      listener.handleCommands(this);
   }

   public <S> RootCommandNode<S> getRoot(final CommandBuildContext context, final ClientboundCommandsPacket$NodeBuilder<S> builder) {
      return (RootCommandNode<S>)new ClientboundCommandsPacket$NodeResolver<>(context, builder, this.entries).resolve(this.rootIndex);
   }
}
