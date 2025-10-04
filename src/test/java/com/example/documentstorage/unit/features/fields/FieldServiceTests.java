package com.example.documentstorage.unit.features.fields;

import com.example.documentstorage.entities.DataRecord;
import com.example.documentstorage.features.fields.FieldDataRecordRepository;
import com.example.documentstorage.features.fields.FieldServiceImpl;
import com.example.documentstorage.features.fields.requests.UpdateFieldsRequest;
import com.example.documentstorage.shared.base.exceptions.InvalidClientInputException;
import com.example.documentstorage.shared.base.models.requests.FieldInfo;
import com.example.documentstorage.shared.base.models.responses.DataRecordDetail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class FieldServiceTests {

    @Mock
    private FieldDataRecordRepository repository;

    @InjectMocks
    private FieldServiceImpl fieldService;

    @Test
    void updateDataRecordFields_whenNoDataRecord_shouldThrowException() {
        List<FieldInfo> fieldInfos = List.of();
        UpdateFieldsRequest request = new UpdateFieldsRequest(fieldInfos);

        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fieldService.updateDataRecordFields(1, request))
                .isInstanceOf(InvalidClientInputException.class)
                .hasMessage("Key for message 'features.fields.on.update.datarecord.datarecord.not.found'");
        //ToDO order on every test
        verify(repository, times(1)).findById(1);
    }

    @Test
    void updateDataRecordFields_whenAddingNoNewValidFieldsWithNoExistingFields_shouldAddNoNewFields() {
        List<FieldInfo> fieldInfos = List.of();
        UpdateFieldsRequest request = new UpdateFieldsRequest(fieldInfos);
        DataRecord dataRecord = new DataRecord();
        dataRecord.setId(1);
        dataRecord.setFields(List.of());
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        DataRecordDetail response = fieldService.updateDataRecordFields(1, request);

        assertThat(response).isNotNull();
        assertThat(response.fields()).isNotNull();
        assertThat(response.fields()).isEmpty();

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).saveAndFlush(dataRecord);
    }

    @Test
    void updateDataRecordFields_whenAddingTwoNewFieldsWithNoExistingFields_shouldAddTwoFields() {
        List<FieldInfo> fieldInfos = List.of(
                new FieldInfo("Name 1", "Value 1"),
                new FieldInfo("Name 2", "Value 2")
        );
        UpdateFieldsRequest request = new UpdateFieldsRequest(fieldInfos);
        DataRecord dataRecord = new DataRecord();
        dataRecord.setId(1);
        dataRecord.setFields(new ArrayList<>());
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        DataRecordDetail actualResponse = fieldService.updateDataRecordFields(1, request);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.fields()).isNotNull();
        assertThat(actualResponse.fields()).hasSize(2);
        assertThat(actualResponse.fields())
                //ToDo order on every test
                .anySatisfy(field -> {
                    assertThat(field.name()).isEqualTo("Name 1");
                    assertThat(field.value()).isEqualTo("Value 1");
                });

        assertThat(actualResponse.fields())
                .anySatisfy(field -> {
                    assertThat(field.name()).isEqualTo("Name 2");
                    assertThat(field.value()).isEqualTo("Value 2");
                });

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).saveAndFlush(dataRecord);
    }

    @Test
    void updateDataRecordFields_whenAddingTwoNewFieldsWithSameNameAndWithNoExistingFields_shouldAddOneField() {
        List<FieldInfo> fieldInfos = List.of(
                new FieldInfo("Name 1", "Value 1"),
                new FieldInfo("Name 1", "Value 2")
        );
        UpdateFieldsRequest request = new UpdateFieldsRequest(fieldInfos);
        DataRecord dataRecord = new DataRecord();
        dataRecord.setId(1);
        dataRecord.setFields(new ArrayList<>());
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        DataRecordDetail actualResponse = fieldService.updateDataRecordFields(1, request);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.fields()).isNotNull();
        assertThat(actualResponse.fields()).hasSize(1);
        assertThat(actualResponse.fields())
                .element(0)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Name 1");
                    assertThat(field.value()).isEqualTo("Value 2");
                });

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).saveAndFlush(dataRecord);
    }

    @Test
    void updateDataRecordFields_whenAddingTwoNewValidFieldsWithExistingFields_shouldAddTwoNewFields() {

    }

    @Test
    void updateFields_whenAddingOneOverridingFieldWithExistingFields_shouldOverrideExistingField() {

    }

    @Test
    void updateDataRecordFields_whenAddingOneOverridingFieldAndOneNewFieldWithExistingFields_shouldOverrideExistingFieldAndAddOneNewField() {

    }

    @Test
    void updateFields_whenAddingOneNewField_shouldStoreFieldInDatabase() {

    }

    @Test
    void removeFields_whenFieldNameIsEmpty_shouldReturnError() {

    }

    @Test
    void removeFields_whenRemovingTwoFieldsFromDataRecordWithNoFields_shouldReturnNoFields() {

    }

    @Test
    void removeFields_whenRemovingNoFieldsFromDataRecordWithFields_shouldReturnExistingFields() {

    }

    @Test
    void removeFields_whenRemovingOneValidFieldsFromDataRecordWithFields_shouldRemoveOneField() {
    }

    @Test
    void removeFields_whenRemovingOneValidAndOneInvalidFieldsFromDataRecordWithFields_shouldRemoveOneField() {

    }

    @Test
    void removeFields_whenRemovingOneValidFieldFromDataRecordWithFields_shouldRemoveFieldFromDatabase() {

    }

}
