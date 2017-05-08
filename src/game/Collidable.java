package game;

public interface Collidable {
	public void onCollision(Collidable other);
	public boolean didCollide(Sprite other);
}
