import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.video.*; 
import javax.imageio.*; 
import java.io.*; 
import java.awt.image.*; 
import hypermedia.net.*; 
import controlP5.*; 
import gifAnimation.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class IFWVideoCamClientRanking extends PApplet {








int green = 0xff00FF00;
String HOST_IP = "172.16.17.44";
UDP udp2, udp4,udp6;
Gif loopinGif;
byte[] bitA;
PImage lastFrame, fondo, ifae, semR, semG;
PImage open, openp, nosignal, view, viewr, ranking, rankingp;
PImage [] animation;
StringList rankingList;
boolean thresholdViewRanking = false;
boolean accesView = false;
int yAxisSeparation=20;
int displacement = 0;
boolean buttonToggle=true, openDoor=false;
int screen;//Arman
ControlP5 cp5;
String gifs;
StringList listOfGifs;
public void setup() {
  
  frameRate(60);

  PFont font= createFont("Alice_in_Wonderland_3.ttf", 18);
  textFont(font);

  // create a UDP connection
  udp2 = new UDP(this, 3001);
  // wait constantly for incomming data
  udp2.listen(true);
  udp4 = new UDP(this, 3002);
  udp4.listen(true);
  
  udp6 = new UDP(this, 3100);
  udp6.listen(true);
  
  rankingList = new StringList();

  ifae= loadImage("data/images/IFAE.png");
  ifae.resize (PApplet.parseInt(0.20f*width), PApplet.parseInt(0.15f*height));

  fondo= loadImage("data/images/backgroundRanking.png");
  fondo.resize (width, height);

  open = loadImage("data/icons/OPEN.png");
  open.resize (PApplet.parseInt(0.27f*width), PApplet.parseInt(0.31f*height));

  openp = loadImage("data/icons/OPENP.png");
  openp.resize(PApplet.parseInt(0.27f*width), PApplet.parseInt(0.31f*height));

  view = loadImage("data/icons/VIEW.png");
  view.resize (PApplet.parseInt(0.27f*width), PApplet.parseInt(0.31f*height));

  viewr = loadImage("data/icons/VIEWR.png");
  viewr.resize (PApplet.parseInt(0.27f*width), PApplet.parseInt(0.31f*height));

  ranking = loadImage("data/icons/RANKING.png");
  ranking.resize (PApplet.parseInt(0.27f*width), PApplet.parseInt(0.31f*height));

  rankingp = loadImage("data/icons/RANKINGP.png");
  rankingp.resize (PApplet.parseInt(0.27f*width), PApplet.parseInt(0.31f*height));

  semR= loadImage("data/icons/SEMRED.png");
  semR.resize (PApplet.parseInt(width*0.15f), PApplet.parseInt(height*0.15f));

  semG= loadImage("data/icons/SEMGREEN.png");
  semG.resize (PApplet.parseInt(width*0.15f), PApplet.parseInt(height*0.15f));

  nosignal = loadImage("data/icons/nosignal.png");

  //gifs= "giphy.gif";
  gifs = "data/GIF/GIF"+PApplet.parseInt(random(37))+".gif";
  loopinGif = new Gif(this, gifs);
  loopinGif.loop();




  
  cp5 = new ControlP5(this);
  cp5.addButton("OPEN")
    //.setPosition(0.9*width, 0.85*height)
    .setPosition(0.55f*width, 0.74f*height)
    .setImages(open, open, openp)
    .updateSize()
    ;
  cp5 = new ControlP5(this);
  cp5.addButton("VIEW")
    .setPosition(0.35f*width, 0.74f*height)
    .setImages(view, view, viewr)
    .updateSize()
    ;
  cp5.addButton("RANKING")

    .setPosition(0.15f*width, 0.74f*height)
    .setImages(ranking, ranking, rankingp)
    .updateSize()
    ;
}

public void draw() {


  if (accesView) {
    if (thresholdViewRanking) {
      image(fondo, width*0.001f, height*0.001f);
      image(ifae, width*0.75f, height*0.02f);
      //background(0);
      //fill(255);   
      drawRanking();
    }
  } else if (lastFrame==null) {
    background(0);
    image(loopinGif, width*0.1f, height*0.2f );
    image(nosignal, width*0.1f, height*0.2f, width/4, height/4);
  } else{ image(lastFrame, 0, 0); 
    if (openDoor==true) {
      println("cambio de semaforo");
      image(semG, width*0.85f, height*0.90f);
    } else  image(semR, width*0.85f, height*0.90f);
  }
}


public void receive( byte[] data, String ip, int port ) {  // <-- extended handler

  if (port==5000) {
    data = subset(data, 0, data.length);
    String message = new String( data );
    //println(message + "\n" );
    if (message.equals("finish")&& accesView) {

      //si a acabado de enviar los datos imprime por pantalla la stringlist

      //println(rankingList);
      thresholdViewRanking=true;
    } else {
      //println(message + "\n" );
      rankingList.append(message);
    }
  } else if (port==2000) {
    data = subset(data, 0, data.length-2);
    String message = new String( data );
    if (data!=null) {
      lastFrame=convert(data);
      //println(lastFrame);
    }
  } else if (port==2100) {
    data = subset(data, 0, data.length-2);
    String message = new String( data );
      println(message);
    if (message.equals("OP")) {
      println("openDoor=true");
      openDoor=true;
    } else if (message.equals("CLO")) {
      println("openDoor=false");
      openDoor=false;
    }
  }
}    


//receives a byte[] with JPEG encoding and returns a PImage
public PImage convert( byte[] data) {


  // println("convert print: "+data.length);
  //bitA = data;
  PImage video = createImage(width, height, RGB);
  ByteArrayInputStream bais = new ByteArrayInputStream( data );
  // We need to unpack JPG and put it in the PImage img
  video.loadPixels();
  try {
    BufferedImage bimg = ImageIO.read(bais);
    bimg.getRGB(0, 0, video.width, video.height, video.pixels, 0, video.width);
  }
  catch (Exception e) {
    e.printStackTrace();
  }
  // Update the PImage pixels
  video.updatePixels();
  return video;
}

public void OPEN(int theValue) {
  println("Puerta abierta");

  String openDoor = new String("openDoor");

  udp2.send(openDoor, HOST_IP, 2000);
}

public void VIEW(int theValue) {

  println("Live Cam Video");
  String view = new String("View");

  udp2.send(view, HOST_IP, 2000);

  //ctr = false;
  //image(loopinGif, 10, height - loopinGif.height );

  //image(nosignal,10,180,width/4,height/4);
}

public void RANKING(int theValue) {

  if (buttonToggle==true) {

    buttonToggle = false;
    accesView = true;
    println("Ranking solicitado");
    //toogle 
    //rankingList.clear();
    //background(0);
    String openDoor = new String("ranking");

    udp2.send(openDoor, HOST_IP, 2000);
  } else if (buttonToggle==false) {
    displacement=0;
    rankingList.clear();
    lastFrame=null;
    accesView = false; 
    buttonToggle = true;
  }
}

class CircularButton implements ControllerView<Button> {

  public void display(PGraphics theApplet, Button theButton) {
    theApplet.pushMatrix();
    if (theButton.isInside()) {
      if (theButton.isPressed()) { // button is pressed
        //strokeWeight(5);
        //stroke(green);
        //theApplet.fill(ControlP5.GREEN);
      } else { // mouse hovers the button
        //strokeWeight(5);
        //stroke(green);
        // theApplet.fill(ControlP5.RED);
      }
    } else { // the mouse is located outside the button area
      //theApplet.fill(ControlP5.RED);
      //theApplet.image (ControlP5.close);
    }

    theApplet.ellipse(0, 0, theButton.getWidth(), theButton.getHeight());

    // center the caption label 
    int x = theButton.getWidth()/2 - theButton.getCaptionLabel().getWidth()/2;
    int y = theButton.getHeight()/2 - theButton.getCaptionLabel().getHeight()/2;

    translate(x, y);
    theButton.getCaptionLabel().draw(theApplet);

    theApplet.popMatrix();
  }
}

public void drawRanking() {


  for (int i=0; i<rankingList.size(); i++) {

    text(rankingList.get(i), width/2.2f, i*yAxisSeparation + height - displacement);
  }

  displacement++;
  //if(displacement == height) displacement = 0;
  if (displacement == height-20) thresholdViewRanking=false;
}
  public void settings() {  size(640, 480);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "IFWVideoCamClientRanking" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
