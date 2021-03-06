package ru.smak.gui.graphics;

import ru.smak.gui.graphics.coordinates.CartesianScreenPlane;
import ru.smak.gui.graphics.coordinates.Converter;
import ru.smak.gui.graphics.fractalcolors.Colorizer;
import ru.smak.math.Complex;
import ru.smak.math.Fractal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class FractalPainter extends Painter {

    private final CartesianScreenPlane plane;
    private final Fractal fractal;
    private BufferedImage sbi = null;
    private BufferedImage bi = null;
    private boolean recreate = true;
    private static final int stripCount = Runtime.getRuntime().availableProcessors();
    private int done = 0;

    private ArrayList<FinishedListener> fin = new ArrayList<>();

    public Colorizer col;

    public FractalPainter(CartesianScreenPlane plane,
                          Fractal f){
        this.plane = plane;
        fractal = f;
    }
    ArrayList<Thread> ts = new ArrayList<>();
    ArrayList<FractalStripPainter> fsp = new ArrayList<>();

    public void addFinishedListener(FinishedListener f){
        fin.add(f);
    }

    public void removeFinishedListener(FinishedListener f){
        fin.remove(f);
    }

    /**
     * метод рисования множества фрактала
     * @param graphics графика, на которой ведется рисование
     */
//    @Override
//    public void paint(Graphics graphics) {
//        //var t1 = System.currentTimeMillis();
//        for (int i = 0; i < plane.getWidth(); i++){
//            for (int j = 0; j < plane.getHeight(); j++){
//                var x = Converter.xScr2Crt(i, plane);
//                var y = Converter.yScr2Crt(j, plane);
//                var is = fractal.isInSet(new Complex(x, y));
//                Color c = (col!=null)?col.getColor(is):((is==1.0F)?Color.BLACK:Color.WHITE);
//                graphics.setColor(c);
//                graphics.fillRect(i, j, 1, 1);
//            }
//        }
//        //var t2 = System.currentTimeMillis();
//    }
    @Override
    public void paint(Graphics graphics) {
        if (sbi != null){
            graphics.drawImage(sbi, 0, 0, plane.getWidth(), plane.getHeight(), null);
            if (!recreate) {
                recreate = true;
                return;
            }
        }
        bi = new BufferedImage(plane.getWidth(), plane.getHeight(), BufferedImage.TYPE_INT_RGB);
        var g = bi.getGraphics();
        done = 0;
        for (var sp : fsp){
            sp.stop();//останавливает метод run
        }
        fsp.clear();//очистили список рисовальщиков полос
        for (var t : ts){
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        }
        ts.clear();//отчистили список потоков
        for (int i = 0; i < stripCount; i++){
            fsp.add(new FractalStripPainter(g, i));
            ts.add(new Thread(fsp.get(i)));
            ts.get(i).start();//запускаем потоки на выполнение "по списку"
        }
    }
    private void finished() {
        sbi = new BufferedImage(plane.getWidth(), plane.getHeight(), BufferedImage.TYPE_INT_RGB);
        var sg = sbi.getGraphics();
        sg.drawImage(bi, 0, 0, null);
        recreate = false;
        for (var f : fin) {
            f.finished();
        }
    }

    class FractalStripPainter implements Runnable{

        private final int begPx;
        private final int endPx;
        private final Graphics graphics;
        private final BufferedImage bi;
        private boolean stop = false;

        public FractalStripPainter(Graphics g, int stripId){
            graphics = g;
            int width = plane.getWidth() / stripCount;
            begPx = stripId * width;
            if (stripId == stripCount - 1)
                width += plane.getWidth() % stripCount;
            endPx = begPx + width;
            bi = new BufferedImage(width, plane.getHeight(), BufferedImage.TYPE_INT_RGB);
        }

        @Override
        public void run() {
            var g = bi.getGraphics();
            for (int i = begPx; i < endPx; i++){
                for (int j = 0; j < plane.getHeight(); j++){
                    if (stop) return;
                    var x = Converter.xScr2Crt(i, plane);
                    var y = Converter.yScr2Crt(j, plane);
                    var is = fractal.isInSet(new Complex(x, y));
                    Color c = (col!=null)?col.getColor(is):((is==1.0F)?Color.BLACK:Color.WHITE);
                    g.setColor(c);
                    g.fillRect(i-begPx, j, 1, 1);
                }
            }
            synchronized (graphics) {
                graphics.drawImage(bi, begPx, 0, null);
            }
            done++;
            if (done == stripCount)
                finished();
        }

        public void stop() {
            stop = true;
        }
    }
}
