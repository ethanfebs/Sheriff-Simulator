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

public class Sprite{

BufferedImage image;
BufferedImage flipImage;
BufferedImage bwImage;
int position;
int direction;
boolean isWanted;


public Sprite(BufferedImage b, BufferedImage b2, BufferedImage b3, int pos,int d,boolean w){

	image=b;
	flipImage=b2;
	bwImage=b3;
	position=pos;
	direction=d;
	isWanted = w;
}

public BufferedImage getImage(){
	return image;
}
public BufferedImage getFlipImage(){
	return flipImage;
}
public BufferedImage getBWImage(){
	return bwImage;
}
public int getPosition(){
	return position;
}
public int getDirection(){
	return direction;
}
public boolean getWanted(){
	return isWanted;
}
public void setWanted(boolean w){
	isWanted=w;
}

public void move(){
	if((int)(Math.random()*200)==0)
		direction*=-1;

	if(direction==-1&&position<=20)
		direction*=-1;
	else if(direction==1&&position>=4850)
		direction*=-1;

	position=position+direction;
}


}
