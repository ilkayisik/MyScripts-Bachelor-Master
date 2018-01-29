/*
 * AdaptiveOrientationDiscrimination.java
 * 
 * Copyright (C) 2005-2012 Huseyin Boyaci. All Rights Reserved.
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
 * 
 */

import static java.lang.Math.PI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.sound.sampled.LineUnavailableException;

import psychWithJava.Extras;
import psychWithJava.FullScreen;
import psychWithJava.PixelOutOfRangeException;

/**
 * Implements a simple forced-choice experiment 
 * with an adaptive transformed (1 up/D down) and weighted 
 * (different up down steps) method
 * 
 * 
 * @see FullScreen
 * 
 * @author Huseyin Boyaci
 * @since 2012-4-9
 *
 */
public class AdaptiveOrientationDiscrimination extends FullScreen implements Runnable{
  
	/** degree to radian conversion */
	static final double ONE_DEGREE = PI/180;
	
  /** pixel value of the background */
  static final int MEAN = 127;  
  /** frequency of the sinusoidal grating, in cycles per pixel */
  static final double CPP = 0.02;
  /** determines the horizontal shift of the sinusoidal grating */
  static final double PHASE = 0; 
  /** determines the spread of the Gaussian envelope */
  static final int SIGMA = 24;
  /** Maximum contrast of the Gabor */
  static final double CONTRAST = 0.5;
  
  /** the initial orientations, vertical = 0 */
  static final double[] INITIAL_ORIENTATION = {
  	1*ONE_DEGREE,-1*ONE_DEGREE};
  /** up and down step sizes */
  static final double DELTA_DOWN = 0.1 * ONE_DEGREE;
  static final double DELTA_UP = 0.1 * ONE_DEGREE;
  /** After D consecutive correct responses 
   * stimulus level goes down by DELTA_DOWN */
  static final int D = 3;
  /** Number of trials for each staircase */
  static final int N_TRIALS = 30;
   
  public static void main(String[] args){
    
    AdaptiveOrientationDiscrimination aod = new AdaptiveOrientationDiscrimination();
    new Thread(aod).start();
  }

  public void run() {

    try{
    
    	// Open the output file for saving the data
      PrintWriter outputFile = Extras.setOutputFile("data");

      setNBuffers(2);
      setBackground(new Color(MEAN,MEAN,MEAN));

      setFont(new Font("SansSerif", Font.BOLD, 26));
      displayText(100, 200, "Task:");
      displayText(100, 300, "  Indicate the orientation of");
      displayText(100, 350, "  the Gabor patch");    
      displayText(100, 400, "  (Use the Left/Right arrows)");
      displayText(100, 500, "Now press any key to continue");
      updateScreen();
      getKeyPressed(-1);
      blankScreen();
      updateScreen();
      hideCursor();
  
      // keeps track of stimulus level and correct count
      // for each staircase
      HashMap<Integer,HashMap<Double,Integer>> trials = 
      	new HashMap<Integer,HashMap<Double,Integer>>();
      
      for(int i=0; i<INITIAL_ORIENTATION.length;i++){
      	HashMap<Double,Integer> tmp1 = new HashMap<Double,Integer>();
      	tmp1.put(INITIAL_ORIENTATION[i], 0);
      	trials.put(i,tmp1);
      }
                  
      for(int trial=0; trial<N_TRIALS; trial++){
      	
      	// generate and shuffle a List of staircase id's
      	Set<Integer> staircaseID =	trials.keySet();
      	ArrayList<Integer> tmp2 = new ArrayList<Integer>(staircaseID);
      	Collections.shuffle(tmp2);
      	Iterator<Integer> staircaseIDIt = tmp2.iterator();

      	// this loop goes over the shuffled staircases
      	while(staircaseIDIt.hasNext()){
      		
      		int stID = staircaseIDIt.next();
      		HashMap<Double,Integer> tmp3 = trials.get(stID);
      		double stimLevel = tmp3.keySet().iterator().next();
      		int correctCount = tmp3.get(stimLevel);
      		
      		outputFile.printf("%3.5f ",stimLevel*180/PI);

      		BufferedImage gbr = 
      			Extras.gabor(MEAN, CPP, CONTRAST, PHASE, SIGMA, stimLevel);

      		blankScreen();
      		displayImage(gbr);
      		updateScreen();
      		flushKeyPressed();

      		Thread.sleep(250);
      		blankScreen();
      		updateScreen();

      		int response = getKeyPressed(-1).getKey();

      		/* wait until a left or right arrow is pressed */
      		while(!(response== KeyEvent.VK_LEFT || response==KeyEvent.VK_RIGHT))
      			response=getKeyPressed(-1).getKey();
      		
      		if((response == KeyEvent.VK_LEFT && stimLevel<0) ||
      				(response == KeyEvent.VK_RIGHT && stimLevel>0) ){
      			Extras.beep(300,200);
      			outputFile.println("correct");
      			correctCount++;
      			if(correctCount == D){
      				if(stimLevel>0)
      					stimLevel = Math.max(DELTA_DOWN,stimLevel-DELTA_DOWN);
      				else
      					stimLevel = Math.min(-DELTA_DOWN,stimLevel+DELTA_DOWN);
      				correctCount = 0;
      			}
      		}
      		else{
      			Extras.beep(3800,200);
      			if(stimLevel>0)
      				stimLevel += DELTA_UP;
      			if(stimLevel<0)
      				stimLevel -= DELTA_UP;
      			correctCount = 0;
      			outputFile.println("incorrect");
      		}
      		outputFile.flush();

      		HashMap<Double,Integer> tmp4 = new HashMap<Double, Integer>();
      		tmp4.put(stimLevel, correctCount);
      		trials.put(stID,tmp4);

      		Thread.sleep(1000);
      	}
      }
    } catch (PixelOutOfRangeException e) {
      e.printStackTrace();
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
    	e.printStackTrace();
    	Thread.currentThread().interrupt();
    } catch (FileNotFoundException e) {
			System.err.println("cannot open data file for writing");
			e.printStackTrace();
		}
    finally {
      closeScreen();
    }
  }
  
  static class Factors{
  	
  	
  }
  
}