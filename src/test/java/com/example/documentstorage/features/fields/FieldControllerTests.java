package com.example.documentstorage.features.fields;

import com.example.documentstorage.features.fields.requests.UpdateFieldsRequest;
import com.example.documentstorage.shared.base.errorresponse.ErrorResponse;
import com.example.documentstorage.shared.base.models.requests.FieldInfo;
import com.example.documentstorage.shared.base.models.responses.DataRecordDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class FieldControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void addFields_whenSubmittingEmptyFieldName_shouldReturnError() throws Exception {
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
        ErrorResponse actualResponse = objectMapper.readValue(json, ErrorResponse.class);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.errors()).hasSize(1);
        assertThat(actualResponse.errors())
                .anySatisfy(e -> {
                    assertThat(e.code()).isEqualTo("1000");
                    assertThat(e.message()).isEqualTo("The field name must not be blank");
                });
    }

    @Test
    void addFields_whenSubmittingEmptyFieldValue_shouldReturnError() throws Exception {
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
        ErrorResponse actualResponse = objectMapper.readValue(json, ErrorResponse.class);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.errors()).hasSize(1);
        assertThat(actualResponse.errors())
                .anySatisfy(e -> {
                    assertThat(e.code()).isEqualTo("1000");
                    assertThat(e.message()).isEqualTo("The field value must not be blank");
                });
    }

    @Test
    void addFields_whenSubmittingEmptyFieldNameAndValue_shouldReturnTwoErrors() throws Exception {
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
        ErrorResponse actualResponse = objectMapper.readValue(json, ErrorResponse.class);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.errors()).hasSize(2);

        assertThat(actualResponse.errors())
                .allSatisfy(s -> assertThat(s.code()).isEqualTo("1000"));

        assertThat(actualResponse.errors())
                .anySatisfy(s -> assertThat(s.message()).isEqualTo("The field value must not be blank"))
                .anySatisfy(s -> assertThat(s.message()).isEqualTo("The field name must not be blank"));
    }

    @Test
    void addFields_whenAddingNoNewValidFieldsWithNoExistingFields_shouldAddNoNewFields() throws Exception {
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
    void addFields_whenAddingTwoNewFieldWithNoExistingFields_shouldAddTwoFields() throws Exception {
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
                .anySatisfy(field -> {
                    assertThat(field.name()).isEqualTo("Test 1");
                    assertThat(field.value()).isEqualTo("Value 1");
                });

        assertThat(actualResponse.fields())
                .anySatisfy(field -> {
                    assertThat(field.name()).isEqualTo("Test 2");
                    assertThat(field.value()).isEqualTo("Value 2");
                });
    }

    @Test
    void addFields_whenAddingTwoNewValidFieldsWithNoExistingFields_shouldAddTwoNewFields() throws Exception {
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
                .anySatisfy(field -> {
                    assertThat(field.name()).isEqualTo("Test 1");
                    assertThat(field.value()).isEqualTo("Value 1");
                });

        assertThat(actualResponse.fields())
                .anySatisfy(field -> {
                    assertThat(field.name()).isEqualTo("Test 2");
                    assertThat(field.value()).isEqualTo("Value 2");
                });
    }

    @Test
    void addFields_whenAddingTwoNewValidFieldsWithExistingFields_shouldAddTwoNewFields() throws Exception {
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
                .anySatisfy(field -> {
                    assertThat(field.name()).isEqualTo("IBAN");
                    assertThat(field.value()).isEqualTo("112233");
                });

        assertThat(actualResponse.fields())
                .anySatisfy(field -> {
                    assertThat(field.name()).isEqualTo("Beneficiary");
                    assertThat(field.value()).isEqualTo("TODOR GOGOV");
                });

        assertThat(actualResponse.fields())
                .anySatisfy(field -> {
                    assertThat(field.name()).isEqualTo("Alpha");
                    assertThat(field.value()).isEqualTo("Beta");
                });

        assertThat(actualResponse.fields())
                .anySatisfy(field -> {
                    assertThat(field.name()).isEqualTo("Test 1");
                    assertThat(field.value()).isEqualTo("Value 1");
                });

        assertThat(actualResponse.fields())
                .anySatisfy(field -> {
                    assertThat(field.name()).isEqualTo("Test 2");
                    assertThat(field.value()).isEqualTo("Value 2");
                });
    }

    @Test
    void addFields_whenAddingOneOverridingFieldWithExistingFields_shouldOverrideExistingField() throws Exception {
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
                .anySatisfy(field -> {
                    assertThat(field.name()).isEqualTo("IBAN");
                    assertThat(field.value()).isEqualTo("OVERRIDDEN");
                });

        assertThat(actualResponse.fields())
                .anySatisfy(field -> {
                    assertThat(field.name()).isEqualTo("Beneficiary");
                    assertThat(field.value()).isEqualTo("TODOR GOGOV");
                });

        assertThat(actualResponse.fields())
                .anySatisfy(field -> {
                    assertThat(field.name()).isEqualTo("Alpha");
                    assertThat(field.value()).isEqualTo("Beta");
                });
    }

    @Test
    void addFields_whenAddingOneOverridingFieldAndOneNewFieldWithExistingFields_shouldOverrideExistingFieldAndAddOneNewField() throws Exception {
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
                .anySatisfy(field -> {
                    assertThat(field.name()).isEqualTo("IBAN");
                    assertThat(field.value()).isEqualTo("OVERRIDDEN");
                });

        assertThat(actualResponse.fields())
                .anySatisfy(field -> {
                    assertThat(field.name()).isEqualTo("Beneficiary");
                    assertThat(field.value()).isEqualTo("TODOR GOGOV");
                });

        assertThat(actualResponse.fields())
                .anySatisfy(field -> {
                    assertThat(field.name()).isEqualTo("Alpha");
                    assertThat(field.value()).isEqualTo("Beta");
                });

        assertThat(actualResponse.fields())
                .anySatisfy(field -> {
                    assertThat(field.name()).isEqualTo("Test 1");
                    assertThat(field.value()).isEqualTo("Value 1");
                });
    }

    //ToDo manual database inserts for each test. No data.sql.
    //ToDo no transactional on tests - bugs can happen due to it.
    @Test
    void addFields_whenAddingOneNewField_shouldStoreFieldInDatabase() throws Exception {
        List<FieldInfo> infos = List.of(
                new FieldInfo("Test count", "1234")
        );
        UpdateFieldsRequest request = new UpdateFieldsRequest(infos);

        mockMvc.perform(put("/api/data-records/{id}/fields", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM fields WHERE data_record_id = 3", Integer.class);
        assertThat(count).isEqualTo(1);
    }
}
