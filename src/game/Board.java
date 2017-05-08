package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Board implements RenderableObject {
    BufferedImage backdrop, sea = null;
    public Board(){
    	try{
    		backdrop = ImageIO.read(new File("C:\\Projects\\MavenSandbox\\src\\main\\resources\\img.jpg"));
    		sea = ImageIO.read(new File("C:\\Projects\\MavenSandbox\\src\\main\\resources\\img.jpg"));
    	}catch(IOException ex){
    		
    	}
    }
	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
	}

}
