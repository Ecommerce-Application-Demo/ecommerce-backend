package com.ecommerce.customer.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import com.ecommerce.customer.Constants;
import com.ecommerce.customer.dto.OtpDetailsDto;
import com.ecommerce.customer.repository.OtpDetailsRepository;
import com.ecommerce.customer.service.declaration.OtpService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.ecommerce.customer.entity.OtpDetails;

@Service
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
	public void sendOtpByEmail(String email, String otp) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("onetimepassword_otp@outlook.in");
		message.setTo(email);
		message.setSubject("OTP from Ecommerce Application to verify Email");
		message.setText("Your OTP to verify email  " + email + " is:\r\n " + otp + "\r\nIt is valid for "+ OTP_VALIDITY +" minutes.");
		mailSender.send(message);
	}

	@Scheduled(cron = Constants.FIXED_DELAY)
	void cleanup() {
		otpDetailsRepository.findAll().forEach(otp -> {
			if (otp.getOtpTime().isAfter(LocalDateTime.now())) {
				otpDetailsRepository.delete(otp);
			}
		});
	}
}
