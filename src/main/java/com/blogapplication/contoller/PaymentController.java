package com.blogapplication.contoller;

import com.blogapplication.dto.payment.OrderRequest;
import com.blogapplication.dto.payment.OrderResponse;
import com.blogapplication.dto.payment.PaymentRequest;
import com.blogapplication.dto.payment.PaymentResponse;
import com.razorpay.*;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private RazorpayClient razorpayClient;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Value("${razorpay.currency}")
    private String currency;

    @PostMapping(
            value = "/create-order",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            JSONObject orderData = new JSONObject();
            orderData.put("amount", orderRequest.getAmount() * 100); // Convert to paise
            orderData.put("currency", currency);
            orderData.put("receipt", orderRequest.getReceipt());

            Order order = razorpayClient.orders.create(orderData);

            // Convert Order to a proper response object
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.get("id"));
            response.put("amount", orderRequest.getAmount());
            response.put("currency", currency);
            response.put("receipt", orderRequest.getReceipt());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to create order");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);
        }
    }

    @PostMapping(
            value = "/verify-payment",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentResponse paymentResponse) {
        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", paymentResponse.getRazorpayOrderId());
            attributes.put("razorpay_payment_id", paymentResponse.getRazorpayPaymentId());
            attributes.put("razorpay_signature", paymentResponse.getRazorpaySignature());

            boolean isValid = Utils.verifyPaymentSignature(attributes, keySecret);

            Map<String, Object> response = new HashMap<>();
            response.put("valid", isValid);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to verify payment");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);
        }
    }
}