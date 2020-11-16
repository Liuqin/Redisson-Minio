package org.qin.algorithm;

import lombok.var;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: Coordinates
 * @decription: 坐标计算
 * @author: liuqin
 * @date: 2020/8/13 10:14
 */
public class Coord {

    //km 地球半径 平均值，千米
    static double EARTH_RADIUS = 6371.0;


    public static double HaverSin(double theta) {
        var v = Math.sin(theta / 2);
        return v * v;
    }


    public static double Distance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = ConvertDegreesToRadians(lat1);
        lon1 = ConvertDegreesToRadians(lon1);
        lat2 = ConvertDegreesToRadians(lat2);
        lon2 = ConvertDegreesToRadians(lon2);
        //差值
        var vLon = Math.abs(lon1 - lon2);
        var vLat = Math.abs(lat1 - lat2);
        //h is the great circle distance in radians, great circle就是一个球体上的切面，它的圆心即是球心的一个周长最大的圆。
        var h = HaverSin(vLat) + Math.cos(lat1) * Math.cos(lat2) * HaverSin(vLon);
        var distance = 2 * EARTH_RADIUS * Math.asin(Math.sqrt(h));
        return distance;
    }


    // 将角度转换为弧度
    private static double ConvertDegreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }


    // 将弧度转换为角度
    public static double ConvertRadiansToDegrees(double radian) {
        return radian * 180.0 / Math.PI;
    }


    /**
     * @param lat1     中心坐标经度
     * @param lon1     中心坐标纬度
     * @param distaceM 到中心的距离(正方形内切圆的半径）单位*米
     * @return 左上 右上 左下 右下 4个GPS坐标点
     */
    public static List<Double> get4limit(double lat1, double lon1, double distaceM) {
        //换成千米
        double distace = distaceM / 1000;
        List<Double> list = new ArrayList<Double>();
        double dx = 2 * Math.asin(Math.sin(distace / (2 * 6371)) / Math.cos(ConvertDegreesToRadians(lon1)));
        dx = ConvertRadiansToDegrees(dx);
        double dy = distace / 6371;
        dy = ConvertRadiansToDegrees(dy);
        double min_lat = lat1 - dx;
        double max_lat = lat1 + dx;
        double min_lon = lon1 - dy;
        double max_lon = lon1 + dy;
        list.add(Double.valueOf(String.format("%.8f", min_lat)));
        list.add(Double.valueOf(String.format("%.8f", max_lat)));
        list.add(Double.valueOf(String.format("%.8f", min_lon)));
        list.add(Double.valueOf(String.format("%.8f", max_lon)));
        return list;
    }

}
