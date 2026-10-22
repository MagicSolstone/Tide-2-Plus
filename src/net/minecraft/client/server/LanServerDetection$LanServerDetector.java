package net.minecraft.client.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.DefaultUncaughtExceptionHandler;

@Environment(EnvType.CLIENT)
public class LanServerDetection$LanServerDetector extends Thread {
   private final LanServerDetection$LanServerList serverList;
   private final InetAddress pingGroup;
   private final MulticastSocket socket;

   public LanServerDetection$LanServerDetector(final LanServerDetection$LanServerList serverList) throws IOException {
      super("LanServerDetector #" + LanServerDetection.UNIQUE_THREAD_ID.incrementAndGet());
      this.serverList = serverList;
      this.setDaemon(true);
      this.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LanServerDetection.LOGGER));
      this.socket = new MulticastSocket(4445);
      this.pingGroup = InetAddress.getByName("224.0.2.60");
      this.socket.setSoTimeout(5000);
      this.socket.joinGroup(this.pingGroup);
   }

   @Override
   public void run() {
      byte[] buf = new byte[1024];

      while (!this.isInterrupted()) {
         DatagramPacket packet = new DatagramPacket(buf, buf.length);

         try {
            this.socket.receive(packet);
         } catch (SocketTimeoutException var5) {
            continue;
         } catch (IOException var6) {
            LanServerDetection.LOGGER.error("Couldn't ping server", var6);
            break;
         }

         String received = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
         LanServerDetection.LOGGER.debug("{}: {}", packet.getAddress(), received);
         this.serverList.addServer(received, packet.getAddress());
      }

      try {
         this.socket.leaveGroup(this.pingGroup);
      } catch (IOException var4) {
      }

      this.socket.close();
   }
}
