package com.example.techtask.service.impl;

import com.example.techtask.model.Order;
import com.example.techtask.model.User;
import com.example.techtask.model.enumiration.UserStatus;
import com.example.techtask.repository.OrderRepository;
import com.example.techtask.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements com.example.techtask.service.OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    @Override
    public Order findOrder() {
        List<Order> orders = orderRepository.findAll();
        Order newOrder = orders.get(0);
        for (Order order : orders) {
            if (order.getCreatedAt().isAfter(newOrder.getCreatedAt()) && order.getQuantity() >= 2) {
                newOrder = order;
            }
        }
        if(newOrder.getQuantity() >= 2){
            return newOrder;
        }else {
            return null;
        }
    }

    @Override
    public List<Order> findOrders() {
        List<User> users = userRepository.findAll();
        return users.stream()
                        .filter(user -> user.getUserStatus() == UserStatus.ACTIVE)
                        .flatMap(user -> user.getOrders().stream())
                        .sorted(Comparator.comparing(Order::getCreatedAt))
                        .collect(Collectors.toList());
    }
}
