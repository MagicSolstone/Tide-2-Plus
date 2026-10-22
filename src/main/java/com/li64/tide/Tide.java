package com.li64.tide;

import com.li64.tide.config.TideClientConfig;
import com.li64.tide.config.TideConfig;
import com.li64.tide.config.TideServerConfig;
import com.li64.tide.data.TideFishingManager;
import com.li64.tide.loaders.LoaderPlatform;
import com.li64.tide.loaders.NetworkPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 import com.li64.tide.loaders.fabric.*;
 /*import com.li64.tide.loaders.neoforge.*;*/
 /*import com.li64.tide.loaders.forge.*;*/

// looks like you've stumbled upon the code for
// .___________. __   _______   _______
// |           ||  | |       \ |   ____|
// `---|  |----`|  | |  .--.  ||  |__
//     |  |     |  | |  |  |  ||   __|
//     |  |     |  | |  '--'  ||  |____
//     |__|     |__| |_______/ |_______|  (v2)
// everyone's favorite fishing mod! (probably)
// released in 2023 by Lightning64 (v2 in 2026)
public class Tide {
    public static final String MOD_ID = "tide";
    public static final String MOD_NAME = "Tide";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final LoaderPlatform PLATFORM;
    public static final NetworkPlatform NETWORK;
    static {
        PLATFORM = new FabricPlatform();
        NETWORK = new FabricNetworkPlatform();
                   /*new FabricLegacyNetworkPlatform();*/
    }

    public static TideConfig CONFIG;
    public static TideClientConfig CLIENT_CONFIG;
    public static TideServerConfig SERVER_CONFIG;

    public static TideFishingManager FISHING_MANAGER;

    public static ResourceLocation resource(String path) {
        return resource(MOD_ID, path);
    }

    public static ResourceLocation resource(String namespace, String path) {
         return ResourceLocation.fromNamespaceAndPath(namespace, path);
         /*return new ResourceLocation(namespace, path);*/
    }

    public static void initialize() {
        Tide.setupConfigs();
        LOG.info("Initialized Tide on {} {}", PLATFORM.getPlatformName(), PLATFORM.getMCVersion());
    }

    public static void setupConfigs() {
        if (CONFIG == null) {
            CONFIG = TideConfig.load(net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir());
            CLIENT_CONFIG = CONFIG.client();
            SERVER_CONFIG = CONFIG.server();
        }
    }
}