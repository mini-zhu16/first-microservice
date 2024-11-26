package com.ahold.ctp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MyMicroserviceApplication

fun main(args: Array<String>) {
    runApplication<MyMicroserviceApplication>(*args)
}