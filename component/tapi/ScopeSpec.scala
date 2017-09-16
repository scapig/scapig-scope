package tapi

import models.Scope
import play.api.http.Status.{NO_CONTENT, OK}
import play.api.libs.json.Json
import play.mvc.Http.HeaderNames._
import models.JsonFormatters._

import scalaj.http.Http

class ScopeSpec extends BaseFeatureSpec {

  val scope = Scope("scopeKey", "scopeName", "scopeDescription")
  val scope2 = Scope("scope2", "scopeName", "scopeDescription")

  feature("create and fetch scope") {

    scenario("create and fetch single") {

      When("A scope create request is received")
      val createdResponse = Http(s"$serviceUrl/scope")
        .headers(Seq(CONTENT_TYPE -> "application/json"))
        .postData(Json.toJson(scope).toString()).asString

      Then("I receive a 204 (NoContent)")
      createdResponse.code shouldBe NO_CONTENT

      And("The scope can be retrieved")
      val fetchResponse = Http(s"$serviceUrl/scope/${scope.key}").asString
      fetchResponse.code shouldBe OK
      Json.parse(fetchResponse.body) shouldBe Json.toJson(scope)
    }

    scenario("fetch multiple") {

      Given("A scope1")
      createScope(scope)

      And("A scope2")
      createScope(scope2)

      When("I fetch the scopes matching scope1 and scope2")
      val fetchResponse = Http(s"$serviceUrl/scope?keys=${scope.key}%20${scope2.key}").asString

      Then("I receive a 200 (Ok) with the scopes")
      fetchResponse.code shouldBe OK
      Json.parse(fetchResponse.body) shouldBe Json.toJson(Set(scope, scope2))
    }
  }

  private def createScope(scope: Scope): Unit = {
    Http(s"$serviceUrl/scope")
      .headers(Seq(CONTENT_TYPE -> "application/json"))
      .postData(Json.toJson(scope).toString()).asString

  }
}