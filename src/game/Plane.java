package game;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Plane extends Sprite implements Renderable {
	private Game game;
	static final int PLANE_SPEED = 2;

	public Plane(Game game) {
		this.game = game;
		try {
			this.image = ImageIO.read(
					new File("C:\\Users\\home\\Documents\\ParachutersJava\\Parachuters_Java\\Resources\\plane.png"));
			this.width = image.getWidth();
			this.height = image.getHeight();
			this.y = 0;
			this.x = game.getWidth() - this.width;
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	protected void update() {
		this.x -= PLANE_SPEED;
		if (this.x <= 0) {
			this.x = game.getWidth() - this.width;
		}
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(image, this.x, this.y, null);

	}
}
