import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import psychWithJava.FullScreen;

/*
 * FaceExperiment.java
 * 
 * Copyright (C) 2013 Huseyin Boyaci. All Rights Reserved.
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

/**
 * Present stimuli with accurate timing. 1-back task
 * 
 * @author Huseyin Boyaci
 * @since 2013-03-04
 */
public class FaceExperiment extends FullScreen implements Runnable{
	
	final static String TRIGGER = "6";
	
	final static long DURATION_INITIAL_BLANK = 12000l;
	final static long DURATION_FACES_BLOCK = 12000l;
	final static long DURATION_BLANK_BLOCK = 12000l;
	final static long DURATION_FINAL_BLANK = 10000l;
	final static long DURATION_FACES_FLICK = 300l;
	final static long DURATION_BLANK_FLICK = 700l;
	final static int N_REP = 5;
	
	final static String[] FILENAMES_PICTURES = {"face1.jpg",
		"face2.jpg", "face3.jpg", "face4.jpg"};
	
	public static void main(String[] s){
		FaceExperiment fe = new FaceExperiment();
		Thread experiment = new Thread(fe);
		experiment.run();
	}

	@Override
	public void run() {
		
		setNBuffers(2);
		
		try{
			
			BufferedImage[] faces = new BufferedImage[FILENAMES_PICTURES.length];
			
			for(int i=0; i<faces.length; i++)
				faces[i]= ImageIO.read(
						FaceExperiment.class.getResource(FILENAMES_PICTURES[i]));
		
			blankScreen();
			displayText(10,50,"Stimulus program " + getClass().getName() + " ready to start ....");
			updateScreen();
						
			long timeLeftInBlock;

			while(!getKeyTyped(-1).getKey().equals(TRIGGER));
			
			long bigStart = System.currentTimeMillis();
			
			// The initial blank
			blankScreen();
			updateScreen();
			Thread.sleep(
					Math.max(0, 
							DURATION_INITIAL_BLANK - (System.currentTimeMillis() - bigStart)));
			for(int i=0; i<N_REP; i++){
				
				// faces block
				timeLeftInBlock = DURATION_FACES_BLOCK;
				int index;
				int indexPrev = -1;
				boolean match;
				
				while(timeLeftInBlock>0){
			
					// face
					long flickStart = System.currentTimeMillis();
					index = (int)Math.floor(Math.random() * faces.length);
					blankScreen();
					displayImage(faces[index]);
					updateScreen();
					flushKeyPressed();
					long trialStart = System.currentTimeMillis();
		
					Thread.sleep(Math.max(0, DURATION_FACES_FLICK - 
							(System.currentTimeMillis() - flickStart)));
					
					// blank
					flickStart = System.currentTimeMillis();
					blankScreen();
					updateScreen();
					Thread.sleep(Math.max(0, DURATION_BLANK_FLICK - 
							(System.currentTimeMillis() - flickStart)));

					if(indexPrev != -1){
						
						if(index == indexPrev)
							match = true;
						else 
							match = false;

						KeyPressed kp = getKeyPressed();
						if( kp != null){
							if(kp.getKey()==KeyEvent.VK_1 && match)
								System.out.printf("%d %d \n",1 , kp.getWhen() - trialStart);
							else if (kp.getKey()==KeyEvent.VK_0 && !match)
								System.out.printf("%d %d \n",1 , kp.getWhen() - trialStart);
							else 
								System.out.printf("%d %d \n", 0, kp.getWhen() - trialStart );
						} else {
							System.out.printf("%d %d \n",0,0);
						}
					}
					
					indexPrev = index;
					
					timeLeftInBlock = 
							DURATION_INITIAL_BLANK + 
							i * (DURATION_FACES_BLOCK+DURATION_BLANK_BLOCK) +
							DURATION_FACES_BLOCK -
							(System.currentTimeMillis() - bigStart);
									
				}
				
				System.out.println();
				
				// Rest block
				blankScreen();
				updateScreen();
				timeLeftInBlock = 
						DURATION_INITIAL_BLANK + 
						(i+1) * (DURATION_FACES_BLOCK+DURATION_BLANK_BLOCK) -
						(System.currentTimeMillis() - bigStart);
				Thread.sleep(Math.max(0, timeLeftInBlock));
				
			}
			
			// final blank
			blankScreen();
			updateScreen();
			timeLeftInBlock = 
					DURATION_INITIAL_BLANK + 
					(N_REP) * (DURATION_FACES_BLOCK+DURATION_BLANK_BLOCK) 
					+ DURATION_FINAL_BLANK -
					(System.currentTimeMillis() - bigStart);
			Thread.sleep(Math.max(0, timeLeftInBlock));

			// report the timing mismatch; should ideally be zero
			blankScreen();
			displayText("Time mismatch = " + 
					String.valueOf(System.currentTimeMillis() - bigStart - 
							(DURATION_INITIAL_BLANK
									+N_REP*(DURATION_FACES_BLOCK+DURATION_BLANK_BLOCK)
									+DURATION_FINAL_BLANK)));
			updateScreen();
			Thread.sleep(2000);
			
		} catch (IOException e) {
			System.err.println("File not found");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} finally {
			closeScreen();
		}
	}
}
