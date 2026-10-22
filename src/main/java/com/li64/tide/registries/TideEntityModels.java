package com.li64.tide.registries;

import com.li64.tide.Tide;
import com.li64.tide.registries.entities.misc.StarArrowRenderer;
import com.li64.tide.registries.entities.misc.fishing.StarArrowModel;
import com.li64.tide.registries.entities.misc.fishing.TideFishingHookModel;
import com.li64.tide.registries.entities.misc.fishing.TideFishingHookRenderer;
import com.li64.tide.registries.entities.models.*;
import com.li64.tide.registries.entities.renderers.FishRenderer;
import com.li64.tide.registries.entities.renderers.GlowingEyesFishRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.lang3.function.TriFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class TideEntityModels {
    public static final ArrayList<RendererRegistration<?>> RENDERERS = new ArrayList<>();
    public static final Map<ModelLayerLocation, Supplier<LayerDefinition>> LAYER_DEFS = new HashMap<>();

    public record RendererRegistration<T extends Entity>(EntityType<T> entityType, EntityRendererProvider<T> renderer) {}

    public static void init() {
        registerEntityRenderer(TideEntityTypes.FISHING_BOBBER, TideFishingHookRenderer::new);
        registerLayerDefinition(TideFishingHookModel.MODEL_LOCATION, TideFishingHookModel::createBodyLayer);
        registerLayerDefinition(TideFishingHookModel.BOBBER_LOCATION, TideFishingHookModel::createBodyLayer);

        registerEntityRenderer(TideEntityTypes.STAR_ARROW, StarArrowRenderer::new);
        registerLayerDefinition(StarArrowModel.MODEL_LOCATION, StarArrowModel::createBodyLayer);

        registerEntityRenderer(TideEntityTypes.LOOT_CRATE, FallingBlockRenderer::new);

        registerSimpleFish("rainbow_trout", TroutModel::new, TroutModel.MODEL_LOCATION, TroutModel::createBodyLayer);
        registerSimpleFish("brook_trout", TroutModel::new, TroutModel.MODEL_LOCATION, TroutModel::createBodyLayer);
        registerSimpleFish("largemouth_bass", LargemouthBassModel::new, LargemouthBassModel.MODEL_LOCATION, LargemouthBassModel::createBodyLayer);
        registerSimpleFish("smallmouth_bass", SmallmouthBassModel::new, SmallmouthBassModel.MODEL_LOCATION, SmallmouthBassModel::createBodyLayer);
        registerSimpleFish("white_crappie", CrappieModel::new, CrappieModel.MODEL_LOCATION, CrappieModel::createBodyLayer);
        registerSimpleFish("black_crappie", CrappieModel::new, CrappieModel.MODEL_LOCATION, CrappieModel::createBodyLayer);
        registerSimpleFish("yellow_perch", PerchModel::new, PerchModel.MODEL_LOCATION, PerchModel::createBodyLayer);
        registerSimpleFish("carp", CarpModel::new, CarpModel.MODEL_LOCATION, CarpModel::createBodyLayer);
        registerSimpleFish("pike", PikeModel::new, PikeModel.MODEL_LOCATION, PikeModel::createBodyLayer);
        registerSimpleFish("guppy", GuppyModel::new, GuppyModel.MODEL_LOCATION, GuppyModel::createBodyLayer);
        registerSimpleFish("bluegill", BluegillModel::new, BluegillModel.MODEL_LOCATION, BluegillModel::createBodyLayer);
        registerSimpleFish("catfish", CatfishModel::new, CatfishModel.MODEL_LOCATION, CatfishModel::createBodyLayer);
        registerSimpleFish("walleye", WalleyeModel::new, WalleyeModel.MODEL_LOCATION, WalleyeModel::createBodyLayer);
        registerSimpleFish("arapaima", ArapaimaModel::new, ArapaimaModel.MODEL_LOCATION, ArapaimaModel::createBodyLayer);
        registerSimpleFish("mirage_catfish", MirageCatfishModel::new, MirageCatfishModel.MODEL_LOCATION, CatfishModel::createBodyLayer);
        registerSimpleFish("sand_tiger_shark", SharkModel::new, SharkModel.MODEL_LOCATION, SharkModel::createBodyLayer);
        registerSimpleFish("slimy_salmon", SlimySalmonModel::new, SlimySalmonModel.MODEL_LOCATION, SlimySalmonModel::createBodyLayer);
        registerSimpleFish("frostbite_flounder", FlounderModel::new, FlounderModel.MODEL_LOCATION, FlounderModel::createBodyLayer);
        registerSimpleFish("blossom_bass", SmallmouthBassModel::new, SmallmouthBassModel.MODEL_LOCATION, SmallmouthBassModel::createBodyLayer);
        registerSimpleFish("sturgeon", SturgeonModel::new, SturgeonModel.MODEL_LOCATION, SturgeonModel::createBodyLayer);
        registerSimpleFish("spore_stalker", SporeStalkerModel::new, SporeStalkerModel.MODEL_LOCATION, SporeStalkerModel::createBodyLayer);
        registerSimpleFish("mooneye", MooneyeModel::new, MooneyeModel.MODEL_LOCATION, MooneyeModel::createBodyLayer);
        registerSimpleFish("bull_shark", SharkModel::new, SharkModel.MODEL_LOCATION, SharkModel::createBodyLayer);

        registerSimpleFish("ocean_perch", OceanPerchModel::new, OceanPerchModel.MODEL_LOCATION, OceanPerchModel::createBodyLayer);
        registerSimpleFish("red_snapper", SnapperModel::new, SnapperModel.MODEL_LOCATION, SnapperModel::createBodyLayer);
        registerSimpleFish("flounder", FlounderModel::new, FlounderModel.MODEL_LOCATION, FlounderModel::createBodyLayer);
        registerSimpleFish("anchovy", AnchovyModel::new, AnchovyModel.MODEL_LOCATION, AnchovyModel::createBodyLayer);
        registerSimpleFish("tuna", TunaModel::new, TunaModel.MODEL_LOCATION, TunaModel::createBodyLayer);
        registerSimpleFish("mackerel", MackerelModel::new, MackerelModel.MODEL_LOCATION, MackerelModel::createBodyLayer);
        registerSimpleFish("snook", SnookModel::new, SnookModel.MODEL_LOCATION, SnookModel::createBodyLayer);
        registerSimpleFish("angelfish", AngelfishModel::new, AngelfishModel.MODEL_LOCATION, AngelfishModel::createBodyLayer);
        registerSimpleFish("mahi_mahi", MahiMahiModel::new, MahiMahiModel.MODEL_LOCATION, MahiMahiModel::createBodyLayer);
        registerSimpleFish("sailfish", SailfishModel::new, SailfishModel.MODEL_LOCATION, SailfishModel::createBodyLayer);
        registerSimpleFish("swordfish", SwordfishModel::new, SwordfishModel.MODEL_LOCATION, SwordfishModel::createBodyLayer);
        registerSimpleFish("manta_ray", MantaRayModel::new, MantaRayModel.MODEL_LOCATION, MantaRayModel::createBodyLayer);
        registerSimpleFish("aquathorn", AquathornModel::new, AquathornModel.MODEL_LOCATION, AquathornModel::createBodyLayer);
        registerSimpleFish("neptune_koi", NeptuneKoiModel::new, NeptuneKoiModel.MODEL_LOCATION, NeptuneKoiModel::createBodyLayer);
        registerSimpleFish("pluto_snail", SnailModel::new, SnailModel.MODEL_LOCATION, SnailModel::createBodyLayer);
        registerSimpleFish("sun_emblem", SunEmblemModel::new, SunEmblemModel.MODEL_LOCATION, SunEmblemModel::createBodyLayer);
        registerSimpleFish("saturn_cuttlefish", SaturnCuttlefishModel::new, SaturnCuttlefishModel.MODEL_LOCATION, SaturnCuttlefishModel::createBodyLayer);
        registerSimpleFish("marstilus", MarstilusModel::new, MarstilusModel.MODEL_LOCATION, MarstilusModel::createBodyLayer);
        registerSimpleFish("uranias_pisces", UraniasPiscesModel::new, UraniasPiscesModel.MODEL_LOCATION, UraniasPiscesModel::createBodyLayer);
        registerSimpleFish("great_white_shark", GreatWhiteSharkModel::new, GreatWhiteSharkModel.MODEL_LOCATION, GreatWhiteSharkModel::createBodyLayer);
        registerSimpleFish("shooting_starfish", StarfishModel::new, StarfishModel.MODEL_LOCATION, StarfishModel::createBodyLayer);
        registerSimpleFish("coelacanth", CoelacanthModel::new, CoelacanthModel.MODEL_LOCATION, CoelacanthModel::createBodyLayer);

        registerSimpleFish("cave_eel", EelModel::new, EelModel.MODEL_LOCATION, EelModel::createBodyLayer);
        registerSimpleFish("cave_crawler", CaveCrawlerModel::new, CaveCrawlerModel.MODEL_LOCATION, CaveCrawlerModel::createBodyLayer);
        registerSimpleFish("deep_grouper", GrouperModel::new, GrouperModel.MODEL_LOCATION, GrouperModel::createBodyLayer);
        registerSimpleFish("shadow_snapper", BigMouthFishModel::new, BigMouthFishModel.MODEL_LOCATION, BigMouthFishModel::createBodyLayer);
        registerSimpleFish("glowfish", GlowfishModel::new, GlowfishModel.MODEL_LOCATION, GlowfishModel::createBodyLayer);
        registerCustomFish("anglerfish", AnglerfishModel::new, AnglerfishModel.MODEL_LOCATION, AnglerfishModel::createBodyLayer, GlowingEyesFishRenderer::new);
        registerCustomFish("abyss_angler", AbyssAnglerModel::new, AbyssAnglerModel.MODEL_LOCATION, AbyssAnglerModel::createBodyLayer, GlowingEyesFishRenderer::new);
        registerSimpleFish("iron_tetra", TetraModel::new, TetraModel.MODEL_LOCATION, TetraModel::createBodyLayer);
        registerSimpleFish("lapis_lanternfish", LanternfishModel::new, LanternfishModel.MODEL_LOCATION, LanternfishModel::createBodyLayer);
        registerSimpleFish("crystal_shrimp", ShrimpModel::new, ShrimpModel.MODEL_LOCATION, ShrimpModel::createBodyLayer);
        registerSimpleFish("dripstone_darter", DripstoneDarterModel::new, DripstoneDarterModel.MODEL_LOCATION, DripstoneDarterModel::createBodyLayer);
        registerSimpleFish("luminescent_jellyfish", JellyfishModel::new, JellyfishModel.MODEL_LOCATION, JellyfishModel::createBodyLayer);
        registerSimpleFish("crystalline_carp", CrystalCarpModel::new, CrystalCarpModel.MODEL_LOCATION, CrystalCarpModel::createBodyLayer);
        registerSimpleFish("gilded_minnow", MinnowModel::new, MinnowModel.MODEL_LOCATION, MinnowModel::createBodyLayer);
        registerSimpleFish("bedrock_tetra", TetraModel::new, TetraModel.MODEL_LOCATION, TetraModel::createBodyLayer);
        registerCustomFish("chasm_eel", ChasmEelModel::new, ChasmEelModel.MODEL_LOCATION, ChasmEelModel::createBodyLayer, GlowingEyesFishRenderer::new);
        registerSimpleFish("echo_snapper", EchoSnapperModel::new, EchoSnapperModel.MODEL_LOCATION, EchoSnapperModel::createBodyLayer);
        registerSimpleFish("windbass", WindbassModel::new, WindbassModel.MODEL_LOCATION, WindbassModel::createBodyLayer);
        registerSimpleFish("devils_hole_pupfish", PupfishModel::new, PupfishModel.MODEL_LOCATION, PupfishModel::createBodyLayer);
        registerSimpleFish("midas_fish", MidasFishModel::new, MidasFishModel.MODEL_LOCATION, MidasFishModel::createBodyLayer);

        registerSimpleFish("magma_mackerel", MackerelModel::new, MackerelModel.MODEL_LOCATION, MackerelModel::createBodyLayer);
        registerSimpleFish("ember_koi", KoiModel::new, KoiModel.MODEL_LOCATION, KoiModel::createBodyLayer);
        registerSimpleFish("ash_perch", PerchModel::new, PerchModel.MODEL_LOCATION, PerchModel::createBodyLayer);
        registerSimpleFish("obsidian_pike", PikeModel::new, PikeModel.MODEL_LOCATION, PikeModel::createBodyLayer);
        registerSimpleFish("volcano_tuna", GlowfishModel::new, GlowfishModel.MODEL_LOCATION, GlowfishModel::createBodyLayer);
        registerSimpleFish("inferno_guppy", InfernoGuppyModel::new, InfernoGuppyModel.MODEL_LOCATION, GuppyModel::createBodyLayer);
        registerSimpleFish("warped_guppy", GuppyModel::new, GuppyModel.MODEL_LOCATION, GuppyModel::createBodyLayer);
        registerSimpleFish("crimson_fangjaw", BigMouthFishModel::new, BigMouthFishModel.MODEL_LOCATION, BigMouthFishModel::createBodyLayer);
        registerSimpleFish("soulscale", SmallmouthBassModel::new, SmallmouthBassModel.MODEL_LOCATION, SmallmouthBassModel::createBodyLayer);
        registerSimpleFish("witherfin", WitherfinModel::new, WitherfinModel.MODEL_LOCATION, WitherfinModel::createBodyLayer);
        registerSimpleFish("blazing_swordfish", BlazingSwordfishModel::new, BlazingSwordfishModel.MODEL_LOCATION, BlazingSwordfishModel::createBodyLayer);

        registerSimpleFish("incandescent_larva", IncandescentLarvaModel::new, IncandescentLarvaModel.MODEL_LOCATION, IncandescentLarvaModel::createBodyLayer);
        registerSimpleFish("bedrock_bug", BedrockBugModel::new, BedrockBugModel.MODEL_LOCATION, BedrockBugModel::createBodyLayer);
        registerSimpleFish("sleepy_carp", SleepyCarpModel::new, SleepyCarpModel.MODEL_LOCATION, SleepyCarpModel::createBodyLayer);
        registerCustomFish("blue_neonfish", AnglerfishModel::new, AnglerfishModel.MODEL_LOCATION, AnglerfishModel::createBodyLayer, GlowingEyesFishRenderer::new);
        registerSimpleFish("judgment_fish", MackerelModel::new, MackerelModel.MODEL_LOCATION, MackerelModel::createBodyLayer);
        registerCustomFish("deep_blue", DeepBlueModel::new, DeepBlueModel.MODEL_LOCATION, DeepBlueModel::createBodyLayer, GlowingEyesFishRenderer::new);
        registerCustomFish("nephrosilu", NephrosiluModel::new, NephrosiluModel.MODEL_LOCATION, NephrosiluModel::createBodyLayer, GlowingEyesFishRenderer::new);
        registerCustomFish("vengeance", VengeanceModel::new, VengeanceModel.MODEL_LOCATION, VengeanceModel::createBodyLayer, GlowingEyesFishRenderer::new);
        registerSimpleFish("pentapus", PentapusModel::new, PentapusModel.MODEL_LOCATION, PentapusModel::createBodyLayer);
        registerSimpleFish("darkness_eater", DarknessEaterModel::new, DarknessEaterModel.MODEL_LOCATION, DarknessEaterModel::createBodyLayer);
        registerCustomFish("shadow_shark", ShadowSharkModel::new, ShadowSharkModel.MODEL_LOCATION, ShadowSharkModel::createBodyLayer, GlowingEyesFishRenderer::new);
        registerSimpleFish("alpha_fish", AlphaFishModel::new, AlphaFishModel.MODEL_LOCATION, AlphaFishModel::createBodyLayer);

        registerSimpleFish("pale_clubfish", PaleClubfishModel::new, PaleClubfishModel.MODEL_LOCATION, PaleClubfishModel::createBodyLayer);
        registerSimpleFish("amber_rockfish", AmberRockfishModel::new, AmberRockfishModel.MODEL_LOCATION, AmberRockfishModel::createBodyLayer);
        registerSimpleFish("enderfin", PerchModel::new, PerchModel.MODEL_LOCATION, PerchModel::createBodyLayer);
        registerSimpleFish("chorus_cod", ChorusCodModel::new, ChorusCodModel.MODEL_LOCATION, ChorusCodModel::createBodyLayer);
        registerSimpleFish("ender_glider", EnderGliderModel::new, EnderGliderModel.MODEL_LOCATION, EnderGliderModel::createBodyLayer);
        registerSimpleFish("endergazer", EndergazerModel::new, EndergazerModel.MODEL_LOCATION, EndergazerModel::createBodyLayer);
        registerSimpleFish("violet_carp", VioletCarpModel::new, VioletCarpModel.MODEL_LOCATION, VioletCarpModel::createBodyLayer);
        registerSimpleFish("red_40", Red40Model::new, Red40Model.MODEL_LOCATION, Red40Model::createBodyLayer);
        registerCustomFish("dutchman_sock", DutchmanSockModel::new, DutchmanSockModel.MODEL_LOCATION, DutchmanSockModel::createBodyLayer, GlowingEyesFishRenderer::new);
        registerSimpleFish("elytrout", ElytroutModel::new, ElytroutModel.MODEL_LOCATION, ElytroutModel::createBodyLayer);
        registerCustomFish("mantyvern", MantyvernModel::new, MantyvernModel.MODEL_LOCATION, MantyvernModel::createBodyLayer, GlowingEyesFishRenderer::new);
        registerSimpleFish("snatcher_squid", SnatcherSquidModel::new, SnatcherSquidModel.MODEL_LOCATION, SnatcherSquidModel::createBodyLayer);
        registerCustomFish("voidseeker", VoidseekerModel::new, VoidseekerModel.MODEL_LOCATION, VoidseekerModel::createBodyLayer, GlowingEyesFishRenderer::new);
        registerCustomFish("dragon_fish", DragonFishModel::new, DragonFishModel.MODEL_LOCATION, DragonFishModel::createBodyLayer, GlowingEyesFishRenderer::new);
    }

    public static void registerSimpleFish(String key, Function<EntityRendererProvider.Context, FishModel> modelSupplier,
                                          ModelLayerLocation location, Supplier<LayerDefinition> layerDefinition) {
        registerCustomFish(key, modelSupplier, location, layerDefinition, FishRenderer::new);
    }

    public static void registerCustomFish(String key, Function<EntityRendererProvider.Context, FishModel> modelSupplier,
                                          ModelLayerLocation location, Supplier<LayerDefinition> layerDefinition,
                                          TriFunction<String, FishModel, EntityRendererProvider.Context, FishRenderer<?>> rendererSupplier) {
        registerFishRenderer(key, ctx -> rendererSupplier.apply(key, modelSupplier.apply(ctx), ctx));
        registerLayerDefinition(location, layerDefinition);
    }

    @SuppressWarnings("unchecked")
    public static <T extends LivingEntity> void registerFishRenderer(String key, EntityRendererProvider<T> renderer) {
        EntityType<?> entityType = TideEntityTypes.ENTITY_TYPES.get(key);
        if (entityType == null) {
            Tide.LOG.error("Failed to register fish renderer for unknown entity type: '{}'", key);
            return;
        }
        try {
            registerEntityRenderer((EntityType<T>) entityType, renderer);
        } catch (Exception e) {
            Tide.LOG.error("Failed to register fish renderer for incompatible entity type: '{}'", key);
        }
    }

    public static <T extends Entity> void registerEntityRenderer(EntityType<T> entityType, EntityRendererProvider<T> renderer) {
        RENDERERS.add(new RendererRegistration<>(entityType, renderer));
    }

    public static void registerLayerDefinition(ModelLayerLocation location, Supplier<LayerDefinition> layerDefinition) {
        LAYER_DEFS.putIfAbsent(location, layerDefinition);
    }
}
