package net.minecraft.server.commands;

import com.google.common.collect.Iterables;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementNode;
import net.minecraft.advancements.AdvancementTree;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class AdvancementCommands {
   private static final Dynamic2CommandExceptionType ERROR_CRITERION_NOT_FOUND = new Dynamic2CommandExceptionType(
      (name, criterion) -> Component.translatableEscape("commands.advancement.criterionNotFound", name, criterion)
   );

   public static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(
         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("advancement")
                  .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
               .then(
                  Commands.literal("grant")
                     .then(
                        ((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument(
                                          "targets", EntityArgument.players()
                                       )
                                       .then(
                                          Commands.literal("only")
                                             .then(
                                                ((RequiredArgumentBuilder)Commands.argument("advancement", ResourceKeyArgument.key(Registries.ADVANCEMENT))
                                                      .executes(
                                                         c -> perform(
                                                            (CommandSourceStack)c.getSource(),
                                                            EntityArgument.getPlayers(c, "targets"),
                                                            AdvancementCommands$Action.GRANT,
                                                            getAdvancements(
                                                               c, ResourceKeyArgument.getAdvancement(c, "advancement"), AdvancementCommands$Mode.ONLY
                                                            )
                                                         )
                                                      ))
                                                   .then(
                                                      Commands.argument("criterion", StringArgumentType.greedyString())
                                                         .suggests(
                                                            (c, p) -> SharedSuggestionProvider.suggest(
                                                               ResourceKeyArgument.getAdvancement(c, "advancement").value().criteria().keySet(), p
                                                            )
                                                         )
                                                         .executes(
                                                            c -> performCriterion(
                                                               (CommandSourceStack)c.getSource(),
                                                               EntityArgument.getPlayers(c, "targets"),
                                                               AdvancementCommands$Action.GRANT,
                                                               ResourceKeyArgument.getAdvancement(c, "advancement"),
                                                               StringArgumentType.getString(c, "criterion")
                                                            )
                                                         )
                                                   )
                                             )
                                       ))
                                    .then(
                                       Commands.literal("from")
                                          .then(
                                             Commands.argument("advancement", ResourceKeyArgument.key(Registries.ADVANCEMENT))
                                                .executes(
                                                   c -> perform(
                                                      (CommandSourceStack)c.getSource(),
                                                      EntityArgument.getPlayers(c, "targets"),
                                                      AdvancementCommands$Action.GRANT,
                                                      getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), AdvancementCommands$Mode.FROM)
                                                   )
                                                )
                                          )
                                    ))
                                 .then(
                                    Commands.literal("until")
                                       .then(
                                          Commands.argument("advancement", ResourceKeyArgument.key(Registries.ADVANCEMENT))
                                             .executes(
                                                c -> perform(
                                                   (CommandSourceStack)c.getSource(),
                                                   EntityArgument.getPlayers(c, "targets"),
                                                   AdvancementCommands$Action.GRANT,
                                                   getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), AdvancementCommands$Mode.UNTIL)
                                                )
                                             )
                                       )
                                 ))
                              .then(
                                 Commands.literal("through")
                                    .then(
                                       Commands.argument("advancement", ResourceKeyArgument.key(Registries.ADVANCEMENT))
                                          .executes(
                                             c -> perform(
                                                (CommandSourceStack)c.getSource(),
                                                EntityArgument.getPlayers(c, "targets"),
                                                AdvancementCommands$Action.GRANT,
                                                getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), AdvancementCommands$Mode.THROUGH)
                                             )
                                          )
                                    )
                              ))
                           .then(
                              Commands.literal("everything")
                                 .executes(
                                    c -> perform(
                                       (CommandSourceStack)c.getSource(),
                                       EntityArgument.getPlayers(c, "targets"),
                                       AdvancementCommands$Action.GRANT,
                                       ((CommandSourceStack)c.getSource()).getServer().getAdvancements().getAllAdvancements(),
                                       false
                                    )
                                 )
                           )
                     )
               ))
            .then(
               Commands.literal("revoke")
                  .then(
                     ((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument(
                                       "targets", EntityArgument.players()
                                    )
                                    .then(
                                       Commands.literal("only")
                                          .then(
                                             ((RequiredArgumentBuilder)Commands.argument("advancement", ResourceKeyArgument.key(Registries.ADVANCEMENT))
                                                   .executes(
                                                      c -> perform(
                                                         (CommandSourceStack)c.getSource(),
                                                         EntityArgument.getPlayers(c, "targets"),
                                                         AdvancementCommands$Action.REVOKE,
                                                         getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), AdvancementCommands$Mode.ONLY)
                                                      )
                                                   ))
                                                .then(
                                                   Commands.argument("criterion", StringArgumentType.greedyString())
                                                      .suggests(
                                                         (c, p) -> SharedSuggestionProvider.suggest(
                                                            ResourceKeyArgument.getAdvancement(c, "advancement").value().criteria().keySet(), p
                                                         )
                                                      )
                                                      .executes(
                                                         c -> performCriterion(
                                                            (CommandSourceStack)c.getSource(),
                                                            EntityArgument.getPlayers(c, "targets"),
                                                            AdvancementCommands$Action.REVOKE,
                                                            ResourceKeyArgument.getAdvancement(c, "advancement"),
                                                            StringArgumentType.getString(c, "criterion")
                                                         )
                                                      )
                                                )
                                          )
                                    ))
                                 .then(
                                    Commands.literal("from")
                                       .then(
                                          Commands.argument("advancement", ResourceKeyArgument.key(Registries.ADVANCEMENT))
                                             .executes(
                                                c -> perform(
                                                   (CommandSourceStack)c.getSource(),
                                                   EntityArgument.getPlayers(c, "targets"),
                                                   AdvancementCommands$Action.REVOKE,
                                                   getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), AdvancementCommands$Mode.FROM)
                                                )
                                             )
                                       )
                                 ))
                              .then(
                                 Commands.literal("until")
                                    .then(
                                       Commands.argument("advancement", ResourceKeyArgument.key(Registries.ADVANCEMENT))
                                          .executes(
                                             c -> perform(
                                                (CommandSourceStack)c.getSource(),
                                                EntityArgument.getPlayers(c, "targets"),
                                                AdvancementCommands$Action.REVOKE,
                                                getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), AdvancementCommands$Mode.UNTIL)
                                             )
                                          )
                                    )
                              ))
                           .then(
                              Commands.literal("through")
                                 .then(
                                    Commands.argument("advancement", ResourceKeyArgument.key(Registries.ADVANCEMENT))
                                       .executes(
                                          c -> perform(
                                             (CommandSourceStack)c.getSource(),
                                             EntityArgument.getPlayers(c, "targets"),
                                             AdvancementCommands$Action.REVOKE,
                                             getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), AdvancementCommands$Mode.THROUGH)
                                          )
                                       )
                                 )
                           ))
                        .then(
                           Commands.literal("everything")
                              .executes(
                                 c -> perform(
                                    (CommandSourceStack)c.getSource(),
                                    EntityArgument.getPlayers(c, "targets"),
                                    AdvancementCommands$Action.REVOKE,
                                    ((CommandSourceStack)c.getSource()).getServer().getAdvancements().getAllAdvancements()
                                 )
                              )
                        )
                  )
            )
      );
   }

   private static int perform(
      final CommandSourceStack source,
      final Collection<ServerPlayer> players,
      final AdvancementCommands$Action action,
      final Collection<AdvancementHolder> advancements
   ) throws CommandSyntaxException {
      return perform(source, players, action, advancements, true);
   }

   private static int perform(
      final CommandSourceStack source,
      final Collection<ServerPlayer> players,
      final AdvancementCommands$Action action,
      final Collection<AdvancementHolder> advancements,
      final boolean showAdvancements
   ) throws CommandSyntaxException {
      CommandResponseTracker<ServerPlayer> tracker = CommandResponseTracker.create();

      for (ServerPlayer player : players) {
         tracker.track(player, action.perform(player, advancements, showAdvancements));
      }

      int advancementCount = advancements.size();
      if (advancementCount == 1) {
         AdvancementHolder advancementName = (AdvancementHolder)Iterables.getOnlyElement(advancements);
         if (tracker.totalValue() == 0) {
            throw (CommandSyntaxException)tracker.dispatch(CommandResponseTracker$ElementType.ANY, action.singleAdvancementsError, advancementName);
         } else {
            return tracker.sendFeedback(source, true, CommandResponseTracker$ElementType.NON_ZERO, action.singleAdvancementSuccessResponse, advancementName);
         }
      } else if (tracker.totalValue() == 0) {
         throw (CommandSyntaxException)tracker.dispatch(CommandResponseTracker$ElementType.ANY, action.multipleAdvancementsError, advancementCount);
      } else {
         return tracker.sendFeedback(source, true, CommandResponseTracker$ElementType.NON_ZERO, action.multipleAdvancementsSuccessResponse, advancementCount);
      }
   }

   private static int performCriterion(
      final CommandSourceStack source,
      final Collection<ServerPlayer> players,
      final AdvancementCommands$Action action,
      final AdvancementHolder holder,
      final String criterion
   ) throws CommandSyntaxException {
      Advancement advancement = holder.value();
      if (!advancement.criteria().containsKey(criterion)) {
         throw ERROR_CRITERION_NOT_FOUND.create(Advancement.name(holder), criterion);
      } else {
         CommandResponseTracker<ServerPlayer> tracker = CommandResponseTracker.create();

         for (ServerPlayer player : players) {
            tracker.track(player, action.performCriterion(player, holder, criterion));
         }

         if (tracker.totalValue() == 0) {
            throw (CommandSyntaxException)tracker.dispatch(CommandResponseTracker$ElementType.ANY, action.criterionError, holder, criterion);
         } else {
            return tracker.sendFeedback(source, true, CommandResponseTracker$ElementType.NON_ZERO, action.criterionSuccessResponse, holder, criterion);
         }
      }
   }

   private static List<AdvancementHolder> getAdvancements(
      final CommandContext<CommandSourceStack> context, final AdvancementHolder target, final AdvancementCommands$Mode mode
   ) {
      AdvancementTree advancementTree = ((CommandSourceStack)context.getSource()).getServer().getAdvancements().tree();
      AdvancementNode targetNode = advancementTree.get(target);
      if (targetNode == null) {
         return List.of(target);
      } else {
         List<AdvancementHolder> advancements = new ArrayList<>();
         if (mode.parents) {
            for (AdvancementNode parent = targetNode.parent(); parent != null; parent = parent.parent()) {
               advancements.add(parent.holder());
            }
         }

         advancements.add(target);
         if (mode.children) {
            addChildren(targetNode, advancements);
         }

         return advancements;
      }
   }

   private static void addChildren(final AdvancementNode parent, final List<AdvancementHolder> output) {
      for (AdvancementNode child : parent.children()) {
         output.add(child.holder());
         addChildren(child, output);
      }
   }
}
