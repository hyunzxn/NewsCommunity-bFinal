package com.teamharmony.newscommunity.users.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig {
	
	@Value("${aws.credentials.access-key}")
	private String accessKey;
	@Value("${aws.credentials.secret-key}")
	private String secretKey;
	
	@Bean
	public AmazonS3Client s3() {
		AWSCredentials credentials = new BasicAWSCredentials(
				accessKey,
				secretKey
		);
		return (AmazonS3Client) AmazonS3ClientBuilder.standard()
		                                             .withCredentials(new AWSStaticCredentialsProvider(credentials))
		                                             .build();
	}
}
