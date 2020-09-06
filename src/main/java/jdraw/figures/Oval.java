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
public class Oval extends AbstractFigure/*implements Figure*/ {

	/** Use the java.awt.Rectangle in order to save/reuse code. */
	//private final Rectangle rectangle;
	
	/** list of listeners. */
	//private final List<FigureListener> listeners = new CopyOnWriteArrayList<>();

	/**
	 * Create a new rectangle of the given dimension.
	 * @param x the x-coordinate of the upper left corner of the rectangle
	 * @param y the y-coordinate of the upper left corner of the rectangle
	 * @param w the rectangle's width
	 * @param h the rectangle's height
	 */
	public Oval(int x, int y, int w, int h) {
		super(x,y,w,h);
		//rectangle = new Rectangle(x, y, w, h);
	}

	/**
	 * Draw the rectangle to the given graphics context.
	 * @param g the graphics context to use for drawing.
	 */
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		Rectangle rect = getRectangle();
		g.fillOval(rect.x, rect.y, rect.width, rect.height);
		g.setColor(Color.RED);
		g.drawOval(rect.x, rect.y, rect.width, rect.height);
	}


	@Override
	public boolean contains(int x, int y) {
        double a = getRectangle().width*0.5;//rectangle.width*0.5;
        double b = getRectangle().height*0.5;//rectangle.height*0.5;
        double xx = x - (getRectangle().x/*rectangle.x*/ + a);
        double yy = y - (getRectangle().y/*rectangle.y*/ + b);
		return xx*xx/(a*a)+yy*yy/(b*b) <= 1;//rectangle.contains(x, y);
	}


}
