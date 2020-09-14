/*
 * Copyright (c) 2018 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package jdraw.figures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import jdraw.framework.Figure;



/*import java.awt.Rectangle;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import jdraw.framework.Figure;
import jdraw.framework.FigureEvent;
import jdraw.framework.FigureListener;
*/
/**
 * Represents rectangles in JDraw.
 * 
 * @author Christoph Denzler
 *
 */
public class Line extends AbstractFigure/* implements Figure */ {

	/** Use the java.awt.Rectangle in order to save/reuse code. */
	// private final Rectangle rectangle;

	private Point or = new Point(0, 0);
	private Point end = new Point(0, 0);

	/** list of listeners. */
	// private final List<FigureListener> listeners = new CopyOnWriteArrayList<>();

	/**
	 * Create a new rectangle of the given dimension.
	 * 
	 * @param x the x-coordinate of the upper left corner of the rectangle
	 * @param y the y-coordinate of the upper left corner of the rectangle
	 * @param w the rectangle's width
	 * @param h the rectangle's height
	 */
	public Line(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	/**
	 * Draw the rectangle to the given graphics context.
	 * 
	 * @param g the graphics context to use for drawing.
	 */
	@Override
	public void draw(Graphics g) {

		g.setColor(Color.BLUE);
		g.setColor(Color.RED);
		g.drawLine(or.x, or.y, end.x, end.y);

	}

	@Override
	public void setBounds(Point origin, Point corner) {// einzige mit Unterschied
		super.setBounds(origin, corner);
		or = origin;
		end = corner;
	}

	@Override
	public void move(int dx, int dy) {// hier anders gleich
		super.move(dx, dy);

		if (dx != 0 || dy != 0) {
			or.x += dx;
			or.y += dy;
			end.x += dx;
			end.y += dy;
		}
	}

	@Override
	public List<Handle> getHandles() {
		List<Handle> list = new ArrayList<Handle>();
		if (or.x <= end.x && or.y <= end.y || or.x > end.x && or.y > end.y) {
			list.add(Figure.Handle.NE);
			list.add(Figure.Handle.SW);
		}
		else {
			list.add(Figure.Handle.SE);
			list.add(Figure.Handle.NW);
		}
		return list;//List.of(Handle.values());
	}

	@Override
	public Line clone() {
		Rectangle r = super.getRectangle();
		return new Line((int) r.getX(),(int) r.getY(),(int)r.getWidth(),(int)r.getHeight());
	}




}
