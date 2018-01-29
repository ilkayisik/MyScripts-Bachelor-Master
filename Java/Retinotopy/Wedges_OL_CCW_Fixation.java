/**
 * Wedges_OL_CCW_Fixation
 * 
 * Modified:2012-XX-XX
 * 
 * Copyright (C) 2005-2010 Huseyin Boyaci. All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. 
 * 
 * See the GNU General Public License version 2 for more details 
 * (a copy is included in the LICENSE file that accompanied this code) 
 * (also available at http://www.gnu.org) You should have received a copy of 
 * the GNU General Public License along with this program; if not, write to 
 * the Free Software Foundation, Inc., 59 Temple Place - Suite 330, 
 * Boston, MA 02111-1307, USA.
 */

import static java.lang.Math.PI;
import static java.lang.Math.max;
import static java.lang.Math.random;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Counter clockwise rotating flickering wedges with overlapping jumps 
 * in both visual fields
 * 
 * @author boyaci
 * @version 2012-XX-XX
 *
 */
public class Wedges_OL_CCW_Fixation extends FullScreen implements Runnable {
  
  static final String TRIGGER = "6";
  static final String[] RESPONSE_BUTTONS = {"4","3"}; // Task: red key (4) alternative1; green key (3) alternative2

  static final int MEAN = 128;
  static final Color MEAN_COLOR = new Color(MEAN, MEAN, MEAN);
  
  static final int ONE_DEGREE = 35; // for UMRAM 3T projector  
  
  static final long FLICK = 64; // ms, flicker rate, f = 1000/(2*FLICK) Hz

  // dimensions of the wedges
  static final int OUTER_RADIUS = 11 * ONE_DEGREE;
  static final int INNER_RADIUS = (int)(0.8 * ONE_DEGREE);
  static final int ANGLE_ARC = 30;
  static final int N_CHECK_RADIAL = 8;
  static final int N_CHECK_POLAR = 5;
  
  // Angle of the first wedge (0 degree is equal to 3 o'clock), 
  // and angle difference between consecutive wedges
  static final int ANGLE_FIRST_WEDGE = 0;
  static final int ANGLE_DELTA = -10;

  static final long TR = 2000;
  
  static final int N_WEDGES = 36;
  static final int REPEAT = 7;
  static final long BLOCK_DURATION = TR;
  // total time = (REPEAT+0.5) * BLOCK_DURATION * N_WEDGES = 
  // 7.5 * 2 * 36 = 540 sec = 9 min = 270 TR 
  // The additional 0.5 cycle comes because of the discarded 0.25 cycle in the beginning 
  // (9-to-12 o'clock) and 0.25 cycle in the end (12-to-3 o'clock)
  
  // Fixation mark...
  static final int FIX_SIZE = ONE_DEGREE / 4;
  static final Color[] FIX_COLORS = {Color.BLACK, Color.red, Color.green};
  static final long[] FIX_DURATIONS = {2000l, 200l, 200l};
  
  boolean showFix = false;
  static int xFix;
  static int yFix;
  BufferedImage fixationMark;
  
  public static void main(String[] args){
  
    Wedges_OL_CCW_Fixation loc = new Wedges_OL_CCW_Fixation();
    loc.setNBuffers(2);
    loc.setBackground(MEAN_COLOR);
 
    xFix = (loc.getWidth() - FIX_SIZE) / 2;
    yFix = (loc.getHeight() - FIX_SIZE) / 2;
    
    System.err.println( "Total number of TRs: " + 
        ((REPEAT+0.5) * N_WEDGES * BLOCK_DURATION )/TR);
    
    new Thread(loc).run();
  }
  
