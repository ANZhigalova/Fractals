package ru.smak.gui.graphics.fractalcolors;

import java.awt.*;

public class ColorScheme1 implements Colorizer{

    @Override
    public Color getColor(float x) {
        float r, g, b;
        r = (float)(12*x/Math.exp(10*x));
        g = (float)Math.abs(Math.tan(x*x/5) * Math.tanh(x));
        b = (float)Math.abs(Math.sin(3*x*x*x/7)/Math.cosh(9*x+3));
//        r = (float)Math.abs(x*x);
//        g = (float)Math.abs(x*0.5);
//        b = (float)Math.abs(Math.sin(0.5*x));
        return new Color(r, g, b);
    }
}
