import java.util.ArrayList;
import java.util.Objects;

public class Main {
	public static void main(String[] args) {
		ArrayList<Instruction> assemblyInstructions = new ArrayList<>();
		if (args.length <= 0) {
			System.out.println("No argument supplied!");
		} else {
			for (byte[] b : Objects.requireNonNull(Parser.readLinesFromFile(args[0]))) {
				assemblyInstructions.add(Parser.translate(b));
			}
			labelInstructions(assemblyInstructions);
			for (Instruction instruction : assemblyInstructions) {
				System.out.println(instruction);
			}
		}
	}

	private static void labelInstructions(ArrayList<Instruction> assemblyInstructions) {
		for (int idx = 0; idx < assemblyInstructions.size(); idx++) {
			Instruction instruction = assemblyInstructions.get(idx);
			if (instruction.format.equals("B") || instruction.format.equals("CB")) {
				int beginIndex;
				switch (instruction.name) {
					case "B":
						beginIndex = 2;
						updateLabels(assemblyInstructions, idx, instruction, beginIndex);
						break;
					case "BL":
						beginIndex = 3;
						updateLabels(assemblyInstructions, idx, instruction, beginIndex);
						break;
					case "B.":
						beginIndex = 5;
						updateLabels(assemblyInstructions, idx, instruction, beginIndex);
						break;
					case "CBZ":
					case "CBNZ":
						beginIndex = instruction.assemblyLine.lastIndexOf(",") + 1;
						updateLabels(assemblyInstructions, idx, instruction, beginIndex);
						break;
				}
			}
		}
	}

	private static void updateLabels(ArrayList<Instruction> assemblyInstructions, int idx, Instruction i, int beginIndex) {
		String identifier;
		int offset;
		identifier = i.assemblyLine.substring(beginIndex, i.assemblyLine.length() - 1).trim();
		offset = Integer.parseInt(identifier);
		if (!assemblyInstructions.get(idx + offset).hasLabel()) assemblyInstructions.get(idx + offset).addLabel(
				"label_" + (idx + offset));
		assemblyInstructions.get(idx).assemblyLine =
				assemblyInstructions.get(idx).assemblyLine.replace(identifier, assemblyInstructions.get(idx + offset).label);
	}
}