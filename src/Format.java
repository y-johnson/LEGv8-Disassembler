import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Instruction format mapper.
 */
public class Format {
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
	private static List<Format> formatList = null;

	static {
		try {
			formatList = Format.loadInstructions();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String opcode;
	public String name;
	public String format;
	/**
	 * Creates a basic instruction skeleton that contains the opcode and format of a given instruction_formats. This creates an opcode that is checked
	 * whenever an {@code Instruction} is being created.
	 *
	 * @param opcode the binary sequence that represents the opcode
	 * @param name   the human-readable name of the code
	 * @param format the letter of the instruction format
	 */
	public Format(String opcode, String name, String format) {
		this.opcode = opcode;
		this.name = name;
		this.format = format;
	}

	public static List<Format> getBaseInstructionList() {
		return formatList;
	}

	public static Map<String, String> getConditionals() {
		return opcodeMnemonicMap;
	}

	/**
	 * Loads instruction formats from the specified file.
	 *
	 * @return a list of {@code Format} as read from the file.
	 *
	 * @throws IOException if the file was not able to be read
	 */
	private static List<Format> loadInstructions() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(Format.FORMATS_TXT));
		String line;
		List<Format> formatList = new ArrayList<>();
		while ((line = br.readLine()) != null) {
			if (line.startsWith("0") || line.startsWith("1")) {
				String[] arr = line.split(",");
				formatList.add(new Format(arr[0], arr[1], arr[2]));
			}
		}
		return formatList;
	}

	@Override
	public String toString() {
		return "Format{" +
				"opcode='" + opcode + '\'' +
				", name='" + name + '\'' +
				", format='" + format + '\'' +
				'}';
	}
}