package ru.smak.math;

public class Mandelbrot implements Fractal{

    private int maxIters = 200;
    private double r2 = 4;

    /**
     * функция записи количества итераций
     * @param value число итераций, будет присвоено, если больше 5
     */
    public void setMaxIters(int value){
        maxIters = Math.max(5, value);
    }

    /**
     * функция записи числа R для сравнения
     * @param value число R, будет записно, если больше 0,1
     */
    public void setR(int value){
        var r = Math.max(0.1, value);
        r2 = r*r;
    }

    /**
     * проверка на принадлежность числа множеству
     * @param c комплексное число для проверки
     * @return true, если принадлежит, иначе - false
     */
    @Override
    public float isInSet(Complex c) {
        final var z = new Complex();
        for (int i = 0; i<maxIters; i++){
            z.timesAssign(z);
            z.plusAssign(c);
            if (z.abs2() > r2) return (float)i/maxIters;
        }
        return 1.0F;
    }
}
