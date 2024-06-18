import com.google.gson.annotations.SerializedName


data class OrdersResponse(

    @field:SerializedName("message") val message: String? = null,
)

data class OrdersItem(

    @field:SerializedName("ProductName") val productName: String? = null,

    @field:SerializedName("PaymentDate") val paymentDate: String? = null,

    @field:SerializedName("CreatedAt") val createdAt: String? = null,

    @field:SerializedName("PaymentId") val paymentId: String? = null,

    @field:SerializedName("Quantity") val quantity: Int? = null,

    @field:SerializedName("ProductId") val productId: String? = null,

    @field:SerializedName("StatusName") val statusName: String? = null,

    @field:SerializedName("CustomerId") val customerId: String? = null,

    @field:SerializedName("ShipDate") val shipDate: String? = null,

    @field:SerializedName("OrderId") val orderId: String? = null,

    @field:SerializedName("ShipperId") val shipperId: String? = null,

    @field:SerializedName("UpdatedAt") val updatedAt: String? = null,

    @field:SerializedName("ImgUrl") val imgUrl: String? = null,

    @field:SerializedName("OrderDate") val orderDate: String? = null,

    @field:SerializedName("CompanyName") val companyName: String? = null,

    @field:SerializedName("ShipLimitDate") val shipLimitDate: String? = null,

    @field:SerializedName("Price") val price: Int? = null,

    @field:SerializedName("Picture") val picture: String? = null,

    @field:SerializedName("FullName") val fullName: String? = null,

    @field:SerializedName("Total") val total: Int? = null,

    @field:SerializedName("PaymentType") val paymentType: String? = null,

    @field:SerializedName("CartId") val cartId: String? = null,

    @field:SerializedName("OrderStatusId") val orderStatusId: String? = null,

    @field:SerializedName("Freight") val freight: Int? = null
)

data class OrderStatusItem(

    @field:SerializedName("OrderStatusId") val orderStatusId: String? = null,

    @field:SerializedName("StatusName") val statusName: String? = null,
)