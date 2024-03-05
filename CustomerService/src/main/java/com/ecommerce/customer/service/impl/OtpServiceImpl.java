package com.ecommerce.customer.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import com.ecommerce.customer.Constants;
import com.ecommerce.customer.dto.OtpDetailsDto;
import com.ecommerce.customer.exception.CustomerException;
import com.ecommerce.customer.repository.OtpDetailsRepository;
import com.ecommerce.customer.service.declaration.OtpService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.ecommerce.customer.entity.OtpDetails;

@Service
@Slf4j
public class OtpServiceImpl implements OtpService {
	
	@Value("#{new Integer(${OTP_VALIDITY})}")
	public int OTP_VALIDITY;

	@Autowired
    OtpDetailsRepository otpDetailsRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	JavaMailSenderImpl mailSender;

	@Override
	public Integer generateOtp(String email) {
		Random random = new Random();
		int otp = random.nextInt(1001, 10000);
		otpDetailsRepository
				.save(new OtpDetails(email, otp, LocalDateTime.now().plusMinutes(OTP_VALIDITY)));
		return otp;
	}

	@Override
	public boolean validateOtp(OtpDetailsDto otpDetailsDto) {
		OtpDetails otpDetails = modelMapper.map(otpDetailsDto, OtpDetails.class);
		Optional<OtpDetails> details = otpDetailsRepository.findById(otpDetails.getEmail());
		if (details.isPresent()) {
			if (details.get().getOtpTime().isAfter(LocalDateTime.now())
					&& otpDetails.getOtp() == details.get().getOtp()) {
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
			String messageBody = "<html lang='en'><head><style>body{font-family:Arial,sans-serif;" +
					"line-height:1.6;background-color:#f4f4f4;margin:0;padding:20px;}.container{max-width:600px;margin:auto;background:#fff;" +
					"padding:20px;border-radius:5px;box-shadow:0 0 10px rgba(0,0,0,0.1);}.btn{display:inline-block;background:#007bff;" +
					"color:#fff;text-decoration:none;padding:10px 20px;border-radius:5px;}</style></head><body><div class='container'>" +
					"<h2>Email Verification</h2><p>To verify your email address, please use the following OTP: </p><h3>" + otp + "</h3><br/>" +
					"<p>This OTP is valid for only <b>" + OTP_VALIDITY + " minutes</b>.</p>" +
					"<p>If you didn't sign up for an account with us, you can safely ignore this email.</p><br/><br/>" +
					"<p>Best Regards,<br><h4>DesiCart Team<h4></p></div></body></html>";
			mimeMessage.setContent(messageBody, "text/html");
			helper.setTo(email);
			helper.setSubject(otp + " : OTP to verify your Email for DesiCart");
			helper.setFrom("onetimepassword_otp@outlook.in");
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
}
