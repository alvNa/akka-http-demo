package com.datiobd.demo.resources


import akka.http.scaladsl.server.Route
import com.datiobd.demo.routing.ResourceSupport
import com.datiobd.demo.model.{Question, QuestionUpdate}
import com.datiobd.demo.services.QuestionService


/**
  * Created by AlvaroNav on 24/11/16.
  */
trait QuestionResource extends ResourceSupport {

  val questionService: QuestionService

  def questionRoutes: Route = pathPrefix("questions") {
    pathEnd {
      post {
        entity(as[Question]) { question =>
          completeWithLocationHeader(
            resourceId = questionService.create(question),
            ifDefinedStatus = 201, ifEmptyStatus = 409)
        }
      }
    } ~
      path(Segment) { id =>
        get {
          complete(questionService.get(id))
        } ~
          put {
            entity(as[QuestionUpdate]) { update =>
              complete(questionService.update(id, update))
            }
          } ~
          delete {
            complete(questionService.delete(id))
          }
      }
  }

}
