package com.truspot.backend.entity;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.truspot.backend.abstracts.AEntity;
import com.truspot.backend.util.ValidateUtil;
import java.util.List;

/**
 * Created by yavoryordanov on 3/1/16.
 */
@Entity
public class SocialMediaItem extends AEntity<SocialMediaItem> {

    // variables
    @Id
    private Long id;
    @Parent
    private Key<Venue> parentKey;
    private String username;
    private String avatarUrl;
    private String text;
    private String photoUrl;
    private String videoUrl;

    // constructors
    public SocialMediaItem() {}

    public SocialMediaItem(String username, String avatarUrl, String text, String photoUrl, String videoUrl) {
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.text = text;
        this.photoUrl = photoUrl;
        this.videoUrl = videoUrl;
    }

    // methods
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public Key<SocialMediaItem> getKey() {
        return createKey(parentKey.getId(), id);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public Key<Venue> getVenueKey() {
        return parentKey;
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public void setVenueId(long venueId) {
        this.parentKey = Venue.createKey(venueId);
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public void setVenueKey(Key<Venue> venueKey) {
        this.parentKey = venueKey;
    }

    @Override
    public void checkValidation() throws Exception {
        ValidateUtil.checkNotNull(parentKey);
        ValidateUtil.checkNotEmpty(username);
    }

    // static methods
    public static List<Key<SocialMediaItem>> findKeysByVenue(long venueId) {
        return findKeysByParent(SocialMediaItem.class, Venue.createKey(venueId));
    }

    public static Key<SocialMediaItem> createKey(long venueId, long id) {
        return Key.create(Venue.createKey(venueId), SocialMediaItem.class, id);
    }

    public static SocialMediaItem findById(long venueId, long id) {
        return findByKey(createKey(venueId, id));
    }

    public static SocialMediaItem findByIdSafe(long venueId, long id) {
        return findByKeySafe(createKey(venueId, id));
    }

    public static List<SocialMediaItem> findByVenue(long venueId) {
        return findByParent(SocialMediaItem.class, Venue.createKey(venueId));
    }

    public static void deleteById(long venueId, long id) {
        deleteByKey(createKey(venueId, id));
    }

    public static void deleteByVenue(long venueId) {
        deleteByParent(SocialMediaItem.class, Venue.createKey(venueId));
    }
}