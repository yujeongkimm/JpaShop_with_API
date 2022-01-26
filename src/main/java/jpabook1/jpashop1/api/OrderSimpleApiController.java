package jpabook1.jpashop1.api;

import jpabook1.jpashop1.domain.Address;
import jpabook1.jpashop1.domain.Order;
import jpabook1.jpashop1.domain.OrderStatus;
import jpabook1.jpashop1.repository.OrderRepository;
import jpabook1.jpashop1.repository.OrderSearch;
import jpabook1.jpashop1.repository.OrderSimpleQueryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    /**
     * 주문 조회
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        //Order 테이블 조회 (결과 2개)
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    //DTO로 변환
    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        //생성자
        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //Member 테이블 조회
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getMember().getAddress(); //Delivery 테이블 조회
        }
    }


    //fetch join으로 쿼리 1번 호출(이 경우도 엔티티->DTO로 반환)
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    //JPA에서 DTO로 반환
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderRepository.findOrderDtos();
    }
}
