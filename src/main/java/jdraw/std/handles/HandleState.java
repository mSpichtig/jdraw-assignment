package jdraw.std.handles;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import jdraw.framework.DrawView;
import jdraw.framework.Figure;

public interface HandleState {

    Figure getOwner();
	
	Point getLocation();
	
	Cursor getCursor();
	
	void dragInteraction(int x, int y, MouseEvent e, DrawView v);
	
	HandleState swapHorizontal();
	HandleState swapVertical();
}
