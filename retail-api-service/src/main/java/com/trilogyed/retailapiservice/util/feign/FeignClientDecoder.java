package com.trilogyed.retailapiservice.util.feign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.retailapiservice.domain.*;
import com.trilogyed.retailapiservice.exception.*;
import feign.Response;
import feign.codec.Decoder;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

@Component
public class FeignClientDecoder implements Decoder {
    @Autowired
    ObjectMapper mapper;

    @Override
    public Object decode(Response response, Type type) throws IOException {
        Integer status = response.status();

        InputStream is = response.body().asInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(is, baos);
        byte[] bytes = baos.toByteArray();

        try {
            //String test1 = new String(bytes, Charset.defaultCharset());
            String className = type.getTypeName().toLowerCase();
            if (baos.size() == 0) return null;

            if (status >= 200 && status <= 299) {
                if (className.matches("^.*list<[^<>]+>$")) {
                    return processListObject(bytes, className);
                } else {
                    return processObject(bytes, className);
                }
            } else if (status == 404) {
                throw new TupleNotFoundException("Not found exception for request: " +
                        response.request().url().replaceAll("https?://[^/]*/", "/"));
            } else if (status >= 400 && status <= 499) {
                throw new RequestException("Client-Side Exception (" + status + "): " + response.reason());
            } else if (status >= 500) {
                throw new MicroserviceUnavailableException("Unable to process this request at the moment: " +
                        response.request().url().replaceAll("https?://[^/]*/", "/") +
                        " | reason: " + response.reason());
            } else {
                throw new UnsupportedHttpStatusException("Internal Service produced an unsupported HttpStatus: " +
                        status);
            }

        } catch (UnsupportedDomainReturnType | TupleNotFoundException | RequestException |
                MicroserviceUnavailableException | UnsupportedHttpStatusException e) {
            throw e;
        } catch (IOException e) {
            throw new IOException("Response was not processable: " + e.getMessage() + " | cause: " + e.getCause());
        } catch (Throwable e) {
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage() + " | cause: " + e.getCause());
        }
    }

    public Object processListObject(byte[] bytes, String className) throws IOException {
        if (className.matches("^.*<com[.]trilogyed[.]retailapiservice[.]domain[.]customer>$")) {
            return mapper.readValue(bytes, new TypeReference<List<Customer>>(){});
        } else if (className.matches("^.*<com[.]trilogyed[.]retailapiservice[.]domain[.]customerviewmodel>$")) {
            return mapper.readValue(bytes, new TypeReference<List<CustomerViewModel>>(){});
        } else if (className.matches("^.*<com[.]trilogyed[.]retailapiservice[.]domain[.]inventory>$")) {
            return mapper.readValue(bytes, new TypeReference<List<Inventory>>(){});
        } else if (className.matches("^.*<com[.]trilogyed[.]retailapiservice[.]domain[.]inventoryviewmodel>$")) {
            return mapper.readValue(bytes, new TypeReference<List<InventoryViewModel>>(){});
        } else if (className.matches("^.*<com[.]trilogyed[.]retailapiservice[.]domain[.]invoiceitem>$")) {
            return mapper.readValue(bytes, new TypeReference<List<InvoiceItem>>(){});
        } else if (className.matches("^.*<com[.]trilogyed[.]retailapiservice[.]domain[.]invoiceitemviewmodel>$")) {
            return mapper.readValue(bytes, new TypeReference<List<InvoiceItemViewModel>>(){});
        } else if (className.matches("^.*<com[.]trilogyed[.]retailapiservice[.]domain[.]invoiceviewmodel>$")) {
            return mapper.readValue(bytes, new TypeReference<List<InvoiceViewModel>>(){});
        } else if (className.matches("^.*<com[.]trilogyed[.]retailapiservice[.]domain[.]levelup>$")) {
            return mapper.readValue(bytes, new TypeReference<List<LevelUp>>(){});
        } else if (className.matches("^.*<com[.]trilogyed[.]retailapiservice[.]domain[.]orderviewmodel>$")) {
            return mapper.readValue(bytes, new TypeReference<List<OrderViewModel>>(){});
        } else if (className.matches("^.*<com[.]trilogyed[.]retailapiservice[.]domain[.]product>$")) {
            return mapper.readValue(bytes, new TypeReference<List<Product>>(){});
        } else if (className.matches("^.*<com[.]trilogyed[.]retailapiservice[.]domain[.]productviewmodel>$")) {
            return mapper.readValue(bytes, new TypeReference<List<ProductViewModel>>(){});
        } else {
            throw new UnsupportedDomainReturnType(className + " return type is not supported.");
        }
    }

    public Object processObject(byte[] bytes, String className) throws IOException {
        if (className.matches("^com[.]trilogyed[.]retailapiservice[.]domain[.]customer$")) {
            return mapper.readValue(bytes, new TypeReference<Customer>(){});
        } else if (className.matches("^com[.]trilogyed[.]retailapiservice[.]domain[.]customerviewmodel$")) {
            return mapper.readValue(bytes, new TypeReference<CustomerViewModel>(){});
        } else if (className.matches("^com[.]trilogyed[.]retailapiservice[.]domain[.]inventory$")) {
            return mapper.readValue(bytes, new TypeReference<Inventory>(){});
        } else if (className.matches("^com[.]trilogyed[.]retailapiservice[.]domain[.]inventoryviewmodel$")) {
            return mapper.readValue(bytes, new TypeReference<InventoryViewModel>(){});
        } else if (className.matches("^com[.]trilogyed[.]retailapiservice[.]domain[.]invoiceitem$")) {
            return mapper.readValue(bytes, new TypeReference<InvoiceItem>(){});
        } else if (className.matches("^com[.]trilogyed[.]retailapiservice[.]domain[.]invoiceitemviewmodel$")) {
            return mapper.readValue(bytes, new TypeReference<InvoiceItemViewModel>(){});
        } else if (className.matches("^com[.]trilogyed[.]retailapiservice[.]domain[.]invoiceviewmodel$")) {
            return mapper.readValue(bytes, new TypeReference<InvoiceViewModel>(){});
        } else if (className.matches("^com[.]trilogyed[.]retailapiservice[.]domain[.]levelup$")) {
            return mapper.readValue(bytes, new TypeReference<LevelUp>(){});
        } else if (className.matches("^com[.]trilogyed[.]retailapiservice[.]domain[.]orderviewmodel$")) {
            return mapper.readValue(bytes, new TypeReference<OrderViewModel>(){});
        } else if (className.matches("^com[.]trilogyed[.]retailapiservice[.]domain[.]product$")) {
            return mapper.readValue(bytes, new TypeReference<Product>(){});
        } else if (className.matches("^com[.]trilogyed[.]retailapiservice[.]domain[.]productviewmodel$")) {
            return mapper.readValue(bytes, new TypeReference<ProductViewModel>(){});
        } else {
            throw new UnsupportedDomainReturnType(className + " return type is not supported.");
        }
    }
}
