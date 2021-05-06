package zad2;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Futil {
	private static Path outPath;
	private static FileChannel outFileChannel;
	private static FileChannel inFileChannel;
	private static ByteBuffer byteBuffer;
	
	public static void processDir(String dirName, String resultFileName)
	{
		outPath = Paths.get(System.getProperty("user.dir") + "/" + resultFileName);
		
		try {
			outFileChannel = FileChannel.open(outPath, new OpenOption[] {StandardOpenOption.CREATE, StandardOpenOption.APPEND});
			
			PrintWriter writer = new PrintWriter(outPath.toString());
			writer.print("");
			writer.close();
			
			Files.walk(Paths.get(dirName), FileVisitOption.FOLLOW_LINKS)
				.filter(Files::isRegularFile)
				.forEach(file -> {
					if (file.toString().substring(file.toString().lastIndexOf(".") + 1).equals("txt"))
					{
						try {
							inFileChannel = FileChannel.open(file);
							byteBuffer = ByteBuffer.allocate((int)(new File(file.toString()).length()));
							byteBuffer.clear();
							inFileChannel.read(byteBuffer);
						} catch (IOException ex) {
							System.err.println(ex.getMessage());
						}
						
						byteBuffer.flip();
	
						CharBuffer charBuffer = Charset.forName("Cp1250").decode(byteBuffer);
						ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(charBuffer);
						
						while(byteBuffer.hasRemaining()) {
							try {
								outFileChannel.write(byteBuffer);
							} catch (IOException ex) {
								System.err.println(ex.getMessage());
							}
						}
					}
			});
		} catch (IOException ex) {
		    System.err.println(ex.getMessage());
		}
	}
}
