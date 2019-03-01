package cn.orderMeal.common.kit;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import javax.imageio.ImageIO;

public class FileKit {

	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
			file.delete();
		}
	}

	/**
	 * 将输入注写到file对象中
	 * 
	 * @param ins
	 *            输入流
	 * @param file
	 *            目标file对象
	 * @throws IOException
	 */
	public static void inputStreamToFile(InputStream ins, File file) throws IOException {
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		ins.close();
	}

	/**
	 * 得到文件名不包含后缀
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameWithoutSuffix(String filePath) {
		// new 一个file是防止windows是linux下文件分隔符不一样(\\或/)
		return new File(filePath).getName().replaceAll("[.][^.]+$", "");
	}

	/**
	 * 创建目录
	 * 
	 * @param dirPath
	 */
	public static void createDir(String dirPath) {
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static File createFile(String filePath) {
		File target = new File(filePath);
		if (!target.exists()) {
			try {
				File dir = target.getParentFile();
				if (!dir.exists()) {
					dir.mkdirs();
				}
				target.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return target;
	}

	/**
	 * 文件类型转二进制
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] fileToByteArray(File file) {
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public static String readTxtFile(String filePath) {
		try {
			String encoding = "UTF-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				StringBuffer sb = new StringBuffer();
				while ((lineTxt = bufferedReader.readLine()) != null) {
					sb.append(lineTxt);
				}
				read.close();
				return sb.toString();
			} else {
				return "";
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return "";
	}

	public static boolean isImage(File file) {
		boolean flag = false;
		try {
			BufferedImage bi = ImageIO.read(file);
			if (null == bi) {
				return flag;
			}
			flag = true;
		} catch (Exception e) {
		}
		return flag;
	}
}
