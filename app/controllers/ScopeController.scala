package controllers

import javax.inject.{Inject, Singleton}

import models._
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import services.ScopeService
import models.JsonFormatters._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class ScopeController  @Inject()(cc: ControllerComponents,
                                 scopeService: ScopeService) extends AbstractController(cc) with CommonControllers {

  def createOrUpdate() = Action.async(parse.json) { implicit request =>
    withJsonBody[Scope] { scope: Scope =>
      scopeService.createOrUpdate(scope) map { _ => NoContent}
    }
  }

  def fetch(key: String) = Action.async { implicit request =>
    scopeService.fetch(key) map {
      case Some(scope) => Ok(Json.toJson(scope))
      case None => ScopeNotFound(key).toHttpResponse
    }
  }

  def fetchMultiple(keys: String) = Action.async { implicit request =>
    scopeService.fetchMultiple(keys.split("\\s+").toSet) map { scopes =>
      Ok(Json.toJson(scopes))
    }
  }

}
