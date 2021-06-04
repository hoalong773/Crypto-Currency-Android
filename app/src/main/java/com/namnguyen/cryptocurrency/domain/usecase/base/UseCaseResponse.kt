package com.namnguyen.cryptocurrency.domain.usecase.base

import com.namnguyen.cryptocurrency.domain.model.ApiError

interface UseCaseResponse<Type> {

    fun onSuccess(result: Type)

    fun onError(apiError: ApiError?)
}

