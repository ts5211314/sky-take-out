package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;
    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */

    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> list = new ArrayList<>();

        list.add(begin);

        while(!begin.isEqual(end)){
            begin = begin.plusDays(1);
            list.add(begin);
        }

        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : list) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map<Object, Object> map = new HashMap<>();
            map.put("begin" , beginTime);
            map.put("end" , endTime);
            map.put("status" , Orders.COMPLETED);
            Double turnover = orderMapper.sumByMap(map);
            turnover = turnover == null ? 0.0 : turnover;

            turnoverList.add(turnover);

        }

        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(list , ","))
                .turnoverList(StringUtils.join(turnoverList , ","))
                .build();
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> list = new ArrayList<>();

        list.add(begin);

        while(!begin.isEqual(end)){
            begin = begin.plusDays(1);
            list.add(begin);
        }

        List<Integer> newUserList = new ArrayList<>();

        List<Integer> totalUserList = new ArrayList<>();

        for (LocalDate date : list) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map<Object, Object> map = new HashMap<>();
            map.put("end" , endTime);
            Integer totalUser = userMapper.countByMap(map);
            map.put("begin" ,beginTime);
            Integer newUser = userMapper.countByMap(map);

            totalUserList.add(totalUser);
            newUserList.add(newUser);
        }


        return UserReportVO
                .builder()
                .dateList(StringUtils.join(list , ","))
                .newUserList(StringUtils.join(newUserList , ","))
                .totalUserList(StringUtils.join(totalUserList , ","))
                .build();
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> list = new ArrayList<>();

        list.add(begin);

        while(!begin.isEqual(end)){
            begin = begin.plusDays(1);
            list.add(begin);
        }

        List<Integer> orderCountList = new ArrayList<>();

        List<Integer> validOrderCountList = new ArrayList<>();

        for (LocalDate date : list) {

            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map<Object , Object> map = new HashMap();
            map.put("begin" , beginTime);
            map.put("end" , endTime);
            map.put("status" , null);
            Integer orderCount = orderMapper.countByMap(map);
            map.put("status" , Orders.COMPLETED);
            Integer validOrderCount = orderMapper.countByMap(map);
            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);
        }

        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();
        double orderCompletionRate = 0.0;
        if(totalOrderCount != 0){
              orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }

        return OrderReportVO
                .builder()
                .dateList(StringUtils.join(list , ","))
                .orderCountList(StringUtils.join(orderCountList , ","))
                .validOrderCountList(StringUtils.join(validOrderCountList , ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }


    /**
     * 销量Top10
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> salesTop10 = orderDetailMapper.getSalesTop10(beginTime, endTime);

        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        return SalesTop10ReportVO
                .builder()
                .nameList(StringUtils.join(names , ","))
                .numberList(StringUtils.join(numbers , ","))
                .build();
    }
}
