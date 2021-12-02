import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BaseInstruction {
	static List<BaseInstruction> baseInstructionList = null;
	static final Map<String, String> CONDITIONAL = new HashMap<String, String>() {{
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

	static {
		try {
			baseInstructionList = BaseInstruction.loadInstructions();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String opcode;
	public String name;
	public String format;

	public BaseInstruction(String opcode, String name, String format) {
		this.opcode = opcode;
		this.name = name;
		this.format = format;
	}

	static List<BaseInstruction> loadInstructions() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("instructions"));
		String line;
		List<BaseInstruction> baseInstructionList = new ArrayList<>();
		while ((line = br.readLine()) != null) {
			if (line.startsWith("0") || line.startsWith("1")) {
				String[] arr = line.split(",");
				baseInstructionList.add(new BaseInstruction(arr[0], arr[1], arr[2]));
			}
		}
		return baseInstructionList;
	}

	@Override
	public String toString() {
		return "BaseInstruction{" +
				"opcode='" + opcode + '\'' +
				", name='" + name + '\'' +
				", format='" + format + '\'' +
				'}';
	}
}