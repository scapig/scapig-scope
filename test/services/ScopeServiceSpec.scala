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

  val scope = Scope("scopeKey", "scopeName")
  val scope2 = Scope("scope2", "scopeName")

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

    "return None when the scope does not exist" in new Setup {
      given(mockScopeRepository.fetch(scope.key)).willReturn(successful(None))

      val result = await(underTest.fetch(scope.key))

      result shouldBe None
    }

    "fail when the repository fails" in new Setup {
      given(mockScopeRepository.fetch(scope.key)).willReturn(failed(new RuntimeException("Error message")))

      intercept[RuntimeException]{await(underTest.fetch(scope.key))}
    }

  }

  "fetchMultiple" should {
    "return the scopes" in new Setup {
      given(mockScopeRepository.fetch(scope.key)).willReturn(successful(Some(scope)))
      given(mockScopeRepository.fetch(scope2.key)).willReturn(successful(Some(scope2)))

      val result = await(underTest.fetchMultiple(Set(scope.key, scope2.key)))

      result shouldBe Set(scope, scope2)
    }

    "return an empty set when there is no scope matching" in new Setup {
      given(mockScopeRepository.fetch(scope.key)).willReturn(successful(None))

      val result = await(underTest.fetchMultiple(Set(scope.key)))

      result shouldBe Set.empty
    }

    "ignore the scopes which do not exist" in new Setup {
      given(mockScopeRepository.fetch(scope.key)).willReturn(successful(None))
      given(mockScopeRepository.fetch(scope2.key)).willReturn(successful(Some(scope2)))

      val result = await(underTest.fetchMultiple(Set(scope.key, scope2.key)))

      result shouldBe Set(scope2)
    }

    "fail when the repository fails" in new Setup {
      given(mockScopeRepository.fetch(scope.key)).willReturn(failed(new RuntimeException("Error message")))

      intercept[RuntimeException]{await(underTest.fetchMultiple(Set(scope.key)))}
    }

  }
}