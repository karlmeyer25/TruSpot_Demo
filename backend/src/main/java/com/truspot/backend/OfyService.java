package com.truspot.backend;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.truspot.backend.entity.MapSettings;
import com.truspot.backend.entity.SocialMediaItem;
import com.truspot.backend.entity.Venue;

/**
 * Objectify service wrapper so we can statically register our persistence classes
 * More on Objectify here : https://code.google.com/p/objectify-appengine/
 *
 */
public class OfyService {

    static {
        ObjectifyService.register(Venue.class);
        ObjectifyService.register(SocialMediaItem.class);
        ObjectifyService.register(MapSettings.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
