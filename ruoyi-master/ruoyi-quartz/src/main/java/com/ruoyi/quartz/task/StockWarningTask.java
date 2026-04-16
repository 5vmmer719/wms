package com.ruoyi.quartz.task;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.stock.domain.StockInfo;
import com.ruoyi.stock.service.IStockInfoService;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;

/**
 * 库存预警定时任务
 * 每隔30分钟检查库存，当物料库存低于安全库存时自动发送通知公告
 *
 * @author wms
 */
@Component("stockWarningTask")
public class StockWarningTask
{
    private static final Logger log = LoggerFactory.getLogger(StockWarningTask.class);

    @Autowired
    private IStockInfoService stockInfoService;

    @Autowired
    private ISysNoticeService noticeService;

    /**
     * 库存预警检查（无参方法，供定时任务调用）
     */
    public void checkStockWarning()
    {
        log.info("========== 开始执行库存预警检查任务 ==========");

        // 查询所有物料的库存汇总（含安全库存和库存上限字段）
        StockInfo query = new StockInfo();
        List<StockInfo> statsList = stockInfoService.selectStockInfoStatsList(query);

        // 筛选出库存低于安全库存的物料
        List<StockInfo> lowList = statsList.stream()
            .filter(info -> info.getSafetyStock() != null
                && info.getSafetyStock().compareTo(BigDecimal.ZERO) > 0
                && (info.getStatsQuantity() == null
                    || info.getStatsQuantity().compareTo(info.getSafetyStock()) < 0))
            .collect(Collectors.toList());

        // 筛选出库存超过上限的物料
        List<StockInfo> highList = statsList.stream()
            .filter(info -> info.getMaxStock() != null
                && info.getMaxStock().compareTo(BigDecimal.ZERO) > 0
                && info.getStatsQuantity() != null
                && info.getStatsQuantity().compareTo(info.getMaxStock()) > 0)
            .collect(Collectors.toList());

        if (lowList.isEmpty() && highList.isEmpty())
        {
            log.info("库存预警检查完成，所有物料库存正常");
            return;
        }

        StringBuilder content = new StringBuilder();

        // 低库存预警
        if (!lowList.isEmpty())
        {
            log.warn("发现 {} 种物料库存不足", lowList.size());
            content.append("<h3 style='color:#f56c6c;'>库存不足预警</h3>");
            content.append("<p>以下物料库存低于安全库存阈值，请及时补充：</p>");
            content.append("<table border='1' cellpadding='5' cellspacing='0' style='border-collapse:collapse;'>");
            content.append("<tr style='background-color:#fef0f0;'>");
            content.append("<th>物料编码</th><th>物料名称</th><th>当前库存</th><th>安全库存</th><th>缺口数量</th>");
            content.append("</tr>");

            for (StockInfo info : lowList)
            {
                BigDecimal currentQty = info.getStatsQuantity() != null ? info.getStatsQuantity() : BigDecimal.ZERO;
                BigDecimal shortage = info.getSafetyStock().subtract(currentQty);
                content.append("<tr>");
                content.append("<td>").append(info.getMatCode()).append("</td>");
                content.append("<td>").append(info.getMatName()).append("</td>");
                content.append("<td style='color:red;font-weight:bold;'>").append(currentQty).append("</td>");
                content.append("<td>").append(info.getSafetyStock()).append("</td>");
                content.append("<td style='color:red;'>").append(shortage).append("</td>");
                content.append("</tr>");
            }
            content.append("</table>");
        }

        // 库存超限预警
        if (!highList.isEmpty())
        {
            log.warn("发现 {} 种物料库存超过上限", highList.size());
            content.append("<h3 style='color:#e6a23c;'>库存超限预警</h3>");
            content.append("<p>以下物料库存超过库存上限，请关注：</p>");
            content.append("<table border='1' cellpadding='5' cellspacing='0' style='border-collapse:collapse;'>");
            content.append("<tr style='background-color:#fdf6ec;'>");
            content.append("<th>物料编码</th><th>物料名称</th><th>当前库存</th><th>库存上限</th><th>超出数量</th>");
            content.append("</tr>");

            for (StockInfo info : highList)
            {
                BigDecimal currentQty = info.getStatsQuantity();
                BigDecimal excess = currentQty.subtract(info.getMaxStock());
                content.append("<tr>");
                content.append("<td>").append(info.getMatCode()).append("</td>");
                content.append("<td>").append(info.getMatName()).append("</td>");
                content.append("<td style='color:#e6a23c;font-weight:bold;'>").append(currentQty).append("</td>");
                content.append("<td>").append(info.getMaxStock()).append("</td>");
                content.append("<td style='color:#e6a23c;'>").append(excess).append("</td>");
                content.append("</tr>");
            }
            content.append("</table>");
        }

        int totalWarnings = lowList.size() + highList.size();
        content.append("<p>共 ").append(totalWarnings).append(" 种物料异常，请及时处理。</p>");

        // 创建系统通知公告
        String title = "【库存预警】";
        if (!lowList.isEmpty()) title += lowList.size() + "种不足";
        if (!lowList.isEmpty() && !highList.isEmpty()) title += "，";
        if (!highList.isEmpty()) title += highList.size() + "种超限";

        SysNotice notice = new SysNotice();
        notice.setNoticeTitle(title);
        notice.setNoticeType("1"); // 1=通知
        notice.setNoticeContent(content.toString());
        notice.setStatus("0"); // 0=正常
        notice.setCreateBy("system");

        noticeService.insertNotice(notice);

        log.info("库存预警通知已发送，预警物料数量：{}", totalWarnings);
        log.info("========== 库存预警检查任务执行完毕 ==========");
    }
}

