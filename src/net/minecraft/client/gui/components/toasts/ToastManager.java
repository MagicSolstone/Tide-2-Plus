package net.minecraft.client.gui.components.toasts;

import com.google.common.collect.Queues;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MusicToastDisplayState;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ToastManager {
   private static final int SLOT_COUNT = 5;
   private static final int ALL_SLOTS_OCCUPIED = -1;
   private final Minecraft minecraft;
   private final List<ToastManager$ToastInstance<?>> visibleToasts = new ArrayList<>();
   private final BitSet occupiedSlots = new BitSet(5);
   private final Deque<Toast> queued = Queues.newArrayDeque();
   private final Set<SoundEvent> playedToastSounds = new HashSet<>();
   private ToastManager$ToastInstance<NowPlayingToast> nowPlayingToast;

   public ToastManager(final Minecraft minecraft, final Options options) {
      this.minecraft = minecraft;
      this.initializeMusicToast(options.musicToast().get());
   }

   public void update() {
      MutableBoolean visibilityChangeSoundPlayed = new MutableBoolean(false);
      this.visibleToasts.removeIf(toast -> {
         Toast$Visibility previousVisibility = toast.visibility;
         toast.update();
         if (toast.visibility != previousVisibility && visibilityChangeSoundPlayed.isFalse()) {
            visibilityChangeSoundPlayed.setTrue();
            toast.visibility.playSound(this.minecraft.getSoundManager());
         }

         if (toast.hasFinishedRendering()) {
            this.occupiedSlots.clear(toast.firstSlotIndex, toast.firstSlotIndex + toast.occupiedSlotCount);
            return true;
         } else {
            return false;
         }
      });
      if (!this.queued.isEmpty() && this.freeSlotCount() > 0) {
         this.queued.removeIf(toast -> {
            int occcupiedSlotCount = toast.occcupiedSlotCount();
            int firstSlotIndex = this.findFreeSlotsIndex(occcupiedSlotCount);
            if (firstSlotIndex == -1) {
               return false;
            } else {
               this.visibleToasts.add(new ToastManager$ToastInstance(this, (int)toast, firstSlotIndex, occcupiedSlotCount));
               this.occupiedSlots.set(firstSlotIndex, firstSlotIndex + occcupiedSlotCount);
               SoundEvent toastSound = toast.getSoundEvent();
               if (toastSound != null && this.playedToastSounds.add(toastSound)) {
                  this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(toastSound, 1.0F, 1.0F));
               }

               return true;
            }
         });
      }

      this.playedToastSounds.clear();
      if (this.nowPlayingToast != null) {
         this.nowPlayingToast.update();
      }
   }

   public void extractRenderState(final GuiGraphicsExtractor graphics) {
      if (!this.minecraft.gui.hud.isHidden()) {
         int screenWidth = graphics.guiWidth();
         if (!this.visibleToasts.isEmpty()) {
            graphics.nextStratum();
         }

         for (ToastManager$ToastInstance<?> toast : this.visibleToasts) {
            toast.extractRenderState(graphics, screenWidth);
         }

         if (this.minecraft.options.musicToast().get().renderToast()
            && this.nowPlayingToast != null
            && (this.minecraft.gui.screen() == null || !(this.minecraft.gui.screen() instanceof PauseScreen))) {
            this.nowPlayingToast.extractRenderState(graphics, screenWidth);
         }
      }
   }

   private int findFreeSlotsIndex(final int requiredCount) {
      if (this.freeSlotCount() >= requiredCount) {
         int consecutiveFreeSlotCount = 0;

         for (int i = 0; i < 5; i++) {
            if (this.occupiedSlots.get(i)) {
               consecutiveFreeSlotCount = 0;
            } else if (++consecutiveFreeSlotCount == requiredCount) {
               return i + 1 - consecutiveFreeSlotCount;
            }
         }
      }

      return -1;
   }

   private int freeSlotCount() {
      return 5 - this.occupiedSlots.cardinality();
   }

   @Nullable
   public <T extends Toast> T getToast(final Class<? extends T> clazz, final Object token) {
      for (ToastManager$ToastInstance<?> instance : this.visibleToasts) {
         if (clazz.isAssignableFrom(instance.getToast().getClass()) && instance.getToast().getToken().equals(token)) {
            return (T)instance.getToast();
         }
      }

      for (Toast toast : this.queued) {
         if (clazz.isAssignableFrom(toast.getClass()) && toast.getToken().equals(token)) {
            return (T)toast;
         }
      }

      return null;
   }

   public void clear() {
      this.occupiedSlots.clear();
      this.visibleToasts.clear();
      this.queued.clear();
   }

   public void addToast(final Toast toast) {
      this.queued.add(toast);
   }

   public void showNowPlayingToast() {
      if (this.nowPlayingToast != null) {
         this.nowPlayingToast.resetToast();
         this.nowPlayingToast.getToast().showToast(this.minecraft.options);
      }
   }

   public void hideNowPlayingToast() {
      if (this.nowPlayingToast != null) {
         this.nowPlayingToast.getToast().setWantedVisibility(Toast$Visibility.HIDE);
      }
   }

   public Minecraft getMinecraft() {
      return this.minecraft;
   }

   public double getNotificationDisplayTimeMultiplier() {
      return this.minecraft.options.notificationDisplayTime().get();
   }

   private void initializeMusicToast(final MusicToastDisplayState state) {
      switch (state) {
         case PAUSE:
         case PAUSE_AND_TOAST:
            this.nowPlayingToast = new ToastManager$ToastInstance<>(this, (int)(new NowPlayingToast()), 0, 0);
      }
   }

   public void setMusicToastDisplayState(final MusicToastDisplayState state) {
      switch (state) {
         case PAUSE:
            this.nowPlayingToast = new ToastManager$ToastInstance<>(this, (int)(new NowPlayingToast()), 0, 0);
            break;
         case PAUSE_AND_TOAST:
            this.nowPlayingToast = new ToastManager$ToastInstance<>(this, (int)(new NowPlayingToast()), 0, 0);
            if (this.minecraft.options.getFinalSoundSourceVolume(SoundSource.MUSIC) > 0.0F) {
               this.nowPlayingToast.getToast().showToast(this.minecraft.options);
            }
            break;
         case NEVER:
            this.nowPlayingToast = null;
      }
   }
}
