



package unsw.graphics.world;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import unsw.graphics.Application3D;
import unsw.graphics.Matrix4;
import unsw.graphics.Shader;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;

import com.jogamp.newt.event.KeyListener;
import com.jogamp.nativewindow.NativeWindow;
import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseListener;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class World extends Application3D {

    private Terrain terrain;
    
    private Camera camera;
    private float xRotated=0;
    private float yRotated=0;
    private boolean torch;
    //private Avatar avatar;
    
    public World(Terrain terrain) {
    	super("Assignment 2", 1000, 600);
        this.terrain = terrain;
        camera = new Camera(terrain, new Point3D(0,0,9));
        //this.avatar = new Avatar(camera.getPos().getX(),camera.getPos().getY(),camera.getPos().getZ());
    }
    
  
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Terrain terrain = LevelIO.load(new File("res/worlds/test1.json"));
        World world = new World(terrain);
        world.start();
    }

	@Override
	public void display(GL3 gl) {
		super.display(gl);
		
		//camera.avaMaker(gl);
		camera.CreateViewMatrix(gl);
		terrain.display(gl,xRotated,yRotated);
		
		torch = camera.getTorch();

		Shader.setColor(gl, "specularCoeff", new Color(.9f,.9f,.9f));

		float spotCutoff = 15f;
		float spotExp = 64f;
		Shader.setPoint3D(gl, "torchlightPos", camera.getAvatarPos());
		if(!torch) { //if torch off
		 Shader.setInt(gl, "torchOn", 0);
		 Shader.setPoint3D(gl, "torchlightPos", camera.getAvatarPos());
			Shader.setFloat(gl, "spotCutoff", spotCutoff);
			Shader.setFloat(gl, "spotExp", spotExp);
			//Shader.setColor(gl, "sunlightIntensity", new Color(.4f, .4f, .4f));
		 }
		 else { //if torch on
		 
		//Shader.setColor(gl, "sunlightIntensity", new Color(.1f, .1f, .1f));
		 Shader.setColor(gl, "torchlightIntensity", new Color(100, 100, 100));
		 Shader.setInt(gl, "torchOn", 1);
		 
		 }
		
		Shader.setColor(gl, "sunlightIntensity", Color.WHITE);
		
	}

	
	@Override
	public void reshape(GL3 gl, int width, int height) {
        super.reshape(gl, width, height);
        Shader.setProjMatrix(gl, Matrix4.perspective(60, width/(float)height, .1f, 100));
	}
	@Override
	public void destroy(GL3 gl) {
		super.destroy(gl);
		camera.avaDestroy(gl);
		
	}

	@Override
	public void init(GL3 gl) {
		super.init(gl);
//		addKeyListener(this);
		//getWindow().addKeyListener(new AL());
		terrain.init(gl);
		
		getWindow().addKeyListener(camera);
		camera.avaInit(gl);
	}


	// TODO Auto-generated method stub
		
	}
	
//////	private void addKeyListener(World world) {
//////		// TODO Auto-generated method stub
//////		
//////	}
//////
//////
//////	@Override
//////	public void keyPressed(KeyEvent key) {
//////		switch(key.getKeyCode()) {
//////		case KeyEvent.VK_UP:
//////			break;
//////		case KeyEvent.VK_DOWN:
//////			break;
//////		case KeyEvent.VK_RIGHT:
//////			break;
//////		case KeyEvent.VK_LEFT:
//////			break;
//////		}
//////	}
//////	@Override
//////	public void keyReleased(KeyEvent key) {
//////		switch(key.getKeyCode()) {
//////		case KeyEvent.VK_UP:
//////			break;
//////		case KeyEvent.VK_DOWN:
//////			break;
//////		case KeyEvent.VK_RIGHT:
//////			break;
//////		case KeyEvent.VK_LEFT:
//////			break;
//////		}
//////	}
////	
////	

//
//
//
//
//package unsw.graphics.world;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//
//import com.jogamp.opengl.GL;
//import com.jogamp.opengl.GL3;
//
//import unsw.graphics.Application3D;
//import unsw.graphics.Matrix4;
//import unsw.graphics.Shader;
//import unsw.graphics.geometry.Point2D;
//import unsw.graphics.geometry.Point3D;
//
//import com.jogamp.newt.event.KeyListener;
//import com.jogamp.nativewindow.NativeWindow;
//import com.jogamp.newt.event.KeyAdapter;
//import com.jogamp.newt.event.KeyEvent;
//import com.jogamp.newt.event.MouseListener;
//
//
//
///**
// * COMMENT: Comment Game 
// *
// * @author malcolmr
// */
//public class World extends Application3D {
//
//    private Terrain terrain;
//    
//    private Camera camera;
//    private float xRotated=0;
//    private float yRotated=0;
//    
//    
//    public World(Terrain terrain) {
//    	super("Assignment 2", 1000, 600);
//        this.terrain = terrain;
//        camera = new Camera(terrain, new Point3D(0,0,9));
//        
//    }
//    
//  
//    
//    /**
//     * Load a level file and display it.
//     * 
//     * @param args - The first argument is a level file in JSON format
//     * @throws FileNotFoundException
//     */
//    public static void main(String[] args) throws FileNotFoundException {
//        Terrain terrain = LevelIO.load(new File("res/worlds/test1.json"));
//        World world = new World(terrain);
//        world.start();
//    }
//
//	@Override
//	public void display(GL3 gl) {
//		super.display(gl);
//		camera.CreateViewMatrix(gl);
//		terrain.display(gl,xRotated,yRotated);
//		
//	}
//
////	
////	KeyListener listener = new KeyAdapter()
////	{
////	    public void keyPressed(KeyEvent e) {
////	    	switch(e.getKeyCode()) {
////				case KeyEvent.VK_UP:
////					System.out.println("testing, up");
////					break;
////				case KeyEvent.VK_DOWN:
////					break;
////				case KeyEvent.VK_RIGHT:
////					break;
////				case KeyEvent.VK_LEFT:
////					break;
////			}
////	    }
////	};
////	
//	
//	@Override
//	public void reshape(GL3 gl, int width, int height) {
//        super.reshape(gl, width, height);
//        Shader.setProjMatrix(gl, Matrix4.perspective(60, width/(float)height, .1f, 100));
//	}
//	@Override
//	public void destroy(GL3 gl) {
//		super.destroy(gl);
//		
//	}
//
//	@Override
//	public void init(GL3 gl) {
//		super.init(gl);
////		addKeyListener(this);
//		//getWindow().addKeyListener(new AL());
//		terrain.init(gl);
//		getWindow().addKeyListener(camera);
//	
//	}
//	
//	
//
//
//	// TODO Auto-generated method stub
//		
//	}
//	
////////	private void addKeyListener(World world) {
////////		// TODO Auto-generated method stub
////////		
////////	}
////////
////////
////////	@Override
////////	public void keyPressed(KeyEvent key) {
////////		switch(key.getKeyCode()) {
////////		case KeyEvent.VK_UP:
////////			break;
////////		case KeyEvent.VK_DOWN:
////////			break;
////////		case KeyEvent.VK_RIGHT:
////////			break;
////////		case KeyEvent.VK_LEFT:
////////			break;
////////		}
////////	}
////////	@Override
////////	public void keyReleased(KeyEvent key) {
////////		switch(key.getKeyCode()) {
////////		case KeyEvent.VK_UP:
////////			break;
////////		case KeyEvent.VK_DOWN:
////////			break;
////////		case KeyEvent.VK_RIGHT:
////////			break;
////////		case KeyEvent.VK_LEFT:
////////			break;
////////		}
////////	}
//////	
//////	