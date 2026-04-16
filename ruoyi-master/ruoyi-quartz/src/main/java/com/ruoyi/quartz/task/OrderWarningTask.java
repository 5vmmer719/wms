package com.ruoyi.quartz.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ruoyi.base.domain.QualityTask;
import com.ruoyi.base.service.IQualityTaskService;
import com.ruoyi.stock.domain.StockCustomerOrder;
import com.ruoyi.stock.domain.StockProdOrder;
import com.ruoyi.stock.service.IStockCustomerOrderService;
import com.ruoyi.stock.service.IStockProdOrderService;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;

/**
 * 订单异常预警定时任务
 * 定时扫描：
 *   1. 交付日期临近（≤3天）但未完工的客户订单
 *   2. 已逾期的客户订单
 *   3. 超过计划完成日期但仍未完工的生产工单
 *   4. 质检不合格的未处理记录
 *
 * @author wms
 */
@Component("orderWarningTask")
public class OrderWarningTask
{
    private static final Logger log = LoggerFactory.getLogger(OrderWarningTask.class);

    @Autowired
    private IStockCustomerOrderService customerOrderService;

    @Autowired
    private IStockProdOrderService prodOrderService;

    @Autowired
    private IQualityTaskService qualityTaskService;

    @Autowired
    private ISysNoticeService noticeService;

    /**
     * 订单异常预警检查（无参方法，供定时任务调用）
     */
    public void checkOrderWarning()
    {
        log.info("========== 开始执行订单异常预警检查任务 ==========");

        Date now = new Date();
        StringBuilder content = new StringBuilder();
        int warningCount = 0;

        // 1. 检查即将到期和已逾期的客户订单
        List<StockCustomerOrder> allOrders = customerOrderService.selectStockCustomerOrderList(new StockCustomerOrder());
        List<StockCustomerOrder> overdueOrders = allOrders.stream()
            .filter(o -> o.getDeliveryDate() != null
                && !"delivered".equals(o.getOrderStatus())
                && !"closed".equals(o.getOrderStatus())
                && o.getDeliveryDate().before(now))
            .collect(Collectors.toList());

        List<StockCustomerOrder> urgentOrders = allOrders.stream()
            .filter(o -> o.getDeliveryDate() != null
                && !"delivered".equals(o.getOrderStatus())
                && !"closed".equals(o.getOrderStatus())
                && !o.getDeliveryDate().before(now)
                && (o.getDeliveryDate().getTime() - now.getTime()) / (1000 * 60 * 60 * 24) <= 3)
            .collect(Collectors.toList());

        if (!overdueOrders.isEmpty() || !urgentOrders.isEmpty())
        {
            content.append("<h3>客户订单交付预警</h3>");
            content.append("<table border='1' cellpadding='5' cellspacing='0' style='border-collapse:collapse;'>");
            content.append("<tr style='background-color:#f2f2f2;'>");
            content.append("<th>订单号</th><th>客户</th><th>要求交付日期</th><th>状态</th><th>预警</th>");
            content.append("</tr>");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (StockCustomerOrder o : overdueOrders)
            {
                long days = (now.getTime() - o.getDeliveryDate().getTime()) / (1000 * 60 * 60 * 24);
                content.append("<tr>");
                content.append("<td>").append(o.getOrderNo()).append("</td>");
                content.append("<td>").append(o.getCustomerName()).append("</td>");
                content.append("<td>").append(sdf.format(o.getDeliveryDate())).append("</td>");
                content.append("<td>").append(o.getOrderStatus()).append("</td>");
                content.append("<td style='color:red;font-weight:bold;'>已逾期 ").append(days).append(" 天</td>");
                content.append("</tr>");
                warningCount++;
            }
            for (StockCustomerOrder o : urgentOrders)
            {
                long days = (o.getDeliveryDate().getTime() - now.getTime()) / (1000 * 60 * 60 * 24);
                content.append("<tr>");
                content.append("<td>").append(o.getOrderNo()).append("</td>");
                content.append("<td>").append(o.getCustomerName()).append("</td>");
                content.append("<td>").append(sdf.format(o.getDeliveryDate())).append("</td>");
                content.append("<td>").append(o.getOrderStatus()).append("</td>");
                content.append("<td style='color:orange;font-weight:bold;'>还有 ").append(days).append(" 天到期</td>");
                content.append("</tr>");
                warningCount++;
            }
            content.append("</table><br/>");
        }

        // 2. 检查生产延期的工单
        List<StockProdOrder> allProdOrders = prodOrderService.selectStockProdOrderList(new StockProdOrder());
        List<StockProdOrder> delayedOrders = allProdOrders.stream()
            .filter(po -> po.getPlanEndDate() != null
                && !"completed".equals(po.getOrderStatus())
                && !"closed".equals(po.getOrderStatus())
                && po.getPlanEndDate().before(now))
            .collect(Collectors.toList());

        if (!delayedOrders.isEmpty())
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            content.append("<h3>生产工单延期预警</h3>");
            content.append("<table border='1' cellpadding='5' cellspacing='0' style='border-collapse:collapse;'>");
            content.append("<tr style='background-color:#f2f2f2;'>");
            content.append("<th>工单号</th><th>产品</th><th>计划完成日期</th><th>超期天数</th>");
            content.append("</tr>");

            for (StockProdOrder po : delayedOrders)
            {
                long days = (now.getTime() - po.getPlanEndDate().getTime()) / (1000 * 60 * 60 * 24);
                content.append("<tr>");
                content.append("<td>").append(po.getOrderNo()).append("</td>");
                content.append("<td>").append(po.getMatName()).append("</td>");
                content.append("<td>").append(sdf.format(po.getPlanEndDate())).append("</td>");
                content.append("<td style='color:red;'>").append(days).append(" 天</td>");
                content.append("</tr>");
                warningCount++;
            }
            content.append("</table><br/>");
        }

