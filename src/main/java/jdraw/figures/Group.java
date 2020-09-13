package jdraw.figures;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import jdraw.framework.Figure;
import jdraw.framework.FigureListener;

public class Group implements Figure, jdraw.framework.FigureGroup {

    List<Figure> parts;

    /** list of listeners. */
    private final List<FigureListener> listeners = new CopyOnWriteArrayList<>();

    public Group(List<Figure> parts) {
        this.parts = parts;
    }

    @Override
    public void draw(Graphics g) {
        // TOD Auto-generated method stub
        for (Figure f : parts) {
            f.draw(g);
        }

    }

    @Override
    public void move(int dx, int dy) {
        // TOD Auto-generated method stub
        for (Figure f : parts) {
            f.move(dx, dy);
        }

    }

    @Override
    public boolean contains(int x, int y) {
        // TOD Auto-generated method stub
        boolean c = false;
        for (Figure f : parts) {
            if (f.contains(x, y))
                c = true;
        }
        return c;
    }

    @Override
    public void setBounds(Point origin, Point corner) {
        // TOD Auto-generated method stub

    }

    @Override
    public Rectangle getBounds() {
        // TOD Auto-generated method stub
        Rectangle rect = parts.get(0).getBounds();
        for (int i = 1; i < parts.size(); i++) {
            rect.add(parts.get(i).getBounds());
        }
        return rect;
    }

    @Override
    public void addFigureListener(FigureListener listener) {
        // TOO Auto-generated method stub
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeFigureListener(FigureListener listener) {
        // TOO Auto-generated method stub
        listeners.remove(listener);
    }

    @Override
    public Figure clone() {
        // TOD Auto-generated method stub
        return null;
    }

    @Override
    public Stream<Figure> getFigureParts() {
        // TOD Auto-generated method stub
        return parts.stream();
    }

    
}
