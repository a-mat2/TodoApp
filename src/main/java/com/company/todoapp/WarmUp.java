package com.company.todoapp;

import com.company.todoapp.model.Task;
import com.company.todoapp.model.TaskGroup;
import com.company.todoapp.model.TaskGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class WarmUp implements ApplicationListener<ContextRefreshedEvent> {
    public static final Logger logger = LoggerFactory.getLogger(WarmUp.class);
    private final TaskGroupRepository groupRepository;

    public WarmUp(TaskGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("Application warmup after context refreshed.");
        final String description = "ApplicationContextEvent";
        if (!groupRepository.existsByDescription(description)) {
            logger.info("No required group found! Adding it!");
            var group = new TaskGroup();
            group.setDescription(description);
            group.setTasks(Set.of(
                    new Task("ContextClosedEvent", null),
                    new Task("ContextRefreshedEvent", null),
                    new Task("ContextStoppedEvent", null),
                    new Task("ContextStartedEvent", null)
            ));
            groupRepository.save(group);
        }
    }
}
