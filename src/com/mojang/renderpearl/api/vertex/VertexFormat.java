package com.mojang.renderpearl.api.vertex;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class VertexFormat {
   private static final int VERTEX_ALIGNMENT = 4;
   public static final int MAX_VERTEX_ELEMENTS = 16;
   private final Map<String, VertexFormatElement> elements = new Object2ObjectArrayMap(16);
   private final int vertexSize;
   private final int stepRate;
   private final List<VertexFormatElement> elementValues;

   private VertexFormat(final List<VertexFormatElement> elements, final int vertexSize, final int stepRate) {
      this.vertexSize = vertexSize;
      this.stepRate = stepRate;

      for (VertexFormatElement element : elements) {
         this.elements.putIfAbsent(element.name(), element);
      }

      this.elementValues = elements;
   }

   public static VertexFormat$Builder builder(final int stepRate) {
      return new VertexFormat$Builder(stepRate);
   }

   @Override
   public String toString() {
      return "VertexFormat" + this.elementValues.stream().map(VertexFormatElement::name).collect(Collectors.joining(", ", "[", "]"));
   }

   public int getVertexSize() {
      return this.vertexSize;
   }

   public int getStepRate() {
      return this.stepRate;
   }

   public List<VertexFormatElement> getElements() {
      return this.elementValues;
   }

   @Nullable
   public VertexFormatElement getElement(final String attributeName) {
      return this.elements.get(attributeName);
   }

   public boolean contains(final String attributeName) {
      return this.elements.containsKey(attributeName);
   }

   @Override
   public boolean equals(final Object o) {
      return this == o ? true : o instanceof VertexFormat format && this.elements.equals(format.elements) && this.vertexSize == format.vertexSize;
   }

   @Override
   public int hashCode() {
      return this.elementValues.hashCode();
   }
}
