package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Board implements Renderable {
	static final int SEA_HEIGHT = 600;
    BufferedImage backdrop, sea = null;
    public Board(){
    	try{
    		backdrop = ImageIO.read(new File("./Resources/background.png"));
    		sea = ImageIO.read(new File("./Resources/sea.png"));
    	}catch(IOException ex){
    		System.out.println("Failed to load board resources");
    	}
    }
	@Override
	public void render(Graphics2D g) {
		
		g.drawImage(backdrop, 0, 0, null);
		g.drawImage(sea, 0, SEA_HEIGHT, null);
	}

}
