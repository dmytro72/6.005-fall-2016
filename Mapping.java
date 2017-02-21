/* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package geo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Methods for building useful maps of points of interest.
 * 
 * <p>
 * PS1 instructions: do NOT change the method signatures or specifications of
 * these methods, but you should implement their method bodies, and you may add
 * new public or private methods or classes if you like.
 */
public class Mapping {

    /**
     * Find categories applicable to a point of interest (POI) by searching
     * their names and descriptions.
     * <p>
     * Category names and keywords must be nonempty strings of letters (A-Z,
     * a-z), digits (0-9), underscores ("_"), hyphens ("-"), or spaces (" ").
     * <p>
     * String comparisons with them are case-insensitive. For example, the
     * category name or keyword "supermarket" is the same as "SuperMarket", and
     * "Rob's SUPERmarket" contains it. A name or keyword should appear at most
     * once as an element in a set, or as a key in a map; it may not appear
     * multiple times with different case.
     * 
     * @param pointOfInterest
     *            POI to categorize
     * @param categoryKeywords
     *            a map, not modified by this method, that maps category names
     *            to a set of keywords for that category
     * @return all and only the category names in categoryKeywords where the
     *         name or description of the POI contains the category name or at
     *         least one of its keywords as a substring
     */
    public static Set<String> findCategories(PointOfInterest pointOfInterest,Map<String, Set<String>> categoryKeywords) {
           
        Set<String> validMap = new HashSet<String>();
        
        for (Map.Entry<String, Set<String>> category : categoryKeywords.entrySet()) {
            // checking whether name of object contains key of the category
            if (pointOfInterest.name().toLowerCase().contains(category.getKey().toLowerCase())) {
                validMap.add(category.getKey());
              //checking whether description contains key of the category
            } else if (pointOfInterest.description().toLowerCase().contains(category.getKey().toLowerCase())) {
                validMap.add(category.getKey());
            }
            for (String value : category.getValue()) {
                // checking whether name of object contains values of categories
                if (pointOfInterest.name().toLowerCase().contains(value.toLowerCase())) {
                    validMap.add(category.getKey());
                  // checking whether description contains values of categories
                } else if (pointOfInterest.description().toLowerCase().contains(value.toLowerCase())) {
                    validMap.add(category.getKey());

                }

            }
        }

        return validMap;
    }

    // Helper function to see whether two points are similar
    public static boolean areSimilar(PointOfInterest point1, PointOfInterest point2) {
        if (point1.latitude().equals(point2.latitude()) && point1.longitude().equals(point2.longitude())
                && point1.name().equals(point2.name())) {
            return true;

        }
        return false;
    }

    /**
     * Guess which points of interest (POIs) are duplicates that represent the
     * same physical place.
     * <p>
     * This method identifies groups of POIs in the input list that appear to be
     * duplicative, and picks a best entry from the group. That entry is a key
     * in the returned map, and its value is a list of the other duplicates in
     * the group.
     * <p>
     * POIs are identified as duplicates if and only if their location, name,
     * and/or description provide evidence that they represent the same physical
     * place. POIs with exactly the same latitude, longitude, and name must be
     * identified as duplicates. Other evidence may be used at the implementor's
     * discretion.
     * 
     * @param pointsOfInterest
     *            a list of POIs, not modified by this method
     * @return a map in which all and only the POIs in pointsOfInterest appear
     *         exactly once as either a key or in a value list, and the value
     *         for each key is the list of POIs that are identified as its
     *         duplicates, as described above
     */

    public static Map<PointOfInterest, List<PointOfInterest>> reduceDuplicates(List<PointOfInterest> pointsOfInterest) {
        Map<PointOfInterest, List<PointOfInterest>> nonDuplicates = new HashMap<PointOfInterest, List<PointOfInterest>>();

        // Split into partitions of duplicates. Partitions are disjoint,
        // every point is in one part of the partition.
        List<List<PointOfInterest>> partitions = new ArrayList<>();
        for (PointOfInterest point : pointsOfInterest) {
            boolean found = false;
            for (List<PointOfInterest> partition : partitions) {
                if (areSimilar(point, partition.get(0))) {
                    partition.add(point);
                    found = true;
                }
            }
            if (!found) {
                List<PointOfInterest> newPartition = new ArrayList<>();
                newPartition.add(point);
                partitions.add(newPartition);
            }
        }

        // Pick the best point from a partition and compute nonDuplicates
        for (List<PointOfInterest> partition : partitions) {

            // Find the best point by longest description
            PointOfInterest pointLongestDesc = partition.get(0);
            for (PointOfInterest point : partition) {
                if (point.description().length() >= pointLongestDesc.description().length()) {
                    pointLongestDesc = point;
                }
            }
            // remove the key itself from the list of values
            boolean removed = partition.remove(pointLongestDesc);
            assert (removed);
            nonDuplicates.put(pointLongestDesc, partition);
        }

        return nonDuplicates;
    }

}
