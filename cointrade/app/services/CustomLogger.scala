package services

import play.Logger

object CustomLogger {
 def debug(msg :String){
     Logger.debug(msg);
 }
 
}