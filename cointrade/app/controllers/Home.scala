package controllers


import javax.inject._
import play.api._
import play.api.mvc._
import javax.inject.Inject
import score._
import scala.concurrent.ExecutionContext

@Singleton
class Home @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  
  implicit val ec: ExecutionContext = cc.executionContext
  def index(userId:String) = Action { implicit request: Request[AnyContent] =>
     Ok(views.html.home(userId))
  }
}