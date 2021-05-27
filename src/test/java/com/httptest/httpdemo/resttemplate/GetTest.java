package com.httptest.httpdemo.resttemplate;

import com.httptest.httpdemo.model.Coffee;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class GetTest {

    //@Autowired
    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void getAll() {
        String result = restTemplate.getForObject("http://localhost:8000/coffee", String.class);
        System.out.println(result);
    }

    @Test
    public void getByRequestParam() {
        Coffee result = restTemplate.getForObject("http://localhost:8000/coffee?name=mocha", Coffee.class);
        System.out.println(result);
    }

    @Test
    public void getByRequestParam1() {
        Coffee result = restTemplate.getForObject("http://localhost:8000/coffee?name={name}", Coffee.class, "mocha");
        System.out.println(result);
    }

    @Test
    public void getByRequestParam2() {
        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8000/coffee?name={name}").build("mocha");
        Coffee result = restTemplate.getForObject(uri, Coffee.class);
        System.out.println(result);
    }

    @Test
    public void getByRequestParam3() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "moca");
        Coffee result = restTemplate.getForObject("http://localhost:8000/coffee?name={name}", Coffee.class, map);
        System.out.println(result);
    }

    /**
     * name 接收不正常,接收成数组了
     */
    @Test
    public void getByRequestParam4() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("name", "moca");
        Coffee result = restTemplate.getForObject("http://localhost:8000/coffee?name={name}", Coffee.class, map);
        System.out.println(result);//输出 Coffee(id=1, name=[moca], price=20)
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------

    @Test
    public void getByPathVariable() {
        Coffee result = restTemplate.getForObject("http://localhost:8000/coffee/2", Coffee.class);
        System.out.println(result);
    }

    @Test
    public void getByPathVariable1() {
        Coffee result = restTemplate.getForObject("http://localhost:8000/coffee/{id}", Coffee.class, "4");
        System.out.println(result);
    }

    /**
     * {id}、{idabc} 都可以,对大括号内的字符没有要求,但是必填
     */
    @Test
    public void getByPathVariable2() {
        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8000/coffee/{id}").build("3");
        Coffee result = restTemplate.getForObject(uri, Coffee.class);
        System.out.println(result);
    }

    @Test
    public void getByPathVariable3() {
        Map<String, String> map = new HashMap<>();
        map.put("id", "5");
        Coffee result = restTemplate.getForObject("http://localhost:8000/coffee/{id}", Coffee.class, map);
        System.out.println(result);
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------

    @Test
    public void getByRequestObject() {
        Coffee result = restTemplate.getForObject("http://localhost:8000/coffee/find?id=1&name=mocha&price=100", Coffee.class);
        System.out.println(result);
    }

    @Test
    public void getByRequestObject2() {
        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8000/coffee/find?id={id}&name={name}&price={price}").build("2", "mocha", "50");
        Coffee result = restTemplate.getForObject(uri, Coffee.class);
        System.out.println(result);
    }

    /**
     * 接收不到参数
     */
    @Test
    public void getByRequestObject3() {
        Map<String, String> map = new HashMap<>();
        map.put("id", "5");
        map.put("name", "mmo");
        map.put("price", "33");
        Coffee result = restTemplate.getForObject("http://localhost:8000/coffee/find", Coffee.class, map);
        System.out.println(result);//输出 Coffee(id=null, name=null, price=null)
    }

    /**
     * 接收不到参数
     */
    @Test
    public void getByRequestObject4() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id", "12");
        map.add("name", "moca");
        map.add("price", "90");
        Coffee result = restTemplate.getForObject("http://localhost:8000/coffee/find", Coffee.class, map);
        System.out.println(result);//输出 Coffee(id=null, name=null, price=null)
    }
}
