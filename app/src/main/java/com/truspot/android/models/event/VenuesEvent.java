package com.truspot.android.models.event;

import com.truspot.backend.api.model.VenueFull;

import java.util.List;

/**
 * Created by yavoryordanov on 3/3/16.
 */
public class VenuesEvent {

    public static class StartLoading {}

    public static class CompleteLoading {
        private List<VenueFull> venues;

        public CompleteLoading(List<VenueFull> venues) {
            this.venues = venues;
        }

        public List<VenueFull> getVenues() {
            return venues;
        }

        public void setVenues(List<VenueFull> venues) {
            this.venues = venues;
        }
    }
}
