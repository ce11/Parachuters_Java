package game;

public abstract class SpaceAwareObject {

	protected float x, y, width, height;
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	protected abstract void onCollision(SpaceAwareObject other);
	protected abstract void move();
	protected abstract void didCollide(SpaceAwareObject other);
	
}
