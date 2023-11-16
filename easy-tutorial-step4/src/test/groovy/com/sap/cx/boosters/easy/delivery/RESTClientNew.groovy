package com.sap.cx.boosters.easy.delivery

import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import groovyx.net.http.ResponseParseException
import org.codehaus.groovy.runtime.IOGroovyMethods

class RESTClientNew extends RESTClient {

    RESTClientNew() { super() }

    RESTClientNew( Object defaultURI ) throws URISyntaxException {
        super( defaultURI )
    }

    @Override
    protected HttpResponseDecorator defaultSuccessHandler(HttpResponseDecorator resp, Object data) throws ResponseParseException {
        try
        {
            //If response is streaming, buffer it in a byte array:
            if (data instanceof InputStream) {
                def buffer = new ByteArrayOutputStream()
                // we've updated the below line
                IOGroovyMethods.leftShift(buffer, (InputStream) data)
                resp.setData(new ByteArrayInputStream(buffer.toByteArray()))
                return resp
            }
            if (data instanceof Reader) {
                def buffer = new StringWriter()
                // we've updated the below line
                IOGroovyMethods.leftShift(buffer, (Reader) data)
                resp.setData(new StringReader(buffer.toString()))
                return resp
            }
            return super.defaultSuccessHandler(resp, data)
        }
        catch (IOException ex) {
            throw new ResponseParseException(resp, ex)
        }
    }

}