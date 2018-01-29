import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import psychWithJava.FullScreen;

/*
 * FaceObjectExperiment.java
 * 
 * This program was written for the Requirements of the Experimental Psychology Course (PSYC 405)
 * by  Ayse Ilkay Isik
 * 
 * 11.03.2013
 * 
 */

/**
 *  Present Faces vs. objects in a block design experiment. 1-back task
 * 
 */
public class FaceObjectExperiment extends FullScreen implements Runnable{
	private static final long serialVersionUID = 1L;

	final static String TRIGGER = "6";
	
	final static long DURATION_INITIAL_BLANK = 20000l;
	final static long DURATION_FACES_BLOCK = 12000l;
	final static long DURATION_REST_BLOCK = 12000l;
	final static long DURATION_OBJECTS_BLOCK = 12000;
	final static long DURATION_FINAL_BLANK = 10000l;
	final static long DURATION_FACES_FLICK = 300l;
	final static long DURATION_OBJECTS_FLICK = 300l;
	final static long DURATION_FACEBLANK_FLICK = 700l;
	final static long DURATION_OBJECTBLANK_FLICK = 700l;
	final static int N_REP = 5;
	
	final static String[] FILENAMES_FACE_PICTURES = {"myFacePicture1.jpg","myFacePicture2.jpg", "myFacePicture3.jpg", "myFacePicture4.jpg"};
	
	final static String[] FILENAMES_OBJECT_PICTURES = {"myObjectPicture1.jpg", "myObjectPicture2.jpg", "myObjectPicture3.jpg", "myObjectPicture4.jpg"};
	
	public static void main(String[] s){
		FaceObjectExperiment foe = new FaceObjectExperiment();
		Thread experiment = new Thread(foe);
		experiment.run();
	}
	
	public void run() {
		
		setNBuffers(2);
		
		try{
			
			BufferedImage[] faces = new BufferedImage[FILENAMES_FACE_PICTURES.length];
			BufferedImage[] objects = new BufferedImage[FILENAMES_OBJECT_PICTURES.length];
			
			for(int i=0; i<faces.length; i++)
				faces[i]= ImageIO.read(
						FaceObjectExperiment.class.getResource(FILENAMES_FACE_PICTURES[i]));
			for(int i=0; i<objects.length; i++)
				objects[i]= ImageIO.read(
						FaceObjectExperiment.class.getResource(FILENAMES_OBJECT_PICTURES[i]));
		
			blankScreen();
			displayText(5,50,"Stimulus program " + getClass().getName() + " ready to start...");
			updateScreen();
			long timeLeftInBlock;

			while(!getKeyTyped(-1).getKey().equals(TRIGGER));
			
			long bigStart = System.currentTimeMillis();
			
			// The initial blank
			blankScreen();
			updateScreen();
			Thread.sleep(
					Math.max(0, DURATION_INITIAL_BLANK - (System.currentTimeMillis() - bigStart)));
			for(int i=0; i<N_REP; i++){
				
				// First Block _ Faces
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
					
					//Face blank
					flickStart = System.currentTimeMillis();
					blankScreen();
					updateScreen();
					Thread.sleep(Math.max(0, DURATION_FACEBLANK_FLICK - 
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
							i * (DURATION_FACES_BLOCK+2*DURATION_REST_BLOCK+DURATION_OBJECTS_BLOCK) +
							DURATION_FACES_BLOCK -
							(System.currentTimeMillis() - bigStart);
									
				}
				
				System.out.println();
				
				// Rest block
				blankScreen();
				updateScreen();
				timeLeftInBlock = 
						DURATION_INITIAL_BLANK + 
						i * (DURATION_FACES_BLOCK+2*DURATION_REST_BLOCK+DURATION_OBJECTS_BLOCK) +
						(DURATION_FACES_BLOCK+DURATION_REST_BLOCK) -
						(System.currentTimeMillis() - bigStart);
				Thread.sleep(Math.max(0, timeLeftInBlock));
				
			
				//  objects block
				timeLeftInBlock = DURATION_OBJECTS_BLOCK;
				indexPrev = -1;
				
				while(timeLeftInBlock>0){
			
					// object
					long flickStart = System.currentTimeMillis();
					index = (int)Math.floor(Math.random() * objects.length);
					blankScreen();
					displayImage(objects[index]);
					updateScreen();
					flushKeyPressed();
					long trialStart = System.currentTimeMillis();
		
					Thread.sleep(Math.max(0, DURATION_OBJECTS_FLICK - 
							(System.currentTimeMillis() - flickStart)));
					
					// objectblank
					flickStart = System.currentTimeMillis();
					blankScreen();
					updateScreen();
					Thread.sleep(Math.max(0, DURATION_OBJECTBLANK_FLICK - 
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
					
					timeLeftInBlock = DURATION_INITIAL_BLANK +
							(i+1) * (DURATION_FACES_BLOCK+2*DURATION_REST_BLOCK+DURATION_OBJECTS_BLOCK)
							- DURATION_REST_BLOCK -
							(System.currentTimeMillis() - bigStart);
									
				}
				
				System.out.println();
				// Rest block
				blankScreen();
				updateScreen();
				timeLeftInBlock = 
						DURATION_INITIAL_BLANK + 
						(i+1) * (DURATION_FACES_BLOCK+DURATION_REST_BLOCK +
								 DURATION_OBJECTS_BLOCK+DURATION_REST_BLOCK) -
						(System.currentTimeMillis() - bigStart);
				Thread.sleep(Math.max(0, timeLeftInBlock));
			}	
			
			// final blank
			blankScreen();
			updateScreen();
			
			timeLeftInBlock = 
					DURATION_INITIAL_BLANK + 
					(N_REP) * (DURATION_FACES_BLOCK+DURATION_REST_BLOCK+ DURATION_OBJECTS_BLOCK+DURATION_REST_BLOCK) 
					+ DURATION_FINAL_BLANK -
					(System.currentTimeMillis() - bigStart);
			Thread.sleep(Math.max(0, timeLeftInBlock));
	
			// report the timing mismatch; should ideally be zero
			blankScreen();
			displayText("Time mismatch = " + 
					String.valueOf(System.currentTimeMillis() - bigStart - 
							(DURATION_INITIAL_BLANK + 
									(N_REP) * (DURATION_FACES_BLOCK+DURATION_REST_BLOCK+ DURATION_OBJECTS_BLOCK+DURATION_REST_BLOCK) 
									+ DURATION_FINAL_BLANK)));
			updateScreen();
			Thread.sleep(2000);
		
		} catch (IOException e) {
			System.err.println("File not found");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} finally  {
			closeScreen();
		}
	}

}

	
	

