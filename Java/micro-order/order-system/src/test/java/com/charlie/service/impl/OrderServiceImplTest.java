package com.charlie.service.impl;

import com.charlie.dao.OrderDao;
import com.charlie.pojo.dto.OrderDTO;
import com.charlie.pojo.entity.OrderDO;
import com.charlie.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService批量创建订单测试")
class OrderServiceTest {
    @Mock
    private OrderDao orderMapper;
    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private OrderServiceImpl orderService; // 假设你的服务类叫OrderService

    private static final String ORDER_CREATE_KEY = "order:create:count"; // 根据实际情况调整

    @BeforeEach
    void setUp() {
        // Mock Redis操作
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);

        // 如果有其他需要mock的方法，在这里添加
        // 例如：doNothing().when(orderService).saveBatch(anyList());
        // 例如：doNothing().when(orderService).printLog(any());
    }

    @Test
    @DisplayName("使用saveBatch方法 - 成功")
    void testBatchCreate_WithSaveBatch_Success() {
        // Arrange
        OrderDTO orderDTO = createValidOrderDTO();

        // 如果使用saveBatch，直接mock返回成功
        OrderServiceImpl spyOrderService = spy(orderService);
        when(spyOrderService.saveBatch(anyList())).thenReturn(true);

        // Mock Redis increment操作
        when(valueOperations.increment(ORDER_CREATE_KEY, 2L)).thenReturn(2L);

        // 注入依赖到spy对象
        ReflectionTestUtils.setField(spyOrderService, "stringRedisTemplate", stringRedisTemplate);

        // Act
        ResponseEntity<String> result = spyOrderService.batchCreate(orderDTO);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("success", result.getBody());

        // 验证方法调用
        verify(spyOrderService, times(1)).saveBatch(anyList());
        verify(valueOperations, times(1)).increment(ORDER_CREATE_KEY, 2L);

        // 验证传递给saveBatch的数据结构正确性
        ArgumentCaptor<List<OrderDO>> listCaptor = ArgumentCaptor.forClass(List.class);
        verify(spyOrderService, times(1)).saveBatch(listCaptor.capture());

        List<OrderDO> capturedOrders = listCaptor.getValue();
        assertEquals(2, capturedOrders.size());

        // 只验证业务字段，不验证时间字段
        capturedOrders.forEach(order -> {
            assertNotNull(order.getUserId());
            assertNotNull(order.getItemId());
            assertTrue(order.getCount() > 0);
            assertEquals(0, order.getStatus());
            assertNotNull(order.getId());
            assertNotNull(order.getOrderId());
        });
    }

    @Test
    @DisplayName("用户ID为空 - 抛出异常")
    void testBatchCreate_EmptyUserId_ThrowsException() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId("");
        orderDTO.setOrders(Collections.emptyList());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.batchCreate(orderDTO));

        assertEquals("用户ID不能为空", exception.getMessage());

        // 验证Redis没有被调用
        verify(valueOperations, never()).increment(anyString(), anyLong());
    }

    @Test
    @DisplayName("用户ID为null - 抛出异常")
    void testBatchCreate_NullUserId_ThrowsException() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(null);
        orderDTO.setOrders(Collections.emptyList());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.batchCreate(orderDTO));

        assertEquals("用户ID不能为空", exception.getMessage());
    }

    @Test
    @DisplayName("商品数量小于等于0 - 抛出异常")
    void testBatchCreate_InvalidCount_ThrowsException() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId("user123");

        OrderDTO.Order orderItem = new OrderDTO.Order();
        orderItem.setItemId("item123");
        orderItem.setCount(0); // 无效数量

        orderDTO.setOrders(Arrays.asList(orderItem));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.batchCreate(orderDTO));

        assertEquals("商品数量必须大于0", exception.getMessage());
    }

    @Test
    @DisplayName("商品数量为负数 - 抛出异常")
    void testBatchCreate_NegativeCount_ThrowsException() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId("user123");

        OrderDTO.Order orderItem = new OrderDTO.Order();
        orderItem.setItemId("item123");
        orderItem.setCount(-1); // 负数数量

        orderDTO.setOrders(Arrays.asList(orderItem));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.batchCreate(orderDTO));

        assertEquals("商品数量必须大于0", exception.getMessage());
    }

    @Test
    @DisplayName("商品ID为空 - 抛出异常")
    void testBatchCreate_EmptyItemId_ThrowsException() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId("user123");

        OrderDTO.Order orderItem = new OrderDTO.Order();
        orderItem.setItemId(""); // 空商品ID
        orderItem.setCount(1);

        orderDTO.setOrders(Arrays.asList(orderItem));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.batchCreate(orderDTO));

        assertEquals("商品ID不能为空", exception.getMessage());
    }

    @Test
    @DisplayName("商品ID为null - 抛出异常")
    void testBatchCreate_NullItemId_ThrowsException() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId("user123");

        OrderDTO.Order orderItem = new OrderDTO.Order();
        orderItem.setItemId(null); // null商品ID
        orderItem.setCount(1);

        orderDTO.setOrders(Arrays.asList(orderItem));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.batchCreate(orderDTO));

        assertEquals("商品ID不能为空", exception.getMessage());
    }

    @Test
    @DisplayName("多个订单项混合验证 - 第二个订单项数量无效")
    void testBatchCreate_MultipleOrders_SecondInvalid_ThrowsException() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId("user123");

        OrderDTO.Order validOrder = new OrderDTO.Order();
        validOrder.setItemId("item1");
        validOrder.setCount(2);

        OrderDTO.Order invalidOrder = new OrderDTO.Order();
        invalidOrder.setItemId("item2");
        invalidOrder.setCount(0); // 无效数量

        orderDTO.setOrders(Arrays.asList(validOrder, invalidOrder));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.batchCreate(orderDTO));

        assertEquals("商品数量必须大于0", exception.getMessage());
    }

    @Test
    @DisplayName("空订单列表 - 成功但不增加Redis计数")
    void testBatchCreate_EmptyOrderList_Success() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId("user123");
        orderDTO.setOrders(Collections.emptyList());

        OrderService spyOrderService = spy(orderService);
        doNothing().when(spyOrderService).saveBatch(anyList());

        // Act
        ResponseEntity<String> result = spyOrderService.batchCreate(orderDTO);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("success", result.getBody());

        verify(spyOrderService, times(1)).saveBatch(Collections.emptyList());
        verify(valueOperations, times(1)).increment(ORDER_CREATE_KEY, 0L);
    }

    /**
     * 创建有效的订单DTO用于测试
     */
    private OrderDTO createValidOrderDTO() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId("user123");

        OrderDTO.Order order1 = new OrderDTO.Order();
        order1.setItemId("item1");
        order1.setCount(2);

        OrderDTO.Order order2 = new OrderDTO.Order();
        order2.setItemId("item2");
        order2.setCount(3);

        orderDTO.setOrders(Arrays.asList(order1, order2));
        return orderDTO;
    }
}

// 如果OrderDTO和OrderItem类不存在，你需要创建它们
// 以下是示例DTO类结构：

/*
public class OrderDTO {
    private String userId;
    private List<OrderItem> orders;

    // getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<OrderItem> getOrders() { return orders; }
    public void setOrders(List<OrderItem> orders) { this.orders = orders; }

    public static class OrderItem {
        private String itemId;
        private int count;

        // getters and setters
        public String getItemId() { return itemId; }
        public void setItemId(String itemId) { this.itemId = itemId; }
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
    }
}
*/