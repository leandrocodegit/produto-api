package com.api.produto.configurations

import com.api.produto.events.ImageContentEventListener
import org.springframework.content.fs.io.FileSystemResourceLoader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.File
import kotlin.io.path.Path


@Configuration
class StoreConfig{

    @Bean
    fun filesystemRoot(): File? {
        try {
            return File("/workspace/imagens/")
        } catch (ioe: Exception) {
            ioe.printStackTrace()
        }
        return null
    }
    @Bean
    fun fileSystemResourceLoader(): FileSystemResourceLoader? {
        return FileSystemResourceLoader(filesystemRoot()?.absolutePath)
    }
    @Bean
    fun imageContentEventListener(): ImageContentEventListener {
        println("********* " + filesystemRoot()?.absolutePath )
        return ImageContentEventListener()
    }

}