package com.zxh.NioTest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SelectorTest {

	public static void main(String[] args) throws IOException {
		
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(9091));
		Selector selector = Selector.open();
		serverSocketChannel.configureBlocking(false);
		// 把channel注册到selector，并绑定感兴趣的事件
		SelectionKey serverSelectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		while(true) {
			// 检查是否有已准备好的通道
			int readyChannels = selector.select();
			if(readyChannels == 0) continue;
			
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			
			// 使用完需要删除SelectionKey，所以使用迭代器操作
			Iterator<SelectionKey> iterator = selectionKeys.iterator();
			while(iterator.hasNext()) {
				SelectionKey selectionKey = iterator.next();
			    if(selectionKey.isAcceptable()) {
			    	System.out.println("accept selection key");
			    	SocketChannel channel = (SocketChannel) selectionKey.channel();
			    } else if (selectionKey.isConnectable()) {
			    	System.out.println("connect selection key");
			    } else if (selectionKey.isReadable()) {
			    	System.out.println("read selection key");
			    } else if (selectionKey.isWritable()) {
			    	System.out.println("write selection key");
			    }
			    
			    iterator.remove();
			}
		}
	}
	
}
