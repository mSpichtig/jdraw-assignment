package jdraw.figures;


import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import jdraw.framework.Figure;
import jdraw.framework.FigureEvent;
import jdraw.framework.FigureListener;


public abstract class AbstractFigure implements Figure {

    /** Use the java.awt.Rectangle in order to save/reuse code. */
	private final Rectangle rectangle;
	
	/** list of listeners. */
	private final List<FigureListener> listeners = new CopyOnWriteArrayList<>();

	/**
	 * Create a new rectangle of the given dimension.
	 * @param x the x-coordinate of the upper left corner of the rectangle
	 * @param y the y-coordinate of the upper left corner of the rectangle
	 * @param w the rectangle's width
	 * @param h the rectangle's height
	 */
	public AbstractFigure(int x, int y, int w, int h) {
		rectangle = new Rectangle(x, y, w, h);
	}

	


	

	/**
	 * Draw the rectangle to the given graphics context.
	 * @param g the graphics context to use for drawing.
	 */
	@Override
    public abstract void draw(Graphics g);
	// XXX die aus dem Interface geerbten Methoden müssen nicht wiederholt werden, ist eher unüblich.
    	
	@Override
	public void setBounds(Point origin, Point corner) {//Line anders
		rectangle.setFrameFromDiagonal(origin, corner);
		propagateFigureEvent();
	}

	@Override
	public void move(int dx, int dy) {// alle gleich
		if (dx != 0 || dy != 0) { // notification only if there is a change
			rectangle.setLocation(rectangle.x + dx, rectangle.y + dy);
			propagateFigureEvent();
		}
	}

	@Override
	public boolean contains(int x, int y) {// Oval anders
		return rectangle.contains(x, y);
	}

	@Override
	public Rectangle getBounds() {// alle gleich
		return rectangle.getBounds();
	}

	@Override
	public void addFigureListener(FigureListener listener) {
		if (listener != null && !listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeFigureListener(FigureListener listener) {
		listeners.remove(listener);
	}

	@Override
	public Figure clone() {
		// XXX diese Implementierung gefällt mir nicht da sie null zurück gibt. Ok, von dieser Klasse kann es keine
		//     Instanzen geben, aber falls jemand in der Unterklasse super.clone() aufruft dann hat er verloren.
		//     Aber ich weiss, wenn Du die Methode weglässt, dann gibt es eine Fehlermeldung da die beiden von 
		//     Figure und Object geerbten clone-Methoden nicht kompatibel sind.
		//     Lösung: Definiere hier eine abstrakte clone Methode (und definiere so die Schnittstelle von clone).
		//     Ich habe unten angegeben wie das aussehen müsste.
		return null;	
	}
//	public abstract Figure clone() ;
	
	protected void propagateFigureEvent() {
		FigureEvent fe = new FigureEvent(this);
		for (FigureListener listener : listeners) {
			listener.figureChanged(fe);
		}
	}

	public Rectangle getRectangle() {
		return rectangle;
	}
    
}
