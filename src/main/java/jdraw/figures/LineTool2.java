package jdraw.figures;

import jdraw.framework.DrawContext;


public class LineTool2 extends AbstractDrawTool<Line> {


    public LineTool2(DrawContext context) {
        super(context);
        super.setType("Line");
    }

    @Override
    public AbstractFigure createFigure(int x, int y, int w, int h) {
        // TOD Auto-generated method stub
        return new Line(x,y,w,h);
    }

    
    
}
