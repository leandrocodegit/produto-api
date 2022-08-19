
ALTER TABLE  produto_imagens
ADD CONSTRAINT produto_produto_codigo_fk
  FOREIGN KEY (`produto_codigo`)
  REFERENCES `produto`.`produto` (`codigo`)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;
ALTER TABLE  produto_imagens
ADD CONSTRAINT produto_imagem_id_fk
  FOREIGN KEY (`imagens_id`)
  REFERENCES `produto`.`imagem` (`id`)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;
