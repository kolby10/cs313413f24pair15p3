package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    // TODO entirely your job (except onCircle)

    private final Canvas canvas;

    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas; // stores presented canvas
        this.paint = paint; // stores presented paint
        paint.setStyle(Style.STROKE); // sets selected paint style
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        // changes paint color while drawing inner shape, then reverts to original color
        int originalColor = paint.getColor(); // stores current color
        paint.setColor(c.getColor()); // sets inner shape color
        c.getShape().accept(this); // draws inner shape
        paint.setColor(originalColor); // restores color
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        // changes paint color to the color needed to shape fill
        Style originalStyle = paint.getStyle(); // saves current color
        paint.setStyle(Style.FILL); // changes paint to FILL style
        f.getShape().accept(this); // draws inner shape
        paint.setStyle(originalStyle); // reverts to original color
        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        // loops through all shapes to draw them individually
        for (Shape shape : g.getShapes()) {
            shape.accept(this); // draws each shape
        }
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        // moves position to draw new shape, resets after
        canvas.save(); // saves current canvas
        canvas.translate(l.getX(), l.getY()); // moves canvas to new location
        l.getShape().accept(this); // draws the shape @ new location
        canvas.restore(); // reverts canvas to original location
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {

        return null;
    }

    @Override
    public Void onOutline(Outline o) {

        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {

        final float[] pts = null;

        canvas.drawLines(pts, paint);
        return null;
    }
}
