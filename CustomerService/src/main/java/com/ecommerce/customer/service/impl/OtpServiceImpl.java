package com.ecommerce.customer.service.impl;

import com.ecommerce.customer.Constants;
import com.ecommerce.customer.entity.OtpDetails;
import com.ecommerce.customer.repository.OtpDetailsRepository;
import com.ecommerce.customer.service.declaration.OtpService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class OtpServiceImpl implements OtpService {
	
	@Value("#{new Integer(${OTP_VALIDITY})}")
	public int OTP_VALIDITY;
	@Value("spring.mail.username")
	String fromEmail;

	@Autowired
    OtpDetailsRepository otpDetailsRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	JavaMailSenderImpl mailSender;
	@Autowired
	ResourceLoader resourceLoader;

	@Override
	public Integer generateOtp(String email) {
		Random random = new Random();
		int otp =1234; 	//random.nextInt(1001, 10000);
		otpDetailsRepository
				.save(new OtpDetails(email.toLowerCase(), otp, LocalDateTime.now().plusMinutes(OTP_VALIDITY)));
		return otp;
	}

	@Override
	public boolean validateOtp(String email, int otp) {
		Optional<OtpDetails> details = otpDetailsRepository.findById(email.toLowerCase());
		if (details.isPresent()) {
			if (details.get().getOtpTime().isAfter(LocalDateTime.now())
					&& otp == details.get().getOtp()) {
				otpDetailsRepository.deleteById(details.get().getEmail());
				return true;
			}
		}
		return false;
	}

	@Override
	public void sendOtpByEmail(String email, String otp) throws MessagingException {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

			mimeMessage.setContent(htmlTemplate(otp), "text/html");
			helper.setTo(email.toLowerCase());
			helper.setSubject(otp + " : OTP to verify your Email for DesiCart");
			helper.setFrom(fromEmail);
			mailSender.send(mimeMessage);
	}

	@Scheduled(cron = Constants.FIXED_DELAY)
	void cleanup() {
		otpDetailsRepository.findAll().forEach(otp -> {
			if (otp.getOtpTime().isBefore(LocalDateTime.now())) {
				otpDetailsRepository.delete(otp);
				log.info("Otp Details Repo cleanup executed.");
			}
		});
	}


	public String htmlTemplate(String otp){

		return
				" <!DOCTYPE html>\n" +
						"<html lang=\"en\">\n" +
						"<head>\n" +
						"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=us-ascii\">\n" +
						"    <style>\n" +
						"        body {\n" +
						"            font-family: Arial, sans-serif;\n" +
						"            line-height: 1.6;\n" +
						"            background-color: #f4f4f4;\n" +
						"            margin: 0;\n" +
						"            padding: 20px;\n" +
						"        }\n" +
						"        .container {\n" +
						"            max-width: 600px;\n" +
						"            margin: auto;\n" +
						"            background: #fff;\n" +
						"            padding: 20px;\n" +
						"            border-radius: 5px;\n" +
						"            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
						"            /* display: table-cell; */\n" +
						"            vertical-align: middle;\n" +
						"            text-align: center;\n" +
						"			 font-size: 16px;\n" +
						"        }\n" +
						"        .otp-text {\n" +
						"            background-color: #db4444;\n" +
						"            color: white;\n" +
						"            padding: 8px 35px;\n" +
						"            border-radius: 4px;\n" +
						"            cursor: pointer;\n" +
						"        }\n" +
						"    </style>\n" +
						"</head>\n" +
						"<body>\n" +
						"<div class=\"container\">\n" +
						"    <p style=\"font-size: 20px;font-weight: bold;\">VERIFICATION CODE</p>\n" +
						"    <b class=\"otp-text\">"+ otp +"</b>\n" +
						"    <p>This OTP is valid for only <b>"+ OTP_VALIDITY +"</b> minutes.</p>\n" +
						"    <p>If you didn't sign up for an account with us, you can safely ignore this email.</p>\n" +
						"    <p>Best Regards,\n" +
						"    <h4 style=\"color: #db4444;\">DesiCart Team</h4>\n" +
						"    </p>\n" +
						"</div>\n" +
						"</body>\n" +
						"</html>";
	}
	
}
