package com.ruoyi.quartz.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.base.domain.BaseEquipment;
import com.ruoyi.base.domain.BaseEquipmentMaintain;
import com.ruoyi.base.mapper.BaseEquipmentMapper;
import com.ruoyi.base.service.IBaseEquipmentMaintainService;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;

/**
 * 设备定时维护任务
 * 每天凌晨扫描到期设备，自动创建维护单据并发送通知
 *
 * @author wms
 */
@Component("equipmentMaintainTask")
public class EquipmentMaintainTask
{
    private static final Logger log = LoggerFactory.getLogger(EquipmentMaintainTask.class);

    @Autowired
    private BaseEquipmentMapper baseEquipmentMapper;

    @Autowired
    private IBaseEquipmentMaintainService maintainService;

    @Autowired
    private ISysNoticeService noticeService;

    /**
     * 自动创建设备维护单据（无参方法，供定时任务调用）
     * 扫描条件：设备状态=正常(0)，设置了维护周期，下次维护日期 <= 今天
     */
    public void autoCreateMaintain()
    {
        log.info("========== 开始执行设备定时维护检查任务 ==========");

        // 查询所有到期需要维护的设备
        List<BaseEquipment> equipmentList = baseEquipmentMapper.selectEquipmentNeedMaintain();

        if (equipmentList.isEmpty())
        {
            log.info("设备维护检查完成，暂无到期需要维护的设备");
            return;
        }

        log.info("发现 {} 台设备需要维护，开始自动创建维护单据", equipmentList.size());

        int successCount = 0;
        StringBuilder noticeContent = new StringBuilder();
        noticeContent.append("<h3>设备定期维护通知</h3>");
        noticeContent.append("<p>以下设备已到维护周期，系统已自动创建维护单据：</p>");
        noticeContent.append("<table border='1' cellpadding='5' cellspacing='0' style='border-collapse:collapse;'>");
        noticeContent.append("<tr style='background-color:#f2f2f2;'>");
        noticeContent.append("<th>设备编码</th><th>设备名称</th><th>维护周期(天)</th><th>计划维护日期</th><th>维护单号</th>");
        noticeContent.append("</tr>");

        for (BaseEquipment equipment : equipmentList)
        {
            try
            {
                // 创建维护单据
                BaseEquipmentMaintain maintain = new BaseEquipmentMaintain();
                maintain.setEquipmentCode(equipment.getEquipmentCode());
                maintain.setEquipmentName(equipment.getEquipmentName());
                maintain.setMaintainType("routine"); // 例行保养
                maintain.setMaintainDate(new Date());
                maintain.setMaintainDesc("系统自动创建的定期维护单据，维护周期：" + equipment.getMaintainCycle() + "天");
                maintain.setSource("auto"); // 自动创建
                maintain.setCreateBy("system");

                maintainService.insertBaseEquipmentMaintain(maintain);

                String planDate = new SimpleDateFormat("yyyy-MM-dd").format(equipment.getNextMaintainDate());
                noticeContent.append("<tr>");
                noticeContent.append("<td>").append(equipment.getEquipmentCode()).append("</td>");
                noticeContent.append("<td>").append(equipment.getEquipmentName()).append("</td>");
                noticeContent.append("<td>").append(equipment.getMaintainCycle()).append("</td>");
                noticeContent.append("<td>").append(planDate).append("</td>");
                noticeContent.append("<td>").append(maintain.getMaintainNo()).append("</td>");
                noticeContent.append("</tr>");

                successCount++;
                log.info("已为设备[{}]{}自动创建维护单据：{}", equipment.getEquipmentCode(),
                    equipment.getEquipmentName(), maintain.getMaintainNo());
            }
            catch (Exception e)
            {
                log.error("为设备[{}]创建维护单据失败：{}", equipment.getEquipmentCode(), e.getMessage(), e);
            }
        }

        noticeContent.append("</table>");
        noticeContent.append("<p>共 ").append(successCount).append(" 台设备已自动创建维护单据，请及时安排维护人员处理。</p>");

        // 创建系统通知公告
        if (successCount > 0)
        {
            SysNotice notice = new SysNotice();
            notice.setNoticeTitle("【设备维护】" + successCount + "台设备需要定期维护");
            notice.setNoticeType("1"); // 1=通知
            notice.setNoticeContent(noticeContent.toString());
            notice.setStatus("0"); // 0=正常
            notice.setCreateBy("system");
            noticeService.insertNotice(notice);
        }

        log.info("========== 设备定时维护检查任务执行完毕，成功创建 {} 个维护单据 ==========", successCount);
    }
}

