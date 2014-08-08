package com.lqk.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.util.Log;

import com.lqk.framework.app.Ioc;

/** 
 * @Company: Dilitech
 * @author longqiankun
 * @email qiankun.long@dilitech.com
 * @Title: FileUtils.java
 * @Description: 文件工具
 * @version 1.0  
 * @created 2013-12-30 下午2:28:03 
 */

public class FileUtils {
	 private final static int BUFFER_SIZE = 400000;
	    public final static String FILE_EXTENSION_SEPARATOR = ".";

		/**
		 * read file
		 * 
		 * @param filePath
		 * @return if file not exist, return null, else return content of file
		 * @throws IOException
		 *             if an error occurs while operator BufferedReader
		 */
		public static StringBuilder readFile(String filePath) {
			File file = new File(filePath);
			StringBuilder fileContent = new StringBuilder("");
			if (file == null || !file.isFile()) {
				return null;
			}

			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				String line = null;
				while ((line = reader.readLine()) != null) {
					if (!fileContent.toString().equals("")) {
						fileContent.append("\r\n");
					}
					fileContent.append(line);
				}
				reader.close();
				return fileContent;
			} catch (IOException e) {
				throw new RuntimeException("IOException occurred. ", e);
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						throw new RuntimeException("IOException occurred. ", e);
					}
				}
			}
		}



		/**
		 * read file to string list, a element of list is a line
		 * 
		 * @param filePath
		 * @return if file not exist, return null, else return content of file
		 * @throws IOException
		 *             if an error occurs while operator BufferedReader
		 */
		public static List<String> readFileToList(String filePath) {
			File file = new File(filePath);
			List<String> fileContent = new ArrayList<String>();
			if (file == null || !file.isFile()) {
				return null;
			}

			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				String line = null;
				while ((line = reader.readLine()) != null) {
					fileContent.add(line);
				}
				reader.close();
				return fileContent;
			} catch (IOException e) {
				throw new RuntimeException("IOException occurred. ", e);
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						throw new RuntimeException("IOException occurred. ", e);
					}
				}
			}
		}

		/**
		 * 文件拷贝
		 * 
		 * @author gdpancheng@gmail.com 2014-2-15 下午7:15:51
		 * @param sourceFile
		 * @param targetFile
		 * @throws IOException
		 * @return void
		 */
		public static void copyFile(File sourceFile, File targetFile) {
			BufferedInputStream inBuff = null;
			BufferedOutputStream outBuff = null;
			try {
				// 新建文件输入流并对它进行缓冲
				inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
				// 新建文件输出流并对它进行缓冲
				outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
				// 缓冲数组
				byte[] b = new byte[1024 * 5];
				int len;
				while ((len = inBuff.read(b)) != -1) {
					outBuff.write(b, 0, len);
				}
				// 刷新此缓冲的输出流
				outBuff.flush();
				sourceFile.delete();
				sourceFile.deleteOnExit();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 关闭流
				try {
					if (inBuff != null)
						inBuff.close();
					if (outBuff != null)
						outBuff.close();
				} catch (IOException e) {
				}
			}
		}

		public static File getExternalCacheDir(Context context, String dirs_name) {
			File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
			File appCacheDir = new File(new File(new File(dataDir, context.getPackageName()), "cache"), dirs_name);
			if (!appCacheDir.exists()) {
				try {
					new File(dataDir, ".nomedia").createNewFile();
				} catch (IOException e) {
					Log.w("创建目录", "Can't create \".nomedia\" file in application external cache directory", e);
				}
				if (!appCacheDir.mkdirs()) {
					Log.w("创建目录", "Unable to create external cache directory");
					return null;
				}
			}
			return appCacheDir;
		}

		public static void write(File file, String data) {
			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new FileWriter(file), 1024);
				out.write(data);
			} catch (IOException e) {
				Ioc.getIoc().getLogger().d("write " + file.getAbsolutePath() + " data failed!");
				e.printStackTrace();
			} finally {
				if (out != null) {
					try {
						out.flush();
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		public static void saveObject(String fileName,Serializable object) {
			FileOutputStream fos = null;
			 ObjectOutputStream oos = null;
			try {
				fos = Ioc.getIoc().getApplication().openFileOutput(fileName,Context.MODE_PRIVATE);
				oos = new ObjectOutputStream(fos);
		        oos.writeObject(object);// 写入
	        } catch (Exception e) {
		        e.printStackTrace();
	        } finally{
	        	try {
		            if (fos!=null) {
		            	fos.close();
		            }
		            if (oos!=null) {
		            	oos.close();
		            }
	            } catch (IOException e) {
	            }
	        }
	    }
		
		public static <T>T getObject(String fileName) {
			FileInputStream fis = null;
			ObjectInputStream ois = null;
			try {
				fis=Ioc.getIoc().getApplication().openFileInput(fileName);   //获得输入流
				ois = new ObjectInputStream(fis);
				return (T) ois.readObject();
	        } catch (Exception e) {
		        e.printStackTrace();
	        }finally{
	        	try {
		            if (fis!=null) {
		            	fis.close();
		            }
		            if (ois!=null) {
		            	ois.close();
		            }
	            } catch (IOException e) {
	            }
	        }
			return null;
	    }
	    /**
	     * read file
	     * 
	     * @param filePath
	     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
	     * @return if file not exist, return null, else return content of file
	     * @throws RuntimeException if an error occurs while operator BufferedReader
	     */
	    public static StringBuilder readFile(String filePath, String charsetName) {
	        File file = new File(filePath);
	        StringBuilder fileContent = new StringBuilder("");
	        if (file == null || !file.isFile()) {
	            return null;
	        }

	        BufferedReader reader = null;
	        try {
	            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
	            reader = new BufferedReader(is);
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                if (!fileContent.toString().equals("")) {
	                    fileContent.append("\r\n");
	                }
	                fileContent.append(line);
	            }
	            reader.close();
	            return fileContent;
	        } catch (IOException e) {
	            throw new RuntimeException("IOException occurred. ", e);
	        } finally {
	            if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e) {
	                    throw new RuntimeException("IOException occurred. ", e);
	                }
	            }
	        }
	    }

	    /**
	     * write file
	     * 
	     * @param filePath
	     * @param content
	     * @param append is append, if true, write to the end of file, else clear content of file and write into it
	     * @return return false if content is empty, true otherwise
	     * @throws RuntimeException if an error occurs while operator FileWriter
	     */
	    public static boolean writeFile(String filePath, String content, boolean append) {
	        if (StringUtils.isEmpty(content)) {
	            return false;
	        }

	        FileWriter fileWriter = null;
	        try {
	            makeDirs(filePath);
	            fileWriter = new FileWriter(filePath, append);
	            fileWriter.write(content);
	            fileWriter.close();
	            return true;
	        } catch (IOException e) {
	            throw new RuntimeException("IOException occurred. ", e);
	        } finally {
	            if (fileWriter != null) {
	                try {
	                    fileWriter.close();
	                } catch (IOException e) {
	                    throw new RuntimeException("IOException occurred. ", e);
	                }
	            }
	        }
	    }

	    /**
	     * write file
	     * 
	     * @param filePath
	     * @param contentList
	     * @param append is append, if true, write to the end of file, else clear content of file and write into it
	     * @return return false if contentList is empty, true otherwise
	     * @throws RuntimeException if an error occurs while operator FileWriter
	     */
	    public static boolean writeFile(String filePath, List<String> contentList, boolean append) {
	        if (ListUtils.isEmpty(contentList)) {
	            return false;
	        }

	        FileWriter fileWriter = null;
	        try {
	            makeDirs(filePath);
	            fileWriter = new FileWriter(filePath, append);
	            int i = 0;
	            for (String line : contentList) {
	                if (i++ > 0) {
	                    fileWriter.write("\r\n");
	                }
	                fileWriter.write(line);
	            }
	            fileWriter.close();
	            return true;
	        } catch (IOException e) {
	            throw new RuntimeException("IOException occurred. ", e);
	        } finally {
	            if (fileWriter != null) {
	                try {
	                    fileWriter.close();
	                } catch (IOException e) {
	                    throw new RuntimeException("IOException occurred. ", e);
	                }
	            }
	        }
	    }

	    /**
	     * write file, the string will be written to the begin of the file
	     * 
	     * @param filePath
	     * @param content
	     * @return
	     */
	    public static boolean writeFile(String filePath, String content) {
	        return writeFile(filePath, content, false);
	    }

	    /**
	     * write file, the string list will be written to the begin of the file
	     * 
	     * @param filePath
	     * @param contentList
	     * @return
	     */
	    public static boolean writeFile(String filePath, List<String> contentList) {
	        return writeFile(filePath, contentList, false);
	    }

	    /**
	     * write file, the bytes will be written to the begin of the file
	     * 
	     * @param filePath
	     * @param stream
	     * @return
	     * @see {@link #writeFile(String, InputStream, boolean)}
	     */
	    public static boolean writeFile(String filePath, InputStream stream) {
	        return writeFile(filePath, stream, false);
	    }

	    /**
	     * write file
	     * 
	     * @param file the file to be opened for writing.
	     * @param stream the input stream
	     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
	     * @return return true
	     * @throws RuntimeException if an error occurs while operator FileOutputStream
	     */
	    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
	        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
	    }

	    /**
	     * write file, the bytes will be written to the begin of the file
	     * 
	     * @param file
	     * @param stream
	     * @return
	     * @see {@link #writeFile(File, InputStream, boolean)}
	     */
	    public static boolean writeFile(File file, InputStream stream) {
	        return writeFile(file, stream, false);
	    }

	    /**
	     * write file
	     * 
	     * @param file the file to be opened for writing.
	     * @param stream the input stream
	     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
	     * @return return true
	     * @throws RuntimeException if an error occurs while operator FileOutputStream
	     */
	    public static boolean writeFile(File file, InputStream stream, boolean append) {
	        OutputStream o = null;
	        try {
	            makeDirs(file.getAbsolutePath());
	            o = new FileOutputStream(file, append);
	            byte data[] = new byte[1024];
	            int length = -1;
	            while ((length = stream.read(data)) != -1) {
	                o.write(data, 0, length);
	            }
	            o.flush();
	            return true;
	        } catch (FileNotFoundException e) {
	            throw new RuntimeException("FileNotFoundException occurred. ", e);
	        } catch (IOException e) {
	            throw new RuntimeException("IOException occurred. ", e);
	        } finally {
	            if (o != null) {
	                try {
	                    o.close();
	                    stream.close();
	                } catch (IOException e) {
	                    throw new RuntimeException("IOException occurred. ", e);
	                }
	            }
	        }
	    }

	    /**
	     * copy file
	     * 
	     * @param sourceFilePath
	     * @param destFilePath
	     * @return
	     * @throws RuntimeException if an error occurs while operator FileOutputStream
	     */
	    public static boolean copyFile(String sourceFilePath, String destFilePath) {
	        InputStream inputStream = null;
	        try {
	            inputStream = new FileInputStream(sourceFilePath);
	        } catch (FileNotFoundException e) {
	            throw new RuntimeException("FileNotFoundException occurred. ", e);
	        }
	        return writeFile(destFilePath, inputStream);
	    }

	    /**
	     * read file to string list, a element of list is a line
	     * 
	     * @param filePath
	     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
	     * @return if file not exist, return null, else return content of file
	     * @throws RuntimeException if an error occurs while operator BufferedReader
	     */
	    public static List<String> readFileToList(String filePath, String charsetName) {
	        File file = new File(filePath);
	        List<String> fileContent = new ArrayList<String>();
	        if (file == null || !file.isFile()) {
	            return null;
	        }

	        BufferedReader reader = null;
	        try {
	            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
	            reader = new BufferedReader(is);
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                fileContent.add(line);
	            }
	            reader.close();
	            return fileContent;
	        } catch (IOException e) {
	            throw new RuntimeException("IOException occurred. ", e);
	        } finally {
	            if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e) {
	                    throw new RuntimeException("IOException occurred. ", e);
	                }
	            }
	        }
	    }

	    /**
	     * get file name from path, not include suffix
	     * 
	     * <pre>
	     *      getFileNameWithoutExtension(null)               =   null
	     *      getFileNameWithoutExtension("")                 =   ""
	     *      getFileNameWithoutExtension("   ")              =   "   "
	     *      getFileNameWithoutExtension("abc")              =   "abc"
	     *      getFileNameWithoutExtension("a.mp3")            =   "a"
	     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
	     *      getFileNameWithoutExtension("c:\\")              =   ""
	     *      getFileNameWithoutExtension("c:\\a")             =   "a"
	     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
	     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
	     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
	     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
	     * </pre>
	     * 
	     * @param filePath
	     * @return file name from path, not include suffix
	     * @see
	     */
	    public static String getFileNameWithoutExtension(String filePath) {
	        if (StringUtils.isEmpty(filePath)) {
	            return filePath;
	        }

	        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
	        int filePosi = filePath.lastIndexOf(File.separator);
	        if (filePosi == -1) {
	            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
	        }
	        if (extenPosi == -1) {
	            return filePath.substring(filePosi + 1);
	        }
	        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
	    }

	    /**
	     * get file name from path, include suffix
	     * 
	     * <pre>
	     *      getFileName(null)               =   null
	     *      getFileName("")                 =   ""
	     *      getFileName("   ")              =   "   "
	     *      getFileName("a.mp3")            =   "a.mp3"
	     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
	     *      getFileName("abc")              =   "abc"
	     *      getFileName("c:\\")              =   ""
	     *      getFileName("c:\\a")             =   "a"
	     *      getFileName("c:\\a.b")           =   "a.b"
	     *      getFileName("c:a.txt\\a")        =   "a"
	     *      getFileName("/home/admin")      =   "admin"
	     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
	     * </pre>
	     * 
	     * @param filePath
	     * @return file name from path, include suffix
	     */
	    public static String getFileName(String filePath) {
	        if (StringUtils.isEmpty(filePath)) {
	            return filePath;
	        }

	        int filePosi = filePath.lastIndexOf(File.separator);
	        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
	    }

	    /**
	     * get folder name from path
	     * 
	     * <pre>
	     *      getFolderName(null)               =   null
	     *      getFolderName("")                 =   ""
	     *      getFolderName("   ")              =   ""
	     *      getFolderName("a.mp3")            =   ""
	     *      getFolderName("a.b.rmvb")         =   ""
	     *      getFolderName("abc")              =   ""
	     *      getFolderName("c:\\")              =   "c:"
	     *      getFolderName("c:\\a")             =   "c:"
	     *      getFolderName("c:\\a.b")           =   "c:"
	     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
	     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
	     *      getFolderName("/home/admin")      =   "/home"
	     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
	     * </pre>
	     * 
	     * @param filePath
	     * @return
	     */
	    public static String getFolderName(String filePath) {

	        if (StringUtils.isEmpty(filePath)) {
	            return filePath;
	        }

	        int filePosi = filePath.lastIndexOf(File.separator);
	        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
	    }

	    /**
	     * get suffix of file from path
	     * 
	     * <pre>
	     *      getFileExtension(null)               =   ""
	     *      getFileExtension("")                 =   ""
	     *      getFileExtension("   ")              =   "   "
	     *      getFileExtension("a.mp3")            =   "mp3"
	     *      getFileExtension("a.b.rmvb")         =   "rmvb"
	     *      getFileExtension("abc")              =   ""
	     *      getFileExtension("c:\\")              =   ""
	     *      getFileExtension("c:\\a")             =   ""
	     *      getFileExtension("c:\\a.b")           =   "b"
	     *      getFileExtension("c:a.txt\\a")        =   ""
	     *      getFileExtension("/home/admin")      =   ""
	     *      getFileExtension("/home/admin/a.txt/b")  =   ""
	     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
	     * </pre>
	     * 
	     * @param filePath
	     * @return
	     */
	    public static String getFileExtension(String filePath) {
	        if (StringUtils.isBlank(filePath)) {
	            return filePath;
	        }

	        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
	        int filePosi = filePath.lastIndexOf(File.separator);
	        if (extenPosi == -1) {
	            return "";
	        }
	        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
	    }

	    /**
	     * Creates the directory named by the trailing filename of this file, including the complete directory path required
	     * to create this directory. <br/>
	     * <br/>
	     * <ul>
	     * <strong>Attentions:</strong>
	     * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
	     * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
	     * </ul>
	     * 
	     * @param filePath
	     * @return true if the necessary directories have been created or the target directory already exists, false one of
	     *         the directories can not be created.
	     *         <ul>
	     *         <li>if {@link FileUtils#getFolderName(String)} return null, return false</li>
	     *         <li>if target directory already exists, return true</li>
	     *         <li>return {@link java.io.File#makeFolder}</li>
	     *         </ul>
	     */
	    public static boolean makeDirs(String filePath) {
	        String folderName = getFolderName(filePath);
	        if (StringUtils.isEmpty(folderName)) {
	            return false;
	        }

	        File folder = new File(folderName);
	        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
	    }

	    /**
	     * @param filePath
	     * @return
	     * @see #makeDirs(String)
	     */
	    public static boolean makeFolders(String filePath) {
	        return makeDirs(filePath);
	    }

	    /**
	     * Indicates if this file represents a file on the underlying file system.
	     * 
	     * @param filePath
	     * @return
	     */
	    public static boolean isFileExist(String filePath) {
	        if (StringUtils.isBlank(filePath)) {
	            return false;
	        }

	        File file = new File(filePath);
	        return (file.exists() && file.isFile());
	    }

	    /**
	     * Indicates if this file represents a directory on the underlying file system.
	     * 
	     * @param directoryPath
	     * @return
	     */
	    public static boolean isFolderExist(String directoryPath) {
	        if (StringUtils.isBlank(directoryPath)) {
	            return false;
	        }

	        File dire = new File(directoryPath);
	        return (dire.exists() && dire.isDirectory());
	    }

	    /**
	     * delete file or directory
	     * <ul>
	     * <li>if path is null or empty, return true</li>
	     * <li>if path not exist, return true</li>
	     * <li>if path exist, delete recursion. return true</li>
	     * <ul>
	     * 
	     * @param path
	     * @return
	     */
	    public static boolean deleteFile(String path) {
	        if (StringUtils.isBlank(path)) {
	            return true;
	        }

	        File file = new File(path);
	        if (!file.exists()) {
	            return true;
	        }
	        if (file.isFile()) {
	            return file.delete();
	        }
	        if (!file.isDirectory()) {
	            return false;
	        }
	        for (File f : file.listFiles()) {
	            if (f.isFile()) {
	                f.delete();
	            } else if (f.isDirectory()) {
	                deleteFile(f.getAbsolutePath());
	            }
	        }
	        return file.delete();
	    }

	    /**
	     * get file size
	     * <ul>
	     * <li>if path is null or empty, return -1</li>
	     * <li>if path exist and it is a file, return file size, else return -1</li>
	     * <ul>
	     * 
	     * @param path
	     * @return returns the length of this file in bytes. returns -1 if the file does not exist.
	     */
	    public static long getFileSize(String path) {
	        if (StringUtils.isBlank(path)) {
	            return -1;
	        }

	        File file = new File(path);
	        return (file.exists() && file.isFile() ? file.length() : -1);
	    }
	public static boolean appIsInstalled(Context context, String pageName) {
		  try {
		    context.getPackageManager().getPackageInfo(pageName, 0);
		    return true;
		  } catch (NameNotFoundException e) {
		    return false;
		  }
		}

	/**
	 * 
	 * 描述:将assert 或raw中的文件复制到sd卡中
	 * @param mContext
	 * @param dbfile sd卡的路径
	 * @return
	 * @throws IOException
	 */
	public static File copyRawToSd(Context mContext,String dbfile,int resouceId) throws IOException{
	
		File file=new File(dbfile);
	     if (!(file.exists())) {
	    	 file.createNewFile();
	    	 InputStream is = mContext.getResources().openRawResource(resouceId);
             FileOutputStream fos = new FileOutputStream(file);
             byte[] buffer = new byte[BUFFER_SIZE];
             int count = 0;
             while ((count = is.read(buffer)) > 0) {
                 fos.write(buffer, 0, count);
                 fos.flush();
             }
             
             fos.close();
             is.close();
         }
	     return file;
	}
	/**
	 * 
	 * 描述:将assert中的文件复制到sd卡中
	 * @param sign 表示是assert:2 或raw:1 ,
	 * @param mContext
	 * @param dbfile sd卡的路径
	 * @return
	 * @throws IOException
	 */
	public static File copyAssertToSd(Context mContext,String dbfile,String filename) throws IOException{
	
		File file=new File(dbfile);
	     if (!(file.exists())) {
	    	 file.createNewFile();
	    	 InputStream is=
	    		 mContext.getAssets().open(filename);
             FileOutputStream fos = new FileOutputStream(file);
             byte[] buffer = new byte[BUFFER_SIZE];
             int count = 0;
             while ((count = is.read(buffer)) > 0) {
                 fos.write(buffer, 0, count);
                 fos.flush();
             }
             
             fos.close();
             is.close();
         }
	     return file;
	}

	public static String getAsString(File file) {
		if (!file.exists())
			return null;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(file));
			String readString = "";
			String currentLine;
			while ((currentLine = in.readLine()) != null) {
				readString += currentLine;
			}
			return readString;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	

	/**
	 * <p>将文件转成base64 字符串</p>
	 * @param path 文件路径
	 * @return
	 * @throws Exception
	 */
	public static String encodeBase64File(String path) throws Exception {
		File  file = new File(path);
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int)file.length()];
		inputFile.read(buffer);
        inputFile.close();
        return   Base64.encode(buffer);
	}
	/**
	 * <p>将base64字符解码保存文件</p>
	 * @param base64Code
	 * @param targetPath
	 * @throws Exception
	 */
	public static void decoderBase64File(String base64Code,String targetPath) throws Exception {
		byte[] buffer =Base64.decode(base64Code);
		FileOutputStream out = new FileOutputStream(targetPath);
		out.write(buffer);
		out.close();
	}
	/**
	 * <p>将base64字符保存文本文件</p>
	 * @param base64Code
	 * @param targetPath
	 * @throws Exception
	 */
	public static void toFile(String base64Code,String targetPath) throws Exception {
		byte[] buffer = base64Code.getBytes();
		FileOutputStream out = new FileOutputStream(targetPath);
		out.write(buffer);
		out.close();
	}
}
