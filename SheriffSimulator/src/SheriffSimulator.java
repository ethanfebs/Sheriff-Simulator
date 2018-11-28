//Ethan Febinger
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;
import java.math.*;
import java.awt.image.*;
import java.applet.*;
import javax.swing.border.*;
import javax.imageio.ImageIO;
import java.util.*;
import java.awt.Font.*;

import java.net.URL;
import javax.sound.sampled.*;

public class SheriffSimulator extends JPanel implements MouseListener,KeyListener,Runnable
{
	private float angle;
	private int x;
	private int y;
	private JFrame frame;
	Thread t;
	private boolean gameOn;
	BufferedImage wildWestImage;
	BufferedImage backgroundImage;
	BufferedImage guy;
	BufferedImage flipGuy;
	BufferedImage wantedPoster;
	BufferedImage info;
	BufferedImage bwGuy;
	ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	ArrayList<Sprite> savedSprites = new ArrayList<Sprite>();
	boolean restart=false;
	int imgCount=0;
	boolean right=false;
	boolean showInfo=false;
	boolean left=false;

	boolean pregame;

	int points;
	int wantedSprite;

	double timer;
	int gameLength;

	Font font,smallFont;


	public SheriffSimulator()
	{
		frame=new JFrame();
		x=2150;
		y=0;
		points=0;
		gameLength=60;

		gameOn=false;
		pregame=true;

		try{

		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("Yellowjacket.ttf");
		font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(72f);
		smallFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(36f);

		}catch(Exception e){

		}

		try{

		// Open an audio input stream.
		URL url = this.getClass().getClassLoader().getResource("CowboyTheme.wav");
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
		// Get a sound clip resource.
		Clip clip = AudioSystem.getClip();
		// Open audio clip and load samples from the audio input stream.
		clip.open(audioIn);
		clip.loop(Clip.LOOP_CONTINUOUSLY);

		}catch(Exception e){
			System.out.println(e);
		}

		try {
			guy = ImageIO.read(this.getClass().getResource("st1.png"));
			flipGuy = ImageIO.read(this.getClass().getResource("st1-flip.png"));
			wildWestImage = ImageIO.read(this.getClass().getResource("WildWest.png"));
			bwGuy = ImageIO.read(this.getClass().getResource("st1-b&w.png"));
			wantedPoster = ImageIO.read(this.getClass().getResource("wantedPoster.jpeg"));
			info = ImageIO.read(this.getClass().getResource("info.png"));
			System.out.println(""+wildWestImage.getHeight()+" "+wildWestImage.getWidth());


			int random;




			for(int x=0;x<12;x++){
				for(int y=0;y</*8*/2;y++){
					random=(int)(Math.random()*2);
					if(random==0)
						sprites.add(new Sprite(guy.getSubimage(x*81,y*81,85,85),
							flipGuy.getSubimage((11-x)*81,y*81,85,85),
							bwGuy.getSubimage(x*81,y*81,85,85),(int)(Math.random()*4800)+100,-1,false));
					else
						sprites.add(new Sprite(guy.getSubimage(x*81,y*81,85,85),
						flipGuy.getSubimage((11-x)*81,y*81,85,85),
						bwGuy.getSubimage(x*81,y*81,85,85),(int)(Math.random()*4800)+100,1,false));

				}
			}

			savedSprites.addAll(sprites);

			wantedSprite = (int)(Math.random()*sprites.size());
			sprites.get(wantedSprite).setWanted(true);

		}
		catch (IOException e) {
			System.out.println(e);
		}

		frame.addKeyListener(this);
		addMouseListener(this);
		frame.add(this);
		frame.setSize(800,540);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		t=new Thread(this);
		t.start();

	}

