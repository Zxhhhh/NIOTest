package com.zxh.NioTest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {
	
	public static void main(String[] args) throws IOException {
		RandomAccessFile raf = new RandomAccessFile("F:\\BaiduYunDownload\\test.txt", "rw");
		FileChannel channel = raf.getChannel();
		
		/*
		 * 	force(): 
		 *  将通道里尚未写入磁盘的数据强制写到磁盘上。
		 *  出于性能方面的考虑，操作系统会将数据缓存在内存中，
		 *  所以无法保证写入到FileChannel里的数据一定会即时写到磁盘上。
		 *  要保证这一点，需要调用force()方法
		 */
		channel.force(true);
		
		
		// 创建buffer，开辟空间
		ByteBuffer buffer = ByteBuffer.allocate(48);
		
		int byteRead = 0;
		//把源的数据通过channel读取到buffer中
		while((byteRead = channel.read(buffer)) != -1 ) {
			
			// position：获取FileChannel的当前位置
			long position = channel.position();
			System.out.println("channel position：" + position);
			
			
			// 反转buffer，从读变为写
			buffer.flip();
			
			StringBuilder builder = new StringBuilder();
			while(buffer.hasRemaining()) {
				// 把buffer的数据写到目标中
				builder.append((char)buffer.get());
			}
			System.out.println(builder.toString());
			buffer.clear();
		}
		channel.close();
		raf.close();
	}

}
