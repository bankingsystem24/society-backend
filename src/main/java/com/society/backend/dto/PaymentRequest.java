package com.society.backend.dto;

public class PaymentRequest {

    private Long billId;
    private String paymentMethod;


        public Long getBillId(){
            return billId;
        }

        public void setBillId(Long billId){
            this.billId = billId;
        }

        public String getPaymentMethod(){
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod){
            this.paymentMethod = paymentMethod;
        }
   
    
}
