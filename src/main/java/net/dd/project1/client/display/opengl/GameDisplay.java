package net.dd.project1.client.display.opengl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.dd.project1.client.display.DrawHelper;
import net.dd.project1.client.display.KeyHandler;
import net.dd.project1.client.display.MouseHandler;
import net.dd.project1.client.game.Game;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

@RequiredArgsConstructor
public class GameDisplay {

  // The window handle
  private long window;

  private final Game game;
  private final String title;
  @Setter
  private KeyHandler keyHandler;
  @Setter
  private MouseHandler mouseHandler;

  public void run() {
    init();
    loop();

    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window);
    glfwDestroyWindow(window);

    // Terminate GLFW and free the error callback
    glfwTerminate();
    glfwSetErrorCallback(null).free();
  }

  public int getWidth() {
    return this.windowSize[0];
  }

  public int getHeight() {
    return this.windowSize[1];
  }

  public int getFps() {
    return this.lastFps;
  }

  private void init() {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set();

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!glfwInit())
      throw new IllegalStateException("Unable to initialize GLFW");

    // Configure GLFW
    glfwDefaultWindowHints(); // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable

    // Create the window
    window = glfwCreateWindow(this.windowSize[0], this.windowSize[1], this.title, NULL, NULL);
    if (window == NULL)
      throw new RuntimeException("Failed to create the GLFW window");

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
        glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
      else {
        keyHandler.onInput(key, action);
      }
    });
    glfwSetMouseButtonCallback(window, (window1, button, action, mods) -> {
      double[] cursorX = new double[1];
      double[] cursorY = new double[1];
      glfwGetCursorPos(window, cursorX, cursorY);
      if (this.mouseHandler != null) {
        this.mouseHandler.onInput(button, action, (int) cursorX[0], (int) cursorY[0]);
      }
    });

    // Get the thread stack and push a new frame
    try (MemoryStack stack = stackPush()) {
      IntBuffer pWidth = stack.mallocInt(1); // int*
      IntBuffer pHeight = stack.mallocInt(1); // int*

      // Get the window size passed to glfwCreateWindow
      glfwGetWindowSize(window, pWidth, pHeight);

      // Get the resolution of the primary monitor
      GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

      // Center the window
      glfwSetWindowPos(
              window,
              (vidmode.width() - pWidth.get(0)) / 2,
              (vidmode.height() - pHeight.get(0)) / 2
      );
    } // the stack frame is popped automatically

    // Make the OpenGL context current
    glfwMakeContextCurrent(window);
    // Enable v-sync
    glfwSwapInterval(1);

    // Make the window visible
    glfwShowWindow(window);
  }

  private int[] windowSize = new int[]{600, 600};

  public void checkWindowResize() {
    int[] currentSize = this.currentWindowSize();
    if (this.windowSize[0] != currentSize[0] || this.windowSize[1] != currentSize[1]) {
      //glViewport(0, 0, currentSize[0], currentSize[1]);
      this.windowSize = currentSize;
    }
  }

  public int[] currentWindowSize() {
    try (MemoryStack stack = stackPush()) {
      IntBuffer pWidth = stack.mallocInt(1); // int*
      IntBuffer pHeight = stack.mallocInt(1); // int*

      // Get the window size passed to glfwCreateWindow
      glfwGetWindowSize(window, pWidth, pHeight);

      return new int[]{pWidth.get(0), pHeight.get(0)};
    }
  }

  private int lastFps;
  private int currentFrame;
  private long lastFrameChangeTime;

  private void loop() {
    GL.createCapabilities();

    glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

    int[] originalSize = this.currentWindowSize();

    DrawHelper drawHelper = new OpenGlDrawHelper();

    while (!glfwWindowShouldClose(window)) {
      this.checkWindowResize();

      long now = System.currentTimeMillis();
      if (now - lastFrameChangeTime > 1000) {
        lastFps = currentFrame;
        currentFrame = 0;
        //System.out.println(lastFps);
        lastFrameChangeTime = now;
      }
      currentFrame++;

      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

      glPushMatrix();
      double deltaX = 2 / (double) originalSize[0];
      double deltaY = 2 / (double) originalSize[1];

      // move to origin
      glTranslated(-1, 1, 0);
      glScaled(deltaX, deltaY, 1);
      glTranslated(0, -this.getHeight(), 0);

      this.game.render(drawHelper);

      glPopMatrix();

      glfwSwapBuffers(window);
      glfwPollEvents();
    }
  }


}
