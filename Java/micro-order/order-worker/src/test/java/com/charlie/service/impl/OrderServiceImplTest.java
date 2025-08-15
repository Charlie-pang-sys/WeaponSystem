//package com.charlie.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
//import com.charlie.dao.OrderDao;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(SpringExtension.class)
//public class OrderServiceImplTest {
//    @InjectMocks
//    private OrderServiceImpl orderService; // 被测服务实现类
//
//    @Mock
//    private OrderDao orderDao; // Mock 数据访问层
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this); // 初始化 Mock 对象 (JUnit 5方式)
//    }
//
//    /**
//     * 测试正常更新订单状态
//     */
//    @Test
//    public void testUpdateOrderStatus_Success() {
//        // Given
//        String orderId = "123";
//        int status = 1;
//
//        // Mock dao.update 方法返回值
//        when(orderDao.update(isNull(), any(UpdateWrapper.class))).thenReturn(1);
//
//        // When
//        ResponseEntity<String> response = orderService.updateOrderStatus(orderId, status);
//
//        // Then
//        assertEquals("更新成功", response.getBody());
//        verify(orderDao, times(1)).update(isNull(), any(UpdateWrapper.class));
//    }
//}