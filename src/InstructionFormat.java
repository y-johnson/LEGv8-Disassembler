import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AssemblyInstruction format mapper.
 * @author y-johnson
 */
public class InstructionFormat {
	private static final String FORMATS_TXT = "instruction_formats";
	/**
	 * The conditionals that are used to identify paths for CB instructions.
	 */
	private static final Map<String, String> opcodeMnemonicMap = new HashMap<>() {{
		put("0000", "EQ");
		put("0001", "NE");
		put("0010", "HS");
		put("0011", "LO");
		put("0100", "MI");
		put("0101", "PL");
		put("0110", "VS");
		put("0111", "VC");
		put("1000", "HI");
		put("1001", "LS");
		put("1010", "GE");
		put("1011", "LT");
		put("1100", "GT");
		put("1101", "LE");
	}};
	private static List<InstructionFormat> formatList = null;

	static {
		try {
			formatList = InstructionFormat.loadInstructions();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String opcode;
	public String name;
	public String format;
	/**
	 * Creates a basic instruction skeleton that contains the opcode and format of a given instruction_formats. This creates an opcode that is checked
	 * whenever an {@code AssemblyInstruction} is being created.
	 *
	 * @param opcode the binary sequence that represents the opcode
	 * @param name   the human-readable name of the code
	 * @param format the letter of the instruction format
	 */
	public InstructionFormat(String opcode, String name, String format) {
		this.opcode = opcode;
		this.name = name;
		this.format = format;
	}

	public static List<InstructionFormat> getBaseInstructionList() {
		return formatList;
	}

	public static Map<String, String> getConditionals() {
		return opcodeMnemonicMap;
	}

	/**
	 * Loads instruction formats from the specified file.
	 *
	 * @return a list of {@code InstructionFormat} as read from the file.
	 *
	 * @throws IOException if the file was not able to be read
	 */
	private static List<InstructionFormat> loadInstructions() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(InstructionFormat.FORMATS_TXT));
		String line;
		List<InstructionFormat> instructionFormatList = new ArrayList<>();
		while ((line = br.readLine()) != null) {
			if (line.startsWith("0") || line.startsWith("1")) {
				String[] arr = line.split(",");
				instructionFormatList.add(new InstructionFormat(arr[0], arr[1], arr[2]));
			}
		}
		return instructionFormatList;
	}

	@Override
	public String toString() {
		return "InstructionFormat{" +
				"opcode='" + opcode + '\'' +
				", name='" + name + '\'' +
				", format='" + format + '\'' +
				'}';
	}
}