package net.minecraft.advancements.predicates.entity;

import java.util.Optional;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.ItemPredicate$Builder;

public class EntityEquipmentPredicate$Builder {
   private Optional<ItemPredicate> head = Optional.empty();
   private Optional<ItemPredicate> chest = Optional.empty();
   private Optional<ItemPredicate> legs = Optional.empty();
   private Optional<ItemPredicate> feet = Optional.empty();
   private Optional<ItemPredicate> body = Optional.empty();
   private Optional<ItemPredicate> mainhand = Optional.empty();
   private Optional<ItemPredicate> offhand = Optional.empty();

   public static EntityEquipmentPredicate$Builder equipment() {
      return new EntityEquipmentPredicate$Builder();
   }

   public EntityEquipmentPredicate$Builder head(final ItemPredicate$Builder head) {
      this.head = Optional.of(head.build());
      return this;
   }

   public EntityEquipmentPredicate$Builder chest(final ItemPredicate$Builder chest) {
      this.chest = Optional.of(chest.build());
      return this;
   }

   public EntityEquipmentPredicate$Builder legs(final ItemPredicate$Builder legs) {
      this.legs = Optional.of(legs.build());
      return this;
   }

   public EntityEquipmentPredicate$Builder feet(final ItemPredicate$Builder feet) {
      this.feet = Optional.of(feet.build());
      return this;
   }

   public EntityEquipmentPredicate$Builder body(final ItemPredicate$Builder body) {
      this.body = Optional.of(body.build());
      return this;
   }

   public EntityEquipmentPredicate$Builder mainhand(final ItemPredicate$Builder mainhand) {
      this.mainhand = Optional.of(mainhand.build());
      return this;
   }

   public EntityEquipmentPredicate$Builder offhand(final ItemPredicate$Builder offhand) {
      this.offhand = Optional.of(offhand.build());
      return this;
   }

   public EntityEquipmentPredicate build() {
      return new EntityEquipmentPredicate(this.head, this.chest, this.legs, this.feet, this.body, this.mainhand, this.offhand);
   }
}
