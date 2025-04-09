package com.example.barun;

import com.example.barun.controllers.HealthCheck;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HealthCheck.class)
public class HealthCheckTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHealthCheckEndpoint() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hey, Barun! Working good and it's running well."));
    }
}
