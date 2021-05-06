package zad1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil extends SimpleFileVisitor<Path>{
	private static Path outPath;
	private static FileChannel outFileChannel;
	private FileChannel inFileChannel;
	private ByteBuffer byteBuffer;
	private static String fileName;
	
	public static void processDir(String dirName, String resultFileName)
	{
		fileName = resultFileName;
		outPath = Paths.get(System.getProperty("user.dir") + "/" + resultFileName);
		
		try {
			outFileChannel = FileChannel.open(outPath, new OpenOption[] {StandardOpenOption.CREATE, StandardOpenOption.APPEND});
			
			PrintWriter writer = new PrintWriter(outPath.toString());
			writer.print("");
			writer.close();
			
			Path walkFileTree = Files.walkFileTree(Paths.get(dirName), new Futil());
		} catch (IOException ex) {
		    System.err.println(ex.getMessage());
		}
	}
	@Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws FileNotFoundException, IOException
	{
		if (attr.isRegularFile())
		{
			if (file.toString().substring(file.toString().lastIndexOf(".") + 1).equals("txt"))
			{
	        	inFileChannel = FileChannel.open(file);
	        	byteBuffer = ByteBuffer.allocate((int) attr.size());
	        	byteBuffer.clear();
	            inFileChannel.read(byteBuffer);
	            byteBuffer.flip();
	            CharBuffer charBuffer = Charset.forName("Cp1250").decode(byteBuffer);
	            ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(charBuffer);
	            
	            while(byteBuffer.hasRemaining()) 
	            	outFileChannel.write(byteBuffer);
			}
        }
        return FileVisitResult.CONTINUE;
    }
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc)
    {    
    	 return FileVisitResult.CONTINUE;
    }
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc)
    {    
    	System.err.println("Error: " + exc);
        return FileVisitResult.CONTINUE;
    }
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
	{
		return FileVisitResult.CONTINUE;
	}
}
