package services

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import scala.collection.immutable.Range
import score._
import org.scalatest._
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import org.scalatest.mockito._
import scala.concurrent._
import Matchers._
import play.api.libs.json._
import play.api.libs.functional.syntax._

class ScoreServiceSpec extends AsyncFlatSpec with MockitoSugar {
 
   implicit val buyScoreWrite = Json.writes[BuyScore]
  "ScoreService.scoresJson" should  "corrects correctly create string json" in {
    val mockMarketService = mock[MarketService]
    val mockCalc = mock[Calc]
    val m1="m1"
    val m2="m2"
    val v1=2.0
    val v2=4.0
    
    val expectedJson = Json.toJson( Json.toJson(BuyScore(m1,v1)), Json.toJson(BuyScore(m2,v2)))
    when(mockMarketService.getMarkets()) thenReturn Future{List(m1,m2)}
    when(mockCalc.score(m1)) thenReturn Future{ BuyScore(m1,v1)}
    when(mockCalc.score(m2)) thenReturn Future{ BuyScore(m2,v2)}
    val scoreService = new ScoreService(mockMarketService, mockCalc)
    scoreService.scoresJson().map{ res=>
      res should be (expectedJson)
    }  
  }
   
   
    it should  "corrects correctly when one of the respones fail" in {
    val mockMarketService = mock[MarketService]
    val mockCalc = mock[Calc]
    val m1="m1"
    val m2="m2"
    val v1=2.0
    val v2=4.0
    
    val expectedJson = Json.toJson(List( Json.toJson(BuyScore(m1,v1))))
    when(mockMarketService.getMarkets()) thenReturn Future{List(m1,m2)}
    when(mockCalc.score(m1)) thenReturn Future{ BuyScore(m1,v1)}
    when(mockCalc.score(m2)) thenReturn Future{ throw new TimeoutException()}
    val scoreService = new ScoreService(mockMarketService, mockCalc)
    scoreService.scoresJson().map{ res=>
      res should be (expectedJson)
    }  
  }
}