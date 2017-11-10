package controllers

import models.Scope
import org.mockito.BDDMockito.given
import org.mockito.Mockito.{verify, verifyZeroInteractions}
import org.scalatest.mockito.MockitoSugar
import play.api.http.Status
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.test.{FakeRequest, Helpers}
import services.ScopeService
import utils.UnitSpec
import models.JsonFormatters._

import scala.concurrent.Future.successful

class ScopeControllerSpec extends UnitSpec with MockitoSugar {

  val scope = Scope("scopeKey", "scopeName")

  trait Setup {
    val mockScopeService: ScopeService = mock[ScopeService]
    val underTest = new ScopeController(Helpers.stubControllerComponents(), mockScopeService)

    val request = FakeRequest()

    given(mockScopeService.createOrUpdate(scope)).willReturn(successful(scope))
  }

  "createOrUpdate" should {

    "succeed with a 204 (NoContent) when payload is valid and service responds successfully" in new Setup {

      val result: Result = await(underTest.createOrUpdate()(request.withBody(Json.toJson(scope))))

      status(result) shouldBe Status.NO_CONTENT
      verify(mockScopeService).createOrUpdate(scope)
    }

    "fail with a 400 (Bad Request) when the json payload is invalid for the request" in new Setup {

      val body = """{ "invalid": "json" }"""

      val result: Result = await(underTest.createOrUpdate()(request.withBody(Json.parse(body))))

      status(result) shouldBe Status.BAD_REQUEST
      jsonBodyOf(result) shouldBe Json.parse("""{"code":"INVALID_REQUEST","message":"name is required"}""")
      verifyZeroInteractions(mockScopeService)
    }

  }

  "fetch" should {

    "succeed with a 200 (Ok) with the scope when it exists" in new Setup {

      given(mockScopeService.fetch(scope.key)).willReturn(successful(Some(scope)))

      val result: Result = await(underTest.fetch(scope.key)(request))

      status(result) shouldBe Status.OK
      jsonBodyOf(result) shouldBe Json.toJson(scope)
    }

    "fail with a 404 (Not Found) when the scope does not exist" in new Setup {
      given(mockScopeService.fetch(scope.key)).willReturn(successful(None))

      val result: Result = await(underTest.fetch(scope.key)(request))

      status(result) shouldBe Status.NOT_FOUND
      jsonBodyOf(result) shouldBe Json.parse(s"""{"code": "NOT_FOUND", "message": "no scope found for key ${scope.key}"}""")
    }
  }

  "fetchMultiple" should {

    "succeed with a 200 (Ok) with the scopes" in new Setup {

      given(mockScopeService.fetchMultiple(Set(scope.key))).willReturn(successful(Set(scope)))

      val result: Result = await(underTest.fetchMultiple(scope.key)(request))

      status(result) shouldBe Status.OK
      jsonBodyOf(result) shouldBe Json.toJson(Set(scope))
    }
  }
}