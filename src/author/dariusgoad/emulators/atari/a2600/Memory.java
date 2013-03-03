package author.dariusgoad.emulators.atari.a2600;

import java.io.*;

public class Memory implements Device {

	public int[] mem = new int[0x2000];
	public TIA tia;
	public CPU cpu;
	public int swbcnt;
	
	@Override
	public void tick()
	{
	}

	@Override
	public void reset()
	{
	}
	
	public void reset(String input, TIA tia, CPU cpu)
	{
		File file = new File(input);
		InputStream is = null;
		byte[] res = new byte[0x2000];
		try
		{
			is = new BufferedInputStream(new FileInputStream(file));
			int totalBytesRead = 0;
			while(totalBytesRead < 0x800){
		          int bytesRemaining = 0x800 - totalBytesRead;
		          //input.read() returns -1, 0, or more :
		          int bytesRead = is.read(res, totalBytesRead, bytesRemaining); 
		          if (bytesRead > 0){
		            totalBytesRead = totalBytesRead + bytesRead;
		          }
		        }
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Congratulations! You have triggered an ID10T error, by trying to access a file that isn't there!");
		}
		catch(IOException e)
		{
			System.out.println("WAT HAPPEND 2 DAT I/O THANG, DAWG?!");
		}
		for(int i = 0;i<0x800;i++)
		{
			mem[i+0x1000] = res[i];
			mem[i+0x1800] = res[i];
		}
		this.tia = tia;
		this.cpu = cpu;
	}
	
	public int rb(int addr)
	{
		if((addr & 0x1FFF) <= 0x0D)
		{
			switch(addr & 0x1FFF)
			{
			default:
			{
				System.out.println("Unemulated memory read at address " + addr);
				break;
			}
			}
		}
		if((addr & 0x1FFF) >= 0x80 && (addr & 0x1FFF) < 0x200)
		{
			return mem[addr & 0x1EFF] & 0xFF;
		}
		if((addr & 0x1FFF) >= 0x280 && (addr & 0x1FFF) <= 0x297)
		{
			switch(addr & 0x1FFF)
			{
			case 0x283:
			{
				return swbcnt & 0x9B;
			}
			}
		}
		return mem[addr & 0x1FFF] & 0xFF;
	}
	public void wb(int addr, int value)
	{
		if(addr >= 0x80 && addr < 0x200)
		{
			mem[addr & 0x1EFF] = value & 0xFF;
			return;
		}
		if((addr & 0x1FFF) >= 0x1000)
		{
			mem[addr & 0x1FFF] = value & 0xFF;
		}
		else{rb(addr); return;}
		switch(addr & 0x1FFF)
		{
		case 0x00:
		{
			tia.vsync = (value >>> 1) & 1;
			break;
		}
		case 0x01:
		{
			tia.vblank = (value >>> 1) & 1;
			break;
		}
		case 0x02:
		{
			cpu.cycles += (228 - tia.x) / 3;
			break;
		}
		case 0x10:
		{
			tia.p0x = tia.x;
			break;
		}
		case 0x11:
		{
			tia.p1x = tia.x;
			break;
		}
		case 0x20:
		{
			int tmp = (value >>> 4) & 0x0F;
			if((tmp & 8) == 8) tmp = ~tmp + 1;
			tia.hmp0 = tmp;
			break;
		}
		case 0x21:
		{
			int tmp = (value >>> 4) & 0x0F;
			if((tmp & 8) == 8) tmp = ~tmp + 1;
			tia.hmp1 = tmp;
			break;
		}
		case 0x2A:
		{
			tia.p0x += tia.hmp0;
			tia.p1x += tia.hmp1;
			break;
		}
		case 0x2B:
		{
			tia.hmp0 = 0;
			tia.hmp1 = 0;
			break;
		}
		case 0x19:
		{
			tia.audv0 = value & 0xFF;
			break;
		}
		case 0x28:
		{
			tia.mh0 = (value & 2) == 2 ? true : false;
			break;
		}
		case 0x29:
		{
			tia.mh1 = (value & 2) == 2 ? true : false;
			break;
		}
		case 0x283:
		{
			swbcnt = value & 0x9B;
			break;
		}
		default:
		{
			System.out.println("Unemulated memory write at address " + addr + " with data " + (value & 0xFF));
			break;
		}
		}
	}
}
