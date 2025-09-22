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

import static org.assertj.core.api.Assertions.assertThat;
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
    void addFields_whenAddingTwoNewValidFields_shouldAddTwoNewFields() throws Exception {
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

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.id()).isEqualTo(1);
        assertThat(actualResponse.title()).isEqualTo("Payment note");
        assertThat(actualResponse.description()).isEqualTo("A payment note");
        assertThat(actualResponse.sections())
                .isNotNull()
                .hasSize(0);
        assertThat(actualResponse.fields())
                .isNotNull()
                .hasSize(5);
        assertThat(actualResponse.fields().get(0).id()).isEqualTo(1);
        assertThat(actualResponse.fields().get(0).name()).isEqualTo("IBAN");
        assertThat(actualResponse.fields().get(0).value()).isEqualTo("112233");
        assertThat(actualResponse.fields().get(1).id()).isEqualTo(2);
        assertThat(actualResponse.fields().get(1).name()).isEqualTo("Beneficiary");
        assertThat(actualResponse.fields().get(1).value()).isEqualTo("TODOR GOGOV");
        assertThat(actualResponse.fields().get(2).id()).isEqualTo(3);
        assertThat(actualResponse.fields().get(2).name()).isEqualTo("Alpha");
        assertThat(actualResponse.fields().get(2).value()).isEqualTo("Beta");
        assertThat(actualResponse.fields().get(3).id()).isEqualTo(6);
        assertThat(actualResponse.fields().get(3).name()).isEqualTo("Test 1");
        assertThat(actualResponse.fields().get(3).value()).isEqualTo("Value 1");
        assertThat(actualResponse.fields().get(4).id()).isEqualTo(4);
        assertThat(actualResponse.fields().get(4).name()).isEqualTo("Test 2");
        assertThat(actualResponse.fields().get(4).value()).isEqualTo("Value 2");
        //ToDo fix assertions. Remove id check. Pointless.
    }

}
