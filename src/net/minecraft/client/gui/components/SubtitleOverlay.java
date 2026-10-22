package net.minecraft.client.gui.components;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.audio.ListenerTransform;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEventListener;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class SubtitleOverlay implements SoundEventListener {
   private static final int LOOPING_DISPLAY_FULL_BRIGHTNESS_TIME = 500;
   private static final long DISPLAY_TIME = 3000L;
   private final Minecraft minecraft;
   private final List<SubtitleOverlay$Subtitle> subtitles = Lists.newArrayList();
   private boolean isListening;
   private final List<SubtitleOverlay$Subtitle> audibleSubtitles = new ArrayList<>();

   public SubtitleOverlay(final Minecraft minecraft) {
      this.minecraft = minecraft;
   }

   public void extractRenderState(final GuiGraphicsExtractor graphics) {
      SoundManager soundManager = this.minecraft.getSoundManager();
      if (!this.isListening && this.minecraft.options.showSubtitles().get()) {
         soundManager.addListener(this);
         this.isListening = true;
      } else if (this.isListening && !this.minecraft.options.showSubtitles().get()) {
         soundManager.removeListener(this);
         this.isListening = false;
      }

      if (this.isListening) {
         ListenerTransform listener = soundManager.getListenerTransform();
         Vec3 position = listener.position();
         Vec3 forwards = listener.forward();
         Vec3 right = listener.right();
         double displayTimeMultiplier = this.minecraft.options.notificationDisplayTime().get();
         this.audibleSubtitles.clear();
         int width = 0;

         for (SubtitleOverlay$Subtitle subtitle : this.subtitles) {
            if (!subtitle.playedAt.isEmpty()) {
               subtitle.purgeOldInstances(3000.0 * displayTimeMultiplier);
            }

            if (subtitle.isAudibleFrom(position) && subtitle.isStillActive()) {
               this.audibleSubtitles.add(subtitle);
               width = Math.max(width, this.minecraft.font.width(subtitle.getText()));
            }
         }

         if (!this.audibleSubtitles.isEmpty()) {
            width += this.minecraft.font.width("<") + this.minecraft.font.width(" ") + this.minecraft.font.width(">") + this.minecraft.font.width(" ");
            if (!this.audibleSubtitles.isEmpty()) {
               graphics.nextStratum();
            }

            int row = 0;

            for (SubtitleOverlay$Subtitle subtitle : this.audibleSubtitles) {
               int alpha = 255;
               Component text = subtitle.getText();
               SubtitleOverlay$SoundPlayedAt closestRecentLocation = subtitle.getBestSubtitleCandidate(position);
               if (closestRecentLocation != null) {
                  Vec3 delta = closestRecentLocation.location.subtract(position).normalize();
                  double rightness = right.dot(delta);
                  double forwardness = forwards.dot(delta);
                  boolean inView = forwardness > 0.5;
                  int halfWidth = width / 2;
                  int height = 9;
                  int halfHeight = height / 2;
                  float scale = 1.0F;
                  int textWidth = this.minecraft.font.width(text);
                  int brightness = subtitle.getBrightness(closestRecentLocation, displayTimeMultiplier);
                  graphics.pose().pushMatrix();
                  graphics.pose().translate(graphics.guiWidth() - halfWidth * 1.0F - 2.0F, graphics.guiHeight() - 35 - row * (height + 1) * 1.0F);
                  graphics.pose().scale(1.0F, 1.0F);
                  graphics.fill(-halfWidth - 1, -halfHeight - 1, halfWidth + 1, halfHeight + 1, this.minecraft.options.getBackgroundColor(0.8F));
                  int textColor = ARGB.color(255, brightness, brightness, brightness);
                  if (!inView) {
                     if (rightness > 0.0) {
                        graphics.text(this.minecraft.font, ">", halfWidth - this.minecraft.font.width(">"), -halfHeight, textColor);
                     } else if (rightness < 0.0) {
                        graphics.text(this.minecraft.font, "<", -halfWidth, -halfHeight, textColor);
                     }
                  }

                  graphics.text(this.minecraft.font, text, -textWidth / 2, -halfHeight, textColor);
                  graphics.pose().popMatrix();
                  row++;
               }
            }
         }
      }
   }

   @Override
   public void onPlaySound(final SoundInstance sound, final WeighedSoundEvents soundEvent, final float range) {
      if (soundEvent.getSubtitle() != null) {
         Component text = soundEvent.getSubtitle();
         if (!this.subtitles.isEmpty()) {
            for (SubtitleOverlay$Subtitle subtitle : this.subtitles) {
               if (subtitle.getText().equals(text)) {
                  subtitle.refresh(new Vec3(sound.getX(), sound.getY(), sound.getZ()));
                  return;
               }
            }
         }

         this.subtitles.add(new SubtitleOverlay$Subtitle(text, range, new Vec3(sound.getX(), sound.getY(), sound.getZ()), sound.isLooping()));
      }
   }
}
