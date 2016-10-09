/* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package geo;

import java.util.ArrayList;
import java.util.List;

/**
 * Methods for computing with angles.
 * 
 * <p>
 * PS1 instructions: do NOT change the method signatures or specifications of
 * these methods, but you should implement their method bodies, and you may add
 * new public or private methods or classes if you like.
 */
public class Angular {

    /**
     * Convert a degree-minutes-seconds angle to signed floating-point degrees.
     * 
     * @param dmsAngle
     *            angle in degrees-minutes-seconds
     * @return degrees in dmsAngle, accurate to within 0.0001 degrees, where
     *         angles north & east are positive and south & west are negative
     */
    public static double toDegrees(Angle dmsAngle) {
        final double base = 60.0;
        final double baseSquared = 3600.0;
        if (dmsAngle.direction().equals(CardinalDirection.NORTH)
                || dmsAngle.direction().equals(CardinalDirection.EAST)) {
            return dmsAngle.degrees() + dmsAngle.minutes() / base + dmsAngle.seconds() / baseSquared;
        }
        return -(dmsAngle.degrees() + dmsAngle.minutes() / base + dmsAngle.seconds() / baseSquared);

    }

    /**
     * Convert a non - valid dms angle to valid dms (for example, when seconds <
     * 0, it is not valid input for seconds)
     *
     * @param list
     *            of degrees ,minutes, seconds
     * 
     * @return list of valid degrees, minutes, seconds ( all i in list > 0)
     * 
     * 
     * 
     */

    public static List<Integer> validConverter(int degrees, int minutes, int seconds) {
        List<Integer> validShifter = new ArrayList<>();
        final int baseOfAngle = 60;
        if (seconds % baseOfAngle == 0 && seconds < 0) {
            // correcting minutes because of minus seconds
            minutes -= (-seconds) / baseOfAngle;
            seconds = 0;
        } else if (seconds < 0) {
            // correcting minutes because of minus seconds
            minutes -= ((-seconds) / baseOfAngle + 1);
            // correcting seconds
            seconds = seconds + ((-seconds) / baseOfAngle + 1) * baseOfAngle;
        }
        if (minutes % baseOfAngle == 0 && minutes < 0) {
            // correcting degrees because of minus minutes
            degrees -= (-minutes) / baseOfAngle;
            minutes = 0;
        } else if (minutes < 0) {
            // correcting degrees because of minus minutes
            degrees -= ((-minutes) / baseOfAngle + 1);
            // correcting minutes
            minutes = minutes + ((-minutes) / baseOfAngle + 1) * baseOfAngle;
        }

        validShifter.add(degrees);
        validShifter.add(minutes);
        validShifter.add(seconds);

        return validShifter;

    }

    /**
     * Angular displacement from begin to end. Returns an angle with smallest
     * absolute value, for example like this: <br>
     * <img src="doc-files/displacement.svg"><br>
     * Not this: <br>
     * <img src="doc-files/displacement-invert.svg"><br>
     * 
     * @param begin
     *            starting angle, must be a valid latitude or longitude as
     *            defined in {@link Angle}
     * @param end
     *            ending angle, must be a valid angle measuring the same
     *            coordinate (latitude or longitude) as begin
     * @return angle with a smallest absolute value, measuring the same
     *         coordinate as the inputs, that sweeps from begin to end
     */

    public static Angle displacement(Angle begin, Angle end) {
        int displacedDegrees;
        int displacedMinutes;
        int displacedSeconds;
        final double baseAngle = 180.0;
        final int maxDegree = 359;
        final int maxMinutes = 59;
        final int maxSeconds = 60;
        CardinalDirection displacedDirection;
        Angle newAngle;

        if (begin.direction() != end.direction()) {
            displacedDegrees = begin.degrees() + end.degrees();
            displacedMinutes = begin.minutes() + end.minutes();
            displacedSeconds = begin.seconds() + end.seconds();
            displacedDirection = end.direction();

            newAngle = new Angle(displacedDegrees, displacedMinutes, displacedSeconds, displacedDirection);

            if (newAngle.degrees() > baseAngle) {

                displacedDirection = begin.direction();
                newAngle = new Angle(maxDegree - newAngle.degrees(), maxMinutes - newAngle.minutes(),
                        maxSeconds - newAngle.seconds(), displacedDirection);
                return newAngle;
            } else {
                return newAngle;
            }

        } else {
            if (Math.abs(toDegrees(begin)) >= Math.abs(toDegrees(end))) {

                displacedDegrees = begin.degrees() - end.degrees();
                displacedMinutes = begin.minutes() - end.minutes();
                displacedSeconds = begin.seconds() - end.seconds();
                displacedDirection = end.direction();
                List<Integer> correctDmsBeginGreater = validConverter(displacedDegrees, displacedMinutes,
                        displacedSeconds);
                if (displacedDirection == CardinalDirection.NORTH) {
                    displacedDirection = CardinalDirection.SOUTH;
                } else if (displacedDirection == CardinalDirection.SOUTH) {
                    displacedDirection = CardinalDirection.NORTH;
                }

                else if (displacedDirection == CardinalDirection.WEST) {
                    displacedDirection = CardinalDirection.EAST;
                } else if (displacedDirection == CardinalDirection.EAST) {
                    displacedDirection = CardinalDirection.WEST;
                }
                newAngle = new Angle(correctDmsBeginGreater.get(0), correctDmsBeginGreater.get(1),
                        correctDmsBeginGreater.get(2), displacedDirection);

                return newAngle;
            } else {
                displacedDegrees = end.degrees() - begin.degrees();
                displacedMinutes = end.minutes() - begin.minutes();
                displacedSeconds = end.seconds() - begin.seconds();
                displacedDirection = begin.direction();
                List<Integer> correctDmsEndGreater = validConverter(displacedDegrees, displacedMinutes,
                        displacedSeconds);
                newAngle = new Angle(correctDmsEndGreater.get(0), correctDmsEndGreater.get(1),
                        correctDmsEndGreater.get(2), displacedDirection);

                return newAngle;

            }

        }

    }

}
