package org.example.realvalue_adverts.service;

import org.example.realvalue_adverts.entity.Listing;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import static org.example.realvalue_adverts.service.SearchService.getListingRowMapper;
import static org.example.realvalue_adverts.service.SearchService.getStringObjectMap;

@Service
public class ListingService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> getTopListings(int limit, int page) {
        int offset = (page - 1) * limit;
        String query = "WITH grouped_listings AS (\n" +
                "    SELECT \n" +
                "        listing_id, \n" +
                "        platform_id, \n" +
                "        listing_url, \n" +
                "        price, \n" +
                "        price_per_sqm, \n" +
                "        mortgage_rate, \n" +
                "        address, \n" +
                "        area, \n" +
                "        rooms, \n" +
                "        floor, \n" +
                "        house_floors, \n" +
                "        description, \n" +
                "        updated_date, \n" +
                "        company_name, \n" +
                "        property_type, \n" +
                "        deal_type, \n" +
                "        latitude, \n" +
                "        longitude, \n" +
                "        subway_names, \n" +
                "        subway_distances, \n" +
                "        photo_urls,\n" +
                "        group_id,\n" +
                "        ROW_NUMBER() OVER (PARTITION BY group_id ORDER BY listing_id) as rn\n" +
                "    FROM estate.property_listings\n" +
                "    WHERE floor != 0\n" +
                ")\n" +
                "SELECT \n" +
                "    listing_id, \n" +
                "    platform_id, \n" +
                "    listing_url, \n" +
                "    price, \n" +
                "    price_per_sqm, \n" +
                "    mortgage_rate, \n" +
                "    address, \n" +
                "    area, \n" +
                "    rooms, \n" +
                "    floor, \n" +
                "    house_floors, \n" +
                "    description, \n" +
                "    updated_date, \n" +
                "    company_name, \n" +
                "    property_type, \n" +
                "    deal_type, \n" +
                "    latitude, \n" +
                "    longitude, \n" +
                "    subway_names, \n" +
                "    subway_distances, \n" +
                "    photo_urls\n" +
                "FROM (\n" +
                "    -- Сначала выбираем по одной записи для каждого group_id, который встречается более одного раза\n" +
                "    SELECT * FROM grouped_listings \n" +
                "    WHERE group_id IN (\n" +
                "        SELECT group_id \n" +
                "        FROM estate.property_listings \n" +
                "        WHERE group_id IS NOT NULL \n" +
                "        GROUP BY group_id \n" +
                "        HAVING COUNT(*) > 1\n" +
                "    ) \n" +
                "    AND rn = 1\n" +
                "    \n" +
                "    UNION ALL\n" +
                "    \n" +
                "    -- Затем добавляем все записи, у которых group_id NULL или встречается только один раз\n" +
                "    SELECT * FROM grouped_listings \n" +
                "    WHERE group_id IS NULL \n" +
                "    OR group_id IN (\n" +
                "        SELECT group_id \n" +
                "        FROM estate.property_listings \n" +
                "        WHERE group_id IS NOT NULL \n" +
                "        GROUP BY group_id \n" +
                "        HAVING COUNT(*) = 1\n" +
                "    )\n" +
                ") AS combined_results\n" +
                "ORDER BY \n" +
                "    CASE \n" +
                "        WHEN group_id IN (\n" +
                "            SELECT group_id \n" +
                "            FROM estate.property_listings \n" +
                "            WHERE group_id IS NOT NULL \n" +
                "            GROUP BY group_id \n" +
                "            HAVING COUNT(*) > 1\n" +
                "        ) THEN 0 \n" +
                "        ELSE 1 \n" +
                "    END,\n" +
                "    listing_id\n" +
                "LIMIT ? OFFSET ?";
        List<Listing> listings = jdbcTemplate.query(query, new Object[]{limit, offset}, listingRowMapper());

        return getStringObjectMap(listings);
    }

    private RowMapper<Listing> listingRowMapper() {
        return getListingRowMapper();
    }

    public int getTotalPages() {
        String countQuery = "SELECT COUNT(listing_id)" +
                "FROM estate.property_listings WHERE floor != 0 ";
        Integer totalRecords = jdbcTemplate.queryForObject(countQuery, Integer.class);

        return (int) Math.ceil((double) totalRecords / 9);
    }

    public Map<String, Object> getListingById(String cardId) {
        try {
            long id = Long.parseLong(cardId);
            String query = "SELECT listing_id, platform_id, listing_url, price, price_per_sqm, mortgage_rate, address, area, rooms, floor, house_floors, description, updated_date, company_name, property_type, deal_type, latitude, longitude, subway_names, subway_distances, photo_urls " +
                    "FROM estate.property_listings WHERE listing_id = ?";
            List<Listing> listings = jdbcTemplate.query(query, new Object[]{id}, listingRowMapper());

            if (listings.isEmpty()) {
                return Map.of("error", "Listing not found");
            }

            return getStringObjectMap(listings);
        } catch (NumberFormatException e) {
            return Map.of("error", "Invalid listing ID format");
        }
    }

    public Map<String, Object> getGroupedListings(String cardId) {
        try {
            long id = Long.parseLong(cardId);

            String groupQuery = "SELECT group_id FROM estate.property_listings WHERE listing_id = ?";
            String groupId = jdbcTemplate.queryForObject(groupQuery, String.class, id);

            if (groupId == null || groupId.isEmpty()) {
                return Map.of("error", "Group not found for this listing");
            }

            String listingsQuery = "SELECT platform_id, price, listing_url, flat_rating, house_rating, location_rating " +
                    "FROM estate.property_listings WHERE group_id = ?";
            List<Map<String, Object>> listings = jdbcTemplate.queryForList(listingsQuery, groupId);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("group_id", groupId);
            result.put("listings", listings);

            return result;
        } catch (NumberFormatException e) {
            return Map.of("error", "Invalid listing ID format");
        } catch (EmptyResultDataAccessException e) {
            return Map.of("error", "Listing not found");
        }
    }

}
