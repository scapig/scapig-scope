package services

import javax.inject.{Inject, Singleton}

import models.Scope
import repository.ScopeRepository

import scala.concurrent.Future

@Singleton
class ScopeService @Inject()(scopeRepository: ScopeRepository) {

  def fetch(key: String): Future[Option[Scope]] = {
    scopeRepository.fetch(key)
  }

  def createOrUpdate(scope: Scope): Future[Scope] = {
    scopeRepository.save(scope)
  }

}
