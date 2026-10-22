package com.mojang.renderpearl.api.commands;

import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceLists;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector4fc;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record RenderPassDescriptor(
   Supplier<String> label,
   List<RenderPassDescriptor$Attachment<Optional<Vector4fc>>> colorAttachments,
   @Nullable RenderPassDescriptor$Attachment<OptionalDouble> depthAttachment,
   RenderPass$RenderArea renderArea
) {
   public RenderPassDescriptor(
      Supplier<String> label,
      List<RenderPassDescriptor$Attachment<Optional<Vector4fc>>> colorAttachments,
      @Nullable RenderPassDescriptor$Attachment<OptionalDouble> depthAttachment,
      RenderPass$RenderArea renderArea
   ) {
      List<RenderPassDescriptor$Attachment<Optional<Vector4fc>>> var5 = ReferenceLists.unmodifiable(new ReferenceArrayList(colorAttachments));
      this.label = label;
      this.colorAttachments = var5;
      this.depthAttachment = depthAttachment;
      this.renderArea = renderArea;
   }

   public static RenderPassDescriptor$Builder builder(final Supplier<String> label) {
      return new RenderPassDescriptor$Builder(label);
   }
}
