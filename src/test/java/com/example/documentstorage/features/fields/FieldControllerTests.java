package com.example.documentstorage.features.fields;

import com.example.documentstorage.features.fields.requests.UpdateFieldsRequest;
import com.example.documentstorage.shared.base.models.requests.FieldInfo;
import com.example.documentstorage.shared.base.models.responses.DataRecordDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FieldControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addFields_whenAddingTwoNewValidFields_shouldAddFields() throws Exception {
        List<FieldInfo> infos = List.of(
                new FieldInfo("Test 1", "Value 1"),
                new FieldInfo("Test 2", "Value 2")
        );
        UpdateFieldsRequest request = new UpdateFieldsRequest(infos);

        MvcResult result = mockMvc.perform(put("/api/data-records/{id}/fields", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        DataRecordDetail actualResponse = objectMapper.readValue(json, DataRecordDetail.class);

//        assertEquals("Alice", actualResponse.getCustomer());
//        assertEquals(2, actualResponse.getItems().size());
//        assertEquals("Laptop", actualResponse.getItems().get(0).getName());
    }

}
