package com.example.calculator;

import com.example.calculator.controller.CalculatorController;
import com.example.calculator.service.CalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CalculatorControllerIntegrationTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        CalculatorService calculatorService = new CalculatorService();
        CalculatorController calculatorController =
                new CalculatorController(calculatorService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(calculatorController)
                .build();
    }

    @Test
    void testAddEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/add")
                        .param("a", "2")
                        .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("add"))
                .andExpect(jsonPath("$.result").value(5.0));
    }
}