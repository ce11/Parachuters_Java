package game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game implements Runnable{

	static final int VIEW_WIDTH = 1000;
	static final int VIEW_HEIGHT = 700;
	long desiredFPS = 60;
	long desiredDeltaLoop = (1000*1000*1000)/desiredFPS;

	boolean running = true;
	JFrame frame;
	Canvas canvas;
	BufferStrategy bufferStrategy;
	LinkedList<RenderableObject> renderableObjects;
	LinkedList<SpaceAwareObject> spaceAwareObjects;
	
	public Game(){
		frame = new JFrame("Basic Game");

		JPanel panel = (JPanel) frame.getContentPane();
		panel.setPreferredSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
		panel.setLayout(null);

		canvas = new Canvas();
		canvas.setBounds(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
		canvas.setIgnoreRepaint(true);

		panel.add(canvas);

		canvas.addMouseListener(new MouseAdapter() {
			// TODO: put mouse logic, the boat will go towards the mouse x
		});

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);

		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();

		canvas.requestFocus();
		
		renderableObjects = new LinkedList<>();
		spaceAwareObjects = new LinkedList<>();
		// adding objects
		Board board = new Board();
		renderableObjects.add(board);

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
	// TODO: remove this
	private double x = 0;
	private void render() {
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		g.clearRect(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
		render(g);
		g.dispose();
		bufferStrategy.show();
	}

	protected void render(Graphics2D g){
		
		for(RenderableObject renderableObject : renderableObjects){
			renderableObject.render(g);
		}
		g.fillRect((int)x, 0, 200, 200);
	}
	protected void update(int deltaTime){
		x += deltaTime * 0.2;
		while(x > 500){
			x -= 500;
		}
	}
	public static void main(String [] args){
		Game ex = new Game();
		new Thread(ex).start();
	}
}
