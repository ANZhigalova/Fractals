package ru.smak.gui.graphics;

import ru.smak.gui.graphics.coordinates.CartesianScreenPlane;
import ru.smak.gui.graphics.coordinates.Converter;

import java.awt.*;

public class SelectionPainter {
    private boolean isVisible = false;
    private Point startPoint = null;
    private Point currentPoint = null;
    private Graphics g;

    public SelectionPainter(Graphics g){
        this.g = g;
    }

    public void setGraphics(Graphics g){
        this.g = g;
    }

    public void setVisible(boolean value){
        if (!value){
            paint();
            currentPoint = null;
            startPoint = null;
        }
        isVisible = value;
    }

    public void setStartPoint(Point p){
        startPoint = p;
    }

    public void setCurrentPoint(Point p){
        if (currentPoint!=null)
            paint();
        currentPoint = p;
        paint();
    }
    public void changePlane(CartesianScreenPlane csp){
        var xMin = Converter.xScr2Crt(Math.min(startPoint.x,currentPoint.x),csp);
        var xMax = Converter.xScr2Crt(Math.max(startPoint.x,currentPoint.x), csp);
        var yMin = Converter.yScr2Crt(Math.max(startPoint.y,currentPoint.y),csp);
        var yMax = Converter.yScr2Crt(Math.min(startPoint.y, currentPoint.y),csp);
        csp.xMin = xMin;
        csp.xMax = xMax;
        csp.yMin = yMin;
        csp.yMax = yMax;
    }
    public boolean q(){
        return this.startPoint == this.currentPoint;
    }
//    public Point getStartPoint(){
//        return startPoint;
//    }
//    public Point getCurrentPoint(){
//        return currentPoint;
//    }
    private void paint(){
        if (startPoint!=null && currentPoint!=null) {
            g.setXORMode(Color.WHITE);
            g.setColor(Color.BLACK);
            g.drawRect(
                    Math.min(startPoint.x,currentPoint.x),
                    Math.min(startPoint.y,currentPoint.y),
                    Math.abs(currentPoint.x - startPoint.x),
                    Math.abs(currentPoint.y - startPoint.y)
            );
            g.setPaintMode();
        }
    }
}
