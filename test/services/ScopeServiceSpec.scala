package services

import models.Scope
import org.mockito.BDDMockito.given
import org.mockito.Matchers.any
import org.mockito.Mockito.{verify, when}
import org.scalatest.mockito.MockitoSugar
import repository.ScopeRepository
import utils.UnitSpec

import scala.concurrent.Future.{failed, successful}

class ScopeServiceSpec extends UnitSpec with MockitoSugar {

  val scope = Scope("scopeKey", "scopeName", "scopeDescription")

  trait Setup {
    val mockScopeRepository = mock[ScopeRepository]
    val underTest = new ScopeService(mockScopeRepository)

    when(mockScopeRepository.save(any())).thenAnswer(returnSame[Scope])
  }

  "createOrUpdate" should {

    "save the scope in the repository" in new Setup {

      val result = await(underTest.createOrUpdate(scope))

      result shouldBe scope
      verify(mockScopeRepository).save(scope)
    }

    "fail when the repository fails" in new Setup {

      given(mockScopeRepository.save(scope)).willReturn(failed(new RuntimeException("Error message")))

      intercept[RuntimeException]{await(underTest.createOrUpdate(scope))}
    }
  }

  "fetch" should {
    "return the scope when it exists" in new Setup {
      given(mockScopeRepository.fetch(scope.key)).willReturn(successful(Some(scope)))

      val result = await(underTest.fetch(scope.key))

      result shouldBe Some(scope)
    }

    "return None when the API does not exist" in new Setup {
      given(mockScopeRepository.fetch(scope.key)).willReturn(successful(None))

      val result = await(underTest.fetch(scope.key))

      result shouldBe None
    }

    "fail when the repository fails" in new Setup {
      given(mockScopeRepository.fetch(scope.key)).willReturn(failed(new RuntimeException("Error message")))

      intercept[RuntimeException]{await(underTest.fetch(scope.key))}
    }

  }
}