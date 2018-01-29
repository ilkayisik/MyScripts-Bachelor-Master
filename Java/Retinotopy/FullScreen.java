

/*
 * FullScreen.java
 * 
 * Modified:2010-12-15
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
 * 
 */

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;

/**
 * <code>FullScreen</code> class provides methods to display visual stimuli
 * and interact with the observer in full screen exclusive mode (FSEM).
 * <P>
 * <code>FullScreen</code> is capable of chosing the best available display 
 * mode and
 * strategy on a particular software (OS) and hardware. By default the 
 * user does not
 * have to change any of these strategies. However it provides
 * methods for expert users who wish to modify those strategies. Requires Java
 * SE 5.0 or higher.
 * <P>
 * Methods provided include: <code>displayImage()</code> to display images,
 * <code>displayText()</code> to display text, and
 * <code>getKeyPressed()</code> to get observer's keyboard responses. Because it
 * is inherited from the Java core class <code>JFrame</code>, all its methods
 * are available, such as <code>setForeground()</code>.
 * <P>
 * See
 * <a href="../../../index.html">Psychophysics Programming with Java</a>
 * for more information and sample demo programs.
 * <P>
 * <code>FullScreen</code> class utilizes the 
 * Full screen exclusive mode (FSEM) is a feature of Java (after J2SE 1.4) 
 * that allows
 * programmers to suspend the windowing system of the underlying OS, and
 * directly access the video card and draw on the screen. If exclusive full
 * screen is not supported, a regular window is positioned at upper left corner
 * (0,0) and resized
 * to fit the whole screen to mimic full screen exclusive mode. Whether in full
 * screen exclusive mode or not, <code>FullScreen</code> uses active
 * rendering as opposed to passive rendering - in passive rendering the
 * underlying OS may intervene and send directives to the rendering program,
 * whereas in active rendering the program itself is responsible of drawing and
 * re-drawing the contents on the screen without the intervention of 
 * OS's directives.
 * <P>
 * For more details, developers can see JSE API definitions at <a
 * href="http://java.sun.com">http://java.sun.com</a>. See also 
 * <a href="http://java.sun.com/products/java-media/2D/index.jsp">
 * Java 2D API</a>,
 * <a
 * href="http://java.sun.com/docs/books/tutorial/extra/fullscreen/example.html">
 * Full-Screen Exclusive Mode API tutorial</a>, <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/2d/spec/j2d-bookTOC.html">
 * Programmer's Guide to the Java 2D API</a>, 
 * <a href="http://java.sun.com/products/jfc/tsc/articles/painting/index.html">
 * Painting in AWT and Swing</a>, 
 * <a href="http://java.sun.com/docs/books/tutorial/2d/index.html"> 
 * Trail: 2D Graphics</a>, and <a
 * href="http://java.sun.com/j2se/1.5.0/docs/guide/2d/new_features.html">
 * Java 2D new features in J2SE 5.0</a>.
 * <P>
 * Note: Java 2D API (and rest of the entire platform) is improved with JSE 5.0,
 * and the <code>FullScreen</code> requires at least edition 5.0. 
 * Java SE 5.0 and 6.0 is available on all
 * platforms (Linux, Mac OS X (only Tiger and later), and MS Windows).
 * <P>
 * Note: Real full screen exclusive mode is not supported on Linux in Java SE
 * 5.0, but it simulates the exclusive mode. However this situation is fixed
 * in edition 6. Now FSEM
 * on Linux works fine especially with opengl pipeline enabled. 
 * To enable opengl pipeline user must specify the following 
 * system property on the command line: 
 * <code>-Dsun.java2d.opengl=True</code>. More information is available 
 * at <a href="../../../index.html">Psychophysics programming with Java</a>.
 * <P>
 * <b>Matlab and Mathematica development:</b>
 * <br>
 * It is possible to create Java objects from within Matlab and Mathematica. You
 * can, therefore, create a Java object in Matlab or Mathematica and
 * invoke its methods. In other words, if you choose, you could use this package
 * with Matlab or Mathematica as a tool for psychophysics programming in a way much 
 * like the well known "Psychtoolbox" package.
 * See <a href="../../../index.html">
 * Psychophysics Programming with Java</a> for more information.
 * 
 * @see NormalWindow
 * @see BitsPP
 * 
 * @author Huseyin Boyaci
 */
