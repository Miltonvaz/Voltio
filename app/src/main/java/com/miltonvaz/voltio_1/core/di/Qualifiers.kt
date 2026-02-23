package com.miltonvaz.voltio_1.core.di

import jakarta.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RickAndMortyRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class JsonPlaceHolderRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class VoltioRetrofit