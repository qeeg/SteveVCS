package author.dariusgoad.emulators.atari.a2600;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TIA tia = new TIA();
		CPU cpu = new CPU(args[0],tia);
		for(int i = 0;i<10000;i++)
		{
			cpu.tick();
			tia.tick();
			tia.tick();
			tia.tick();
		}
	}

}
