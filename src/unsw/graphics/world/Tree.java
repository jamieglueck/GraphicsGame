package unsw.graphics.world;

import java.awt.Color;
import java.io.IOException;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleMesh;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree {

    private Point3D position;
    private TriangleMesh tree;
    //private Vector3 sunlight;
    
    
    public Tree(float x, float y, float z) {
        position = new Point3D(x, y, z);
    }
    
    public Point3D getPosition() {
        return position;
    }
    public void init(GL3 gl) {
   
        try{
        	tree = new TriangleMesh("res/models/horse.ply", true, true);
        } catch (IOException i) {
        	
        }
    
    	tree.init(gl);
    }
  
    public void display(GL3 gl, CoordFrame3D view) {
    	
    //	CoordFrame3D TFrame = view.translate(getPosition().getX(),getPosition().getY()+0.5f ,getPosition().getZ()).scale(0.1f, 0.1f, 0.1f);
 
    	CoordFrame3D TFrame = view.translate(getPosition().getX(),getPosition().getY()+.54f ,getPosition().getZ()).scale(7f, 7f, 7f).rotateY(90).rotateX(-90);
        tree.draw(gl, TFrame);
     }    

}
