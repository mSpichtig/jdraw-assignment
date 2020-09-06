/*
 * Copyright (c) 2018 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package jdraw.std;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import jdraw.framework.DrawCommandHandler;
import jdraw.framework.DrawModel;
import jdraw.framework.DrawModelEvent;
import jdraw.framework.DrawModelListener;
import jdraw.framework.Figure;
import jdraw.framework.FigureEvent;
import jdraw.framework.FigureListener;
import jdraw.framework.DrawModelEvent.Type;

/**
 * Provide a standard behavior for the drawing model. This class initially does
 * not implement the methods in a proper way. It is part of the course
 * assignments to do so.
 *
 */
public class StdDrawModel implements DrawModel, FigureListener {

	private final List<Figure> figures = new LinkedList<>();
	private final List<DrawModelListener> listeners = new CopyOnWriteArrayList<>();

	@Override
	public void addFigure(Figure f) {
		assert f != null;
		if (!figures.contains(f)) {
			figures.add(f);
			f.addFigureListener(this);
			notifyListeners(f, Type.FIGURE_ADDED);
		}
	}

	@Override
	public void removeFigure(Figure f) {
		if (figures.remove(f)) {
			f.removeFigureListener(this);
			notifyListeners(f, Type.FIGURE_REMOVED);
		}
	}

	@Override
	public void removeAllFigures() {
		for (Figure f : figures)
			f.removeFigureListener(this);
		figures.clear();
		notifyListeners(null, Type.DRAWING_CLEARED);
	}

	@Override
	public Stream<Figure> getFigures() {
		return figures.stream();
	}

	@Override
	public void addModelChangeListener(DrawModelListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeModelChangeListener(DrawModelListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void figureChanged(FigureEvent e) {
		notifyListeners(e.getFigure(), Type.FIGURE_CHANGED);
	}

	private void notifyListeners(Figure figure, Type type) {
		DrawModelEvent e = new DrawModelEvent(this, figure, type);
		for (DrawModelListener l : listeners) {
			l.modelChanged(e);
		}
	}

	/** The draw command handler. Initialized here with a dummy implementation. */
	private DrawCommandHandler handler = new EmptyDrawCommandHandler();

	/**
	 * Retrieve the draw command handler in use.
	 * 
	 * @return the draw command handler.
	 */
	@Override
	public DrawCommandHandler getDrawCommandHandler() {
		return handler;
	}

	@Override
	public void setFigureIndex(Figure f, int index) {
		if (index < 0 || index >= figures.size())
			throw new IndexOutOfBoundsException();
		int i = figures.indexOf(f);
		if (i < 0)
			throw new IllegalArgumentException();
		if (i != index) {
			figures.remove(f);
			figures.add(index, f);
			notifyListeners(f, Type.DRAWING_CHANGED);
		}
	}

}

