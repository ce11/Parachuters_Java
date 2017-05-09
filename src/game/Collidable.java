package game;

public interface Collidable {
	public void onCollision(Sprite other);
	public boolean didCollide(Sprite other);
}
