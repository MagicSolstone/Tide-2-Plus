package net.minecraft.world.level.storage.loot;

import java.util.Optional;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

public class LootContext$Builder {
   private final LootParams params;
   @Nullable
   private RandomSource random;

   public LootContext$Builder(final LootParams params) {
      this.params = params;
   }

   public LootContext$Builder withOptionalRandomSeed(final long seed) {
      if (seed != 0L) {
         this.random = RandomSource.create(seed);
      }

      return this;
   }

   public LootContext$Builder withOptionalRandomSource(final RandomSource randomSource) {
      this.random = randomSource;
      return this;
   }

   public ServerLevel getLevel() {
      return this.params.getLevel();
   }

   public LootContext create(final Optional<Identifier> randomSequenceKey) {
      ServerLevel level = this.getLevel();
      MinecraftServer server = level.getServer();
      RandomSource random = Optional.ofNullable(this.random).or(() -> randomSequenceKey.map(server::getRandomSequence)).orElseGet(level::getRandom);
      return new LootContext(this.params, random, server.reloadableRegistries().lookup());
   }
}
