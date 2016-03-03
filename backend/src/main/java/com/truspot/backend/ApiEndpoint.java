/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.truspot.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.truspot.backend.entity.SocialMediaItem;
import com.truspot.backend.entity.Venue;
import com.truspot.backend.entity.VenueFull;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(
  name = "api",
  version = "v1",
  namespace = @ApiNamespace(
    ownerDomain = "backend.truspot.com",
    ownerName = "backend.truspot.com",
    packagePath=""
  )
)
public class ApiEndpoint {

    private static final String BASIC_TAG = ApiEndpoint.class.getName();

    private static final Logger LOG = Logger.getLogger(BASIC_TAG);

    @ApiMethod(name = "createVenue", path = "venues", httpMethod = ApiMethod.HttpMethod.POST)
    public Venue createVenue(Venue venue) throws Exception {
        venue.save();

        return venue;
    }

    @ApiMethod(name = "updateVenue", path = "venues/{id}", httpMethod = ApiMethod.HttpMethod.PUT)
    public Venue updateVenue(@Named("id") Long id, Venue venue) throws Exception {
        Venue old = Venue.findByIdSafe(id);

        venue.setId(old.getId());
        venue.save();

        return venue;
    }

    @ApiMethod(name = "removeVenue", path = "venues/{id}", httpMethod = ApiMethod.HttpMethod.DELETE)
    public void removeVenue(@Named("id") Long id) {
        Venue.deleteById(id);
    }

    @ApiMethod(name = "getVenue", path = "venues/{id}", httpMethod = ApiMethod.HttpMethod.GET)
    public Venue getVenue(@Named("id") Long id) {
        return Venue.findByIdSafe(id);
    }

    @ApiMethod(name = "getVenues", path = "venues", httpMethod = ApiMethod.HttpMethod.GET)
    public List<Venue> getVenues() {
        return Venue.findAll();
    }

    @ApiMethod(name = "createSocialMediaItem", path = "venues/{venueId}/socialmedia", httpMethod = ApiMethod.HttpMethod.POST)
    public SocialMediaItem createSocialMediaItem(@Named("venueId") Long venueId, SocialMediaItem item) throws Exception {
        item.setVenueId(venueId);
        item.save();

        return item;
    }

    @ApiMethod(name = "updateSocialMediaItem", path = "venues/{venueId}/socialmedia/{id}", httpMethod = ApiMethod.HttpMethod.PUT)
    public SocialMediaItem updateSocialMediaItem(@Named("venueId") Long venueId, @Named("id") Long id, SocialMediaItem item) throws Exception {
        SocialMediaItem old = SocialMediaItem.findByIdSafe(venueId, id);

        item.setId(old.getId());
        item.setVenueId(venueId);
        item.save();

        return item;
    }

    @ApiMethod(name = "removeSocialMediaItem", path = "venues/{venueId}/socialmedia/{id}", httpMethod = ApiMethod.HttpMethod.DELETE)
    public void removeSocialMediaItem(@Named("venueId") Long venueId, @Named("id") Long id) {
        LOG.info(String.format("remove SMI - Venue id: %s, id: %s", venueId, id));

        SocialMediaItem.deleteById(venueId, id);
    }

    @ApiMethod(name = "removeSocialMedia", path = "venues/{venueId}/socialmedia", httpMethod = ApiMethod.HttpMethod.DELETE)
    public void removeSocialMedia(@Named("venueId") Long venueId) {
        SocialMediaItem.deleteByVenue(venueId);
    }

    @ApiMethod(name = "getSocialMediaItem", path = "venues/{venueId}/socialmedia/{id}", httpMethod = ApiMethod.HttpMethod.GET)
    public SocialMediaItem getSocialMediaItem(@Named("venueId") Long venueId, @Named("id") Long id) {
            return SocialMediaItem.findByIdSafe(venueId, id);
    }

    @ApiMethod(name = "getSocialMedia", path = "venues/{venueId}/socialmedia", httpMethod = ApiMethod.HttpMethod.GET)
    public List<SocialMediaItem> getSocialMedia(@Named("venueId") Long venueId) {
        return SocialMediaItem.findByVenue(venueId);
    }

    @ApiMethod(name = "getVenueFull", path = "venues/{id}/full", httpMethod = ApiMethod.HttpMethod.GET)
    public VenueFull getVenueFull(@Named("id") Long id) {
        Venue venue = Venue.findByIdSafe(id);
        List<SocialMediaItem> feed = SocialMediaItem.findByVenue(id);

        return new VenueFull(venue, feed);
    }

    @ApiMethod(name = "getVenuesFull", path = "venues/full", httpMethod = ApiMethod.HttpMethod.GET)
    public Collection<VenueFull> getVenuesFull() {
        List<Venue> venues = Venue.findAll();

        if (venues != null && !venues.isEmpty()) {
            Map<Long, VenueFull> vfMap = new HashMap<>();

            for (Venue venue : venues) {
                VenueFull vf = new VenueFull(venue, null);

                vfMap.put(venue.getId(), vf);
            }

            List<SocialMediaItem> feed = SocialMediaItem.findAll();

            if (feed != null && !feed.isEmpty()) {
                for (SocialMediaItem item : feed) {
                    vfMap.get(item.getVenueId()).addSocialMediaItem(item);
                }
            }

            return vfMap.values();
        }

        return null;
    }
}