package ru.rksp.yagunov_producer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rksp.yagunov_producer.model.StudentMessage;

import java.time.Instant;

@RestController
@RequestMapping("/api/producer/students")
@RequiredArgsConstructor
public class StudentProducerController {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;

    @PostMapping
    public ResponseEntity<String> sendStudent(@RequestBody StudentMessage request) {
        // Добавим timestamp на стороне продьюсера
        request.setCreatedAt(Instant.now());

        rabbitTemplate.convertAndSend(exchangeName, routingKey, request);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Message sent to RabbitMQ");
    }
}

