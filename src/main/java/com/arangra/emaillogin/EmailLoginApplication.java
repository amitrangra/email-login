package com.arangra.emaillogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

import com.arangra.emaillogin.adapter.inbound.InboundAdapterConfig;
import com.arangra.emaillogin.adapter.outbound.OutboundAdapterConfig;
import com.arangra.emaillogin.domain.DomainConfig;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import({InboundAdapterConfig.class, OutboundAdapterConfig.class, DomainConfig.class})
public class EmailLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailLoginApplication.class, args);
	}

}
