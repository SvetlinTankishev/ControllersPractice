package org.example.service;

import org.example.models.entity.GovEmployee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.example.config.IntegrationTestBase;

@SpringBootTest
@Transactional
public class GovEmployeeServiceITest extends IntegrationTestBase {
    @Autowired
    private GovEmployeeService govEmployeeService;

    @Test
    void addAndGetGovEmployee() {
        GovEmployee emp = govEmployeeService.add("Charlie");
        assertThat(emp.getId()).isNotNull();
        GovEmployee found = govEmployeeService.getById(emp.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Charlie");
    }

    @Test
    void deleteGovEmployee() {
        GovEmployee emp = govEmployeeService.add("Alice");
        boolean deleted = govEmployeeService.delete(emp.getId());
        assertThat(deleted).isTrue();
        assertThat(govEmployeeService.getById(emp.getId())).isNull();
    }

    @Test
    void searchByName() {
        govEmployeeService.add("Bob");
        List<GovEmployee> bobs = govEmployeeService.searchByName("Bob");
        assertThat(bobs).anyMatch(e -> e.getName().equals("Bob"));
    }

    @Test
    void getPage() {
        govEmployeeService.add("Alice");
        govEmployeeService.add("Bob");
        List<GovEmployee> page = govEmployeeService.getPage(0, 2);
        assertThat(page.size()).isLessThanOrEqualTo(2);
    }
} 