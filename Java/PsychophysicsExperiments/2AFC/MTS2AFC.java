import static java.lang.Math.PI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.sound.sampled.LineUnavailableException;

import psychWithJava.Extras;
import psychWithJava.FullScreen;
import psychWithJava.PixelOutOfRangeException;

public class MTS2AFC extends FullScreen implements Runnable{
	private static final long serialVersionUID = 1L;

  /** pixel value of the background */
  static final int MEAN = 127;  
  /** frequency of the sinusoidal grating, in cycles per pixel */
  static final double CPP = 0.02;
  /** determines the horizontal shift of the sinusoidal grating */
  static final double PHASE = 0; 
  static final double ONE_DEGREE = PI/180;
  /** orientations, vertical = 0 */
  static final double[] ORIENTATION = {
		  0.25*ONE_DEGREE,-0.25*ONE_DEGREE,
		  0.5*ONE_DEGREE,-0.5*ONE_DEGREE,
		  0.75*ONE_DEGREE,-0.75*ONE_DEGREE,
		  ONE_DEGREE,-ONE_DEGREE,
		  1.25*ONE_DEGREE,-1.25*ONE_DEGREE,
		  1.5*ONE_DEGREE,-1.5*ONE_DEGREE,
		  1.75*ONE_DEGREE,-1.75*ONE_DEGREE,
		  2*ONE_DEGREE,-2*ONE_DEGREE,
		  4.0*ONE_DEGREE,-4.0*ONE_DEGREE,
		  6*ONE_DEGREE,-6*ONE_DEGREE,
		  };
  /** determines the spread of the Gaussian envelope */
  static final int SIGMA = 50;
  static final long ISI = 1000l;
  /* Experimental procedure parameters */
  static final double CONTRAST = 0.5;
  static final int N_TRIALS = 50;
  
  static PrintWriter outputFile;
  
  /* public static void main(String[] args){
    
    // Open the output file for saving the data
    try {
      outputFile = Extras.setOutputFile("data");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    
    MTS2AFC gd = new MTS2AFC();
    new Thread(gd).start();
  }
*/
  public void run() {

    try{
    
      setNBuffers(2);
      setBackground(new Color(MEAN,MEAN,MEAN));

      setFont(new Font("SansSerif", Font.BOLD, 26));
      displayText(100, 200, "Task:");
      displayText(100, 300, "Indicate the Gabor in the second interval");
      displayText(100, 350, "matching the one in the first interval");    
      displayText(100, 400, "Use arrow keys: left or right");
      displayText(100, 500, "Now press any key to continue");
      updateScreen();
      getKeyPressed(-1);
      blankScreen();
      updateScreen();
      hideCursor();
            
      /* First put the elements of ORIENTATION in an ArrayList object */
      ArrayList<Double> orientations = new ArrayList<Double>();
      for(double o: ORIENTATION)
    	  orientations.add(o);
      
      for(int trial=0; trial<N_TRIALS; trial++){

      	/* Next shuffle the elements. Do this at every repetition (N_TRIALS times) */
    	  Collections.shuffle(orientations);
    	
    	  /* Then construct an Iterator with the shuffled ArrayList */
    	  Iterator<Double> it = orientations.iterator();
    	
    	  /* Next one by one get elements from that iterator, until no elements left */
      	while(it.hasNext()){
      		
      		double stimulusLevel = it.next();
      		
      		outputFile.printf("%3.5f ",stimulusLevel);

      		BufferedImage gbr1 = Extras.gabor(MEAN, CPP, CONTRAST, PHASE, SIGMA, stimulusLevel);
      		BufferedImage gbr2 = Extras.gabor(MEAN, CPP, CONTRAST, PHASE, SIGMA, -stimulusLevel);

      		BufferedImage sample;
      		if (Math.random() > 0.5)
      			sample = gbr1;
      		else
      			sample = gbr2;
      		
//      	BufferedImage sample = (Math.random() > 0.5)?gbr1:gbr2;
      		
      		int y = (getHeight() - gbr1.getHeight())/2;
      		int x1 = getWidth()/4 - gbr1.getWidth()/2;
      		int x2 = 3*getWidth()/4 - gbr1.getWidth()/2;
      		
      		blankScreen();
      		displayImage(sample);
      		updateScreen();
      		Thread.sleep(250);
      		
      		blankScreen();
      		updateScreen();
      		Thread.sleep(ISI);
      		
      		blankScreen();
      		displayImage(x1,y,gbr1);
      		displayImage(x2,y,gbr2);
      		displayText ("+");
      		updateScreen();
      		flushKeyPressed();

      		Thread.sleep(250);
      		blankScreen();
      		updateScreen();
      		
      		int response = getKeyPressed(-1).getKey();
      		
      		/* wait until a left or right arrow is pressed */
      		while(!(response== KeyEvent.VK_LEFT || response==KeyEvent.VK_RIGHT))
      			response=getKeyPressed(-1).getKey();
      		
      		if((response == KeyEvent.VK_LEFT && sample == gbr1) ||
      				(response == KeyEvent.VK_RIGHT && sample == gbr2) ){
      			Extras.beep(300,200);
      			outputFile.println("1");

      		}
      		else{
      			Extras.beep(3800,200);
      			outputFile.println("0");
      		}
      		outputFile.flush();
      		
      		Thread.sleep(1000);
      	}
      }
    } catch (PixelOutOfRangeException e) {
      e.printStackTrace();
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Thread.currentThread().interrupt();
	}
    finally {
      closeScreen();
    }
  }
}