import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
	public static void main(String[] args) throws IOException {
		ArrayList<String> s = new ArrayList<>();
		for (byte[] b : Objects.requireNonNull(Parser.readLinesFromFile("C:\\Users\\yjohn\\Desktop\\instructions.legv8asm.machine"))) {
			s.add(Parser.translate(b));
		}

		for (String str : s) {
			System.out.println(str);
		}
	}
}