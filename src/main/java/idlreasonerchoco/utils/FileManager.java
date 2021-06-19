package idlreasonerchoco.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import idlreasonerchoco.configuration.ErrorType;
import idlreasonerchoco.configuration.IDLException;

public class FileManager {
	
	private final static Logger LOG = Logger.getLogger(FileManager.class);

    public static File createFileIfNotExists(String filePath) throws IDLException {
    	File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (Exception e) {
            	ExceptionManager.rethrow(LOG, ErrorType.ERROR_CREATING_FILE.toString(), e);
            }
        }
        return file;
    }

	public static void appendContentToFile(String filePath, String content) throws IDLException {
		File file = new File(filePath);
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
			out.append(content);
			out.flush();
			out.close();
		} catch (IOException e) {
			ExceptionManager.rethrow(LOG, ErrorType.ERROR_WRITING_FILE.toString(), e);
		}
	}

}
