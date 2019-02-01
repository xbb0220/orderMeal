#sql("getOrderItemByOrderId")
	select order_item.* ,dish.dishTypeId
	from order_item
	left join dish
	on order_item.dishId=dish.id
	where order_item.orderId=?
#end

