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
class StoreConfig: WebMvcConfigurer{

    @Bean
    fun filesystemRoot(): File? {
        try {
            return File("imagens/")
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
        return ImageContentEventListener()
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        exposeDirectory("imagens", registry)
    }

    private fun exposeDirectory(dirName: String, registry: ResourceHandlerRegistry) {
        var dirName = Path("").toAbsolutePath().toString() + "\\imagens"
        println("************** file:$dirName")
         dirName = dirName.replace("\\", "/")
        println("**************  $dirName ")
        registry.addResourceHandler("$dirName/**").addResourceLocations("file:/$dirName")
    }
}