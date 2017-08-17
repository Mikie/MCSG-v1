package me.smiileyface.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.bukkit.ChatColor;

public class ZipUtils {

	public static void delete(File file) throws IOException {
		if(file.isDirectory()) {
			if(file.list().length == 0) {
				file.delete();
				System.out.println("Directory is delted : " + file.getAbsolutePath());
			} else {
				String files[] = file.list();
				
				for(String temp : files) {
					File fileDelete = new File(file, temp);
					delete(fileDelete);
				}
				
				if(file.list().length == 0) {
					file.delete();
					System.out.println("Directory is delted : " + file.getAbsolutePath());
				}
			}
		} else {
			file.delete();
			System.out.println("Directory is delted : " + file.getAbsolutePath());
		}
	}
	
	public static void extractZIP(File archive, File destDir) throws IOException {
		if(!destDir.exists()) {
			destDir.mkdirs();
		}
		ZipFile zipFile = new ZipFile(archive);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		
		byte[] buffer = new byte[16384];
		int len;
		while(entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String entryFileName = entry.getName();
			File dir = buildDirectoryHierarchyFor(entryFileName, destDir);
			
			if(!dir.exists()) {
				dir.mkdirs();
			}
			
			if(!entry.isDirectory()) {
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(destDir, entryFileName)));
				BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
				while((len = bis.read(buffer)) > 0) {
					bos.write(buffer, 0, len);
				}
				
				bos.flush();
				bos.close();
				bis.close();
			}
		}
		zipFile.close();
		System.out.println(ChatColor.RED + "The file has been unzipped.");
	}
	
	public static File buildDirectoryHierarchyFor(String entryName, File destDir) {
		int lastIndex = entryName.lastIndexOf('/');
		String internalPathToEntry = entryName.substring(0, lastIndex + 1);
		return new File(destDir, internalPathToEntry);
	}
}
