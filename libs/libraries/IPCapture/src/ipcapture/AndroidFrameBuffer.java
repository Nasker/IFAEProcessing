package ipcapture;

import java.io.ByteArrayInputStream;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

public class AndroidFrameBuffer extends FrameBuffer {
  Bitmap buffer;
  
  public AndroidFrameBuffer(ByteArrayInputStream in) {
	this.buffer = BitmapFactory.decodeStream(in);
  }
  
  public boolean isAvailable() {
	return buffer != null;
  }
  
  public int getWidth() {
	return buffer.getWidth();
  }
  
  public int getHeight() {
	return buffer.getHeight();
  }
  
  public void writePixels(int[] pixels, int w, int h) {
	buffer.getPixels(pixels, 0, w, 0, 0, w, h);
  }
}
