/**
 * 
 */
package com.autonavi.mapart.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;
import org.apache.tools.ant.taskdefs.Zip;

/**
 * @author huandi.yang
 *
 */
public class ZipUtil {

	/**
	 * @param args
	 * @throws IOException
	 * 
	 */
	private static Logger log = Logger.getLogger("ZipUtil"); 
	public static void main(String[] args) throws IOException {
		/**
		 * 解压文件
		 */
		String path = "D:\\home\\MapFacilityArea\\importFile\\20150116163904_14Q4版变形前母库设施区域.zip";
		File zipFile = new File(path);
		System.out.println(zipFile.exists());
		String fileStr = zipFile.getAbsolutePath().substring(0,
				zipFile.getAbsolutePath().lastIndexOf(".zip")).replaceAll("\\\\", "/")+"/";
		unZipFiles(zipFile, fileStr);
//		extractFolder(zipFile.getAbsolutePath());

	}

	/**
	 * 解压文件到指定目录
	 * 
	 * @param zipFile
	 * @param descDir
	 * @author isea533
	 */
	@SuppressWarnings({ "rawtypes", "resource" })
	public static void unZipFiles(File zipFile, String descDir) throws IOException {
		log.debug("存放解压文件的目录："+descDir);
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		ZipFile zip = new ZipFile(zipFile,Charset.forName("Cp437"));
		for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = zip.getInputStream(entry);
			String kk = zipEntryName.substring(zipEntryName.lastIndexOf("/")+1,zipEntryName.length());
			String outPath = descDir + kk;
			log.debug("解压后的文件路径："+outPath);
			if (new File(outPath).isDirectory()) {// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
				continue;
			}
			OutputStream out = new FileOutputStream(outPath);// 输出文件路径信息
			byte[] buf1 = new byte[1024];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			in.close();
			out.close();
		}
		System.out.println("******************解压完毕********************");
	}

	static public void extractFolder(String zipFile) throws ZipException,
			IOException {
		int BUFFER = 2048;
		File file = new File(zipFile);

		ZipFile zip = new ZipFile(file);
		String newPath = zipFile.substring(0, zipFile.length() - 4);

		// new File(newPath).mkdir();
		Enumeration zipFileEntries = zip.entries();

		// Process each entry
		while (zipFileEntries.hasMoreElements()) {
			// grab a zip file entry
			ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
			String currentEntry = entry.getName();
			File destFile = new File(newPath, currentEntry);
			// destFile = new File(newPath, destFile.getName());
			File destinationParent = destFile.getParentFile();

			// create the parent directory structure if needed
			destinationParent.mkdirs();

			if (!entry.isDirectory()) {
				BufferedInputStream is = new BufferedInputStream(
						zip.getInputStream(entry));
				int currentByte;
				// establish buffer for writing file
				byte data[] = new byte[BUFFER];

				// write the current file to disk
				FileOutputStream fos = new FileOutputStream(destFile);
				BufferedOutputStream dest = new BufferedOutputStream(fos,
						BUFFER);

				// read and write until last byte is encountered
				while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, currentByte);
				}
				dest.flush();
				dest.close();
				is.close();
			}

			if (currentEntry.endsWith(".zip")) {
				// found a zip file, try to open
				extractFolder(destFile.getAbsolutePath());
			}
		}
	}

}
