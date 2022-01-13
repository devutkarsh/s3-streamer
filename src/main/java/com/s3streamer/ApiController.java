package com.s3streamer;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

@RestController("/")
public class ApiController {

	@Value("${aws.region}")
	private String awsRegion;

	@GetMapping(value = "/**", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public ResponseEntity<StreamingResponseBody> getObject(HttpServletRequest request) {
		try {
			AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(awsRegion).build();

			String uri = request.getRequestURI();
			String uriParts[] = uri.split("/", 2)[1].split("/", 2);
			String bucket = uriParts[0];
			String key = uriParts[1];
			System.out.println("Fetching " + uri);
			S3Object object = s3client.getObject(bucket, key);
			S3ObjectInputStream finalObject = object.getObjectContent();

			final StreamingResponseBody body = outputStream -> {
				int numberOfBytesToWrite = 0;
				byte[] data = new byte[1024];
				while ((numberOfBytesToWrite = finalObject.read(data, 0, data.length)) != -1) {
					outputStream.write(data, 0, numberOfBytesToWrite);
				}	
				finalObject.close();
			};
			return new ResponseEntity<StreamingResponseBody>(body, HttpStatus.OK);
		} catch (Exception e) {
			System.err.println("Error "+ e.getMessage());
			return new ResponseEntity<StreamingResponseBody>(HttpStatus.BAD_REQUEST);
		}

	}
}
