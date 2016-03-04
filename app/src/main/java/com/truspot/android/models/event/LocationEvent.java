package com.truspot.android.models.event;

import android.location.Location;

public class LocationEvent {
    public static class LocationAvailable {
        private Location location;

        public LocationAvailable(Location location) {
            this.location = location;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }
}
