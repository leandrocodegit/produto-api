package com.api.produto.file

import com.api.produto.model.image.ImageContentProfile
import com.api.produto.model.image.Imagem
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import javax.imageio.ImageIO
import kotlin.io.path.Path

class RenderImagem {

    companion object {


        fun createIcon(imagem: ImageContentProfile)  {
            try {
                val imageFile: ImageFile = ImageFile(imagem.contentId, null)
                val path = "${Path("").toAbsolutePath().toString()}/imagens/${imagem.contentId.toString()}"
                val file = File(path)
                val buffer = ImageIO.read(file)

                ImageIO.write(redimenssionarImagem(file,
                        (buffer.width / 2 / 3),
                        (buffer.height / 2 / 3),
                        buffer), "PNG",
                        File(
                        "${Path("").toAbsolutePath().toString()}/imagens/${imagem.contentId.toString()}"
                ))
            } catch (err: Exception) {
                null
            }
        }

        private fun redimenssionarImagem(file: File, width: Int, heigth: Int, imagem: BufferedImage): BufferedImage? {
            try {
                if (file.exists()) {
                    val buffer = BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB)
                    val g = buffer.createGraphics()
                    g.drawImage(imagem, 0, 0, width, heigth, null)
                    return buffer
                }
            } catch (err: java.lang.Exception) {
            }
            return null
        }

        fun deleteFile(imageFile: ImageFile): Boolean {
            try {
                val path = "${Path("").toAbsolutePath().toString()}/imagens/${imageFile.id.toString()}"
                val file = File(path)
                if (file.exists()) {
                    file.delete()
                    return true
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return false
        }
    }
}