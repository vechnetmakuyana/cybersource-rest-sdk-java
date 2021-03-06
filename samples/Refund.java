import java.math.BigDecimal;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.cybersource.payments.ApiException;
import com.cybersource.payments.Configuration;
import com.cybersource.payments.Configuration.ConfigurationBuilder;
import com.cybersource.payments.model.AuthCaptureRequest;
import com.cybersource.payments.model.GetRefund;
import com.cybersource.payments.model.Payment;
import com.cybersource.payments.model.RefundRequest;
import com.cybersource.payments.api.SalesApi;
import com.cybersource.payments.api.RefundsApi;

public class Refund {
	public static void main(String[] args) {
		// Set ApiKey, secretKey and timeoutMilliseconds
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setApiKey("apikey").setSecretKey("secretkey")
				.setTimeoutMilliseconds(30000);
		String nextYear = Integer.toString((Calendar.getInstance().get(
				Calendar.YEAR) + 1));

		Configuration config = builder.build();
		SalesApi saleApi = new SalesApi(config);
		RefundsApi refundApi = new RefundsApi(config);

		AuthCaptureRequest request = new AuthCaptureRequest();
		Payment payment = new Payment();
		payment.setCardNumber("4111111111111111");
		payment.setCardExpirationMonth("10");
		payment.setCardExpirationYear(nextYear);

		request.setPayment(payment);
		request.setAmount(new BigDecimal(5.00));
		request.setCurrency("USD");
		request.setReferenceId("123");

		try {
			// Perform a sale
			com.cybersource.payments.model.Sale sale = saleApi.createSale(request);
			String saleId = sale.getId();
			System.out.println("Sale created and returned with saleId: "
					+ saleId);

			// Please note that there could be a delay for the transaction
			// detail to be available for retrieval after a requests is posted
			// to the server for processing.
			TimeUnit.SECONDS.sleep(2);

			RefundRequest refundRequest = new RefundRequest();
			refundRequest.setAmount(new BigDecimal(5.00));
			refundRequest.setReferenceId("123");
			com.cybersource.payments.model.Refund refund = refundApi.refundSale(
					saleId, refundRequest);
			System.out.println("Sale refunded: " + refund);

			// Please note that there could be a delay for the transaction
			// detail to be available for retrieval after a requests is posted
			// to the server for processing.
			TimeUnit.SECONDS.sleep(2);

			// Retrieve a refund
			GetRefund getRefund = refundApi.getRefund(refund.getId());
			System.out.println("Retrieve refund: " + getRefund);
		} catch (ApiException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
