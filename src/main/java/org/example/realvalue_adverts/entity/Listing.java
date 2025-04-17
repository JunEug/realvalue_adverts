package org.example.realvalue_adverts.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "listing", schema = "estate")
@Getter
@Setter
public class Listing {

    @Id
    @Column(name = "listing_id", nullable = false)
    private String listingId;

    @Column(name = "platform_id")
    private String platformId;

    @Column(name = "listing_url")
    private String listingUrl;

    @Column(name = "price")
    private String price;

    @Column(name = "price_per_sqm")
    private double pricePerSqm;

    @Column(name = "mortgage_rate")
    private double mortgageRate;

    @Column(name = "address")
    private String address;

    @Column(name = "area")
    private double area;

    @Column(name = "rooms")
    private int rooms;

    @Column(name = "floor")
    private int floor;

    @Column(name = "house_floors")
    private int houseFloors;

    @Column(name = "description")
    private String description;

    @Column(name = "updated_date")
    private String updateDate;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "property_type")
    private String propertyType;

    @Column(name = "deal_type")
    private String dealType;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "subway_names")
    private String subwayNames;

    @Column(name = "subway_distances")
    private String subwayDistances;

    @Column(name = "photo_urls")
    private String photoUrls;

    // No-arg constructor for JPA
    protected Listing() {}

    // All-args constructor
    public Listing(String listingId, String platformId, String listingUrl, String price, double pricePerSqm,
                   double mortgageRate, String address, double area, int rooms, int floor, int houseFloors,
                   String description, String updateDate, String companyName, String propertyType,
                   String dealType, double latitude, double longitude, String subwayNames,
                   String subwayDistances, String photoUrls) {
        this.listingId = listingId;
        this.platformId = platformId;
        this.listingUrl = listingUrl;
        this.price = price;
        this.pricePerSqm = pricePerSqm;
        this.mortgageRate = mortgageRate;
        this.address = address;
        this.area = area;
        this.rooms = rooms;
        this.floor = floor;
        this.houseFloors = houseFloors;
        this.description = description;
        this.updateDate = updateDate;
        this.companyName = companyName;
        this.propertyType = propertyType;
        this.dealType = dealType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.subwayNames = subwayNames;
        this.subwayDistances = subwayDistances;
        this.photoUrls = photoUrls;
    }

}