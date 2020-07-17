//package org.qin.base.util;
//
//
//import com.esotericsoftware.kryo.Kryo;
//import com.esotericsoftware.kryo.io.Input;
//import com.esotericsoftware.kryo.io.Output;
//
//import java.io.*;
//
//
///**
// * @title: GsonUtil
// * @decription:
// * @author: liuqin
// * @date: 2020/6/19 10:39
// */
//public class KryoUtil {
//    /**
//     * @param filepath
//     * @param obj      需序列化的 JAVA 基础对象，List Map
//     */
//    public static void writeKryo(String filepath, String fileName, Object obj) {
//        OutputStream os = null;
//        Output output = null;
//        try {
//            File file = new File(filepath);
//            if (!file.exists()) {
//                FileUtil.createDier(filepath);
//            }
//            file = new File(filepath + fileName);
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//            os = new FileOutputStream(file);
//            output = new Output(os);// kryo序列化输出流
//            KryoSingleton.getInstance().writeClassAndObject(output, obj);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            close(output, os);
//        }
//    }
//
//    public static <T> T readKryo(String filepath) {
//        InputStream is = null;
//        Input input = null;
//        T o = null;
//        try {
//            is = new FileInputStream(filepath);
//            input = new Input(is, 1024 * 2);
//            o = (T) KryoSingleton.getInstance().readClassAndObject(input);
//        } catch (IOException e) {
//            throw new IllegalAccessError(e.getMessage());
//        } finally {
//            close(is, input);
//        }
//        return o;
//    }
//
//    /**
//     * 关闭Closeable
//     *
//     * @param iss
//     */
//    public static void close(Closeable... iss) {
//        for (Closeable is : iss) {
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                }
//            }
//        }
//    }
//
//    /**
//     * 序列化
//     *
//     * @param filepath
//     * @param obj
//     * @param kryo
//     */
//    public static void writeKryo(String filepath, Object obj, Kryo kryo) {
//        OutputStream os = null;
//        Output output = null;
//        try {
//            File file = new File(filepath);
//            os = new FileOutputStream(file);
//            output = new Output(os);// kryo序列化输出流
//            kryo.writeClassAndObject(output, obj);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            close(output, os);
//        }
//    }
//
//
//}