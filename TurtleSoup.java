/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package turtle;

import java.util.List;
import java.util.ArrayList;


public class TurtleSoup {
	static final double BASE_ANGLE = 180.0;
	static final double FULL_ROTATION_ANGLE = 360.0;
    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
		int numberOfSides = 4;
		double turningAngleInDegrees = 90.0;
		for (int i = 0; i < numberOfSides; i++) {
			turtle.forward(sideLength);
			turtle.turn(turningAngleInDegrees); // turn 90 degrees
		}
        
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
    	
    	return BASE_ANGLE*(sides -2)/ sides;
        
    }
    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
	public static int calculatePolygonSidesFromAngle(double angle) {
		return (int) (Math.round(FULL_ROTATION_ANGLE / (BASE_ANGLE - angle)));

	}

    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
	public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
		for (int i = 0; i < sides; i++) {
			turtle.forward(sideLength);
			turtle.turn(BASE_ANGLE - calculateRegularPolygonAngle(sides));

		}

	}
  /**
   * Given the number of sides, draw a regular polygon.
   * (0,0) is the lower-left corner of the polygon; use only left-hand turns to draw.
   * 
   * @param turtle the turtle context
   * @param sides number of sides of the polygon to draw
   * @param sideLength length of each side
   */
	public static void drawRegularPolygonReverse(Turtle turtle, int sides, int sideLength) {
		for (int i = sides; i > 0; i--) {
			turtle.forward(sideLength);
			turtle.turn(calculateRegularPolygonAngle(sides) - BASE_ANGLE);
		}
	}
  /**
   * Given the length, draw an edge
   * @param turtle the turtle context
   * @param length the length we want to extend
   * @param depth just to avoid from infinite loop
   */
	public static void drawEdge(Turtle turtle, double length, int depth) {
		if (depth == 5) {
			turtle.forward((int) (length / 3));

		} else {

			turtle.forward((int) (length / 3));
			turtle.turn(60);
			drawEdge(turtle, length / 3, depth + 1);
			turtle.turn(120);
			turtle.forward((int) (length / 3));
		}
	}
    /**
     * Given the current direction, current location, and a target location, calculate the heading
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentHeading. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentHeading current direction as clockwise from north
     * @param currentX current location x-coordinate
     * @param currentY current location y-coordinate
     * @param targetX target point x-coordinate
     * @param targetY target point y-coordinate
     * @return adjustment to heading (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateHeadingToPoint(double currentHeading, int currentX, int currentY,
                                                 int targetX, int targetY) {
    	
    	double deltaX = targetX - currentX;  // horizontal difference
    	double deltaY = targetY - currentY;  // vertical difference
    	double desiredAngle = Math.toDegrees(Math.atan2(deltaX,deltaY)); // converting it back to 0-180 scale
    	double angle =  desiredAngle - currentHeading; // the angle that has to be adjusted
    	if (angle < 0) {
    		return angle + FULL_ROTATION_ANGLE; // in case the angle is less than 0, we add 360 to make it positive
    	
    	}
    	
    	else {
    		return angle; // in normal case
    	}
    	
    		
        
    }

    /**
     * Given a sequence of points, calculate the heading adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateHeadingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of heading adjustments between points, of size 0 if (# of points) == 0,
     *         otherwise of size (# of points) - 1
     */
    public static List<Double> calculateHeadings(List<Integer> xCoords, List<Integer> yCoords) {
        List<Double> adjustments = new ArrayList<Double>();
        double currentHeading = 0.0;
        for(int i = 0; i< xCoords.size()-1; i++){
        	double relativeHeading = calculateHeadingToPoint(currentHeading, xCoords.get(i), yCoords.get(i),xCoords.get(i+1),yCoords.get(i+1));
        	currentHeading += relativeHeading;
        	adjustments.add(relativeHeading);
        }
        return adjustments;
    	
    }

    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * 
     * @param turtle the turtle context
     */
	public static void drawPersonalArt(Turtle turtle) {
		for (int i = 50; i > 0; i -= 10) {
			for (int j = 20; j > 0; j -= 10) {
				drawRegularPolygon(turtle, 10, j);
			}
			drawRegularPolygon(turtle, 10, i);
			turtle.forward(i);
			drawEdge(turtle, 10, 0);

		}
		turtle.forward(-150);
		for (int i = 50; i > 0; i -= 10) {
			for (int j = 20; j > 0; j -= 10) {
				drawRegularPolygonReverse(turtle, 10, j);
			}
			drawRegularPolygonReverse(turtle, 10, i);
			turtle.forward(i);
			drawEdge(turtle, 10, 0);
		}

	}
    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     * 
     * @param args unused
     */
    public static void main(String args[]) {
    	
		DrawableTurtle turtle = new DrawableTurtle();
		drawPersonalArt(turtle);
		// drawSquare(turtle, 40);
		// drawRegularPolygon(turtle, 5, 50);
		// draw the window

		turtle.draw();
        
        
    }

}