public class FullScreen extends JFrame implements KeyListener {
  
  private static final GraphicsEnvironment gEnvironment = GraphicsEnvironment
      .getLocalGraphicsEnvironment();
  private static final GraphicsDevice[] gDevices = gEnvironment
      .getScreenDevices();
  private GraphicsDevice gDevice;
  private GraphicsConfiguration gConfiguration;
  private final static int defaultNBuffers = 1;
  private final static Color defaultBgColor = Color.BLACK;
  private final static Color defaultFgColor = Color.LIGHT_GRAY;
  private final Font defaultFont = new Font("SansSerif", Font.BOLD, 36);
  private int nBuffers;
  private Color bgColor;
  private DisplayMode oldDisplayMode;
  private DisplayMode displayMode;
  private BlockingQueue<KeyTyped> keyTyped;
  private BlockingQueue<KeyPressed> keyPressed;
  private BlockingQueue<KeyReleased> keyReleased;
  
  private BufferStrategy bs; 
  
  /**
   * A class used to handle key typing events. 
   * 
   * 
   * @author boyaci
   * 
   * @param key The key that is typed (String)
   * @param when Time of the key type event (Long) 
   * 
   * @see KeyTyped
   * @see KeyEvent
   */
  public class KeyTyped {
    String key;
    Long when;
    
    public KeyTyped(String key, Long when){
      this.key = key;
      this.when = when;
    }
    
    /**
     * 
     * @return The key typed by the user
     */
    public String getKey(){
      return key;
    }
    
    /**
     * 
     * @return Time of the key typing event
     */
    public Long getWhen(){
      return when;
    }

  }
  
  /**
   * A class used to handle key pressing events. 
   * 
   * 
   * @author boyaci
   * 
   * @param key The code of the key that is pressed (Integer)
   * @param when Time of the key press event (Long) 
   * 
   * @see KeyPressed
   * @see KeyEvent
   */
  public class KeyPressed {
    
    Integer key;
    Long when;
    
    public KeyPressed(Integer key, Long when){
      this.key = key;
      this.when = when;
    }
    
    /**
     * 
     * @return The code of the key that is pressed 
     * 
     */
    public Integer getKey(){
      return key;
    }
    
    /**
     * 
     * @return Time of the key pressing event
     */
    public Long getWhen(){
      return when;
    }

  }

  /**
   * A class used to handle key releasing events. 
   * 
   * 
   * @author boyaci
   * 
   * @param key The code of the key that is released (Integer)
   * @param when Time of the key release event (Long) 
   * 
   * @see KeyReleased
   * @see KeyEvent
   */
  public class KeyReleased {
    
    Integer key;
    Long when;
    
    public KeyReleased(Integer key, Long when){
      this.key = key;
      this.when = when;
    }
    
    /**
     * 
     * @return The code of the key that is released 
     * 
     */
    public Integer getKey(){
      return key;
    }
    
    /**
     * 
     * @return Time of the key releasing event
     */
    public Long getWhen(){
      return when;
    }

  }

  public void keyTyped(KeyEvent ke) {

    keyTyped.offer(new KeyTyped(String.valueOf(ke.getKeyChar()),ke.getWhen()));    
  }

  public void keyReleased(KeyEvent ke) {

    keyReleased.offer(new KeyReleased(ke.getKeyCode(),ke.getWhen()));
  }

  public void keyPressed(KeyEvent ke) {

    Integer key = ke.getKeyCode();
    Long when = ke.getWhen();
    if (key == KeyEvent.VK_ESCAPE) {
      closeScreen();
      System.exit(0);
    }
    else {
      keyPressed.offer(new KeyPressed(key,when));
    }
  }
  
