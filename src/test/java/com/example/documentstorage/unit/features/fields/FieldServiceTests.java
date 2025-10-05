package com.example.documentstorage.unit.features.fields;

import com.example.documentstorage.entities.DataRecord;
import com.example.documentstorage.entities.Field;
import com.example.documentstorage.features.fields.FieldDataRecordRepository;
import com.example.documentstorage.features.fields.FieldServiceImpl;
import com.example.documentstorage.features.fields.requests.RemoveFieldsRequest;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    }

    @Test
    void updateDataRecordFields_whenAddingTwoNewValidFieldsWithExistingFields_shouldAddTwoNewFields() {
        List<FieldInfo> fieldInfos = List.of(
                new FieldInfo("Name 1", "Value 1"),
                new FieldInfo("Name 2", "Value 2")
        );
        UpdateFieldsRequest request = new UpdateFieldsRequest(fieldInfos);
        DataRecord dataRecord = new DataRecord();
        dataRecord.setId(1);
        Field firstExisting = new Field();
        firstExisting.setName("Existing 1");
        firstExisting.setValue("Existing value 1");

        Field secondExisting = new Field();
        secondExisting.setName("Existing 2");
        secondExisting.setValue("Existing value 2");
        dataRecord.setFields(new ArrayList<>(List.of(firstExisting, secondExisting)));
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        DataRecordDetail actualResponse = fieldService.updateDataRecordFields(1, request);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.fields()).isNotNull();
        assertThat(actualResponse.fields()).hasSize(4);
        assertThat(actualResponse.fields())
                .element(0)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Existing 1");
                    assertThat(field.value()).isEqualTo("Existing value 1");
                });

        assertThat(actualResponse.fields())
                .element(1)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Existing 2");
                    assertThat(field.value()).isEqualTo("Existing value 2");
                });

        assertThat(actualResponse.fields())
                .element(2)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Name 1");
                    assertThat(field.value()).isEqualTo("Value 1");
                });

        assertThat(actualResponse.fields())
                .element(3)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Name 2");
                    assertThat(field.value()).isEqualTo("Value 2");
                });
    }

    @Test
    void updateFields_whenAddingOneOverridingFieldWithExistingFields_shouldOverrideExistingField() {
        List<FieldInfo> fieldInfos = List.of(
                new FieldInfo("Existing 1", "Overridden Value")
        );
        UpdateFieldsRequest request = new UpdateFieldsRequest(fieldInfos);
        DataRecord dataRecord = new DataRecord();
        dataRecord.setId(1);
        Field firstExisting = new Field();
        firstExisting.setName("Existing 1");
        firstExisting.setValue("Existing value 1");

        Field secondExisting = new Field();
        secondExisting.setName("Existing 2");
        secondExisting.setValue("Existing value 2");
        dataRecord.setFields(new ArrayList<>(List.of(firstExisting, secondExisting)));
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        DataRecordDetail actualResponse = fieldService.updateDataRecordFields(1, request);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.fields()).isNotNull();
        assertThat(actualResponse.fields()).hasSize(2);
        assertThat(actualResponse.fields())
                .element(0)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Existing 1");
                    assertThat(field.value()).isEqualTo("Overridden Value");
                });

        assertThat(actualResponse.fields())
                .element(1)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Existing 2");
                    assertThat(field.value()).isEqualTo("Existing value 2");
                });
    }

    @Test
    void updateFields_whenAddingOneOverridingFieldAndOneNewFieldWithExistingFields_shouldOverrideExistingFieldAndAddOneNewField() {
        List<FieldInfo> fieldInfos = List.of(
                new FieldInfo("Existing 1", "Overridden Value"),
                new FieldInfo("Name 1", "Value 1")
        );
        UpdateFieldsRequest request = new UpdateFieldsRequest(fieldInfos);
        DataRecord dataRecord = new DataRecord();
        dataRecord.setId(1);
        Field firstExisting = new Field();
        firstExisting.setName("Existing 1");
        firstExisting.setValue("Existing value 1");

        Field secondExisting = new Field();
        secondExisting.setName("Existing 2");
        secondExisting.setValue("Existing value 2");
        dataRecord.setFields(new ArrayList<>(List.of(firstExisting, secondExisting)));
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        DataRecordDetail actualResponse = fieldService.updateDataRecordFields(1, request);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.fields()).isNotNull();
        assertThat(actualResponse.fields()).hasSize(3);
        assertThat(actualResponse.fields())
                .element(0)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Existing 1");
                    assertThat(field.value()).isEqualTo("Overridden Value");
                });

        assertThat(actualResponse.fields())
                .element(1)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Existing 2");
                    assertThat(field.value()).isEqualTo("Existing value 2");
                });

        assertThat(actualResponse.fields())
                .element(2)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Name 1");
                    assertThat(field.value()).isEqualTo("Value 1");
                });
    }

    @Test
    void updateFields_whenAddingOneValidFieldWithExistingFields_shouldReturnOrderedFields() {
        List<FieldInfo> fieldInfos = List.of(
                new FieldInfo("Greater name", "Some value")
        );
        UpdateFieldsRequest request = new UpdateFieldsRequest(fieldInfos);
        DataRecord dataRecord = new DataRecord();
        dataRecord.setId(1);
        Field firstExisting = new Field();
        firstExisting.setName("Existing 1");
        firstExisting.setValue("Existing value 1");

        Field secondExisting = new Field();
        secondExisting.setName("Higher name");
        secondExisting.setValue("Higher value");
        dataRecord.setFields(new ArrayList<>(List.of(firstExisting, secondExisting)));
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        DataRecordDetail actualResponse = fieldService.updateDataRecordFields(1, request);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.fields()).isNotNull();
        assertThat(actualResponse.fields()).hasSize(3);
        assertThat(actualResponse.fields())
                .element(0)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Existing 1");
                    assertThat(field.value()).isEqualTo("Existing value 1");
                });

        assertThat(actualResponse.fields())
                .element(1)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Greater name");
                    assertThat(field.value()).isEqualTo("Some value");
                });

        assertThat(actualResponse.fields())
                .element(2)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Higher name");
                    assertThat(field.value()).isEqualTo("Higher value");
                });
    }

    @Test
    void updateFields_whenAddingOneNewField_shouldStoreFieldInDatabase() {
        List<FieldInfo> fieldInfos = List.of(
                new FieldInfo("Name 1", "Value 1")
        );
        UpdateFieldsRequest request = new UpdateFieldsRequest(fieldInfos);
        DataRecord dataRecord = new DataRecord();
        dataRecord.setId(1);
        dataRecord.setFields(new ArrayList<>());
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        fieldService.updateDataRecordFields(1, request);

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).saveAndFlush(dataRecord);
    }

    @Test
    void removeDataRecordFields_whenRemovingTwoFieldsFromDataRecordWithNoFields_shouldReturnNoFields() {
        List<String> inputFields = List.of("Name 1", "Name 2");
        RemoveFieldsRequest request = new RemoveFieldsRequest(inputFields);
        DataRecord dataRecord = new DataRecord();
        dataRecord.setId(1);
        dataRecord.setFields(new ArrayList<>());
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        DataRecordDetail actualResponse = fieldService.removeDataRecordFields(1, request);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.fields()).isNotNull();
        assertThat(actualResponse.fields()).hasSize(0);
    }

    @Test
    void removeDataRecordFields_whenRemovingNoFieldsFromDataRecordWithFields_shouldReturnExistingFields() {
        List<String> inputFields = List.of();
        RemoveFieldsRequest request = new RemoveFieldsRequest(inputFields);
        DataRecord dataRecord = new DataRecord();
        dataRecord.setId(1);
        Field firstExisting = new Field();
        firstExisting.setName("Existing 1");
        firstExisting.setValue("Existing value 1");

        Field secondExisting = new Field();
        secondExisting.setName("Existing 2");
        secondExisting.setValue("Existing value 2");
        dataRecord.setFields(new ArrayList<>(List.of(firstExisting, secondExisting)));
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        DataRecordDetail actualResponse = fieldService.removeDataRecordFields(1, request);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.fields()).isNotNull();
        assertThat(actualResponse.fields()).hasSize(2);

        assertThat(actualResponse.fields())
                .element(0)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Existing 1");
                    assertThat(field.value()).isEqualTo("Existing value 1");
                });

        assertThat(actualResponse.fields())
                .element(1)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Existing 2");
                    assertThat(field.value()).isEqualTo("Existing value 2");
                });
    }

    @Test
    void removeDataRecordFields_whenRemovingOneValidFieldsFromDataRecordWithFields_shouldRemoveOneField() {
        List<String> inputFields = List.of("Existing 1");
        RemoveFieldsRequest request = new RemoveFieldsRequest(inputFields);
        DataRecord dataRecord = new DataRecord();
        dataRecord.setId(1);
        Field firstExisting = new Field();
        firstExisting.setName("Existing 1");
        firstExisting.setValue("Existing value 1");

        Field secondExisting = new Field();
        secondExisting.setName("Existing 2");
        secondExisting.setValue("Existing value 2");
        dataRecord.setFields(new ArrayList<>(List.of(firstExisting, secondExisting)));
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        DataRecordDetail actualResponse = fieldService.removeDataRecordFields(1, request);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.fields()).isNotNull();
        assertThat(actualResponse.fields()).hasSize(1);

        assertThat(actualResponse.fields())
                .element(0)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Existing 2");
                    assertThat(field.value()).isEqualTo("Existing value 2");
                });
    }

    @Test
    void removeDataRecordFields_whenRemovingOneValidAndOneInvalidFieldsFromDataRecordWithFields_shouldRemoveOneField() {
        List<String> inputFields = List.of("Existing 1", "Invalid field");
        RemoveFieldsRequest request = new RemoveFieldsRequest(inputFields);
        DataRecord dataRecord = new DataRecord();
        dataRecord.setId(1);
        Field firstExisting = new Field();
        firstExisting.setName("Existing 1");
        firstExisting.setValue("Existing value 1");

        Field secondExisting = new Field();
        secondExisting.setName("Existing 2");
        secondExisting.setValue("Existing value 2");
        dataRecord.setFields(new ArrayList<>(List.of(firstExisting, secondExisting)));
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        DataRecordDetail actualResponse = fieldService.removeDataRecordFields(1, request);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.fields()).isNotNull();
        assertThat(actualResponse.fields()).hasSize(1);

        assertThat(actualResponse.fields())
                .element(0)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Existing 2");
                    assertThat(field.value()).isEqualTo("Existing value 2");
                });
    }

    @Test
    void removeDataRecordFields_whenRemovingOneValidFromDataRecordWithFields_shouldReturnOrderedFields() {
        List<String> inputFields = List.of("Existing 1");
        RemoveFieldsRequest request = new RemoveFieldsRequest(inputFields);
        DataRecord dataRecord = new DataRecord();
        dataRecord.setId(1);
        Field firstExisting = new Field();
        firstExisting.setName("Existing 1");
        firstExisting.setValue("Existing value 1");

        Field secondExisting = new Field();
        secondExisting.setName("Existing 2");
        secondExisting.setValue("Existing value 2");

        Field thridField = new Field();
        secondExisting.setName("Existing 3");
        secondExisting.setValue("Existing value 3");
        dataRecord.setFields(new ArrayList<>(List.of(thridField, firstExisting, secondExisting)));
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        DataRecordDetail actualResponse = fieldService.removeDataRecordFields(1, request);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.fields()).isNotNull();
        assertThat(actualResponse.fields()).hasSize(2);

        assertThat(actualResponse.fields())
                .element(0)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Existing 2");
                    assertThat(field.value()).isEqualTo("Existing value 2");
                });

        assertThat(actualResponse.fields())
                .element(1)
                .satisfies(field -> {
                    assertThat(field.name()).isEqualTo("Existing 3");
                    assertThat(field.value()).isEqualTo("Existing value 3");
                });
    }

    @Test
    void removeDataRecordFields_whenRemovingOneValidFieldFromDataRecordWithFields_shouldCallDatabase() {
        List<String> inputFields = List.of("Existing 1");
        RemoveFieldsRequest request = new RemoveFieldsRequest(inputFields);
        DataRecord dataRecord = new DataRecord();
        dataRecord.setId(1);
        Field firstExisting = new Field();
        firstExisting.setName("Existing 1");
        firstExisting.setValue("Existing value 1");

        Field secondExisting = new Field();
        secondExisting.setName("Existing 2");
        secondExisting.setValue("Existing value 2");
        dataRecord.setFields(new ArrayList<>(List.of(firstExisting, secondExisting)));
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        fieldService.removeDataRecordFields(1, request);

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).saveAndFlush(dataRecord);
    }
}
