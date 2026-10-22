package net.minecraft.client.renderer.debug;

import java.util.Map;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.SectionPos;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.TextGizmo$Style;
import net.minecraft.util.Util;
import net.minecraft.util.debug.DebugValueAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class ChunkDebugRenderer implements DebugRenderer$SimpleDebugRenderer {
   private final Minecraft minecraft;
   private double lastUpdateTime = Double.MIN_VALUE;
   private final int radius = 12;
   private ChunkDebugRenderer$ChunkData data;

   public ChunkDebugRenderer(final Minecraft minecraft) {
      this.minecraft = minecraft;
   }

   @Override
   public void emitGizmos(
      final double camX, final double camY, final double camZ, final DebugValueAccess debugValues, final Frustum frustum, final float partialTicks
   ) {
      double time = Util.getNanos();
      if (time - this.lastUpdateTime > 3.0E9) {
         this.lastUpdateTime = time;
         IntegratedServer server = this.minecraft.getSingleplayerServer();
         if (server != null) {
            this.data = new ChunkDebugRenderer$ChunkData(this, server, camX, camZ);
         } else {
            this.data = null;
         }
      }

      if (this.data != null) {
         Map<ChunkPos, String> serverData = this.data.serverData.getNow(null);
         double y = this.minecraft.gameRenderer.mainCamera().position().y * 0.85;

         for (Entry<ChunkPos, String> entry : this.data.clientData.entrySet()) {
            ChunkPos pos = entry.getKey();
            String value = entry.getValue();
            if (serverData != null) {
               value = value + serverData.get(pos);
            }

            String[] parts = value.split("\n");
            int yOffset = 0;

            for (String part : parts) {
               Gizmos.billboardText(
                     part,
                     new Vec3(SectionPos.sectionToBlockCoord(pos.x(), 8), y + yOffset, SectionPos.sectionToBlockCoord(pos.z(), 8)),
                     TextGizmo$Style.whiteAndCentered().withScale(2.4F)
                  )
                  .setAlwaysOnTop();
               yOffset -= 2;
            }
         }
      }
   }
}
