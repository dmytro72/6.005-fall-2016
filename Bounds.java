/* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package geo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Methods for computing with latitude-longitude bounding rectangles.
 * 
 * <p>
 * A latitude-longitude bounding rectangle is represented by a four-element
 * {@code List<Angle>}, where every angle is valid for measuring its coordinate:
 * <ul>
 * <li>list[0] is the northern boundary latitude,
 * <li>list[1] is the eastern boundary longitude,
 * <li>list[2] is the southern boundary latitude, and
 * <li>list[3] is the western boundary longitude.
 * </ul>
 * Note: "northern," for example, does not mean "north latitude;" the northern
 * boundary might still be a south latitude, and similarly for other boundaries.
 * <p>
 * The bounding rectangle contains a point (lat, long) if:
 * <ul>
 * <li>sweeping north from the southern to northern bound goes through lat, and
 * <li>sweeping east from the western to eastern bound goes through long.
 * </ul>
 * Sweeping east may cross the antimeridian, and a bounding rectangle may span
 * the antimeridian. A bounding rectangle includes the points on its boundaries.
 * 
 * <p>
 * PS1 instructions: do NOT change the method signatures or specifications of
 * these methods, but you should implement their method bodies, and you may add
 * new public or private methods or classes if you like.
 */

public class Bounds {

    // Helper function for sorting

    /**
     * 
     * @param list of Angle objects
     *     
     * @return sorted list of Angle objects in increasing order (using toDegree
     *         method)
     */
    public static List<Angle> sortingList(List<Angle> listOfAngles) {
        Collections.sort(listOfAngles, new Comparator<Angle>() {
            @Override
            public int compare(Angle sortedAngle, Angle angle) {
                double sortedAngleToDegrees = Angular.toDegrees(sortedAngle);
                double angleToDegrees = Angular.toDegrees(angle);
                if (sortedAngleToDegrees - angleToDegrees > 0.0) {
                    return 1;
                } else if (sortedAngleToDegrees == angleToDegrees) {
                    return 0;
                } else {
                    return -1;
                }

            }

        });

        return listOfAngles;
    }

    /**
     * Find latitude-longitude bounds for a set of points of interest (POIs).
     * Returns a smallest bounding rectangle, for example like this: <br>
     * <img src="doc-files/bounds.svg"><br>
     * Not this: <br>
     * <img src="doc-files/bounds-wrap.svg"><br>
     * unless POIs on the far side of the globe require wrapping around.
     * 
     * @param pointsOfInterest
     *            set of POIs, not modified by this method
     * @return a smallest latitude-longitude bounding rectangle, as defined in
     *         the documentation for this class, containing every POI in the
     *         input
     */
 
    public static List<Angle> boundingBox(Set<PointOfInterest> pointsOfInterest) {
        List<Angle> boundingList = new ArrayList<Angle>();
        List<Angle> listOfLatitudes = new ArrayList<Angle>();
        List<Angle> listOfLongitudes = new ArrayList<Angle>();
        double biggestGap = 0.0;
        double fullRotation = 360.0;
        // in case the set of PointOfInterest is empty
        if (pointsOfInterest.isEmpty()) {
            Angle startN = new Angle(0, 0, 0, CardinalDirection.NORTH);
            Angle startE = new Angle(0, 0, 0, CardinalDirection.WEST);
            List<Angle> resultList = Arrays.asList(startN, startE, startN, startE);
            return resultList;

        }
        // Sorting by latitude angles
        for (PointOfInterest point : pointsOfInterest) {
            listOfLatitudes.add(point.latitude());
        }
        List<Angle> sortedLatitude = sortingList(listOfLatitudes);
        // Sorting by longitude angles
        for (PointOfInterest point : pointsOfInterest) {
            listOfLongitudes.add(point.longitude());
        }
        List<Angle> sortedListLongitudes = sortingList(listOfLongitudes);
        int index = 0;
        int bestIndex = 0;
        int nextBestIndex = bestIndex + 1;

        for (index = 0; index < sortedListLongitudes.size(); ++index) {
            int nextIndex = index + 1;

            if (nextIndex == sortedListLongitudes.size()) {
                nextIndex = 0;
            }
            double temp = Angular.toDegrees(listOfLongitudes.get(nextIndex))
                    - Angular.toDegrees(listOfLongitudes.get(index));

            if (temp < 0) {
                temp = temp + fullRotation;
            }
            if (temp > biggestGap) {
                biggestGap = temp;
                bestIndex = index;
                nextBestIndex = bestIndex + 1;
            }
        }

        if (bestIndex == sortedListLongitudes.size() - 1) {
            nextBestIndex = 0;
        }
        if (bestIndex == sortedListLongitudes.size() - 1 && nextBestIndex == 0) {
            boundingList.add(sortedLatitude.get(sortedLatitude.size() - 1));
            boundingList.add(sortedListLongitudes.get(bestIndex));
            boundingList.add(sortedLatitude.get(0));
            boundingList.add(sortedListLongitudes.get(nextBestIndex));
        }

        else {
            boundingList.add(sortedLatitude.get(sortedLatitude.size() - 1));
            boundingList.add(sortedListLongitudes.get(bestIndex));
            boundingList.add(sortedLatitude.get(0));
            boundingList.add(sortedListLongitudes.get(nextBestIndex));
        }
        return boundingList;

    }

    
    
    
    // helper function for finding similar point objects from set of objects for given object
    
