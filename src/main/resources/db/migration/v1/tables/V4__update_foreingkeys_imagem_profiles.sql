
ALTER TABLE  imagem_profiles
ADD CONSTRAINT image_profile_fk
  FOREIGN KEY (`profiles_content_id`)
  REFERENCES `produto`.`image_content_profile` (`content_id`)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;
ALTER TABLE  imagem_profiles
ADD CONSTRAINT imagem_fk
  FOREIGN KEY (`imagem_id`)
  REFERENCES `produto`.`imagem` (`id`)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;
