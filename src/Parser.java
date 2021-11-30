import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Parser {

	public static void main(String[] args) throws IOException {
		for (byte[] b : Objects.requireNonNull(readLinesFromFile("C:\\Users\\yjohn\\Desktop\\instructions.legv8asm.machine"))) {
			Main.translate(b);
		}
	}


	public static List<byte[]> readLinesFromFile(String fileLocation) {
		try {
			InputStream is = new FileInputStream(fileLocation);
			List<byte[]> byteList = new ArrayList<>();
			byte[] buffer = new byte[4];
			while (is.read(buffer) != -1) {
				arrayToBinary(buffer);
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

	public static String[] arrayToBinary(byte[] buffer) {
		String[] arr = new String[4];
		int i=0;
		String s;
		for (byte b : buffer) {
			s = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
			arr[i] = s;
//			System.out.print(s + " ");
			++i;
			i = i %4;
		}
		return arr;

	}
}