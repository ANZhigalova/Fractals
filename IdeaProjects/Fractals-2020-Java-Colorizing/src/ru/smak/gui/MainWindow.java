package ru.smak.gui;

import ru.smak.gui.graphics.FinishedListener;
import ru.smak.gui.graphics.FractalPainter;
import ru.smak.gui.graphics.SelectionPainter;
import ru.smak.gui.graphics.components.GraphicsPanel;
import ru.smak.gui.graphics.coordinates.CartesianScreenPlane;
import ru.smak.gui.graphics.coordinates.Converter;
import ru.smak.gui.graphics.fractalcolors.ColorScheme1;
import ru.smak.gui.graphics.fractalcolors.ColorScheme2;
import ru.smak.math.Mandelbrot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame {
    GraphicsPanel mainPanel;
    CartesianScreenPlane plane;

    static final Dimension MIN_SIZE = new Dimension(450, 350);
    static final Dimension MIN_FRAME_SIZE = new Dimension(600, 500);

    public MainWindow(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(MIN_FRAME_SIZE);
        setTitle("Фракталы");

        mainPanel = new GraphicsPanel();

        mainPanel.setBackground(Color.WHITE);

        GroupLayout gl = new GroupLayout(getContentPane());
        setLayout(gl);
        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGap(4)
                .addComponent(mainPanel, (int)(MIN_SIZE.height*0.8), MIN_SIZE.height, GroupLayout.DEFAULT_SIZE)
                .addGap(4)
        );
        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addGap(4)
                .addGroup(gl.createParallelGroup()
                        .addComponent(mainPanel, MIN_SIZE.width, MIN_SIZE.width, GroupLayout.DEFAULT_SIZE)
                )
                .addGap(4)
        );
        pack();
        plane = new CartesianScreenPlane(
                mainPanel.getWidth(),
                mainPanel.getHeight(),
                -2, 1, -1, 1
        );

        var m = new Mandelbrot();
        var c = new ColorScheme1();
        var fp = new FractalPainter(plane, m);
        fp.col = c;
        mainPanel.addPainter(fp);
        var sp = new SelectionPainter(mainPanel.getGraphics());

        fp.addFinishedListener(new FinishedListener() {
            @Override
            public void finished() {
                mainPanel.repaint();
            }
        });
        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                plane.setWidth(mainPanel.getWidth());
                plane.setHeight(mainPanel.getHeight());
                sp.setGraphics(mainPanel.getGraphics());
                mainPanel.repaint();
            }
        });
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                sp.setVisible(true);
                sp.setStartPoint(e.getPoint());
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if(sp.q()) return;
                sp.changePlane(plane);
                sp.setVisible(false);
                mainPanel.repaint();
            }
        });
        mainPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                sp.setCurrentPoint(e.getPoint());
            }
        });
    }
}
