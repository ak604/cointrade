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

@Singleton
class Calc @Inject()(val coinPrice : CoinPrice) {

	implicit val ec= ExecutionContext.global
			def topN(n :Int)={
					var futLst :List[Future[(String,Int)]]  = Nil
							Coins.lst.take(100).foreach{coin=>
							futLst=  futLst++List(score(coin))
	}

	val resFut = Future.sequence(futLst)
			val result= resFut.map{lst=>
			val  sorted =lst.sortBy{ case(coin,score) => score}
			sorted.map{ case(coin,score) =>
			Logger.debug( coin + "  => "+ score)
			coin + "  => "+ score
			}.mkString("\n")
	}
	result
	}


	def score(coin:String)={
			val values = coinPrice.lastNHours(coin, 12, Calc.interval);
			values.map{lst =>
			Calc.buyScore(coin,lst)
			}
	}
}

object Calc{
	    val interval=5
			val weightByHour = Array(1,2,4,8,10,8,6,4,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1)
			val div:Int=60/interval
			val normalizeCnt=5
			val percentFactor =1000

			def repalceZeroWithLastElement(lst : List[Int])={
					lst.scan(0){case(x,y) =>
					if(y==0) x else y
					}.drop(1)
			}
			def normalize(lst:List[Int])={ 		
					repalceZeroWithLastElement(lst).zipWithIndex
					.filter { case(value,i) => ((i+div)%div < normalizeCnt)  }
					.map{ case(e,_)=> e}
					.grouped(normalizeCnt)
					.map{ lst5 => 
					(lst5.sum/lst5.length)
					}.toList
			}

			def positiveHike(normalized:List[Int])={  	  
					val hikes =normalized.dropRight(1).zip(normalized.drop(1)).map{
					case(x,y) => ((Calc.percentFactor*(y-x))/x)
					}
					(hikes.reverse.span(hike=> hike>0 )._1)
			}


			def buyScore(coin:String,lst:List[Int])={

					val normalized = normalize(lst)
							val positiveHikes= positiveHike(normalized)
							val nHours = positiveHikes.length;
					var score=0
							if(nHours>0){
								val avgHike = (positiveHikes.sum )/ nHours
										score = avgHike * weightByHour(nHours)
							}
					(coin,score)
			}

}