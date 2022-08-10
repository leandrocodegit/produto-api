package com.api.produto.file

import java.io.InputStream

interface Renderable<E>{
    fun getRendition(entity:E, mimeType: String ):InputStream;
}