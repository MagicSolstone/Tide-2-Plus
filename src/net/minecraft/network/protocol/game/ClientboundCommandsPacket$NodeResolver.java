package net.minecraft.network.protocol.game;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.commands.CommandBuildContext;

class ClientboundCommandsPacket$NodeResolver<S> {
   private final CommandBuildContext context;
   private final ClientboundCommandsPacket$NodeBuilder<S> builder;
   private final List<ClientboundCommandsPacket$Entry> entries;
   private final List<CommandNode<S>> nodes;

   private ClientboundCommandsPacket$NodeResolver(
      final CommandBuildContext context, final ClientboundCommandsPacket$NodeBuilder<S> builder, final List<ClientboundCommandsPacket$Entry> entries
   ) {
      this.context = context;
      this.builder = builder;
      this.entries = entries;
      ObjectArrayList<CommandNode<S>> nodes = new ObjectArrayList();
      nodes.size(entries.size());
      this.nodes = nodes;
   }

   public CommandNode<S> resolve(final int index) {
      CommandNode<S> currentNode = this.nodes.get(index);
      if (currentNode != null) {
         return currentNode;
      } else {
         ClientboundCommandsPacket$Entry entry = this.entries.get(index);
         CommandNode<S> result;
         if (entry.stub == null) {
            result = new RootCommandNode();
         } else {
            ArgumentBuilder<S, ?> resultBuilder = entry.stub.build(this.context, this.builder);
            if ((entry.flags & 8) != 0) {
               resultBuilder.redirect(this.resolve(entry.redirect));
            }

            boolean isExecutable = (entry.flags & 4) != 0;
            boolean isRestricted = (entry.flags & 32) != 0;
            result = this.builder.configure(resultBuilder, isExecutable, isRestricted).build();
         }

         this.nodes.set(index, result);

         for (int childId : entry.children) {
            CommandNode<S> child = this.resolve(childId);
            if (!(child instanceof RootCommandNode)) {
               result.addChild(child);
            }
         }

         return result;
      }
   }
}
