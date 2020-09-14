/*
 * Copyright (c) 2018 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package jdraw.figures;

import java.awt.Color;
import java.awt.Graphics;
/*import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import jdraw.framework.Figure;
import jdraw.framework.FigureEvent;
import jdraw.framework.FigureListener;*/
import java.awt.Rectangle;



/**
 * Represents rectangles in JDraw.
 * 
 * @author Christoph Denzler
 *
 */
public class Rect extends AbstractFigure/* implements Figure */ {

	/**
	 * Create a new rectangle of the given dimension.
	 * 
	 * @param x the x-coordinate of the upper left corner of the rectangle
	 * @param y the y-coordinate of the upper left corner of the rectangle
	 * @param w the rectangle's width
	 * @param h the rectangle's height
	 */
	public Rect(int x, int y, int w, int h) {
		super(x, y, w, h);
		// rectangle = new Rectangle(x, y, w, h);
	}

	/**
	 * Draw the rectangle to the given graphics context.
	 * 
	 * @param g the graphics context to use for drawing.
	 */
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		Rectangle rectangle = getRectangle();
		g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		g.setColor(Color.BLACK);
		g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		g.drawLine(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height);
	}

	@Override
	public Rect clone() {
		Rectangle r = super.getRectangle();
		return new Rect((int) r.getX(),(int) r.getY(),(int)r.getWidth(),(int)r.getHeight());
	}



}
