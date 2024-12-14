package org.example.realvalue_adverts.service;

import org.example.realvalue_adverts.entity.Listing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Service
public class ListingService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> getTopListings(int limit, int page) {
        int offset = (page - 1) * limit;
        String query = "SELECT listing_id, platform_id, listing_url, price, price_per_sqm, mortgage_rate, address, area, rooms, floor, house_floors, description, updated_date, company_name, property_type, deal_type, latitude, longitude, subway_names, subway_distances, photo_urls " +
                "FROM estate.listings WHERE floor != 0 " +
                "LIMIT ? OFFSET ?";
        List<Listing> listings = jdbcTemplate.query(query, new Object[]{limit, offset}, listingRowMapper());

        // Используем LinkedHashMap для сохранения порядка добавления элементов
        Map<String, Object> result = new LinkedHashMap<>();
        for (int i = 0; i < listings.size(); i++) {
            Listing listing = listings.get(i);
            Map<String, Object> listingData = new LinkedHashMap<>();
            listingData.put("listingId", listing.getListingId());
            listingData.put("platformId", listing.getPlatformId());
            listingData.put("listingUrl", listing.getListingUrl());
            listingData.put("price", listing.getPrice());
            listingData.put("pricePerSqm", listing.getPricePerSqm());
            listingData.put("mortgageRate", listing.getMortgageRate());
            listingData.put("address", listing.getAddress());
            listingData.put("area", listing.getArea());
            listingData.put("rooms", listing.getRooms());
            listingData.put("floor", listing.getFloor());
            listingData.put("house_floors", listing.getHouseFloors());
            listingData.put("description", listing.getDescription());
            listingData.put("updateDate", listing.getUpdateDate());
            listingData.put("companyName", listing.getCompanyName());
            listingData.put("propertyType", listing.getPropertyType());
            listingData.put("dealType", listing.getDealType());
            listingData.put("latitude", listing.getLatitude());
            listingData.put("longitude", listing.getLongitude());
            listingData.put("subwayNames", listing.getSubwayNames());
            listingData.put("subwayDistances", listing.getSubwayDistances());
            listingData.put("photoUrls", listing.getPhotoUrls());

            result.put("listing" + (i + 1), listingData); // Ключи будут идти по порядку: listing1, listing2, listing3
        }

        return result;
    }

    private RowMapper<Listing> listingRowMapper() {
        return (rs, rowNum) -> new Listing(
                rs.getString("listing_id"),
                rs.getString("platform_id"),
                rs.getString("listing_url"),
                rs.getInt("price"),
                rs.getDouble("price_per_sqm"),
                rs.getDouble("mortgage_rate"),
                rs.getString("address"),
                rs.getDouble("area"),
                rs.getInt("rooms"),
                rs.getInt("floor"),
                rs.getInt("house_floors"),
                rs.getString("description"),
                rs.getString("updated_date"),
                rs.getString("company_name"),
                rs.getString("property_type"),
                rs.getString("deal_type"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude"),
                rs.getString("subway_names"),
                rs.getString("subway_distances"),
                rs.getString("photo_urls")
        );
    }
}
