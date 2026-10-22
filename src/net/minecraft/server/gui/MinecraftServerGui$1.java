package net.minecraft.server.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import net.minecraft.server.dedicated.DedicatedServer;

class MinecraftServerGui$1 extends WindowAdapter {
   MinecraftServerGui$1(final MinecraftServerGui val$gui, final JFrame val$frame, final DedicatedServer val$server) {
      this.val$gui = val$guix;
      this.val$frame = val$framex;
      this.val$server = val$serverx;
   }

   @Override
   public void windowClosing(final WindowEvent event) {
      if (!this.val$gui.isClosing.getAndSet(true)) {
         this.val$frame.setTitle("Minecraft server - shutting down!");
         this.val$server.halt(true);
         this.val$gui.runFinalizers();
      }
   }
}
