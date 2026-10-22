package net.minecraft.server;

import com.mojang.serialization.Codec;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.Identifier;

record PlayerAdvancements$Data(Map<Identifier, AdvancementProgress> map) {
   public static final Codec<PlayerAdvancements$Data> CODEC = Codec.unboundedMap(Identifier.CODEC, AdvancementProgress.CODEC)
      .xmap(PlayerAdvancements$Data::new, PlayerAdvancements$Data::map);

   public void forEach(final BiConsumer<Identifier, AdvancementProgress> consumer) {
      this.map.entrySet().stream().sorted(Entry.comparingByValue()).forEach(entry -> consumer.accept(entry.getKey(), entry.getValue()));
   }
}
