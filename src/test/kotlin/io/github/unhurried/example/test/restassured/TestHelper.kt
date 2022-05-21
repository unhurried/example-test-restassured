package io.github.unhurried.example.test.restassured

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import java.util.Date

class TestHelper {
    companion object {
        fun createRequestSpec(): RequestSpecification {
            return RequestSpecBuilder().setBaseUri("http://localhost:3001")
                .addFilter(RequestLoggingFilter()).addFilter(RequestLoggingFilter())
                .addFilter(RequestLoggingFilter()).addFilter(ResponseLoggingFilter())
                .addHeader("Authorization",  "Bearer " + createToken())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build()
        }

        fun createRequestSpecWithoutToken(): RequestSpecification {
            return RequestSpecBuilder().setBaseUri("http://localhost:3001")
                .addFilter(RequestLoggingFilter()).addFilter(ResponseLoggingFilter())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build()
        }

        private fun createToken(): String {
            val hmaC256 = Algorithm.HMAC256("secret")
            val algorithm: Algorithm = hmaC256
            return JWT.create()
                .withKeyId("kid-for-test")
                .withIssuer("http://localhost:3002")
                .withClaim("scope", "todo")
                .withExpiresAt(Date(Date().time + 60 * 60 * 1000))
                .withAudience("todo-api")
                .withSubject("sub-for-test")
                .sign(algorithm)
        }
    }
}