package com.example.techtask.service.impl;

import com.example.techtask.model.Order;
import com.example.techtask.model.User;
import com.example.techtask.model.enumiration.OrderStatus;
import com.example.techtask.repository.OrderRepository;
import com.example.techtask.repository.UserRepository;
import com.example.techtask.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    @Override
    public User findUser() {
        List<Order> orders = orderRepository.findAll();
        Optional<Integer> userId =
        orders.stream()
                .filter(order -> order.getCreatedAt().getYear() == 2003)
                .collect(Collectors.groupingBy(
                        Order::getUserId,
                        Collectors.summingDouble(Order::getPrice)
                )).entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
        return userId.flatMap(userRepository::findById).orElse(null);
    }

    @Override
    public List<User> findUsers() {
        List<Order> orders = orderRepository.findAll();
        List<User> users = userRepository.findAll();
        Set<Integer> usersWithOrders =
        orders.stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.PAID)
                .filter(order -> order.getCreatedAt().getYear() == 2010).map(Order::getUserId)
                .collect(Collectors.toSet());
        return users.stream().filter(user -> usersWithOrders.contains(user.getId())) // Фильтруем по userId
                .collect(Collectors.toList());
    }
}
