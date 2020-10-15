package com.example.hello;

import java.util.function.Function;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class KafkaFunApplication {

	Logger log = LoggerFactory.getLogger(KafkaFunApplication.class);

    @Autowired
    private KafkaTemplate<Object, Object> template;

    private final TaskExecutor exec = new SimpleAsyncTaskExecutor();

    @KafkaListener(id = "test", topics = "hello")
    public void listen(String in) {
        log.info("Received: " + in);
        this.exec.execute(() -> {});
    }

    @Bean
    public NewTopic topic() {
        return new NewTopic("hello", 1, (short) 1);
    }

    @Bean
	public Function<String, String> hello() {
		return (in) -> {
            this.template.send("hello", in);
			return "Sent " + in + " to 'hello' topic";
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(KafkaFunApplication.class, args);
	}

}
