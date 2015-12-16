package data;

/** Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing I (Unthief)
 * (C) 2015 Kevin Otte, Adrian Schmidt, Fabian Schneider
 */

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class FileLister {
	/** Pfad zur Datei. */
	private final String path;
	/** Liste mit gefundenen Datein. */
	private ArrayList<String> files = new ArrayList<String>();
	private final PathMatcher pm;
	
	public FileLister(final String path, final String ext) {
		this.path = path;
		pm = FileSystems.getDefault().getPathMatcher("glob:**." + ext);
		grabFiles();
	}
	
	/**
	 * Sucht nach Dateien im angegebenen Pfad.
	 */
	private void grabFiles() {
		try {
			Files.walk(Paths.get(path)).forEach(file -> {
				if(Files.isRegularFile(file) && pm.matches(file)){
					files.add(file.getFileName().toString());
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Getter fuer eine zufaellige Datei.
	 * @return die zufaellige Datei.
	 */
	public String getRandomFile() {
		ArrayList<String> shuffled = new ArrayList<String>();
		files.forEach(element -> shuffled.add(element));
		Collections.shuffle(shuffled);
		return shuffled.get(0);
	}

	/**
	 * Getter fuer die gefundenen Dateien.
	 * @return die gefundenen Dateien.
	 */
	public ArrayList<String> getFiles() {
		return files;
	}
}
