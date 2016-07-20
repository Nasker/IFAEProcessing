int numPelotitas = 1000;
PelotitaIFAE[] pelotitas;

void setup(){
  size(1280,960,P3D);
  pelotitas = new PelotitaIFAE[numPelotitas];
  for(int i=0; i<numPelotitas; i++){
    color c = color(int(random(0,255)),int(random(0,255)),int(random(0,255)));
    pelotitas[i] = new PelotitaIFAE(int(random(0,width)), int(random(0,height)), int(random(-40,40)), 
    int(random(-40,40)), int(random(0,40)), c);
  }
}

void draw(){
  background(0);
  for(int i=0; i<numPelotitas; i++){
    pelotitas[i].move();
    pelotitas[i].show();
  }
}