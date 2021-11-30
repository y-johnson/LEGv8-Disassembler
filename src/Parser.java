import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Parser {

	public static void main(String[] args) throws IOException {
		readLinesFromFile("C:\\Users\\yjohn\\Desktop\\instructions.legv8asm.machine");
		System.out.println(Instruction.loadInstructions());
	}


	public static List<byte[]> readLinesFromFile(String fileLocation) {
		try {
			InputStream is = new FileInputStream(fileLocation);
			List<byte[]> byteList = new ArrayList<>();
			byte[] buffer = new byte[4];
			while (is.read(buffer) != -1) {
				for (byte b : buffer) {
					String s1 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
					System.out.print(s1 + " ");
				}
				System.out.println();
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