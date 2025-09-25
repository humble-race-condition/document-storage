INSERT INTO data_records(id, version, title, description, created_at, modified_at)
VALUES (1, 2, 'Payment note', 'A payment note', NOW(), NOW()),
       (2, 2, 'Calculation', 'A calculation', NOW(), NOW()),
       (3, 2, 'Storage bill', 'A bill for storage', NOW(), NOW());


INSERT INTO fields (id, name, field_value, data_record_id, version, created_at,
                                         modified_at)
VALUES (1, 'IBAN', '112233', 1, 2, NOW(), NOW()),
       (2, 'Beneficiary', 'TODOR GOGOV', 1, 2, NOW(), NOW()),
       (3, 'Alpha', 'Beta', 1, 2, NOW(), NOW()),
       (4, 'Sum', 'Amount', 2, 2, NOW(), NOW()),
       (5, 'City', 'Sofia', 2, 2, NOW(), NOW());
