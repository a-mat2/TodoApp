package com.company.todoapp.model.projection;

import com.company.todoapp.model.Task;
import com.company.todoapp.model.TaskGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupReadModelTest {

    @Test
    @DisplayName("should create null deadline for group when no task deadlines")
    public void constructor_noDeadlines_createsNullDeadline() {
        // given
        var source = new TaskGroup();
        source.setDescription("example");
        source.setTasks(Set.of(new Task("sampleTask", null)));

        // when
        var result = new GroupReadModel((source));

        // then
        assertThat(result).hasFieldOrPropertyWithValue("deadline", null);
    }

}