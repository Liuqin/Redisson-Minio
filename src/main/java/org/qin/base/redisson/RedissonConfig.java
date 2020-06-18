package org.qin.base.redisson;

import org.redisson.Redisson;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RedissonConfig {
    @Autowired
    private RedisConfigProperties redisConfigProperties;

    /**
     * @return
     * @descripttion 添加redisson的bean
     * @parms
     * @author liuqin
     * @date 2020/6/18
     */
    @Bean
    public Redisson redisson() {
        Config config = new Config();
        //redisson版本是3.5，集群的ip前面要加上“redis://”，不然会报错，3.2版本可不加
        List<String> clusterNodes = new ArrayList<>();

        if (redisConfigProperties.getCluster() != null) {
            for (int i = 0; i < redisConfigProperties.getCluster().getNodes().size(); i++) {
                clusterNodes.add("redis://" + redisConfigProperties.getCluster().getNodes().get(i));
            }
            ClusterServersConfig clusterServersConfig = config.useClusterServers()
                    .addNodeAddress(clusterNodes.toArray(new String[clusterNodes.size()]));
            //设置密码
            clusterServersConfig.setPassword(redisConfigProperties.getPassword());
        } else {
            String url = redisConfigProperties.getHost() + ":" + redisConfigProperties.getPort();
            String password = redisConfigProperties.getPassword();
            config.useSingleServer().setAddress("redis://" + url).setPassword(password);
        }

        return (Redisson) Redisson.create(config);
    }
}