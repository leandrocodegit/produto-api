
ALTER TABLE  imagem_profile
ADD CONSTRAINT imagem_profile_fk
  FOREIGN KEY (`profiles_content_id`)
  REFERENCES `produto`.`imagem_content_profile` (`content_id`)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;
ALTER TABLE  imagem_profile
ADD CONSTRAINT imagem_fk
  FOREIGN KEY (`imagem_id`)
  REFERENCES `produto`.`imagem` (`id`)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;
