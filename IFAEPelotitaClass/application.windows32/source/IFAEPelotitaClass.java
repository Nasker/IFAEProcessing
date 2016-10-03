import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class IFAEPelotitaClass extends PApplet {

int numPelotitas = 128;
PelotitaIFAE[] pelotitas;

public void setup(){
  
  pelotitas = new PelotitaIFAE[numPelotitas];
  for(int i=0; i<numPelotitas; i++){
    int c = color(PApplet.parseInt(random(0,255)),PApplet.parseInt(random(0,255)),PApplet.parseInt(random(0,255)));
    pelotitas[i] = new PelotitaIFAE(PApplet.parseInt(random(0,width)), PApplet.parseInt(random(0,height)), PApplet.parseInt(random(-40,40)), 
    PApplet.parseInt(random(-40,40)), PApplet.parseInt(random(0,40)), c);
  }
}

public void draw(){
  background(0);
  for(int i=0; i<numPelotitas; i++){
    pelotitas[i].move();
    pelotitas[i].show();
  }
}
class PelotitaIFAE{
  int _posX;
  int _posY;
  int _velX;
  int _velY;
  int _size;
  int _c;
  
  PelotitaIFAE(int posX, int posY, int velX, int velY, int size, int c){
    _posX = posX;
    _posY = posY;
    _velX = velX;
    _velY = velY;
    _size = size;
    _c = c;
  }
  
  public void move(){
    _posX = _posX + _velX;
    _posY = _posY + _velY;
    if(_posX > width || _posX < 0) _velX = -_velX;
    if(_posY > height || _posY < 0) _velY = -_velY;
  }
  
  public void show(){
    fill(_c);
    ellipse(_posX, _posY,_size, _size);
  } 
}
  public void settings() {  size(1280,960,P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "IFAEPelotitaClass" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
