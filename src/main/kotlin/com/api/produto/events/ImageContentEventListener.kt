package com.api.produto.events

import com.api.produto.file.RenderImagem
import com.api.produto.model.image.ImageContentProfile
import com.api.produto.model.image.Imagem
import org.springframework.content.commons.annotations.HandleAfterSetContent
import org.springframework.content.commons.annotations.HandleBeforeGetContent
import org.springframework.content.commons.annotations.HandleBeforeUnsetContent
import org.springframework.content.commons.annotations.StoreEventHandler
import org.springframework.data.rest.core.annotation.HandleAfterDelete
import org.springframework.data.rest.core.annotation.HandleAfterSave

@StoreEventHandler
class ImageContentEventListener {

    @HandleAfterSetContent
    fun handleAfterSetContent(doc: ImageContentProfile) {
        println("###################### " + doc.contentId)

    }
    @HandleAfterSetContent
    fun onAfterSetContent(doc: ImageContentProfile) {
        if (doc.isRendered)
            RenderImagem.createIcon(doc)
        println("Rendereizando " + doc.contentId)
    }

    @HandleBeforeUnsetContent
    fun onBeforeUnsetContent(doc: ImageContentProfile) {
         println("********************* " + doc.contentId)
    }

    @HandleAfterDelete
    fun onAfterDelete(doc: ImageContentProfile) {
        println("********************* Delete: " + doc.contentId)
    }
}