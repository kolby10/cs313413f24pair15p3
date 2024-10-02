package edu.luc.etl.cs313.android.shapes.model;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    // TODO entirely your job (except onCircle) **DONE-K

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onFill(final Fill f) {
        // Sets the surrounding box calculation to fit the shape fill
        return f.getShape().accept(this);
    }

    @Override
    public Location onGroup(final Group g) {
        // calc bounding box of shape
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Shape shape : g.getShapes()) {
            Location loc = shape.accept(this); // gets location on bounding box for current shape
            Rectangle rect = (Rectangle) loc.getShape();
            minX = Math.min(minX, loc.getX()); //35-38 updates coordinates for current shape, based on bounding box
            minY = Math.min(minY, loc.getY());
            maxX = Math.max(maxX, loc.getX() + rect.getWidth());
            maxY = Math.max(maxY, loc.getY() + rect.getHeight());

        }

        return new Location(minX, minY, new Rectangle(maxX - minX, maxY-minY)); 
        // returns new location of the bounding box, represents the top-left corner and the size (maxX-minX, maxY-minY)
    }

    @Override
    public Location onLocation(final Location l) {
        // calcs inside shape of bounding box, then shifts that to a specific position
        Location innerLoc = l.getShape().accept(this); // gives Location representing bBox of inner shape
        return new Location ( // returns new location object w/ adjusted coordinates of Location and innerLoc
                l.getX() + innerLoc.getX(), // adding x/y coordinates to innerLoc's
                l.getY() + innerLoc.getY(),
               innerLoc.getShape()
        );
    }

    @Override
    public Location onRectangle(final Rectangle r) {
        //Sets rectangles bounding box to rectangle itself at (0, 0)
        return new Location(0, 0, r);
    }

    @Override
    public Location onStrokeColor(final StrokeColor c) {
        // specifies to Coloring in inner shape. keeps size/position of the shape the same
        return c.getShape().accept(this);
    }

    @Override
    public Location onOutline(final Outline o) {
        // keeps color to the shapes outline only, no change on shapes size/position
        return o.getShape().accept(this);
    }

    @Override
    public Location onPolygon(final Polygon s) {
        // holds polygon's min/max x/y points
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point p : s.getPoints()) { // loops through points to set max/min x/y of each point
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());

        }
        // returns the top left Location of polygon, and rectangle bounding box min/max x/y
        return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));
    }
}
