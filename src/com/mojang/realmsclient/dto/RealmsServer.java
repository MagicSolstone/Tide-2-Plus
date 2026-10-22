package com.mojang.realmsclient.dto;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.mojang.logging.LogUtils;
import com.mojang.util.UUIDTypeAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerData$Type;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class RealmsServer extends ValueObject implements ReflectionBasedSerialization {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final int NO_VALUE = -1;
   public static final Component WORLD_CLOSED_COMPONENT = Component.translatable("mco.play.button.realm.closed");
   @SerializedName("id")
   public long id = -1L;
   @SerializedName("remoteSubscriptionId")
   @Nullable
   public String remoteSubscriptionId;
   @SerializedName("name")
   @Nullable
   public String name;
   @SerializedName("motd")
   public String motd = "";
   @SerializedName("state")
   public RealmsServer$State state = RealmsServer$State.CLOSED;
   @SerializedName("owner")
   @Nullable
   public String owner;
   @SerializedName("ownerUUID")
   @JsonAdapter(UUIDTypeAdapter.class)
   public UUID ownerUUID = Util.NIL_UUID;
   @SerializedName("players")
   public List<PlayerInfo> players = Lists.newArrayList();
   @SerializedName("slots")
   private List<RealmsSlot> slotList = createEmptySlots();
   @Exclude
   public Map<Integer, RealmsSlot> slots = new HashMap<>();
   @SerializedName("expired")
   public boolean expired;
   @SerializedName("expiredTrial")
   public boolean expiredTrial = false;
   @SerializedName("daysLeft")
   public int daysLeft;
   @SerializedName("worldType")
   public RealmsServer$WorldType worldType = RealmsServer$WorldType.NORMAL;
   @SerializedName("isHardcore")
   public boolean isHardcore = false;
   @SerializedName("gameMode")
   public int gameMode = -1;
   @SerializedName("activeSlot")
   public int activeSlot = -1;
   @SerializedName("minigameName")
   @Nullable
   public String minigameName;
   @SerializedName("minigameId")
   public int minigameId = -1;
   @SerializedName("minigameImage")
   @Nullable
   public String minigameImage;
   @SerializedName("parentWorldId")
   public long parentRealmId = -1L;
   @SerializedName("parentWorldName")
   @Nullable
   public String parentWorldName;
   @SerializedName("activeVersion")
   public String activeVersion = "";
   @SerializedName("compatibility")
   public RealmsServer$Compatibility compatibility = RealmsServer$Compatibility.UNVERIFIABLE;
   @SerializedName("regionSelectionPreference")
   @Nullable
   public RegionSelectionPreferenceDto regionSelectionPreference;

   public String getDescription() {
      return this.motd;
   }

   @Nullable
   public String getName() {
      return this.name;
   }

   @Nullable
   public String getMinigameName() {
      return this.minigameName;
   }

   public void setName(final String name) {
      this.name = name;
   }

   public void setDescription(final String motd) {
      this.motd = motd;
   }

   public static RealmsServer parse(final GuardedSerializer gson, final String json) {
      try {
         RealmsServer server = gson.fromJson(json, RealmsServer.class);
         if (server == null) {
            LOGGER.error("Could not parse McoServer: {}", json);
            return new RealmsServer();
         } else {
            finalize(server);
            return server;
         }
      } catch (Exception var3) {
         LOGGER.error("Could not parse McoServer", var3);
         return new RealmsServer();
      }
   }

   public static void finalize(final RealmsServer server) {
      if (server.players == null) {
         server.players = Lists.newArrayList();
      }

      if (server.slotList == null) {
         server.slotList = createEmptySlots();
      }

      if (server.slots == null) {
         server.slots = new HashMap<>();
      }

      if (server.worldType == null) {
         server.worldType = RealmsServer$WorldType.NORMAL;
      }

      if (server.activeVersion == null) {
         server.activeVersion = "";
      }

      if (server.compatibility == null) {
         server.compatibility = RealmsServer$Compatibility.UNVERIFIABLE;
      }

      if (server.regionSelectionPreference == null) {
         server.regionSelectionPreference = RegionSelectionPreferenceDto.DEFAULT;
      }

      sortInvited(server);
      finalizeSlots(server);
   }

   private static void sortInvited(final RealmsServer server) {
      server.players
         .sort(
            (o1, o2) -> ComparisonChain.start()
               .compareFalseFirst(o2.accepted, o1.accepted)
               .compare(o1.name.toLowerCase(Locale.ROOT), o2.name.toLowerCase(Locale.ROOT))
               .result()
         );
   }

   private static void finalizeSlots(final RealmsServer server) {
      server.slotList.forEach(s -> server.slots.put(s.slotId, s));

      for (int i = 1; i <= 3; i++) {
         if (!server.slots.containsKey(i)) {
            server.slots.put(i, RealmsSlot.defaults(i));
         }
      }
   }

   private static List<RealmsSlot> createEmptySlots() {
      List<RealmsSlot> slots = new ArrayList<>();
      slots.add(RealmsSlot.defaults(1));
      slots.add(RealmsSlot.defaults(2));
      slots.add(RealmsSlot.defaults(3));
      return slots;
   }

   public boolean isCompatible() {
      return this.compatibility.isCompatible();
   }

   public boolean needsUpgrade() {
      return this.compatibility.needsUpgrade();
   }

   public boolean needsDowngrade() {
      return this.compatibility.needsDowngrade();
   }

   public boolean shouldPlayButtonBeActive() {
      boolean active = !this.expired && this.state == RealmsServer$State.OPEN;
      return active && (this.isCompatible() || this.needsUpgrade() || this.isSelfOwnedServer());
   }

   private boolean isSelfOwnedServer() {
      return Minecraft.getInstance().isLocalPlayer(this.ownerUUID);
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.id, this.name, this.motd, this.state, this.owner, this.expired);
   }

   @Override
   public boolean equals(final Object obj) {
      if (obj == null) {
         return false;
      } else if (obj == this) {
         return true;
      } else if (obj.getClass() != this.getClass()) {
         return false;
      } else {
         RealmsServer rhs = (RealmsServer)obj;
         return new EqualsBuilder()
            .append(this.id, rhs.id)
            .append(this.name, rhs.name)
            .append(this.motd, rhs.motd)
            .append(this.state, rhs.state)
            .append(this.owner, rhs.owner)
            .append(this.expired, rhs.expired)
            .append(this.worldType, this.worldType)
            .isEquals();
      }
   }

   public RealmsServer copy() {
      RealmsServer server = new RealmsServer();
      server.id = this.id;
      server.remoteSubscriptionId = this.remoteSubscriptionId;
      server.name = this.name;
      server.motd = this.motd;
      server.state = this.state;
      server.owner = this.owner;
      server.players = this.players;
      server.slotList = this.slotList.stream().map(RealmsSlot::copy).toList();
      server.slots = this.cloneSlots(this.slots);
      server.expired = this.expired;
      server.expiredTrial = this.expiredTrial;
      server.daysLeft = this.daysLeft;
      server.worldType = this.worldType;
      server.isHardcore = this.isHardcore;
      server.gameMode = this.gameMode;
      server.ownerUUID = this.ownerUUID;
      server.minigameName = this.minigameName;
      server.activeSlot = this.activeSlot;
      server.minigameId = this.minigameId;
      server.minigameImage = this.minigameImage;
      server.parentWorldName = this.parentWorldName;
      server.parentRealmId = this.parentRealmId;
      server.activeVersion = this.activeVersion;
      server.compatibility = this.compatibility;
      server.regionSelectionPreference = this.regionSelectionPreference != null ? this.regionSelectionPreference.copy() : null;
      return server;
   }

   public Map<Integer, RealmsSlot> cloneSlots(final Map<Integer, RealmsSlot> slots) {
      Map<Integer, RealmsSlot> newSlots = Maps.newHashMap();

      for (Entry<Integer, RealmsSlot> entry : slots.entrySet()) {
         newSlots.put(entry.getKey(), new RealmsSlot(entry.getKey(), entry.getValue().options.copy(), entry.getValue().settings));
      }

      return newSlots;
   }

   public boolean isSnapshotRealm() {
      return this.parentRealmId != -1L;
   }

   public boolean isMinigameActive() {
      return this.worldType == RealmsServer$WorldType.MINIGAME;
   }

   public String getWorldName(final int slotId) {
      return this.name == null
         ? this.slots.get(slotId).options.getSlotName(slotId)
         : this.name + " (" + this.slots.get(slotId).options.getSlotName(slotId) + ")";
   }

   public ServerData toServerData(final String ip) {
      return new ServerData(Objects.requireNonNullElse(this.name, "unknown server"), ip, ServerData$Type.REALM);
   }
}
