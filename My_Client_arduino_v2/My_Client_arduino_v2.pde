import processing.video.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import hypermedia.net.*;
import controlP5.*;
import gifAnimation.*;
int green = #00FF00;
//UDP udp3;

String HOST_IP = "localhost";

UDP udp2;
Gif loopinGif;
byte[] bitA;
PImage lastFrame;
PImage open,close,nosignal;
PImage [] animation;
boolean ctr=true;
ControlP5 cp5;
void setup() {
  size(1280, 720);
  frameRate(60);
  // create a UDP connection
  udp2 = new UDP(this, 3000);
  // wait constantly for incomming data
  udp2.listen(true);

  
  nosignal = loadImage("nosignal.png");
 loopinGif = new Gif(this,"giphy.gif");
 loopinGif.loop();

  

  
  smooth();
  cp5 = new ControlP5(this);
  cp5.addButton("OPEN")
     .setPosition(260, 180)
     .setSize(50,50)
     .setView(new CircularButton())
     ;
  cp5 = new ControlP5(this);
  cp5.addButton("VIEW")
     .setPosition(200, 180)
     .setSize(50,50)
     .setView(new CircularButton())
     ;
}

void draw(){
 
  if(lastFrame==null){
    background(0);
    image(loopinGif, 10, height - loopinGif.height );
    image(nosignal,10,180,width/4,height/4);

  }else image(lastFrame,0,0);
}


void receive( byte[] data, String ip, int port ) {  // <-- extended handler

  data = subset(data, 0, data.length-2);
  String message = new String( data );
  

  if(data!=null){
    lastFrame=convert(data);
    //println(lastFrame);
  }

 
}

//receives a byte[] with JPEG encoding and returns a PImage
PImage convert( byte[] data) {
  
  
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
  println("Mensaje enviado a servidor");
  
   String openDoor = new String("openDoor");
   
  udp2.send(openDoor,HOST_IP,2000);
      
}

public void VIEW(int theValue) {
  
  
   String view = new String("View");
   
  udp2.send(view,HOST_IP,2000);
  
  //ctr = false;
  //image(loopinGif, 10, height - loopinGif.height );
  
  //image(nosignal,10,180,width/4,height/4);
  
  
}



class CircularButton implements ControllerView<Button> {

  public void display(PGraphics theApplet, Button theButton) {
    theApplet.pushMatrix();
    if (theButton.isInside()) {
      if (theButton.isPressed()) { // button is pressed
        strokeWeight(5);
        stroke(green);
        theApplet.fill(ControlP5.GREEN);
        //theApplet.image (open,250,170);
        
      }  else { // mouse hovers the button
        strokeWeight(5);
        stroke(green);
        theApplet.fill(ControlP5.RED);
       // theApplet.image (close,250,170);
      }
    } else { // the mouse is located outside the button area
      theApplet.fill(ControlP5.RED);
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