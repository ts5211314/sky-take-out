package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 数据统计
 */
@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "数据统计")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin , @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){

        TurnoverReportVO turnoverStatistics = reportService.getTurnoverStatistics(begin, end);
        return Result.success(turnoverStatistics);
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/userStatistics")
    @ApiOperation("用户统计")
    public Result<UserReportVO>  userStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin , @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){

        UserReportVO userStatistics = reportService.getUserStatistics(begin, end);
        return Result.success(userStatistics);
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计")
    public  Result<OrderReportVO> orderStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin , @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        OrderReportVO orderStatistics = reportService.getOrderStatistics(begin, end);
        return Result.success(orderStatistics);
    }

    /**
     * 销量排名Top10
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/top10")
    @ApiOperation("销量排名Top10")
    public Result<SalesTop10ReportVO>  top10(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin , @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        SalesTop10ReportVO top10ReportVO = reportService.getTop10(begin , end);
        return Result.success(top10ReportVO);
    }

}
