package org.carrot2.elasticsearch;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.elasticsearch.rest.RestRequest.Method;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * REST API tests for {@link ListAlgorithmsAction}.
 */
public class ListAlgorithmsActionRestTests extends AbstractApiTest {
    @SuppressWarnings("unchecked")
    @Test(dataProvider = "postOrGet")
    public void testListAlgorithms(Method method) throws IOException {
        final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpRequestBase request;
            String requestString = restBaseUrl + "/" 
                    + ListAlgorithmsAction.RestListAlgorithmsAction.NAME + "?pretty=true";

            switch (method) {
                case POST:
                    request = new HttpPost(requestString);
                    break;

                case GET:
                    request = new HttpGet(requestString);
                    break;

                default: throw Preconditions.unreachable();
            }

            HttpResponse response = httpClient.execute(request);
            Map<?,?> map = checkHttpResponse(response);

            // Check that we do have some algorithms.
            Assertions.assertThat(map.get("algorithms"))
                .describedAs("A list of algorithms")
                .isInstanceOf(List.class);

            Assertions.assertThat((List<String>) map.get("algorithms"))
                .describedAs("A list of algorithms")
                .contains("stc", "lingo", "kmeans");            
        } finally {
            httpClient.close();
        }
    }
}