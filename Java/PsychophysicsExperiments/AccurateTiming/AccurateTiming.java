import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import psychWithJava.FullScreen;

/*
 * AccurateTiming.java
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
 * Present stimuli with accurate timing.
 * 
 * @author Huseyin Boyaci
 * @since 2013-02-28
 */
public class AccurateTiming extends FullScreen implements Runnable {
	
	static final long FLICK_RATE = 100;
	static final long ACTIVE_BLOCK = 1000;
	static final long BASELINE_BLOCK = 700;
	static final int N_REPEAT = 10;
	
	static final String TRIGGER = "6";
	
	static final String[] LETTERS = {"X","Y","Z","M","N"};
	/*
	public static void main(String args[]){
		
		AccurateTiming at = new AccurateTiming();
		Thread experiment = new Thread(at);
		experiment.start();
	}
*/
	@Override
	public void run() {

		setNBuffers(2);
		
		try{
			
			blankScreen();
			displayText(10,50,"Stimulus program " + getClass().getName() + " ready to start ....");
			updateScreen();
			
			setFont(new Font("SansSerif", Font.BOLD, 60));
			
			while(!getKeyTyped(-1).getKey().equals(TRIGGER));
			 
			long bigStart = System.currentTimeMillis();
			
			for (int j=0; j < N_REPEAT; j++){
				
				// first block - active
				long activeBlockTimeLeft = ACTIVE_BLOCK;
				int i = 0;
				while(activeBlockTimeLeft>0){
					long flickStartTime = System.currentTimeMillis();
					blankScreen();
					displayText(LETTERS[i++%LETTERS.length]);
					updateScreen();
					Thread.sleep(Math.max(0, FLICK_RATE - 
							(System.currentTimeMillis() - flickStartTime)));
					activeBlockTimeLeft = j*(ACTIVE_BLOCK+BASELINE_BLOCK) + ACTIVE_BLOCK - (System.currentTimeMillis() - bigStart);
				}

				// second block - baseline
				blankScreen();
				updateScreen();
				long baselineTimeLeft = (j+1)*(ACTIVE_BLOCK+BASELINE_BLOCK) - (System.currentTimeMillis() - bigStart);
				Thread.sleep(baselineTimeLeft);
			}
			
			blankScreen();
			displayText("Total mismatch in time: " + String.valueOf( 
					N_REPEAT*(ACTIVE_BLOCK+BASELINE_BLOCK) - (System.currentTimeMillis()-bigStart)) + "ms");
			updateScreen();
			Thread.sleep(2000);
			
		} catch (InterruptedException e) {
      Thread.currentThread().interrupt();
			e.printStackTrace();
		} finally {
			closeScreen();
		}
	}
}
