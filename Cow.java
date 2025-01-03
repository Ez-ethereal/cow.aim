// TODO TODO TODO keyHeld (probably) needs the same treatment as mouseHeld
// TODO: sound
// TODO: helper functions (snippets) go in Cow :) (all homeworks can extend Cow)
// TODO: a crash should cause the window to close
// TODO: a nice kitchen sink with COW branding, maybe some 3d stuff
// TODO: turn off automatic repaint (right now only happens when you resize the window)
// TODO: protect the entire student-facing API with checks for BEGINFRAME() already called
// TODO: set_canvas_clear_color

// // TODO: demos
// TODO: paint
// TODO: tic tac toe
// TODO: flappy bird

// // NOTE: limitations
// NOTE: drops very fast press and release
// TODO: PRINT 2d arrays
// ! TODO: use our own Color Class

// doing these so i can avoid importing Color,
// define my own, and have students use it
// in a Cow app without an import
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.lang.Math;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Cursor;
import java.io.*;

class ASSERT_Exception extends RuntimeException {
    public ASSERT_Exception(String message) { super(message); }
    private static final long serialVersionUID = 1L;
}

class Cow {

    static class Vector2 {
        double x;
        double y;
        Vector2(double x, double y) {
            this.x = x;
            this.y = y;
        }
        Vector2 plus(Vector2 other) {
            return new Vector2(this.x + other.x, this.y + other.y);
        }
        Vector2 minus(Vector2 other) {
            return new Vector2(this.x - other.x, this.y - other.y);
        }
        Vector2 times(double fac) {
            return new Vector2(fac * this.x, fac * this.y);
        }
        Vector2 dividedBy(double den) {
            return this.times(1.0 / den);
        }
        double squaredLength() {
            return this.x * this.x + this.y * this.y;
        }
        double length() {
            return SQRT(this.squaredLength());
        }
        Vector2 normalized() {
            return this.dividedBy(this.length());
        }
        static Vector2 directionVectorFrom(Vector2 a, Vector2 b) {
            return (b.minus(a)).normalized();
        }
    }

    static class Color {
        float r;
        float g;
        float b;

        Color(double r, double g, double b) {
            this.r = (float) r;
            this.g = (float) g;
            this.b = (float) b;
        }
    }