  public void run(){
    
    try{
        
      hideCursor();
      blankScreen();
      updateScreen();
      
      // prepare wedges....
      //
      // First the original wedge  
      BufferedImage[][] wedges = new BufferedImage[N_WEDGES][2]; 
      wedges[0] = makeCheckerWedge(INNER_RADIUS, OUTER_RADIUS, 
          ANGLE_ARC, N_CHECK_RADIAL, N_CHECK_POLAR);
      // next rotate it a little to center at 3 o'clock (ccw) or 9 o'clock (cw)
      for(int i=0; i<wedges[0].length; i++)
        wedges[0][i] = rotateWedge(wedges[0][i], (ANGLE_FIRST_WEDGE + ANGLE_ARC/2)*PI/180);        
      
      // CCW/CW rotation
      double orientationIncrement = ANGLE_DELTA * PI / 180;
      double orientation = orientationIncrement;
      // generate the remaining N_WEDGES-1 wedges by rotating the original wedge
      for(int i=1; i<wedges.length; i++){
        wedges[i][0] =rotateWedge(wedges[0][0],orientation);
        wedges[i][1] =rotateWedge(wedges[0][1],orientation);
        orientation += orientationIncrement;
      }
              
      blankScreen();
      displayText(10,50, "Stimulus program " + getClass().getName() + " ready to start...");
      updateScreen();
      while (!getKeyTyped(-1).getKey().equals(TRIGGER));

      // variables used to keep precise timing
      long overTime = 0;
      long start = System.currentTimeMillis();
      long bigStart = start;

      blankScreen();
      updateScreen();
      
      showFix = true;
      DynamicFixation f = new DynamicFixation(FIX_COLORS, FIX_DURATIONS);
      f.startTask();

      // cycles
      for(int i=0; i<REPEAT; i++){
        for(int j=0; j<N_WEDGES; j++){
          start = System.currentTimeMillis();
          long init = System.currentTimeMillis();
          for(int k=0; k<(int)((BLOCK_DURATION-overTime)/FLICK/2); k++){
            init = System.currentTimeMillis();
            if(System.currentTimeMillis()-start > BLOCK_DURATION)
              break;
            displayImage(wedges[j][0]);
            updateScreen();

            Thread.sleep(max(0,FLICK - System.currentTimeMillis() + init));

            init = System.currentTimeMillis();
            displayImage(wedges[j][1]);
            updateScreen();

            Thread.sleep(max(0,FLICK - System.currentTimeMillis() + init));
          }
          overTime += (System.currentTimeMillis() - start) - BLOCK_DURATION;
        }
      }
      
      // half cycle in the end for discarded quarter cycles
      for(int j=0 ; j<N_WEDGES/2; j++){
      	start = System.currentTimeMillis();
      	long init = System.currentTimeMillis();
      	for(int k=0; k<(int)((BLOCK_DURATION-overTime)/FLICK/2); k++){
      		init = System.currentTimeMillis();
      		if(System.currentTimeMillis()-start > BLOCK_DURATION)
      			break;
      		displayImage(wedges[j][0]);
      		updateScreen();
      		Thread.sleep(max(0,FLICK - System.currentTimeMillis() + init));
      		init = System.currentTimeMillis();
      		displayImage(wedges[j][1]);
      		updateScreen();
      		Thread.sleep(max(0,FLICK - System.currentTimeMillis() + init));
      	}
      	overTime += (System.currentTimeMillis() - start) - BLOCK_DURATION;
      }
      
      System.err.println("Total elapsed time: " + (System.currentTimeMillis() - bigStart));
      
      f.stopTask();
    
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      e.printStackTrace();
    } finally {
      closeScreen();
    }
  }
  
  /**
   * Rotates wedges about the fixation mark
   * 
   * @param wedgeIm
   * @param rotation
   * @return
   */
  public BufferedImage rotateWedge(BufferedImage wedgeIm, double rotation) {

    int dim = wedgeIm.getHeight();
    if(rotation !=0 ){
      AffineTransformOp ato = new AffineTransformOp(AffineTransform
          .getRotateInstance(rotation, (double) dim / 2, (double) dim / 2),
          AffineTransformOp.TYPE_BICUBIC);
      BufferedImage bic = new BufferedImage(dim, dim,
          BufferedImage.TYPE_BYTE_GRAY);
      Graphics2D bicG = bic.createGraphics();
      bicG.setColor(new Color(MEAN,MEAN,MEAN));
      bicG.fillRect(0,0,dim,dim);
      bicG.dispose();
      ato.filter(wedgeIm, bic);
      return bic;
    }else{
      return wedgeIm;
    }
  }
    
