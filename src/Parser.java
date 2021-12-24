import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Parser {

	public static Instruction translate(byte[] buffer) {
		StringBuilder lineBuilder = new StringBuilder();
		String[] arr = Parser.arrayToBinary(buffer);
		for (String s : arr) {
			lineBuilder.append(s);
		}
		String line = lineBuilder.toString();
		for (Format i : Format.getBaseInstructionList()) {
			Instruction assemblyFromBinary = getAssemblyFromBinary(line, i);
			if (assemblyFromBinary != null) return assemblyFromBinary;
		}
		return null;
	}

	public static String[] arrayToBinary(byte[] buffer) {
		String[] arr = new String[4];
		int i = 0;
		String s;
		for (byte b : buffer) {
			s = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
			arr[i] = s;
			++i;
			i = i % 4;
		}
		return arr;

	}

	private static Instruction getAssemblyFromBinary(String line, Format i) {
		String output = "Disassembly error!";
		Format match = null;
		switch (i.format) {
			case "R":
				// 0-10 opcode
				// 11-15 Rm
				// 16-21 shamt
				// 22-26 Rn
				// 27-31 Rd
				if (line.startsWith(i.opcode)) {
					match = i;
					String Rm = line.substring(11, 16);
					String shamt = line.substring(16, 22);
					String Rn = line.substring(22, 27);
					String Rd = line.substring(27);

					output = switch (i.name) {
						case "LSL", "LSR" -> String.format(
								"%s X%d, X%d, #%d;",
								i.name,
								Integer.parseInt(Rd, 2),
								Integer.parseInt(Rn, 2),
								Integer.parseUnsignedInt(shamt, 2)
						);
						case "PRNL", "DUMP", "HALT" -> String.format("%s;", i.name);
						case "PRNT" -> String.format("%s X%d;", i.name, Integer.parseInt(Rd, 2));
						default -> String.format(
								"%s X%d, X%d, X%d;",
								i.name,
								Integer.parseInt(Rd, 2),
								Integer.parseInt(Rn, 2),
								Integer.parseInt(Rm, 2)
						);
					};
				}
				break;
			case "I":
				// 0-9 opcode
				// 10-21 immediate
				// 22-26 Rn
				// 27-31 Rd
				if (line.startsWith(i.opcode)) {
					match = i;
					String immediate = line.substring(10, 22);
					String Rn = line.substring(22, 27);
					String Rd = line.substring(27);

					output = String.format(
							"%s X%d, X%d, #%s;",
							i.name,
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
				if (line.startsWith(i.opcode)) {
					match = i;
					String address = line.substring(11, 20);
//					String opcode2 = line.substring(20, 22);
					String Rn = line.substring(22, 27);
					String Rd = line.substring(27);

					output = String.format(
							"%s X%d, [X%d, #%s];",
							i.name,
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
				if (line.startsWith(i.opcode)) {
					match = i;
					String branchAddress = line.substring(6);
					output = String.format(
							"%s %s;",
							i.name,
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
				if (line.startsWith(i.opcode)) {
					match = i;
					String branchAddress = line.substring(8, 27);
					String Rt = line.substring(27);
					Object conditionalString = branchAddress.startsWith("0") ? Integer.parseInt(branchAddress, 2) : "-" + (Integer.parseInt(
							"10000000000000000000",
							2
					) - Integer.parseInt(branchAddress, 2));
					if (i.name.equals("B.")) {
						output = String.format(
								"%s%s %s;",
								i.name,
								Format.getConditionals().get(Rt.substring(1)),
								conditionalString
						);

					} else {
						output = String.format(
								"%s X%s, %s;",
								i.name,
								Integer.parseInt(Rt, 2),
								conditionalString
						);
					}
				}
				break;
			default:
		}
		if (match == null) return null;
		return new Instruction(match, output);
	}

	public static List<byte[]> readLinesFromFile(String fileLocation) {
		try {
			InputStream is = new FileInputStream(fileLocation);
			List<byte[]> byteList = new ArrayList<>();
			byte[] buffer = new byte[4];
			while (is.read(buffer) != -1) {
				arrayToBinary(buffer);
				byteList.add(buffer.clone());
			}
			return byteList;
		} catch (FileNotFoundException e) {
			System.err.println("The program could not locate or access the file at \"" + fileLocation + "\".");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}