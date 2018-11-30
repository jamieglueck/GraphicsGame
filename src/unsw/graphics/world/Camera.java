package unsw.graphics.world;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.world.Avatar;
import unsw.graphics.Matrix4;

import unsw.graphics.Texture;
import unsw.graphics.geometry.Point2D;

public class Camera implements KeyListener{
		public Point3D mypos;
		private float myRotation;
		private Terrain myTerr;
		private Avatar avatar;
		private static final float speed = .1f;
		private static final float RotSpeed = 1;
		private boolean thirdperson = true;
		private boolean torch = false;
	
	
	public Camera(Terrain terrain, Point3D pos) {
		myTerr = terrain;
		mypos = pos;
		myRotation = 0;
		this.avatar = new Avatar(0,0,9);
	}
	
 	public void keyPressed(KeyEvent e) {
 		
		int keyCode = e.getKeyCode();
		
		if (keyCode == KeyEvent.VK_SPACE) {
			thirdperson = !thirdperson;
		}
		
		if(keyCode == KeyEvent.VK_UP) {
			System.out.println("up key pressed");
			moveForward();
		}
		if(keyCode == KeyEvent.VK_DOWN) {
			System.out.println("down key pressed");
			moveBackward();
		}
		if(keyCode == KeyEvent.VK_LEFT) {
			System.out.println("left key pressed");
			turnLeft();
		}
		if(keyCode == KeyEvent.VK_RIGHT) {
			System.out.println("right key pressed");
			turnRight();
		}
		if(keyCode == KeyEvent.VK_T) {
			System.out.println("torch");
			torch = !torch;

			}
 	}
 	
 	public boolean getTorch() {
 		 return torch;
 		 }
 	
 	public void portalMovement() {
 		
 		Point3D portalPos = new Point3D(Terrain.firstTreePos.getX()-1f,Terrain.firstTreePos.getY(),Terrain.firstTreePos.getZ()+2f);
 		mypos = portalPos;
 		
 	}
 	
public void portalMovementTwo() {
 		
 		Point3D portalPos = new Point3D(Terrain.firstTreePos.getX()+2.5f,Terrain.firstTreePos.getY(),Terrain.firstTreePos.getZ()+6.5f);
 		mypos = portalPos;
 	}
 	
 	  public void moveForward() {
 		 System.out.println("current rotation" + myRotation);
 		 System.out.println("current pos" + mypos.getX() + " "  + mypos.getZ());
 		 float x = (float) (-1*(-1 * speed*Math.sin(-1*Math.toRadians(myRotation))));
 		 float z = (float) (-1*speed*Math.cos(-1*Math.toRadians(myRotation)));
 		 float y = myTerr.altitude(x +mypos.getX(),z +mypos.getZ());
 		 y = y -mypos.getY()+ 1;
 		 System.out.println("current x: "+ x);
 		System.out.println("current y: "+ y);
 		System.out.println("current z: "+ z);
 		 mypos = mypos.translate(x, y, z);
 		 
 		 
 		 //if avatar hits portal
 		 float new_x = Terrain.firstTreePos.getX() + 2f;
 		 float new_x2 = Terrain.firstTreePos.getX() - 1f;
 		 float new_z = Terrain.firstTreePos.getZ() + 6f;
 		 float new_z2 = Terrain.firstTreePos.getZ() + 2f;
 		
 		 if(Math.abs(mypos.getX()-new_x)<0.5f && Math.abs(mypos.getZ()-new_z)<0.5f) {
 			 portalMovement();
 		 }
 	
 		if(Math.abs(mypos.getX()-new_x2)<0.5f && Math.abs(mypos.getZ()-new_z2)<0.5f) {
			 portalMovementTwo();
		 }
 	
 	    }
 	    
 	    public void moveBackward() {
 	    	 	float x = (float) ((speed*Math.sin(Math.toRadians(myRotation))));
 	 		 float z = (float) (speed*Math.cos(Math.toRadians(myRotation)));
  	 		//System.out.println(mypos.getX() + " "  + mypos.getZ());

 	 		 float y = myTerr.altitude(x +mypos.getX(),z +mypos.getZ());
 	 		 y = y -mypos.getY() + 1;
 	 		 mypos = mypos.translate(x, y, z);
 	 		 
 	 		 //if hits portal
 	 		float new_x = Terrain.firstTreePos.getX() + 2f;
 	 		 float new_x2 = Terrain.firstTreePos.getX() - 1f;
 	 		 float new_z = Terrain.firstTreePos.getZ() + 6f;
 	 		float new_z2 = Terrain.firstTreePos.getZ() + 2f;
 	 		 if(Math.abs(mypos.getX()-new_x)<0.5f && Math.abs(mypos.getZ()-new_z)<0.5f) {
 	 			 portalMovement();
 	 		 }
 	 	
 	 		if(Math.abs(mypos.getX()-new_x2)<0.5f && Math.abs(mypos.getZ()-new_z2)<0.5f) {
 				 portalMovementTwo();
 			 }
 	 		//avatar.update(mypos.getX(), mypos.getY(), mypos.getZ());
 	    }
 	    
 	    public void turnLeft() {
 	    	myRotation = myRotation + RotSpeed;
 	    //	avatar.update(mypos.getX(), mypos.getY(), mypos.getZ());
 	    }
 	    
 	    public void turnRight() {
 	   	myRotation = myRotation - RotSpeed;
 	   //avatar.update(mypos.getX(), mypos.getY(), mypos.getZ());
 	    }
 	    
 	    public void CreateViewMatrix(GL3 gl) {
 	    		CoordFrame3D myFrame = CoordFrame3D.identity();
 	    		myFrame = myFrame.rotateY(-myRotation);
 	    		myFrame = myFrame.translate(-1*mypos.getX(),-1*mypos.getY(),-1*mypos.getZ());
 	    		if(thirdperson) {
 	    			myFrame = myFrame.translate((float)-Math.sin(Math.toRadians(myRotation)), -.5f, (float)-Math.cos(Math.toRadians(myRotation)));
 	    		}
 	    		else {
 	    			myFrame = myFrame.translate(0, -.7f, 0);
 	    		}
 	    		Shader.setViewMatrix(gl,myFrame.getMatrix());
 	    		avaMaker(gl);
 	    		//System.out.println(mypos.getZ() + " " + myRotation);
 	    }
 	    public void avaInit(GL3 gl) {
 	    		avatar.init(gl);
 	    }
 	    public void avaMaker(GL3 gl) {
 	    		avatar.update(mypos.getX(), mypos.getY(), mypos.getZ(),myRotation);
	    		avatar.display(gl);
 	    		
	    		
 	    }
 	    
 	    public void avaDestroy(GL3 gl) {
 	    		avatar.destroy(gl);
 	    }
 	    
 	    public Point3D getPos() {
 	    		return mypos;
 	    }
 	    public Point3D getAvatarPos() {
 	    	return avatar.getPosition();
 	    }

	@Override
	public void keyReleased(KeyEvent arg) {
		// TODO Auto-generated method stub
		
	}	
}

