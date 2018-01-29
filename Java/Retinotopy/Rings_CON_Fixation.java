/**
 * Rings_CON
 * 
 * Modified:2011-5-4
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

import static java.lang.Math.ceil;
import static java.lang.Math.max;
import static java.lang.Math.random;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Expanding flickering rings with non-overlapping jumps 
 * 
 * @author boyaci
 * @version 2011-05-04
 *
 */
public class Rings_CON_Fixation extends FullScreen implements Runnable {
  
  static final String TRIGGER = "6";
  static final String[] RESPONSE_BUTTONS = {"4","3"}; // Task: red key (4) alternative1; green key (3) alternative2
  
  static final int MEAN = 128;
  static final Color MEAN_COLOR = new Color(MEAN, MEAN, MEAN);
  
  static final int ONE_DEGREE = 35; // for UMRAM 3T projector  
  
  static final long FLICK = 64; // ~1000/(2*Hz) ms

  static final int N_RINGS = 6;
  static final int REPEAT = 5;
  static final long TR = 2000;
  static final long FIRST_BLANK = (10000/TR) * TR;
  static final long BLOCK_DURATION = (10000/TR) * TR;
  static final long LAST_BLANK = (10000/TR) * TR;
  // total time = FIRST_BLANK + REPEAT * BLOCK_DURATION * N_RINGS + LAST_DURATION = 
  // 10 + 5 * 10 * 6 + 10 = 320 sec = 5 min. 20 sec. = 160 TR
  
  static final int FIX_SIZE = ONE_DEGREE / 5;
  static final Color[] FIX_COLORS = {Color.BLACK, Color.red, Color.green};
  static final long[] FIX_DURATIONS = {2000l, 200l, 200l};
  
  boolean showFix = false;
  static int xFix;
  static int yFix;
  BufferedImage fixationMark;
  
  public static void main(String[] args){
  
    Rings_CON_Fixation loc = new Rings_CON_Fixation();
    loc.setNBuffers(2);
    loc.setBackground(MEAN_COLOR);
    xFix = (loc.getWidth() - FIX_SIZE) / 2;
    yFix = (loc.getHeight() - FIX_SIZE) / 2;
    
    System.err.println( "Total number of TRs: " + 
        (FIRST_BLANK + REPEAT * N_RINGS * BLOCK_DURATION  + LAST_BLANK)/TR);
    new Thread(loc).run();
  }
  
  public void run(){
    
    try{
      
      BufferedImage[][] rings = new BufferedImage[N_RINGS][2];
      int lWidth = ONE_DEGREE*22;
                  
      double rIn = 0.5 * ONE_DEGREE;
      double rOut = 2 * ONE_DEGREE;
      
      double rIncr = (lWidth/2 - rOut) / (N_RINGS - 1) ;

      int nCheck = 8;
      int nCheckPolar = nCheck;
      int nCheckRadial = 2;
      
      for(int i=rings.length-1; i>=0; i--){    
        
        rings[i][0] = makeCheckerRing(lWidth, nCheckRadial, nCheckPolar, rIn, rOut, 0);
        rings[i][1] = makeCheckerRing(lWidth, nCheckRadial, nCheckPolar, rIn, rOut, 1);
        rIn = rOut;
        rOut += rIncr;
        nCheckPolar += 2;
      }
      
      hideCursor();
      blankScreen();
      updateScreen();
      
      //long adjust  = 0;
      long overTime = 0;
  
      
      blankScreen();
      displayText(10,50, "Stimulus program " + getClass().getName() + " ready to start...");
      updateScreen();
      while (!getKeyTyped(-1).getKey().equals(TRIGGER));
     
      showFix = true;
      DynamicFixation f = new DynamicFixation(FIX_COLORS, FIX_DURATIONS);
      f.startTask();

      long start = System.currentTimeMillis();
      long bigStart = start;
      blankScreen();
      updateScreen();
      Thread.sleep(max(0,FIRST_BLANK - System.currentTimeMillis() + start));

      for(int i=0; i<REPEAT; i++){
        
    	blankScreen();
    	  
        for(int j=0 ; j<rings.length; j++){
        
          start = System.currentTimeMillis();
          long init = System.currentTimeMillis();
          
          for(int k=0; k<(int)((BLOCK_DURATION-overTime)/FLICK/2); k++){
            
            init = System.currentTimeMillis();
            if(System.currentTimeMillis()-start > BLOCK_DURATION)
              break;
            
            blankScreen();
            displayImage(rings[j][0]);
            updateScreen();
            
            Thread.sleep(max(0,FLICK - System.currentTimeMillis() + init));
            
            init = System.currentTimeMillis();
            //blankScreen();
            displayImage(rings[j][1]);
            updateScreen();
            
            Thread.sleep(FLICK - System.currentTimeMillis() + init);
          }
          overTime += (System.currentTimeMillis() - start) - BLOCK_DURATION;
          }
        //adjust = (System.currentTimeMillis()-bigStart) - N_RINGS*BLOCK_DURATION;
      }
      
      blankScreen();
      updateScreen();
      Thread.sleep(LAST_BLANK);
      f.stopTask();
    
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      e.printStackTrace();
    } finally {
      closeScreen();
    }
  }
  
  public BufferedImage makeCheckerRing(int dim, int Nradsegment,
      int Npolsegment, double rin, double rout, int color){
        
    BufferedImage ringIm = new BufferedImage((int)rout, (int)rout,
        BufferedImage.TYPE_BYTE_GRAY);
    
    Color[] colring=new Color[2];
    if (color ==0){
      colring[0]=Color.BLACK;
      colring[1]=Color.WHITE;
    }
    if (color==1){
      colring[0]=Color.WHITE;
      colring[1]=Color.BLACK;
    }
    
    Graphics2D g = (Graphics2D)ringIm.getGraphics();
    g.setColor(MEAN_COLOR);
    g.fillRect(0, 0, (int)rout, (int)rout);
    
    g.setColor(colring[0]);
    g.fillOval(0, 0, (int)rout, (int)rout);
    
    int rIncr = (int)(rout - rin) / Nradsegment;
    int tIncr = 360/Npolsegment;
      
    g.setColor(colring[1]);
    
    for(double r = rout; r>rin; r -= 2*rIncr){
  
      for(int t = 0; t<360; t+= tIncr){
      
        g.setColor(colring[1]);
        g.fillArc((int)(rout-r)/2, (int)(rout-r)/2, (int)(ceil(r)),(int)(ceil(r)), t, tIncr);

        g.setColor(colring[0]);
        double rp = r - rIncr;
        if (rp<=rin)
          break;
        g.fillArc((int)(rout-rp)/2, (int)(rout-rp)/2, (int)(ceil(rp)),(int)(ceil(rp)), t, tIncr);
      
        Color tmp = colring[1];
        colring[1] = colring[0];
        colring[0] = tmp;
      }
    
    }
    
    g.setColor(MEAN_COLOR);
    g.fillOval((int)(rout-rin-2)/2, (int)(rout-rin-2)/2, (int)rin+2, (int)rin+2);
    
    return ringIm;
    
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
	      fsG.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	RenderingHints.VALUE_ANTIALIAS_ON);
	      fsG.setRenderingHint(RenderingHints.KEY_RENDERING,
	RenderingHints.VALUE_RENDER_QUALITY);
	      fsG.setPaint(c);
	      fsG.fill(new Ellipse2D.Double(0, 0, FIX_SIZE, FIX_SIZE));
	      
	    } 
	  }
}
