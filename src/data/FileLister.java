package data;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileLister {
	private final String path;
	private ArrayList<String> files = new ArrayList<String>();
	private final PathMatcher pm = FileSystems.getDefault().getPathMatcher("glob:**.jpg");
	
	public FileLister(final String path) {
		this.path = path;
		grabFiles();
	}

	public ArrayList<String> getFiles() {
		return files;
	}
	
	private void grabFiles() {
		try {
			Files.walk(Paths.get(path)).forEach(file -> {
				if(Files.isRegularFile(file) && pm.matches(file)){
					System.out.println(file.getFileName().toString());
					files.add(file.getFileName().toString());
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
