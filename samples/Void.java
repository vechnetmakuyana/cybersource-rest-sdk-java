import java.math.BigDecimal;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.cybersource.payments.ApiException;
import com.cybersource.payments.Configuration;
import com.cybersource.payments.Configuration.ConfigurationBuilder;
import com.cybersource.payments.model.AuthCaptureRequest;
import com.cybersource.payments.model.Payment;
import com.cybersource.payments.model.VoidRequest;
import com.cybersource.payments.api.SalesApi;
import com.cybersource.payments.api.VoidsApi;

public class Void {
	public static void main(String[] args) {
		// Set ApiKey, secretKey and timeoutMilliseconds
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setApiKey("apikey").setSecretKey("secretkey")
				.setTimeoutMilliseconds(30000);
		String nextYear = Integer.toString((Calendar.getInstance().get(
				Calendar.YEAR) + 1));

		Configuration config = builder.build();
		SalesApi saleApi = new SalesApi(config);
		VoidsApi voidApi = new VoidsApi(config);

		AuthCaptureRequest authRequest = new AuthCaptureRequest();
		Payment payment = new Payment();
		payment.setCardNumber("4111111111111111");
		payment.setCardExpirationMonth("10");
		payment.setCardExpirationYear(nextYear);

		authRequest.setPayment(payment);
		authRequest.setAmount(new BigDecimal(5.00));
		authRequest.setCurrency("USD");
		authRequest.setReferenceId("123");

		try {
			// Perform a sale
			com.cybersource.payments.model.Sale sale = saleApi.createSale(authRequest);
			String saleId = sale.getId();
			System.out.println("Sale created and returned with saleId: "
					+ saleId);

			// Please note that there could be a delay for the transaction
			// detail to be available for retrieval after a requests is posted
			// to the server for processing.
			TimeUnit.SECONDS.sleep(2);

			// Void a sale
			VoidRequest voidRequest = new VoidRequest();
			voidRequest.setReferenceId("123");
			com.cybersource.payments.model.Void voidOutput = voidApi.voidSale(saleId,
					voidRequest);
			System.out.println("Void a sale: " + voidOutput);
		} catch (ApiException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
