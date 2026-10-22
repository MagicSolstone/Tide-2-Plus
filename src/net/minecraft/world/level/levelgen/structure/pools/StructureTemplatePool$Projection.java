package net.minecraft.world.level.levelgen.structure.pools;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.StringRepresentable$EnumCodec;
import net.minecraft.world.level.levelgen.Heightmap$Types;
import net.minecraft.world.level.levelgen.structure.templatesystem.GravityProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;

public enum StructureTemplatePool$Projection implements StringRepresentable {
   TERRAIN_MATCHING("terrain_matching", ImmutableList.of(new GravityProcessor(Heightmap$Types.WORLD_SURFACE_WG, -1))),
   RIGID("rigid", ImmutableList.of());

   public static final StringRepresentable$EnumCodec<StructureTemplatePool$Projection> CODEC = StringRepresentable.fromEnum(
      StructureTemplatePool$Projection::values
   );
   private final String name;
   private final ImmutableList<StructureProcessor> processors;

   private StructureTemplatePool$Projection(final String name, final ImmutableList<StructureProcessor> processors) {
      this.name = name;
      this.processors = processors;
   }

   public String getName() {
      return this.name;
   }

   public static StructureTemplatePool$Projection byName(final String name) {
      return CODEC.byName(name);
   }

   public ImmutableList<StructureProcessor> getProcessors() {
      return this.processors;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
