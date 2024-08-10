package com.ecommerce.customer.serviceimpl;

import com.ecommerce.customer.entity.OtpDetails;
import com.ecommerce.customer.repository.OtpDetailsRepository;
import com.ecommerce.customer.service.impl.OtpServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OtpServiceImplTest {

    @Mock
    private OtpDetailsRepository otpRepository;

    @Mock
    JavaMailSenderImpl mailSender;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private OtpServiceImpl otpService;

    private OtpDetails otp;

    @BeforeEach
    public void setUp() {
        otp = new OtpDetails();
        otp.setEmail("test@example.com");
        otp.setOtp(1234);
        otp.setOtpTime(LocalDateTime.now().plusMinutes(5));
    }

    @Test
    public void testGenerateOtp() {
        when(otpRepository.save(any(OtpDetails.class))).thenReturn(any());
        otpService.generateOtp("test@example.com");

        verify(otpRepository, times(1)).save(any(OtpDetails.class));
    }

    @Test
    public void testValidateOtp_Valid() {
        when(otpRepository.findById(anyString())).thenReturn(Optional.of(otp));

        boolean result = otpService.validateOtp("test@example.com", 1234);

        assertTrue(result);
    }

    @Test
    public void testValidateOtp_Invalid() {
        when(otpRepository.findById(anyString())).thenReturn(Optional.of(otp));
        boolean result = otpService.validateOtp("test@example.com", 4567);

        assertFalse(result);
    }

    @Test
    public void testSendOtpByEmail() throws MessagingException {
        ReflectionTestUtils.setField(otpService, "fromEmail", "noreply@example.com");
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        otpService.sendOtpByEmail("test@example.com", "1234");

        verify(mailSender, times(1)).send(mimeMessage);
    }
}