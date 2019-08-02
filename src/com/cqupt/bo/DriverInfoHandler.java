package com.cqupt.bo;

import com.alibaba.fastjson.JSONObject;
import com.cqupt.dao.DriverInfoDao;
import com.cqupt.entity.DriverInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.nio.channels.SocketChannel;

@Service("driverInfoHandler")
public class DriverInfoHandler {

    @Autowired
    private DriverInfoDao driverInfoDao;

    public DriverInfoDao getDriverInfoDao() {
        return driverInfoDao;
    }

    public void setDriverInfoDao(DriverInfoDao driverInfoDao) {
        this.driverInfoDao = driverInfoDao;
    }

    /**
     * 接收用户注册信息（有返回值）
     *
     * @param recieveStr 接收信息
     * @param client     Socket客户端
     */
    @SuppressWarnings({"unchecked"})
    public String register(String recieveStr, SocketChannel client) {
        //回复信息
        JSONObject resp = new JSONObject();
        resp.put("fromtype", "cloud");
        resp.put("datatype", "registerResponse");
        //判断是否已经注册过
        DriverInfo driverInfo = JSONObject.parseObject(recieveStr, DriverInfo.class);
        System.out.println("driverID=" + driverInfo.getId());
        Serializable id = driverInfo.getId();           //序列化ID
        if (driverInfoDao.get(id) == null) {                 //在数据库中查找id对应车辆
            driverInfoDao.save(driverInfo);
            resp.put("isSuccess", true);
            resp.put("content", "车辆注册成功");
        } else {
            resp.put("isSuccess", false);
            resp.put("content", "该id已被注册，请更换id再注册");
        }
        return resp.toString();
    }

    /**
     * 接收用户登录请求（有返回值）
     *
     * @param recieveStr 接收信息
     * @param client     Socket客户端
     */
    @SuppressWarnings({"unchecked"})
    public String login(String recieveStr, SocketChannel client) {
        JSONObject resp = new JSONObject();

        DriverInfo driverInfo = JSONObject.parseObject(recieveStr, DriverInfo.class);
        Vehicle veh = new Vehicle();
        //Vehicle veh = JSONObject.parseObject(recJson, Vehicle.class);    //车载发来的消息存到veh中
        Serializable id = jsonObject.getString("veh_id");
        String veh_pwd = new String(jsonObject.getString("veh_pwd"));
        veh = driverInfoDao.get(id);
        if (veh != null) {
            boolean veh_isonline = veh.isVeh_isonline();
            if (!veh_isonline) {
                String password = veh.getVeh_pwd();
                if (veh_pwd.equals(password)) {
                    veh.setVeh_ip(clientIp);
                    veh.setVeh_isonline(true);            //把车辆状态写入veh
                    vehicleDao.update(veh);               //更新车辆信息表单
                    resp.put("isSuccesslogin", true);
                    resp.put("content", "Login success");
                } else {
                    resp.put("isSuccesslogin", false);
                    resp.put("content", "User id or password is wrong");
                }
            } else {
                resp.put("isSuccesslogin", false);
                resp.put("content", "The vehicle was logged in another device");
            }
        } else {
            resp.put("isSuccesslogin", false);
            resp.put("content", "Login failed,the vehicle is not registered");
        }
        return resp.toString();

    }
}
