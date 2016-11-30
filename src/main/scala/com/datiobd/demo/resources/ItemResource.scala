package com.datiobd.demo.resources

import akka.http.scaladsl.server._
import com.datiobd.demo.model.Item
import com.datiobd.demo.routing.ResourceSupport
import com.datiobd.demo.services.ItemService

/**
  * Created by AlvaroNav on 24/11/16.
  */
trait ItemResource extends ResourceSupport {

  val itemService: ItemService

  def itemRoutes: Route = pathPrefix("items") {
    pathEnd {
      post {
        entity(as[Item]) { item =>
          completeWithLocationHeader(
            resourceId = itemService.create(item),
            ifDefinedStatus = 201, ifEmptyStatus = 409)
        }
      }
    } ~
      path(Segment) { id =>
        get {
          complete(itemService.get(id.toInt))
        } ~
          put {
            entity(as[Item]) { update =>
              complete(itemService.update(id.toInt, update))
            }
          } ~
          delete {
            complete(itemService.delete(id.toInt))
          }
      }
  }

}