  /**
   * 
   * Returns a pair of contrast reversed checker-board pattern mapped wedges.
   * 
   * @param innerRadius
   * @param outerRadius
   * @param arcAngle
   * @param nRadSegment
   * @param nPolSegment
   * @return
   */
  public BufferedImage[] makeCheckerWedge(int innerRadius, int outerRadius, int arcAngle, 
      int nRadSegment, int nPolSegment) {

    BufferedImage[] wedgePair = new BufferedImage[2];

    Color[] checkColor = {Color.WHITE, Color.BLACK};

    int innerDiameter = innerRadius * 2;
    int outerDiameter = outerRadius * 2;
    int deltaPolAngle = arcAngle / nPolSegment;
    int deltaRadius = outerDiameter / nRadSegment;
    
    for(int i=0; i<wedgePair.length; i++){

      wedgePair[i] = new BufferedImage(outerDiameter, outerDiameter, BufferedImage.TYPE_BYTE_GRAY);
      Graphics2D g2d = wedgePair[i].createGraphics();
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g2d.setColor(MEAN_COLOR);
      g2d.fillRect(0, 0, outerDiameter, outerDiameter);

      int color1 = i;
      int radius = outerDiameter;
      
      while(radius >= innerDiameter) {

        int color = color1;   
        int arcStart = 0;
        int x = (outerDiameter - radius)/2;
        int y = (outerDiameter - radius)/2;

        while(arcStart + deltaPolAngle <= arcAngle){
          g2d.setColor(checkColor[color]);
          g2d.fillArc(x, y, radius, radius, arcStart, deltaPolAngle);
          arcStart += deltaPolAngle;
          color++;
          color %= 2;
        }
        radius -= deltaRadius;
        color1++;
        color1 %= 2;
      }

      g2d.setColor(MEAN_COLOR);
      g2d.fillOval((outerDiameter-innerDiameter)/2, (outerDiameter-innerDiameter)/2, 
          innerDiameter, innerDiameter);

      g2d.dispose();

    }
    
    return wedgePair;
  }
    
  public void updateScreen(){
  	if(showFix)
  		displayImage(xFix,yFix,fixationMark);
  	super.updateScreen();
  }
  
  class DynamicFixation implements Runnable{
    
    ArrayList<Long> sequence;
    boolean running = false;
    Thread task;
    PrintWriter pwTask;
    
    Color defaultColor;
    Color alternativeColor1;
    Color alternativeColor2;
    long defaultDuration;
    long alternativeDuration;
    long jitter = 500;
    
    public void startTask() {

      if (task == null || !running) {
        task = new Thread(this);
        running = true;
        task.start();
      }
    }

    public void stopTask() {

      running = false;
    }
        
    public DynamicFixation(Color[] fixColors, long[] fixDurations){
      
    	defaultColor = fixColors[0];
    	alternativeColor1 = fixColors[1];
    	alternativeColor2 = fixColors[2];
    	
    	defaultDuration = fixDurations[0];
    	alternativeDuration = fixDurations[1];

    	try {
    		pwTask = Extras.setOutputFile("data");
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	}

    	int numChar = 100;
    	sequence = new ArrayList<Long>(numChar);

    	for (int i = 0; i < numChar; i++) 
    		sequence.add((long) (random() * jitter) * 2 - jitter + defaultDuration);
       
    }
    
    public void run(){
      
      try{
        Iterator<Long> it = sequence.listIterator();
        Long onset = 0L;
        
        while(running){
          
          int c = (int)(random()*2);
          if(c==0)
            setFixation(alternativeColor1);
          else
            setFixation(alternativeColor2);
          updateScreen();
          onset = System.currentTimeMillis();
          flushKeyTyped();
          
          Thread.sleep(alternativeDuration);
          
          if(!running)
            break;
          setFixation(defaultColor);
          updateScreen();
          long mark = it.next();
          Thread.sleep(mark);
          
          while(true){
          	KeyTyped kt = getKeyTyped();
            if(kt == null){
              pwTask.println(0 + " " + 0);
              pwTask.flush();
              break;
            }
            String resp = kt.getKey();
            Long respWhen = kt.getWhen();
            //System.out.println(resp);
            if(!resp.equals(TRIGGER)){
              if( (c==0 && resp.equals(RESPONSE_BUTTONS[0]) ) || 
                  (c==1 && resp.equals(RESPONSE_BUTTONS[1])))
                pwTask.println(1 + " " + (respWhen-onset));
              else 
                pwTask.println(0 + " " + (respWhen-onset));
              pwTask.flush();
              break;
            }
          }
          if(!it.hasNext())
            it = sequence.listIterator();
        }      
        pwTask.close();
      }catch(InterruptedException e){
        Thread.currentThread().interrupt();
      }  
    }
        
    public void setFixation(Color c) {
    	
      fixationMark = 
        getGraphicsConfiguration().createCompatibleImage(FIX_SIZE + 2,
        		FIX_SIZE + 2, ColorModel.TRANSLUCENT);
      Graphics2D fsG = (Graphics2D) fixationMark.getGraphics();
      fsG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      fsG.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      fsG.setPaint(c);
      fsG.fill(new Ellipse2D.Double(0, 0, FIX_SIZE, FIX_SIZE));
      
    } 
  }
}
