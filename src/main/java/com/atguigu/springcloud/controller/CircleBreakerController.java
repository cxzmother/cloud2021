package com.atguigu.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author lisalou
 * @create 2021-09-23-2:25 下午
 */
@RestController
public class CircleBreakerController {

    //@Value("${server-url.nacos-user-service}")
    public static  String SERVICE_URL="http://nacos-payment-provider";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping(value="/cosummer/fallback/{id}")
    //@SentinelResource(value="fallback")
    //@SentinelResource(value="fallback",fallback="handlerFallback")
    //@SentinelResource(value="fallback",blockHandler="blockHandler")
    @SentinelResource(value="fallback",fallback = "handlerFallback",blockHandler = "blockHandler",exceptionsToIgnore = IllegalArgumentException.class)
    public CommonResult<Payment> fallbach(@PathVariable("id") Long id)  {
        System.out.println(SERVICE_URL);
        CommonResult<Payment> result = restTemplate.getForObject(SERVICE_URL+"/paymentSQL/"+id,CommonResult.class,id);
        if(id==4){
            throw new IllegalArgumentException("IllegalArgumentException,非法参数异常");
        }else if(result.getData()==null){
            throw new NullPointerException("NullpointerException,该ID没有对应记录，空指针异常");
        }
        return result ;
    }

    public CommonResult handlerFallback(@PathVariable("id") Long id,Throwable e){
        Payment payment = new Payment(id ,"null");
        return new CommonResult(444,"兜底异常handlerFallback，exception内容"+e.getMessage(),payment);
    }

    public CommonResult blockHandler(@PathVariable("id") Long id, BlockException exception){
        Payment payment = new Payment(id,"null");
        return new CommonResult(4445,"兜底异常blockHandler，exceprion内容"+exception.getMessage(),payment);
    }

    @Resource
    private PaymentService paymentService;

    @GetMapping(value="/consumer/paymentSQL/{id}")
    public CommonResult<Payment> paymentSQL (@PathVariable("id") Long id){
        System.out.println(111);
        return paymentService.paymentSQL(id);
    }


}
