package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    // TODO entirely your job (except onCircle) **DONE -k

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
        canvas.translate(-200.0f, -100.0f);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        // changes paint color while drawing inner shape, then reverts to original color
        paint.setColor(c.getColor());
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
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
        // draws rectangle at (0, 0), getWidth/getHeight adjusts based on requirements
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        canvas.translate(-70.0f, -30.0f);
        return null;
    }

    @Override
    public Void onOutline(Outline o) {
        // draws outline based on the inner shape
        Style originalStyle = paint.getStyle(); // saves current paint style
        paint.setStyle(Style.STROKE); // sets paint style to stroke, for the outline
        o.getShape().accept(this); // draws inner shape with an outline
        paint.setStyle(originalStyle); // instates the original paint style
        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {
        // creates a list of lines that results in connecting vertices
        final float[] pts = new float[s.getPoints().size() * 4]; // 4 values per line
        int i = 0;
        for (int j = 0; j < s.getPoints().size(); j++) {
            Point p1 = s.getPoints().get(j);
            Point p2 = s.getPoints().get((j + 1) % s.getPoints().size()); // connects 1st and last vertices
            pts[i++] = p1.getX();
            pts[i++] = p1.getY();
            pts[i++] = p2.getX();
            pts[i++] = p2.getY();
        }

        canvas.drawLines(pts, paint); // draws the polygon lines
        return null;
    }
}
