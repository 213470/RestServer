package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.FilesInfo;

public class Crawler {

	public static String JSON_FILENAME = "files.json";

	private String homeDir;
	private List<File> fileList;
	private List<File> differenceList;
	private String username;

	public Crawler(String homeDir, String username) {
		this.homeDir = homeDir.endsWith(File.separator) ? homeDir : homeDir + File.separator;
		this.fileList = new LinkedList<File>();
		this.differenceList = new LinkedList<File>();
		new File(homeDir + File.separator + JSON_FILENAME);
		this.username = username;
	}

	public void scanFiles() {
		fileList.clear();
		walk(homeDir);
	}

	private void walk(String path) {
		File root = new File(path);
		File[] list = root.listFiles();
		if (list == null)
			return;

		Arrays.sort(list, new Comparator<Object>() {
			@Override
			public int compare(Object f1, Object f2) {
				return ((File) f1).getPath().compareTo(((File) f2).getPath());
			}
		});

		for (File f : list) {
			if (!f.isDirectory()) {
				if (!f.getPath().endsWith("files.json")) {
					fileList.add(f);
					System.out.println(f.getPath());
				}
			} else {
				walk(f.getPath());
			}

		}

	}

	/*
	 * Creates JSON file mapped
	 */
	public void updateJSON() {
		File home = new File(homeDir + "/files.json");
		FilesInfo fp = new FilesInfo(username, fileList);
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(home, fp);
		} catch (JsonGenerationException e) {
			System.out.println(e.getClass());
			e.printStackTrace();
		} catch (JsonMappingException e) {
			System.out.println(e.getClass());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.getClass());
			e.printStackTrace();
		}
	}

	public String returnJson() {
		File file = new File(homeDir + File.separator + "files.json");
		BufferedReader br = null;
		FileReader fr = null;
		String json = "";
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String character;
			while ((character = br.readLine()) != null) {
				json += character;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return json;
	}

	public boolean checkForDifference(List<File> listToCheck) {
		ObjectMapper mapper = new ObjectMapper();
		List<File> retainedFiles = new LinkedList<>();
		retainedFiles.addAll(fileList);
		differenceList.clear();
		differenceList.addAll(fileList);
		// try {
		// FilesInfo savedContent = mapper.readValue(new File(homeDir +
		// JSON_FILENAME), FilesInfo.class);
		// retainedFiles.retainAll(savedContent.getFileList());
		retainedFiles.retainAll(listToCheck);
		differenceList.removeAll(retainedFiles);
		// if (savedContent.getFileList().retainAll(fileList)) {
		// updateJSON(savedContent.getUsername());
		// System.out.println("JSON updated");
		// }
		// } catch (JsonParseException e) {
		// System.out.println(e.getClass());
		// e.printStackTrace();
		// } catch (JsonMappingException e) {
		// System.out.println(e.getClass());
		// e.printStackTrace();
		// } catch (IOException e) {
		// System.out.println(e.getClass());
		// e.printStackTrace();
		// }
		differenceList = retainClientToServerFiles(listToCheck);
		
		System.out.println("File List");
		System.out.println(listToCheck);
		System.out.println("Retained List");
		System.out.println(retainedFiles);
		System.out.println("Difference List");
		System.out.println(differenceList);
		
		

		if (!fileList.isEmpty() && !differenceList.isEmpty()) {
			return true;
		}
		return false;
	}

	public List<File> getDifferenceList() {
		return differenceList;
	}

	public List<File> getFileList() {
		return fileList;
	}

	public String getHomeDir() {
		return homeDir;
	}

	private List<File> retainClientToServerFiles(List<File> toCheck){
		List<File> retained = new LinkedList<>();
		List<File> diff = fileList;
		
		for(File client : fileList){
			Pattern p = Pattern.compile(".+Server\\\\"+username+"\\\\"+client.getName()+"");
			System.out.println(client.getName());
			for(File server : toCheck){
				Matcher m = p.matcher(server.getAbsolutePath());
				if(m.matches()){
					retained.add(client);
					System.out.println(server.getName() + " - " +client.getName());
				}
			}
			
		}		
		diff.removeAll(retained);
		
		return diff;
	}

}
