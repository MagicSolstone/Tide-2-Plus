package net.minecraft.world.level.storage.loot;

import net.minecraft.util.StringRepresentable;
import net.minecraft.util.StringRepresentable$EnumCodec;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public enum LootContext$EntityTarget implements StringRepresentable, LootContextArg$SimpleGetter<Entity> {
   THIS("this", LootContextParams.THIS_ENTITY),
   ATTACKER("attacker", LootContextParams.ATTACKING_ENTITY),
   DIRECT_ATTACKER("direct_attacker", LootContextParams.DIRECT_ATTACKING_ENTITY),
   ATTACKING_PLAYER("attacking_player", LootContextParams.LAST_DAMAGE_PLAYER),
   TARGET_ENTITY("target_entity", LootContextParams.TARGET_ENTITY),
   INTERACTING_ENTITY("interacting_entity", LootContextParams.INTERACTING_ENTITY);

   public static final StringRepresentable$EnumCodec<LootContext$EntityTarget> CODEC = StringRepresentable.fromEnum(LootContext$EntityTarget::values);
   private final String name;
   private final ContextKey<? extends Entity> param;

   private LootContext$EntityTarget(final String name, final ContextKey<? extends Entity> param) {
      this.name = name;
      this.param = param;
   }

   @Override
   public ContextKey<? extends Entity> contextParam() {
      return this.param;
   }

   public static LootContext$EntityTarget getByName(final String name) {
      LootContext$EntityTarget target = CODEC.byName(name);
      if (target != null) {
         return target;
      } else {
         throw new IllegalArgumentException("Invalid entity target " + name);
      }
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
