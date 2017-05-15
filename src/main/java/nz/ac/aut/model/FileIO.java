/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The FileIO class takes the responsibility of saving the current progress
 * information to a text file and loading the progress information from a text
 * file.
 * 
 * @author xfn
 */
public class FileIO {
	private ArrayList<ChessPoint> chessPointCollection;

	private PrintWriter pw = null;
	private FileOutputStream fos = null;

	private BufferedReader br = null;
	private FileReader fr = null;

	private String folderName = null;
	private String pathName = null;
	private File dir = null;

	public FileIO(ArrayList<ChessPoint> chessPointCollection) {
		this.chessPointCollection = chessPointCollection;
		// The destination folder under which the game progress information is
		// to be saved.
		folderName = "./dataFile/";

		initialize();
	}

	// Initialize the the FileIo instance. Such as the pathName and the dir File
	// instance.
	private void initialize() {
		DateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();

		pathName = folderName + "progress - " + dateFormatter.format(date) + ".txt";
		setDir(new File(folderName));
	}

	// Save game progress information into a text file.
	public void saveProgress() {
		try {
			fos = new FileOutputStream(pathName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		pw = new PrintWriter(fos);

		// Print the chess point information to the pw chess point by chess
		// point.
		for (ChessPoint chessPoint : chessPointCollection) {
			pw.println(chessPoint.getX() + "," + chessPoint.getY() + "," + chessPoint.getChessColor().toString());
		}

		pw.flush();
		pw.close();
	}

	/**
	 * Print the list of the text files under the directory line by line and
	 * return an File array containing each file.
	 */
	public File[] listFiles() {
		int index = 1;
		File[] files = dir.listFiles();

		for (File file : files) {
			System.out.println(index + ". " + file.getName());
			index++;
		}

		return files;
	}

	/**
	 * Read the game progress information from the text file.
	 */
	public void readProgress(String pathName) {
		String line = null;

		try {
			fr = new FileReader(pathName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		br = new BufferedReader(fr);

		try {
			while ((line = br.readLine()) != null) {
				String[] split = line.split(",");
				// Populate the chess point collection by using the constructed
				// chess point object.
				chessPointCollection.add(new ChessPoint(Integer.valueOf(split[0]), Integer.valueOf(split[1]), ChessColor.valueOf(split[2])));
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	public PrintWriter getPw() {
		return pw;
	}

	public FileOutputStream getFos() {
		return fos;
	}

	public String getPathString() {
		return pathName;
	}

	public void setPw(PrintWriter pw) {
		this.pw = pw;
	}

	public void setFos(FileOutputStream fos) {
		this.fos = fos;
	}

	public void setPathString(String pathString) {
		this.pathName = pathString;
	}

	public File getDir() {
		return dir;
	}

	public void setDir(File dir) {
		this.dir = dir;
	}

}
