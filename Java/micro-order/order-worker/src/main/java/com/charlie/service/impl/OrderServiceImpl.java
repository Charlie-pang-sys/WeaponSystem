package com.charlie.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charlie.constant.TopicConstant;
import com.charlie.dao.OrderDao;
import com.charlie.pojo.entity.OrderDO;
import com.charlie.pojo.entity.PageResult;
import com.charlie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderDO> implements OrderService {

    @Resource
    private OrderDao orderDao;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    private static int flagNum = 0;
    @Resource
    RocketMQTemplate rocketMQTemplate;

    private static final String ORDER_ZSET_KEY = "zset:orders:%s"; // %s=userId

    @Transactional
    @Override
    public boolean updateOrderStatus(String orderId, int status) {
        try {
            if (flagNum % 100 == 0) {
                update(orderId, 2);
                throw new RuntimeException("订单异常");
            }
            update(orderId, 1);
            flagNum++;
        } catch (RuntimeException e) {
            log.error("1/100的订单异常");
            String str = "订单id："+orderId+"修改状态异常，预修改的状态："+status;
            rocketMQTemplate.convertAndSend(TopicConstant.ORDER_ALARM_TOPIC, str);
            flagNum++;
        } catch (Exception e) {
            log.error("订单更新异常：{}", e.getMessage());
        }

        return true;
    }

    private boolean update(String orderId, int status) {
        //订单一定存在
        UpdateWrapper<OrderDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("order_id", orderId);
        updateWrapper.set("status", status);
        orderDao.update(null, updateWrapper);
        log.debug("修改订单状态：{}", orderId);
        return true;
    }

    /**
     * 保存订单
     */
    public void saveOrderToRedis(OrderDO order) {
        String hashKey = "order:" + order.getId();
        String zsetKey = String.format(ORDER_ZSET_KEY, order.getUserId());
        // 保存订单详情
        redisTemplate.opsForValue().set(hashKey, order);

        // 添加到 ZSet，score 为时间戳
        redisTemplate.opsForZSet().add(zsetKey, order.getId(), order.getCreateTime().toEpochSecond(ZoneOffset.of("+8")));

    }

    @Override
    public boolean insertBatch(List<OrderDO> orderDOList) {
        return this.saveBatch(orderDOList);
    }

    @Override
    public boolean insert(OrderDO orderDO) {
        return this.save(orderDO);
    }

    /**
     * 分页查询用户订单（支持状态、时间范围）
     *
     * @param userId    用户ID
     * @param status    状态（可选）
     * @param startTime 开始时间（时间戳，可选）
     * @param endTime   结束时间（时间戳，可选）
     * @param pageNum   当前页（从 1 开始）
     * @param pageSize  每页大小
     * @return 分页结果
     */
    public PageResult<List<OrderDO>> queryOrders(
            String userId,
            int status,
            Long startTime,
            Long endTime,
            int pageNum,
            int pageSize) {
        String zsetKey = String.format(ORDER_ZSET_KEY, userId);
        // 校验分页参数
        if (pageNum < 1) pageNum = 1;
        if (pageSize > 100) pageSize = 50;

        double startScore = startTime != null ? startTime : System.currentTimeMillis() - 7 * 24 * 3600_000L;
        double endScore = endTime != null ? endTime : System.currentTimeMillis();

        Set<ZSetOperations.TypedTuple<Object>> tuples = redisTemplate.opsForZSet()
                .reverseRangeByScoreWithScores(zsetKey, startScore, endScore,
                        (long) (pageNum - 1) * pageSize, pageSize);

        if (tuples == null || tuples.isEmpty()) {
            return PageResult.empty();
        }

        List<String> orderKeys = tuples.stream()
                .map(ZSetOperations.TypedTuple::getValue)
                .map(id -> "order:" + id)
                .collect(Collectors.toList());

        List<Object> results = redisTemplate.opsForValue().multiGet(orderKeys);
        List<OrderDO> pageOrders = new ArrayList<>();
        if (results != null) {
            for (Object obj : results) {
                if (obj instanceof OrderDO) {
                    OrderDO order = (OrderDO) obj;
                    // 4. 内存中过滤 status（如果业务需要）
                    if (order.getStatus() == status) {
                        pageOrders.add(order);
                    }
                }
            }
        }
        int total = redisTemplate.opsForZSet().count(zsetKey, startScore, endScore).intValue();
        return PageResult.of(pageOrders, total, pageNum, pageSize);
    }

    @Override
    public ResponseEntity<String> batchCreate(OrderDO orderDO) {
        this.save(orderDO);
        saveOrderToRedis(orderDO);
        return ResponseEntity.ok("success");
    }

}
