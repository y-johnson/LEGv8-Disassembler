import java.util.ArrayList;

public class Instruction extends Format {
	String label;
	String assemblyLine;

	/**
	 * The normal assembly instruction as deciphered from the given binary string
	 *
	 * @param format       the format that corresponds to this instruction
	 * @param assemblyLine the binary string that defines the assembly
	 */
	public Instruction(Format format, String assemblyLine) {
		super(format.opcode, format.name, format.format);
		this.assemblyLine = assemblyLine.trim();
	}

	/**
	 * Performs a check on each instruction for branch instructions and retroactively updates the branching destinations with labels.
	 * @param assemblyInstructions the instructions to parse through
	 */
	protected static void labelInstructions(ArrayList<Instruction> assemblyInstructions) {
		for (int idx = 0; idx < assemblyInstructions.size(); idx++) {
			Instruction instruction = assemblyInstructions.get(idx);
			if (instruction.format.equals("B") || instruction.format.equals("CB")) {
				int beginIndex;
				switch (instruction.name) {
					case "B" -> {
						beginIndex = 2;
						updateLabels(assemblyInstructions, idx, instruction, beginIndex);
					}
					case "BL" -> {
						beginIndex = 3;
						updateLabels(assemblyInstructions, idx, instruction, beginIndex);
					}
					case "B." -> {
						beginIndex = 5;
						updateLabels(assemblyInstructions, idx, instruction, beginIndex);
					}
					case "CBZ", "CBNZ" -> {
						beginIndex = instruction.assemblyLine.lastIndexOf(",") + 1;
						updateLabels(assemblyInstructions, idx, instruction, beginIndex);
					}
				}
			}
		}
	}

	private static void updateLabels(ArrayList<Instruction> assemblyInstructions, int idx, Instruction i, int beginIndex) {
		String identifier;
		int offset;
		identifier = i.assemblyLine.substring(beginIndex, i.assemblyLine.length() - 1).trim();
		offset = Integer.parseInt(identifier);
		if (assemblyInstructions.get(idx + offset).missingLabel()) assemblyInstructions.get(idx + offset).addLabel(
				"label_" + (idx + offset));
		assemblyInstructions.get(idx).assemblyLine =
				assemblyInstructions.get(idx).assemblyLine.replace(identifier, assemblyInstructions.get(idx + offset).label);
	}

	public void addLabel(String label) {
		this.label = label;
	}

	public boolean missingLabel() {
		return label == null || label.isEmpty();
	}

	@Override
	public String toString() {
		if (missingLabel()) return assemblyLine;
		return label + ": " + assemblyLine;
	}
}