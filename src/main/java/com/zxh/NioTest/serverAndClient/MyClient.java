package com.zxh.NioTest.serverAndClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CountDownLatch;

import javax.sound.midi.Receiver;

/**
 * 客户端
 * @author Administrator
 *
 */
public class MyClient {
	
	private int SIZE = 1024;
	private SocketChannel socketChannel;
	private ByteBuffer byteBuffer;

	public void connectServer() throws IOException {
		socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress("localhost",9091));
		// 设置为非阻塞
		socketChannel.configureBlocking(false);
		byteBuffer = ByteBuffer.allocate(SIZE);
		receive();
	}
	
	private void receive() throws IOException {
		while(true) {
			byteBuffer.clear();
			int readCount;
			while((readCount = socketChannel.read(byteBuffer)) >0) {
				byteBuffer.flip();
				while(byteBuffer.hasRemaining()) {
					System.out.println((char)byteBuffer.get());
				}
				send2Server("zxh info".getBytes());
				byteBuffer.clear(); 
			}
		}
	}
	
	/**
	 * 发送数据到服务器中(把数据写到通道)
	 * @param bytes
	 * @throws IOException
	 */
	private void send2Server(byte[] bytes) throws IOException {
		byteBuffer.clear();
		byteBuffer.put(bytes);
		byteBuffer.flip();
		socketChannel.write(byteBuffer);
	}
	
	public static void main(String[] args) {
		new MyClient().connectServer();
	}
}