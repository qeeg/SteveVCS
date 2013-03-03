package author.dariusgoad.emulators.atari.a2600;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

public class TIA
{
	AudioInputStream ais;
	ByteBuffer tone;
	Clip clip;
	AudioFormat af = new AudioFormat(44100, 8, 1, true, false);;
	public int audv0;
	public int audf0;
	public int audc0;
	public int x = 0;
	public int y = 0;
	public int p0x,p1x,m0x,m1x,bx;
	public int hmp0,hmp1;
	public int vsync = 0;
	public int vblank = 0;
	public boolean mh0, mh1; //Missile hide
	
	void tick()
	{
		/*tone = ByteBuffer.allocate(4);
		tone.put((byte)audf0);
		try
		{
			ais = new AudioInputStream(new ByteArrayInputStream(tone.array()), af, 1);
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(audv0 - 8);
		}
		catch(LineUnavailableException e)
		{
		}
		catch(IOException e){}
		tone = null;
		ais = null;*/
		
		if(vblank != 0)
		{
			x++;
			if(x == 228)
			{
				x=0;
				y++;
				if(y == 262) y=0;
			}
		}
		else
		{
			x = 0;
			y = 0;
		}
	}
}
