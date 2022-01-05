import java.util.ArrayList;
import java.util.Objects;

/**
 * @author y-johnson
 */
public class Main {
	private static final String credits = "LEGv8 Disassembler\nAuthored by y-johnson (https://github.com/y-johnson)";
	public static void main(String[] args) {
		ArrayList<AssemblyInstruction> assemblyInstructions = new ArrayList<>();
		System.out.println(credits);
		if (args.length <= 0) {
			System.err.println("No argument supplied!");
		} else {
			System.out.println("\nBinary:");
			for (byte[] b : Objects.requireNonNull(Parser.readLinesFromFile(args[0]))) {
				assemblyInstructions.add(Parser.translate(b));
			}
			AssemblyInstruction.labelInstructions(assemblyInstructions);
			System.out.println("\nLEGv8 Assembly:");
			for (AssemblyInstruction assemblyInstruction : assemblyInstructions) {
				System.out.println(assemblyInstruction);
			}
		}
	}
}