package com.httptest.httpdemo.resttemplate;

import com.httptest.httpdemo.model.Coffee;
import com.httptest.httpdemo.model.CoffeeFile;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * 不含@RequestBody的接口,单个参数可以接收多个值,并以逗号分隔如 1,2,3
 *
 * Map 用于调用含@RequestBody的接口
 * MultiValueMap 用于调用不含@RequestBody的接口
 * 传对象参数调用不含@RequestBody的接口时接收不到参数
 * 传对象参数调用含@RequestBody的接口时接收得到参数
 *
 * xxx?a=x&b=x 和 MultiValueMap 传参形式可以被不含@RequestBody的接口和单个参数形式接口(String a, int b)接收
 *
 * 含有 @RequestBody 的接口不接收url ?xx=xx 类型的参数
 *
 * @RequestBody配合MediaType.APPLICATION_JSON_VALUE使用
 * 非@RequestBody不配合MediaType.APPLICATION_JSON_VALUE使用
 *
 * 报415时添加设置包含content-type的HttpHeaders
 */
@SpringBootTest
public class PostTest {

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void postByRequestObject() {
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee?id=1&name=mocha&price=100", null, Coffee.class);
        System.out.println(result);
    }

    @Test
    public void postByRequestObject1() {
        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8000/coffee?id={id}&name={name}&price={price}").build("2", "mocha", "50");
        Coffee result = restTemplate.postForObject(uri, null, Coffee.class);
        System.out.println(result);
    }

    /**
     * 接收不到参数
     */
    @Test
    public void postByRequestObject2() {
        Coffee request = Coffee.builder().id("1").name("mmoc").price(new BigDecimal(100)).build();
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee", request, Coffee.class);
        System.out.println(result);//输出 Coffee(id=null, name=null, price=null)
    }

    /**
     * 接收不到参数
     */
    @Test
    public void postByRequestObject3() {
        Coffee coffee = Coffee.builder().id("1").name("mmoc").price(new BigDecimal(100)).build();
        HttpEntity<Coffee> request = new HttpEntity<>(coffee);
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee", request, Coffee.class);
        System.out.println(result);//输出 Coffee(id=null, name=null, price=null)
    }

    /**
     * 接收不到参数
     */
    @Test
    public void postByRequestObject4() {
        String jsonString = "{\"id\":\"1\",\"name\":\"mmoc\",\"price\":\"100\"}";
        HttpEntity<String> request = new HttpEntity<>(jsonString);
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee", request, Coffee.class);
        System.out.println(result);//输出 Coffee(id=null, name=null, price=null)
    }

    /**
     * 接收不到参数
     */
    @Test
    public void postByRequestObject22() {
        Map<String, String> map = new HashMap<>();
        map.put("id", "5");
        map.put("name", "mmo");
        map.put("price", "33");
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee", null, Coffee.class, map);
        System.out.println(result);//输出 Coffee(id=null, name=null, price=null)
    }

    /**
     * 接收不到参数
     */
    @Test
    public void postByRequestObject23() {
        Map<String, String> map = new HashMap<>();
        map.put("id", "5");
        map.put("name", "mmo");
        map.put("price", "33");
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee", map, Coffee.class);
        System.out.println(result);//输出 Coffee(id=null, name=null, price=null)
    }

