package com.api.produto.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.content.fs.io.FileSystemResourceLoader
import java.io.File

@Configuration
class StoreConfig {

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
        print("************************* " + filesystemRoot()?.absolutePath + "\n")
        return FileSystemResourceLoader(filesystemRoot()?.absolutePath)
    }
}