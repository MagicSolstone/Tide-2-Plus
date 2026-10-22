package net.minecraft.advancements;

import java.util.Collection;

public interface AdvancementRequirements$Strategy {
   AdvancementRequirements$Strategy AND = AdvancementRequirements::allOf;
   AdvancementRequirements$Strategy OR = AdvancementRequirements::anyOf;

   AdvancementRequirements create(Collection<String> criteria);
}
