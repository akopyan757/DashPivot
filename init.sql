DO
$$
BEGIN
    -- Проверка и создание пользователя
    IF NOT EXISTS (
        SELECT FROM pg_catalog.pg_roles WHERE rolname = 'akopyan_albert'
    ) THEN
        CREATE USER akopyan_albert WITH PASSWORD 'za1avGU8';
        ALTER USER akopyan_albert WITH SUPERUSER;
    END IF;

    -- Проверка и создание таблицы
    IF NOT EXISTS (
        SELECT FROM pg_catalog.pg_tables WHERE tablename = 'users'
    ) THEN
        CREATE TABLE users (
            id SERIAL PRIMARY KEY,
            email VARCHAR(255) UNIQUE NOT NULL,
            password_hash VARCHAR(255) NOT NULL,
            is_verified BOOLEAN DEFAULT FALSE,
            verification_token VARCHAR(255),
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            last_login TIMESTAMP
        );
    END IF;
END
$$;