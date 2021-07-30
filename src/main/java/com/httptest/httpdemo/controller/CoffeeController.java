package com.httptest.httpdemo.controller;

import com.httptest.httpdemo.model.Coffee;
import com.httptest.httpdemo.model.CoffeeFile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.math.BigDecimal;
import java.util.*;


/**
 * form-data主要是以键值对的形式来上传参数，同时参数之间以&分隔符分开，同时也可以上传文件，文件上传要指定文件类型
 * x-www-form-urlencode与form-data最大的区别是，x-www-form-urlencode只能是以键值对的形式传参，不能上传文件
 */
@RestController
@RequestMapping("/coffee")
public class CoffeeController {

    @GetMapping("")
    public List<Coffee> getAll() {
        return Arrays.asList(Coffee.builder().name("natie").price(new BigDecimal(10)).build(),
                Coffee.builder().name("kaboqino").price(new BigDecimal(11)).build());
    }

    @GetMapping(value = "", params = "name")
    public Coffee getByName(@RequestParam String name) {
        return Coffee.builder().id("1").name(name).price(new BigDecimal(20)).build();
    }

    @GetMapping("/{id}")
    public Coffee getById(@PathVariable String id) {
        return Coffee.builder().id(id).name("natie").price(new BigDecimal(20)).build();
    }

    /**
     * @GetMapping不能与@RequestBody一起使用
     * params + form-data
     * params + form-urlencoded 不行
     * @param coffee
     * @return
     */
    @GetMapping("/find")
    public Coffee find(Coffee coffee) {
        return coffee;
    }

    @GetMapping("/findById")
    public Coffee findById(String id) {
        return Coffee.builder().id(id).name("abc").price(new BigDecimal(100)).build();
    }

    /**
     * 若传参数包函name则调用
     * params + (form-data / form-urlencoded)
     * @param name
     * @param price
     * @return
     */
    @PostMapping(value = "", params = "name")
    public Coffee add(String name, BigDecimal price) {
        return Coffee.builder().id("1").name(name).price(price).build();
    }

    /**
     * params + (form-data / form-urlencoded)
     * @param coffee
     * @return
     */
    @PostMapping("")
    public Coffee add2(Coffee coffee) {
        return coffee;
    }

    /**
     * json
     * @param coffee
     * @return
     */
    @PostMapping("/body")
    public Coffee addWithBody(@RequestBody Coffee coffee) {
        return coffee;
    }

    /**
     * params + form-data / form-data
     * @param coffee
     * @return
     */
    @PostMapping(value = "/form-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Coffee addWithFormdata(Coffee coffee) {
        return coffee;
    }

    /**
     * params + form-data-urlencoded / form-data-urlencoded
     * @param coffee
     * @return
     */
    @PostMapping(value = "/form-data-urlencoded", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Coffee addWithFormdataUrlencoded(Coffee coffee) {
        return coffee;
    }

    /**
     * params + json 模式可以调用, 但是只有params传参会被接收到,json传参没有接收到
     * @param coffee
     * @return
     */
    @PostMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Coffee addWithJson(Coffee coffee) {
        return coffee;
    }

    /**
     * 1.params + json 模式可以调用, 但是params传参没有接收到,只有json传参会被接收到
     * 2.json
     * @param coffee
     * @return
     */
    @PostMapping(value = "/body-json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Coffee addWithBodyJson(@RequestBody Coffee coffee) {
        return coffee;
    }

    @PostMapping(value = "/upload")
    public String upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        return originalFilename;
    }

    @PostMapping(value = "/upload1")
    public String upload1(MultipartRequest request) {
        MultipartFile multipartFile = request.getFile("file");
        String originalFilename = multipartFile.getOriginalFilename();
        return originalFilename;
    }

    @PostMapping(value = "/upload2")
    public String upload2(MultipartRequest request) {
        Map<String, MultipartFile> map = request.getFileMap();
        Set<Map.Entry<String, MultipartFile>> set = map.entrySet();
        Iterator<Map.Entry<String, MultipartFile>> iterator = set.iterator();
        List<String> list = new ArrayList<>();
        while(iterator.hasNext()) {
            list.add(iterator.next().getValue().getOriginalFilename());
        }
        return Arrays.toString(list.toArray());
    }

    @PostMapping("/upload3")
    public CoffeeFile upload3(MultipartFile file, CoffeeFile coffeeFile) {
        coffeeFile.setFileName(file.getOriginalFilename());
        return coffeeFile;
    }
}
