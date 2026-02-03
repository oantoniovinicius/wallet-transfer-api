-- USERS
INSERT INTO users (id, full_name, cpf_cnpj, email, user_type)
VALUES
  (1, 'João Silva', '12345678901', 'joao@teste.com', 'COMMON'),
  (4, 'Maria Santos', '98765432100', 'maria@teste.com', 'COMMON'),
  (15, 'Loja XPTO', '12345678000199', 'loja@teste.com', 'MERCHANT');

-- WALLETS
INSERT INTO wallets (id, balance, user_id)
VALUES
  (1, 100.00, 4),
  (2, 50.00, 1),
  (3, 0.00, 15);
