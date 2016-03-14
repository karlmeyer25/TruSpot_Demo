package com.truspot.backend.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.truspot.backend.abstracts.AEntity;

/**
 * Created by yavoryordanov on 3/14/16.
 */
@Entity
public class MapSettings extends AEntity<MapSettings>{

    // variables
    @Id
    private Long id;
    private Double lat;
    private Double lng;
    private Integer zoom;

    // constructors
    public MapSettings() {}

    public MapSettings(Double lat, Double lng, Integer zoom) {
        this.lat = lat;
        this.lng = lng;
        this.zoom = zoom;
    }

    // methods
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getZoom() {
        return zoom;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    @Override
    public void checkValidation() throws Exception {
    }

    // static methods
    public static MapSettings findFirst() {
        return findFirst(MapSettings.class);
    }

    public static MapSettings findFirstSafe() {
        return findFirstSafe(MapSettings.class);
    }
}