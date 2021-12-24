import java.util.ArrayList;
import java.util.Objects;

public class Main {
	public static void main(String[] args) {
		ArrayList<Instruction> assemblyInstructions = new ArrayList<>();
		if (args.length <= 0) {
			System.out.println("No argument supplied!");
		} else {
			for (byte[] b : Objects.requireNonNull(Parser.readLinesFromFile(args[0]))) assemblyInstructions.add(Parser.translate(b));
			Instruction.labelInstructions(assemblyInstructions);
			for (Instruction instruction : assemblyInstructions) {
				System.out.println(instruction);
			}
		}
	}
}