package com.example.documentstorage.unit.features.fields;

import com.example.documentstorage.entities.DataRecord;
import com.example.documentstorage.entities.Field;
import com.example.documentstorage.entities.Section;
import com.example.documentstorage.features.datarecords.DataRecordRepository;
import com.example.documentstorage.features.datarecords.DataRecordServiceImpl;
import com.example.documentstorage.features.datarecords.requests.CreateDataRecordRequest;
import com.example.documentstorage.features.datarecords.requests.UpdateDataRecordRequest;
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
import static org.mockito.ArgumentMatchers.any;
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
                .hasMessage("Key for message 'features.datarecords.on.datarecord.get.by.id.datarecord.not.found'");

        verify(repository, times(1)).findById(1);
    }

    @Test
    void getDataRecordById_whenValidDataRecordSelected_shouldGetDataRecord() {

        Field firstExistingField = createField(31, "Existing 1", "Existing value 1");
        Field secondExistingField = createField(35, "Existing 2", "Existing value 2");
        Section section = createSection(15, "orange-text-book.txt", "path/orange-text-book.txt", "text/plain");
        DataRecord dataRecord = createDataRecord(2,
                "Random title",
                "Random description",
                new ArrayList<>(List.of(firstExistingField, secondExistingField)),
                new ArrayList<>(List.of(section)));

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        DataRecordDetail actualDataRecord = dataRecordService.getDataRecordById(1);

        assertThat(actualDataRecord).isNotNull();
        assertThat(actualDataRecord.id()).isEqualTo(2);
        assertThat(actualDataRecord.title()).isEqualTo("Random title");
        assertThat(actualDataRecord.description()).isEqualTo("Random description");
        assertThat(actualDataRecord.fields()).isNotNull();
        assertThat(actualDataRecord.fields()).hasSize(2);
        assertThat(actualDataRecord.fields()).element(0).satisfies(field -> {
            assertThat(field.id()).isEqualTo(31);
            assertThat(field.name()).isEqualTo("Existing 1");
            assertThat(field.value()).isEqualTo("Existing value 1");
        });

        assertThat(actualDataRecord.fields()).element(1).satisfies(field -> {
            assertThat(field.id()).isEqualTo(35);
            assertThat(field.name()).isEqualTo("Existing 2");
            assertThat(field.value()).isEqualTo("Existing value 2");
        });

        assertThat(actualDataRecord.sections()).isNotNull();
        assertThat(actualDataRecord.sections()).hasSize(1);
        assertThat(actualDataRecord.sections()).element(0).satisfies(sectionDetail -> {
            assertThat(sectionDetail.id()).isEqualTo(15);
            assertThat(sectionDetail.fileName()).isEqualTo("orange-text-book.txt");
            assertThat(sectionDetail.storageLocation()).isEqualTo("path/orange-text-book.txt");
        });
    }

    @Test
    void getDataRecordById_whenValidDataRecordSelectedWithMultipleFields_shouldReturnDataRecordWithOrderedFields() {
        Field firstExistingField = createField(31, "Existing 1", "Existing value 1");
        Field secondExistingField = createField(35, "Existing 2", "Existing value 2");
        Field thrirdExistingField = createField(40, "Existing 3", "Existing value 3");
        DataRecord dataRecord = createDataRecord(1,
                "Actual title",
                "Actual description",
                new ArrayList<>(List.of(thrirdExistingField, firstExistingField, secondExistingField)),
                new ArrayList<>());

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        DataRecordDetail actualDataRecord = dataRecordService.getDataRecordById(1);

        assertThat(actualDataRecord).isNotNull();
        assertThat(actualDataRecord.id()).isEqualTo(1);
        assertThat(actualDataRecord.title()).isEqualTo("Actual title");
        assertThat(actualDataRecord.description()).isEqualTo("Actual description");
        assertThat(actualDataRecord.sections()).isNotNull();
        assertThat(actualDataRecord.sections()).isEmpty();
        assertThat(actualDataRecord.fields()).isNotNull();
        assertThat(actualDataRecord.fields()).hasSize(3);
        assertThat(actualDataRecord.fields()).element(0).satisfies(field -> {
            assertThat(field.id()).isEqualTo(31);
            assertThat(field.name()).isEqualTo("Existing 1");
            assertThat(field.value()).isEqualTo("Existing value 1");
        });

        assertThat(actualDataRecord.fields()).element(1).satisfies(field -> {
            assertThat(field.id()).isEqualTo(35);
            assertThat(field.name()).isEqualTo("Existing 2");
            assertThat(field.value()).isEqualTo("Existing value 2");
        });

        assertThat(actualDataRecord.fields()).element(2).satisfies(field -> {
            assertThat(field.id()).isEqualTo(40);
            assertThat(field.name()).isEqualTo("Existing 3");
            assertThat(field.value()).isEqualTo("Existing value 3");
        });
    }

    @Test
    void getDataRecordById_whenValidDataRecordSelectedWithMultipleSections_shouldReturnDataRecordWithOrderedSections() {
        Section firstSection = createSection(15, "orange-text-book.txt", "path/orange-text-book.txt", "text/plain");
        Section secondSection = createSection(16, "purple-text-book.txt", "path/purple-text-book.txt", "application/pdf");
        Section thirdSection = createSection(17, "red-text-book.txt", "path/red-text-book.txt", "application/xml");
        DataRecord dataRecord = createDataRecord(1,
                "Actual title",
                "Actual description",
                new ArrayList<>(),
                new ArrayList<>(List.of(thirdSection, firstSection, secondSection)));

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        DataRecordDetail actualDataRecord = dataRecordService.getDataRecordById(1);

        assertThat(actualDataRecord).isNotNull();
        assertThat(actualDataRecord.id()).isEqualTo(1);
        assertThat(actualDataRecord.title()).isEqualTo("Actual title");
        assertThat(actualDataRecord.description()).isEqualTo("Actual description");
        assertThat(actualDataRecord.fields()).isNotNull();
        assertThat(actualDataRecord.fields()).isEmpty();
        assertThat(actualDataRecord.sections()).isNotNull();
        assertThat(actualDataRecord.sections()).hasSize(3);
        assertThat(actualDataRecord.sections()).element(0).satisfies(sectionDetail -> {
            assertThat(sectionDetail.id()).isEqualTo(15);
            assertThat(sectionDetail.fileName()).isEqualTo("orange-text-book.txt");
            assertThat(sectionDetail.storageLocation()).isEqualTo("path/orange-text-book.txt");
        });

        assertThat(actualDataRecord.sections()).element(1).satisfies(sectionDetail -> {
            assertThat(sectionDetail.id()).isEqualTo(16);
            assertThat(sectionDetail.fileName()).isEqualTo("purple-text-book.txt");
            assertThat(sectionDetail.storageLocation()).isEqualTo("path/purple-text-book.txt");
        });

        assertThat(actualDataRecord.sections()).element(2).satisfies(sectionDetail -> {
            assertThat(sectionDetail.id()).isEqualTo(17);
            assertThat(sectionDetail.fileName()).isEqualTo("red-text-book.txt");
            assertThat(sectionDetail.storageLocation()).isEqualTo("path/red-text-book.txt");
        });
    }

    @Test
    void createDataRecord_whenTitleAndDescriptionAndNoFields_shouldCreateDataRecord() {
        CreateDataRecordRequest request = new CreateDataRecordRequest(
                "Some title",
                "Some description",
                new ArrayList<>()
        );

        DataRecordDetail actualDataRecord = dataRecordService.createDataRecord(request);

        assertThat(actualDataRecord).isNotNull();
        assertThat(actualDataRecord.title()).isEqualTo("Some title");
        assertThat(actualDataRecord.description()).isEqualTo("Some description");
        assertThat(actualDataRecord.fields()).isNotNull();
        assertThat(actualDataRecord.fields()).isEmpty();
        assertThat(actualDataRecord.sections()).isNotNull();
        assertThat(actualDataRecord.sections()).isEmpty();
    }

    @Test
    void createDataRecord_whenTitleAndDescriptionAndNoFields_shouldCallDatabase() {
        CreateDataRecordRequest request = new CreateDataRecordRequest(
                "Some title",
                "Some description",
                new ArrayList<>()
        );

        dataRecordService.createDataRecord(request);

        verify(repository, times(1)).saveAndFlush(any());
    }

    @Test
    void createDataRecord_whenTitleAndDescriptionAndMultipleFields_shouldReturnOrderedFields() {
        CreateDataRecordRequest request = new CreateDataRecordRequest(
                "Some title",
                "Some description",
                List.of(
                        new FieldInfo("Name 3", "Value 3"),
                        new FieldInfo("Name 1", "Value 1"),
                        new FieldInfo("Name 2", "Value 2")
                )
        );

        DataRecordDetail actualDataRecord = dataRecordService.createDataRecord(request);

        assertThat(actualDataRecord).isNotNull();
        assertThat(actualDataRecord.title()).isEqualTo("Some title");
        assertThat(actualDataRecord.description()).isEqualTo("Some description");
        assertThat(actualDataRecord.sections()).isNotNull();
        assertThat(actualDataRecord.sections()).isEmpty();
        assertThat(actualDataRecord.fields()).isNotNull();
        assertThat(actualDataRecord.fields()).hasSize(3);

        assertThat(actualDataRecord.fields()).element(0).satisfies(field -> {
            assertThat(field.name()).isEqualTo("Name 1");
            assertThat(field.value()).isEqualTo("Value 1");
        });

        assertThat(actualDataRecord.fields()).element(1).satisfies(field -> {
            assertThat(field.name()).isEqualTo("Name 2");
            assertThat(field.value()).isEqualTo("Value 2");
        });

        assertThat(actualDataRecord.fields()).element(2).satisfies(field -> {
            assertThat(field.name()).isEqualTo("Name 3");
            assertThat(field.value()).isEqualTo("Value 3");
        });
    }

    @Test
    void updateDataRecord_whenInvalidDataRecordId_shouldThrowException() {
        UpdateDataRecordRequest request = new UpdateDataRecordRequest(
                "Some title",
                "Some description"
        );

        assertThatThrownBy(() -> dataRecordService.updateDataRecord(1, request))
                .isInstanceOf(InvalidClientInputException.class)
                .hasMessage("Key for message 'features.datarecords.on.datarecord.update.datarecord.not.found'");

        verify(repository, times(1)).findById(1);
    }

    @Test
    void updateDataRecord_whenGivenTitleAndDescription_shouldUpdateDataRecord() {
        UpdateDataRecordRequest request = new UpdateDataRecordRequest(
                "Some title",
                "Some description"
        );

        DataRecord dataRecord = createDataRecord(1,
                "Actual title",
                "Actual description",
                new ArrayList<>(),
                new ArrayList<>());

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        DataRecordDetail actualDataRecord = dataRecordService.updateDataRecord(1, request);

        assertThat(actualDataRecord).isNotNull();
        assertThat(actualDataRecord.title()).isEqualTo("Some title");
        assertThat(actualDataRecord.description()).isEqualTo("Some description");
        assertThat(actualDataRecord.fields()).isNotNull();
        assertThat(actualDataRecord.fields()).isEmpty();
        assertThat(actualDataRecord.sections()).isNotNull();
        assertThat(actualDataRecord.sections()).isEmpty();
    }

    @Test
    void updateDataRecord_whenValidRequest_shouldPersistInDatabase() {
        UpdateDataRecordRequest request = new UpdateDataRecordRequest(
                "Some title",
                "Some description"
        );

        DataRecord dataRecord = createDataRecord(1,
                "Actual title",
                "Actual description",
                new ArrayList<>(),
                new ArrayList<>());

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(dataRecord));

        dataRecordService.updateDataRecord(1, request);

        verify(repository, times(1)).saveAndFlush(any());
    }

    private static DataRecord createDataRecord(int id, String title, String description, List<Field> fields, List<Section> sections) {
        DataRecord dataRecord = new DataRecord();
        dataRecord.setId(id);
        dataRecord.setTitle(title);
        dataRecord.setDescription(description);
        dataRecord.setFields(fields);
        dataRecord.setSections(sections);

        return dataRecord;
    }

    public static Section createSection(int id, String fileName, String storageLocation, String contentType) {
        Section section = new Section();
        section.setId(id);
        section.setFileName(fileName);
        section.setStorageLocation(storageLocation);
        section.setContentType(contentType);

        return section;
    }

    public static Field createField(int id, String name, String value) {
        Field field = new Field();
        field.setId(id);
        field.setName(name);
        field.setValue(value);

        return field;
    }
}
