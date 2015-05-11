package com.huami.employeemanager.thread;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import com.huami.employeemanager.base.BaseApplication;
import com.huami.employeemanager.net.URL;
import com.huami.employeemanager.ui.DownLoadDialog;
import android.os.Handler;
import android.os.Message;

public class DownLoadThread extends Thread {
	private Socket socket;

	private String filePath;

	private String fileName;
	
	//�ļ�����
	private long fileLength;
	
	public final static int BUFFER = 8 * 1024;
	
	private Handler handler;

	private boolean start = true;
	
	private BaseApplication application;
	
	public DownLoadThread(Handler handler, long fileLength, String filePath,
							String fileName, BaseApplication application){
		this.handler = handler;
		this.fileLength = fileLength;
		this.filePath = filePath;
		this.fileName = fileName;
		this.application = application;
	}
	
	@Override
	public void run() {
		downLoad();
	}
	
	// �ӷ����������ļ�
	private void downLoad() {
		try {

			String localFilePath = filePath + fileName;
			
			File file = new File(localFilePath);
			
			// ����һ���µ�Ŀ¼
			if(new File(filePath).exists()){
				new File(filePath).mkdirs();
			}
			
			if(file.exists()){
				file.delete();
			}	
			
			
			String tempFilePath = filePath + fileName.substring(0, fileName.indexOf(".")) + ".tmp";

			// ��ʱ�ļ�
			File tempFile = new File(tempFilePath);
			
			socket = new Socket(application.getIpAdress(), URL.PC_DOWNLOAD_PORT);
			
			if (fileLength > 0) {

				// ���浽����
				receiveFile(file, tempFile);
				
			} else {
				handler.sendEmptyMessage(DownLoadDialog.DOWNLOAD_FAILL);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ���ձ����ļ�
	 * 
	 * @param message
	 * 
	 * @param localFile
	 *            �ļ�
	 * @param tempFile
	 *            ��ʱ�ļ�
	 * @param fileLength
	 *            �ļ�����
	 * @param message
	 */
	private void receiveFile(File localFile, File tempFile)
			throws IOException {

		// ��ȡsocket����������װ��bufferedInputStream
		BufferedInputStream in = new BufferedInputStream(
				socket.getInputStream());

		// ��ȡ���ع�������ʱ�ļ���
		FileOutputStream out = new FileOutputStream(tempFile);

		byte[] buf = new byte[BUFFER];

		// ÿ�����صĳ���
		int len;

		// �ۼ����صĳ���
		int count = 0;
		

		while (start && ((len = in.read(buf)) >= 0)) {

			out.write(buf, 0, len);

			out.flush();

			count += len;

			Message message = new Message();

			message.what = DownLoadDialog.DOWNLOAD_UPDATE;

			// ��ǰ����ֵ
			int progress = (int) (((float) count / fileLength) * 100);

			// ���������ļ���С
			String currentSize = DownLoadThread.formatFileSize(count);

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("progress", progress);

			map.put("currentSize", currentSize);

			map.put("fileSize", DownLoadThread.formatFileSize(fileLength));

			message.obj = map;

			handler.sendMessage(message);
			
			if (count == fileLength) break;
		}
		
		out.close();

		in.close();

		// ����������
		if (count == fileLength) {
			// ��ʱ�ļ�������
			if (tempFile.renameTo(localFile)) {

				Message message = new Message();

				message.what = DownLoadDialog.DOWNLOAD_SUCCESS;

				message.obj = localFile.getAbsolutePath();

				handler.sendMessage(message);
				
			}
		}
		
		// �����ͣ���� ɾ�����е���ʱ�ļ�
		if (!start) {
			if (tempFile.exists()) {
				tempFile.delete();
			}
		}
	}
		
		/**
		 * ת���ļ���С
		 * 
		 * @param fileS
		 * @return B/KB/MB/GB
		 */	
		public static String formatFileSize(long fileS) {

			if (fileS == 0) {
				return "0.00B";
			}

			DecimalFormat dFormat = new DecimalFormat("#.00");

			String fileSizeString = "";

			if (fileS < 1024) {
				fileSizeString = dFormat.format((double) fileS) + "B";
			} else if (fileS < 1048576) {
				fileSizeString = dFormat.format((double) fileS / 1024) + "KB";
			} else if (fileS < 1073741824) {
				fileSizeString = dFormat.format((double) fileS / 1048576) + "MB";
			} else {
				fileSizeString = dFormat.format((double) fileS / 1073741824) + "GB";
			}
			return fileSizeString;
		}
		
		public boolean isStart() {
			return start;
		}

		public void setStart(boolean start) {
			this.start = start;
		}
}
