package com.example.documentstorage.unit.features.fields;

import com.example.documentstorage.entities.DataRecord;
import com.example.documentstorage.entities.Field;
import com.example.documentstorage.entities.Section;
import com.example.documentstorage.features.datarecords.DataRecordRepository;
import com.example.documentstorage.features.datarecords.DataRecordServiceImpl;
import com.example.documentstorage.shared.base.exceptions.InvalidClientInputException;
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
class DataRecordServiceTests {

    @Mock
    private DataRecordRepository repository;

    @InjectMocks
    private DataRecordServiceImpl dataRecordService;

    @Test
    void getDataRecordById_whenNoDataRecord_shouldThrowException() {

        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dataRecordService.getDataRecordById(1))
                .isInstanceOf(InvalidClientInputException.class)
                .hasMessage("Key for message 'features.fields.on.update.datarecord.datarecord.not.found'");

        verify(repository, times(1)).findById(1);
    }

    @Test
    void getDataRecordById_whenValidDataRecordSelected_shouldGetDataRecord() {
        DataRecord dataRecord = new DataRecord();
        dataRecord.setId(1);
        dataRecord.setTitle("Actual title");
        dataRecord.setDescription("Actual description");
        Field firstExisting = new Field();
        firstExisting.setId(31);
        firstExisting.setName("Existing 1");
        firstExisting.setValue("Existing value 1");

        Field secondExisting = new Field();
        secondExisting.setId(35);
        secondExisting.setName("Existing 2");
        secondExisting.setValue("Existing value 2");
        dataRecord.setFields(new ArrayList<>(List.of(firstExisting, secondExisting)));

        Section section = new Section();
        section.setId(15);
        section.setFileName("orange-text-book.txt");
        section.setStorageLocation("path/orange-text-book.txt");
        section.setContentType("text/plain");
        dataRecord.setSections(new ArrayList<>(List.of(section)));

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        DataRecordDetail actualDataRecord = dataRecordService.getDataRecordById(1);

        assertThat(actualDataRecord).isNotNull();
        assertThat(actualDataRecord.id()).isEqualTo(1);
        assertThat(actualDataRecord.title()).isEqualTo("Actual title");
        assertThat(actualDataRecord.description()).isEqualTo("Actual description");
        assertThat(actualDataRecord.fields()).isNotNull();
        assertThat(actualDataRecord.fields()).hasSize(2);
        assertThat(actualDataRecord.fields())
                .element(0)
                .satisfies(field -> {
                    assertThat(field.id()).isEqualTo(31);
                    assertThat(field.name()).isEqualTo("Existing 1");
                    assertThat(field.value()).isEqualTo("Existing value 1");
                });

        assertThat(actualDataRecord.fields())
                .element(1)
                .satisfies(field -> {
                    assertThat(field.id()).isEqualTo(35);
                    assertThat(field.name()).isEqualTo("Existing 2");
                    assertThat(field.value()).isEqualTo("Existing value 2");
                });

        assertThat(actualDataRecord.sections()).isNotNull();
        assertThat(actualDataRecord.sections()).hasSize(1);
        assertThat(actualDataRecord.sections())
                .element(0)
                .satisfies(sectionDetail -> {
                    assertThat(sectionDetail.id()).isEqualTo(15);
                    assertThat(sectionDetail.fileName()).isEqualTo("orange-text-book.txt");
                    assertThat(sectionDetail.storageLocation()).isEqualTo("path/orange-text-book.txt");
                });
    }
}
