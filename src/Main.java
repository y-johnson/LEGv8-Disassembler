public class Main {
	public static Instruction translate(byte[] buffer) {
		StringBuilder lineBuilder = new StringBuilder();
		String[] arr = Parser.arrayToBinary(buffer);
		for (String s : arr) {
			lineBuilder.append(s);
		}
		String line = lineBuilder.toString();

		for (Instruction i : Instruction.INSTRUCTION_LIST) {
			switch (i.format) {
				case "R":
					// 0-10 opcode
					// 11-15 Rm
					// 16-21 shamt
					// 22-26 Rn
					// 27-31 Rd
					if (line.startsWith(i.opcode)) {
						System.out.println(line + "// R-type instruction " + i.name);
					}
					break;
				case "I":
					// 0-9 opcode
					// 10-21 immediate
					// 22-26 Rn
					// 27-31 Rd
					if (line.startsWith(i.opcode)) {
						System.out.println(line + "// I-type instruction " + i.name);
					}
					break;
				case "D":
					// 0-10 opcode
					// 11-19 address
					// 20-21 opcode2
					// 22-26 Rn
					// 27-31 Rd
					if (line.startsWith(i.opcode)) {
						System.out.println(line + "// D-type instruction " + i.name);
					}
					break;
				case "B":
					// 0-5 opcode
					// 6-31 branch address
					if (line.startsWith(i.opcode)) {
						System.out.println(line + "// B-type instruction " + i.name);
					}
					break;
				case "CB":
					// 0-5 opcode
					// 6-26 branch address
					// 27-31 Rt
					if (line.startsWith(i.opcode)) {
						if (i.name.equals("B.")) {

							System.out.println(line + "// CB-type instruction " + i.name + Instruction.CONDITIONAL.get(line.substring(28)));
						} else System.out.println(line + "// CB-type instruction " + i.name);
					}
					break;
				default:
			}
		}
		return null;
	}
}