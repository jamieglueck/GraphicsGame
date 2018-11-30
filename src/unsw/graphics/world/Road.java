package unsw.graphics.world;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;

import unsw.graphics.Matrix4;
import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleMesh;

/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road {

    private List<Point2D> points;
    private float width;
    private Terrain terr;
    private TriangleMesh road;
    
    /**
     * Create a new road with the specified spine 
     *
     * @param width
     * @param spine
     */
    public Road(float width, List<Point2D> spine) {
        this.width = width;
        this.points = spine;
    }

    /**
     * The width of the road.
     * 
     * @return
     */
    public double width() {
        return width;
    }
    
    /**
     * Get the number of segments in the curve
     * 
     * @return
     */
    public int size() {
        return points.size() / 3;
    }

    /**
     * Get the specified control point.
     * 
     * @param i
     * @return
     */
    public Point2D controlPoint(int i) {
        return points.get(i);
    }
    
    /**
     * Get a point on the spine. The parameter t may vary from 0 to size().
     * Points on the kth segment take have parameters in the range (k, k+1).
     * 
     * @param t
     * @return
     */
    public Point2D point(float t) {
        int i = (int)Math.floor(t);
        t = t - i;
        
        i *= 3;
        
        Point2D p0 = points.get(i++);
        Point2D p1 = points.get(i++);
        Point2D p2 = points.get(i++);
        Point2D p3 = points.get(i++);
        

        float x = b(0, t) * p0.getX() + b(1, t) * p1.getX() + b(2, t) * p2.getX() + b(3, t) * p3.getX();
        float y = b(0, t) * p0.getY() + b(1, t) * p1.getY() + b(2, t) * p2.getY() + b(3, t) * p3.getY();        
        
        return new Point2D(x, y);
    }
    
    /**
     * Calculate the Bezier coefficients
     * 
     * @param i
     * @param t
     * @return
     */
    private float b(int i, float t) {
        switch(i) {
        
        case 0:
            return (1-t) * (1-t) * (1-t);

        case 1:
            return 3 * (1-t) * (1-t) * t;
            
        case 2:
            return 3 * (1-t) * t * t;

        case 3:
            return t * t * t;
        }
        
        // this should never happen
        throw new IllegalArgumentException("" + i);
    }

	public void init(GL3 gl) {
		List<Point3D> verts = new ArrayList<Point3D>();
		List<Integer> indys = new ArrayList<Integer>();
		List<Point2D> texCoords = new ArrayList<Point2D>();
		List<Vector3> normals = new ArrayList<Vector3>();
		Point3D w1 = new Point3D(width/2, 0, 0);
		Point3D w2 = new Point3D(-width/2, 0, 0);
		
		float globYVal = getAltitude(); //offset for z fighting
		
		// generate the vertices
		for (float t=0; t < size()-0.004f; t+=0.004f) {
			Point2D first = point(t);
			Point2D secondPoint = point(t + 0.005f);
			
			Vector3 k = new Vector3(secondPoint.getX()-first.getX(), 0,
					secondPoint.getY() - first.getY()).normalize();
			Vector3 i = new Vector3(-k.getZ(), 0, k.getX());
			Vector3 j = new Vector3(0, 1, 0);
			
			float[] values = new float[]{i.getX(), 0, i.getZ(), 0,j.getX(), j.getY(), j.getZ(), 0,k.getX(), k.getY(), k.getZ(), 0,first.getX(), 0, first.getY(), 1};
		
			Matrix4 frenet = new Matrix4(values);
			Point3D p1 = frenet.multiply(w1.asHomogenous()).asPoint3D();
			Point3D p1f = new Point3D(p1.getX(), globYVal, p1.getZ());
			Point2D p2 = new Point2D(p1.getX(), p1.getZ());
			texCoords.add(p2);
			verts.add(p1f);
			normals.add(new Vector3(0, 1, 0));
			
			Point3D p2x = frenet.multiply(w2.asHomogenous()).asPoint3D();
			Point3D p2f = new Point3D(p2.getX(), globYVal, p2x.getZ());
			texCoords.add(new Point2D(p2x.getX(), p2x.getZ()));
			verts.add(p2f);
			normals.add(new Vector3(0, 1, 0));	
		}
		
		for (int i = 0; i < (verts.size())-3; i++) {
			
			indys.add(i);
			indys.add(i+2);
			indys.add(i+3);
			indys.add(i+3);
			indys.add(i+1);
			indys.add(i);
		}
		
		road = new TriangleMesh(verts, normals, indys, texCoords);
		road.init(gl);
	}

	private float getAltitude() {
		System.out.println(controlPoint(0).getX());
		
		Point2D start = controlPoint(0);
		System.out.println("hah");
		
		System.out.println("made it");
		float getX = start.getX();
		float getY = start.getY()+0.005f;
		return terr.altitude(getX, getY); 
		
	}

	public void display(GL3 gl) {
		gl.glPolygonMode(GL3.GL_FRONT_AND_BACK, GL3.GL_TRIANGLES);
		
		if(road.equals(null)) {
			System.out.println("yikes");
		}
		if(road==null) {
			System.out.println("yikes");
		}
		road.draw(gl);
	}
	
	public void setTerrain(Terrain terrain) {
		this.terr = terrain;
	}

}
