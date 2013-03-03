package author.dariusgoad.emulators.atari.a2600;

public class CPU implements Device
{
	public int a,x,y,s,p,pc;
	public Memory mem;
	public int cycles;
	
	@Override
	public void tick()
	{
		int op = mem.rb(pc);
		if(cycles==0)
		{
		switch(op)
		{
		case 0x01:
		{
			a |= mem.rb(mem.rb(pc+1)+x);
			if(a>=0x80) p |= 0x80;
			else p &= 0x7F;
			if(a==0) p |= 2;
			else p &= 0xFD;
			cycles=6;
			pc+=2;
			break;
		}
		case 0x08:
		{
			mem.wb(0x100 + s, p);
			s--;
			cycles=3;
			pc++;
			break;
		}
		case 0x09:
		{
			a |= mem.rb(pc+1);
			if(a>=0x80) p |= 0x80;
			else p &= 0x7F;
			if(a==0) p |= 2;
			else p &= 0xFD;
			cycles=2;
			pc+=2;
			break;
		}
		case 0x0A:
		{
			p &= 0xFE;
			p |= (a >>> 7) & 1;
			a = (a << 1) & 0xFF;
			cycles+=2;
			pc++;
			break;
		}
		case 0x10:
		{
			int tmp = mem.rb(pc+1);
			cycles = 2;
			pc+=2;
			if((p & 0x80) == 0)
			{
				cycles++;
				if(tmp >= 0x80) tmp = ~tmp + 1;
				if((pc & 0xFF00) != ((pc+tmp)&0xFF00)) cycles++;
				pc+=tmp;
			}
			break;
		}
		case 0x18:
		{
			p &= 0xFE;
			cycles=2;
			pc++;
			break;
		}
		case 0x20:
		{
			mem.wb(0x100 + s,(pc+2)>>>8);
			s--;
			mem.wb(0x100 + s,(pc+2)&0xFF);
			s--;
			pc = mem.rb(pc+1)|(mem.rb(pc+2)<<8);
			cycles=6;
			break;
		}
		case 0x24:
		{
			int tmp = mem.rb(mem.rb(pc+1));
			int tmp1 = a & tmp;
			if(tmp>=0x80) p |= 0x80;
			else p &= 0x7F;
			if((tmp & 0x40) == 0x40) p |= 0x40;
			else p &= 0x40;
			if(tmp1==0) p |= 2;
			else p &= 0xFD;
			cycles=3;
			pc+=2;
			break;
		}
		case 0x29:
		{
			a &= mem.rb(pc+1);
			if(a>=0x80) p |= 0x80;
			else p &= 0x7F;
			if(a==0) p |= 2;
			else p &= 0xFD;
			cycles=2;
			pc+=2;
			break;
		}
		case 0x30:
		{
			int tmp = mem.rb(pc+1);
			cycles = 2;
			pc+=2;
			if((p & 0x80) == 0x80)
			{
				cycles++;
				if(tmp >= 0x80) tmp = ~tmp + 1;
				if((pc & 0xFF00) != ((pc+tmp)&0xFF00)) cycles++;
				pc+=tmp;
			}
			break;
		}
		case 0x38:
		{
			p |= 1;
			cycles=2;
			pc++;
			break;
		}
		case 0x48:
		{
			mem.wb(0x100 + s, a);
			s--;
			cycles=3;
			pc++;
			break;
		}
		case 0x49:
		{
			a ^= mem.rb(pc+1);
			if(a>=0x80) p |= 0x80;
			else p &= 0x7F;
			if(a==0) p |= 2;
			else p &= 0xFD;
			cycles=2;
			pc+=2;
			break;
		}
		case 0x4A:
		{
			p &= 0xFE;
			p |= a & 1;
			a >>>= 1;
			cycles+=2;
			pc++;
			break;
		}
		case 0x4C:
		{
			pc = mem.rb(pc+1)|(mem.rb(pc+2)<<8);
			cycles=3;
			break;
		}
		case 0x50:
		{
			int tmp = mem.rb(pc+1);
			cycles = 2;
			pc+=2;
			if((p & 0x40) == 0)
			{
				cycles++;
				if(tmp >= 0x80) tmp = ~tmp + 1;
				if((pc & 0xFF00) != ((pc+tmp)&0xFF00)) cycles++;
				pc+=tmp;
			}
			break;
		}
		case 0x60:
		{
			s++;
			pc = mem.rb(0x100+s);
			s++;
			pc |= mem.rb(0x100+s)<<8;
			pc++;
			cycles=6;
			break;
		}
		case 0x69:
		{
			int tmp = mem.rb(pc+1);
			if((p & 8) == 8)
			{
				int lo = (a & 0x0F) + (tmp & 0x0F) + (p & 1);
				int hi = (a & 0xF0) + (tmp & 0xF0);
				if(~((lo+hi)&0xFF) == 1) p |= 2;
				else p &= 0xFD;
				if(lo > 0x09)
				{
					hi += 0x10;
					lo += 0x06;
				}
				if((hi&0x80)==0x80) p |= 0x80;
				else p &= 0x7F;
				if((~(a^tmp) & (a ^ hi) & 0x80) == 0x80) p |= 0x40;
				else p &= 0xBF;
				if(hi > 0x90) hi += 0x60;
				if(hi > 0xFF) p |= 1;
				else p &= 0xFE;
				a = (lo & 0x0F) | (hi & 0xF0);
			}
			cycles=2;
			pc+=2;
			break;
		}
		case 0x75:
		{
			int tmp = mem.rb(mem.rb(pc+1));
			a = a + tmp + (p & 1);
			if(a==0) p |= 2;
			else p &= 0xFD;
			cycles=4;
			pc+=2;
			break;
		}
		case 0x78:
		{
			p |= 4;
			cycles=2;
			pc++;
			break;
		}
		case 0x85:
		{
			mem.wb(mem.rb(pc + 1), a);
			cycles=3;
			pc+=2;
			break;
		}
		case 0x8A:
		{
			a = x;
			cycles=2;
			pc++;
			break;
		}
		case 0x8D:
		{
			mem.wb(mem.rb(pc + 1) | (mem.rb(pc+2)<<8), a);
			cycles=4;
			pc+=3;
			break;
		}
		case 0x90:
		{
			int tmp = mem.rb(pc+1);
			cycles = 2;
			pc+=2;
			if((p & 1) == 0)
			{
				cycles++;
				if(tmp >= 0x80) tmp = ~tmp + 1;
				if((pc & 0xFF00) != ((pc+tmp)&0xFF00)) cycles++;
				pc+=tmp;
			}
			break;
		}
		case 0x95:
		{
			mem.wb(mem.rb(pc + 1)+x, a);
			cycles=4;
			pc+=2;
			break;
		}
		case 0x96:
		{
			mem.wb(mem.rb(pc + 1)+y, x);
			cycles=4;
			pc+=2;
			break;
		}
		case 0x9A:
		{
			s = x;
			cycles=2;
			pc++;
			break;
		}
		case 0xA2:
		{
			x = mem.rb(pc+1);
			cycles=2;
			pc+=2;
			break;
		}
		case 0xA4:
		{
			y = mem.rb(mem.rb(pc+1));
			cycles=3;
			pc+=2;
			break;
		}
		case 0xA5:
		{
			a = mem.rb(mem.rb(pc+1));
			cycles=3;
			pc+=2;
			break;
		}
		case 0xA6:
		{
			x = mem.rb(mem.rb(pc+1));
			cycles=3;
			pc+=2;
			break;
		}
		case 0xA8:
		{
			y = a;
			cycles=2;
			pc++;
			break;
		}
		case 0xA9:
		{
			a = mem.rb(pc+1);
			cycles=2;
			pc+=2;
			break;
		}
		case 0xAA:
		{
			x = a;
			cycles=2;
			pc++;
			break;
		}
		case 0xAD:
		{
			int tmp = (mem.rb(pc + 1) | (mem.rb(pc+2)<<8));
			a = mem.rb(tmp);
			cycles=4;
			pc+=3;
			break;
		}
		case 0xB0:
		{
			int tmp = mem.rb(pc+1);
			cycles = 2;
			pc+=2;
			if((p & 1) == 1)
			{
				cycles++;
				if(tmp >= 0x80) tmp = ~tmp + 1;
				if((pc & 0xFF00) != ((pc+tmp)&0xFF00)) cycles++;
				pc+=tmp;
			}
			break;
		}
		case 0xB5:
		{
			a = mem.rb(mem.rb(pc+1)+x);
			if(a>=0x80) p |= 0x80;
			else p &= 0x7F;
			if(a==0) p |= 2;
			else p &= 0xFD;
			cycles=4;
			pc+=2;
			break;
		}
		case 0xB9:
		{
			int tmp = mem.rb(pc+1)|(mem.rb(pc+2)<<8);
			a = mem.rb(tmp + y);
			if(a>=0x80) p |= 0x80;
			else p &= 0x7F;
			if(a==0) p |= 2;
			else p &= 0xFD;
			cycles=4;
			if((tmp & 0xFF00) != ((tmp + y) & 0xFF00)) cycles++;
			pc+=3;
			break;
		}
		case 0xBD:
		{
			int tmp = (mem.rb(pc + 1) | (mem.rb(pc+2)<<8));
			a = mem.rb(tmp+x);
			if(a>=0x80) p |= 0x80;
			else p &= 0x7F;
			if(a==0) p |= 2;
			else p &= 0xFD;
			cycles=4;
			if((tmp & 0xFF00) != ((tmp + x) & 0xFF00)) cycles++;
			pc+=3;
			break;
		}
		case 0xC9:
		{
			int tmp = (a - mem.rb(pc+1)) & 0xFF;
			if(a>=0x80) p |= 0x80;
			else p &= 0x7F;
			if(tmp==0) p |= 2;
			else p &= 0xFD;
			if(tmp>a) p |= 1;
			else p &= 0xFE;
			cycles=2;
			pc+=2;
			break;
		}
		case 0xCA:
		{
			x = (x - 1) & 0xFF;
			if(x>=0x80) p |= 0x80;
			else p &= 0x7F;
			if(x==0) p |= 2;
			else p &= 0xFD;
			cycles=2;
			pc++;
			break;
		}
		case 0xD0:
		{
			int tmp = mem.rb(pc+1);
			cycles = 2;
			pc+=2;
			if((p & 2) == 0)
			{
				cycles++;
				if(tmp >= 0x80) tmp = ~tmp + 1;
				if((pc & 0xFF00) != ((pc+tmp)&0xFF00)) cycles++;
				pc+=tmp;
			}
			break;
		}
		case 0xD6:
		{
			int tmp = mem.rb(pc+1);
			int tmp1 = mem.rb(tmp+x);
			mem.wb(tmp+x, tmp1-1);
			cycles=6;
			pc+=2;
			break;
		}
		case 0xD8:
		{
			p &= 0xF7;
			cycles=2;
			pc++;
			break;
		}
		case 0xE6:
		{
			int tmp = mem.rb(pc+1);
			int tmp1 = mem.rb(tmp);
			mem.wb(tmp, tmp1+1);
			cycles=5;
			pc+=2;
			break;
		}
		case 0xE8:
		{
			x++;
			if(x>=0x80) p |= 0x80;
			else p &= 0x7F;
			if(x==0) p |= 2;
			else p &= 0xFD;
			cycles=2;
			pc++;
			break;
		}
		case 0xE9:
		{
			int tmp = mem.rb(pc+1);
			a = a + ~tmp + (p & 1);
			if(a==0) p |= 2;
			else p &= 0xFD;
			cycles=2;
			pc+=2;
			break;
		}
		case 0xF0:
		{
			int tmp = mem.rb(pc+1);
			cycles = 2;
			pc+=2;
			if((p & 2) == 2)
			{
				cycles++;
				if(tmp >= 0x80) tmp = ~tmp + 1;
				if((pc & 0xFF00) != ((pc+tmp)&0xFF00)) cycles++;
				pc+=tmp;
			}
			break;
		}
		case 0xF6:
		{
			int tmp = mem.rb(pc+1);
			int tmp1 = mem.rb(tmp+x);
			mem.wb(tmp+x, tmp1+1);
			cycles=6;
			pc+=2;
			break;
		}
		case 0xF8:
		{
			p |= 8;
			cycles=2;
			pc++;
			break;
		}
		default:
		{
			System.out.println("Unemulated opcode " + op);
			pc++;
			break;
		}
		}
		System.out.println("" + op);
		System.out.println("a: " + a);
		System.out.println("x: " + x);
		System.out.println("y: " + y);
		System.out.println("s: " + s);
		System.out.println("p: " + p);
		System.out.println("pc: " + pc);
		}
		else cycles--;
	}

	@Override
	public void reset(){}
	public void reset(String input, TIA tia)
	{
		mem = new Memory();
		mem.reset(input,tia,this);
		p = 0x24;
		pc = mem.rb(0xFFFC)|(mem.rb(0xFFFD)<<8);
		cycles=0;
	}

	CPU(String input, TIA tia){reset(input,tia);}
}
