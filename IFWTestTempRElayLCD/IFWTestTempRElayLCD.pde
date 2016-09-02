import processing.serial.*;
import controlP5.*;

Serial myPort; 
ControlP5 cp5;

void setup(){
  size(800, 600);
  
  println(Serial.list());
  String portName = Serial.list()[2];
  myPort = new Serial(this, portName, 115200);
  
  cp5 = new ControlP5(this);
  cp5.addToggle("toggleRelay")
  .setPosition(width/2-width/16, height/2-height/24)
  .setSize(width/8, height/12)
  .setMode(ControlP5.SWITCH)
  ;
   cp5.addButton("getTemperature")
  .setPosition(width/2-width/20, height/4)
  .setSize(width/10, height/18)
  ;
  background(0);
}

void draw(){ 
  if (myPort.available() > 0) {  
    String receivedString = myPort.readString();
    println(receivedString); 
    if(receivedString.equals("RELAY_IS_ON!") == true){
      cp5.getController("toggleRelay").setColorCaptionLabel(0);
      background(255);
    }
    else if(receivedString.equals("RELAY_IS_OFF!") == true){
      cp5.getController("toggleRelay").setColorCaptionLabel(255);
      background(0);
    }
  }
}

public void controlEvent(ControlEvent theEvent){
  println(theEvent.getController().getName());
  //n=0;
}

public void getTemperature(boolean State){
    myPort.write("GET_TEMPERATURE");
}

public void toggleRelay(boolean State){
  println("STATE OF TOGGLE IS: "+State);
  if(State){
    myPort.write("SET_RELAY_ON");
  }
  else{
    myPort.write("SET_RELAY_OFF");
  }
}

void keyPressed(){
  switch(key){
  case 'o':
    myPort.write("SET_RELAY_ON");
    break;  
  case 'f':
    myPort.write("SET_RELAY_OFF");
    break;
  default:
    myPort.write("FUCK");
    break;
  }
}