    static void ASSERT(boolean condition) {
        if (!condition) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            StackTraceElement parent = stackTrace[2];
            int lineNumber = parent.getLineNumber();
            String methodName = parent.getMethodName();
            String message = "ASSERT failed on Line " + lineNumber + " of " + methodName + "(...)";
            PRINT(message);
            throw new ASSERT_Exception(message);
        }
    }	

    public static void main(String[] arguments) {
        PRINT((char)(0));
        PRINT(0);
        PRINT(0.0);
        PRINT(false);
        PRINT('0');
        PRINT(new int[] { 0, 1, 2 });
        PRINT(new char[] { 'a', 'b', 'c' });
        PRINT(new double[] { 0.0, 1.0, 2.0 });
        PRINT(new boolean[] { false, true });
        Object object = null;
        PRINT(object);
        PRINT(ROUND(3.1));
        PRINT(ROUND(-2.9));
    }

    /////////////////////////////////////////////////////////////////////////

    // TODO mouse and key stuff
    // TODO imgui

    static int MIN(int a, int b) { return Math.min(a, b); }
    static float MIN(double a, double b) { return (float) Math.min(a, b); }
    static int MAX(int a, int b) { return Math.max(a, b); }
    static float MAX(double a, double b) { return (float) Math.max(a, b); }
    static int ABS(int a) { return Math.abs(a); }
    static float ABS(double a) { return (float) Math.abs(a); }
    static int MODULO(int a, int b) { return Math.floorMod(a, b); }
    static float SQRT(double a) { return (float) Math.sqrt(a); }
    static boolean ARE_EQUAL(double a, double b) { return ABS(a - b) < 0.0001; }
    static int ROUND(double a) { return (int) Math.round(a); }

    static void PRINT() { System.out.println(); }
    static void PRINT(String a) { System.out.println(a); }
    static void PRINT(int a) { System.out.println("" + a); }
    static void PRINT(char a) {
        if (a == '\0') {
            System.out.println("'\\0'");
        } else {
            System.out.println("'" + a + "'");
        }
    }
    static void PRINT(double a) { System.out.println("" + a); }
    static void PRINT(boolean a) { System.out.println("" + a); }
    static <ElementType> void PRINT(ElementType e) {
        System.out.println("" + e);
    }
    static void PRINT(int[] array) {
        System.out.print("[ ");
        for (int i = 0; i < array.length; ++i) {
            System.out.print(array[i]);
            if (i != array.length - 1) System.out.print(", ");
        }
        System.out.println(" ]");
    }
    static void PRINT(char[] array) {
        System.out.print("[ ");
        for (int i = 0; i < array.length; ++i) {
            System.out.print(array[i]);
            if (i != array.length - 1) System.out.print(", ");
        }
        System.out.println(" ]");
    }
    static void PRINT(double[] array) {
        System.out.print("[ ");
        for (int i = 0; i < array.length; ++i) {
            System.out.print(array[i]);
            if (i != array.length - 1) System.out.print(", ");
        }
        System.out.println(" ]");
    }
    static void PRINT(boolean[] array) {
        System.out.print("[ ");
        for (int i = 0; i < array.length; ++i) {
            System.out.print(array[i]);
            if (i != array.length - 1) System.out.print(", ");
        }
        System.out.println(" ]");
    }
    static <ElementType> void PRINT(ElementType[] array) {
        System.out.print("[ ");
        for (int i = 0; i < array.length; ++i) {
            System.out.print(array[i]);
            if (i != array.length - 1) System.out.print(", ");
        }
        System.out.println(" ]");
    }


    static Color BLACK   = new Color(  0 / 255.0,   0 / 255.0,   0 / 255.0);
    static Color BLUE    = new Color(  4 / 255.0,  51 / 255.0, 255 / 255.0);
    static Color BROWN   = new Color(170 / 255.0, 121 / 255.0,  66 / 255.0);
    static Color CYAN    = new Color(  0 / 255.0, 253 / 255.0, 255 / 255.0);
    static Color GREEN   = new Color(  0 / 255.0, 249 / 255.0,   0 / 255.0);
    static Color MAGENTA = new Color(255 / 255.0,  64 / 255.0, 255 / 255.0);
    static Color ORANGE  = new Color(255 / 255.0, 147 / 255.0,   0 / 255.0);
    static Color PURPLE  = new Color(148 / 255.0,  33 / 255.0, 146 / 255.0);
    static Color RED     = new Color(255 / 255.0,  38 / 255.0,   0 / 255.0);
    static Color YELLOW  = new Color(255 / 255.0, 251 / 255.0,   0 / 255.0);
    static Color WHITE   = new Color(255 / 255.0, 255 / 255.0, 255 / 255.0);
    static Color colorRainbowSwirl(double time) {
        double TAU   = 6.283;
        double red   = 0.5f + 0.5f * (float) Math.cos(TAU * ( 0.000 - time));
        double green = 0.5f + 0.5f * (float) Math.cos(TAU * ( 0.333 - time));
        double blue  = 0.5f + 0.5f * (float) Math.cos(TAU * (-0.333 - time));
        return new Color(red, green, blue);
    }

    static final char LEFT_ARROW = KeyEvent.VK_LEFT;
    static final char RIGHT_ARROW = KeyEvent.VK_RIGHT;
    static final char UP_ARROW = KeyEvent.VK_UP;
    static final char DOWN_ARROW = KeyEvent.VK_DOWN;
    static final char BACKSPACE = KeyEvent.VK_BACK_SPACE;
    static final char DELETE = KeyEvent.VK_DELETE;
    static final char SHIFT = KeyEvent.VK_SHIFT;
    static final char ENTER = KeyEvent.VK_ENTER;
    static final char CONTROL = KeyEvent.VK_CONTROL;
    static final char COMMAND = KeyEvent.VK_META;
    static final char ALT = KeyEvent.VK_ALT;
    static final char TAB = KeyEvent.VK_TAB;

    static boolean mousePressed;
    static boolean mouseHeld;
    static boolean mouseReleased;
    static float mouseX;
    static float mouseY;
    static float mouseScrollAmount;

    static boolean keyPressed (int i) { return _keyPressed[i];  }
    static boolean keyHeld    (int i) { return _keyHeld[i];     }
    static boolean keyReleased(int i) { return _keyReleased[i]; }
    static boolean keyToggled (int i) { return _keyToggled[i];  }
    static boolean keyAnyPressed;
    static char    keyLastPressed; // NOTE: Not cleared between frames

    static boolean _keyPressed[]  = new boolean[256];
    static boolean _keyHeld[]     = new boolean[256];
    static boolean _keyReleased[] = new boolean[256];
    static boolean _keyToggled[]  = new boolean[256];

    /////////////////////////////////////////////////////////////////////////

    // // TODO: User-Facing API 
    // takes doubles and return floats (so students don't have to worry about narrowing conversions)
    // all arguments in World coordinates unless otherwise specified (and then only in advanced API)
    // - hide advanced API with Java-style default args -- foo(a) { foo(a, defaults ...); }

    static Color _canvas_color = WHITE;

    static float _canvas_left_World   = -256;
    static float _canvas_right_World  = 256;
    static float _canvas_bottom_World = -256;
    static float _canvas_top_World    = 256;
    static int _canvas_height_Pixel   = 512;

    static float _canvas_get_aspect_ratio() { return _canvas_get_width_World() / _canvas_get_height_World(); }
    static int _canvas_get_width_Pixel() { return (int) (_canvas_get_aspect_ratio() * _canvas_height_Pixel); }
    static float _canvas_get_width_World() { return _canvas_right_World - _canvas_left_World; }
    static float _canvas_get_height_World() { return _canvas_top_World - _canvas_bottom_World; }
    static float _canvas_get_Pixel_per_World_ratio() { return _canvas_height_Pixel / _canvas_get_height_World(); }

    static int _xPIXELfromWORLD(double x_World) { return (int) (_canvas_get_Pixel_per_World_ratio() * (x_World - _canvas_left_World)); }
    static int _yPIXELfromWORLD(double y_World) { return (int) (_canvas_height_Pixel - (_canvas_get_Pixel_per_World_ratio() * (y_World - _canvas_bottom_World))); }
    static int _LPIXELfromWORLD(double length_world) { return (int) (_canvas_get_Pixel_per_World_ratio() * length_world); }
    static float _canvas_get_x_World_from_x_Pixel(int x_Pixel) { return (float) ((x_Pixel / _canvas_get_Pixel_per_World_ratio()) + _canvas_left_World); }
    static float _canvas_get_y_World_from_y_Pixel(int y_Pixel) { return (float) (((_canvas_height_Pixel - y_Pixel) / _canvas_get_Pixel_per_World_ratio()) + _canvas_bottom_World); }


    static float canvasGetTop() { return _canvas_top_World; }
    static float canvasGetBottom() { return _canvas_bottom_World; }
    static float canvasGetLeft() { return _canvas_left_World; }
    static float canvasGetRight() { return _canvas_right_World; }


    static Color CANVAS_CONFIG_DEFAULT_COLOR = WHITE;
    static int CANVAS_CONFIG_DEFAULT_MAX_DIMENSION_IN_PIXELS = 512;
    static void canvasConfig(double left, double bottom, double right, double top) { canvasConfig(left, bottom, right, top, CANVAS_CONFIG_DEFAULT_COLOR, CANVAS_CONFIG_DEFAULT_MAX_DIMENSION_IN_PIXELS); }
    static void canvasConfig(double left, double bottom, double right, double top, Color color) { canvasConfig(left, bottom, right, top, color, CANVAS_CONFIG_DEFAULT_MAX_DIMENSION_IN_PIXELS); }
    static void canvasConfig(double left, double bottom, double right, double top, Color color, int maxDimensionInPixels) {
        _canvas_left_World   = (float) left;
        _canvas_right_World  = (float) right;
        _canvas_bottom_World = (float) bottom;
        _canvas_top_World    = (float) top;

        _canvas_color = color;

        float max_dim_World = Math.max(_canvas_get_width_World(), _canvas_get_height_World());
        float Pixel_per_World = maxDimensionInPixels / max_dim_World;
        _canvas_height_Pixel = (int) (Pixel_per_World * _canvas_get_height_World());

        if (_cow_initialized) _canvasReattach();
    }
    static void windowSetTitle(String title) { _jFrame.setTitle(title); }
    static void _canvasReattach() {
        _buffered_image = new BufferedImage(_canvas_get_width_Pixel(), _canvas_height_Pixel, BufferedImage.TYPE_INT_ARGB);
        _buffered_image_graphics = _buffered_image.createGraphics();
        _jPanel_extender.setPreferredSize(new Dimension(_canvas_get_width_Pixel(), _canvas_height_Pixel));
        _jFrame.pack();
    }

    static Color DRAW_LINE_DEFAULT_COLOR = BLACK;
    static double DRAW_LINE_DEFAULT_THICKNESS = 2.0;
    static void drawLine(double x1, double y1, double x2, double y2) { drawLine(x1, y1, x2, y2, DRAW_LINE_DEFAULT_COLOR, DRAW_LINE_DEFAULT_THICKNESS); }
    static void drawLine(double x1, double y1, double x2, double y2, Color color) { drawLine(x1, y1, x2, y2, color, DRAW_LINE_DEFAULT_THICKNESS); }
    static void drawLine(double x1, double y1, double x2, double y2, Color color, double thickness) {
        _draw_set_color(color);
        _draw_set_line_thickness(thickness);
        _buffered_image_graphics.drawLine(_xPIXELfromWORLD(x1), _yPIXELfromWORLD(y1), _xPIXELfromWORLD(x2), _yPIXELfromWORLD(y2));
    }

    static Color DRAW_CIRCLE_DEFAULT_COLOR = BLACK;
    static void drawCircle(double x, double y, double radius) { drawCircle(x, y, radius, DRAW_CIRCLE_DEFAULT_COLOR); }
    static void drawCircle(double x, double y, double radius, Color color) {
        _draw_set_color(color);
        _draw_circle(x, y, radius, false);
    }

    static Color DRAW_RECTANGLE_DEFAULT_COLOR = BLACK;
    static void drawRectangle(double x1, double y1, double x2, double y2) { drawRectangle(x1, y1, x2, y2, DRAW_RECTANGLE_DEFAULT_COLOR); }
    static void drawRectangle(double x1, double y1, double x2, double y2, Color color) {
        _draw_set_color(color);
        _draw_rectangle(x1, y1, x2, y2, false);
    }

    static Color DRAW_TRIANGLE_DEFAULT_COLOR = BLACK;
    static void drawTriangle(double x1, double y1, double x2, double y2, double x3, double y3) { drawTriangle(x1, y1, x2, y2, x3, y3, DRAW_TRIANGLE_DEFAULT_COLOR); }
    static void drawTriangle(double x1, double y1, double x2, double y2, double x3, double y3, Color color) {
        _draw_set_color(color);
        _draw_triangle(x1, y1, x2, y2, x3, y3, false);
    }



    // // TODO: Utility API

    static void _set_monospaced_font_character_width(double character_width) { // setFont with World character_width
        int char_height_Pixel = 4096;
        _buffered_image_graphics.setFont(new Font(Font.MONOSPACED, Font.PLAIN, char_height_Pixel)); 
        FontMetrics fontMetrics = _buffered_image_graphics.getFontMetrics(); 
        int char_width_Pixel = fontMetrics.charWidth('A');
        double char_height_World = char_height_Pixel * (character_width / char_width_Pixel);
        char_height_Pixel = _LPIXELfromWORLD(char_height_World);
        _buffered_image_graphics.setFont(new Font(Font.MONOSPACED, Font.PLAIN, char_height_Pixel)); 
    }

    // // core drawing API
    // _draw_set_color(r, g, b)
    // _draw_set_color(r, g, b, a)
    // _draw_set_color(color)
    // _draw_set_color(color, a)
    // _draw_set_line_thickness(diameter|line_width)
    // draw_set_polygon_mode(FILL|OUTLINE) // NOTE: lines are always drawn
    // draw_begin(CIRCLES|RECTANGLES|LINES|LINE_STRIP|TRIANGLES|QUADS)
    // draw_vertex(double x, double y)
    // draw_end()

    static void _draw_set_line_thickness(double w) {
        assert w >= 0;
        ((Graphics2D) _buffered_image_graphics).setStroke(new BasicStroke((float) w));
    }

    static void _draw_set_color(double r, double g, double b, double a) {
        assert r >= 0;
        assert r <= 1;
        assert g >= 0;
        assert g <= 1;
        assert b >= 0;
        assert b <= 1;
        assert a >= 0;
        assert a <= 1;
        _buffered_image_graphics.setColor(new java.awt.Color((float) r, (float) g, (float) b, (float) a));
    }

    static void _draw_set_color(double r, double g, double b) {
        _draw_set_color(r, g, b, 1.0);
    }

    static void _draw_set_color(Color color, double a) {
        _draw_set_color(color.r, color.g, color.b, a);
    }

    static void _draw_set_color(Color color) {
        _draw_set_color(color, 1.0);
    }




    static void fill_rectangle(double x1, double y1, double x2, double y2) { _draw_rectangle(x1, y1, x2, y2, false); }
    static void outline_rectangle(double x1, double y1, double x2, double y2) { _draw_rectangle(x1, y1, x2, y2, true); }
    static void _draw_rectangle(double x1, double y1, double x2, double y2, boolean outlined) {
        int Xx1 = _xPIXELfromWORLD(x1);
        int Yy1 = _yPIXELfromWORLD(y1);
        int Xx2 = _xPIXELfromWORLD(x2);
        int Yy2 = _yPIXELfromWORLD(y2);
        int arg1 = Math.min(Xx1, Xx2);
        int arg2 = Math.min(Yy1, Yy2);
        int arg3 = Math.abs(Xx1 - Xx2);
        int arg4 = Math.abs(Yy1 - Yy2);
        if (!outlined) {
            _buffered_image_graphics.fillRect(arg1, arg2, arg3, arg4);
        } else {
            _buffered_image_graphics.drawRect(arg1, arg2, arg3, arg4);
        }
    }

    static void fill_triangle(double x1, double y1, double x2, double y2, double x3, double y3) { _draw_triangle(x1, y1, x2, y2, x3, y3, false); }
    static void outline_triangle(double x1, double y1, double x2, double y2, double x3, double y3) { _draw_triangle(x1, y1, x2, y2, x3, y3, true); }
    static void _draw_triangle(double x1, double y1, double x2, double y2, double x3, double y3, boolean outlined) {
        int Xx1 = _xPIXELfromWORLD(x1);
        int Yy1 = _yPIXELfromWORLD(y1);
        int Xx2 = _xPIXELfromWORLD(x2);
        int Yy2 = _yPIXELfromWORLD(y2);
        int Xx3 = _xPIXELfromWORLD(x3);
        int Yy3 = _yPIXELfromWORLD(y3);
        if (!outlined) {
            _buffered_image_graphics.fillPolygon(new int[] { Xx1, Xx2, Xx3 }, new int[] { Yy1, Yy2, Yy3 }, 3);
        } else {
            _buffered_image_graphics.drawPolygon(new int[] { Xx1, Xx2, Xx3 }, new int[] { Yy1, Yy2, Yy3 }, 3);
        }
    }

    // static void fill_center_rectangle(double x, double y, double width, double height) { _draw_center_rectangle(x, y, width, height, false); }
    // static void outline_center_rectangle(double x, double y, double width, double height) { _draw_center_rectangle(x, y, width, height, true); }
    // static void _draw_center_rectangle(double x, double y, double width, double height, boolean outlined) {
    //     assert width >= 0;
    //     assert height >= 0;
    //     double half_width = width / 2;
    //     double half_height = height / 2;
    //     _draw_corner_rectangle(x - half_width, y - half_height, x + half_width, y + half_height, outlined);
    // }

    static void fill_circle(double x, double y, double r) { _draw_circle(x, y, r, false); }
    static void outline_circle(double x, double y, double r) { _draw_circle(x, y, r, true); }
    static void _draw_circle(double x, double y, double r, boolean outlined) {
        int arg1 = _xPIXELfromWORLD(x - r);
        int arg2 = _yPIXELfromWORLD(y + r);
        int arg3 = _LPIXELfromWORLD(2 * r);
        int arg4 = arg3;
        if (!outlined) {
            _buffered_image_graphics.fillOval(arg1, arg2, arg3, arg4);
        } else {
            _buffered_image_graphics.drawOval(arg1, arg2, arg3, arg4);
        }
    }

    // TODO: camera (ability to zoom out, pan)
    // TODO: ability to resize window
    // TODO: window decorations


    static BufferedImage _buffered_image;
    static Graphics _buffered_image_graphics;
    static CowJPanelExtender _jPanel_extender;
    static JFrame _jFrame;
    static boolean _cow_initialized;

    static class NullOutputStream extends OutputStream {
        @Override
        public void write(int b) throws IOException {}
    }

    static void _cow_safe_attempt_initialize() {
        if (!_cow_initialized) {
            _cow_initialized = true;

            _buffered_image = new BufferedImage(_canvas_get_width_Pixel(), _canvas_height_Pixel, BufferedImage.TYPE_INT_ARGB);
            assert _buffered_image != null;
            _buffered_image_graphics = _buffered_image.createGraphics();
            assert _buffered_image_graphics != null;

            { // Trigger and suppress one-time Mac warnings about missing fonts.
                PrintStream systemDotErr = System.err;
                System.setErr(new PrintStream(new NullOutputStream()));
                _buffered_image_graphics.getFontMetrics(); 
                System.setErr(systemDotErr);
            }

            _jPanel_extender = new CowJPanelExtender();
            _jPanel_extender.setPreferredSize(new Dimension(_canvas_get_width_Pixel(), _canvas_height_Pixel));

            _jFrame = new JFrame();
            _jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            _jFrame.setLocation(0, 0);
            _jFrame.getContentPane().add(_jPanel_extender, BorderLayout.CENTER);
            _jFrame.pack();
            _jFrame.setVisible(true);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image emptyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB); 
            Cursor invisibleCursor = toolkit.createCustomCursor(emptyImage, new Point(0, 0), "Invisible Cursor");
            _jFrame.getContentPane().setCursor(invisibleCursor);
        }
    }


    static boolean beginFrame() {
        _cow_safe_attempt_initialize();

        { // *_pressed, *_released
            { // mousePressed, mouseReleased
                mousePressed = _jPanel_extender._mousePressed;
                _jPanel_extender._mousePressed = false;
                if (mousePressed) mouseHeld = true;

                mouseReleased = _jPanel_extender._mouseReleased;
                _jPanel_extender._mouseReleased = false;
                if (mouseReleased) mouseHeld = false;
            }
            { // mouseScrollAmount
            	mouseScrollAmount = _jPanel_extender._mouseScrollAmount;
            	_jPanel_extender._mouseScrollAmount = 0.0f;
            }
            { // keyboard
                keyAnyPressed = false;
                for (char i = 0; i < 256; ++i) {
                    _keyPressed[i]  = _jPanel_extender._keyPressed[i];
                    if (_keyPressed[i]) {
                        keyAnyPressed = true;
                        keyLastPressed = i;
                    }
                    _keyReleased[i] = _jPanel_extender._keyReleased[i];
                    _jPanel_extender._keyPressed[i] = false;
                    _jPanel_extender._keyReleased[i] = false;
                    if (_keyPressed[i]) {
                        _keyHeld[i] = true;
                        _keyToggled[i] = !_keyToggled[i];
                    }
                    if (_keyReleased[i]) _keyHeld[i] = false;
                }
            }
        }

        _jPanel_extender.repaint();

        try { Thread.sleep(1000 / 60); } catch (Exception exception) {};

        // beginning of next frame

        { // mouse
            Point point;
            {
                point = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(point, _jPanel_extender);
            }
            mouseX = _canvas_get_x_World_from_x_Pixel(point.x);
            mouseY = _canvas_get_y_World_from_y_Pixel(point.y);
        }


        { // canvas
            java.awt.Color tmp = _buffered_image_graphics.getColor();
            _draw_set_color(_canvas_color);
            _buffered_image_graphics.fillRect(0, 0, _canvas_get_width_Pixel(), _canvas_height_Pixel);
            _buffered_image_graphics.setColor(tmp);
        }

        // return !(_keyHeld[CONTROL] && _keyPressed['Q']);
        return true;
    }



    // HW-Specific Functions
    static void HW02_drawText(char[] buffer, int length, Color color) {
        _draw_set_color(color);
        _set_monospaced_font_character_width(1.0);
        _buffered_image_graphics.drawChars(buffer, 0, length, _xPIXELfromWORLD(0.0), _yPIXELfromWORLD(0.0));
    }
    static void HW04_drawTimeline(int numFrames, int currentFrameIndex) {

        ASSERT(currentFrameIndex < numFrames);

        int h = 16;

        _draw_set_color(BLACK);
        _buffered_image_graphics.setFont(new Font(Font.MONOSPACED, Font.PLAIN, h)); 

        FontMetrics fontMetrics = _buffered_image_graphics.getFontMetrics(); 

        double eps = 8.0;
        double x = _canvas_left_World + eps;
        double y = _canvas_top_World - eps;
        String timeline = "";
        double x2 = 0.0;
        for (int i = 0; i < numFrames; ++i) {
            if (i == currentFrameIndex) {
                x2 = x + fontMetrics.stringWidth(timeline) / _LPIXELfromWORLD(1.0); // FORNOW: sloppy
                if (currentFrameIndex > 9) x2 += fontMetrics.charWidth('0') / 2 / _LPIXELfromWORLD(1.0); // FORNOW: sloppy
            }
            timeline += i + " ";
        }
        _buffered_image_graphics.drawString("|", _xPIXELfromWORLD(x2), _yPIXELfromWORLD(y));
        _buffered_image_graphics.drawString("v", _xPIXELfromWORLD(x2), _yPIXELfromWORLD(y - h / 2));
        _buffered_image_graphics.drawString(timeline, _xPIXELfromWORLD(x), _yPIXELfromWORLD(y - 2 * h / 2));
    }

    static void drawString(String string, double x, double y, Color color, int fontSize) {
        _draw_set_color(color);
        _buffered_image_graphics.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize)); 
        _buffered_image_graphics.drawString(string, _xPIXELfromWORLD(x), _yPIXELfromWORLD(y));
    }

    private static class _HW09_Hidden_Node { 
        _HW09_Hidden_Node[] children;
        boolean isTerminal;

        _HW09_Hidden_Node() {
            this.children = new _HW09_Hidden_Node[26];
            this.isTerminal = false;
        }
    }

    // this function works (maybe???)
    // NOTE: will cause exception if non node is passed but just dont do that
    protected static _HW09_Hidden_Node convertToHidden(Object node) { 
        if (node == null) return null;
        try {
            _HW09_Hidden_Node convertedNode = new _HW09_Hidden_Node();
            java.lang.reflect.Field isTerminalField = node.getClass().getDeclaredField("isTerminal"); // hmmmmmmm yes very standard java
            java.lang.reflect.Field childrenField = node.getClass().getDeclaredField("children");

            convertedNode.isTerminal = isTerminalField.getBoolean(node);
            Object[] unconvertedNodes = (Object[]) childrenField.get(node);

            for (int i = 0; i < 26; i++) {
                convertedNode.children[i] = convertToHidden(unconvertedNodes[i]);
            }
            return convertedNode;

        } catch (Exception e) {
            throw new ASSERT_Exception(e.getMessage()); // incredible error handling (#CatchAndRelease)
        }
    }

    static HashMap<_HW09_Hidden_Node, Integer> calculateNumLeafDescendants(_HW09_Hidden_Node root) {
        HashMap<_HW09_Hidden_Node, Integer> result = new HashMap<_HW09_Hidden_Node, Integer>();
        _calculateNumLeafDescendants(root, result);
        return result;
    }

    static void _calculateNumLeafDescendants(_HW09_Hidden_Node node, HashMap<_HW09_Hidden_Node, Integer> result) {
        int n = 0;
        boolean end = true;
        for(int i = 0; i < node.children.length; i++) {
            if (node.children[i] != null) {
                _calculateNumLeafDescendants(node.children[i], result);
                n += result.getOrDefault(node.children[i], 0); // TODO: is getOrDefault necessary?
                end = false;
            }
        }
        result.put(node, (end) ? 1 : n);
    }

    static class _HW09_DrawData {
        char letter;
        Vector2 parentPosition;
        _HW09_Hidden_Node curNode;

        _HW09_DrawData(_HW09_Hidden_Node curNode, Vector2 parentPosition, char letter) {
            this.curNode = curNode;
            this.parentPosition = parentPosition;
            this.letter = letter;
        }
    }

    static void _HW09_drawTrie(Object start_node, double width, double height) {
        _HW09_Hidden_Node root = convertToHidden(start_node);
        HashMap<_HW09_Hidden_Node, Integer> leafs = calculateNumLeafDescendants(root);

        ArrayDeque<_HW09_DrawData> queue = new ArrayDeque<>();
        queue.add(new _HW09_DrawData(root, new Vector2(width/2.0, height - 1), ' '));

        while(queue.size() > 0) {
            _HW09_DrawData curData = queue.remove();
            _HW09_Hidden_Node curNode = curData.curNode;
            Vector2 parentPosition = curData.parentPosition;
            double parentIndex = parentPosition.x - (leafs.get(curNode) / 2.0);
            for(int i = 0; i < curNode.children.length; i++) {
                if(curNode.children[i] != null) {
                    _HW09_Hidden_Node node = curNode.children[i];
                    int childNodes = leafs.get(node);
                    double childPositionX = parentIndex + childNodes/2.0;
                    parentIndex += childNodes;

                    Vector2 childPosition = new Vector2(childPositionX, parentPosition.y-1);
                    queue.add(new _HW09_DrawData(node, childPosition, (char)(i + 'a')));

                    Vector2 nudge = Vector2.directionVectorFrom(parentPosition, childPosition).times(1.2 * .25);
                    Vector2 a = parentPosition.plus(nudge);
                    Vector2 b = childPosition.minus(nudge);
                    drawLine(a.x, a.y, b.x, b.y, BLACK);
                }
            }
            drawCircle(parentPosition.x, parentPosition.y, .25, (!curNode.isTerminal) ? new Color(0.9, 0.9, 0.9) : CYAN);
            drawString("" + curData.letter, parentPosition.x - 0.075, parentPosition.y - 0.08, BLACK, 16);
        }
    }




    // 14            x            14
    // 13           /1\           13
    // 12          / 3 \          12
    // 11         /  5  \         11
    // 10        /   7   \        10
    // 9        /    9    \        9
    // 8       /     11    \       8
    // 7      /      13     \      7
    // 6     x       15      x     6
    // 5    /1\      13     /1\    5
    // 4   / 3 \     11    / 3 \   4
    // 3  /  5  \    9    /  5  \  3
    // 2 x   7   x   7   x   7   x 2
    // 1/1\  5  /1\  5  /1\  5  /1\1
    // x 3 x 3 x 3 x 3 x 3 x 3 x 3 x
    //                              
    //       0      
    //      / \     
    //     /   \    
    //    /     \   
    //   1       2  
    //  /       / \ 
    // 3       4   5
    // root = new Node(0);
    // root.leftChild = new Node(1);
    // root.rightChild = new Node(2);
    // root.leftChild.leftChild = new Node(3);
    // root.rightChild.leftChild = new Node(4);
    // root.rightChild.rightChild = new Node(5);

    // root = new Node(0);
    // root.leftChild = new Node(1);
    // root.rightChild = new Node(2);
    // root.leftChild.leftChild = new Node(3);
    // root.leftChild.rightChild = new Node(4);
    // root.rightChild.leftChild = new Node(5);
    // root.rightChild.rightChild = new Node(6);
    // root.leftChild.leftChild.leftChild = new Node(7);
    // root.leftChild.leftChild.rightChild = new Node(8);
    // root.leftChild.rightChild.leftChild = new Node(9);
    // root.leftChild.rightChild.rightChild = new Node(10);
    // root.rightChild.leftChild.leftChild = new Node(11);
    // root.rightChild.leftChild.rightChild = new Node(12);
    // root.rightChild.rightChild.leftChild = new Node(13);
    // root.rightChild.rightChild.rightChild = new Node(14);

    private static class _HW10_Hidden_Node { 
        int value;
        _HW10_Hidden_Node leftChild;
        _HW10_Hidden_Node rightChild;
    }
    protected static _HW10_Hidden_Node _HW10_convertToHidden(Object node) { 
        if (node == null) return null;
        try {
            _HW10_Hidden_Node convertedNode = new _HW10_Hidden_Node();
            java.lang.reflect.Field valueField = node.getClass().getDeclaredField("value");
            java.lang.reflect.Field leftChildField = node.getClass().getDeclaredField("leftChild");
            java.lang.reflect.Field rightChildField = node.getClass().getDeclaredField("rightChild");
            convertedNode.value = valueField.getInt(node);
            convertedNode.leftChild = _HW10_convertToHidden(leftChildField.get(node));
            convertedNode.rightChild = _HW10_convertToHidden(rightChildField.get(node));
            return convertedNode;
        } catch (Exception e) {
            throw new ASSERT_Exception(e.getMessage());
        }
    }

    static void HW10_printBinaryTree(Object _root) {
        _HW10_Hidden_Node root = _HW10_convertToHidden(_root);
        if (root == null) {
            System.out.println("[HW10_printBinaryTree] root is null");
            return;
        }
        class MagicNode {
            boolean isNull;
            int value;
            int depth;
            _HW10_Hidden_Node leftChild;
            _HW10_Hidden_Node rightChild;
            MagicNode(_HW10_Hidden_Node node, int depth) {
                if (node == null) {
                    isNull = true;
                } else {
                    this.value = node.value;
                    this.leftChild = node.leftChild;
                    this.rightChild = node.rightChild;
                }
                this.depth = depth;
            }
        }

        int maxDepth = _HW10_maxDepthHelper(root, 0);

        System.out.println();

        int[] numNodesAtDepth = new int[maxDepth + 1];
        int depth = -1;
        ArrayDeque<MagicNode> queue = new ArrayDeque<>();
        queue.add(new MagicNode(root, 0));
        while (!queue.isEmpty()) {
            MagicNode curr = queue.remove();

            boolean first = (curr.depth != depth);
            if (first) depth = curr.depth;

            int k = maxDepth - depth;
            int pre   = (1 << (k + 1)) - 2;
            int intra = (1 << (k + 2)) - 1;

            if (first) {
                System.out.println();
                if (depth != 0) { // branches
                                  // a b c b  a
                    int a0 = (1 << (k + 2)) - 2; // FORNOW (pre(k + 1))
                    int a = a0;
                    int b = -1;
                    int c = (1 << (k + 3)) - 1; // FORNOW (intra(k + 1))
                    MagicNode[] _FORNOW = queue.toArray(new MagicNode[0]);
                    ArrayList<MagicNode> FORNOW = new ArrayList<>();
                    FORNOW.add(curr);
                    for (MagicNode _fornow : _FORNOW) FORNOW.add(_fornow);
                    while (b < a0 - 1) {
                        --a;
                        b += 2;
                        c -= 2;
                        _HW10_printSpaces(a);
                        int i_FORNOW = 0;
                        for (int rep = 0; rep < (1 << (depth - 1)); ++rep) {
                            System.out.print((FORNOW.get(i_FORNOW++)).isNull ? ' ' : '/');
                            _HW10_printSpaces(b);
                            System.out.print((FORNOW.get(i_FORNOW++)).isNull ? ' ' : '\\');
                            _HW10_printSpaces(c);
                        }
                        System.out.println();
                    };
                }
                _HW10_printSpaces(pre); // pre
            }

            System.out.print((curr.isNull) ? " " : curr.value);
            _HW10_printSpaces(intra - (_HW10_numDigits(curr.value) - 1)); // intra

            if (curr.depth < maxDepth) {
                queue.add(new MagicNode(curr.leftChild, curr.depth + 1));
                queue.add(new MagicNode(curr.rightChild, curr.depth + 1));
            }
        }
        System.out.println();
    }
    static int _HW10_maxDepthHelper(_HW10_Hidden_Node curr, int level) {
        int left = (curr.leftChild != null) ? _HW10_maxDepthHelper(curr.leftChild, level + 1) : level;
        int right = (curr.rightChild != null) ? _HW10_maxDepthHelper(curr.rightChild, level + 1) : level;
        return Math.max(left, right);
    }
    static void _HW10_printSpaces(int numSpaces) {
        String spaces = "";
        for (int i = 0; i < numSpaces; ++i) spaces += " "; // FORNOW
        System.out.print(spaces);
    }
    static int _HW10_numDigits(int n) {
        if (n == 0) return 1;

        int result = 0;
        if (n < 0) {
            ++result;
            n = -n;
        }
        do {
            ++result;
            n /= 10;
        } while (n != 0);
        return result;
    }


}


