package utils
import scala.concurrent._
import scala.util._

object FutureUtils {
  
  
  implicit val ec= ExecutionContext.global
  def futureToFutureTry[T](f: Future[T]): Future[Try[T]] ={
    f.map(Success(_)).recover{case x => Failure(x)}
  }
}