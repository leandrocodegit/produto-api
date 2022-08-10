package com.api.produto.file

import org.springframework.core.io.WritableResource

interface AssociatedStoreResource<S> : WritableResource, StoreResource {
    val association: S
}