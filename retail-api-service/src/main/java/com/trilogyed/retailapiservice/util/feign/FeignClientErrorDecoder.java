package com.trilogyed.retailapiservice.util.feign;

import com.trilogyed.retailapiservice.exception.MicroserviceUnavailableException;
import com.trilogyed.retailapiservice.exception.RequestException;
import com.trilogyed.retailapiservice.exception.TupleNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        Integer status = response.status();
        if (status < 400) {
            return new Exception("This should be an error. Status code: " + status);
        } else if (status == 404) {
            throw new TupleNotFoundException("Not found exception for request: " +
                    response.request().url().replaceAll("https?://[^/]*/", "/"));
        } else if (status <= 499) {
            throw new RequestException("Client-Side Exception (status " + status + "): " +
                    " | cause: " + response.reason());
        } else {
            throw new MicroserviceUnavailableException("Unable to process this request at the moment: " +
                    response.request().url().replaceAll("https?://[^/]*/", "/") +
                    " | reason: " + response.reason());
        }
    }
}