  /**
   * Constructs a FullScreen object on the screen designated with 
   * displayID. By default it uses a single video buffer and the current 
   * display mode of the client's OS. 
   * Once it is constructed, FullScreen captures the entire screen
   * immediately.  At the end user's program must
   * terminate this mode and go back to system's default display. 
   * To do this use the method <code>closeScreen()</code>.
   *
   * @param displayID a numerical id indicating the screen device 
   * 
   * @see #setDisplayMode(DisplayMode)
   * @see #setNBuffers(int)
   * @see #closeScreen()
   *
   */
  public FullScreen(int displayID) {

    super(gDevices[displayID].getDefaultConfiguration());
    // For Mac OS X - set true if you don't want FSEM 
    // expand over all the displays
    // apple.awt.fullscreencapturealldisplays = false;
    gDevice = gDevices[displayID];
    gConfiguration = gDevice.getDefaultConfiguration();
    oldDisplayMode = gDevice.getDisplayMode();
    displayMode = gDevice.getDisplayMode();
    try {
      
      //setResizable(false);
      setUndecorated(true);
      setIgnoreRepaint(true);
      
      setBackground(defaultBgColor);
      super.setBackground(defaultBgColor);
      setFont(defaultFont);
      setForeground(defaultFgColor);
      
      setBounds(gConfiguration.getBounds());
      gDevice.setFullScreenWindow(this);

      setNBuffers(defaultNBuffers);
      
      keyTyped = new LinkedBlockingQueue<KeyTyped>();
      keyPressed = new LinkedBlockingQueue<KeyPressed>();
      keyReleased = new LinkedBlockingQueue<KeyReleased>();
      addKeyListener(this);
    }
    finally {}
  }
  
  /**
   * 
   * Constructs a FullScreen object on the default screen.
   * By default it uses a single video buffer and the current 
   * display mode. Once it is constructed, FullScreen captures the entire screen
   * immediately. At the end user's program must
   * terminate this mode and go back to system's default display. 
   * To do this use the method <code>closeScreen()</code>.
   * 
   * @see #setDisplayMode(DisplayMode)
   * @see #setNBuffers(int)
   * @see #closeScreen()
   */
  public FullScreen() {

    this(0);
  }
  
