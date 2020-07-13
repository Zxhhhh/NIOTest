package com.zxh.NioTest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerSocketChannelTest {
	
	public static void main(String[] args) throws IOException {
		
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.socket().bind(new InetSocketAddress(9091));
		 
		while(true){
		    SocketChannel socketChannel = serverSocketChannel.accept();
		    if(socketChannel != null) {
		    	System.out.println("连接来了");
		    	ByteBuffer readBuffer = ByteBuffer.allocate(48);
		    	while(socketChannel.read(readBuffer) != -1) {
		    		readBuffer.flip();
		    		StringBuilder builder = new StringBuilder();
		    		while(readBuffer.hasRemaining()) {
		    			builder.append((char)readBuffer.get());
		    		}
		    		System.out.println(builder.toString());
		    		
		    		readBuffer.clear();
		    	}
		    }
		}

		
	}

}
