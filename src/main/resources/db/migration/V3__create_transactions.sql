CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,

    payer_id BIGINT NOT NULL,
    payee_id BIGINT NOT NULL,

    amount NUMERIC(19, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transaction_payer
        FOREIGN KEY (payer_id)
        REFERENCES users (id),

    CONSTRAINT fk_transaction_payee
        FOREIGN KEY (payee_id)
        REFERENCES users (id),

    CONSTRAINT chk_transaction_amount_positive
        CHECK (amount > 0)
);
