package com.datiobd.demo

/**
  * Created by AlvaroNav on 24/11/16.
  */
import scala.concurrent.ExecutionContext
import akka.http.scaladsl.server.Route
import com.datiobd.demo.resources.{ItemResource, QuestionResource}
import com.datiobd.demo.services.{ItemService, QuestionService}


trait RestInterface extends Resources
 {

  implicit def executionContext: ExecutionContext

  lazy val questionService = new QuestionService()
  lazy val itemService = new ItemService()

  val routes: Route = questionRoutes ~ itemRoutes

}

trait Resources extends QuestionResource with ItemResource