package com.li64.tide.registries.entities.renderers;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

/** What a fish renderer needs to know about one fish for one frame (26.x renders from a snapshot, not the entity). */
public class FishRenderState extends LivingEntityRenderState {
    /** In water or lava (fish flop sideways on land). */
    public boolean inLiquid;
    /** Nose up/down from swimming up or down, in radians. */
    public float tilt;
    /** Rendered on a fish display block rather than in the world: no swim offsets. */
    public boolean isDisplay;
}
