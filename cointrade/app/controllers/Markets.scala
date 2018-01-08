package controllers



import javax.inject._
import play.api._
import play.api.mvc._
import javax.inject.Inject
import score._
import scala.concurrent.ExecutionContext
import services._
import play.api.libs.json._

@Singleton
class Markets @Inject()(cc: ControllerComponents, val marketService : MarketService) extends AbstractController(cc) {
  
  
  implicit val ec: ExecutionContext = cc.executionContext
  def refreshMarkets() = Action.async { implicit request: Request[AnyContent] =>
     marketService.refreshMarket().map{str=>
       Ok(str)
     }
  }
}

