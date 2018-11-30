package unsw.graphics.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;


import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleMesh;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class OtherPortal {

    private Point3D position;
    private Point3D new_position;
   
    private TriangleMesh portal;
    
    private static final int NUM_SLICES = 64;
	
    private float height = 2;
    private float radius = 0.5f;
    private TriangleMesh cone;
  
    
    
    public OtherPortal(float x, float y, float z) {
        position = new Point3D(x, y, z);
    }
    
    public Point3D getPosition() {
    	if(Terrain.firstTreePos != null) {
    		float new_x = Terrain.firstTreePos.getX() - 1f;
    		float new_y = Terrain.firstTreePos.getY();
    		float new_z = Terrain.firstTreePos.getZ() + 2f;
    		new_position = new Point3D(new_x,new_y,new_z);
    		position = new_position;
    	}
//    	System.out.println("portal two position: "+ position.getX());
        return position;
    }
    public void init(GL3 gl) {
//    	
         cone = makeCone(gl);
   
    }
    
    private TriangleMesh makeCone(GL3 gl) {
        // Make the approximating triangular mesh.
        List<Point3D> vertices = new ArrayList<Point3D>();
        List<Vector3> normals = new ArrayList<Vector3>();
        List<Integer> indices = new ArrayList<Integer>();
        
        float tIncrement = 1f/NUM_SLICES;
        for (int i = 0; i < NUM_SLICES; i++) {
        	float t = i*tIncrement;
        	vertices.add(new Point3D(0, 0, height));
        
        	float x = getX(t);
            float y = getY(t);
            
            normals.add(new Vector3(x, y, radius/height));
        }
        
        
        for(int i = 0; i < NUM_SLICES; i++) {
           float t = i*tIncrement;
           
           float x = getX(t);
           float y = getY(t);
 	   	  
           vertices.add(new Point3D(getX(t), getY(t), 0));
           
           normals.add(new Vector3(x, y, radius/height));
           
           indices.add(i+NUM_SLICES);
           indices.add((i+1) % NUM_SLICES + NUM_SLICES);
           indices.add(i);
        }
        
        TriangleMesh cone = new TriangleMesh(vertices, normals, indices);
        
        cone.init(gl);
        return cone;
    }
    
    private float getX(float t){
    	return (float) (radius* Math.cos(2 * Math.PI * t));
    	
    }
    
    private float getY(float t){
    	return (float) (radius* Math.sin(2 * Math.PI * t));
    	
    }
  
    public void display(GL3 gl, CoordFrame3D view) {
 
    	CoordFrame3D TFrame = view.translate(getPosition().getX(),getPosition().getY()+.80f ,getPosition().getZ()).translate(0,-0.5f,0).rotateY(180).rotateX(90).scale(1, 1, 1);
        
        Shader.setPenColor(gl, Color.MAGENTA);
        cone.draw(gl, TFrame);
     }    

}
