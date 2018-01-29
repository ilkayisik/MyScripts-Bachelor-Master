/*
 * HelloPsychophysicist.java
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

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import psychWithJava.FullScreen;

/**
 * Displays the text "Hello Psychophysicist" 
 * and two images on an otherwise entirely blank screen
 * 
 * @author Huseyin Boyaci
 * @since 2012-02-28
 */
public class HelloPsychophysicist extends FullScreen implements Runnable {
  
  public static void main(String[] args) {

    HelloPsychophysicist fs = new HelloPsychophysicist();
    fs.setNBuffers(2);
    Thread experiment = new Thread(fs);
    experiment.start();
  }
  
  public void run() {

    try {
      blankScreen();
      displayText("Hello Psychophysicist");
      updateScreen();
      Thread.sleep(2000);
      blankScreen();
      hideCursor();
      BufferedImage bi1 = ImageIO.read(
          HelloPsychophysicist.class.getResource("psychophysik.png"));
      displayImage(bi1);
      updateScreen();
      Thread.sleep(2000);
      blankScreen();
      BufferedImage bi2 = ImageIO.read(
          HelloPsychophysicist.class.getResource("fechner.png"));
      displayImage(0,0,bi2);
      updateScreen();
      Thread.sleep(2000);
    } catch (IOException e) {
      System.err.println("Image File not found");
      e.printStackTrace();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    finally {
      closeScreen();
    }
  }
}