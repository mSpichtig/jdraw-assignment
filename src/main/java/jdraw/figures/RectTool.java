/*
 * Copyright (c) 2018 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package jdraw.figures;

import jdraw.framework.DrawContext;

/**
 * This tool defines a mode for drawing rectangles.
 *
 * @see jdraw.framework.Figure
 *
 * @author  Christoph Denzler
 */
public class RectTool extends AbstractDrawTool<Rect>/*implements DrawTool*/ {
  
	

	/**
	 * Create a new rectangle tool for the given context.
	 * @param context a context to use this tool in.
	 */
	public RectTool(DrawContext context) {
		//this.context = context;
		//this.view = context.getView();
		super(context);
		setType("Rectangle");
		
	}

	@Override
	public AbstractFigure createFigure(int x, int y, int w, int h) {
		// TOD Auto-generated method stub
		return new Rect(x, y, w, h);
	}

	

}
