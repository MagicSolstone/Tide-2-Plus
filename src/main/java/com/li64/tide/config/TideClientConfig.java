package com.li64.tide.config;

import com.li64.tide.Tide;
public final class TideClientConfig {
    public General general;
    public Journal journal;
    public Minigame minigame;

    public TideClientConfig() {
        this.general = new General();
        this.journal = new Journal();
        this.minigame = new Minigame();
    }

    public static class General {
        public InfoPlacement infoPlacement = InfoPlacement.TOP_RIGHT;

        public enum InfoPlacement {
            TOP_LEFT, TOP_RIGHT,
            BOTTOM_LEFT, BOTTOM_RIGHT;

            public boolean isTop() {
                return this.ordinal() <= 1;
            }

            public boolean isLeft() {
                return (this.ordinal() % 2) == 0;
            }
        }
        public int infoOffsetX = 0;
        public int infoOffsetY = 0;
        public boolean ambientVoidParticles = true;
        public boolean defaultLineColor = false;
    }

    public static class Journal {
        public boolean showUnread = true;
        public boolean useAmPm = true;
        public boolean useFahrenheit = true;
        /** Fish lengths in inches/feet and depths in feet (instead of cm/m). */
        public boolean useImperial = true;
        public boolean useRealDate = true;
    }

    public static class Minigame {
        public boolean doFeedback = true;
    }
}