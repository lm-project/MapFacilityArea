package com.autonavi.mapart.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class TarUtil {
	private static Logger log = Logger.getLogger("TarUtil");
	public static File pack(File[] sources, File target) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(target);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		TarArchiveOutputStream os = new TarArchiveOutputStream(out);
		for (File file : sources) {
			try {
				File tmpFile = new File(file.getName());
				FileUtils.copyFile(file, tmpFile);
				os.putArchiveEntry(new TarArchiveEntry(tmpFile));

				IOUtils.copy(new FileInputStream(tmpFile), os);

				os.closeArchiveEntry();
				log.debug("要删除的文件："+tmpFile.getAbsolutePath());
				FileUtils.forceDelete(tmpFile);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}
		if (os != null) {
			try {
				os.flush();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return target;
	}
}
