/*
 * Copyright (c) 2000-2014 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved.
 */
package jdraw.std.handles;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import jdraw.framework.DrawView;
import jdraw.framework.Figure;
import jdraw.framework.FigureHandle;

public class DefaultHandleStates {
	
	private static Map<Figure, List<Handle>> handles = new WeakHashMap<>();
	
	public static List<Handle> getHandles(Figure figure) {
		if(handles.containsKey(figure)) return handles.get(figure);
		List<Handle> list = new ArrayList<>(8);
		List<jdraw.framework.Figure.Handle> res = figure.getHandles();
		if(res.contains(jdraw.framework.Figure.Handle.NW)) list.add(new Handle(new NW(figure), Color.WHITE));
		if(res.contains(jdraw.framework.Figure.Handle.NE)) list.add(new Handle(new NE(figure), Color.WHITE));
		if(res.contains(jdraw.framework.Figure.Handle.SW)) list.add(new Handle(new SW(figure), Color.WHITE));
		if(res.contains(jdraw.framework.Figure.Handle.SE)) list.add(new Handle(new SE(figure), Color.WHITE));
		if(res.contains(jdraw.framework.Figure.Handle.N)) list.add(new Handle(new N(figure), Color.WHITE));
		if(res.contains(jdraw.framework.Figure.Handle.S)) list.add(new Handle(new S(figure), Color.WHITE));
		if(res.contains(jdraw.framework.Figure.Handle.E)) list.add(new Handle(new E(figure), Color.WHITE));
		if(res.contains(jdraw.framework.Figure.Handle.W)) list.add(new Handle(new W(figure), Color.WHITE));
		handles.put(figure, list);
		return list;
	}
	
	private static void swapHorizontal(Figure owner) {
		for(FigureHandle fh : handles.get(owner)) {
			Handle h = (Handle)fh;
			h.setState(h.getState().swapHorizontal());
		}
	}
	
	private static void swapVertical(Figure owner) {
		for(FigureHandle fh : handles.get(owner)) {
			Handle h = (Handle)fh;
			h.setState(h.getState().swapVertical());
		}
	}

	public static abstract class AbstractHandleState implements HandleState {

		private final Figure owner;
		private final Cursor cursor;

		protected AbstractHandleState(Figure owner, Cursor cursor) {
			this.owner = owner;
			this.cursor = cursor;
		}

		@Override
		public final Figure getOwner() {
			return this.owner;
		}

		protected Rectangle getFigureBounds() {
			return owner.getBounds();
		}

		protected void setFigureBounds(Point p1, Point p2) {
			owner.setBounds(p1, p2);
		}

		@Override
		public final Cursor getCursor() {
			return cursor;
		}
	}

	public static class NE extends AbstractHandleState {

		public NE(Figure owner) {
			super(owner, Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
		}

		@Override
		public Point getLocation() {
			Rectangle r = getFigureBounds();
			return new Point(r.x + r.width, r.y);
		}

		@Override
		public void dragInteraction(int x, int y, MouseEvent e, DrawView v) {
			Rectangle r = getFigureBounds();

			if (r != null) {
				int x0 = r.x;
				int y0 = r.y + r.height;
				setFigureBounds(new Point(x0, y0), new Point(x, y));

				if (x < x0) {
					DefaultHandleStates.swapHorizontal(getOwner());
				}
				if (y > y0) {
					DefaultHandleStates.swapVertical(getOwner());
				}
			}
		}

		@Override
		public HandleState swapHorizontal() {
			return new NW(getOwner());
		}

		@Override
		public HandleState swapVertical() {
			return new SE(getOwner());
		}

	}

	public static class NW extends AbstractHandleState {

		public NW(Figure owner) {
			super(owner, Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
		}

		@Override
		public Point getLocation() {
			Rectangle r = getFigureBounds();
			return new Point(r.x, r.y);
		}

		@Override
		public void dragInteraction(int x, int y, MouseEvent e, DrawView v) {
			Rectangle r = getFigureBounds();

			if (r != null) {
				int x0 = r.x + r.width;
				int y0 = r.y + r.height;
				setFigureBounds(new Point(x0, y0), new Point(x, y));

				if (x > x0) {
					DefaultHandleStates.swapHorizontal(getOwner());
				}
				if (y > y0) {
					DefaultHandleStates.swapVertical(getOwner());
				}
			}
		}

		@Override
		public HandleState swapHorizontal() {
			return new NE(getOwner());
		}

		@Override
		public HandleState swapVertical() {
			return new SW(getOwner());
		}

	}

	public static class SE extends AbstractHandleState {

