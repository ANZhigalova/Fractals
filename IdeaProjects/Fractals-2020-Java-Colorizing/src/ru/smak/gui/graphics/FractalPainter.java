package ru.smak.gui.graphics;

import ru.smak.gui.graphics.coordinates.CartesianScreenPlane;
import ru.smak.gui.graphics.coordinates.Converter;
import ru.smak.gui.graphics.fractalcolors.Colorizer;
import ru.smak.math.Complex;
import ru.smak.math.Fractal;

import java.awt.*;

public class FractalPainter extends Painter {

    private final CartesianScreenPlane plane;
    private final Fractal fractal;

    public Colorizer col;

    public FractalPainter(CartesianScreenPlane plane,
                          Fractal f){
        this.plane = plane;
        fractal = f;
    }

    /**
     * метод рисования множества фрактала
     * @param graphics графика, на которой ведется рисование
     */
    @Override
    public void paint(Graphics graphics) {
        //var t1 = System.currentTimeMillis();
        for (int i = 0; i < plane.getWidth(); i++){
            for (int j = 0; j < plane.getHeight(); j++){
                var x = Converter.xScr2Crt(i, plane);
                var y = Converter.yScr2Crt(j, plane);
                var is = fractal.isInSet(new Complex(x, y));
                Color c = (col!=null)?col.getColor(is):((is==1.0F)?Color.BLACK:Color.WHITE);
                graphics.setColor(c);
                graphics.fillRect(i, j, 1, 1);
            }
        }
        //var t2 = System.currentTimeMillis();
    }
}
