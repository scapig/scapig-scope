package tapi

import models.Scope
import play.api.http.Status.{NO_CONTENT, OK}
import play.api.libs.json.Json
import play.mvc.Http.HeaderNames._
import models.JsonFormatters._

import scalaj.http.Http

class ScopeSpec extends BaseFeatureSpec {

  val scope = Scope("scopeKey", "scopeName", "scopeDescription")

  feature("create and fetch scope") {

    scenario("happy path") {

      When("A scope create request is received")
      val createdResponse = Http(s"$serviceUrl/scope")
        .headers(Seq(CONTENT_TYPE -> "application/json"))
        .postData(Json.toJson(scope).toString()).asString

      Then("I receive a 204 (NoContent)")
      createdResponse.code shouldBe NO_CONTENT

      And("The scope can be retrieved")
      val fetchResponse = Http(s"$serviceUrl/scope?key=${scope.key}").asString
      fetchResponse.code shouldBe OK
      Json.parse(fetchResponse.body) shouldBe Json.toJson(scope)
    }
  }
}