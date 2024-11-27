package com.ahold.ctp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

// The @SpringBootApplication annotation is a convenience annotation that adds all of the following:
// @Configuration: Tags the class as a source of bean definitions for the application context
// @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings
// @ComponentScan: Tells Spring to look for other components, configurations, and services in the current package
@SpringBootApplication
class BackendAssignmentApplication

// The main function is the entry point of the Kotlin application
// runApplication is a Spring Boot utility function that runs the Spring Application
fun main(args: Array<String>) {
    // This line starts the entire Spring Boot application
    // It will scan for components in the current package and its sub-packages
    runApplication<BackendAssignmentApplication>(*args)
}