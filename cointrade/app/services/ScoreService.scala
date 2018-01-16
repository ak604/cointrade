package services
import scala.concurrent._
import javax.inject._
import play.api.libs.json._
import score._
import utils._
import scala.util._

@Singleton
class ScoreService @Inject()(val marketService : MarketService, val calc : Calc)
{
  implicit val ec= ExecutionContext.global
 
  def scoresJson()={
      marketService.getMarkets().flatMap{lst=>  
       val futList =   lst.map(market=>calc.score(market))
         val listOfFutureTrys = futList.map(FutureUtils.futureToFutureTry(_))
         Future.sequence(listOfFutureTrys).map{ lst=>
         lst.filter(x=> x.isSuccess).collect{case Success(x) =>x}
         }.map(Json.toJson(_))
   }
}
}