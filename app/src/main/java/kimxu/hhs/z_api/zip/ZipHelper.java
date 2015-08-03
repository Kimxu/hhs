package kimxu.hhs.z_api.zip;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import kimxu.hhs.z_volley.base.GenericHelper;

public interface ZipHelper extends GenericHelper {

	public abstract byte[] unzip(byte[] src, String name) throws IOException;

	public abstract byte[] unzip(String file, String name)
			throws FileNotFoundException, IOException;

	public abstract String unzipToFile(String file, String name, String oFile)
			throws IOException;

	public abstract List<String> unzipAllToFile(String file, String dist)
			throws IOException;

	public abstract File zip(String dist, String doc, Map<String, String> srcMap)
			throws FileNotFoundException, IOException;

	public abstract File zip(String dist, String[] srcFiles,
							 Map<String, String> srcMap) throws IOException;

}