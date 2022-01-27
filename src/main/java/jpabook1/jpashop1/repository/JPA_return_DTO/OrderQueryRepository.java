package jpabook1.jpashop1.repository.JPA_return_DTO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private EntityManager em;

    /**
     * 컬렉션은 별도로 조회
     */
    public List<OrderQueryDto> findOrderQueryDtos() {

        List<OrderQueryDto> result = findOrders();

        //루프 돌리면서 컬렉션 추가
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new jpabook1.jpashop1.repository.JPA_return_DTO.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)"
                +
                        " from Order o" +
                        " from o.member m" +
                        " join o.delivery d", OrderQueryDto.class)
                .getResultList();

    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook1.jpashop1.repository.JPA_return_DTO.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id = : orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }
}
