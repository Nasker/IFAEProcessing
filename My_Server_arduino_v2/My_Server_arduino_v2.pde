import processing.video.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import hypermedia.net.*;
import processing.serial.*;
// Capture object
Capture cam;
UDP udp1;
//UDP udp4;
Serial port;
int ring, x=0;
int sms=1;
int open = 1;
StringList connectedDevicesIp;
void setup() {
  size(320, 240);
  connectedDevicesIp = new StringList();
  //frameRate(60);
  // Initiaize Camera
  cam = new Capture( this, width, height, 30);
  cam.start();

  udp1 = new UDP(this, 2000);
  udp1.listen(true);
  
  port = new Serial(this, Serial.list()[2], 9600);
}

void captureEvent( Capture c ) {
  c.read();
  process(c);
}

void draw() {
  //if (port.available()>0) {
  //  ring = port.read();
  //  println(ring);
  //}
  image(cam, 0, 0);
  if (sms==0) {
    sms = 1;
    port.write("openDoor");
    println("mensaje enviado a Arduino");
  }
}


void process(PImage img) {

  // We need a buffered image to do the JPG encoding
  BufferedImage bimg = new BufferedImage( img.width, img.height, 
    BufferedImage.TYPE_INT_RGB );

  // Transfer pixels from localFrame to the BufferedImage
  img.loadPixels();
  bimg.setRGB( 0, 0, img.width, img.height, img.pixels, 0, img.width);
  ByteArrayOutputStream baStream = new ByteArrayOutputStream();
  BufferedOutputStream bos = new BufferedOutputStream(baStream);

  // Conv. into a JPG
  try {
    ImageIO.write(bimg, "jpg", bos);
  }
  catch (IOException e) {
    e.printStackTrace();
  }
  byte[] packet = baStream.toByteArray();

  
//println("primera ip: "+ips[0]);
  if (connectedDevicesIp.size()!=0) {
   for (int p=0; p<connectedDevicesIp.size(); p++) {
      udp1.send(packet, connectedDevicesIp.get(p), 3000);
     
   }

    }
 
}
void receive (byte[] mensaje, String ip, int port) {
  String message = new String( mensaje);
  
  if (message.equals("View")==true) {
    int index = findIndexOfElementInList(connectedDevicesIp,ip);
    if(index == -1) connectedDevicesIp.append(ip); 
    else connectedDevicesIp.remove(index);
    println(connectedDevicesIp);
    
  }
  //  if (x==500){//reseteamos el numero de ip
   //   x=0;
   //   ips[x]=ip;
  //  }
    //eliminamos ips repetidas
    /*
    }*/
 // } 
  if (message.equals("openDoor") == true) {
    sms = 0;
  }
  //}
}

int findIndexOfElementInList(StringList list, String element){
  int index = -1;
  for (int i=0; i<list.size(); i++) {
    String currentString = list.get(i);
        if (currentString.equals(element) ==true) {
          index = i;
          println("FOUND IT!");
        }
  }
  println("FIND FUNCTION RETURNS:"+index);
  return index;

}