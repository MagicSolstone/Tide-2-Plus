package net.minecraft.world.level.storage.loot;

import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;

public class LootParams {
   private final ServerLevel level;
   private final ContextMap params;
   private final Map<Identifier, LootParams$DynamicDrop> dynamicDrops;
   private final float luck;

   public LootParams(final ServerLevel level, final ContextMap params, final Map<Identifier, LootParams$DynamicDrop> dynamicDrops, final float luck) {
      this.level = level;
      this.params = params;
      this.dynamicDrops = dynamicDrops;
      this.luck = luck;
   }

   public ServerLevel getLevel() {
      return this.level;
   }

   public ContextMap contextMap() {
      return this.params;
   }

   public void addDynamicDrops(final Identifier location, final Consumer<ItemStack> output) {
      LootParams$DynamicDrop dynamicDrop = this.dynamicDrops.get(location);
      if (dynamicDrop != null) {
         dynamicDrop.add(output);
      }
   }

   public float getLuck() {
      return this.luck;
   }
}
