CREATE TABLE PRODUTO(
codigo VARCHAR(255) PRIMARY KEY NOT NULL,
tipo VARCHAR(255) NOT NULL,
descricao VARCHAR(255) NOT NULL,
unidade VARCHAR(255) NOT NULL,
preco DOUBLE NOT NULL,
preco_custo DOUBLE NOT NULL,
peso_liq DOUBLE NOT NULL,
peso_bruto DOUBLE NOT NULL,
estoque_minimo INT NOT NULL,
estoque_maximo  INT NOT NULL,
gtin VARCHAR(255),
gtin_embalagem VARCHAR(255),
descricao_curta VARCHAR(255) NOT NULL,
descricao_complementar VARCHAR(255) ,
largura INT NOT NULL,
altura INT NOT NULL,
profundidade INT NOT NULL,
unidade_medida VARCHAR(255) NOT NULL,
data_inclusao VARCHAR(255),
data_alteracao VARCHAR(255),
nome_fornecedor VARCHAR(255) NOT NULL,
marca VARCHAR(255) NOT NULL,
class_fiscal VARCHAR(255),
cest VARCHAR(255),
origem VARCHAR(255),
idgrupo VARCHAR(255),
link_externo VARCHAR(255),
observacoes VARCHAR(255),
grupo VARCHAR(255),
itens_por_caixa INT NOT NULL,
volumes INT,
url_video VARCHAR(255),
localizacao VARCHAR(255),
crossdocking VARCHAR(255),
garantia INT NOT NULL,
condicao VARCHAR(255),
frete_gratis VARCHAR(255) NOT NULL,
producao VARCHAR(255),
data_validade VARCHAR(255),
descricao_fornecedor VARCHAR(255),
codigopai VARCHAR(255),
status BIT(1)
)