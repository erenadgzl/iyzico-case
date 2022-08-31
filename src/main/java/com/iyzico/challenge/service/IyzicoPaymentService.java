package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Payment;
import com.iyzico.challenge.model.BankPaymentRequest;
import com.iyzico.challenge.model.BankPaymentResponse;
import com.iyzico.challenge.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class IyzicoPaymentService {

    private Logger logger = LoggerFactory.getLogger(IyzicoPaymentService.class);

    private BankService bankService;
    private PaymentRepository paymentRepository;

    public IyzicoPaymentService(BankService bankService, PaymentRepository paymentRepository) {
        this.bankService = bankService;
        this.paymentRepository = paymentRepository;
    }

    public void pay(BigDecimal price) {
        //pay with bank
        BankPaymentResponse response = bankPayment(price);
        logger.info("Bank payment successfully!");
        //save payment
        savePayment(response.getResultCode(), price);
        logger.info("Payment saved successfully!");
    }

    private BankPaymentResponse bankPayment(BigDecimal price) {
        BankPaymentRequest request = new BankPaymentRequest();
        request.setPrice(price);
        return bankService.pay(request);
    }

    /**
     * QUESTION 2
     *
     * When using transactional annotation, database session start when calling function.
     * So in this case we create 100 thread for task but just 2 thread can run,
     * because connection pool is 2 and process take ~5 seconds so other threads waiting for connection.
     * That's mean when unit test start only 2 thread can run then after these tasks finish the other 2 thread can run.
     * So threads waiting for connection and after 30 second we got connection timeout error.
     *
     * Extracted from function which takes a lot of our time and does not need database operation.
     * Then used transactional annotation just this method.
     */
    @Transactional
    void savePayment(String bankResultCode, BigDecimal price) {
        Payment payment = new Payment(price, bankResultCode);
        paymentRepository.save(payment);
    }
}
