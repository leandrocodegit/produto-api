package com.api.prdouto.exceptions

import com.api.prdouto.enuns.CodeError

class EntityResponseException(message: String, var codeError: CodeError): RuntimeException(message)