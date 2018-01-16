package controllers



import javax.inject._
import play.api._
import play.api.mvc._
import javax.inject.Inject
import score._
import scala.concurrent._
import services._

@Singleton
class Markets @Inject()(cc: ControllerComponents, val marketService : MarketService, val scoreService : ScoreService) extends AbstractController(cc) {
  
  implicit val ec: ExecutionContext = cc.executionContext
  def refreshMarkets() = Action.async { implicit request: Request[AnyContent] =>
     marketService.refreshMarket().map{str=>
       Ok(str)
     }
  }
  
  def scores() = Action.async { implicit request: Request[AnyContent] =>
    scoreService.scoresJson().map{retVal =>
      Ok(retVal)
    }
  }
}

