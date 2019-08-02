package com.cqupt;

import com.alibaba.fastjson.JSONObject;
import com.cqupt.bo.DriverInfoHandler;
import com.cqupt.bo.WarningInfoHandler;
import com.cqupt.util.ChannelManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 程序入口
 */
public class Dispatcher {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private static final int port = 8888;
    private static ByteBuffer readBuffer = ByteBuffer.allocate(2048);

    /**
     * The channel manager.
     */
    // 全局的socketmap，如在其他类中有需要，则需要做成单例且线程安全的。
    private ChannelManager channelManager = ChannelManager.create();

    //Beans
    private DriverInfoHandler driverInfoHandler;
    private WarningInfoHandler warningInfoHandler;

    private Dispatcher(ApplicationContext app) {
        try {
            //Socket Server部分
            selector = Selector.open(); // 创建选择器
            serverSocketChannel = ServerSocketChannel.open(); // 打开监听信道
            serverSocketChannel.socket().bind(new InetSocketAddress(port)); // 与本地端口绑定
            serverSocketChannel.configureBlocking(false); // 设置为非阻塞模式
            // 将channel注册到selector上，
            // 将选择器绑定到监听信道,只有非阻塞信道才可以注册选择器.并在注册过程中指出该信道可以进行Accept操作
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器准备就绪,等待连接......");

            //通过IOC容器加载Beans
            driverInfoHandler = (DriverInfoHandler) app.getBean("driverInfoHandler");
            warningInfoHandler = (WarningInfoHandler) app.getBean("warningInfoHandler");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ApplicationContext app = new ClassPathXmlApplicationContext("spring-applicationContext.xml");

        new Dispatcher(app).startServer();
    }

    private void startServer() {
        boolean flag = true;
        try {
            while (flag) {
                /*
                 * public abstract int select() throws IOException;
                 * 函数功能（根据源码上的注释翻译而来）：选择一些I/O操作已经准备好的管道。每个管道对应着一个key。这个方法
                 * 是一个阻塞的选择操作。当至少有一个通道被选择时才返回。当这个方法被执行时，当前线程是允许被中断的。
                 */
                selector.select();//选着一组键，相应的通道已经打开
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();// 返回此选择键器的已选择键集
                while (it.hasNext()) {
                    SelectionKey selectionKey = it.next();
                    it.remove();
                    handle(selectionKey);// 处理key
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        }
    }

    // 处理请求
    private void handle(SelectionKey selectionKey) {
        // 接受请求
        ServerSocketChannel serverSocketChannel = null;
        SocketChannel client = null;

        try {
            if (selectionKey.isAcceptable()) { // a connection was accepted by a ServerSocketChannel.
                //从SelectionKey访问Channel 返回为之创建此键的通道。
                serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                //测试此键的通道是否已准备好接受新的套接字连接。
                client = serverSocketChannel.accept();
                //接受到此通道套接字的连接。
                //此方法返回的套接字通道（如果有）将处于阻塞模式。
                client.configureBlocking(false); // 配置为非阻塞
                client.register(selector, SelectionKey.OP_READ); //将client注册到selector，等待连接
                System.out.println(client.socket().getRemoteSocketAddress() + "连入了服务器");

            } else if (selectionKey.isReadable()) {
                client = (SocketChannel) selectionKey.channel();// 返回为之创建此键的通道。
                readBuffer.clear();// 将缓冲区清空以备下次读取
                int count = client.read(readBuffer);// 读取服务器发送来的数据到缓冲区中
                String recieveStr = "";
                if (count >= 0) {
                    recieveStr = new String(readBuffer.array(), 0, count);

                    System.out.println("服务器端接受客户端数据-----" + recieveStr);

                    String fromType = "";
                    String dataType = "";
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(recieveStr);
                        fromType = jsonObject.getString("fromtype");
                        dataType = jsonObject.getString("datatype");
                    } catch (Exception e1) {
                        System.out.println("error=====error" + e1.getMessage());
                    }

                    //针对不同的Json消息，进行对应的业务处理
                    if ("hmi".equals(fromType)) {
                        System.out.println("移动终端HMI.....");
                        switch (dataType) {
                            case "register":
                                driverInfoHandler.register(recieveStr, client);
                                break;
                            case "login":
                                driverInfoHandler.login(recieveStr, client);
                                break;
                            case "warning":
                                warningInfoHandler.warning(recieveStr, client);
                                break;
                            default:
                                System.out.println("[ERROR]:HMI Send Unknown dataType");
                                break;
                        }
                    }

                } else {
                    //count == -1 时，无数据接收，断开
                    this.offline(client, "客户端连接断开");
                }
            } else if (selectionKey.isValid()) {
            }
        } catch (Exception e) {
            e.printStackTrace();
            selectionKey.cancel();
            this.offline(client, "发生异常，断开");
        }
    }

    private void offline(SocketChannel client, String msg) {
        // 修改数据库离线
        SocketAddress SocketAddressIp = client.socket()
                .getRemoteSocketAddress();
        String stringIp = SocketAddressIp.toString();
        String ip = stringIp.replace("/", "");

        // 从管道映射中删除对应项
        channelManager.removeChannel(client);
        try {
            if (client != null)
                System.out.println(ip + ":" + msg);
            client.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
