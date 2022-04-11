package com.dsc.grocerymanagement.model

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


@Keep
data class RazorLink(
    @SerializedName("status") val status: Int,
    @SerializedName("response") val response: Response?,
    @SerializedName("message") val message: String?
)

@Keep
data class Response(
    @SerializedName("id") val id: String,
    @SerializedName("entity") val entity: String,
    @SerializedName("receipt") val receipt: Any?,
    @SerializedName("invoice_number") val invoiceNumber: Any?,
    @SerializedName("customer_id") val customerId: String,
    @SerializedName("customer_details") val customerDetails: CustomerDetails,
    @SerializedName("order_id") val orderId: String,
    @SerializedName("line_items") val lineItems: List<Any>,
    @SerializedName("payment_id") val paymentId: Any?,
    @SerializedName("status") val status: String,
    @SerializedName("expire_by") val expireBy: Any?,
    @SerializedName("issued_at") val issuedAt: Int,
    @SerializedName("paid_at") val paidAt: Any?,
    @SerializedName("cancelled_at") val cancelledAt: Any?,
    @SerializedName("expired_at") val expiredAt: Any?,
    @SerializedName("sms_status") val smsStatus: String,
    @SerializedName("email_status") val emailStatus: Any?,
    @SerializedName("date") val date: Int,
    @SerializedName("terms") val terms: Any?,
    @SerializedName("partial_payment") val partialPayment: Boolean,
    @SerializedName("gross_amount") val grossAmount: Int,
    @SerializedName("tax_amount") val taxAmount: Int,
    @SerializedName("taxable_amount") val taxableAmount: Int,
    @SerializedName("amount") val amount: Int,
    @SerializedName("amount_paid") val amountPaid: Int,
    @SerializedName("amount_due") val amountDue: Int,
    @SerializedName("currency") val currency: String,
    @SerializedName("currency_symbol") val currencySymbol: String,
    @SerializedName("description") val description: String,
    @SerializedName("notes") val notes: List<Any>,
    @SerializedName("comment") val comment: Any?,
    @SerializedName("short_url") val shortUrl: String,
    @SerializedName("view_less") val viewLess: Boolean,
    @SerializedName("billing_start") val billingStart: Any?,
    @SerializedName("billing_end") val billingEnd: Any?,
    @SerializedName("type") val type: String,
    @SerializedName("group_taxes_discounts") val groupTaxesDiscounts: Boolean,
    @SerializedName("user") val user: Any?,
    @SerializedName("created_at") val createdAt: Int
)

@Keep
data class CustomerDetails(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("contact") val contact: String,
    @SerializedName("gstin") val gstIn: Any?,
    @SerializedName("billing_address") val billingAddress: Any?,
    @SerializedName("shipping_address") val shippingAddress: Any?,
    @SerializedName("customer_name") val customerName: String,
    @SerializedName("customer_email") val customerEmail: String,
    @SerializedName("customer_contact") val customerContact: String
)