class CowJPanelExtender extends JPanel {
    private static final long serialVersionUID = 1L;

    boolean _mousePressed;
    boolean _mouseReleased;
    float _mouseScrollAmount;
    boolean _keyPressed[]  = new boolean[256];
    boolean _keyReleased[] = new boolean[256];

    CowJPanelExtender() {
        super();

        this.addMouseListener( 
                new MouseAdapter() {
                    @Override public void mousePressed(MouseEvent e) { _mousePressed = true; }
                    @Override public void mouseReleased(MouseEvent e) { _mouseReleased = true; }
                });
        this.addMouseWheelListener(
        		new MouseWheelListener() {
                    @Override public void mouseWheelMoved(MouseWheelEvent e) { _mouseScrollAmount += e.getPreciseWheelRotation(); }
        		});
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(event -> {
            synchronized (Cow.class) {
                int _key = event.getKeyCode();
                if (_key >= 256) return false;
                char key = (char) _key;

                if (event.getID() == KeyEvent.KEY_PRESSED) {
                    _keyPressed[key] = true;
                } else if (event.getID() == KeyEvent.KEY_RELEASED) {
                    _keyReleased[key] = true;
                }

                return false;
            }
        });


    }

    @Override
    public void paintComponent(Graphics paintComponentGraphics) { 
        super.paintComponent(paintComponentGraphics);
        while (Cow._buffered_image_graphics == null) {}
        while (Cow._buffered_image == null) {}
        paintComponentGraphics.drawImage(Cow._buffered_image, 0, 0, null);
    }
}
