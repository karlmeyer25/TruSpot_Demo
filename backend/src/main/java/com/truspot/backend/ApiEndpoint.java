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
import java.util.List;
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

    @ApiMethod(name = "createVenue", path = "venues", httpMethod = ApiMethod.HttpMethod.POST)
    public Venue createVenue(Venue venue) throws Exception {
        venue.save();

        return venue;
    }

    @ApiMethod(name = "updateVenue", path = "venues/{id}", httpMethod = ApiMethod.HttpMethod.PUT)
    public Venue updateVenue(@Named("id") Long id, Venue venue) throws Exception {
        venue.setId(id);
        venue.save();

        return venue;
    }

    @ApiMethod(name = "getVenue", path = "venues/{id}", httpMethod = ApiMethod.HttpMethod.GET)
    public Venue getVenue(@Named("id") Long id) {
        return Venue.findByIdSafe(id);
    }

    @ApiMethod(name = "getVenues", path = "venues", httpMethod = ApiMethod.HttpMethod.GET)
    public List<Venue> getVenues() {
        return Venue.findAll();
    }

    @ApiMethod(name = "createSocialMedia", path = "venues/{id}/socialmedia", httpMethod = ApiMethod.HttpMethod.POST)
    public SocialMediaItem createSocialMedia(SocialMediaItem item) throws Exception {
        item.save();

        return item;
    }
}