        // 3. 检查质检不合格记录
        QualityTask taskQuery = new QualityTask();
        taskQuery.setTaskStatus("failed");
        List<QualityTask> failedTasks = qualityTaskService.selectQualityTaskList(taskQuery);
        if (!failedTasks.isEmpty())
        {
            content.append("<h3>质检不合格预警</h3>");
            content.append("<table border='1' cellpadding='5' cellspacing='0' style='border-collapse:collapse;'>");
            content.append("<tr style='background-color:#f2f2f2;'>");
            content.append("<th>任务编号</th><th>物料</th><th>不合格数量</th><th>来源单号</th>");
            content.append("</tr>");

            for (QualityTask task : failedTasks)
            {
                content.append("<tr>");
                content.append("<td>").append(task.getTaskNo()).append("</td>");
                content.append("<td>").append(task.getMatName()).append("</td>");
                content.append("<td style='color:red;'>").append(task.getUnqualifiedQty() != null ? task.getUnqualifiedQty() : "N/A").append("</td>");
                content.append("<td>").append(task.getSourceNo() != null ? task.getSourceNo() : "-").append("</td>");
                content.append("</tr>");
                warningCount++;
            }
            content.append("</table>");
        }

        if (warningCount == 0)
        {
            log.info("订单异常预警检查完成，暂无异常");
            return;
        }

        // 创建系统通知公告
        SysNotice notice = new SysNotice();
        notice.setNoticeTitle("【订单预警】" + warningCount + "条异常需要关注");
        notice.setNoticeType("1"); // 1=通知
        notice.setNoticeContent(content.toString());
        notice.setStatus("0"); // 0=正常
        notice.setCreateBy("system");
        noticeService.insertNotice(notice);

        log.info("订单异常预警通知已发送，预警条数：{}", warningCount);
        log.info("========== 订单异常预警检查任务执行完毕 ==========");
    }
}

