package org.qin.algorithm.algorithm;

import lombok.Data;

import java.util.*;

/**
 * @title: Fitting
 * @decription: 数据密集分布拟合算法
 * @author: liuqin
 * @date: 2020/8/7 10:15
 */

@Data
public class Fitting {

    private double[] data;


    public List<Map.Entry<Integer, Integer>> fittingResutl() {
        if (data != null && data.length > 0) {
            Arrays.sort(data);
            int move = data.length - 1;
            double[] subs = new double[move];
            for (int i = 0; i < move; i++) {
                double sub = data[i + 1] - data[i];
                subs[i] = sub;
            }
            // 计算聚集区域
            double avg = Arrays.stream(subs).average().getAsDouble();
            StringBuilder stringBuilder = new StringBuilder();
            for (int m = 0; m < subs.length; m++) {
                if (subs[m] > avg) {
                    stringBuilder.append("1");
                } else {
                    stringBuilder.append("0");
                }
            }
            System.out.println(stringBuilder.toString());
            char[] chars = stringBuilder.toString().toCharArray();
            int[] indexs = new int[chars.length];
            int indexed = 0;
            for (int n = 0; n < chars.length; n++) {
                if (chars[n] == '0') {
                    indexs[indexed] = n;
                    indexed++;
                }
            }
            HashMap<Integer, Integer> map = new HashMap<>();
            int key;
            int index = -1;
            int count = 1;
            for (int x = 1; x < indexs.length; x++) {
                boolean isContiune = indexs[x] - indexs[x - 1] == 1;
                if (isContiune) {
                    count++;
                    if (index == -1) {
                        index = x - 1;
                    }
                    if (x == indexs.length - 1) {
                        map.put(index, count);
                    }
                } else {
                    map.put(index, count);
                    index = x - 1;
                    count = 1;
                    map.put(index, count);
                }
            }
            List<Map.Entry<Integer, Integer>> list = new ArrayList<>(map.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
                //升序排序
                @Override
                public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }

            });
            return list;

        } else {
            return null;
        }

    }
}