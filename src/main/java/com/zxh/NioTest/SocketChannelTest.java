package com.zxh.NioTest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;

public class SocketChannelTest {
	
	public static void main(String[] args) throws IOException {
		
		SocketChannel socketChannel = SocketChannel.open();
		
		// socketChannel可以设置非阻塞模式，connect()不会阻塞，需要循环调用finishConnect()来确认连接
		socketChannel.configureBlocking(false);
		socketChannel.connect(new InetSocketAddress("http://localhost", 9091));
		while(!socketChannel.finishConnect()) {
			
		}
		
		ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
		
		//写入数据到目标服务器
		String newData = "New String to write to file..." + System.currentTimeMillis();
		writeBuffer.put(newData.getBytes());
		
		writeBuffer.flip();
		
		while(writeBuffer.hasRemaining()) {
			socketChannel.write(writeBuffer);
		}
		writeBuffer.clear();
		
		int byteReads = 0;
		ByteBuffer readBuffer = ByteBuffer.allocate(1024);
		while((byteReads = socketChannel.read(readBuffer)) != -1 ) {
			// 反转buffer，从读变为写
			readBuffer.flip();
			
			StringBuilder builder = new StringBuilder();
			while(readBuffer.hasRemaining()) {
				// 把buffer的数据写到目标中
				builder.append((char)readBuffer.get());
			}
		}
		
	}

}
