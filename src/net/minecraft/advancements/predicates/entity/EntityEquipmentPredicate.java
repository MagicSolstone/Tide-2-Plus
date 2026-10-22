package net.minecraft.advancements.predicates.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.DataComponentMatchers$Builder;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.ItemPredicate$Builder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record EntityEquipmentPredicate(
   Optional<ItemPredicate> head,
   Optional<ItemPredicate> chest,
   Optional<ItemPredicate> legs,
   Optional<ItemPredicate> feet,
   Optional<ItemPredicate> body,
   Optional<ItemPredicate> mainhand,
   Optional<ItemPredicate> offhand
) implements EntitySubPredicate {
   public static final Codec<EntityEquipmentPredicate> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ItemPredicate.CODEC.optionalFieldOf("head").forGetter(EntityEquipmentPredicate::head),
            ItemPredicate.CODEC.optionalFieldOf("chest").forGetter(EntityEquipmentPredicate::chest),
            ItemPredicate.CODEC.optionalFieldOf("legs").forGetter(EntityEquipmentPredicate::legs),
            ItemPredicate.CODEC.optionalFieldOf("feet").forGetter(EntityEquipmentPredicate::feet),
            ItemPredicate.CODEC.optionalFieldOf("body").forGetter(EntityEquipmentPredicate::body),
            ItemPredicate.CODEC.optionalFieldOf("mainhand").forGetter(EntityEquipmentPredicate::mainhand),
            ItemPredicate.CODEC.optionalFieldOf("offhand").forGetter(EntityEquipmentPredicate::offhand)
         )
         .apply(i, EntityEquipmentPredicate::new)
   );

   public static EntityEquipmentPredicate captainPredicate(final HolderGetter<Item> items, final HolderGetter<BannerPattern> patternGetter) {
      return EntityEquipmentPredicate$Builder.equipment()
         .head(
            ItemPredicate$Builder.item()
               .of(items, Items.BANNER.white())
               .withComponents(
                  DataComponentMatchers$Builder.components()
                     .exact(
                        DataComponentExactPredicate.someOf(
                           Raid.getBannerComponentPatch(patternGetter).split().added(), DataComponents.BANNER_PATTERNS, DataComponents.ITEM_NAME
                        )
                     )
                     .build()
               )
         )
         .build();
   }

   public boolean matches(@Nullable final Entity entity) {
      if (entity instanceof LivingEntity livingEntity) {
         if (this.head.isPresent() && !this.head.get().test((ItemInstance)livingEntity.getItemBySlot(EquipmentSlot.HEAD))) {
            return false;
         } else if (this.chest.isPresent() && !this.chest.get().test((ItemInstance)livingEntity.getItemBySlot(EquipmentSlot.CHEST))) {
            return false;
         } else if (this.legs.isPresent() && !this.legs.get().test((ItemInstance)livingEntity.getItemBySlot(EquipmentSlot.LEGS))) {
            return false;
         } else if (this.feet.isPresent() && !this.feet.get().test((ItemInstance)livingEntity.getItemBySlot(EquipmentSlot.FEET))) {
            return false;
         } else if (this.body.isPresent() && !this.body.get().test((ItemInstance)livingEntity.getItemBySlot(EquipmentSlot.BODY))) {
            return false;
         } else {
            return this.mainhand.isPresent() && !this.mainhand.get().test((ItemInstance)livingEntity.getItemBySlot(EquipmentSlot.MAINHAND))
               ? false
               : !this.offhand.isPresent() || this.offhand.get().test((ItemInstance)livingEntity.getItemBySlot(EquipmentSlot.OFFHAND));
         }
      } else {
         return false;
      }
   }

   @Override
   public boolean matches(final Entity entity, final ServerLevel level, @Nullable final Vec3 position) {
      return this.matches(entity);
   }
}
