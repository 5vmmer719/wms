package com.ruoyi.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 订单号生成工具类
 *
 * 支持多种类型单号自动生成，格式：前缀 + 年月日时分 + 随机数/序列号
 * 所有前缀定义在 {@link OrderPrefix} 枚举中，便于统一管理和扩展
 *
 * @author ruoyi
 */
public class OrderNoUtil {

    /**
     * 单号前缀枚举
     * 统一管理所有单号前缀，便于维护和扩展
     */
    public enum OrderPrefix {
        // 入库单
        IN_PURCHASE("IP", "原料入库单"),
        IN_ALLOT("IA", "调拨入库单"),

        // 出库单
        OUT_PRODUCTION("OP", "生产领料出库单"),
        OUT_REPAIR("OR", "补领料出库单"),
        OUT_COMMON("OC", "通用领料出库单"),
        OUT_ALLOT("OA", "调拨出库单"),

        // 入库退货单
        IN_PURCHASE_RETURN("IPR", "原料入库退货单"),

        // 出库退货单
        OUT_PRODUCTION_RETURN("OPR", "生产领料退货单"),
        OUT_REPAIR_RETURN("ORR", "补领料退货单"),
        OUT_COMMON_RETURN("OCR", "通用领料退货单"),

        // 调拨单
        ALLOT("A", "调拨单"),

        // 生产订单
        PRODUCTION("P", "生产订单");

        private final String prefix;
        private final String description;

        OrderPrefix(String prefix, String description) {
            this.prefix = prefix;
            this.description = description;
        }

        public String getPrefix() {
            return prefix;
        }

        public String getDescription() {
            return description;
        }

        /**
         * 根据前缀获取枚举
         */
        public static OrderPrefix getByPrefix(String prefix) {
            if (prefix == null) {
                return null;
            }
            for (OrderPrefix op : values()) {
                if (op.getPrefix().equals(prefix)) {
                    return op;
                }
            }
            return null;
        }

        /**
         * 判断给定单号是否以指定前缀开头
         */
        public static boolean startsWith(String orderNo, OrderPrefix prefix) {
            return orderNo != null && prefix != null && orderNo.startsWith(prefix.getPrefix());
        }

        /**
         * 识别单号类型
         */
        public static OrderPrefix identify(String orderNo) {
            if (orderNo == null || orderNo.isEmpty()) {
                return null;
            }
            // 按前缀长度降序检查，避免误判（如 IPR vs IP）
            OrderPrefix[] prefixes = {
                IN_PURCHASE_RETURN, IN_ALLOT,
                OUT_PRODUCTION_RETURN, OUT_REPAIR_RETURN, OUT_COMMON_RETURN, OUT_ALLOT,
                IN_PURCHASE,
                OUT_PRODUCTION, OUT_REPAIR, OUT_COMMON,
                ALLOT,
                PRODUCTION
            };
            for (OrderPrefix op : prefixes) {
                if (orderNo.startsWith(op.getPrefix())) {
                    return op;
                }
            }
            return null;
        }
    }

    /** 日期格式化器，线程安全 */
    private static final ThreadLocal<DateFormat> DATE_FORMAT = ThreadLocal.withInitial(
        () -> new SimpleDateFormat("yyyyMMddHHmm")
    );

    /** 序列号生成器（备用） */
    private static final AtomicLong sequence = new AtomicLong(0);

    /** 随机数最小值 */
    private static final int RANDOM_MIN = 10;

    /** 随机数最大值 */
    private static final int RANDOM_MAX = 99;

    /**
     * 生成唯一单号
     * 格式：前缀 + 年月日时分 + 两位随机数
     *
     * @param prefix 单号前缀
     * @return 唯一单号
     */
    public static synchronized String generateUniqueKey(String prefix) {
        int random = (int) (Math.random() * (RANDOM_MAX - RANDOM_MIN + 1)) + RANDOM_MIN;
        String timeStr = DATE_FORMAT.get().format(new Date());
        return prefix + timeStr + random;
    }

    /**
     * 根据枚举生成唯一单号
     *
     * @param orderPrefix 单号前缀枚举
     * @return 唯一单号
     */
    public static String generate(OrderPrefix orderPrefix) {
        if (orderPrefix == null) {
            throw new IllegalArgumentException("订单前缀不能为空");
        }
        return generateUniqueKey(orderPrefix.getPrefix());
    }

