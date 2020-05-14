package com.saied.reqres

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*
import io.ktor.jackson.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.util.caseInsensitiveMap
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.pipeline.PipelineInterceptor
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
            get(makeRes)
            post(makeRes)
            put(makeRes)
            delete(makeRes)
            head(makeRes)
        }
    }
}

val makeRes: PipelineInterceptor<Unit, ApplicationCall> = {
    val t = call.request.contentType()
    val bodyFields: Map<String, List<String>>? =
        if (call.request.contentType().match(ContentType.Application.FormUrlEncoded))
            call.receiveParameters().toMap()
        else null
    val bodyObject: JsonNode? =
        if (call.request.contentType().match(ContentType.Text.Plain))
            ObjectMapper().readTree(call.receiveText())
        else null
    call.respond(Req(call, bodyObject, bodyFields))
}

class Req(call: ApplicationCall, val body: JsonNode?, val bodyFields: Map<String, List<String>>?) {
    val pathParams: List<String> = call.parameters.getAll("path") ?: listOf()
    val queryParams: Map<String, List<String>> = call.request.queryParameters.toMap()
    val method: String = call.request.httpMethod.value
    val headers: Map<String, List<String>> = call.request.headers.toMap()
}

