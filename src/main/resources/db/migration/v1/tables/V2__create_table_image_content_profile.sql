CREATE TABLE imagem_content_profile(
content_id VARCHAR(255) PRIMARY KEY NOT NULL,
content_length BIGINT,
content_mime_type VARCHAR(40) NOT NULL,
is_rendered BIT(1)
)