    /**
     * 生成带序列号的单号（适用于高并发场景）
     * 格式：前缀 + 年月日时分 + 四位序列号
     *
     * @param prefix 单号前缀
     * @return 唯一单号
     */
    public static synchronized String generateWithSequence(String prefix) {
        long seq = sequence.incrementAndGet() % 10000;
        String timeStr = DATE_FORMAT.get().format(new Date());
        return prefix + timeStr + String.format("%04d", seq);
    }

    // ==================== 兼容旧接口的方法 ====================

    /**
     * 旧版本兼容：获取入库单号
     * @deprecated 请使用 {@link #generate(OrderPrefix)} 替代
     */
    @Deprecated
    public static String getInOrderNo(String orderType) {
        return generate(OrderPrefix.IN_PURCHASE);
    }

    /**
     * 旧版本兼容：获取入库退货单号
     * @deprecated 请使用 {@link #generate(OrderPrefix)} 替代
     */
    @Deprecated
    public static String getInOrderReturnNo(String returnType) {
        return generate(OrderPrefix.IN_PURCHASE_RETURN);
    }

    /**
     * 旧版本兼容：获取出库单号
     * @deprecated 请使用 {@link #generate(OrderPrefix)} 替代
     */
    @Deprecated
    public static String getOutOrderNo(String orderType) {
        // 根据订单类型返回不同前缀
        if ("production".equals(orderType)) {
            return generate(OrderPrefix.OUT_PRODUCTION);
        } else if ("repair".equals(orderType)) {
            return generate(OrderPrefix.OUT_REPAIR);
        } else {
            return generate(OrderPrefix.OUT_COMMON);
        }
    }

    /**
     * 旧版本兼容：获取出库退货单号
     * @deprecated 请使用 {@link #generate(OrderPrefix)} 替代
     */
    @Deprecated
    public static String getOutOrderReturnNo(String returnType) {
        // 根据退货类型返回不同前缀
        if ("production_return".equals(returnType)) {
            return generate(OrderPrefix.OUT_PRODUCTION_RETURN);
        } else if ("repair_return".equals(returnType)) {
            return generate(OrderPrefix.OUT_REPAIR_RETURN);
        } else {
            return generate(OrderPrefix.OUT_COMMON_RETURN);
        }
    }

    /**
     * 获取调拨单号
     */
    public static String getAllotOrderNo() {
        return generate(OrderPrefix.ALLOT);
    }

    /**
     * 获取生产订单号
     */
    public static String getProductionOrderNo() {
        return generate(OrderPrefix.PRODUCTION);
    }

    // ==================== 常量定义（保持向后兼容） ====================

    /**
     * 原料入库前缀
     */
    public static final String IN_PURCHASE_PREFIX = OrderPrefix.IN_PURCHASE.getPrefix();

    /**
     * 生产订单前缀
     */
    public static final String PROD_PREFIX = OrderPrefix.PRODUCTION.getPrefix();

    /**
     * 生产领料前缀
     */
    public static final String OUT_PROD_PREFIX = OrderPrefix.OUT_PRODUCTION.getPrefix();

    /**
     * 补领料前缀
     */
    public static final String OUT_REPAIR_PREFIX = OrderPrefix.OUT_REPAIR.getPrefix();

    /**
     * 通用领料前缀
     */
    public static final String OUT_COMMON_PREFIX = OrderPrefix.OUT_COMMON.getPrefix();

    /**
     * 原料入库退货前缀
     */
    public static final String IN_PURCHASE_RETURN_PREFIX = OrderPrefix.IN_PURCHASE_RETURN.getPrefix();

    /**
     * 生产领料退货前缀
     */
    public static final String OUT_PROD_RETURN_PREFIX = OrderPrefix.OUT_PRODUCTION_RETURN.getPrefix();

    /**
     * 补领料退货前缀
     */
    public static final String OUT_REPAIR_RETURN_PREFIX = OrderPrefix.OUT_REPAIR_RETURN.getPrefix();

    /**
     * 通用领料退货前缀
     */
    public static final String OUT_COMMON_RETURN_PREFIX = OrderPrefix.OUT_COMMON_RETURN.getPrefix();

    /**
     * 调拨单前缀
     */
    public static final String ALLOT_PREFIX = OrderPrefix.ALLOT.getPrefix();
}