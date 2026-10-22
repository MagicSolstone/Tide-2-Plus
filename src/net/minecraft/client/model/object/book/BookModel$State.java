package net.minecraft.client.model.object.book;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public record BookModel$State(float openness, float pageFlip1, float pageFlip2) {
   public static BookModel$State forAnimation(final float progress, final float pageFlip1, final float pageFlip2, final float openness) {
      return new BookModel$State((Mth.sin(progress * 0.02F) * 0.1F + 1.25F) * openness, pageFlip1, pageFlip2);
   }
}
