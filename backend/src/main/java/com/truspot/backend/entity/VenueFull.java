package com.truspot.backend.entity;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yavoryordanov on 3/3/16.
 */
public class VenueFull {
    // variables
    private Venue venue;
    private List<SocialMediaItem> feed;

    // constructors
    public VenueFull() {}

    public VenueFull(Venue venue, List<SocialMediaItem> feed) {
        this.venue = venue;
        this.feed = feed;
    }

    // methods
    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public List<SocialMediaItem> getFeed() {
        return feed;
    }

    public void setFeed(List<SocialMediaItem> feed) {
        this.feed = feed;
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public void addSocialMediaItem(SocialMediaItem item) {
        if (feed == null) {
            feed = new ArrayList<>();
        }

        feed.add(item);
    }
}