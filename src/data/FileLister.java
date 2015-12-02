package data;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class FileLister {
	private final String path;
	private ArrayList<String> files = new ArrayList<String>();
	private final PathMatcher pm;
	
	public FileLister(final String path, final String ext) {
		this.path = path;
		pm = FileSystems.getDefault().getPathMatcher("glob:**." + ext);
		grabFiles();
	}

	public ArrayList<String> getFiles() {
		return files;
	}
	
	public String getRandomFile() {
		ArrayList<String> shuffled = new ArrayList<String>();
		files.forEach(element -> shuffled.add(element));
		Collections.shuffle(shuffled);
		return shuffled.get(0);
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
