package org.alan;

import org.alan.moreFrames.ColorSliders;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import com.sun.opengl.util.GLUT;
import java.nio.FloatBuffer;

/**
 * GLRenderer.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel) <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class GLRenderer implements GLEventListener {

    private GLU glu = new GLU();
    private GLUT glut = new GLUT();
    private AlanDemo frame;
    private ColorSliders colorSliders;
    
    private int arrowDirection;
    private int verticalSpeed;
    private int horizontalSpeed;
    private int rotation;
    private boolean stop;
    private float[] rgb;

    private boolean lightOn = true;
    
    // Planet demo lighting globals
    FloatBuffer lightEmission;
    FloatBuffer lightDiffuseBlue;
    FloatBuffer lightDiffuseYellow;
    FloatBuffer lightDiffuseWhite;
    FloatBuffer sunLightPosition;
    FloatBuffer lightZero;
    
    public GLRenderer(AlanDemo frame){
        this.frame = frame;
        frame.setRenderer(this);
        colorSliders = frame.getColorSliders();
        arrowDirection = -1;
        verticalSpeed = 0;
        horizontalSpeed = 0;
        rotation = 0;
        stop = true;
        
    }
    
    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));

        GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());

        // Enable VSync
        gl.setSwapInterval(1);

        // Setup the drawing area and shading mode
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
        
        // Lighting
        float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
        float mat_shininess[] = { 50.0f };
        float light_position[] = { 1.0f, 1.0f, 1.0f, 0.0f };
        FloatBuffer lightSpecular = FloatBuffer.wrap(mat_specular);
        FloatBuffer lightShininess = FloatBuffer.wrap(mat_shininess);
        FloatBuffer lightPosition = FloatBuffer.wrap(light_position);
        
        // Planet demo lighting
       	float matEmission[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	float matDiffuseBlue[] = { 0.0f, 0.0f, 1.0f, 1.0f };
	float matDiffuseYellow[] = { 1.0f, 1.0f, 0.0f, 1.0f };
	float matDiffuseWhite[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	float matSunLightPosition[] = { 0.0f, 0.0f, 0.0f, 1.0f };
	float matLightZero[] = { 0.0f, 0.0f, 0.0f, 1.0f };
	lightEmission = FloatBuffer.wrap(matEmission);
	lightDiffuseBlue = FloatBuffer.wrap(matDiffuseBlue);
	lightDiffuseYellow = FloatBuffer.wrap(matDiffuseYellow);
	lightDiffuseWhite = FloatBuffer.wrap(matDiffuseWhite);
	sunLightPosition = FloatBuffer.wrap(matSunLightPosition);
	lightZero = FloatBuffer.wrap(matLightZero);
        
        gl.glClearColor (0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel (GL.GL_SMOOTH);

        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, lightSpecular);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, lightShininess);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPosition);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        if (height <= 0) { // avoid a divide by zero error!
        
            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(GLAutoDrawable drawable) {
        update();
        
        GL gl = drawable.getGL();
        gl.glDisable(GL.GL_LIGHTING);
        gl.glDisable(GL.GL_LIGHT0);
        gl.glDisable(GL.GL_DEPTH_TEST);        

        // Clear the drawing area
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        // Reset the current matrix to the "identity"
        gl.glLoadIdentity();

        // Move the "drawing cursor" around
        gl.glTranslatef(-1.5f, 0.0f, -6.0f);

        gl.glRotatef(frame.rotateX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(frame.rotateY, 0.0f, 1.0f, 0.0f);

        switch(frame.demo){
            case 1:
                demo1(gl);
                break;
            case 2:
                demo2(gl);
                break;
            case 3:
                demo3(gl);
                break;
            case 4:
                demo4(gl);
                break;
            case 5:
                demo5(gl);
                break;
            case 6:
                demo6(gl);
                break;
            case 7:
                demo7(gl);
                break;
            default:
                demo1(gl);
        }

        // Flush all drawing operations to the graphics card
        gl.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
    
    private void demo1(GL gl){
        if ((int)frame.secs % 2 == 0) {  
            // Move the "drawing cursor" to another position
            gl.glTranslatef(0.0f, 0.0f, 0.0f);
            
            // Drawing Using Triangles
            gl.glBegin(GL.GL_TRIANGLES);
            gl.glColor3f(1.0f, 0.0f, 0.0f);    // Set the current drawing color to red
            gl.glVertex3f(0.0f, 1.0f, 0.0f);   // Top
            gl.glColor3f(0.0f, 1.0f, 0.0f);    // Set the current drawing color to green
            gl.glVertex3f(-1.0f, -1.0f, 0.0f); // Bottom Left
            gl.glColor3f(0.0f, 0.0f, 1.0f);    // Set the current drawing color to blue
            gl.glVertex3f(1.0f, -1.0f, 0.0f);  // Bottom Right
            // Finished Drawing The Triangle
            gl.glEnd();

            // Move the "drawing cursor" to another position
            gl.glTranslatef(3.0f, 0.0f, 0.0f);
            // Draw A Quad
            gl.glBegin(GL.GL_QUADS);
            gl.glColor3f(0.5f, 0.5f, 1.0f);    // Set the current drawing color to light blue
            gl.glVertex3f(-1.0f, 1.0f, 0.0f);  // Top Left
            gl.glVertex3f(1.0f, 1.0f, 0.0f);   // Top Right
            gl.glVertex3f(1.0f, -1.0f, 0.0f);  // Bottom Right
            gl.glVertex3f(-1.0f, -1.0f, 0.0f); // Bottom Left
            // Done Drawing The Quad
            gl.glEnd();
        }
        else {
            // Move the "drawing cursor" to another position
            gl.glTranslatef(3.0f, 0.0f, 0.0f);
            
            // Drawing Using Triangles
            gl.glBegin(GL.GL_TRIANGLES);
            gl.glColor3f(0.0f, 1.0f, 1.0f);    // Set the current drawing color to red
            gl.glVertex3f(0.0f, -1.0f, 0.0f);   // Top
            gl.glColor3f(1.0f, 0.0f, 1.0f);    // Set the current drawing color to green
            gl.glVertex3f(-1.0f, 1.0f, 0.0f); // Bottom Left
            gl.glColor3f(1.0f, 1.0f, 0.0f);    // Set the current drawing color to blue
            gl.glVertex3f(1.0f, 1.0f, 0.0f);  // Bottom Right
            // Finished Drawing The Triangle
            gl.glEnd();

            // Move the "drawing cursor" to another position
            gl.glTranslatef(-3.0f, 0.0f, 0.0f);
            // Draw A Quad
            gl.glBegin(GL.GL_QUADS);
            gl.glColor3f(0.5f, 0.5f, 0.0f);    // Set the current drawing color to dark yellow!!!
            gl.glVertex3f(-1.0f, 1.0f, 0.0f);  // Top Left
            gl.glVertex3f(1.0f, 1.0f, 0.0f);   // Top Right
            gl.glVertex3f(1.0f, -1.0f, 0.0f);  // Bottom Right
            gl.glVertex3f(-1.0f, -1.0f, 0.0f); // Bottom Left
            // Done Drawing The Quad
            gl.glEnd();
        }

    }

    private void demo2(GL gl){
        if (!stop)
            rotation++;
        
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);
        gl.glEnable(GL.GL_DEPTH_TEST);
        
        //Color of the cube changes according to the speed and angle.
        float fluctuatingRed = Math.abs((float)((verticalSpeed * rotation) % 10)/10);
        float fluctuatingGreen = 1.0f - Math.abs((float)((horizontalSpeed * rotation) % 10)/10);
        float fluctuatingBlue = Math.abs((fluctuatingRed - fluctuatingGreen));
        
        setRGB(fluctuatingRed, fluctuatingGreen, fluctuatingBlue);
        colorSliders.sliderPresentation(rgb);
        
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, FloatBuffer.wrap(rgb));
        
        gl.glPushMatrix();
        gl.glTranslatef(1.5f, 0.0f, 0.0f);

        gl.glRotatef((float)((rotation * verticalSpeed) % 360), 1.0f, 0.0f, 0.0f);
        gl.glRotatef((float)((rotation * horizontalSpeed) % 360), 0.0f, 1.0f, 0.0f);
        
        glut.glutSolidCube(3.0f);
        gl.glPopMatrix();
    }

    private void demo3(GL gl){
    }

    private void demo4(GL gl){
    }

    private void demo5(GL gl){
    }

    private void demo6(GL gl){
        if((int)frame.secs % 3 == 0 && frame.frames % frame.FPS == 0){
            lightOn = lightOn?false:true;
        }
        if(lightOn){
            gl.glEnable(GL.GL_LIGHTING);
            gl.glEnable(GL.GL_LIGHT0);
            gl.glEnable(GL.GL_DEPTH_TEST);
        }
        else{
            gl.glDisable(GL.GL_LIGHTING);
            gl.glDisable(GL.GL_LIGHT0);
            gl.glDisable(GL.GL_DEPTH_TEST);
        }
        gl.glColor3f(0.8f, 0.8f, 0.8f);

        gl.glPushMatrix();
        gl.glTranslatef(-1.0f, 0.0f, 0.0f);
        glut.glutSolidSphere(0.75, 50, 45);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(2.0f, 0.0f, 0.0f);
        float rot = 360 * (frame.secs / 2.0f - (float)Math.floor(frame.secs)); // 180 degrees per second
        gl.glRotatef(rot, 0.0f, 1.0f, 0.0f);
        glut.glutSolidTeapot(1.0f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(5.0f, 0.0f, 0.0f);
        gl.glRotatef(-rot, 0.0f, 1.0f, 0.0f);
        gl.glScalef(0.5f, 0.5f, 0.5f);
        glut.glutSolidDodecahedron();
        gl.glPopMatrix();

    }

    private void demo7(GL gl){
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);
        gl.glEnable(GL.GL_DEPTH_TEST);
        
        gl.glTranslatef(1.5f, 0.0f, 0.0f); // adjust center
        gl.glScalef(1.5f, 1.5f, 1.5f); // adjust scale
        float rot180 = 360 * (frame.secs / 2.0f - (float)Math.floor(frame.secs)); // 180 degrees per second
        float rot90 = 360 * (frame.secs / 4.0f - (float)Math.floor(frame.secs)); // 90 degrees per second
        float rot45 = 360 * (frame.secs / 8.0f - (float)Math.floor(frame.secs)); // 45 degrees per second

        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, sunLightPosition);
        gl.glLightf(GL.GL_LIGHT0, GL.GL_SPOT_CUTOFF, 180.0f);

        gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, lightEmission);
        glut.glutSolidSphere(0.75, 50, 45);   // draw sun
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, lightZero);

        gl.glPushMatrix();
        gl.glRotatef (rot45, 0.0f, 1.0f, 0.0f); // planet revolution
        gl.glTranslatef (2.0f, 0.0f, 0.0f);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, lightDiffuseBlue);
        glut.glutSolidSphere(0.2, 20, 16);
        // draw planet
        gl.glRotatef(rot90, 0.0f, 1.0f, 0.0f); // moon revolution
        gl.glTranslatef(0.4f, 0.0f, 0.0f);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, lightDiffuseWhite);
        glut.glutSolidSphere(0.05, 10, 8);     // draw moon
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef (rot90, 1.0f, 1.0f, 1.0f);  // teapot revolution
        gl.glTranslatef (2.0f, 0.0f, 0.0f);
        gl.glRotatef (rot180, 0.0f, 1.0f, 0.0f); // teapot rotation
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, lightDiffuseYellow);
        glut.glutSolidTeapot(0.1); // draw teapot
        gl.glPopMatrix();
    }
    
    private void update(){
        frame.frames++;
        frame.setSecs((float)frame.frames / (float)frame.FPS);
    }
    
    //For radio button 2
    public void setArrowDirection(int arrowDirection) {
        this.arrowDirection = arrowDirection;
    }
    
    //For radio button 2
    public void changeSpeed() {
        stop = false;
        
        switch (arrowDirection) {
            case 0:
                if (verticalSpeed > -90)
                    verticalSpeed--;
                break;
            case 1:
                if (horizontalSpeed < 90)
                    horizontalSpeed++;
                break;
            case 2:
                if (verticalSpeed < 90)
                    verticalSpeed++;
                break;
            case 3:
                if (horizontalSpeed > -90)
                    horizontalSpeed--;
                break;
        }  
    }
    
    //For radio button 2
    public void stopContinue(boolean go) {
        stop = !go;
    }
    
    //For radio button 2
    public void reset() {
        arrowDirection = -1;
        verticalSpeed = 0;
        horizontalSpeed = 0;
        rotation = 0;
        stop = true;
    }
    
    //For radio button 2
    public int getVerticalSpeed() {
        return verticalSpeed;
    }

    //For radio button 2
    public int getHorizontalSpeed() {
        return horizontalSpeed;
    }
    
    //For radio button 2
    public void setRGB(float r, float g, float b){
        rgb = new float[3];
        
        rgb[0] = r;
        rgb[1] = g;
        rgb[2] = b;
    }
    
    //For radio button 2
    public float[] getRGB() {
        return rgb;
    }
}

