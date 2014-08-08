package com.lqk.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * 
 * @ClassName: ZipUtil
 * @Description: 压缩工具
 * @author longqiankun
 * @date 2014-7-10 下午3:51:09
 * 
 */
public class ZipUtil {
	/**
	 * 功能:压缩多个文件成一个zip文件
	 * 
	 * @param srcfile
	 *            ：源文件列表
	 * @param zipfile
	 *            ：压缩后的文件
	 */
	public static void zipFiles(List<File> srcfile, File zipfile) {
		byte[] buf = new byte[1024];
		try {
			// ZipOutputStream类：完成文件或文件夹的压缩
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					zipfile));
			for (File file : srcfile) {
				FileInputStream in = new FileInputStream(file);
				out.putNextEntry(new ZipEntry(file.getName()));
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 功能:解压缩
	 * 
	 * @param zipfile
	 *            ：需要解压缩的文件
	 * @param descDir
	 *            ：解压后的目标目录
	 */
	public static void unZipFiles(File zipfile, String descDir) {
		try {
			ZipFile zf = new ZipFile(zipfile);
			for (Enumeration entries = zf.entries(); entries.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String zipEntryName = entry.getName();
				InputStream in = zf.getInputStream(entry);
				OutputStream out = new FileOutputStream(descDir + zipEntryName);
				byte[] buf1 = new byte[1024];
				int len;
				while ((len = in.read(buf1)) > 0) {
					out.write(buf1, 0, len);
				}
				in.close();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** * 设置缓冲值 */
	static final int BUFFER = 8192;
	private static final String ALGORITHM = "PBEWithMD5AndDES";

	public static void zip(String zipFileName, String inputFile, String pwd)
			throws Exception {
		zip(zipFileName, new File(inputFile), pwd);
	}

	/**
	 * * 功能描述：压缩指定路径下的所有文件 * @param zipFileName 压缩文件名(带有路径) * @param inputFile
	 * 指定压缩文件夹 * @return * @throws Exception
	 * */
	public static void zip(String zipFileName, String inputFile)
			throws Exception {
		zip(zipFileName, new File(inputFile), null);
	}

	/**
	 * * 功能描述：压缩文件对象 * @param zipFileName 压缩文件名(带有路径) * @param inputFile 文件对象 * @return
	 * * @throws Exception
	 * 
	 * */
	public static void zip(String zipFileName, File inputFile, String pwd)
			throws Exception {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				zipFileName));
		zip(out, inputFile, "", pwd);
		out.close();
	}

	/**
	 * * * @param out 压缩输出流对象 * @param file * @param base * @throws Exception
	 * */
	public static void zip(ZipOutputStream outputStream, File file,
			String base, String pwd) throws Exception {
		if (file.isDirectory()) {
			File[] fl = file.listFiles();
			outputStream.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(outputStream, fl[i], base + fl[i].getName(), pwd);
			}
		} else {
			outputStream.putNextEntry(new ZipEntry(base));
			FileInputStream inputStream = new FileInputStream(file);
			// 普通压缩文件
			if (pwd == null || pwd.trim().equals("")) {
				int b;
				while ((b = inputStream.read()) != -1) {
					outputStream.write(b);
				}
				inputStream.close();
			}
			// 给压缩文件加密
			else {
				PBEKeySpec keySpec = new PBEKeySpec(pwd.toCharArray());
				SecretKeyFactory keyFactory = SecretKeyFactory
						.getInstance(ALGORITHM);
				SecretKey passwordKey = keyFactory.generateSecret(keySpec);
				byte[] salt = new byte[8];
				Random rnd = new Random();
				rnd.nextBytes(salt);
				int iterations = 100;
				PBEParameterSpec parameterSpec = new PBEParameterSpec(salt,
						iterations);
				Cipher cipher = Cipher.getInstance(ALGORITHM);
				cipher.init(Cipher.ENCRYPT_MODE, passwordKey, parameterSpec);
				outputStream.write(salt);
				byte[] input = new byte[64];
				int bytesRead;
				while ((bytesRead = inputStream.read(input)) != -1) {
					byte[] output = cipher.update(input, 0, bytesRead);
					if (output != null) {
						outputStream.write(output);
					}
				}
				byte[] output = cipher.doFinal();
				if (output != null) {
					outputStream.write(output);
				}
				inputStream.close();
				outputStream.flush();
				outputStream.close();
			}
		}
		file.delete();
	}

	public static void unzip(String zipFileName, String outputDirectory)
			throws Exception {
		ZipInputStream inputStream = new ZipInputStream(new FileInputStream(
				zipFileName));
		unzip(inputStream, outputDirectory, null);
	}

	/**
	 * * 功能描述：将压缩文件解压到指定的文件目录下 * @param zipFileName 压缩文件名称(带路径) * @param
	 * outputDirectory 指定解压目录 * @return * @throws Exception
	 * */
	public static void unzip(String zipFileName, String outputDirectory,
			String pwd) throws Exception {
		ZipInputStream inputStream = new ZipInputStream(new FileInputStream(
				zipFileName));
		unzip(inputStream, outputDirectory, pwd);
	}

	/**
	 * 
	 * @Title: unzip
	 * @Description: 文件解密
	 * @param @param zipFile
	 * @param @param outputDirectory
	 * @param @param pwd
	 * @param @throws Exception
	 * @return void
	 * @throws
	 */
	public static void unzip(File zipFile, String outputDirectory, String pwd)
			throws Exception {
		ZipInputStream inputStream = new ZipInputStream(new FileInputStream(
				zipFile));
		unzip(inputStream, outputDirectory, pwd);
	}

	/**
	 * 
	 * @Title: unzip
	 * @Description: 文件解压、解密
	 * @param @param inputStream
	 * @param @param outputDirectory
	 * @param @param pwd 密码
	 * @param @throws Exception
	 * @return void
	 * @throws
	 */
	public static void unzip(ZipInputStream inputStream,
			String outputDirectory, String pwd) throws Exception {
		ZipEntry zipEntry = null;
		FileOutputStream outputStream = null;
		try {
			while ((zipEntry = inputStream.getNextEntry()) != null) {
				if (zipEntry.isDirectory()) {
					String name = zipEntry.getName();
					name = name.substring(0, name.length() - 1);
					File file = new File(outputDirectory + File.separator
							+ name);
					file.mkdir();
				} else {
					File file = new File(outputDirectory + File.separator
							+ zipEntry.getName());
					file.createNewFile();
					outputStream = new FileOutputStream(file);
					// 普通解压缩文件
					if (pwd == null || pwd.trim().equals("")) {
						int b;
						while ((b = inputStream.read()) != -1) {
							outputStream.write(b);
						}
						outputStream.close();
					}
					// 解压缩加密文件
					else {
						PBEKeySpec keySpec = new PBEKeySpec(pwd.toCharArray());
						SecretKeyFactory keyFactory = SecretKeyFactory
								.getInstance(ALGORITHM);
						SecretKey passwordKey = keyFactory
								.generateSecret(keySpec);
						byte[] salt = new byte[8];
						inputStream.read(salt);
						int iterations = 100;
						PBEParameterSpec parameterSpec = new PBEParameterSpec(
								salt, iterations);
						Cipher cipher = Cipher.getInstance(ALGORITHM);
						cipher.init(Cipher.DECRYPT_MODE, passwordKey,
								parameterSpec);
						byte[] input = new byte[64];
						int bytesRead;
						while ((bytesRead = inputStream.read(input)) != -1) {
							byte[] output = cipher.update(input, 0, bytesRead);
							if (output != null) {
								outputStream.write(output);
							}
						}
						byte[] output = cipher.doFinal();
						if (output != null) {
							outputStream.write(output);
						}
						outputStream.flush();
						outputStream.close();
					}
				}
			}
			inputStream.close();
		} catch (IOException ex) {
			throw new Exception("解压读取文件失败");
		} catch (Exception ex) {
			throw new Exception("解压文件密码不正确");
		} finally {
			inputStream.close();

		}

		outputStream.flush();
		outputStream.close();
	}

	/**
	 * 
	 * 使用gzip进行压缩
	 */
	public static String gzip(String primStr) {
		if (primStr == null || primStr.length() == 0) {
			return primStr;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(primStr.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (gzip != null) {
				try {
					gzip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return new Base64().encode(out.toByteArray());
	}

	/**
	 * 
	 * <p>
	 * Description:使用gzip进行解压缩
	 * </p>
	 * 
	 * @param compressedStr
	 * @return
	 */
	public static String gunzip(String compressedStr) {
		if (compressedStr == null) {
			return null;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = null;
		GZIPInputStream ginzip = null;
		byte[] compressed = null;
		String decompressed = null;
		try {

			compressed = new Base64().decode(compressedStr);
			in = new ByteArrayInputStream(compressed);
			ginzip = new GZIPInputStream(in);

			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = ginzip.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ginzip != null) {
				try {
					ginzip.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}

		return decompressed;
	}

	/**
	 * 使用zip进行压缩
	 * 
	 * @param str
	 *            压缩前的文本
	 * @return 返回压缩后的文本
	 */
	public static final String zip(String str) {
		if (str == null)
			return null;
		byte[] compressed;
		ByteArrayOutputStream out = null;
		ZipOutputStream zout = null;
		String compressedStr = null;
		try {
			out = new ByteArrayOutputStream();
			zout = new ZipOutputStream(out);
			zout.putNextEntry(new ZipEntry("0"));
			zout.write(str.getBytes());
			zout.closeEntry();
			compressed = out.toByteArray();
			compressedStr = new Base64().encode(compressed);
		} catch (IOException e) {
			compressed = null;
		} finally {
			if (zout != null) {
				try {
					zout.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return compressedStr;
	}

	/**
	 * 使用zip进行解压缩
	 * 
	 * @param compressed
	 *            压缩后的文本
	 * @return 解压后的字符串
	 */
	public static final String unzip(String compressedStr) {
		if (compressedStr == null) {
			return null;
		}

		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		ZipInputStream zin = null;
		String decompressed = null;
		try {
			byte[] compressed = new Base64().decode(compressedStr);
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressed);
			zin = new ZipInputStream(in);
			zin.getNextEntry();
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			decompressed = null;
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return decompressed;
	}

}
