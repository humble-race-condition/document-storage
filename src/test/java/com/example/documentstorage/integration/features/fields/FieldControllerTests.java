package com.example.documentstorage.integration.features.fields;

import com.example.documentstorage.features.fields.requests.RemoveFieldsRequest;
import com.example.documentstorage.features.fields.requests.UpdateFieldsRequest;
import com.example.documentstorage.shared.base.models.requests.FieldInfo;
import com.example.documentstorage.shared.base.models.responses.DataRecordDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Sql("/fields-test.sql")
class FieldControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void updateFields_whenSubmittingEmptyFieldName_shouldReturnError() throws Exception {
        List<FieldInfo> infos = List.of(
                new FieldInfo(null, "value")
        );
        UpdateFieldsRequest request = new UpdateFieldsRequest(infos);

        MvcResult result = mockMvc.perform(put("/api/data-records/{id}/fields", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        ProblemDetail actualResponse = objectMapper.readValue(json, ProblemDetail.class);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getStatus()).isEqualTo(400);
        assertThat(actualResponse.getTitle()).isEqualTo("You have entered invalid data");
        assertThat(actualResponse.getDetail()).isEqualTo("You have entered invalid data");
        assertThat(actualResponse.getProperties()).isNotNull();
        assertThat(actualResponse.getProperties().get("code")).isEqualTo("VALIDATION_ERROR_MESSAGE");
        assertThat(actualResponse.getProperties().get("timestamp")).isNotNull();
        assertThat(actualResponse.getProperties().get("errors")).isNotNull();
        assertThat(actualResponse.getProperties().get("errors"))
                .asInstanceOf(InstanceOfAssertFactories.list(String.class))
                .hasSize(1)
                .containsExactly("The field info name can not be empty");
    }

