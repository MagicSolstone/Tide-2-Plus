package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Rotation;

public record TemplateFeature$TemplateEntry(Identifier template, List<Rotation> rotations) {
   public static final Codec<TemplateFeature$TemplateEntry> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Identifier.CODEC.fieldOf("id").forGetter(TemplateFeature$TemplateEntry::template),
            Rotation.CODEC.listOf().optionalFieldOf("rotations", List.of(Rotation.values())).forGetter(TemplateFeature$TemplateEntry::rotations)
         )
         .apply(i, TemplateFeature$TemplateEntry::new)
   );

   public static TemplateFeature$TemplateEntry of(final Identifier template) {
      return new TemplateFeature$TemplateEntry(template, List.of(Rotation.values()));
   }
}
