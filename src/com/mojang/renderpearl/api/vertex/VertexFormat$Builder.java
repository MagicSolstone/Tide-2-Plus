package com.mojang.renderpearl.api.vertex;

import com.mojang.renderpearl.api.GpuFormat;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class VertexFormat$Builder {
   private final List<VertexFormatElement> elements = new ArrayList<>(16);
   private int offset = 0;
   private final int stepRate;

   private VertexFormat$Builder(final int stepRate) {
      this.stepRate = stepRate;
   }

   private void createAttribute(final String name, final int offset, final GpuFormat elementFormat) {
      if (this.elements.size() >= 16) {
         throw new IllegalArgumentException("Having more than 16 attributes are not supported");
      } else if (!Mth.isMultipleOf(offset, elementFormat.byteAlignment())) {
         throw new IllegalArgumentException(name + " is not aligned to " + elementFormat.byteAlignment() + " as required by " + elementFormat);
      } else {
         VertexFormatElement element = new VertexFormatElement(name, offset, elementFormat);
         this.elements.add(element);
      }
   }

   private void validateUniqueName(final String name) {
      for (VertexFormatElement element : this.elements) {
         if (element.name().equals(name)) {
            throw new IllegalArgumentException("Another vertex attribute exists with the name " + name);
         }
      }
   }

   public VertexFormat$Builder addAttribute(final String name, final GpuFormat elementFormat) {
      this.validateUniqueName(name);
      this.createAttribute(name, this.offset, elementFormat);
      this.offset = this.offset + elementFormat.blockSize();
      return this;
   }

   public VertexFormat$Builder addAttribute(final String name, final int stride, final GpuFormat elementFormat) {
      this.validateUniqueName(name);
      this.createAttribute(name, this.offset, elementFormat);
      this.offset += stride;
      return this;
   }

   public VertexFormat$Builder addAttribute(final String name, final GpuFormat elementFormat, final int columnCount) {
      this.validateUniqueName(name);

      for (int i = 0; i < columnCount; i++) {
         this.createAttribute(name, this.offset, elementFormat);
         this.offset = this.offset + elementFormat.blockSize();
      }

      return this;
   }

   public VertexFormat$Builder addAttribute(final String name, final int offset, final int stride, final GpuFormat elementFormat, final int columnCount) {
      this.validateUniqueName(name);
      int offsetTracker = offset;

      for (int i = 0; i < columnCount; i++) {
         this.createAttribute(name, offsetTracker, elementFormat);
         offsetTracker += stride;
      }

      this.offset = Math.max(this.offset, offsetTracker);
      return this;
   }

   public VertexFormat build() {
      int vertexSize = this.offset;
      if (!Mth.isMultipleOf(vertexSize, 4)) {
         throw new IllegalStateException("Vertex size must be a multiple of 4, was " + vertexSize);
      } else {
         return new VertexFormat(this.elements, vertexSize, this.stepRate);
      }
   }
}