	public void run()
	{
		while(true)
		{
			if(gameOn)
			{
				//Math happens here!
					if(right&&x+20<4200)
						x+=10;
					if(left&&x-20>0)
						x-=10;
					for(int n=0;n<sprites.size();n++)
						sprites.get(n).move();

				repaint();
			}
			if(restart)
			{
				//restart=false;
				//gameOn=true;
			}
			try
			{
				t.sleep(10);
				if(gameOn)
					timer+=0.01;

				if(timer>gameLength){
					gameOn=false;
					repaint();
				}

			}catch(InterruptedException e)
			{
			}
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;

		g2d.setFont(font);

		//all painting happens here!

		g2d.drawImage(wildWestImage,0-x,y,5000,
                500,null);

	if(gameOn){
		g2d.setColor(Color.BLACK);

        g2d.drawImage(wantedPoster,20,20,120,180,null);
        if(sprites.size()>0){
        	g2d.drawImage(sprites.get(wantedSprite).getBWImage(),25,70,100,100,null);
		}

		g2d.setColor(new Color(194,42,0));
		g2d.fillOval(645,5,110,110);
		g2d.setColor(Color.BLACK);
		g2d.fillOval(650,10,100,100);

		if((int)(gameLength-timer)*360/gameLength<=90)
			g2d.setColor(Color.RED);
		else if((int)(gameLength-timer)*360/gameLength<=180)
			g2d.setColor(Color.ORANGE);
		else
			g2d.setColor(Color.GREEN);

		g2d.fillArc(650,10,100,100,90,(int)(gameLength-timer)*360/gameLength);

		g2d.setColor(new Color(194,42,0));
		//g2d.fillRect(340,10,180,50);
		//g2d.setColor(Color.BLACK);
		if(points<0)
			g2d.drawString("-$"+points*-1,340,80);
		else
			g2d.drawString("$"+points,340,80);



		if((int)(gameLength-timer)<10)
			g2d.drawString(""+(int)(gameLength-timer),685,85);
		else
			g2d.drawString(""+(int)(gameLength-timer),670,85);



		//g2d.drawImage(guys[imgCount],20,405,100,100,null);



		for(int n=0;n<sprites.size();n++){



			if(sprites.get(n).getDirection()==1)
				g2d.drawImage(sprites.get(n).getFlipImage(),sprites.get(n).getPosition()-x,400,100,100,null);
			else
				g2d.drawImage(sprites.get(n).getImage(),sprites.get(n).getPosition()-x,400,100,100,null);

				/*if(sprites.get(n).getWanted()){
					g2d.setColor(Color.GREEN);
					g2d.draw(new Rectangle(sprites.get(n).getPosition()-x+30,430,40,60));
				}else{
					g2d.setColor(Color.RED);
					g2d.draw(new Rectangle(sprites.get(n).getPosition()-x+30,430,40,60));
				}*/
		}

	}else if(pregame){
		g2d.setColor(Color.BLACK);
		g2d.drawString("Sheriff Simulator",150,100);

		g2d.setColor(new Color(150,0,0));
		g2d.drawString("Sheriff Simulator",153,103);

		g2d.setColor(Color.ORANGE);
		g2d.fillRoundRect(320,205,140,60,20,20);

		g2d.setColor(Color.BLACK);
		g2d.drawString("play",335,260);

		g2d.setColor(new Color(150,0,0));
		g2d.drawString("play",338,263);

		g2d.drawImage(info,0,0,60,60,null);
		if(showInfo){
			g2d.setColor(Color.BLUE);
			g2d.fillRoundRect(40,40,650,300,20,20);
			g2d.setFont(font.deriveFont(36f));
			g2d.setColor(Color.BLACK);
			g2d.drawString("Find as many wanted criminals",100,100);
			g2d.drawString("as you can in 60 seconds!",100,150);
			g2d.drawString("Use the mouse to click criminals",100,200);
			g2d.drawString("and arrow keys to move left and right.",100,250);
			g2d.drawString("$500 if correct, -$100 if wrong.",100,300);


		}

	}else{
		g2d.setColor(Color.BLACK);
		g2d.drawString("Game Over",227,97);
		g2d.setColor(new Color(150,0,0));
		g2d.drawString("Game Over",230,100);
		if(points<0){
			g2d.setColor(Color.BLACK);
			g2d.drawString("Your Score:-$"+points*-1,197,297);
			g2d.setColor(new Color(150,0,0));
			g2d.drawString("Your Score:-$"+points*-1,200,300);
		}else{
			g2d.setColor(Color.BLACK);
			g2d.drawString("Your Score:$"+points,197,297);
			g2d.setColor(new Color(150,0,0));
			g2d.drawString("Your Score:$"+points,200,300);
		}

				g2d.setColor(Color.ORANGE);
				g2d.fillRoundRect(280,375,230,60,20,20);

				g2d.setColor(Color.BLACK);
				g2d.drawString("Restart",295,430);

				g2d.setColor(new Color(150,0,0));
		g2d.drawString("Restart",298,433);





	}




	}
	public void keyPressed(KeyEvent key)
	{
		System.out.println(key.getKeyCode());


		if(key.getKeyCode()==39)
		{
			if(x+20<4200)
				right=true;

		}
		if(key.getKeyCode()==37)
		{
			if(x-20>0)
				left=true;
		}
		if(key.getKeyCode()==82)
			restart=true;
	}
	public void keyReleased(KeyEvent key)
	{

		if(key.getKeyCode()==39)
		{

				right=false;

		}
		if(key.getKeyCode()==37)
		{
				left=false;
		}
		if(key.getKeyCode()==82)
			restart=true;
	}
	public void keyTyped(KeyEvent key)
	{
	}

	public void mousePressed(MouseEvent e) {

		int mouseX=e.getX();
		int mouseY=e.getY();


	if(gameOn){

		Rectangle rect;

		Sprite spriteClicked;

		System.out.println(mouseX+" "+mouseY+"x:"+x);
		int n=sprites.size()-1;
		boolean hasHitSprite=false;

		while((n>=0)&&(!hasHitSprite)){

			rect = new Rectangle(sprites.get(n).getPosition()-x+30,430,40,60);



			if(rect.contains(mouseX,mouseY)){
				spriteClicked=sprites.get(n);


				System.out.println("did it "+n);

				if(sprites.get(n).getWanted()){
					hasHitSprite=true;
					points+=500;
					sprites.remove(n);
					if(sprites.size()!=0){
						wantedSprite = (int)(Math.random()*sprites.size());
						sprites.get(wantedSprite).setWanted(true);
						System.out.println("New Wanted:"+wantedSprite);
					}
				}
				else{
					points-=100;
					sprites.remove(n);
					if(n<wantedSprite){
						wantedSprite--;
						System.out.println("New Wanted:"+wantedSprite);
					}


				}

				if(sprites.size()==0){
					repaint();
					gameOn=false;
				}

			}

			n--;

		}
	}else if(pregame){
		Rectangle infoButton = new Rectangle(0,0,60,60);
		if(infoButton.contains(mouseX,mouseY)){
			showInfo=!showInfo;
			repaint();
		}


		Rectangle playButton = new Rectangle(320,205,140,60);
		if(playButton.contains(mouseX,mouseY)){
			gameOn=true;

			pregame=false;
		}


	}else{

		Rectangle restartButton = new Rectangle(280,375,230,60);
		if(restartButton.contains(mouseX,mouseY)){
			gameOn=false;
			pregame=true;
			x=2150;
			int size = sprites.size();

			for(int x=0;x<size;x++)
				sprites.remove(0);

			sprites.addAll(savedSprites);
			System.out.println(sprites.size());
			wantedSprite = (int)(Math.random()*sprites.size());
			sprites.get(wantedSprite).setWanted(true);
			points=0;
			timer=0;

			repaint();
		}

	}

	    }

	public void mouseReleased(MouseEvent e) {
	    }

	public void mouseEntered(MouseEvent e) {
	    }

	public void mouseExited(MouseEvent e) {
	    }
	public void mouseClicked(MouseEvent e) {




	}

	public static void main(String args[])
	{
		SheriffSimulator app=new SheriffSimulator();
	}
}
