package unsw.graphics.world;

import java.awt.Color;
import java.io.IOException;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import unsw.graphics.Application3D;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.Texture;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleMesh;


public class Avatar{
	private Point3D position;
	private float myRoty;
	//private float rotation;
	private float x;
	private float y;
	private float z;
	
	private TriangleMesh bunny;
	private Texture avatarTerr;
	//private Terrain myTerr;
	
	
	public Avatar(float x, float y, float z)  {
		
		position = new Point3D(x, y, z);
		System.out.println("bunny created");
	}

   
    public void init(GL3 gl) {
        try{
        		bunny = new TriangleMesh("res/models/bunny_res2.ply", true, true);
        } catch (IOException i) {
        		System.out.println("error with bunny!!!");	
        }
        		//avatarTerr = new Texture(gl, "res/textures/grass.bmp","bmp", true);
        		//gl.glBindTexture(GL.GL_TEXTURE_2D, avatarTerr.getId());
        		Shader.setPenColor(gl, Color.PINK);
        		bunny.init(gl);

    }
    
    
    public void update(float x, float y, float z, float amount) {
    		this.position = new Point3D(x, y, z);
    		this.myRoty = amount;
    }
    
    public Point3D getPosition() {
        return position;
    }
    
    public void destroy(GL3 gl) {
    		bunny.destroy(gl);
    }

    
    public void display(GL3 gl) {
    		Shader.setPenColor(gl, Color.PINK);
        	CoordFrame3D AvaFrame = CoordFrame3D.identity();
        	
        	AvaFrame = AvaFrame.translate(position.getX(), position.getY(), position.getZ()).translate(0,-0.1f,0).rotateY(myRoty).scale(2f, 2f, 2f).rotateY(-90);
        //	System.out.println(position.getX()+ " " + position.getY() + " "+ position.getZ());
        		//AvaFrame = AvaFrame.rotateY(myRoty).translate(position.getX(), position.getY(), position.getZ() + 1f).scale(3f, 3f, 3f);
            bunny.draw(gl, AvaFrame);
         }    
}