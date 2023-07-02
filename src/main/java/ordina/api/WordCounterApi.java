package ordina.api;

import io.smallrye.common.constraint.NotNull;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import ordina.WordFrequencyAnalyzer;
import ordina.models.ApiResponses.FrequencyResponse;
import org.jboss.resteasy.reactive.RestResponse;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("/v1")
public class WordCounterApi {

    @Inject
    WordFrequencyAnalyzer analyzer;

    private static final String NO_TEXT_PROVIDED = "No text provided";

    @GET
    @Path("highest-frequency")
    @Produces(APPLICATION_JSON)
    @Consumes(TEXT_PLAIN)
    public Response calculateHighestFrequency(@NotNull String text) {
        return text.isEmpty()
                ? Response.status(400, NO_TEXT_PROVIDED).build()
                : RestResponse.ResponseBuilder
                    .ok(new FrequencyResponse(analyzer.calculateHighestFrequency(text)))
                    .build()
                    .toResponse();
    }

    @GET
    @Path("words/{word}/frequency")
    @Produces(APPLICATION_JSON)
    @Consumes(TEXT_PLAIN)
    public Response calculateFrequencyForWord(@PathParam("word") String word, @NotNull String text) {
        return text.isEmpty()
                ? Response.status(400, NO_TEXT_PROVIDED).build()
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
                ? Response.status(400, NO_TEXT_PROVIDED).build()
                : RestResponse.ResponseBuilder
                    .ok(analyzer.calculateMostFrequentNWords(text, limit))
                    .build()
                    .toResponse();
    }
}
