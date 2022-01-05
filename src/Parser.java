import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
/**
 * @author y-johnson
 */
public class Parser {
	public static AssemblyInstruction translate(byte[] buffer) {
		StringBuilder lineBuilder = new StringBuilder();
		for (byte b : buffer) {
			lineBuilder.append(getBinaryString(b));
		}
		String line = lineBuilder.toString();
		for (InstructionFormat i : InstructionFormat.getBaseInstructionList()) {
			try{
				return new AssemblyInstruction(line, i);
			} catch (NullPointerException ignored){}
		}
		return null;
	}

	public static List<byte[]> readLinesFromFile(String fileLocation) {
		try {
			InputStream is = new FileInputStream(fileLocation);
			List<byte[]> byteList = new ArrayList<>();
			byte[] buffer = new byte[4];
			while (is.read(buffer) != -1) {
				printBinaryBuffer(buffer);
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

	private static void printBinaryBuffer(byte[] buffer) {
		for (byte b : buffer) {
			String s1 = getBinaryString(b);
			System.out.print(s1 + " ");
		}
		System.out.println();
	}

	private static String getBinaryString(byte b) {
		return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
	}
}