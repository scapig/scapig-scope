package repository

import javax.inject.{Inject, Singleton}

import models.Scope
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.{UpdateWriteResult, WriteResult}
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.play.json.ImplicitBSONHandlers._
import reactivemongo.play.json.collection.JSONCollection
import models.JsonFormatters._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class ScopeRepository @Inject()(val reactiveMongoApi: ReactiveMongoApi)  {

  def repository: Future[JSONCollection] =
    reactiveMongoApi.database.map(_.collection[JSONCollection]("scapig-api-scope"))

  def save(scope: Scope): Future[Scope] = {
    repository.flatMap(collection =>
      collection.update(
        Json.obj("key"-> scope.key), scope, upsert = true) map {
        case result: UpdateWriteResult if result.ok => scope
        case error => throw new RuntimeException(s"Failed to save scapig-api-scope ${error.errmsg}")
      }
    )
  }

  def fetch(key: String): Future[Option[Scope]] = {
    repository.flatMap(collection =>
      collection.find(Json.obj("key"-> key)).one[Scope]
    )
  }

  private def createIndex(field: String, indexName: String): Future[WriteResult] = {
    repository.flatMap(collection =>
      collection.indexesManager.create(Index(Seq((field, IndexType.Ascending)), Some(indexName)))
    )
  }

  private def ensureIndexes() = {
    Future.sequence(Seq(
    createIndex("key", "keyIndex")
    ))
  }

  ensureIndexes()
}
