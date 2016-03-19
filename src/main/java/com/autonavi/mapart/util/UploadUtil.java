package com.autonavi.mapart.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传类
 * 
 * @author qiang.cai
 * 
 */
public class UploadUtil {

	/**
	 * @param request
	 * @param uploadDir
	 * @param inputfile
	 * @return filename 文件存储路径
	 */
	public static String fileUpload(MultipartFile file,
			String uploadDir, String inputfile) {
		String fileName = null;
		InputStream stream = null;
		OutputStream bos = null;
		try {
			if (!file.isEmpty()) {
				File dirPath = new File(uploadDir);
				if (!dirPath.exists()) {
					dirPath.mkdirs();
				}
				// 原始文件名
				String filename = file.getOriginalFilename();
				// 新文件名
				fileName = DateFormat.getStringCurrentDetialDate() + "_"
						+ filename;
				stream = file.getInputStream();
				bos = new FileOutputStream(dirPath + File.separator + fileName);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}
			}
			return uploadDir + fileName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				bos.close();
				stream.close();
			} catch (IOException e) {}
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param file
	 *            源文件
	 * @return
	 */
	public static void delFile(String filepath) {
		try {
			File file = new File(filepath);
			// 路径为文件且不为空则进行删除
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
