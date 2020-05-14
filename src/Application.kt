package com.saied.reqres

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*
import io.ktor.jackson.*
import io.ktor.features.*
import io.ktor.request.httpMethod
import io.ktor.request.receive
import io.ktor.request.receiveText
import io.ktor.util.caseInsensitiveMap
import io.ktor.util.toMap

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        route("/{path...}") {
            get {
                call.respond(Req(call, call.receiveText()))
            }
            post {
                call.respond(Req(call, call.receiveText()))
            }
            put {
                call.respond(Req(call, call.receiveText()))
            }
            delete {
                call.respond(Req(call, call.receiveText()))
            }
            head {
                call.respond(Req(call, call.receiveText()))
            }
        }
    }
}

class Req(call: ApplicationCall, body: String) {
    val body = ObjectMapper().readTree(body)
    val pathParams: List<String> = call.parameters.getAll("path") ?: listOf()
    val queryParams: Map<String, List<String>> = call.request.queryParameters.toMap()
    val method: String = call.request.httpMethod.value
    val headers: Map<String, List<String>> = call.request.headers.toMap()
}

