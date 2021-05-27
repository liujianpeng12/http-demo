package com.httptest.httpdemo.resttemplate;

import com.httptest.httpdemo.model.Coffee;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootTest
public class ExchangeTest {

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void getAll2() {
        //返回数据是数组
        ParameterizedTypeReference<List<Coffee>> ptr = new ParameterizedTypeReference<List<Coffee>>() {};
        ResponseEntity<List<Coffee>> result = restTemplate.exchange("http://localhost:8000/coffee", HttpMethod.GET, null, ptr);
        System.out.println(result.getBody());
    }
}
