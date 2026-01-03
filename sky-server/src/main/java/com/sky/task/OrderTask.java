package com.sky.task;

import com.sky.constant.MessageConstant;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务类 定时处理订单状态
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 * * * * ?")
    public  void  processTimeOrder(){
        log.info("定时处理超时订单:{}" , LocalDateTime.now());
        LocalDateTime dateTime = LocalDateTime.now().plusMinutes(-15);

        List<Orders> byStatusAndOrderTime = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT, dateTime);

        if(byStatusAndOrderTime != null && !byStatusAndOrderTime.isEmpty()){
            for (Orders orders : byStatusAndOrderTime) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    /**
     * 处理一直派送订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public  void processDeliveryOrder(){
        log.info("定时处理派送中订单 ：{}" , LocalDateTime.now());

        LocalDateTime dateTime = LocalDateTime.now().plusHours(-1);
        List<Orders> byStatusAndOrderTime = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, dateTime);

        if(byStatusAndOrderTime != null && !byStatusAndOrderTime.isEmpty()){
            for (Orders orders : byStatusAndOrderTime) {
                orders.setStatus(Orders.COMPLETED);

                orderMapper.update(orders);
            }
        }
    }
}
