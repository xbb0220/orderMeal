#sql("getOrderById")
	SELECT `order`.*,sum(price * dishNum) totalPrice
	FROM `order`
	left join order_item
	on order_item.orderId=`order`.id
	where `order`.id=?
	group by order_item.orderId
#end


#sql("page")
	SELECT `order`.*,sum(price * dishNum) totalPrice
	FROM `order`
	left join order_item
	on order_item.orderId=`order`.id
	where 1=1
#end

#sql("orderForCancel")
	select id 
	from `order`
	where timeExpire<now() and status=?
#end

