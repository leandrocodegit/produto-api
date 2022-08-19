package com.api.produto.file

import java.io.InputStream;

import org.springframework.content.commons.io.DeletableResource;
import org.springframework.content.commons.storeservice.StoreInfo;
import org.springframework.core.io.WritableResource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;


interface StoreResource : WritableResource, DeletableResource {
    val storeInfo: StoreInfo?
    val eTag: Any?
    val mimeType: MediaType?

    fun isRenderableAs(mimeType: MimeType?): Boolean
    fun renderAs(mimeType: MimeType?): InputStream?
}