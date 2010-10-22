/*
CAS Computer Algebra System
Copyright (C) 2005  William Tracy

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */

package org.matheclipse.symja.plot;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.matheclipse.parser.client.eval.DoubleEvaluator;
import org.matheclipse.parser.client.eval.DoubleVariable;
import org.matheclipse.symja.CoreCallbackFunction;

/**
 * Plots functions in 2D.
 */
public class Plotter extends AbstractPlotter2D {

  /**
   * 
   */
  private static final long serialVersionUID = 5687700167663608759L;

  /**
   * The number of points to plot on each line on this plot.
   */
  protected int thisResolution;

  /**
   * The number of lines to plot.
   */
  protected int numFuncs;

  /**
   * The points on the plotted lines.
   */
  protected double point[][];

  /**
   * The Y coordinates of the points in the component's graphics coordinate
   * space.
   */
  protected int paintPoint[];

  /**
   * The X coordinates of the points in the component's graphics coordinate
   * space.
   */
  protected int xPoint[];

  /**
   * The cached plotters.
   */
  protected static List<Plotter> cache = new ArrayList<Plotter>();

  public void setFunctions(List<String> functions) {
    numFuncs = functions.size();
    // System.out.println("Plot has " + numFuncs + " functions!!!");
    point = new double[numFuncs][thisResolution + 1];
    paintPoint = new int[thisResolution + 1];
    xPoint = new int[thisResolution + 1];
    color = new Color[numFuncs];
    DoubleEvaluator engine = new DoubleEvaluator();
    engine.setCallbackFunction(CoreCallbackFunction.CONST);
    ListIterator<String> i = functions.listIterator();
    while (i.hasNext()) {
      String s = i.next();
      // System.out.println(s);
      populateFunction(s, i.previousIndex(), engine);
    }

    updatePlot();
  }

  /**
   * Readies a plot for display.
   */
  public void updatePlot() {
    thisResolution = newResolution;

    /*
     * if (yMax <= yMin) { if (yMax < 0) yMax = 0; if (yMin > 0) yMin = 0; if
     * (yMax <= yMin) { yMax = yMin = (yMax + yMin) /2; ++yMax; --yMin; } }
     * 
     * yRange = yMax - yMin;
     */

    setupText();

    EventQueue.invokeLater(this);
  }

  /**
   * Populates the points array with function values.
   */
  protected void populateFunction(String expression, int func,
      DoubleEvaluator engine) {
    // System.out.println("Initializing color #" + func + " of " +
    // color.length);
    color[func] = COLOR[func % COLOR.length];
    for (int counter = 0; counter <= thisResolution; ++counter) {
      // System.out.println("Generating point #" + counter + " for function #" +
      // func);
      try {
        populatePoint(func, expression, counter, engine);
      } catch (Exception e) {
        point[func][counter] = Double.POSITIVE_INFINITY;
      }
    }
  }

  /**
   * Inserts a function value into the appropriate point in the array.
   */
  protected void populatePoint(int func, String expression, int x,
      DoubleEvaluator engine) {
    double xVal = xMin + (xRange * x)
        / (double) thisResolution;
    engine.defineVariable("x", new DoubleVariable(xVal));
    point[func][x] = engine.evaluate(expression);
  }

  /**
   * Returns the color encoded in the HObject, or null if the HObject doesn't
   * encode a color.
   */
  /*
   * protected Color getColor(HObject ho) { String s =
   * ho.toString().toLowerCase();
   * 
   * if (s.startsWith("blue")) return Color.BLUE; else if (s.startsWith("cyan"))
   * return Color.CYAN; else if (s.startsWith("green")) return Color.GREEN; else
   * if (s.startsWith("magenta")) return Color.MAGENTA; else if
   * (s.equals("orange")) return Color.ORANGE; else if (s.equals("pink")) return
   * Color.PINK; else if (s.equals("red")) return Color.RED; else if
   * (s.equals("yellow")) return Color.YELLOW; else return null; }
   */

  /**
   * Paints the functions.
   */
  protected void paintPlots(Graphics2D g2d, int top, int height, int bottom,
      int left, int width, int right) {
    for (int func = 0; func < numFuncs; ++func) {
      paintPlot(g2d, top, height, bottom, left, width, right, func);
    }
  }

  /**
   * Paints a function.
   */
  protected void paintPlot(Graphics2D g2d, int top, int height, int bottom,
      int left, int width, int right, int func) {
    // int x[] = new int[thisResolution + 1];
    // int y[] = new int[thisResolution + 1];
    int index = 0;

    g2d.setColor(color[func]);

    for (int counter = 0; counter <= thisResolution; ++counter) {
      xPoint[index] = left + counter * width / thisResolution;
      paintPoint[index] = top + height
          - (int) ((point[func][counter] - yMin) * height / yRange);
      if (paintPoint[index] >= top && paintPoint[index] <= bottom) {
        ++index;
      } else {
        --index;
        g2d.drawPolyline(xPoint, paintPoint, index);
        index = 0;
      }
    }
    g2d.drawPolyline(xPoint, paintPoint, index);
  }

  /**
   * Returns a new instance of this class or a cached instance.
   */
  public static Plotter getPlotter() {
    if (cache.isEmpty()) {
      return new Plotter();
    } else {
      Plotter p = cache.get(0);
      cache.remove(p);
      return p;
    }
  }

  /**
   * Enters this instance into the cache.
   */
  public void reclaim() {
    point = null;
    paintPoint = null;
    xPoint = null;
    cache.add(this);
  }

  /**
   * Empties the cache.
   */
  public static void clearCache() {
    cache.clear();
  }
}
