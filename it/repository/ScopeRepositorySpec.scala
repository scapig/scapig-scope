package repository

import javax.inject.Singleton

import models.Scope
import org.scalatest.BeforeAndAfterEach
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import utils.UnitSpec
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ScopeRepositorySpec extends UnitSpec with BeforeAndAfterEach {

  val scope = Scope("scopeKey", "scopeName")

  lazy val fakeApplication: Application = new GuiceApplicationBuilder()
    .configure("mongodb.uri" -> "mongodb://localhost:27017/tapi-api-scope-test")
    .build()

  lazy val underTest = fakeApplication.injector.instanceOf[ScopeRepository]

  override def afterEach {
    await(underTest.repository).drop(failIfNotFound = false)
  }

  "save" should {
    "insert a new scope" in {
      await(underTest.save(scope))

      await(underTest.fetch(scope.key)) shouldBe Some(scope)
    }

    "update an existing scope" in {
      val updatedScope = scope.copy(name = "updatedName")
      await(underTest.save(scope))

      await(underTest.save(updatedScope))

      await(underTest.fetch(scope.key)) shouldBe Some(updatedScope)
    }

  }
}
