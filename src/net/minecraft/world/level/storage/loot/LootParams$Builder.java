package net.minecraft.world.level.storage.loot;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.NoSuchElementException;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKey;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.util.context.ContextMap;
import net.minecraft.util.context.ContextMap$Builder;
import org.jspecify.annotations.Nullable;

public class LootParams$Builder {
   private final ServerLevel level;
   private final ContextMap$Builder params = ContextMap.builder();
   private final Map<Identifier, LootParams$DynamicDrop> dynamicDrops = Maps.newHashMap();
   private float luck;

   public LootParams$Builder(final ServerLevel level) {
      this.level = level;
   }

   public ServerLevel getLevel() {
      return this.level;
   }

   public <T> LootParams$Builder withParameter(final ContextKey<T> param, final T value) {
      this.params.set(param, value);
      return this;
   }

   public <T> LootParams$Builder withOptionalParameter(final ContextKey<T> param, @Nullable final T value) {
      this.params.set(param, value);
      return this;
   }

   public <T> T getParameter(final ContextKey<T> param) {
      T value = this.params.get(param);
      if (value == null) {
         throw new NoSuchElementException(param.name().toString());
      } else {
         return value;
      }
   }

   @Nullable
   public <T> T getOptionalParameter(final ContextKey<T> param) {
      return this.params.get(param);
   }

   public LootParams$Builder withDynamicDrop(final Identifier location, final LootParams$DynamicDrop dynamicDrop) {
      LootParams$DynamicDrop prev = this.dynamicDrops.put(location, dynamicDrop);
      if (prev != null) {
         throw new IllegalStateException("Duplicated dynamic drop '" + this.dynamicDrops + "'");
      } else {
         return this;
      }
   }

   public LootParams$Builder withLuck(final float luck) {
      this.luck = luck;
      return this;
   }

   public LootParams create(final ContextKeySet contextKeySet) {
      ContextMap keySet = this.params.buildAndValidate(contextKeySet);
      return new LootParams(this.level, keySet, this.dynamicDrops, this.luck);
   }
}
