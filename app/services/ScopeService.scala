package services

import javax.inject.{Inject, Singleton}

import models.Scope
import repository.ScopeRepository

import scala.concurrent.Future
import scala.concurrent.Future.sequence
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ScopeService @Inject()(scopeRepository: ScopeRepository) {

  def fetch(key: String): Future[Option[Scope]] = {
    scopeRepository.fetch(key)
  }

  def fetchMultiple(keys: Set[String]): Future[Set[Scope]] = {
    sequence(keys map (key => scopeRepository.fetch(key))) map (_.flatten)
  }

  def createOrUpdate(scope: Scope): Future[Scope] = {
    scopeRepository.save(scope)
  }

}
