import java.util.ArrayList;
/**
 * @author y-johnson
 */
public class AssemblyInstruction {
	private String label;
	private InstructionFormat instructionFormat;
	private String assemblyLine;

	/**
	 * A direct method to create a {@code AssemblyInstruction}.
	 *
	 * @param instructionFormat the instructionFormat that corresponds to this instruction
	 * @param assemblyLine      the assembly string
	 */
	public AssemblyInstruction(InstructionFormat instructionFormat, String assemblyLine) {
		this.instructionFormat = new InstructionFormat(instructionFormat.opcode, instructionFormat.name, instructionFormat.format);
		this.assemblyLine = assemblyLine.trim();
	}

	/**
	 * The normal assembly instruction as deciphered from the given binary string
	 *
	 * @param instructionFormat the instructionFormat that corresponds to this instruction
	 * @param binaryString      the binary string that defines the assembly
	 */
	public AssemblyInstruction(String binaryString, InstructionFormat instructionFormat) {
		this.assemblyLine = "Disassembly error!";
		InstructionFormat match = null;
		switch (instructionFormat.format) {
			case "R":
				// 0-10 opcode
				// 11-15 Rm
				// 16-21 shamt
				// 22-26 Rn
				// 27-31 Rd
				if (binaryString.startsWith(instructionFormat.opcode)) {
					match = instructionFormat;
					String Rm = binaryString.substring(11, 16);
					String shamt = binaryString.substring(16, 22);
					String Rn = binaryString.substring(22, 27);
					String Rd = binaryString.substring(27);

					switch (instructionFormat.name) {
						case "LSL":
						case "LSR":
							this.assemblyLine = String.format(
									"%s X%d, X%d, #%d;",
									instructionFormat.name,
									Integer.parseInt(Rd, 2),
									Integer.parseInt(Rn, 2),
									Integer.parseUnsignedInt(shamt, 2)
							);
							break;
						case "PRNL":
						case "DUMP":
						case "HALT":
							this.assemblyLine = String.format("%s;", instructionFormat.name);
							break;
						case "PRNT":
							this.assemblyLine = String.format("%s X%d;", instructionFormat.name, Integer.parseInt(Rd, 2));
							break;
						default:
							this.assemblyLine = String.format(
									"%s X%d, X%d, X%d;",
									instructionFormat.name,
									Integer.parseInt(Rd, 2),
									Integer.parseInt(Rn, 2),
									Integer.parseInt(Rm, 2)
							);
							break;
					}
				}
				break;
			case "I":
				// 0-9 opcode
				// 10-21 immediate
				// 22-26 Rn
				// 27-31 Rd
				if (binaryString.startsWith(instructionFormat.opcode)) {
					match = instructionFormat;
					String immediate = binaryString.substring(10, 22);
					String Rn = binaryString.substring(22, 27);
					String Rd = binaryString.substring(27);

					this.assemblyLine = String.format(
							"%s X%d, X%d, #%s;",
							instructionFormat.name,
							Integer.parseInt(Rd, 2),
							Integer.parseInt(Rn, 2),
							immediate.startsWith("0") ? Integer.parseInt(immediate, 2) : "-" + (Integer.parseInt("1000000000000", 2) -
									Integer.parseInt(immediate, 2))

					);

				}
				break;
			case "D":
				// 0-10 opcode
				// 11-19 address
				// 20-21 opcode2
				// 22-26 Rn
				// 27-31 Rd
				if (binaryString.startsWith(instructionFormat.opcode)) {
					match = instructionFormat;
					String address = binaryString.substring(11, 20);
//					String opcode2 = line.substring(20, 22);
					String Rn = binaryString.substring(22, 27);
					String Rd = binaryString.substring(27);

					this.assemblyLine = String.format(
							"%s X%d, [X%d, #%s];",
							instructionFormat.name,
							Integer.parseInt(Rd, 2),
							Integer.parseInt(Rn, 2),
							address.startsWith("0") ? Integer.parseInt(address, 2) : "-" + (Integer.parseInt("1000000000000", 2) -
									Integer.parseInt(address, 2))
					);
				}
				break;
			case "B":
				// 0-5 opcode
				// 6-31 branch address
				if (binaryString.startsWith(instructionFormat.opcode)) {
					match = instructionFormat;
					String branchAddress = binaryString.substring(6);
					this.assemblyLine = String.format(
							"%s %s;",
							instructionFormat.name,
							branchAddress.startsWith("0") ? Integer.parseInt(branchAddress, 2) : "-" + (Integer.parseInt(
									"100000000000000000000000000",
									2
							) - Integer.parseInt(branchAddress, 2))
					);
				}
				break;
			case "CB":
				// 0-5 opcode
				// 6-26 branch address
				// 27-31 Rt
				if (binaryString.startsWith(instructionFormat.opcode)) {
					match = instructionFormat;
					String branchAddress = binaryString.substring(8, 27);
					String Rt = binaryString.substring(27);
					Object conditionalString = branchAddress.startsWith("0") ? Integer.parseInt(branchAddress, 2) : "-" + (Integer.parseInt(
							"10000000000000000000",
							2
					) - Integer.parseInt(branchAddress, 2));
					if (instructionFormat.name.equals("B.")) {
						this.assemblyLine = String.format(
								"%s%s %s;",
								instructionFormat.name,
								InstructionFormat.getConditionals().get(Rt.substring(1)),
								conditionalString
						);

					} else {
						this.assemblyLine = String.format(
								"%s X%s, %s;",
								instructionFormat.name,
								Integer.parseInt(Rt, 2),
								conditionalString
						);
					}
				}
				break;
			default:
		}
		if (match != null) {
			this.instructionFormat = instructionFormat;
		} else throw new NullPointerException("Did not find a match.");
	}

	/**
	 * Performs a check on each instruction for branch instructions and retroactively updates the branching destinations with labels.
	 *
	 * @param assemblyInstructions the instructions to parse through
	 */
	protected static void labelInstructions(ArrayList<AssemblyInstruction> assemblyInstructions) {
		for (int idx = 0; idx < assemblyInstructions.size(); idx++) {
			AssemblyInstruction assemblyInstruction = assemblyInstructions.get(idx);
			if (assemblyInstruction.instructionFormat.format.equals("B") || assemblyInstruction.instructionFormat.format.equals("CB")) {
				int beginIndex;
				switch (assemblyInstruction.instructionFormat.name) {
					case "B":
						beginIndex = 2;
						updateLabels(assemblyInstructions, idx, assemblyInstruction, beginIndex);
						break;
					case "BL":
						beginIndex = 3;
						updateLabels(assemblyInstructions, idx, assemblyInstruction, beginIndex);
						break;
					case "B.":
						beginIndex = 5;
						updateLabels(assemblyInstructions, idx, assemblyInstruction, beginIndex);
						break;
					case "CBZ":
					case "CBNZ":
						beginIndex = assemblyInstruction.assemblyLine.lastIndexOf(",") + 1;
						updateLabels(assemblyInstructions, idx, assemblyInstruction, beginIndex);
						break;
				}
			}
		}
	}

	private static void updateLabels(ArrayList<AssemblyInstruction> assemblyInstructions, int idx, AssemblyInstruction i, int beginIndex) {
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

	public InstructionFormat getFormat() {
		return instructionFormat;
	}

	public void setFormat(InstructionFormat instructionFormat) {
		this.instructionFormat = instructionFormat;
	}

	@Override
	public String toString() {
		if (missingLabel()) return assemblyLine;
		return label + ": " + assemblyLine;
	}


}