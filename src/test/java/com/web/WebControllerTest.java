package com.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class WebControllerTest {

  @Autowired
  private MockMvc mvc;

  @Test
  void whenPathVariableIsInvalid_thenReturnsStatus400() throws Exception {
    mvc.perform(get("/change/ts/MIN").with(user("admin").password("admin")))
            .andExpect(status().isBadRequest());
  }

  @Test
  void whenRequestParameterIsValid_thenReturnsStatus200() throws Exception {
    mvc.perform(get("/change/3/MIN").with(user("admin").password("admin")))
            .andExpect(status().isOk());
  }
  
  @Test
  void whenBillAmountIsValidMin_thenReturnsStatus200() throws Exception {
    mvc.perform(get("/change/25/MIN").with(user("admin").password("admin")))
            .andExpect(status().isOk());
  }
  
  @Test
  void whenBillAmountIsValidMax_thenReturnsStatus200() throws Exception {
    mvc.perform(get("/change/10/MAX").with(user("admin").password("admin")))
            .andExpect(status().isOk());
  }

}
