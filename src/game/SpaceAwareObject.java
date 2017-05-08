package game;

public abstract class SpaceAwareObject {

	protected float x, y, width, height;
	
	protected abstract void Collision(SpaceAwareObject other);
}
