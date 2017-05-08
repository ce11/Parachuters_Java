package game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;
import java.util.function.Predicate;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Game implements Runnable{

	static final int VIEW_WIDTH = 1000;
	static final int VIEW_HEIGHT = 700;
	static final int SPAWN_INCREASE_RATE = 10;
	static final int SCORE_INCREMENT = 10;
	static final int INITAL_LFE_COUNT = 3; 
	static final String GAME_OVER_TEXT = "GAME OVER";

	private int spawnFrames = 100;
	private int timesSpawned = 0;
	private int spawnCounter = 0;
	private int lives = INITAL_LFE_COUNT;
	long desiredFPS = 60;
	long desiredDeltaLoop = (1000*1000*1000)/desiredFPS;

	boolean running = true;
	JFrame frame;
	Canvas canvas;
	BufferStrategy bufferStrategy;
	LinkedList<Renderable> renderableObjects;
	LinkedList<Sprite> updateableObjects;
	private Plane plane;
	private Boat boat;
	private int score = 0;
	
	public Game(){
		frame = new JFrame("Basic Game");
		renderableObjects = new LinkedList<>();
		updateableObjects = new LinkedList<>();
		// adding objects
		Board board = new Board();
		renderableObjects.add(board);
		boat = new Boat(this);
		renderableObjects.add(boat);
		plane = new Plane(this);
		renderableObjects.add(plane);
		updateableObjects.add(plane);
		updateableObjects.add(boat);
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setPreferredSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
		panel.setLayout(null);

		canvas = new Canvas();
		canvas.setBounds(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
		canvas.setIgnoreRepaint(true);
		   
		panel.add(canvas);
		canvas.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				boat.keyPresssed(e);
			}
		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);

		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();

		canvas.requestFocus();
	}

	@Override
	public void run() {
		long beginLoopTime;
		long endLoopTime;
		long currentUpdateTime = System.nanoTime();
		long lastUpdateTime;
		long deltaLoop;

		while(running){
			beginLoopTime = System.nanoTime();
			
			render();
			
			lastUpdateTime = currentUpdateTime;
			currentUpdateTime = System.nanoTime();
			update((int) ((currentUpdateTime - lastUpdateTime)/(1000*1000)));
			checkCollisions();
			endLoopTime = System.nanoTime();
			deltaLoop = endLoopTime - beginLoopTime;

			if(deltaLoop > desiredDeltaLoop){
				System.err.println("Missed frame");
			}else{
				try{
					Thread.sleep((desiredDeltaLoop - deltaLoop)/(1000*1000));
				}catch(InterruptedException e){
					//Do nothing
				}
			}
		}
	}

	private void render() {
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		g.clearRect(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
		render(g);
		g.dispose();
		bufferStrategy.show();
	}
	Font defaultFont = new Font("Verdana", Font.BOLD, 12);
	Font errorFont = new Font("Verdana", Font.BOLD, 40);
	FontMetrics errorFontMetrics;
	protected void render(Graphics2D g){
		for(Renderable renderableObject : renderableObjects){
			renderableObject.render(g);
		}
		// draw scores & lives
		g.setFont(defaultFont);

		g.drawString("SCORE: "+ score , 10, 30);
		g.drawString("LIVES: "+ lives, 10, 60);
		if(lives <= 0){
			g.setFont(errorFont);
			if(errorFontMetrics == null){				
				errorFontMetrics= g.getFontMetrics(errorFont);
			}
			int x = (VIEW_WIDTH- errorFontMetrics.stringWidth(GAME_OVER_TEXT)) / 2;
		    int y = ((VIEW_HEIGHT- errorFontMetrics.getHeight()) / 2) + errorFontMetrics.getAscent();

			g.drawString(GAME_OVER_TEXT,  x, y);
		}

	}
	protected void update(int deltaTime){
		for(Sprite sprite : updateableObjects){
			sprite.update();
		}
		//TODO should this get its own method?
		spawnCounter++;
		if(spawnCounter > spawnFrames && lives > 0){
			spawnCounter = 0;
			timesSpawned++;
			spawnFrames -= SPAWN_INCREASE_RATE / timesSpawned;
			timesSpawned++;
			spawnParachuter();
		}
	}

	private void removeLife(){
		lives--;
		if(lives <= 0){
			// removing all parachuters
			renderableObjects.removeIf((Renderable i)->{return i instanceof Parachuter;});
			updateableObjects.removeIf((Sprite i)->{return i instanceof Parachuter;});
		}
	}
	
	private void addPoints(){
		score += SCORE_INCREMENT;
	}
	protected void checkCollisions(){
		// Find if boat collides with parachuters
		// TODO make this global and clear each time?
		LinkedList<Sprite> toRemove = new LinkedList<>();
		
		for(Sprite sprite: updateableObjects){
			// check is collideable and not boat
			if(sprite instanceof Collidable && !(sprite instanceof Boat)){
				if(boat.didCollide(sprite)){
					// TODO should this happen inside the parachuter?
					toRemove.add(sprite);
					addPoints();
				}
				if(sprite.getY() + sprite.getHeight() > VIEW_HEIGHT){
					toRemove.add(sprite);
					removeLife();
				}
			}
		}
		updateableObjects.removeAll(toRemove);
		renderableObjects.removeAll(toRemove);
		
	}
	// TODO should this be a method of plane?
	private void spawnParachuter(){
		Parachuter parachuter = new Parachuter(this, plane.getX());
		renderableObjects.add(parachuter);
		updateableObjects.add(parachuter);
		
	}
	
	public int getWidth(){
		return VIEW_WIDTH;
	}
	public int getHeight(){
		return VIEW_HEIGHT;
	}
	public static void main(String [] args){
		Game ex = new Game();
		new Thread(ex).start();
	}
}
