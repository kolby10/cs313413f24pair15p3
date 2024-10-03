package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import java.util.List;
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
        int originalColor = paint.getColor();
        paint.setColor(c.getColor());
        c.getShape().accept(this);
        paint.setColor(originalColor);
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        Paint.Style originalStyle = paint.getStyle();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        f.getShape().accept(this);
        paint.setStyle(originalStyle);
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
    public Void onOutline(final Outline o) {
        Paint.Style originalStyle = paint.getStyle();
        paint.setStyle(Paint.Style.STROKE);
        o.getShape().accept(this);
        paint.setStyle(originalStyle);
        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {
        final List<? extends Point> points = s.getPoints();
        final float[] lines = new float[points.size() * 4];
        int i = 0;
        for (int j = 0; j < points.size(); j++) {
            Point p1 = points.get(j);
            Point p2 = points.get((j + 1) % points.size());
            lines[i++] = p1.getX();
            lines[i++] = p1.getY();
            lines[i++] = p2.getX();
            lines[i++] = p2.getY();
        }
        canvas.drawLines(lines, paint);
        return null;
    }
}
