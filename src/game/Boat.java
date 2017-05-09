package game;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;

public class Boat extends Sprite implements Renderable, Collidable{
	static final int boatSpeed = 3;
	private int dx = 0;
	private Game game;
	public Boat(Game game){
		this.game = game;
		try {
			this.image = ImageIO.read(new File("C:\\Users\\home\\Documents\\ParachutersJava\\Parachuters_Java\\Resources\\boat.png"));
			this.width = image.getWidth();
			this.height = image.getHeight();
			this.y = game.getHeight() - this.height;
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		this.x = game.getWidth()/2;
	}
	
	@Override
	protected void update() {
		// TODO Auto-generated method stub
		if(this.x + dx * boatSpeed > 0 && this.x + dx * boatSpeed + this.width < game.getWidth()){			
			this.x += dx * boatSpeed;
			// TODO: should this happen here or in render?
			if(dx > 0 && !isFacingRight){
				flipImage();
			}else if(dx < 0 && isFacingRight){
				flipImage();
			}
		}
		dx = 0;
	}
boolean isFacingRight = false;
	private void flipImage(){
		// Flip the image horizontally
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-image.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = op.filter(image, null);
		isFacingRight = !isFacingRight;
	}
	
	@Override
	public void onCollision(Sprite other) {
		game.addPoints();
		// TODO: should this be here
		if(other instanceof Parachuter){
			game.destroySprite((Parachuter)other);
		}
	}

	@Override
	public boolean didCollide(Sprite other) {
		 Rectangle myRect = new Rectangle(this.x, this.y, this.width, this.height);
		 Rectangle otherRect = new Rectangle(other.getX(), other.getY(), other.getWidth(), other.getHeight());
		 return myRect.intersects(otherRect);
	}

	public void didCollide(List<Sprite> otherSprites){
		List<Sprite> toDestroy = new LinkedList<Sprite>();
		for(Sprite sprite : otherSprites){
			if(this.didCollide(sprite) && sprite instanceof Parachuter){
				toDestroy.add((Parachuter)sprite);
				System.out.println("bam");
			}
		}
		if(toDestroy.size() > 0){
			game.addPoints(toDestroy.size());
			game.destroySprite(toDestroy);	
		}		
	}
	
	@Override
	public void render(Graphics2D g) {
		g.drawImage(image, this.x, this.y, null);
		
	}

	public void keyPresssed(KeyEvent e){
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			dx = -1;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			dx = 1;
	}
}