    /**
     * 接收不到参数
     */
    @Test
    public void postByRequestObject32() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id", "12");
        //map.add("name", "moca");
        map.add("price", "90");
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee", null, Coffee.class, map);
        System.out.println(result);//输出 Coffee(id=null, name=null, price=null)
    }

    @Test
    public void postByRequestObject33() {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("id", "12");
        request.add("price", "90");
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee", request, Coffee.class);
        System.out.println(result);//输出 Coffee(id=12, name=null, price=90)
    }

    @Test
    public void postByRequestObject331() {
        HttpHeaders headers = new HttpHeaders();

        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("id", "12");
        request.add("price", "90");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(request, headers);
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee", requestEntity, Coffee.class);
        System.out.println(result);//输出 Coffee(id=12, name=null, price=90)
    }

    @Test
    public void postByRequestObject332() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic YWNzaWduX2FwaToxMjM0NTY=");

        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("apiKey", "84e0a5e69320d27c54a5989b13993479");
        request.add("apiSecret", "N2YyYzNl0WViYzU2Yzg0NDYyMDEzMzA0ZTdk0TE20GNk0TkzMTliYQ");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(request, headers);
        String result = restTemplate.postForObject("http://202.103.219.149:51990/auth/auth/api", requestEntity, String.class);
        System.out.println(result);//输出 Coffee(id=12, name=null, price=90)
    }

    /**
     * params 没有起作用
     */
    @Test
    public void postByRequestObject34() {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("id", "12");
        request.add("price", "90");

        Map<String, String> params = new HashMap<>();
        params.put("name", "mmo");

        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee", request, Coffee.class, params);
        System.out.println(result);//输出 Coffee(id=12, name=null, price=90)
    }

    /**
     * url拼接参数后 params 起作用
     */
    @Test
    public void postByRequestObject35() {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("id", "12");
        request.add("price", "90");

        Map<String, String> params = new HashMap<>();
        params.put("name", "mmo");

        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee" + "?name={name}", request, Coffee.class, params);
        System.out.println(result);//输出 Coffee(id=1, name=mmo, price=90)
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 报错: "status":415,"error":"Unsupported Media Type"
     */
    @Test
    public void postByRequestBody() {
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/body?id=1&name=mocha&price=100", null, Coffee.class);
        System.out.println(result);
    }

    /**
     * 报错: "status":415,"error":"Unsupported Media Type"
     */
    @Test
    public void postByRequestBody1() {
        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8000/coffee/body?id={id}&name={name}&price={price}").build("2", "mocha", "50");
        Coffee result = restTemplate.postForObject(uri, null, Coffee.class);
        System.out.println(result);
    }

    /**
     *
     */
    @Test
    public void postByRequestBody2() {
        Coffee request = Coffee.builder().id("1").name("mmoc").price(new BigDecimal(100)).build();
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/body", request, Coffee.class);
        System.out.println(result);//输出 Coffee(id=1, name=mmoc, price=100)
    }

    /**
     *
     */
    @Test
    public void postByRequestBody3() {
        Coffee coffee = Coffee.builder().id("1").name("mmoc").price(new BigDecimal(100)).build();
        HttpEntity<Coffee> request = new HttpEntity<>(coffee);
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/body", request, Coffee.class);
        System.out.println(result);
    }

    /**
     * 报错: "status":415,"error":"Unsupported Media Type"
     */
    @Test
    public void postByRequestBody4() {
        String jsonString = "{\"id\":\"1\",\"name\":\"mmoc\",\"price\":\"100\"}";
        HttpEntity<String> request = new HttpEntity<>(jsonString);
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/body", request, Coffee.class);
        System.out.println(result);
    }

    @Test
    public void postByRequestBody41() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(type);
        String jsonString = "{\"id\":\"2\",\"name\":\"mmoc\",\"price\":\"100\"}";
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/body", request, Coffee.class);
        System.out.println(result);
    }

    /**
     * 报错: "status":415,"error":"Unsupported Media Type"
     */
    @Test
    public void postByRequestBody22() {
        Map<String, String> map = new HashMap<>();
        map.put("id", "5");
        map.put("name", "mmo");
        map.put("price", "33");
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/body", null, Coffee.class, map);
        System.out.println(result);
    }

    /**
     *
     */
    @Test
    public void postByRequestBody23() {
        Map<String, String> map = new HashMap<>();
        map.put("id", "5");
        map.put("name", "mmo");
        map.put("price", "33");
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/body", map, Coffee.class);
        System.out.println(result);
    }

    @Test
    public void postByRequestBody24() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(type);
        Map<String, String> request = new HashMap<>();
        request.put("id", "5");
        request.put("name", "mmo");
        request.put("price", "33");
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(request, headers);
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/body?id=1&name=mocha&price=100", requestEntity, Coffee.class);
        System.out.println(result);//输出 Coffee(id=5, name=mmo, price=33
    }

    /**
     * 报错: "status":415,"error":"Unsupported Media Type"
     */
    @Test
    public void postByRequestBody32() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id", "12");
        map.add("name", "moca");
        map.add("price", "90");
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/body", null, Coffee.class, map);
        System.out.println(result);
    }

    /**
     * 报错: "status":415,"error":"Unsupported Media Type"
     */
    @Test
    public void postByRequestBody33() {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("id", "12");
        request.add("price", "90");
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/body", request, Coffee.class);
        System.out.println(result);
    }

    /**
     * 报错: "status":400,"error":"Bad Request"
     */
    @Test
    public void postByRequestBody34() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(type);
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("id", "12");
        request.add("price", "90");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(request, headers);
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/body", requestEntity, Coffee.class);
        System.out.println(result);
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 报错: "status":415,"error":"Unsupported Media Type"
     */
    @Test
    public void postByFormdata() {
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/form-data?id=1&name=mocha&price=100", null, Coffee.class);
        System.out.println(result);
    }

    /**
     * 报错: No HttpMessageConverter for java.util.HashMap and content type "multipart/form-data"
     */
    @Test
    public void postByFormdata1() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.MULTIPART_FORM_DATA_VALUE);//对应consumes指定的类型才正确
        headers.setContentType(type);
        Map<String, String> request = new HashMap<>();
        request.put("id", "12");
        request.put("name", "natie");
        request.put("price", "90");
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(request, headers);
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/form-data?id=1&name=mocha&price=100", requestEntity, Coffee.class);
        System.out.println(result);
    }

    @Test
    public void postByFormdata2() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.MULTIPART_FORM_DATA_VALUE);//对应consumes指定的类型才正确
        headers.setContentType(type);
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("id", "12");
        request.add("name", "natie");
        request.add("price", "90");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(request, headers);
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/form-data?id=1&name=mocha&price=100", requestEntity, Coffee.class);
        System.out.println(result);//输出 Coffee(id=12,1, name=natie,mocha, price=90)
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     *
     */
    @Test
    public void postByFormUrlencoded() {
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/form-data-urlencoded?id=1&name=mocha&price=100", null, Coffee.class);
        System.out.println(result);
    }

    /**
     * 报错: No HttpMessageConverter for java.util.HashMap and content type "application/x-www-form-urlencoded"
     */
    @Test
    public void postByFormUrlencoded1() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);//对应consumes指定的类型才正确
        headers.setContentType(type);
        Map<String, String> request = new HashMap<>();
        request.put("id", "12");
        request.put("name", "natie");
        request.put("price", "90");
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(request, headers);
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/form-data-urlencoded?id=1&name=mocha&price=100", requestEntity, Coffee.class);
        System.out.println(result);
    }

    @Test
    public void postByFormUrlencoded2() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);//对应consumes指定的类型才正确
        headers.setContentType(type);
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("id", "12");
        request.add("name", "natie");
        request.add("price", "90");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(request, headers);
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/form-data-urlencoded?id=1&name=mocha&price=100", requestEntity, Coffee.class);
        System.out.println(result);//输出 Coffee(id=1,12, name=mocha,natie, price=100)
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 报错: "status":415,"error":"Unsupported Media Type"
     */
    @Test
    public void postByJson() {
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/json?id=1&name=mocha&price=100", null, Coffee.class);
        System.out.println(result);
    }

    /**
     * 非@RequestBody不配合MediaType.APPLICATION_JSON_VALUE使用
     */
    @Test
    public void postByJson1() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE);//对应consumes指定的类型才正确
        headers.setContentType(type);
        Map<String, String> request = new HashMap<>();
        request.put("id", "12");
        request.put("name", "natie");
        request.put("price", "90");
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(request, headers);
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/json?id=1&name=mocha&price=100", requestEntity, Coffee.class);
        System.out.println(result);//输出 Coffee(id=1, name=mocha, price=100)
    }

    /**
     * 非@RequestBody不配合MediaType.APPLICATION_JSON_VALUE使用
     */
    @Test
    public void postByJson2() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE);//对应consumes指定的类型才正确
        headers.setContentType(type);
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("id", "12");
        request.add("name", "natie");
        request.add("price", "90");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(request, headers);
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/json?id=1&name=mocha&price=100", requestEntity, Coffee.class);
        System.out.println(result);//输出 Coffee(id=1, name=mocha, price=100)
    }

    /**
     * 非@RequestBody不配合MediaType.APPLICATION_JSON_VALUE使用
     */
    @Test
    public void postByJson3() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE);//对应consumes指定的类型才正确
        headers.setContentType(type);
        String jsonString = "{\"id\":\"2\",\"name\":\"mmoc\",\"price\":\"200\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/json?id=1&name=mocha&price=100", requestEntity, Coffee.class);
        System.out.println(result);//输出 Coffee(id=1, name=mocha, price=100)
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 报错: "status":415,"error":"Unsupported Media Type"
     */
    @Test
    public void postByBodyJson() {
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/body-json?id=1&name=mocha&price=100", null, Coffee.class);
        System.out.println(result);
    }

    /**
     * @RequestBody配合MediaType.APPLICATION_JSON_VALUE使用
     */
    @Test
    public void postByBodyJson1() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE);//对应consumes指定的类型才正确
        headers.setContentType(type);
        Map<String, String> request = new HashMap<>();
        request.put("id", "12");
        request.put("name", "natie");
        request.put("price", "90");
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(request, headers);
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/body-json?id=1&name=mocha&price=100", requestEntity, Coffee.class);
        System.out.println(result);//输出 Coffee(id=12, name=natie, price=90)
    }

    /**
     * 报错: "status":400,"error":"Bad Request"
     */
    @Test
    public void postByBodyJson2() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE);//对应consumes指定的类型才正确
        headers.setContentType(type);
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("id", "12");
        request.add("name", "natie");
        request.add("price", "90");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(request, headers);
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/body-json?id=1&name=mocha&price=100", requestEntity, Coffee.class);
        System.out.println(result);
    }

    /**
     * @RequestBody配合MediaType.APPLICATION_JSON_VALUE使用
     */
    @Test
    public void postByBodyJson3() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE);//对应consumes指定的类型才正确
        headers.setContentType(type);
        String jsonString = "{\"id\":\"2\",\"name\":\"mmoc\",\"price\":\"200\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);
        Coffee result = restTemplate.postForObject("http://localhost:8000/coffee/body-json?id=1&name=mocha&price=100", requestEntity, Coffee.class);
        System.out.println(result);//输出 Coffee(id=2, name=mmoc, price=200)
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 接收不到参数
     */
    @Test
    public void postFile1(){
        FileSystemResource resource = new FileSystemResource(new File("D:\\file.txt"));
        String result = restTemplate.postForObject("http://localhost:8000/coffee/upload", resource, String.class);
        System.out.println(result);
    }

    @Test
    public void postFile11(){
        FileSystemResource resource = new FileSystemResource(new File("D:\\file.txt"));
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("file", resource);
        String result = restTemplate.postForObject("http://localhost:8000/coffee/upload", param, String.class);
        System.out.println(result);//输出 file.txt
    }

    /**
     * 报错: No serializer found for class sun.nio.ch.ChannelInputStream and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: java.util.HashMap["file"]->org.springframework.core.io.FileSystemResource["inputStream"])
     */
    @Test
    public void postFile12(){
        FileSystemResource resource = new FileSystemResource(new File("D:\\file.txt"));
        Map<String, Object> param = new HashMap<>();
        param.put("file", resource);
        String result = restTemplate.postForObject("http://localhost:8000/coffee/upload", param, String.class);
        System.out.println(result);
    }

    @Test
    public void postFile2(){
        FileSystemResource resource = new FileSystemResource(new File("D:\\file.txt"));
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("file", resource);
        String result = restTemplate.postForObject("http://localhost:8000/coffee/upload1", param, String.class);
        System.out.println(result);//输出 file.txt
    }

    @Test
    public void postFile31(){
        FileSystemResource resource = new FileSystemResource(new File("D:\\file.txt"));
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("id", "1");
        param.add("name", "maqino");
        param.add("price", 100);
        param.add("file", resource);
        CoffeeFile result = restTemplate.postForObject("http://localhost:8000/coffee/upload3", param, CoffeeFile.class);
        System.out.println(result);
    }

    /**
     * 接收不到参数
     */
    @Test
    public void postFile32(){
        FileSystemResource resource = new FileSystemResource(new File("D:\\file.txt"));
        Coffee coffee = Coffee.builder().id("1").name("mmoc").price(new BigDecimal(100)).build();
        CoffeeFile result = restTemplate.postForObject("http://localhost:8000/coffee/upload3", coffee, CoffeeFile.class);
        System.out.println(result);
    }
}
