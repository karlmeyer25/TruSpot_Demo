package com.truspot.backend.entity;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.truspot.backend.abstracts.AEntity;
import static com.truspot.backend.OfyService.ofy;

/**
 * Created by yavoryordanov on 3/1/16.
 */
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
    public Key<Venue> getParentKey() {
        return parentKey;
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public void setParentKey(Key<Venue> parentKey) {
        this.parentKey = parentKey;
    }

    @Override
    public void checkValidation() throws Exception {
        // TODO add func
    }

    // static methods
    public static SocialMediaItem findById(long id) {
        return ofy().load().type(SocialMediaItem.class).id(id).now();
    }

    public static SocialMediaItem findByIdSafe(long id) {
        return ofy().load().type(SocialMediaItem.class).id(id).safe();
    }
}
