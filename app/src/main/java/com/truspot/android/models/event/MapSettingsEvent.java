package com.truspot.android.models.event;

import com.truspot.backend.api.model.MapSettings;

/**
 * Created by yavoryordanov on 3/15/16.
 */
public class MapSettingsEvent {
    public static class StartLoading {}

    public static class CompleteLoading {
        private MapSettings ms;

        public CompleteLoading(MapSettings ms) {
            this.ms = ms;
        }

        public MapSettings getMs() {
            return ms;
        }

        public void setMs(MapSettings ms) {
            this.ms = ms;
        }
    }
}