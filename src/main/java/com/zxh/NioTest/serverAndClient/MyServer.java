package com.zxh.NioTest.serverAndClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class MyServer {
	
	private int SIZE = 1024;
	private ServerSocketChannel serverSocketChannel;
	private ByteBuffer byteBuffer;
	private Selector selector;
	private int remoateClientNum = 0;
	
	public MyServer(int port) {
		try {
			initChannel(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Channel 初始化
	public void initChannel(int port) throws IOException {
		// 打开服务端Channel
		serverSocketChannel = ServerSocketChannel.open();
		// 设置为非阻塞模式
		serverSocketChannel.configureBlocking(false);
		// 绑定端口
		serverSocketChannel.bind(new InetSocketAddress(port));
		System.out.println("listener on port:" + port);
		
		// 创建选择器
		selector = Selector.open();
		// 向选择器注册通道，绑定关注的事件
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		// 分配缓冲区BUffer的大小
		byteBuffer = ByteBuffer.allocate(SIZE);
	}
	
	// 监听器，监听Channel上的数据变化
	private void listener() throws Exception {
		while(true) {
			int n = selector.select();
			if(n ==0) continue;
			
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while(iterator.hasNext()) {
				SelectionKey key = iterator.next();
				if(key.isAcceptable()) {
					ServerSocketChannel server =  (ServerSocketChannel) key.channel();
					SocketChannel channel = server.accept();
					// Channel 注册
					registerChannel(selector, channel, SelectionKey.OP_READ);
					remoateClientNum++;
					System.out.println("online client num="+remoateClientNum);
					write(channel, "hello client".getBytes());
				}
				if(key.isReadable()) {
					// read(key);
				}
				iterator.remove();
			}
		}
	}
	
	private void write(SocketChannel channel,byte[] writeData)  throws IOException{
		byteBuffer.clear();
		// 把byteBuffer 从写模式改成读模式
		byteBuffer.flip();
		
		// 讲缓冲区的数据写入到通道中，如果channel是非阻塞的，则要用while(byteBuffer.hasRemaing())来重复写
		channel.write(byteBuffer);
	}
	
	// 向Selector注册通道
	private void registerChannel(Selector selector, SocketChannel channel, int opRead) throws IOException{
		if(channel == null) {
			return ;
		}
		channel.configureBlocking(false);
		channel.register(selector, opRead);
	}

}
