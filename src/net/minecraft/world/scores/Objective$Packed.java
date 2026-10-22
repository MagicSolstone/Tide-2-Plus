package net.minecraft.world.scores;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.numbers.NumberFormat;
import net.minecraft.network.chat.numbers.NumberFormatTypes;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraft.world.scores.criteria.ObjectiveCriteria$RenderType;

public record Objective$Packed(
   String name,
   ObjectiveCriteria criteria,
   Component displayName,
   ObjectiveCriteria$RenderType renderType,
   boolean displayAutoUpdate,
   Optional<NumberFormat> numberFormat
) {
   public static final Codec<Objective$Packed> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.STRING.fieldOf("Name").forGetter(Objective$Packed::name),
            ObjectiveCriteria.CODEC.optionalFieldOf("CriteriaName", ObjectiveCriteria.DUMMY).forGetter(Objective$Packed::criteria),
            ComponentSerialization.CODEC.fieldOf("DisplayName").forGetter(Objective$Packed::displayName),
            ObjectiveCriteria$RenderType.CODEC.optionalFieldOf("RenderType", ObjectiveCriteria$RenderType.INTEGER).forGetter(Objective$Packed::renderType),
            Codec.BOOL.optionalFieldOf("display_auto_update", false).forGetter(Objective$Packed::displayAutoUpdate),
            NumberFormatTypes.CODEC.optionalFieldOf("format").forGetter(Objective$Packed::numberFormat)
         )
         .apply(i, Objective$Packed::new)
   );
}
