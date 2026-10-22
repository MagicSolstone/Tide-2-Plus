package net.minecraft.gametest.framework;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.util.Unit;
import org.slf4j.Logger;

public record TestEnvironmentDefinition$Functions(Optional<Identifier> setupFunction, Optional<Identifier> teardownFunction)
   implements TestEnvironmentDefinition<Unit> {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final MapCodec<TestEnvironmentDefinition$Functions> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Identifier.CODEC.optionalFieldOf("setup").forGetter(TestEnvironmentDefinition$Functions::setupFunction),
            Identifier.CODEC.optionalFieldOf("teardown").forGetter(TestEnvironmentDefinition$Functions::teardownFunction)
         )
         .apply(i, TestEnvironmentDefinition$Functions::new)
   );

   public Unit setup(final ServerLevel level) {
      this.setupFunction.ifPresent(p -> run(level, p));
      return Unit.INSTANCE;
   }

   public void teardown(final ServerLevel level, final Unit saveData) {
      this.teardownFunction.ifPresent(p -> run(level, p));
   }

   private static void run(final ServerLevel level, final Identifier functionId) {
      MinecraftServer server = level.getServer();
      ServerFunctionManager functions = server.getFunctions();
      Optional<CommandFunction<CommandSourceStack>> function = functions.get(functionId);
      if (function.isPresent()) {
         CommandSourceStack source = server.createCommandSourceStack()
            .withPermission(LevelBasedPermissionSet.GAMEMASTER)
            .withSuppressedOutput()
            .withLevel(level);
         functions.execute(function.get(), source);
      } else {
         LOGGER.error("Test Batch failed for non-existent function {}", functionId);
      }
   }

   @Override
   public MapCodec<TestEnvironmentDefinition$Functions> codec() {
      return CODEC;
   }
}
