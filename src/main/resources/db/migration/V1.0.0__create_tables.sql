CREATE TABLE credit_card (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(12) NOT NULL
);

CREATE TABLE nature (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    input_output VARCHAR(255) NOT NULL,
    signal_type VARCHAR(255) NOT NULL
);

CREATE TABLE cnab_transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    value DECIMAL(10,2) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    credit_card_id BIGINT NOT NULL,
    hour TIME NOT NULL,
    store_owner VARCHAR(255) NOT NULL,
    store_name VARCHAR(255) NOT NULL,
    nature_id BIGINT NOT NULL,
    FOREIGN KEY (credit_card_id) REFERENCES credit_card(id),
    FOREIGN KEY (nature_id) REFERENCES nature(id)
);
