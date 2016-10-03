class PelotitaIFAE{
  int _posX;
  int _posY;
  int _velX;
  int _velY;
  int _size;
  color _c;
  
  PelotitaIFAE(int posX, int posY, int velX, int velY, int size, color c){
    _posX = posX;
    _posY = posY;
    _velX = velX;
    _velY = velY;
    _size = size;
    _c = c;
  }
  
  void move(){
    _posX = _posX + _velX;
    _posY = _posY + _velY;
    if(_posX > width || _posX < 0) _velX = -_velX;
    if(_posY > height || _posY < 0) _velY = -_velY;
  }
  
  void show(){
    fill(_c);
    ellipse(_posX, _posY,_size, _size);
  } 
}