package net.minecraft.client.renderer.block.dispatch.multipart;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public record KeyValueCondition(Map<String, KeyValueCondition$Terms> tests) implements Condition {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final Codec<KeyValueCondition> CODEC = ExtraCodecs.nonEmptyMap(Codec.unboundedMap(Codec.STRING, KeyValueCondition$Terms.CODEC))
      .xmap(KeyValueCondition::new, KeyValueCondition::tests);

   @Override
   public <O, S extends StateHolder<O, S>> Predicate<S> instantiate(final StateDefinition<O, S> definition) {
      List<Predicate<S>> predicates = new ArrayList<>(this.tests.size());
      this.tests.forEach((key, valueTest) -> predicates.add(instantiate(definition, key, valueTest)));
      return Util.allOf(predicates);
   }

   private static <O, S extends StateHolder<O, S>> Predicate<S> instantiate(
      final StateDefinition<O, S> definition, final String key, final KeyValueCondition$Terms valueTest
   ) {
      Property<?> property = definition.getProperty(key);
      if (property == null) {
         throw new IllegalArgumentException(String.format(Locale.ROOT, "Unknown property '%s' on '%s'", key, definition.getOwner()));
      } else {
         return valueTest.instantiate(definition.getOwner(), property);
      }
   }
}
