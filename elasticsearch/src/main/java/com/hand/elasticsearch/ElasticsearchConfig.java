package com.hand.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Bean(name = "restHighLevelClient")
    public RestHighLevelClient client() {

        //三个节点的地址
        HttpHost master = new HttpHost("10.211.55.29", 9200, "http");
        HttpHost slave1 = new HttpHost("10.211.55.29", 8200, "http");
        HttpHost slave2 = new HttpHost("10.211.55.29", 7200, "http");

        return new RestHighLevelClient(RestClient.builder(master, slave1, slave2));
    }
}
