package ordina.resources;

import io.smallrye.common.constraint.NotNull;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import ordina.WordFrequencyAnalyzer;
import ordina.resources.responses.FrequencyResponse;
import org.jboss.resteasy.reactive.RestResponse;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;


@Path("/v1")
public class ExampleResource {

    @Inject
    WordFrequencyAnalyzer analyzer;

    @GET
    @Path("highest-frequency")
    @Produces(APPLICATION_JSON)
    @Consumes(TEXT_PLAIN)
    public FrequencyResponse calculateHighestFrequency(@NotNull String text) {
        return new FrequencyResponse(analyzer.calculateHighestFrequency(text));
    }

    @GET
    @Path("words/{word}/frequency")
    @Produces(APPLICATION_JSON)
    @Consumes(TEXT_PLAIN)
    public Response calculateFrequencyForWord(@PathParam("word") String word, @NotNull String text) {
        return text.isEmpty()
                ? Response.status(400, "No text provided").build()
                : RestResponse.ResponseBuilder
                    .ok(new FrequencyResponse(analyzer.calculateFrequencyForWord(text, word)))
                    .build()
                    .toResponse();
    }

    @GET
    @Path("words/frequency")
    @Produces(APPLICATION_JSON)
    public Response calculateMostFrequentNWords(@QueryParam("limit") int limit, @NotNull String text) {
        return text.isEmpty()
                ? Response.status(400, "No text provided").build()
                : RestResponse.ResponseBuilder
                    .ok(analyzer.calculateMostFrequentNWords(text, limit))
                    .build()
                    .toResponse();
    }
}
