package com.gdiama.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("test")
public class SampleResource {

    @GET
    public Response test() {
        return Response.ok("Hello World").build();
    }
}
