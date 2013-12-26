package com.gdiama.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("test")
public class SampleResource {

    @GET
    public Response test() {
        return Response.ok("Hello World").build();
    }

    @GET
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public MessageRepresentation json() {
        return new MessageRepresentation("Hello World");
    }

    public static class MessageRepresentation {
        @JsonProperty("message")
        private final String message;

        public MessageRepresentation(String message) {
            this.message = message;
        }
    }
}
