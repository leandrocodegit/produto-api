
ALTER TABLE  depositos
ADD CONSTRAINT deposito_local_id_fk
  FOREIGN KEY (`local_id`)
  REFERENCES `produto`.`local` (`id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT
