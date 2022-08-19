
ALTER TABLE  estoques_depositos
ADD CONSTRAINT estoque_id_fk
  FOREIGN KEY (`estoque_id`)
  REFERENCES `produto`.`estoques` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
ALTER TABLE  estoques_depositos
ADD CONSTRAINT deposito_id_fk
  FOREIGN KEY (`depositos_id`)
  REFERENCES `produto`.`depositos` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
