package com.truspot.backend.entity;

import com.googlecode.objectify.annotation.Id;
import com.truspot.backend.abstracts.AEntity;
import com.truspot.backend.util.ValidateUtil;
import java.util.List;
import static com.truspot.backend.OfyService.ofy;

/**
 * Created by yavoryordanov on 3/1/16.
 */
public class Venue extends AEntity<Venue> {

    // variables
    @Id
    private Long id;
    private String name;
    private String description;
    private Double lat;
    private Double lng;
    private Integer capacity;
    private Integer occupancy;
    private String pdmColor;

    // constructors
    public Venue() {}

    public Venue(String name, String description, Double lat, Double lng, Integer capacity, Integer occupancy, String pdmColor) {
        this.name = name;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.capacity = capacity;
        this.occupancy = occupancy;
        this.pdmColor = pdmColor;
    }

    // methods
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(Integer occupancy) {
        this.occupancy = occupancy;
    }

    public String getPdmColor() {
        return pdmColor;
    }

    public void setPdmColor(String pdmColor) {
        this.pdmColor = pdmColor;
    }

    @Override
    public void checkValidation() throws Exception {
        ValidateUtil.checkNotEmpty(name, pdmColor);
        ValidateUtil.checkNotNull(lat, lng, capacity, occupancy);
        ValidateUtil.checkColorCode(pdmColor);
    }

    // static methods
    public static Venue findById(long id) {
        return ofy().load().type(Venue.class).id(id).now();
    }

    /**
     * @throws {@link com.google.api.server.spi.response.NotFoundException} - if the loaded value was not found
     * */
    public static Venue findByIdSafe(long id) {
        return ofy().load().type(Venue.class).id(id).safe();
    }

    public static List<Venue> findAll() {
        return ofy().load().type(Venue.class).list();
    }
}