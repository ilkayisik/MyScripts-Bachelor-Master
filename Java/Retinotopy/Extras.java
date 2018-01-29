

import static java.lang.Math.PI;
import static java.lang.Math.exp;
import static java.lang.Math.log;
import static java.lang.Math.max;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * Contains methods for some common tasks and purposes
 * 
 * @author boyaci
 * @since 2012-03-14
 */
public class Extras {
  
	/**
	 * Generates a Gabor patch
	 * 
	 * @param mean Mean luminance (pixel value 0-255)
	 * @param cpp Frequency, cycles per pixel
 	 * @param contrast Half the amplitude of the sinusoidal (Michelson Contrast) 
	 * @param phase Phase shift of the sinusoidal
	 * @param sigma Sigma of the Gaussian envelope, affects the diameter of the Gabor
	 * @param orientation Angle of rotation, 0 is vertical
	 * @return Gabor patch in a BufferedImage
	 * 
	 * @throws Exception
	 */
  public static BufferedImage gabor(int mean, double cpp, double contrast,
      double phase, int sigma, double orientation)
      throws PixelOutOfRangeException {
    
    int size = max ( 1,
        2 * (int) (sqrt( 2 * log(mean*contrast)) * sigma) );
    int[] gPixel = new int[size * size];
    
    int[] tmp = new int[size];
    for (int j = 0; j < size; j++) {
      double x = (j - size / 2);
      int val = (int) (contrast * mean * sin(phase + x * cpp * 2 * PI));
      if ((tmp[j] = val) > 255-mean || tmp[j] < -mean)
        throw new PixelOutOfRangeException(
          "Exception in gabor(): calculated pixel value out of range; must be [0,255]");
    }
    for (int i = 0; i < size; i++)
      for (int j = 0; j < size; j++){
        double x2 = (j-size/2) * (j-size/2) + (i-size/2) * (i-size/2);
        gPixel[i * size + j] = mean + (int)(exp(-x2/(2*sigma*sigma))*tmp[j]);
      }
    
    BufferedImage bi = new BufferedImage(size, size,
        BufferedImage.TYPE_BYTE_GRAY);
    bi.getRaster().setPixels(0, 0, size, size, gPixel);
    if(orientation !=0 ){
      AffineTransformOp ato = new AffineTransformOp(AffineTransform
          .getRotateInstance(orientation, (double) size / 2, (double) size / 2),
          AffineTransformOp.TYPE_BICUBIC);
      BufferedImage bic = new BufferedImage(size, size,
          BufferedImage.TYPE_BYTE_GRAY);
      Graphics2D bicG = bic.createGraphics();
      bicG.setColor(new Color(mean,mean,mean));
      bicG.fillRect(0,0,size,size);
      bicG.dispose();
      ato.filter(bi, bic);
      return bic;
    }
    else
      return bi;
  }
  
  /**
   * Opens an output file with the specified prefix followed by the date. 
   * If one or more files exist with the same date, it generates the new file with a version suffix. 
   * 
   * @param prefix For example subject id
   * @return
   * @throws FileNotFoundException
   */
  public static PrintWriter setOutputFile(String prefix) 
    throws FileNotFoundException{

    PrintWriter pw;

    Calendar rightNow = Calendar.getInstance();
    StringBuilder outputID = new StringBuilder();
    outputID.append(prefix+"_");
    outputID.append(rightNow.get(Calendar.YEAR));
    if(rightNow.get(Calendar.MONTH)<10)
      outputID.append(0);
    outputID.append(rightNow.get(Calendar.MONTH)+1);
    if(rightNow.get(Calendar.DAY_OF_MONTH)<10)
      outputID.append(0);
    outputID.append(rightNow.get(Calendar.DAY_OF_MONTH));
    if(rightNow.get(Calendar.HOUR_OF_DAY)<10)
      outputID.append(0);
    outputID.append(rightNow.get(Calendar.HOUR_OF_DAY));
    if(rightNow.get(Calendar.MINUTE)<10)
      outputID.append(0);
    outputID.append(rightNow.get(Calendar.MINUTE));
    File tmp = new File(outputID+".txt");
    int version = 0;
    while(tmp.exists())
      tmp = new File(outputID + "_" + (version++) + ".txt" );
    pw = new PrintWriter(tmp); 
    return pw;
  }
  
  /**
   * Generates a tone
   * 
   * @param hz Affects the pitch of the tone
   * @param msecs Duration of the tone
   * @throws LineUnavailableException
   */
  public static void beep(int hz, int msecs) throws LineUnavailableException {

    byte[] buf = new byte[msecs * 8];
    for (int i = 0; i < buf.length; i++) {
      double angle = i / (8000.0 / hz) * 2.0 * Math.PI;
      buf[i] = (byte) (Math.sin(angle) * 80.0);
    }
    AudioFormat af = new AudioFormat(8000f, 8, 1, true, false);
    SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
    sdl.open(af);
    sdl.start();
    sdl.write(buf, 0, buf.length);
    sdl.drain();
    sdl.close();
  }
}