    /**
     * Finding an object which has same angle as the input from the list of objects for any given input
     * @param set of point objects
     * @param point object
     * @param set of point objects have all unique point objects
     * @return true if point object is inside of point objects , false otherwise
     */
    public static boolean hasPoint(Set<PointOfInterest> pointsOfInterest, PointOfInterest point){
        for (PointOfInterest pointObject : pointsOfInterest){
            if (pointObject.latitude().equals(point.latitude()) && pointObject.longitude().equals(point.longitude())){
                return true;
            }
            
        }return false;
    }
    /**
     * Find points of interest (POIs) in a latitude-longitude bounding
     * rectangle.
     * 
     * @param pointsOfInterest
     *            set of POIs, not modified by this method
     * @param bounds
     *            a latitude-longitude bounding rectangle as defined in the
     *            documentation for this class, not modified by this method
     * @return all and only the POIs in the input that are contained in the
     *         given bounding rectangle
     */
    public static Set<PointOfInterest> inBoundingBox(Set<PointOfInterest> pointsOfInterest, List<Angle> bounds) {
        Set<PointOfInterest> inBoundingBox = new HashSet<>();
        double mostNorthern = Angular.toDegrees(bounds.get(0));
        double mostEastern = Angular.toDegrees(bounds.get(1));
        double mostSouthern = Angular.toDegrees(bounds.get(2));
        double mostWestern = Angular.toDegrees(bounds.get(3));
        final int maxAngle = 180;
        // considering edge case where 180E = 180W
        Angle pointLongitude = new Angle(0, 0, 0, CardinalDirection.NORTH);
        Angle pointLatitudeEast = new Angle(maxAngle, 0, 0, CardinalDirection.EAST);
        Angle pointLatitudeWest = new Angle(maxAngle, 0, 0, CardinalDirection.WEST);

        PointOfInterest pointEast = new PointOfInterest(pointLongitude, pointLatitudeEast, "pointEast", "");
        PointOfInterest pointWest = new PointOfInterest(pointLongitude, pointLatitudeWest, "pointWest", "");

        for (PointOfInterest point : pointsOfInterest) {
            double pointLatitudeToDegrees = Angular.toDegrees(point.latitude());
            double pointLongitudeToDegrees = Angular.toDegrees(point.longitude());
            if (pointLatitudeToDegrees <= mostNorthern && pointLatitudeToDegrees >= mostSouthern) {
                if (mostEastern < mostWestern) {
                    if (pointLongitudeToDegrees <= mostEastern || pointLongitudeToDegrees >= mostWestern) {
                        inBoundingBox.add(point);
                    }
                    if (inBoundingBox.contains(pointWest) && hasPoint(pointsOfInterest, pointEast)) {
                        inBoundingBox.add(pointEast);
                    }
                } else {
                    if (pointLongitudeToDegrees <= mostEastern && pointLongitudeToDegrees >= mostWestern) {
                        inBoundingBox.add(point);
                    }
                }
            }
        }

        return inBoundingBox;
    }

}
