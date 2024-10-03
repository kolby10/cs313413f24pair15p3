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
        this.canvas = canvas;
        this.paint = paint;
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        paint.setColor(c.getColor());
        paint.setStyle(Style.FILL_AND_STROKE);
        c.getShape().accept(this);
        paint.setColor(0);
        paint.setStyle(Style.STROKE);
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        paint.setStyle(Style.FILL);
        f.getShape().accept(this);
        paint.setStyle(Style.STROKE);
        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        for (Shape shape : g.getShapes()) {
            shape.accept(this);
        }
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        canvas.translate(l.getX(), l.getY());
        l.getShape().accept(this);
        canvas.translate(-l.getX(), -l.getY());
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onOutline(Outline o) {
        paint.setStyle(Style.STROKE);
        o.getShape().accept(this);
        paint.setStyle(Style.FILL); 
        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {
        final float[] points = new float[s.getPoints().size() * 4];
        int i = 0;
        for (Point p : s.getPoints()) {
            points[i++] = p.getX();
            points[i++] = p.getY();
            points[i++] = p.getX();
            points[i++] = p.getY();
        }
        canvas.drawLines(points, paint);
        return null;
    }
}
