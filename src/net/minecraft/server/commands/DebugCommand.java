package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.logging.LogUtils;
import java.util.Locale;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.profiling.ProfileResults;
import org.slf4j.Logger;

public class DebugCommand {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final SimpleCommandExceptionType ERROR_NOT_RUNNING = new SimpleCommandExceptionType(Component.translatable("commands.debug.notRunning"));
   private static final SimpleCommandExceptionType ERROR_ALREADY_RUNNING = new SimpleCommandExceptionType(
      Component.translatable("commands.debug.alreadyRunning")
   );
   private static final SimpleCommandExceptionType NO_RECURSIVE_TRACES = new SimpleCommandExceptionType(
      Component.translatable("commands.debug.function.noRecursion")
   );
   private static final SimpleCommandExceptionType NO_RETURN_RUN = new SimpleCommandExceptionType(Component.translatable("commands.debug.function.noReturnRun"));

   public static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(
         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("debug")
                     .requires(Commands.hasPermission(Commands.LEVEL_ADMINS)))
                  .then(Commands.literal("start").executes(c -> start((CommandSourceStack)c.getSource()))))
               .then(Commands.literal("stop").executes(c -> stop((CommandSourceStack)c.getSource()))))
            .then(
               ((LiteralArgumentBuilder)Commands.literal("function").requires(Commands.hasPermission(Commands.LEVEL_ADMINS)))
                  .then(
                     Commands.argument("name", FunctionArgument.functions())
                        .suggests(FunctionCommand.SUGGEST_FUNCTION)
                        .executes(new DebugCommand$TraceCustomExecutor())
                  )
            )
      );
   }

   private static int start(final CommandSourceStack source) throws CommandSyntaxException {
      MinecraftServer server = source.getServer();
      if (server.isTimeProfilerRunning()) {
         throw ERROR_ALREADY_RUNNING.create();
      } else {
         server.startTimeProfiler();
         source.sendSuccess(() -> Component.translatable("commands.debug.started"), true);
         return 0;
      }
   }

   private static int stop(final CommandSourceStack source) throws CommandSyntaxException {
      MinecraftServer server = source.getServer();
      if (!server.isTimeProfilerRunning()) {
         throw ERROR_NOT_RUNNING.create();
      } else {
         ProfileResults results = server.stopTimeProfiler();
         double seconds = (double)results.getNanoDuration() / TimeUtil.NANOSECONDS_PER_SECOND;
         double tps = results.getTickDuration() / seconds;
         source.sendSuccess(
            () -> Component.translatable(
               "commands.debug.stopped", String.format(Locale.ROOT, "%.2f", seconds), results.getTickDuration(), String.format(Locale.ROOT, "%.2f", tps)
            ),
            true
         );
         return (int)tps;
      }
   }
}
