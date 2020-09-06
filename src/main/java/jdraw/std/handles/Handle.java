package jdraw.std.handles;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import jdraw.framework.DrawView;
import jdraw.framework.Figure;
import jdraw.framework.FigureHandle;
import jdraw.std.commands.SetBoundsCommand;

public class Handle implements FigureHandle {

	private HandleState state;
	private final Color color;
	private static final int HANDLE_SIZE = 6;

	public Handle(HandleState state, Color color) {
		this.state = state;
		this.color = color;
	}
	
	public void setState(HandleState state) {
		this.state = state;
	}
	
	public HandleState getState() {
		return state;
	}
	
	@Override
	public void draw(Graphics g) {
		Point p = state.getLocation();
		g.setColor(color);
		g.fillRect(p.x-HANDLE_SIZE/2, p.y-HANDLE_SIZE/2, HANDLE_SIZE, HANDLE_SIZE);
		g.setColor(Color.BLACK);
		g.drawRect(p.x-HANDLE_SIZE/2, p.y-HANDLE_SIZE/2, HANDLE_SIZE, HANDLE_SIZE);
	}
	
	@Override
	public boolean contains(int x, int y) {
		Point p = state.getLocation();
		Rectangle r = new Rectangle(p.x-HANDLE_SIZE/2, p.y-HANDLE_SIZE/2, HANDLE_SIZE, HANDLE_SIZE);
		return r.contains(x,y);
	}

	@Override
	public Point getLocation() {
		return state.getLocation();
	}

	@Override
	public Cursor getCursor() {
		return state.getCursor();
	}

	@Override
	public Figure getOwner() {
		return state.getOwner();
	}
	
	private Point origin, corner;
	
	@Override
	public void startInteraction(int x, int y, MouseEvent e, DrawView v) {
		Rectangle r = state.getOwner().getBounds();
		origin = r.getLocation();
		corner = r.getLocation();
		corner.translate(r.width, r.height);
	}

	@Override
	public void dragInteraction(int x, int y, MouseEvent e, DrawView v) {
		state.dragInteraction(x, y, e, v);	
	}
	
	@Override
	public void stopInteraction(int x, int y, MouseEvent e, DrawView v) {
		Rectangle r = state.getOwner().getBounds();
		Point origin = r.getLocation();
		Point corner = r.getLocation();
		corner.translate(r.width, r.height);
		// This implementation does not revert the states in the handles, i.e. colors are not reverted
		v.getModel().getDrawCommandHandler().addCommand(
				new SetBoundsCommand(state.getOwner(), this.origin, this.corner, origin, corner));
		this.corner = null;
		this.origin = null;
	}

}