  /**
   * Sets number of video buffers (including the front (visible) one). 
   * If the number is 1, it implies no back buffer. 2 implies
   * one front one back buffer (double buffering).
   * 
   * @param n number of buffers requested; must not be less than 1
   * 
   * @see #getNBuffers()
   */
  public void setNBuffers(int n) {

    try {
      createBufferStrategy(n);
      nBuffers = n;
    } catch (IllegalArgumentException e) {
      System.err.println("Exception in FullScreen.setNBuffers(): "
          + "requested number of Buffers is illegal - falling back to default");
      createBufferStrategy(defaultNBuffers);
    }
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } 
    bs = getBufferStrategy();
  }
  
  /**
   * Returns number of video buffers (including the front (visible) one) 
   * currently used
   * by this FullScreen object.
   * 
   * @return number of video buffers used by this FullScreen object
   * 
   * @see #setNBuffers(int)
   */
  public int getNBuffers() {

    return nBuffers;
  }
  
  /**
   * Updates the entire screen by bringing the back video buffer front (if there
   * exists a back buffer, if not it has no effect). 
   * The methods in FullScreen class (displayImage(), displayText() 
   * or blankScreen(), see below) always manipulate the video bufer, not
   * necessarily the screen.
   * In case there is a back video buffer those methods affect only the 
   * back (invisible) buffer. After invoking those methods 
   * user has to invoke the updateScreen() method to actually 
   * bring the back buffer to front, in other words to make it actually visible
   * on the screen device.
   * 
   * @see #displayImage(int, int, BufferedImage)
   * @see #displayText(int, int, String)
   * @see #blankScreen()
   *
   */
  public void updateScreen() {

    // Note: this and displayImage() method is problematic. 
    // I am not sure how to handle contentsLost and Restored
    // situation when the rendering and screen update happen
    // in two separate methods.
    //do{
      //Toolkit.getDefaultToolkit().sync();
      //bs.show();
      //System.err.println("ContentsLost? " + bs.contentsLost());
      // if the contents of bs is recently lost, we should repeat
      // this process
      // here there should be a redraw in case if bs is restored
      // I cannot test these because on Linux the contents doesnt
      // ever get lost (no true volatile back/front buffer)!
    //}while(bs.contentsLost());
    
    // temporary solution: show only if the contents not lost
    if(!bs.contentsLost())
      bs.show();
  }
  
  /**
   * Displays a BufferedImage at the center of the screen.
   * Note that, in case there is a back video buffer
   * this method draws the image on the back buffer. In that case  
   * user has to invoke the updateScreen() method 
   * to actually display the image on the screen.
   * 
   * @param bi BufferedImage to display
   * 
   * @see #displayImage(int, int, BufferedImage)
   * @see #updateScreen()
   */
  public void displayImage(BufferedImage bi) {

    if (bi != null) {
      double x = (getWidth() - bi.getWidth()) / 2;
      double y = (getHeight() - bi.getHeight()) / 2;
      displayImage((int) x, (int) y, bi);
    }
  }
  
  /**
   * Displays a BufferedImage at the specified position.
   * Note that, in case there is a back video buffer
   * this method draws the image on the back buffer. In that case  
   * user has to invoke the updateScreen() method 
   * to actually display the image on the screen.
   * 
   * @param x horizontal offset of the upper left corner of the image from the
   *          upper left corner of the screen
   * @param y vertical offset of the upper left corner of the image from the
   *          upper left corner of the screen
   * @param bi BufferedImage to display
   * 
   * @see #displayImage(BufferedImage)
   * @see #updateScreen()
   */
  public void displayImage(int x, int y, BufferedImage bi) {

    do {
      do{
        Graphics2D g = (Graphics2D)bs.getDrawGraphics();
        try {
          if (g != null && bi != null)
            g.drawImage(bi, x, y, null);
        }
        finally {
          g.dispose();
        }
        //System.err.println("ContentsRestored? " + bs.contentsRestored());
      }while(bs.contentsRestored());
      //System.err.println("ContentsLost? " + bs.contentsLost());
      // We probably don't need contentsLost check here?
    }while(bs.contentsLost());
  }
  
  /**
   * Displays text at the center of the screen.
   * Note that, in case there is a back video buffer
   * this method draws the text on the back buffer. In that case  
   * user has to invoke the updateScreen() method 
   * to actually display the text on the screen.
   * 
   * @param text a text message to display
   * 
   * @see #displayText(int, int, String)
   * @see #updateScreen()
   */
  public void displayText(String text) {

    Graphics2D g = (Graphics2D) bs.getDrawGraphics();
    if (g != null && text != null) {
      Font font = getFont();
      g.setFont(font);
      FontRenderContext context = g.getFontRenderContext();
      Rectangle2D bounds = font.getStringBounds(text, context);
      double x = (getWidth() - bounds.getWidth()) / 2;
      double y = (getHeight() - bounds.getHeight()) / 2;
      double ascent = -bounds.getY();
      double baseY = y + ascent;
      displayText((int) x, (int) baseY, text);
    }
    g.dispose();
  }
  
  /**
   * Displays text at the specified position.
   * Note that, in case there is a back video buffer
   * this method draws the text on the back buffer. In that case  
   * user has to invoke the updateScreen() method 
   * to actually display the text on the screen.
   * 
   * @param x horizontal offset of the upper left corner of the text from the
   *          upper left corner of the screen
   * @param y vertical offset of the upper left corner of the text from the
   *          upper left corner of the screen
   * @param text a text message to display
   * 
   * @see #displayText(String)
   * @see #updateScreen()
   */
  public void displayText(int x, int y, String text) {

    do{
      do{
        Graphics g = bs.getDrawGraphics();
        try{
          if (g != null && text != null) {
            g.setFont(getFont());
            g.setColor(getForeground());
            g.drawString(text, x, y);          
          }
        }
        finally {
          g.dispose();
        }
        //System.err.println("ContentsRestored? " + bs.contentsRestored());
      }while(bs.contentsRestored());
      //System.err.println("ContentsLost? " + bs.contentsLost());
    }while(bs.contentsLost());
  }
  
  /**
   * Blanks the whole screen using the current background color.
   * Note that, in case there is a back video buffer
   * this method blanks the back buffer. In that case  
   * user has to invoke the updateScreen() method 
   * to actually blank the screen.
   * 
   * @see #updateScreen()
   *
   */
  public void blankScreen() {

    do {
      do{
        Graphics2D g = (Graphics2D)bs.getDrawGraphics();
        try {
          if (g != null){
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
          }
        }
        finally {
          g.dispose();
        }
        //System.err.println("ContentsRestored? " + bs.contentsRestored());
      }while(bs.contentsRestored());
      //System.err.println("ContentsLost? " + bs.contentsLost());
    }while(bs.contentsLost());
  }
  
  /**
   * Returns the current background color.
   * 
   * @return current background color
   * 
   * @see #setBackground(Color)
   * 
   */
  public Color getBackground() {

    return bgColor;
  }
  
  /**
   * Sets the background color.
   * 
   * @param bg new background color
   * 
   * @see #getBackground()
   */
  public void setBackground(Color bg) {

    bgColor = bg;
  }
  
  /** 
   * Renders the cursor invisible.
   * 
   * @see #showCursor()
   *
   */
  public void hideCursor() {

    Cursor noCursor = null;
    Toolkit tk = Toolkit.getDefaultToolkit();
    Dimension d = tk.getBestCursorSize(1, 1);
    if ((d.width | d.height) != 0)
      noCursor = tk.createCustomCursor(new BufferedImage(d.width, d.height,
          BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "noCursor");
    setCursor(noCursor);
  }
  
  /**
   * Renders the cursor visible using default cursor
   * 
   * @see #hideCursor()
   *
   */
  public void showCursor() {

    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
  }
    
  /**
   * Closes the full screen exclusive mode screen. 
   * This method performs also the
   * following additional steps: 
   * It switches the resolution back to system setting (if it was altered), 
   * releases all the screen resources to the operating system.
   * User must invoke this method to re-gain access to normal desktop.
   * 
   * @see #FullScreen(int)
   * @see #FullScreen()
   * 
   */
  public void closeScreen() {

    if (gDevice.isDisplayChangeSupported())
      setDisplayMode(oldDisplayMode);
    gDevice.setFullScreenWindow(null);
    dispose();
  }
  
  /**
   * Returns a KeyTyped object, which contains the key typed and when it is typed 
   * by the observer within <code>ms</code> milliseconds.
   * <P>
   * If <code>ms</code> is positive: 
   * <ul>(i) returns the top element in the
   * keyTyped queue immediately if there is 
   * at least one element in the keyTyped event queue or
   * <br>(ii) waits up to the specified amount of time, <code>ms</code> for an element 
   * to become available. 
   * <br> (iii) If no key is typed
   * within <code>ms</code> milliseconds
   * it returns null.
   * </ul>
   * <P>
   * If the specified time is zero: 
   * <ul>returns the top element in 
   * the keyTyped event queue or null if queue is empty.
   * </ul>
   * <P>
   * If the specified wait time is negative: 
   * <ul>This method
   * either returns the top element in the queue or if the queue is
   * empty it waits indefinitely until the observer types a character.
   * </ul>
   * <P>
   * In all cases, the element returned is removed from the event queue.
   * <P>
   * <a name="event">
   * <b>General principles of event handling in FullScreen:</b></a>
   * <P>
   * FullScreen captures the key events in a separate Thread and stores 
   * them in Thread safe BlockingQueue objects. Any time observer  
   * types, presses or releases a key, that key and the time of the 
   * event are inserted to the end (tail) 
   * of the respective queues. When one of the getKeyTyped(), getKeyPressed()
   * or getKeyReleased() methods is invoked, the top (head) of the respective 
   * queue is retrieved and removed. The returned object, <code>KeyTyped</code>,
   * <code>KeyPressed</code> or <code>KeyReleased</code> contains two fields. To get 
   * the key of the event, use <code>getKey()</code>, and to get the the time of the 
   * event use <code>getWhen()</code>. For example:
   * <pre> 
   * // wait for the participant to respond for a maximum of 2000 ms 
   * KeyTyped response = getKeyTyped(2000);
   * if(response != null){
   *    String responseKey = response.getKey();
   *    Long reponseTime = response.getWhen();
   * }
   * </pre>
   * <P>
   * flushKeyTyped(), flushKeyPressed() and
   * flushKeyReleased() methods clear specified queues. 
   * <P>
   * For more information and examples see <a href="www.bilkent.edu.tr/~hboyaci/PsychWithJava/index.html">
   * Psychophysics programming with Java</a>.
   * 
   * @param ms time in milliseconds to wait for a response
   * 
   * @return the key typed
   * 
   * @see FullScreen#keyTyped(KeyEvent)
   * @see #getKeyTyped()
   * @see #flushKeyTyped()
   */
  public KeyTyped getKeyTyped(long ms) {

    KeyTyped c = null;
    try {
      if (ms < 0)
        c = keyTyped.take();
      else
        c = keyTyped.poll(ms, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      e.printStackTrace();
    }
    return c;
  }

  /**
   * Returns the top element in keyTyped 
   * queue or null if queue is empty. Equivalent to invoking getKeyTyped(0).
   * <P>
   * The element returned is removed from the event queue. See also
   * <a href="#event">general principles of event handling in FullScreen</a>
   * above.
   * <P>
   * For more information and examples see <a href="www.bilkent.edu.tr/~hboyaci/PsychWithJava/index.html">
   * Psychophysics programming with Java</a>.
   * 
   * @return the key typed event
   * 
   * @see #getKeyTyped(long)
   * @see #flushKeyTyped()
   */
  public KeyTyped getKeyTyped() {

    return keyTyped.poll();
  }

  /**
   * Clears the keyTyped queue. See also
   * <a href="#event">general principles of event handling in FullScreen</a>
   * above.
   * <P>
   * For more information and examples see <a href="www.bilkent.edu.tr/~hboyaci/PsychWithJava/index.html">
   * Psychophysics programming with Java</a>.
   * 
   * @see #getKeyTyped(long)
   */
  public void flushKeyTyped() {

    keyTyped.clear();
  }

  /**
   * Returns a KeyPressed object, which contains the code of the key pressed and when it is pressed 
   * by the observer within <code>ms</code> milliseconds.
   * <P>
   * If <code>ms</code> is positive: 
   * <ul>(i) returns the top element in the
   * keyPressed queue immediately if there is 
   * at least one element in the keyPressed event queue or
   * <br>(ii) waits up to the specified amount of time, <code>ms</code> for an element 
   * to become available. 
   * <br> (iii) If no key is pressed
   * within <code>ms</code> milliseconds
   * it returns null.
   * </ul>
   * <P>
   * If the specified time is zero: 
   * <ul>returns the top element in 
   * the keyPressed event queue or null if queue is empty.
   * </ul>
   * <P>
   * If the specified wait time is negative: 
   * <ul>This method
   * either returns the top element in the queue or if the queue is
   * empty it waits indefinitely until the observer presses a key.
   * </ul>
   * <P>
   * In all cases, the element returned is removed from the event queue.
   * <P>
   * For more information see <a href="#event">general principles of event handling in FullScreen</a>
   * and <a href="www.bilkent.edu.tr/~hboyaci/PsychWithJava/index.html">
   * Psychophysics programming with Java</a>.
   * 
   * @param ms time in milliseconds to wait for a response
   * 
   * @return the key pressed event
   * 
   * @see FullScreen#keyPressed(KeyEvent)
   * @see #getKeyPressed()
   * @see #flushKeyPressed()
   */
  public KeyPressed getKeyPressed(long ms) {

    KeyPressed c = null;
    try {
      if (ms < 0)
        c = keyPressed.take();
      else
        c = keyPressed.poll(ms, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      e.printStackTrace();
    }
    return c;
  }

  /**
   * Returns the top element in keyPressed
   * queue or null if queue is empty. Equivalent to invoking getKeyPressed(0).
   * <P>
   * The element returned is removed from the event queue. See also
   * <a href="#event">general principles of event handling in FullScreen</a>
   * above.
   * <P>
   * For more information and examples see <a href="www.bilkent.edu.tr/~hboyaci/PsychWithJava/index.html">
   * Psychophysics programming with Java</a>.
   * 
   * @return the key pressed event
   * 
   * @see #getKeyPressed(long)
   * @see #flushKeyTyped()
   */
  public KeyPressed getKeyPressed() {

    return keyPressed.poll();
  }
  
  /**
   * Clears the keyPressed queue. See also
   * <a href="#event">general principles of event handling in FullScreen</a>
   * above.
   * <P>
   * For more information and examples see <a href="www.bilkent.edu.tr/~hboyaci/PsychWithJava/index.html">
   * Psychophysics programming with Java</a>.
   * 
   * @see #getKeyPressed(long)
   */
  public void flushKeyPressed() {

    keyPressed.clear();
  }

  /**
   * Returns a KeyReleased object, which contains the code of the key released and when it is released
   * by the observer within <code>ms</code> milliseconds.
   * <P>
   * If <code>ms</code> is positive: 
   * <ul>(i) returns the top element in the
   * keyReleased queue immediately if there is 
   * at least one element in the keyReleased event queue or
   * <br>(ii) waits up to the specified amount of time, <code>ms</code> for an element 
   * to become available. 
   * <br> (iii) If no key is pressed
   * within <code>ms</code> milliseconds
   * it returns null.
   * </ul>
   * <P>
   * If the specified time is zero: 
   * <ul>returns the top element in 
   * the keyPressed event queue or null if queue is empty.
   * </ul>
   * <P>
   * If the specified wait time is negative: 
   * <ul>This method
   * either returns the top element in the queue or if the queue is
   * empty it waits indefinitely until the observer released a key.
   * </ul>
   * <P>
   * In all cases, the element returned is removed from the event queue.
   * <P>
   * For more information see <a href="#event">general principles of event handling in FullScreen</a> and 
   * <a href="www.bilkent.edu.tr/~hboyaci/PsychWithJava/index.html">
   * Psychophysics programming with Java</a>.
   * 
   * @param ms time in milliseconds to wait for a response
   * 
   * @return the key released event
   * 
   * @see FullScreen#keyReleased(KeyEvent)
   * @see #getKeyReleased()
   * @see #flushKeyReleased()
   */
  public KeyReleased getKeyReleased(long ms) {

    KeyReleased c = null;
    try {
      if (ms < 0)
        c = keyReleased.take();
      else
        c = keyReleased.poll(ms, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      e.printStackTrace();
    }
    return c;
  }

  /**
   * Returns the top element in keyReleased
   * queue or null if queue is empty. Equivalent to invoking getKeyPressed(0).
   * <P>
   * The element returned is removed from the event queue. See also
   * <a href="#event">general principles of event handling in FullScreen</a>
   * above.
   * <P>
   * For more information and examples see <a href="www.bilkent.edu.tr/~hboyaci/PsychWithJava/index.html">
   * Psychophysics programming with Java</a>.
   * 
   * @return the key released event
   * 
   * @see #getKeyReleased(long)
   * @see #flushKeyReleased()
   */
  public KeyReleased getKeyReleased() {

    return keyReleased.poll();
  }

  /**
   * Clears the keyReleased queue. See also
   * <a href="#event">general principles of event handling in FullScreen</a>
   * above.
   * <P>
   * For more information and examples see <a href="www.bilkent.edu.tr/~hboyaci/PsychWithJava/index.html">
   * Psychophysics programming with Java</a>.
   * 
   * @see #getKeyReleased(long)
   */
  public void flushKeyReleased() {

    keyReleased.clear();
  }

  /**
   * Returns whether or not Full Screen Exclusive Mode (FSEM) is
   * supported on client's system.
   * 
   * @return    true if full screen exclusive mode is supported
   */
  public boolean isFullScreenSupported() {

    return gDevice.isFullScreenSupported();
  }

  /**
   * Sets a new DisplayMode: secreen resolution, vertical synchronization rate,
   * and color depth. If the requested DisplayMode is not applicable or
   * DisplayMode change is not supported it causes the 
   * termination of the user's program, in order to avoid an erronous 
   * experimental session.
   *  
   * @param dm new display mode to apply
   * 
   * @see #isDisplayChangeSupported()
   * @see #isDisplayModeAvailable(DisplayMode)
   */
  public void setDisplayMode(DisplayMode dm) {

    if (displayMode.equals(dm))
      return;
    else if (!gDevice.isDisplayChangeSupported() || dm == null) {
      System.err.println("Exception in FullScreen.setDisplayMode(): "
          + "Display Change not Supported or DisplayMode is null");
      closeScreen();
      System.exit(0);
    }
    else if (!isDisplayModeAvailable(dm)) {
      System.err.println("Exception in FullScreen.setDisplayMode(): "
          + "DisplayMode not available");
      System.err.println("");
      System.err.println("Supported DisplayModes are:");
      String[] dms = reportDisplayModes();
      for (int i = 0; i < dms.length; i++) {
        System.err.print(dms[i]);
        if ((i + 1) % 4 == 0)
          System.err.println();
      }
      closeScreen();
      System.exit(0);
    }
    else {
      try {
        gDevice.setDisplayMode(dm);
        displayMode = dm;
        setBounds(0, 0, dm.getWidth(), dm.getHeight());
        Thread.sleep(200);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } catch (RuntimeException e) {
        System.err.println("Exception in FullScreen.setDisplayMode(): "
            + "DisplayMode not available or " + "Display Change not Supported");
        System.err.println("");
        System.err.println("Supported DisplayModes are:");
        String[] dms = reportDisplayModes();
        for (int i = 0; i < dms.length; i++) {
          System.err.println(dms[i]);
          if ((i + 1) % 4 == 0)
            System.err.println();
        }
        closeScreen();
        System.exit(0);
      }
    }
  }

  /**
   * Returns whether or not given DisplayMode is applicable to client's screen.
   * 
   * @param dm DisplayMode to check
   * 
   * @return whether dm is applicable or not
   */
  public boolean isDisplayModeAvailable(DisplayMode dm) {

    DisplayMode[] mds = gDevice.getDisplayModes();
    for (int i = 0; i < mds.length; i++) {
      if (mds[i].getWidth() == dm.getWidth()
          && mds[i].getHeight() == dm.getHeight()
          && mds[i].getBitDepth() == dm.getBitDepth()
          && mds[i].getRefreshRate() == dm.getRefreshRate())
        return true;
    }
    return false;
  }

  /**
   * Returns the current screen resolution, vertical 
   * synchronization rate and color depth in a readable
   * form.
   *  
   * @return current DisplayMode in readable form
   */
  public String reportDisplayMode() {

    StringBuilder message = new StringBuilder();
    if (displayMode.getBitDepth() == DisplayMode.BIT_DEPTH_MULTI)
      message.append("  Bit Depth = -1 (MULTIPLE) \n");
    else
      message.append("  Bit Depth = " + displayMode.getBitDepth() + "\n");
    message.append("  Width = " + displayMode.getWidth() + "\n");
    message.append("  Height = " + displayMode.getHeight() + "\n");
    if (displayMode.getRefreshRate() == DisplayMode.REFRESH_RATE_UNKNOWN)
      message.append("  Refresh Rate = 0 (unknown/unmodifiable) \n");
    else
      message.append("  Refresh Rate = " + displayMode.getRefreshRate() + "\n");
    return message.toString();
  }

  /** 
   * Returns the current DisplayMode.
   * 
   * @return current DisplayMode 
   */
  public DisplayMode getDisplayMode() {

    return displayMode;
  }

  /**
   * Returns whether or not DisplayMode change is available on 
   * client's system.
   * 
   * @return true if DisplayMode change is available
   */
  public boolean isDisplayChangeSupported() {

    return gDevice.isDisplayChangeSupported();
  }
  
  /**
   * Reports all available DisplayMode parameters in a readable
   * format.
   * 
   * @return all available DisplayMode parameters.
   */
  public String[] reportDisplayModes() {

    DisplayMode[] dms = getDisplayModes();
    String[] message = new String[dms.length];
    for (int i = 0; i < dms.length; i++) {
      StringBuilder m = new StringBuilder("(" + dms[i].getWidth() + ","
          + dms[i].getHeight() + "," + dms[i].getBitDepth() + ","
          + dms[i].getRefreshRate() + ") ");
      message[i] = m.toString();
    }
    return message;
  }

  /**
   * Returns all available DisplayModes on client's system.
   * 
   * @return all available DisplayModes.
   */
  public DisplayMode[] getDisplayModes() {

    return gDevice.getDisplayModes();
  }
}
