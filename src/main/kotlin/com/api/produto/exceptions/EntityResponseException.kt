package com.api.produto.exceptions

import com.api.produto.enuns.CodeError

class EntityResponseException(message: String, var codeError: CodeError): RuntimeException(message)