		public SE(Figure owner) {
			super(owner, Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
		}

		@Override
		public Point getLocation() {
			Rectangle r = getFigureBounds();
			return new Point(r.x + r.width, r.y + r.height);
		}

		@Override
		public void dragInteraction(int x, int y, MouseEvent e, DrawView v) {
			Rectangle r = getFigureBounds();

			if (r != null) {
				int x0 = r.x;
				int y0 = r.y;
				setFigureBounds(new Point(x0, y0), new Point(x, y));

				if (x < x0) {
					DefaultHandleStates.swapHorizontal(getOwner());
				}
				if (y < y0) {
					DefaultHandleStates.swapVertical(getOwner());
				}
			}
		}

		@Override
		public HandleState swapHorizontal() {
			return new SW(getOwner());
		}

		@Override
		public HandleState swapVertical() {
			return new NE(getOwner());
		}

	}

	public static class SW extends AbstractHandleState {

		public SW(Figure owner) {
			super(owner, Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
		}

		@Override
		public Point getLocation() {
			Rectangle r = getFigureBounds();
			return new Point(r.x, r.y + r.height);
		}

		@Override
		public void dragInteraction(int x, int y, MouseEvent e, DrawView v) {
			Rectangle r = getFigureBounds();

			if (r != null) {
				int x0 = r.x + r.width;
				int y0 = r.y;
				setFigureBounds(new Point(x0, y0), new Point(x, y));

				if (x > x0) {
					DefaultHandleStates.swapHorizontal(getOwner());
				}
				if (y < y0) {
					DefaultHandleStates.swapVertical(getOwner());
				}
			}
		}

		@Override
		public HandleState swapHorizontal() {
			return new SE(getOwner());
		}

		@Override
		public HandleState swapVertical() {
			return new NW(getOwner());
		}

	}

	public static class N extends AbstractHandleState {

		public N(Figure owner) {
			super(owner, Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
		}

		@Override
		public Point getLocation() {
			Rectangle r = getFigureBounds();
			return new Point(r.x + r.width / 2, r.y);
		}

		@Override
		public void dragInteraction(int x, int y, MouseEvent e, DrawView v) {
			Rectangle r = getFigureBounds();

			if (r != null) {
				setFigureBounds(new Point(r.x, y), new Point(r.x + r.width, r.y + r.height));

				if (y > r.y + r.height) {
					DefaultHandleStates.swapVertical(getOwner());
				}
			}
		}

		@Override
		public HandleState swapHorizontal() {
			return new N(getOwner());
		}

		@Override
		public HandleState swapVertical() {
			return new S(getOwner());
		}
	}

	public static class S extends AbstractHandleState {

		public S(Figure owner) {
			super(owner, Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
		}

		@Override
		public Point getLocation() {
			Rectangle r = getFigureBounds();
			return new Point(r.x + r.width / 2, r.y + r.height);
		}

		@Override
		public void dragInteraction(int x, int y, MouseEvent e, DrawView v) {
			Rectangle r = getFigureBounds();

			if (r != null) {
				setFigureBounds(new Point(r.x, r.y), new Point(r.x + r.width, y));

				if (y < r.y) {
					DefaultHandleStates.swapVertical(getOwner());
				}
			}
		}

		@Override
		public HandleState swapHorizontal() {
			return new S(getOwner());
		}

		@Override
		public HandleState swapVertical() {
			return new N(getOwner());
		}
	}

	public static class W extends AbstractHandleState {

		public W(Figure owner) {
			super(owner, Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
		}

		@Override
		public Point getLocation() {
			Rectangle r = getFigureBounds();
			return new Point(r.x, r.y + r.height / 2);
		}

		@Override
		public void dragInteraction(int x, int y, MouseEvent e, DrawView v) {
			Rectangle r = getFigureBounds();

			if (r != null) {
				setFigureBounds(new Point(x, r.y), new Point(r.x + r.width, r.y + r.height));

				if (x > r.x + r.width) {
					DefaultHandleStates.swapHorizontal(getOwner());
				}
			}
		}

		@Override
		public HandleState swapHorizontal() {
			return new E(getOwner());
		}

		@Override
		public HandleState swapVertical() {
			return new W(getOwner());
		}
	}

	public static class E extends AbstractHandleState {

		public E(Figure owner) {
			super(owner, Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
		}

		@Override
		public Point getLocation() {
			Rectangle r = getFigureBounds();
			return new Point(r.x + r.width, r.y + r.height / 2);
		}

		@Override
		public void dragInteraction(int x, int y, MouseEvent e, DrawView v) {
			Rectangle r = getFigureBounds();

			if (r != null) {
				setFigureBounds(new Point(r.x, r.y), new Point(x, r.y + r.height));

				if (x < r.x) {
					DefaultHandleStates.swapHorizontal(getOwner());
				}
			}
		}

		@Override
		public HandleState swapHorizontal() {
			return new W(getOwner());
		}

		@Override
		public HandleState swapVertical() {
			return new E(getOwner());
		}
	}

}
