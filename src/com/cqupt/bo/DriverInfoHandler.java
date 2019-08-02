package com.cqupt.bo;

import com.cqupt.dao.DriverInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.channels.SocketChannel;

@Service("driverInfoHandler")
public class DriverInfoHandler {


    private DriverInfoDao driverInfoDao;

    public DriverInfoDao getDriverInfoDao() {
        return driverInfoDao;
    }

    @Autowired
    public void setDriverInfoDao(DriverInfoDao driverInfoDao) {
        this.driverInfoDao = driverInfoDao;
    }

    /**
     * 接收用户注册信息（有返回值）
     * @param recieveStr 接收信息
     * @param client Socket客户端
     */
    @SuppressWarnings({ "unchecked" })
    public void register(String recieveStr, SocketChannel client) {

    }

    /**
     * 接收用户登录请求（有返回值）
     * @param recieveStr 接收信息
     * @param client Socket客户端
     */
    @SuppressWarnings({ "unchecked" })
    public void login(String recieveStr, SocketChannel client) {

    }
}
