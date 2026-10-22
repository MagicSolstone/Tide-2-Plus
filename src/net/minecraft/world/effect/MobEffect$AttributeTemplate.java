package net.minecraft.world.effect;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation;

record MobEffect$AttributeTemplate(Identifier id, double amount, AttributeModifier$Operation operation) {
   public AttributeModifier create(final int amplifier) {
      return new AttributeModifier(this.id, this.amount * (amplifier + 1), this.operation);
   }
}
