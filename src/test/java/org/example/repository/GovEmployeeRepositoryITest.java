package org.example.repository;

import org.example.config.IntegrationTestBase;
import org.example.models.entity.GovEmployee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class GovEmployeeRepositoryITest extends IntegrationTestBase {
    @Autowired
    private GovEmployeeRepository govEmployeeRepository;

    @Test
    void saveAndFindGovEmployee() {
        GovEmployee emp = new GovEmployee();
        emp.setName("Alice");
        GovEmployee saved = govEmployeeRepository.save(emp);
        assertThat(saved.getId()).isNotNull();
        GovEmployee found = govEmployeeRepository.findById(saved.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Alice");
    }

    @Test
    void findAllAndDelete() {
        GovEmployee e1 = govEmployeeRepository.save(new GovEmployee(null, "Alice"));
        GovEmployee e2 = govEmployeeRepository.save(new GovEmployee(null, "Bob"));
        List<GovEmployee> all = govEmployeeRepository.findAll();
        assertThat(all).hasSizeGreaterThanOrEqualTo(2);
        govEmployeeRepository.delete(e1);
        assertThat(govEmployeeRepository.findById(e1.getId())).isEmpty();
    }
} 