package com.daccvo.di

import com.daccvo.repository.DetiaRepository
import com.daccvo.repository.DetiaRepositoryImpl
import org.koin.dsl.module

val KoinModule = module {
    single <DetiaRepository> { DetiaRepositoryImpl() }
}