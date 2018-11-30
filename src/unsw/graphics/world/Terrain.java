
    

    
    

package unsw.graphics.world;
import java.util.Arrays;
import java.util.List;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleMesh;
import unsw.graphics.Shader;
import unsw.graphics.Texture;
import unsw.graphics.Vector3;


import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;


import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;


/**
 */
public class Terrain {

    private int width;
    private int depth;
    private TriangleMesh terrLay;
    private float[][] altitudes;
    private List<Tree> trees;
    private Portal myPortal;
    private OtherPortal myOtherPortal;
    private List<Road> roads;
    private Vector3 sunlight;
    private Texture terr;
    private Texture horseterr;
    private Texture avatarTerr;
    private Texture roadTexture;
    private Shader shader;
    private Color aInt = new Color(0.8f, 0.8f, 0.8f);
    private Color specCo = new Color(0.9f, 0.9f, 0.9f);
    public static Point3D firstTreePos;
    
    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth, Vector3 sunlight) {
        this.width = width;
        this.depth = depth;
        altitudes = new float[width][depth];
        trees = new ArrayList<Tree>();
        roads = new ArrayList<Road>();
        this.sunlight = sunlight;
    }

    public List<Tree> trees() {
    	if (trees.size() > 0 && trees != null) {
    		firstTreePos = trees.get(0).getPosition();
    	}
    	
        return trees;
    }

    public List<Road> roads() {
    	System.out.println("roads: "+ roads.size());
        return roads;
    }

    public Vector3 getSunlight() {
        return sunlight;
    }

    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        sunlight = new Vector3(dx, dy, dz);      
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return altitudes[x][z];
    }

    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, float h) {
        altitudes[x][z] = h;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * @param x
     * @param z
     * @return
     */
    
    public float altcalc(double xr, double a0, double a1) {
    		double alt = (1 - xr)* a0 + xr * a1;
    		return (float)alt;
    }
    
    public float altitude(float x, float z) {
        float altitude = 0;
        // TODO: Implement this
        if(x<0) {
        	return 0;
        }
        if(x>=width) {
        	return 0;
        }
        
        if(z< 0) {
        	return 0;
        }
        
        if(z>=depth) {
        	return 0;
        }
        
        //double xceil = Math.ceil(x);
        //double xfloor = Math.floor(x);
        //double zceil = Math.ceil(z);
        //double zfloor = Math.floor(z);
        //does floor and ceil do same thing as casting as int and just taking remainder
        
        
        int x0 = (int)x; 
        int z0 = (int)z;
        double zr = z - z0;
        double xr = x - x0;
        
       
        if (xr != 0 && zr != 0) {
        	int x1 = x0 +1;
        	double a0 = getGridAltitude(x0, z0);
        	double a1 = getGridAltitude(x1, z0);
       
        	altitude= altcalc(xr,a0,a1);
        } 
        
        else if(zr != 0) {
        	int z1 = z0 +1;
        	if (z1 >= depth) {
        		return 0; //TODO
        	}
        	double a0 = getGridAltitude(x0, z0);
        	double a1 = getGridAltitude(x0, z1);
        	altitude= altcalc(zr,a0,a1);
        } 
        
        else if (xr != 0) {
        	int x1 = x0 +1;
        	if (x1 >= width) {
        		return 0; //TODO
        	}
        	double a0 = getGridAltitude(x0, z0);
        	double a1 = getGridAltitude(x1, z0);
        	altitude= altcalc(xr,a0,a1);
        } 
        
        //this is just the base case where we just take it as it is
        else if(xr ==0 && zr == 0){
        	altitude = (float) getGridAltitude(x0, z0);
        }
        return altitude;
    }
    

    
    public void initPortal(GL3 gl)  {
    	myPortal = new Portal(20,20,20);
    	myOtherPortal = new OtherPortal(20,20,20);
  		myPortal.init(gl);
  		myOtherPortal.init(gl);
  }

    
    private void drawPortal(GL3 gl, CoordFrame3D view) {
//    	gl.glBindTexture(GL.GL_TEXTURE_2D, avatarTerr.getId());
        myPortal.display(gl,view);
        myOtherPortal.display(gl, view);
    }

    public void initTree(GL3 gl, List<Tree> trees) {
  		for (Tree t: trees) {
  			t.init(gl);
  }
  }
    
    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(float x, float z) {
        float y = altitude(x, z);
        Tree tree = new Tree(x, y, z);
        trees.add(tree);
        if (trees.size() > 0 && trees != null) {
    		firstTreePos = trees.get(0).getPosition();
    	}
    }

    private void drawTrees(GL3 gl, CoordFrame3D view) {
    	gl.glBindTexture(GL.GL_TEXTURE_2D, horseterr.getId());
        for(Tree t : trees){
        		t.display(gl, view);
        }
    }
    
    
    public void initRoads(GL3 gl, List<Road> roads) {
//    	System.out.println("roads" + roads.size() );
    	
    		for (Road road : roads) {
    			road.setTerrain(this);
    			road.init(gl);
    			
    		}
  } 
    
    
    /**
     * Add a road 
     * 
     * @param x
     * @param z
     */
    public void addRoad(float width, List<Point2D> spine) {
        Road road = new Road(width, spine);
        roads.add(road);   
    }
    
    private void drawRoads(GL3 gl, CoordFrame3D view) {
    	gl.glBindTexture(GL.GL_TEXTURE_2D, roadTexture.getId());
    	
        for(Road r : roads){
        	
//        	r.display(gl, this.width, this.depth, view);
        	
        	r.display(gl);
        }
    }
    
    private void initMesh(GL3 gl) {
    	//when u read !again out loud its like not again! ha ha isnt that funny carolyn
    boolean again = true;
    terr = new Texture(gl, "res/textures/sky.bmp","bmp", true);
    horseterr = new Texture(gl, "res/textures/BrightPurpleMarble.png","png", true);
   avatarTerr = new Texture(gl, "res/textures/grass.bmp","bmp", true);
    roadTexture = new Texture(gl, "res/textures/rock.bmp","bmp", true);
    List<Point2D> textys = new ArrayList<>();
    List<Integer> indyList = new ArrayList<>();
    List<Point3D> points = new ArrayList<>();
        
   for(int x = 0; x < width-1; x ++) {
	   
         int counterfirst = 0;
         
        	for(int z = 0; z < depth; z ++) {
        		points.add(new Point3D(x, (float)getGridAltitude(x,z), z));	
        		textys.add(new Point2D(x,z));
        			
        		
        		if (counterfirst != 0 && !again) {
        			int xmin = x -1;
        			int zmin = z-1;
        			
        			//double [] a0 = {x, altitude[x][z], z};
        			///double [] a1 = {x+1, altitude[x+1][z], z};
        			//double [] a2 = {x, altitude[x][z+1], z+1};
        			//double [] a3 = {x+1, altitude[x+1][z+1], z+1};
        			
        			
        			
	        		indyList.add(depth*(xmin) + (zmin));
	        		
	        		//nested 4loop adds incr
	        		
	        		indyList.add(depth*(xmin) + z);
	        		indyList.add(depth*x + (zmin));
	        		indyList.add(depth*x + (zmin));
	        		indyList.add(depth*(xmin) +  z);
	        		indyList.add(depth* x + z);
	        		
        		} else {
        			counterfirst++;
        		}
        		
        	}	
        again = false;
        	}
            terrLay = new TriangleMesh(points, indyList, true, textys);
            terrLay.init(gl);
	     
    }
    
        
    public void init(GL3 gl) { 	
        Shader shader = new Shader(gl, "shaders/vertex_tex_phong.glsl",
                "shaders/myshaderfun.glsl");
        shader.use(gl);
        
        Shader.setPoint3D(gl, "lightPos", sunlight.asPoint3D());
        Shader.setColor(gl, "ambientCoeff", Color.WHITE);
//        Shader.setColor(gl, "lightIntensity", Color.WHITE);
       
        
        Shader.setColor(gl, "specularCoeff", specCo);
        
        //this is unfinished we need to get the make the sunlight adjustable somehow
        
        
        Shader.setColor(gl, "diffuseCoeff", aInt);
        Shader.setColor(gl, "ambientIntensity", aInt);
        Shader.setFloat(gl, "phongExp", 16f);
        
        initMesh(gl);
        initTree(gl, trees);
        initPortal(gl);
        initRoads(gl, roads);
    }  
    

    public void display(GL3 gl, float rotateX, float rotateY) {
        Shader.setPenColor(gl, Color.white);
        
        
        Shader.setInt(gl, "tex", 0);
        gl.glActiveTexture(GL.GL_TEXTURE0);
        CoordFrame3D view = CoordFrame3D.identity();
   
    	// terr = new Texture(gl, "res/textures/kittens.jpg","jpg", true);
    

       //}
        draw1(gl,view);
        drawTrees(gl,view);
        
        drawPortal(gl,view);
        drawRoads(gl, view);

//        drawAvatar(gl, view);
    }
   

    private void draw1(GL3 gl, CoordFrame3D view) {
    	//terr = new Texture(gl, "res/textures/rock.bmp","bmp", true);
        gl.glBindTexture(GL.GL_TEXTURE_2D, terr.getId());
        terrLay.draw(gl, view);
    }
}