package kimxu.hhs.z_api.zip;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import kimxu.hhs.z_base.AbstractManager;
import kimxu.hhs.z_base.RuntimeConstant;
import kimxu.hhs.z_volley.structure.ByteArrayPool;
import kimxu.hhs.z_volley.tools.FileHelper;

public final class ZipManager extends AbstractManager implements ZipHelper {
	static final int BUFFER_SIZE = Integer.parseInt(RuntimeConstant.MEMORY_TYPE
			.getConfig("zip_buffer"));
	static final int C_BUFFER_SIZE = 10240;
	static final int R_BUFFER_SIZE = 10240;
	// private ByteArrayOutputStream bas;
	// private ZipFile zipFile;
	// private FileHelper fileHelper;
	private static volatile ZipManager instance;

	public static ZipManager getInstance() {
		if (instance == null) {
			synchronized (ZipManager.class) {
				instance = new ZipManager();
			}
		}
		return instance;
	}

	private ZipManager() {
		// fileHelper = ApiType.file.getHandler();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.china.framework.util.zip.ZipHelper#unzip(byte[],
	 * java.lang.String)
	 */
	public byte[] unzip(byte[] src, String name) throws IOException {
		ByteArrayOutputStream bas = new ByteArrayOutputStream(BUFFER_SIZE);
		ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(src));
		byte[] buffer = null;
		try {
			int read;
			buffer = ByteArrayPool.getInstance().getBuf(C_BUFFER_SIZE);
			ZipEntry entry;
			while ((entry = in.getNextEntry()) != null) {
				String nm = entry.getName();
				if (nm.equals(name)) {
					while ((read = in.read(buffer)) > 0) {
						bas.write(buffer, 0, read);
					}
					in.closeEntry();
					break;
				}
				in.closeEntry();
			}

			return bas.toByteArray();
		} finally {
			in.close();
			if (buffer != null)
				ByteArrayPool.getInstance().returnBuf(buffer);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.china.framework.util.zip.ZipHelper#unzip(java.lang.String,
	 * java.lang.String)
	 */
	public byte[] unzip(String file, String name) throws FileNotFoundException,
			IOException {
		ByteArrayOutputStream bas = new ByteArrayOutputStream(BUFFER_SIZE);
		ZipFile zipFile = new ZipFile(file);
		byte[] buffer = null;
		try {
			ZipEntry entry = zipFile.getEntry(name);
			if (entry != null) {
				InputStream is = zipFile.getInputStream(entry);
				buffer = ByteArrayPool.getInstance().getBuf(R_BUFFER_SIZE);
				int read;
				while ((read = is.read(buffer)) > 0) {
					bas.write(buffer, 0, read);
				}
				// LOG.log(Level.WARNING, "doc size:{0},{1}", new
				// Object[]{bas.size(), name});
			}
			return bas.toByteArray();
		} finally {
			zipFile.close();
			if (buffer != null)
				ByteArrayPool.getInstance().returnBuf(buffer);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.app.china.framework.util.zip.ZipHelper#unzipToFile(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public String unzipToFile(String file, String name, String oFile)
			throws IOException {
		FileOutputStream bas = new FileOutputStream(new File(oFile));
		ZipFile zipFile = new ZipFile(file);
		try {
			ZipEntry entry = zipFile.getEntry(name);
			if (entry != null) {
				InputStream is = zipFile.getInputStream(entry);
				byte[] buffer = new byte[R_BUFFER_SIZE];
				int read;
				while ((read = is.read(buffer)) > 0) {
					bas.write(buffer, 0, read);
				}

			}
			bas.flush();
			bas.close();
			// LOG.log(Level.WARNING, "doc name:{0}", name);
			return oFile;
		} finally {
			zipFile.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.app.china.framework.util.zip.ZipHelper#unzipAllToFile(java.lang.String
	 * , java.lang.String)
	 */
	public List<String> unzipAllToFile(String file, String dist)
			throws IOException {
		ArrayList<String> ret = new ArrayList<String>();
		ZipFile zipFile = new ZipFile(file);
		try {
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (!entry.isDirectory()) {
					String oFile = dist + entry.getName();
					File af = new File(oFile);
					FileHelper.buildDicIfNoExist(af.getParentFile()
							.getAbsolutePath());
					if (af.createNewFile()) {
						FileOutputStream bas = null;
						try {
							bas = new FileOutputStream(af);
							InputStream is = zipFile.getInputStream(entry);
							byte[] buffer = new byte[R_BUFFER_SIZE];
							int read;
							while ((read = is.read(buffer)) > 0) {
								bas.write(buffer, 0, read);
							}
						} finally {
							if (bas != null) {
								bas.close();
							}
						}
					} else {
						ret.add(entry.getName());
					}
				} else {
					String oFile = dist + entry.getName();
					FileHelper.buildDicIfNoExist(oFile);
				}
			}
			return ret;
		} finally {
			zipFile.close();
		}
		// bas.flush();
		// bas.close();
		// LOG.log(Level.WARNING, "doc name:{0}", name);
		// return oFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.china.framework.util.zip.ZipHelper#zip(java.lang.String,
	 * java.lang.String, java.util.Map)
	 */
	public File zip(String dist, String doc, Map<String, String> srcMap)
			throws FileNotFoundException, IOException {
		// System.out.println(doc + " " + dist);

		File dict = new File(doc), oFile = new File(dist);

		if (oFile.exists() && !oFile.delete()) {
			return oFile;
		}
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(oFile));
		Set<String> ks = null;

		if (srcMap != null) {
			ks = srcMap.keySet();
			for (String k : ks) {
				out.putNextEntry(new ZipEntry(k));
				out.write(srcMap.get(k).getBytes());
				out.closeEntry();
			}
		}
		if (dict.isDirectory()) {
			zipFileToEntry(out, ks, dict, "");
		} else if (dict.isFile()) {
			// File tmp=new File(System.currentTimeMillis()+doc);
			// System.out.println("zip a file ");
			ZipInputStream in = new ZipInputStream(new FileInputStream(dict));
			zipZipToEntry(out, in, ks);

		}
		out.finish();
		out.close();
		return oFile;
	}

	private void zipFileToEntry(ZipOutputStream out, Set<String> ks, File dict,
			String prefix) throws FileNotFoundException, IOException {
		File[] fs = dict.listFiles();
		if (fs.length > 0) {
			for (File f : fs) {
				if (f.isDirectory()) {
					zipFileToEntry(out, ks, f, prefix + f.getName() + "/");
				} else if (f.isFile()) {
					String name = f.getName();
					// System.out.println(name);

					String zipName = prefix + name;
					if (ks == null || !ks.contains(zipName)) {
						FileInputStream fin = null;
						try {
							fin = new FileInputStream(f);
							int read;
							byte[] buffer = new byte[R_BUFFER_SIZE];
							out.putNextEntry(new ZipEntry(zipName));
							while ((read = fin.read(buffer)) > 0) {
								out.write(buffer, 0, read);
							}
							out.closeEntry();
						} catch (IOException ex) {
							//L.e(ex);
							throw ex;
						} finally {
							if (fin != null) {
								fin.close();
							}
						}
					}
				}
			}
		} else {
			String zipName = prefix + dict.getName() + "/";
			out.putNextEntry(new ZipEntry(zipName));
			out.closeEntry();
		}
		// System.out.println(dict.getName());
	}

	private void zipZipToEntry(ZipOutputStream out, ZipInputStream in,
			Set<String> ks) throws IOException {

		ZipEntry entry;
		byte[] buffer = new byte[R_BUFFER_SIZE];
		int read;
		while ((entry = in.getNextEntry()) != null) {
			String name = entry.getName();
			// System.out.println("name:" + name);
			if (ks == null || !ks.contains(name)) {
				out.putNextEntry(new ZipEntry(name));

				while ((read = in.read(buffer)) > 0) {
					out.write(buffer, 0, read);
				}
				out.closeEntry();
			}
			in.closeEntry();
		}

	}

	// public boolean containFile(byte[] src,String name){
	//
	//
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.china.framework.util.zip.ZipHelper#zip(java.lang.String,
	 * java.lang.String[], java.util.Map)
	 */
	public File zip(String dist, String[] srcFiles, Map<String, String> srcMap)
			throws IOException {
		ZipOutputStream out = null;

		File oFile = new File(dist);
		if (oFile.exists() && !oFile.delete()) {
			return oFile;
		}
		out = new ZipOutputStream(new FileOutputStream(oFile));
		Set<String> ks = null;
		if (srcMap != null) {
			ks = srcMap.keySet();
			for (String k : ks) {
				out.putNextEntry(new ZipEntry(k));
				out.write(srcMap.get(k).getBytes());
				out.closeEntry();
			}
		}
		if (srcFiles != null) {
			for (String src : srcFiles) {
				// System.out.println(src);
				File af = new File(src);
				if (!ks.contains(src) && af.exists()) {
					out.putNextEntry(new ZipEntry(af.getName()));
					FileInputStream fin = null;
					try {
						fin = new FileInputStream(af);
						int read;
						byte[] buffer = new byte[R_BUFFER_SIZE];
						// out.putNextEntry(new ZipEntry(zipName));
						while ((read = fin.read(buffer)) > 0) {
							out.write(buffer, 0, read);
						}
						out.closeEntry();
					} catch (IOException ex) {
						//L.e(ex);
						throw ex;
					} finally {
						if (fin != null) {
							fin.close();
						}
					}
				}
			}
		}
		out.finish();
		out.close();
		return oFile;

	}
}