    @Test
    void updateFields_whenSubmittingEmptyFieldValue_shouldReturnError() throws Exception {
        List<FieldInfo> infos = List.of(
                new FieldInfo("name", null)
        );
        UpdateFieldsRequest request = new UpdateFieldsRequest(infos);

        MvcResult result = mockMvc.perform(put("/api/data-records/{id}/fields", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        ProblemDetail actualResponse = objectMapper.readValue(json, ProblemDetail.class);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getStatus()).isEqualTo(400);
        assertThat(actualResponse.getTitle()).isEqualTo("You have entered invalid data");
        assertThat(actualResponse.getDetail()).isEqualTo("You have entered invalid data");
        assertThat(actualResponse.getProperties()).isNotNull();
        assertThat(actualResponse.getProperties().get("code")).isEqualTo("VALIDATION_ERROR_MESSAGE");
        assertThat(actualResponse.getProperties().get("timestamp")).isNotNull();
        assertThat(actualResponse.getProperties().get("errors")).isNotNull();
        assertThat(actualResponse.getProperties().get("errors"))
                .asInstanceOf(InstanceOfAssertFactories.list(String.class))
                .hasSize(1)
                .containsExactly("The field info value can not be empty");
    }

    @Test
    void updateFields_whenSubmittingEmptyFieldNameAndValue_shouldReturnTwoErrors() throws Exception {
        List<FieldInfo> infos = List.of(
                new FieldInfo(null, null)
        );
        UpdateFieldsRequest request = new UpdateFieldsRequest(infos);

        MvcResult result = mockMvc.perform(put("/api/data-records/{id}/fields", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        ProblemDetail actualResponse = objectMapper.readValue(json, ProblemDetail.class);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getStatus()).isEqualTo(400);
        assertThat(actualResponse.getTitle()).isEqualTo("You have entered invalid data");
        assertThat(actualResponse.getDetail()).isEqualTo("You have entered invalid data");
        assertThat(actualResponse.getProperties()).isNotNull();
        assertThat(actualResponse.getProperties().get("code")).isEqualTo("VALIDATION_ERROR_MESSAGE");
        assertThat(actualResponse.getProperties().get("timestamp")).isNotNull();
        assertThat(actualResponse.getProperties().get("errors")).isNotNull();
        assertThat(actualResponse.getProperties().get("errors"))
                .asInstanceOf(InstanceOfAssertFactories.list(String.class))
                .hasSize(2)
                .containsAll(List.of("The field info value can not be empty", "The field info value can not be empty"));
    }

    @Test
    void updateFields_whenAddingNoNewValidFieldsWithNoExistingFields_shouldAddNoNewFields() throws Exception {
        List<FieldInfo> infos = List.of();
        UpdateFieldsRequest request = new UpdateFieldsRequest(infos);

        MvcResult result = mockMvc.perform(put("/api/data-records/{id}/fields", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        DataRecordDetail actualResponse = objectMapper.readValue(json, DataRecordDetail.class);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.id()).isEqualTo(3);
        assertThat(actualResponse.title()).isEqualTo("Storage bill");
        assertThat(actualResponse.description()).isEqualTo("A bill for storage");
        assertThat(actualResponse.sections())
                .isNotNull()
                .hasSize(0);
        assertThat(actualResponse.fields())
                .isNotNull()
                .hasSize(0);
    }

    @Test
    void updateFields_whenAddingTwoNewFieldsWithNoExistingFields_shouldAddTwoFields() throws Exception {
        List<FieldInfo> infos = List.of(
                new FieldInfo("Test 1", "Value 1"),
                new FieldInfo("Test 2", "Value 2")
        );
        UpdateFieldsRequest request = new UpdateFieldsRequest(infos);

        MvcResult result = mockMvc.perform(put("/api/data-records/{id}/fields", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        DataRecordDetail actualResponse = objectMapper.readValue(json, DataRecordDetail.class);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.id()).isEqualTo(3);
        assertThat(actualResponse.title()).isEqualTo("Storage bill");
        assertThat(actualResponse.description()).isEqualTo("A bill for storage");
        assertThat(actualResponse.sections())
                .isNotNull()
                .hasSize(0);
        assertThat(actualResponse.fields())
                .isNotNull()
                .hasSize(2);

        assertThat(actualResponse.fields())
                .element(0)
                .satisfies(f -> {
                    assertThat(f.name()).isEqualTo("Test 1");
                    assertThat(f.value()).isEqualTo("Value 1");
                });

        assertThat(actualResponse.fields())
                .element(1)
                .satisfies(f -> {
                    assertThat(f.name()).isEqualTo("Test 2");
                    assertThat(f.value()).isEqualTo("Value 2");
                });
    }

    @Test
    void updateFields_whenAddingTwoNewValidFieldsWithExistingFields_shouldAddTwoNewFields() throws Exception {
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

        assertThat(actualResponse.fields())
                .element(0)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Alpha");
                    assertThat(field.value()).isEqualTo("Beta");
                });

        assertThat(actualResponse.fields())
                .element(1)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Beneficiary");
                    assertThat(field.value()).isEqualTo("TODOR GOGOV");
                });

        assertThat(actualResponse.fields())
                .element(2)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("IBAN");
                    assertThat(field.value()).isEqualTo("112233");
                });

        assertThat(actualResponse.fields())
                .element(3)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Test 1");
                    assertThat(field.value()).isEqualTo("Value 1");
                });

        assertThat(actualResponse.fields())
                .element(4)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Test 2");
                    assertThat(field.value()).isEqualTo("Value 2");
                });
    }

    @Test
    void updateFields_whenAddingOneOverridingFieldWithExistingFields_shouldOverrideExistingField() throws Exception {
        List<FieldInfo> infos = List.of(new FieldInfo("IBAN", "OVERRIDDEN"));
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
                .hasSize(3);

        assertThat(actualResponse.fields())
                .element(0)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Alpha");
                    assertThat(field.value()).isEqualTo("Beta");
                });

        assertThat(actualResponse.fields())
                .element(1)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Beneficiary");
                    assertThat(field.value()).isEqualTo("TODOR GOGOV");
                });

        assertThat(actualResponse.fields())
                .element(2)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("IBAN");
                    assertThat(field.value()).isEqualTo("OVERRIDDEN");
                });
    }

    @Test
    void updateFields_whenAddingOneOverridingFieldAndOneNewFieldWithExistingFields_shouldOverrideExistingFieldAndAddOneNewField() throws Exception {
        List<FieldInfo> infos = List.of(
                new FieldInfo("IBAN", "OVERRIDDEN"),
                new FieldInfo("Test 1", "Value 1")
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
                .hasSize(4);

        assertThat(actualResponse.fields())
                .element(0)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Alpha");
                    assertThat(field.value()).isEqualTo("Beta");
                });

        assertThat(actualResponse.fields())
                .element(1)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Beneficiary");
                    assertThat(field.value()).isEqualTo("TODOR GOGOV");
                });

        assertThat(actualResponse.fields())
                .element(2)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("IBAN");
                    assertThat(field.value()).isEqualTo("OVERRIDDEN");
                });

        assertThat(actualResponse.fields())
                .element(3)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Test 1");
                    assertThat(field.value()).isEqualTo("Value 1");
                });
    }

    @Test
    void updateFields_whenAddingOneNewField_shouldStoreFieldInDatabase() throws Exception {
        List<FieldInfo> infos = List.of(
                new FieldInfo("Test count", "1234")
        );
        UpdateFieldsRequest request = new UpdateFieldsRequest(infos);

        mockMvc.perform(put("/api/data-records/{id}/fields", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM fields WHERE data_record_id = 3", Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @Test
    void removeFields_whenFieldNameIsEmpty_shouldReturnError() throws Exception {
        List<String> infos = new ArrayList<>();
        infos.add(null);
        RemoveFieldsRequest request = new RemoveFieldsRequest(infos);

        MvcResult result = mockMvc.perform(delete("/api/data-records/{id}/fields", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        ProblemDetail actualResponse = objectMapper.readValue(json, ProblemDetail.class);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getStatus()).isEqualTo(400);
        assertThat(actualResponse.getTitle()).isEqualTo("You have entered invalid data");
        assertThat(actualResponse.getDetail()).isEqualTo("You have entered invalid data");
        assertThat(actualResponse.getProperties()).isNotNull();
        assertThat(actualResponse.getProperties().get("code")).isEqualTo("VALIDATION_ERROR_MESSAGE");
        assertThat(actualResponse.getProperties().get("timestamp")).isNotNull();
        assertThat(actualResponse.getProperties().get("errors")).isNotNull();
        assertThat(actualResponse.getProperties().get("errors"))
                .asInstanceOf(InstanceOfAssertFactories.list(String.class))
                .hasSize(1)
                .containsExactly("The field name is empty");
    }

    @Test
    void removeFields_whenRemovingTwoFieldsFromDataRecordWithNoFields_shouldReturnNoFields() throws Exception {
        List<String> infos = List.of(
                "Key 1",
                "Key 2"
        );
        RemoveFieldsRequest request = new RemoveFieldsRequest(infos);

        MvcResult result = mockMvc.perform(delete("/api/data-records/{id}/fields", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        DataRecordDetail actualResponse = objectMapper.readValue(json, DataRecordDetail.class);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.id()).isEqualTo(3);
        assertThat(actualResponse.title()).isEqualTo("Storage bill");
        assertThat(actualResponse.description()).isEqualTo("A bill for storage");
        assertThat(actualResponse.sections())
                .isNotNull()
                .hasSize(0);
        assertThat(actualResponse.fields())
                .isNotNull()
                .hasSize(0);
    }

    @Test
    void removeFields_whenRemovingNoFieldsFromDataRecordWithFields_shouldReturnExistingFields() throws Exception {
        List<String> infos = List.of();
        RemoveFieldsRequest request = new RemoveFieldsRequest(infos);

        MvcResult result = mockMvc.perform(delete("/api/data-records/{id}/fields", 1)
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
                .hasSize(3);

        assertThat(actualResponse.fields())
                .element(0)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Alpha");
                    assertThat(field.value()).isEqualTo("Beta");
                });

        assertThat(actualResponse.fields())
                .element(1)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Beneficiary");
                    assertThat(field.value()).isEqualTo("TODOR GOGOV");
                });

        assertThat(actualResponse.fields())
                .element(2)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("IBAN");
                    assertThat(field.value()).isEqualTo("112233");
                });
    }

    @Test
    void removeFields_whenRemovingOneValidFieldsFromDataRecordWithFields_shouldRemoveOneField() throws Exception {
        List<String> infos = List.of(
                "IBAN"
        );
        RemoveFieldsRequest request = new RemoveFieldsRequest(infos);

        MvcResult result = mockMvc.perform(delete("/api/data-records/{id}/fields", 1)
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
                .hasSize(2);

        assertThat(actualResponse.fields())
                .element(0)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Alpha");
                    assertThat(field.value()).isEqualTo("Beta");
                });

        assertThat(actualResponse.fields())
                .element(1)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Beneficiary");
                    assertThat(field.value()).isEqualTo("TODOR GOGOV");
                });
    }

    @Test
    void removeFields_whenRemovingOneValidAndOneInvalidFieldsFromDataRecordWithFields_shouldRemoveOneField() throws Exception {
        List<String> infos = List.of(
                "Beneficiary",
                "Demo"
        );

        RemoveFieldsRequest request = new RemoveFieldsRequest(infos);

        MvcResult result = mockMvc.perform(delete("/api/data-records/{id}/fields", 1)
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
                .hasSize(2);

        assertThat(actualResponse.fields())
                .element(0)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Alpha");
                    assertThat(field.value()).isEqualTo("Beta");
                });

        assertThat(actualResponse.fields())
                .element(1)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("IBAN");
                    assertThat(field.value()).isEqualTo("112233");
                });
    }

    @Test
    void removeFields_whenRemovingOneValidFieldFromDataRecordWithFields_shouldRemoveFieldFromDatabase() throws Exception {
        List<String> infos = List.of(
                "IBAN"
        );
        RemoveFieldsRequest request = new RemoveFieldsRequest(infos);

        mockMvc.perform(delete("/api/data-records/{id}/fields", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());


        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM fields WHERE data_record_id = 1", Integer.class);
        assertThat(count).isEqualTo(2);
    }
}
