package io.github.unhurried.example.test.restassured

import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test

class TodoTest {
    @Test fun `create, read, update and delete an item`() {
        val spec = TestHelper.createRequestSpec()

        // create
        val id: String = Given {
            spec(spec)
            body(mapOf(
                "title" to "Test",
                "category" to "one",
                "content" to "This is a test todo item."
            ))
        } When {
            post("/todos")
        } Then {
            statusCode(HttpStatus.SC_CREATED)
            body("title", equalTo("Test"))
            body("category", equalTo("one"))
            body("content", equalTo("This is a test todo item."))
        } Extract {
           path("id")
        }

        // read
        Given {
            spec(spec)
        } When {
            get("/todos/$id")
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("title", equalTo("Test"))
            body("category", equalTo("one"))
            body("content", equalTo("This is a test todo item."))
        }

        // update
        Given {
            spec(spec)
            body(mapOf(
                "title" to "Modified",
                "category" to "two",
                "content" to "This item has been updated."
            ))
        } When {
            put("/todos/$id")
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("title", equalTo("Modified"))
            body("category", equalTo("two"))
            body("content", equalTo("This item has been updated."))
        }

        // delete
        Given {
            spec(spec)
       } When {
            delete("/todos/$id")
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }

        // read again
        Given {
            spec(spec)
        } When {
            get("/todos/$id")
        } Then {
            statusCode(HttpStatus.SC_NOT_FOUND)
        }
    }

    @Test fun `read all items`() {
        val spec = TestHelper.createRequestSpec()

        // retrieve the current number of items.
        val total: Int = Given {
            spec(spec)
        } When {
            get("/todos")
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("total", greaterThanOrEqualTo(0))
        } Extract {
            path("total")
        }

        // create 2 items
        val id1: String = Given {
            spec(spec).body(mapOf("title" to "Test1", "category" to "one"))
        } When {
            post("/todos")
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            path("id")
        }
        val id2: String = Given {
            spec(spec).body(mapOf("title" to "Test2", "category" to "two"))
        } When {
            post("/todos")
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            path("id")
        }

        // read all items.
        Given {
            spec(spec)
        } When {
            get("/todos")
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("total", equalTo(total + 2))
            body("items.id", hasItem(id1))
            body("items.id", hasItem(id2))
        }
    }

    @Test fun `authentication failed`() {
        val spec = TestHelper.createRequestSpecWithoutToken()

        // call API without Authorization Header
        Given {
            spec(spec)
        } When {
            get("/todos")
        } Then {
            statusCode(HttpStatus.SC_UNAUTHORIZED)
        }

        // call API with invalid token
        Given {
            spec(spec)
            header("Authorization", "Bearer abc")
        } When {
            get("/todos")
        } Then {
            statusCode(HttpStatus.SC_UNAUTHORIZED)
        }
    }
}
