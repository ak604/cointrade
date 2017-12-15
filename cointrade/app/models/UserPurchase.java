package models;

import java.util.Calendar;
import java.util.List;

import javax.persistence.*;
import javax.persistence.Id;

import io.ebean.Finder;
import io.ebean.Model;

@Entity
@Table(name="user_purchases")
public class UserPurchase extends Model {

	
	public static int PURCHASED=0;
	public static int SOLD=1;
	
    @Id
    public Long userPurchaseId;

    public Long UserId;
    public String coinId;
    public Long amount;
    public Long unitprice;
    public String exchangeId;
    public Long timestamp;
    public Integer status;
    
    
   public static final Finder<Long, UserPurchase> find = new Finder<>(UserPurchase.class);
    
   public UserPurchase( Long userId, String coinId,  Long amount,  Long unitprice, String exchangeId) {
	   this.UserId=userId;
	   this.coinId=coinId;
	   this.amount=amount;
	   this.unitprice=unitprice;
	   this.exchangeId=exchangeId;
	   this.timestamp= Calendar.getInstance().getTimeInMillis()/1000;
	   this.status=PURCHASED;
   }
   
   public static List<UserPurchase> allBought() {
		  return find.query().where()
		   .eq("status", 0)
	      .findList();
	   }
}