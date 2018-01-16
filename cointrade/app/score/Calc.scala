package score

import data._
import javax.inject._
import services._
import scala.concurrent.ExecutionContext
import play.api.Logger
import scala.concurrent.Future
import scala.util._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit
import models._
import collection.mutable._
import scala.collection.JavaConverters._
import scala.collection.immutable.Range
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions.asScalaBuffer
import constants._

@Singleton
class Calc @Inject()(val marketPrice : MarketPriceService,val marketService : MarketService) {

  implicit val ec= ExecutionContext.global
	def sellScore()={
	 
	  val lst : List[UserPurchase]= asScalaBuffer(UserPurchase.allBought()).toList
	  val futLst =lst.map{up=>
	    	val values = marketPrice.lastNBlocks(up.coinId, 48,up.timestamp);
	      values.map{ lst=>
	     val sellScore =Calc.sellScore(lst,up.unitprice)
	     val currentProfit = (100*(lst.last - up.unitprice))/up.unitprice
	     up.coinId + " => " + up.amount+ " => "+
	     up.exchangeId + " => " + up.timestamp+ " => " + up.unitprice + " => "+ sellScore +" => "+ currentProfit+"%"
	    }
	  }
	  val resFut = Future.sequence(futLst)
	  val result= resFut.map{lst=>
	    lst.mkString("\n")
	  }
	  result
	}

	def score(coin:String)={
			val values = marketPrice.lastNBlocks(coin, 18,0);
			values.map{lst =>
			(coin,Calc.buyScore(lst))
			}
	}
}

object Calc{
			val weightByHour = Array(4,8,12,8,4,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1)
			val div:Long=AppConstants.blocks/AppConstants.granularity
			val normalizeCnt=2
			val percentFactor =1000
			val toleranceFactor =20
			
			val tolProfit=40
			val tolLoss=20

			def percent(first:Double, second:Double)={
	      ((first-second)*Calc.percentFactor)/second
	    }
			def repalceZeroWithLastElement(lst : List[Double]):List[Double]={
					lst.scanLeft(0.0){(x,y) =>
					  val xx=x
					  val yy=y
					if(y==0) x else y
					}.drop(1)
			}
	
			def normalize(lst:List[Double])={ 		
					val x =repalceZeroWithLastElement(lst).zipWithIndex
					x.filter { case(value,i) => ((i+div)%div < normalizeCnt)  }
					.map{ case(e,_)=> e}
					.grouped(normalizeCnt)
					.map{ lst5 => 
					(lst5.sum/lst5.length)
					}.toList
			}

			def positiveHike(normalized:List[Double])={  	  
					val hikes =normalized.dropRight(1).zip(normalized.drop(1)).map{
					case(x,y) => ((Calc.percentFactor*(y-x))/x)
					}
					val res = (hikes.reverse.span(hike=> hike> - Calc.toleranceFactor)._1)
					res
			}


			def buyScore(lst:List[Double])={
					val normalized= normalize(lst)
							val positiveHikes= positiveHike(normalized)
					buyScoreGivenHikes(positiveHikes)
			}
			
			def buyScoreGivenHikes(positiveHikes:List[Double])={
			  	val nHours = positiveHikes.length
				if(nHours>0){(positiveHikes.sum * weightByHour(nHours-1) )/ nHours}else 0
			}
			
			def sellScore(lst: List[Double], unitprice:Double)={
			   val localMax=lst.max
	      val currentPrice= lst.last
	      
	      val profitWhenMax = Calc.percent(localMax,unitprice)
	      val currProfit = Calc.percent(currentPrice,unitprice)
	      val profitScore =  Calc.tolProfit-Calc.percent(localMax, currentPrice) 
	      
	      val lossScore = currProfit+Calc.tolLoss
	      val sellScore = if(lossScore>0) profitScore else lossScore
	      sellScore
			}

}

