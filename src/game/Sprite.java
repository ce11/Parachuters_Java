package game;

import java.awt.image.BufferedImage;

public abstract class Sprite {

	protected int x, y, width, height;
    BufferedImage image;

	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	protected abstract void update();
	
}
