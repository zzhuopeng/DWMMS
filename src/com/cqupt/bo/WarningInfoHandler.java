package com.cqupt.bo;

import com.cqupt.dao.WarningInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.channels.SocketChannel;

@Service("warningInfoHandler")
public class WarningInfoHandler {

    private WarningInfoDao warningInfoDao;

    public WarningInfoDao getWarningInfoDao() {
        return warningInfoDao;
    }

    @Autowired
    public void setWarningInfoDao(WarningInfoDao warningInfoDao) {
        this.warningInfoDao = warningInfoDao;
    }

    /**
     * 接收保存预警消息
     * @param recieveStr 预警消息
     * @param client Socket客户端
     */
    @SuppressWarnings({ "unchecked" })
    public void warning(String recieveStr, SocketChannel client) {

    }
}
