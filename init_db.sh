#!/bin/sh
set -e
USERNAME=$(cat /run/secrets/db_username)
PASSWORD=$(cat /run/secrets/db_password)

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
DO \$\$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_catalog.pg_user WHERE usename = '$USERNAME') THEN
      CREATE USER $USERNAME WITH PASSWORD $PASSWORD;
   END IF;
END
\$\$;

DO \$\$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'db') THEN
      CREATE DATABASE db OWNER $USERNAME;
   END IF;
END
\$\$;

GRANT ALL PRIVILEGES ON DATABASE db TO $USERNAME;
EOSQL