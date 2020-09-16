/*
 * Copyright (c) 2018 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package jdraw.figures;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import jdraw.framework.DrawContext;
import jdraw.framework.DrawTool;
import jdraw.framework.DrawView;

/**
 * This tool defines a mode for drawing rectangles.
 *
 * @see jdraw.framework.Figure
 *
 * @author  Christoph Denzler
 */
public abstract class AbstractDrawTool<T extends AbstractFigure> implements DrawTool {
  
	/** 
	 * the image resource path. 
	 */
	private static final String IMAGES = "/images/";

	/**
	 * The context we use for drawing.
	 */
	private final DrawContext context;

	/**
	 * The context's view. This variable can be used as a shortcut, i.e.
	 * instead of calling context.getView().
	 */
	private final DrawView view;

	/**
	 * Temporary variable. During rectangle creation (during a
	 * mouse down - mouse drag - mouse up cycle) this variable refers
	 * to the new rectangle that is inserted.
	 */
    private AbstractFigure newRect = null;
    private String type; // XXX den Typ des Tools würde ich final setzen (und dann auch mit dem Konstruktor übergeben und nicht über eine Setter-Methode,
    				     //     denn es macht keinen Sinn beim Line-Tool den Typ-Namen plötzlich auf Rect zu ändern.

	/**
	 * Temporary variable.
	 * During rectangle creation this variable refers to the point the
	 * mouse was first pressed.
	 */
	private Point anchor = null;

	/**
	 * Create a new rectangle tool for the given context.
	 * @param context a context to use this tool in.
	 */
	public AbstractDrawTool(DrawContext context) {
		this.context = context;
		this.view = context.getView();
    }
    
    

	/**
	 * Deactivates the current mode by resetting the cursor
	 * and clearing the status bar.
	 * @see jdraw.framework.DrawTool#deactivate()
	 */
	@Override
	public void deactivate() {
		this.context.showStatusText("");
	}

	/**
	 * Activates the Rectangle Mode. There will be a
	 * specific menu added to the menu bar that provides settings for
	 * Rectangle attributes
	 */
	@Override
	public void activate() {
		this.context.showStatusText(type+" Mode");
	}

	/**
	 * Initializes a new Rectangle object by setting an anchor
	 * point where the mouse was pressed. A new Rectangle is then
	 * added to the model.
	 * @param x x-coordinate of mouse
	 * @param y y-coordinate of mouse
	 * @param e event containing additional information about which keys were pressed.
	 * 
	 * @see jdraw.framework.DrawTool#mouseDown(int, int, MouseEvent)
	 */
	@Override
	public void mouseDown(int x, int y, MouseEvent e) {
		if (getNewRect() != null) {
			throw new IllegalStateException();
		}
		setAnchor( new Point(x, y));
		AbstractFigure a;
		/*switch(type) {
			case "Rect":
			a = new Rect(x,y,0,0);break;
			case "Line":
			a = new Line(x,y,0,0);break;
			case "Oval":
			a = new Oval(x,y,0,0);break;
			default:
			a = new Rect(x,y,0,0);
		}	*/
		a = createFigure(x,y,0,0);		
		//T a =  new T(x,y,0,0);
		setNewRect(a);
		addFigure(a);
	}

	public abstract AbstractFigure createFigure(int x,int y,int w,int h);
    
    protected void addFigure(AbstractFigure nR){
        view.getModel().addFigure(nR);
    }

	/**
	 * During a mouse drag, the Rectangle will be resized according to the mouse
	 * position. The status bar shows the current size.
	 * 
	 * @param x   x-coordinate of mouse
	 * @param y   y-coordinate of mouse
	 * @param e   event containing additional information about which keys were
	 *            pressed.
	 * 
	 * @see jdraw.framework.DrawTool#mouseDrag(int, int, MouseEvent)
	 */
	@Override
	public void mouseDrag(int x, int y, MouseEvent e) {
		newRect.setBounds(anchor, new Point(x, y));
		java.awt.Rectangle r = newRect.getBounds();
		this.context.showStatusText("w: " + r.width + ", h: " + r.height);
	}

	/**
	 * When the user releases the mouse, the Rectangle object is updated
	 * according to the color and fill status settings.
	 * 
	 * @param x   x-coordinate of mouse
	 * @param y   y-coordinate of mouse
	 * @param e   event containing additional information about which keys were
	 *            pressed.
	 * 
	 * @see jdraw.framework.DrawTool#mouseUp(int, int, MouseEvent)
	 */
	@Override
	public void mouseUp(int x, int y, MouseEvent e) {
		newRect = null;
		anchor = null;
		this.context.showStatusText(type +" Mode");
	}

	@Override
	public Cursor getCursor() {
		return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
	}
	
	@Override
	public Icon getIcon() {
		// XXX ok. Ich würde wohl mit dem type über den Konstruktor auch den Image-Namen übergeben, das gibt etwas
		//     Flexibilität was den Dateinamen angeht.
		return new ImageIcon(getClass().getResource(IMAGES + type.toLowerCase()+".png"));
	}

	@Override
	public String getName() {
		return type;
	}

	// XXX da kann man sich fragen wer das aufrufen möchte. Aktuell wird die Methode getType nicht verwendet.
	//     Unterklassen kennen den Typ und könnten diesen auch in ihrem Konstruktor speichern wenn sie diese Info benötigen.
	//     Noch besser: Sie können getName() aufrufen!
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AbstractFigure getNewRect() {
        return newRect;
    }

    // XXX diese Methode ist schlecht, denn sie zerstört die Invariante die Du in dieser Klasse etablierst,
    //     d.h. das feld newRect (sollte wohl newFigure heissen) ist private und soll nur im mouseDown
    //     gesetzt werden. Aber mit der Methode setNewRect kann diese Eigenschaft von Aussen zerstört werden.
    public void setNewRect(AbstractFigure newRect) {
        this.newRect = newRect;
    }

    // XXX und weil anchor privat ist und nur hier während dem Aufziehen der Figur verwendet wird kann man
    //     sich fragen ob es sinnvoll ist, dieses Feld nach aussen sichtbar zu machen, insbesondere ist 
    //     Point nicht immutable, d.h. der Aufrufer könnte die Felder ändern.
    public Point getAnchor() {
        return anchor;
    }

    // XXX dasselbe gilt für das setAnchor
    public void setAnchor(Point anchor) {
        this.anchor = anchor;
    }

}
