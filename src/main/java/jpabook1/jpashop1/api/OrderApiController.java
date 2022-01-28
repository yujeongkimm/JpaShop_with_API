package jpabook1.jpashop1.api;

import jpabook1.jpashop1.domain.Address;
import jpabook1.jpashop1.domain.Order;
import jpabook1.jpashop1.domain.OrderItem;
import jpabook1.jpashop1.domain.OrderStatus;
import jpabook1.jpashop1.repository.JPA_return_DTO.OrderQueryDto;
import jpabook1.jpashop1.repository.JPA_return_DTO.OrderQueryRepository;
import jpabook1.jpashop1.repository.OrderRepository;
import jpabook1.jpashop1.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;


    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return result;
    }


    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
            this.orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDto {
        private Long orderId;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            orderId = orderItem.getId();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }

    //일대다 패치 조인
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());
        return result;
    }

    //일대다 페이징 처리
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset") int offset,
                                        @RequestParam(value = "limit") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> result = orders.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());

        return result;
    }

    //JPA에서 DTO로 반환
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
    return orderQueryRepository.findOrderQueryDtos();
    }

    //JPA에서 DTO로 반환 - 최적화
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }
}
