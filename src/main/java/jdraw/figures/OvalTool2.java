package jdraw.figures;

import jdraw.framework.DrawContext;


public class OvalTool2 extends AbstractDrawTool<Oval> {

	//private AbstractFigure newRect = null;

    public OvalTool2(DrawContext context) {
        super(context);
        super.setType("Oval");
    }

    @Override
    public AbstractFigure createFigure(int x, int y, int w, int h) {
        return new Oval(x,y,w,h);
    }

}
