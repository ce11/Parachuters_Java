package game;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Parachuter extends Sprite implements Renderable{
	private int dropSpeed = 1;
	private Game game;
	public Parachuter(Game game, int startX) {
		this.game = game;
		this.x = startX;
		try {
			this.image = ImageIO.read(new File("C:\\Users\\home\\Documents\\ParachutersJava\\Parachuters_Java\\Resources\\parachutist.png"));
			this.height = image.getHeight();
			this.width = image.getWidth();
			this.y = 40;
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	@Override
	protected void update() {
		this.y += dropSpeed;
	}

	@Override
	public boolean didLeavePlay() {
		if(this.y + this.height >= game.getHeight()){
			game.removeLife();
			return true;
		}
		return false;
	}
	
	@Override
	public void render(Graphics2D g) {
		g.drawImage(image, this.x, this.y, null);		
	}
}
